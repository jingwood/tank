/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.battleground;

import jingwood.tank.WinMain;
import jingwood.tank.element.obstacle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditBattleGround extends JPanel implements Runnable {
    private Thread runner;

    private int x = 0;
    private int y = 0;
    private boolean isDisplay = true;

    int gridWidth = 13;
    int gridHeight = 13;

    private JFileChooser fc;

    private int current = 1;
    private int[][] obstacles;

    private final WinMain main;
    private boolean end = false;

    public EditBattleGround(WinMain mainper) {
        super();
        this.main = mainper;

        obstacles = new int[this.gridWidth][this.gridHeight];

        this.addMouseListener(new EditBGMouseListener());
        this.addMouseMotionListener(new EditBGMouseMotionListener());

        this.setLayout(null);

        Action action = new AbstractAction("New") {
            public void actionPerformed(ActionEvent evt) {
                obstacles = new int[gridWidth][gridHeight];
                repaint();
            }
        };
        JButton buttonNew = new JButton(action);
        add(buttonNew);

        fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File file) {
                String filename = file.getName();
                return filename.endsWith(".stg") || file.isDirectory();
            }

            public String getDescription() {
                return "Tank Stage File(*.stg)";
            }
        });
        fc.setCurrentDirectory(new File("./stages/"));

        action = new AbstractAction("Open...") {
            public void actionPerformed(ActionEvent evt) {
                // Open the battle ground
                if (fc.showOpenDialog(new JFrame()) != JFileChooser.APPROVE_OPTION) return;
                File file = fc.getSelectedFile();
                try {
                    FileInputStream fin = new FileInputStream(file);
                    for (int n = 0; n < gridHeight; n++) {
                        for (int m = 0; m < gridWidth; m++) {
                            obstacles[m][n] = fin.read();
                        }
                    }
                    fin.close();
                    repaint();
                } catch (IOException ignore) {
                }
            }
        };
        JButton buttonopen = new JButton(action);
        add(buttonopen);

        action = new AbstractAction("Save...") {
            public void actionPerformed(ActionEvent evt) {
                // Save the battle ground
                if (fc.showSaveDialog(new JFrame()) != JFileChooser.APPROVE_OPTION) return;
                File file = fc.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".stg")) {
                    file = new File(file.getPath() + ".stg");
                }
                try {
                    FileOutputStream fout = new FileOutputStream(file);
                    for (int n = 0; n < gridHeight; n++) {
                        for (int m = 0; m < gridWidth; m++) {
                            fout.write(obstacles[m][n]);
                        }
                    }
                    fout.close();
                } catch (IOException ignore) {
                }
            }
        };
        JButton saveButton = new JButton(action);
        add(saveButton);

        action = new AbstractAction("Random") {
            public void actionPerformed(ActionEvent evt) {
                for (int n = 0; n < gridHeight; n++) {
                    for (int m = 0; m < gridWidth; m++) {
                        obstacles[m][n] = WinMain.rand.nextInt(10) > 3 ? 0 : WinMain.rand.nextInt(14);
                    }
                }
                repaint();
            }
        };
        JButton randomButton = new JButton(action);
        add(randomButton);

        add(new JButton(
                new AbstractAction("Main menu") {
                    public void actionPerformed(ActionEvent evt) {
                        main.showGameMenu();
                        end = true;
                    }
                }
        ));

        if (this.runner == null) {
            this.runner = new Thread(this);
            this.runner.start();
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D comp = (Graphics2D) g;

        comp.setColor(new Color(200, 200, 200));
        comp.fillRect(0, 0, this.getWidth(), this.getHeight());

        comp.setColor(Color.black);
        comp.fillRect(20, 10, this.gridWidth * 32, this.gridHeight * 32);

        for (int n = 0; n < this.gridHeight; n++) {
            for (int m = 0; m < this.gridWidth; m++) {
                Image image = getImage(this.obstacles[m][n]);
                if (image != null) {
                    comp.drawImage(image, 20 + m * 32, 10 + n * 32, this);
                }
            }
        }

        // display cursor
        if (this.isDisplay) {
            Image image = getImage(this.current);

            if (image != null) comp.drawImage(image, 20 + this.x, 10 + this.y, this);
            comp.setColor(Color.yellow);
            comp.drawRect(20 + this.x, 10 + this.y, 32, 32);
        }

        int bx = 40 + this.gridWidth * 32;
        int by = 8;
        int bwidth = 100;
        int bheight = 30;

        for (int i = 0; i < this.getComponentCount(); i++) {
            Component component = this.getComponent(i);
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                //button.setFont(new Font("Arial",0,12));
                button.reshape(bx, by + (by + bheight) * i, bwidth, bheight);
            }
        }
    }

    public void run() {
        try {
            while (true) {
                if (this.end) return;
                this.isDisplay = !this.isDisplay;
                repaint();
                Thread.sleep(450);
            }
        } catch (InterruptedException ignore) {
        }
    }

    public void setMousePoint(int x, int y) {
        this.x = x * 32;
        this.y = y * 32;
        this.isDisplay = true;
        repaint();
    }

    public void nextObstacle() {
        if (++this.current > 14) {
            this.current = 1;
        }
        this.isDisplay = true;
        repaint();
    }

    public int getCurrent() {
        return this.current;
    }

    public int[][] getObstacles() {
        return this.obstacles;
    }

    public Point getMousePoint() {
        return new Point(this.x, this.y);
    }

    public Image getImage(int code) {
        switch (code) {
            case 1:
                return new Wall().getImage();
            case 2:
                return new Wall(Wall.WALL_UP).getImage();
            case 3:
                return new Wall(Wall.WALL_RIGHT).getImage();
            case 4:
                return new Wall(Wall.WALL_DOWN).getImage();
            case 5:
                return new Wall(Wall.WALL_LEFT).getImage();
            case 6:
                return new Steel().getImage();
            case 7:
                return new Steel(Steel.STEEL_UP).getImage();
            case 8:
                return new Steel(Steel.STEEL_RIGHT).getImage();
            case 9:
                return new Steel(Steel.STEEL_DOWN).getImage();
            case 10:
                return new Steel(Steel.STEEL_LEFT).getImage();
            case 11:
                return new Grass().getImage();
            case 12:
                return new Water().getImage();
            case 13:
                return new Snow().getImage();
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.getContentPane().add(new EditBattleGround(new WinMain()));
        frame.setVisible(true);
    }
}