/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.battleground;

import jingwood.tank.Statistic;
import jingwood.tank.WinMain;
import jingwood.tank.controller.ControlManager;
import jingwood.tank.sound.SoundManager;
import jingwood.tank.element.Element;
import jingwood.tank.element.tank.Tank;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Vector;

public class BattleGround extends JPanel implements Runnable {
	private Thread runner;

	public int gridwidth = 13;
	public int gridheight = 13;

	private int status = 1;

	public static final int ST_OPENING = 1;
	public static final int ST_NORMAL = 2;
	public static final int ST_LOSING = 3;
	public static final int ST_WINNING = 4;
	public static final int ST_STATISTICING = 5;
	public static final int ST_CLOSING = 6;

	private boolean isDemo = false;
	private int open_i = 0;
	private int score_h = 0;
	private boolean isDisplayStage = false;
	public Statistic stat;
	private int currentStage = 0;  // zero is stage 1
	private Vector ob = new Vector();
	public Tank[] tank = new Tank[2];
	private Color BorderColor = new Color(200, 200, 200);

	private int tankTargetCount;
	private int ttc_speed = 0;
	private Tank[] tankTarget;
	private Image tanksign = WinMain.tk.getImage("images/tanksign.gif");
	public WinMain main;
	private int wintime = 0;
	public boolean end = false;

	protected boolean isProtected = false;
	private int protectedtime = 0;
	private int[] protectedbackup;

	public boolean isPaused = false;

	public BattleGround(WinMain main) {
		super();
		tank[0] = new Tank(Tank.PRIMARY_TANK, 13 / 2 - 2, 13 - 1, Tank.UP, this);
		tank[0].setLevel(Tank.LV_SINGLE);
		this.main = main;

		/**
		 * Bind the control manager to this panel
		 */
		ControlManager.getInstance().attachEventSource(this);

		nextStage();

		if (this.runner == null) {
			this.runner = new Thread(this);
			this.runner.start();
		}
	}

	public void pause() {
		isPaused = true;
	}

	public void resume() {
		isPaused = false;
	}

