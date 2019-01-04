/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MainMenu extends JPanel implements Runnable {
	Thread runner;
	private String[] caption;
	private int title_y = 500;
	//private Image title;

	private boolean isDisplayNote = false;
	private boolean isDisplayListening = false;

	private int menuIndex = 0;
	private Image[] tank = new Image[3];
	protected String[][] menus = new String[2][3];
	protected int menu = 0;
	private WinMain main;
	private boolean end = false;

	private int status = 1;
	public static final int ST_OPENING = 1;
	public static final int ST_WAITING = 2;
	public static final int ST_SELECTING = 3;
	public static final int ST_LISTENING = 4;
	public static final int ST_CONNECTING = 5;

	public MainMenu(WinMain main) {
		super();
		this.main = main;

		this.caption = new String[]{
			" ###  #                          #####            #",
			"#   # #                            #              #",
			"#     #  ###    ####  #### #  ###  #  ###    ###  #  #",
			"#     # #   #  #     #     # #   # # #   #  #   # # #",
			"#     # #   #   ###   ###  # #     # #   #  #   # ## ",
			"#   # # #   #      #     # # #   # # #   #  #   # # #",
			" ###  #  ###^# ####  ####  #  ###  #  ###^# #   # #  #",
		}; // Classic Tank

		menus[0] = new String[]{
			"SINGLE PLAY",
			"MULTI-PLAYER GAME (WIP)",
			"EDIT BATTLEGROUND"
		};

		menus[1] = new String[]{
			"HOST",
			"JOIN",
			"MAIN MENU"
		};

		for (int i = 0; i < tank.length; i++) {
			tank[i] = WinMain.tk.getImage("images/tank112" + (i + 1) + ".gif");
		}

		this.addMouseListener(new myMouseListener());
		this.addMouseMotionListener(new myMouseMotionListener());

		if (this.runner == null) {
			this.runner = new Thread(this);
			this.runner.start();
		}
	}

	public int getMenu() {
		return menu;
	}

	public String[][] getMenus() {
		return menus;
	}

	public int getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(int menuIndex) {
		this.menuIndex = menuIndex;
	}

	public void paintComponent(Graphics g) {
		Graphics2D comp = (Graphics2D) g;
		comp.setBackground(Color.black);

		comp.setColor(Color.black);
		comp.fillRect(0, 0, this.getWidth(), this.getHeight());

		for (int i = 0; i < this.caption.length; i++) {
			for (int k = 0; k < this.caption[i].length(); k++) {
				int x = (int)(40 + k * 10 - ((float)i * 1.0));
				int y = this.title_y + i * 32;
				switch (this.caption[i].charAt(k)) {
					case '#':
						comp.drawImage(WinMain.tk.getImage("images/wallleft.gif"), x, y, this);
						break;
					case ']':
						comp.drawImage(WinMain.tk.getImage("images/wallright.gif"), x, y, this);
						break;
				}
			}
		}

		comp.setColor(Color.gray);
		comp.setFont(new Font("Courier New", 1, 18));
		comp.drawString("C L A S S I C   T A N K   G A M E", 130, this.title_y + 250);

		if (this.isDisplayNote) {
			comp.setColor(Color.red);
			comp.drawString("PRESS ANY BUTTON TO CONTINUE...", 150, 400);
		}

		if (this.isDisplayListening) {
			comp.setColor(Color.red);
			comp.drawString("WAITING FOR OTHER PLAYER...", 180, 400);
		}

		if (this.status == ST_SELECTING) {
			comp.setColor(Color.gray);
			for (int i = 0; i < menus[this.menu].length; i++) {
				comp.drawString(menus[this.menu][i], 260, 330 + i * 30);
				if (this.menuIndex == i) {
					comp.drawImage(tank[WinMain.rand.nextInt(tank.length)], 200, 308 + i * 30, this);
				}
			}
		}

		comp.setColor(Color.white);
		comp.setFont(new Font("Arial", 0, 9));
		comp.drawString("v" + WinMain.version, 2, 10);

	}

	public void run() {
		while (true) {
			if (this.end) return;
			switch (this.status) {
				case ST_OPENING:
					if (this.title_y > 30) {
						this.title_y -= 3;
					} else {
						this.status = ST_WAITING;
						pause(2000);
					}
					repaint();
					pause(20);
					break;
				case ST_WAITING:
					this.isDisplayNote = !this.isDisplayNote;
					repaint();
					pause(800);
					break;
				case ST_SELECTING:
					repaint();
					pause(300);
					break;
				case ST_LISTENING:
					if (WinMain.network.getStatus() != Network.ST_LISTENING
							&& WinMain.network.getStatus() != Network.ST_ACCEPTED) {
						status = ST_SELECTING;
						break;
					}
					if (WinMain.network.getStatus() == Network.ST_ACCEPTED
							&& WinMain.network.P_VERSAME) {
						main.showBattleGround();
						return;
					}
					this.isDisplayListening = !this.isDisplayListening;
					repaint();
					pause(800);
					break;
				case ST_CONNECTING:
					if (WinMain.network.getStatus() != Network.ST_CONNECTED) {
						status = ST_SELECTING;
						break;
					}
					if (WinMain.network.E_VERDIFF) {
						JOptionPane.showMessageDialog(main,
								"Your version is different with server, update your version firstly please!",
								"Error",
								JOptionPane.INFORMATION_MESSAGE);
						this.status = ST_SELECTING;
					} else if (WinMain.network.P_VERSAME) {
						main.showBattleGround();
						return;
					}
					repaint();
					pause(1000);
					break;
			}
		}
	}

	public void pause(int value) {
		try {
			Thread.sleep(value);
		} catch (InterruptedException ignore) {
		}
	}

	public void actionMenuPressed() {
		switch (menu) {
			case 0:
				switch (menuIndex) {
					case 0:
						main.showBattleGround();
						this.end = true;
						break;
					case 1:
						menu = 1;
						break;
					case 2:
						main.showEditPanel();
						this.end = true;
						break;
				}
				break;
			case 1:
				switch (menuIndex) {
					case 0:
						network_listen();
						break;
					case 1:
						network_connect();
						break;
					case 2:
						menu = 0;
						break;
				}
				break;
		}
	}

	public void network_listen() {
		WinMain.network.listen();
		if (WinMain.network.getStatus() == Network.ST_LISTENING) {
			this.status = ST_LISTENING;
		}
	}

	public void network_connect() {
		String address = JOptionPane.showInputDialog(main,
				"Enter the remote server address [localhost]: \n(example:192.168.0.1)");

		if (address == null) {
			return;
		}

		if (address.length() <= 0) address = "localhost";

		WinMain.network.connect(address);
		if (WinMain.network.getStatus() != Network.ST_CONNECTED) {
			JOptionPane.showMessageDialog(main,
					"Connect server failed.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		status = ST_CONNECTING;
	}

	public void network_stoplisten() {
		int response = JOptionPane.showConfirmDialog(main,
				"The server is running now, are you sure to stop the server?",
				"Are you sure",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			WinMain.network.stoplisten();
			if (WinMain.network.getStatus() != Network.ST_LISTENING) {
				isDisplayListening = false;
				status = ST_SELECTING;
			}
		}
	}

	class myMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			if (status == ST_SELECTING) {
				int x = evt.getPoint().x;
				int y = evt.getPoint().y;
				for (int i = 0; i < menus[menu].length; i++) {
					if (x > 200 && x < 460 && y > 314 + i * 30 && y < 314 + i * 30 + 18) {
						menuIndex = i;
						actionMenuPressed();
						return;
					}
				}
			}
			switch (status) {
				case ST_OPENING:
					title_y = 30;
					status = ST_WAITING;
					break;
				case ST_WAITING:
					isDisplayNote = false;
					status = ST_SELECTING;
					repaint();
					break;
				case ST_LISTENING:
					break;
			}
		}
	}

	class myMouseMotionListener extends MouseMotionAdapter {
		private int lastindex = 0;

		public void mouseMoved(MouseEvent evt) {
			if (status == ST_SELECTING) {
				int x = evt.getPoint().x;
				int y = evt.getPoint().y;
				for (int i = 0; i < menus[menu].length; i++) {
					if (x > 200 && x < 460 && y > 314 + i * 30 && y < 314 + i * 30 + 18) {
						if (lastindex != i) {
							menuIndex = i;
							repaint();
							lastindex = i;
						}
					}
				}
			}
		}
	}

	public int getStatus() {
		return this.status;
	}
}

