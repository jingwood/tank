/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank;

import jingwood.tank.battleground.BattleGround;
import jingwood.tank.battleground.EditBattleGround;
import jingwood.tank.controller.KeyProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Random;

public class WinMain extends JFrame {
	public BattleGround bg;
//	private int stage = 0;

	public static final Random rand = new Random();
	public static final Toolkit tk = Toolkit.getDefaultToolkit();
	public static Network network = new Network();
	public static final String version = "0.1beta";

	public WinMain() {
		super("Tank " + version);
		setSize(640, 480);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.addKeyListener(new KeyProcessor());
		this.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				if (bg != null) {
					bg.resume();
				}
			}

			public void windowLostFocus(WindowEvent e) {
				if (bg != null) {
					bg.pause();
				}
			}
		});

		showGameMenu();
//			showEditPanel();
//		showBattleGround();
	}

	public void showGameMenu() {
		this.getContentPane().removeAll();
		this.getContentPane().add(new MainMenu(this));
		this.validate();
		this.setFocusable(true);
		this.repaint();
	}

	public void showEditPanel() {
		this.getContentPane().removeAll();
		this.getContentPane().add(new EditBattleGround(this));
		this.validate();
		this.repaint();
	}

	public void showBattleGround() {
		this.getContentPane().removeAll();
		this.getContentPane().add(bg = new BattleGround(this));
		this.validate();
		this.repaint();
	}

	public static void main(String[] args) {
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignore) {}
		*/
		new WinMain().setVisible(true);
	}


}