	private void nextStage() {
		synchronized (ob) {
			this.currentStage++;
			if (this.currentStage > 10) currentStage = 1;
			//ob = new Vector();
			this.stat = new Statistic(tank, this);
			this.ob = STGReader.getObstacles(new java.io.File("stages/" + ((currentStage) < 10 ? "0" : "") + currentStage + ".stg"), this);
			tank[0].setStatus(Tank.ST_NAISSANCING);
			tank[0].setObstacles(this.ob);
			this.ob.add(tank[0]);
			// TODO: ControlManager
			this.tankTargetCount = 4;
			tankTarget = new Tank[20];
			for (int i = 0; i < 20; i++) {
				tankTarget[i] = new Tank(Tank.TARGET_NORMAL_TANK, 6 * (i % 3), 0, Tank.DOWN, this);
				tankTarget[i].setObstacles(this.ob);
			}
			this.wintime = 0;
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D comp = (Graphics2D) g;

		comp.setColor(this.BorderColor);
		comp.fillRect(0, 0, this.getWidth(), this.getHeight());

		comp.setColor(Color.black);
		comp.fillRect(20, 10, this.gridwidth * 32, this.gridheight * 32);

		this.drawObstacles(comp);

		switch (this.status) {
			case ST_OPENING:
				comp.setColor(this.BorderColor);
				comp.fillRect(20, 10, this.gridwidth * 32, this.gridheight * 32 / 2 - this.open_i);
				comp.fillRect(20, 10 + this.gridheight * 32 / 2 + this.open_i,
						this.gridwidth * 32, this.gridheight * 32 / 2 - this.open_i);
				break;
			case ST_WINNING:
			case ST_NORMAL:
				for (int i = 0; i < 20 - this.tankTargetCount; i++) {
					comp.drawImage(this.tanksign, (this.gridwidth * 32 + 30) + 14 * (i % 2), 10 + 14 * (i / 2), this);
				}
				String[] msg = {
					"P1", tank[0].getLifeString(), "",
					"P2", tank[1] != null ? tank[1].getLifeString() : "00",
				};
				comp.setColor(Color.black);
				comp.setFont(new Font("Arial", 1, 14));
				for (int i = 0; i < msg.length; i++) {
					comp.drawString(msg[i], this.gridwidth * 32 + 30, 200 + i * 20);
				}
				comp.setColor(Color.white);
				comp.setFont(new Font("Arial", 0, 11));
				for (int i = 0; i < tank.length; i++) {
					if (tank[i] != null) comp.drawString("P" + (i + 1) + " " + tank[i].getScore(), 30 + i * 400, 20);
				}
				break;
			case ST_LOSING:
				int x = this.gridheight * 32 / 2;
				int y = this.getHeight() - this.score_h;

				comp.setColor(new Color(200, 0, 0));
				for (int i = 0; i < 2; i++) {
					comp.drawRect(x - 20 + i, y + i, 80 - i * 2, 40 - i * 2);
				}
				comp.setFont(new Font("Arial", 1, 20));
				comp.drawString("G A M E", x - 16, y + 18);
				comp.drawString("O V E R", x - 16, y + 36);
				break;
			case ST_STATISTICING:
				this.stat.draw(comp, this);
				break;
			case ST_CLOSING:
				comp.setColor(this.BorderColor);
				comp.fillRect(20, 10, this.gridwidth * 32, this.open_i);
				comp.fillRect(20, 10 + this.gridheight * 32 - this.open_i,
						this.gridwidth * 32, this.open_i);
				break;
		}

	}

	public void run() {
		while (true) {
			if (this.end) {
				runner.destroy();
				return;
			}
			if (isPaused) {
				pause(500);
				continue;
			}
			protectedprocessor();
			switch (this.status) {
				case ST_OPENING:
					for (this.open_i = 0; this.open_i < this.gridheight * 32 / 2; this.open_i += 10) {
						repaint();
						pause(10);
					}
					this.status = ST_NORMAL;
					SoundManager.getInstance().playSceneSound(SoundManager.SS_GAMESTART);
					break;
				case ST_NORMAL:
					if (this.ttc_speed++ > 150) {
						if (this.tankTargetCount < 20) this.tankTargetCount++;
						this.ttc_speed = 0;
					}
					boolean findactived = false;
					for (int i = 0; i < this.tankTargetCount; i++) {
						if (this.tankTarget[i] != null) {
							if (this.tankTarget[i].getStatus() == Tank.ST_WAITING) {
								if (WinMain.rand.nextInt(4) == 1) this.tankTarget[i].setRich();
								this.tankTarget[i].setStatus(Tank.ST_NAISSANCING);
								this.ob.add(tankTarget[i]);
							}
							if (this.tankTarget[i].getStatus() != Tank.ST_DISABLE) {
								findactived = true;
							}
						}
					}
					if (!(findactived) && this.tankTargetCount >= 20) {
						this.status = ST_WINNING;
					}

					/**
					 * The all life for all controllable tank
					 */
					int lifecount = 0;
					boolean findisable = false;
					for (int i = 0; i < tank.length; i++) {
						if (tank[i] != null) {
							lifecount += tank[i].getLife();
							if (tank[i].getStatus() != Tank.ST_DISABLE) {
								findisable = true;
							}
						}
					}
					if (lifecount <= 0 && !findisable) {
						this.status = ST_LOSING;
						break;
					}
					repaint();
					pause(20);
					break;
				case ST_LOSING:
					for (this.score_h = 0; this.score_h < this.gridheight * 32 / 2 + 40; this.score_h++) {
						repaint();
						pause(30);
					}
					this.status = ST_STATISTICING;
					break;
				case ST_WINNING:
					if (this.wintime++ > 200) {
						this.stat.setWin();
						this.status = ST_STATISTICING;
					}
					repaint();
					pause(20);
					break;
				case ST_STATISTICING:
					this.stat.stating();
					repaint();
					pause(100);
					break;
				case ST_CLOSING:
					for (this.open_i = 0; this.open_i < this.gridheight * 16; this.open_i += 10) {
						repaint();
						pause(10);
					}
					nextStage();
					this.status = ST_OPENING;
					break;
			}
		}
	}

	private void drawObstacles(Graphics2D comp) {
		synchronized (this.ob) {
			for (int order = 0; order <= 5; order++) {
				for (final Iterator i = ob.iterator(); i.hasNext();) {
					final Object obj = i.next();
					if (obj instanceof Element) {
						Element element = ((Element) obj);
						if (element.getOrderLevel() == order) {
							element.draw(comp, this);
						}
					}
				}
			}
		}

/*
			for (int i=0;i<this.tank.length;i++) {
				if (tank[i]!=null) {
					Bullet[] shots = tank[i].getShots();
					for (int k=0;k<shots.length;k++) {
						if (shots[k]!=null) {
							shots[k].draw(comp,this);
						}
					}
					tank[i].draw(comp,this);
				}
			}

			for (int i=0;i<this.tankTarget.length;i++) {
				if (tankTarget[i]!=null) {
					Bullet[] shots = tankTarget[i].getShots();
					for (int k=0;k<shots.length;k++) {
						if (shots[k]!=null) shots[k].draw(comp,this);
					}
					tankTarget[i].draw(comp,this);
				}
			}

			for(Iterator i=ob.iterator();i.hasNext();) {
				Object obnext = i.next();
				if ( obnext instanceof Element) {
					Element element = (Element)obnext;
					element.draw(comp,this);
				}
			}
*/
	}

	private void pause(int value) {
		try {
			Thread.sleep(value);
		} catch (InterruptedException ignore) {
		}
	}

	public void lose() {
		this.status = ST_LOSING;
	}

	public int getStageNumber() {
		return this.currentStage;
	}

	public void close() {
		this.stat = null;
		this.status = ST_CLOSING;
	}

	public int getStatus() {
		return this.status;
	}

	public void setProtected() {
		isProtected = true;
	}

	private void protectedprocessor() {
		if (status == ST_STATISTICING) return;
	}

	public void update(Graphics g) {
		//repaint();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("BattleGround Test");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(640, 480);
		frame.getContentPane().add(new BattleGround(new WinMain()));
		frame.show();
	}
}