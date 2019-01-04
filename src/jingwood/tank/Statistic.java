/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank;

import jingwood.tank.battleground.BattleGround;
import jingwood.tank.element.tank.Tank;

import java.awt.*;

public class Statistic extends Thread {
	private int[][] stat = new int[2][4];
	private Tank[] tank;
	private boolean stating = false;
	private boolean stated = false;
	private int stepid;
	private int step;
	private boolean total = false;
	private boolean bonus = false;
	private int[] count = new int[2];
	private BattleGround bg;
	private boolean win = false;

	public Statistic(Tank[] tank, BattleGround bg) {
		super();
		start();
		this.tank = tank;
		this.bg = bg;
	}

	public void addStat(int tankId, int tankTargetID) {
		this.stat[tankId - 1][tankTargetID - 5]++;
	}

	public int getStat(int tankId, int tankTargetID) {
		return this.stat[tankId - 1][tankTargetID - 5];
	}

	public void draw(Graphics2D comp, BattleGround bg) {
		comp.setColor(Color.black);
		comp.fillRect(20, 10, 13 * 32, 13 * 32);

		comp.setFont(new Font("Arial", 1, 18));
		comp.setColor(Color.white);

		comp.drawString("STAGE " + bg.getStageNumber(), 190, 40);
		comp.drawString("PRIMARY                  SECONDARY", 60, 80);
		comp.drawString("TANK TOTAL               TANK TOTAL", 60, 100);

		comp.drawLine(50, 110, 400, 110);
		comp.drawImage(WinMain.tk.getImage("images/tank5111.gif"), 210, 120, bg);
		comp.drawLine(100, 160, 350, 160);
		comp.drawImage(WinMain.tk.getImage("images/tank5111.gif"), 210, 170, bg);
		comp.drawLine(100, 210, 350, 210);
		comp.drawImage(WinMain.tk.getImage("images/tank5111.gif"), 210, 220, bg);
		comp.drawLine(100, 260, 350, 260);
		comp.drawImage(WinMain.tk.getImage("images/tank5111.gif"), 210, 270, bg);
		comp.drawLine(100, 310, 350, 310);
		comp.drawString("TOTAL", 200, 340);

		if (this.stating) {

			for (int i = 0; i <= stepid && i < 4; i++) {
				if (i == stepid) {
					comp.drawString(Integer.toString(stat[0][i] - step > 0 ? step : stat[0][i]), 150, 140 + i * 50);
					comp.drawString(Integer.toString(stat[1][i] - step > 0 ? step : stat[1][i]), 285, 140 + i * 50);
				} else {
					comp.drawString(Integer.toString(this.stat[0][i]), 150, 140 + i * 50);
					comp.drawString(Integer.toString(this.stat[1][i]), 285, 140 + i * 50);
				}
			}

			if (this.total) {
				for (int k = 0; k < count.length; k++) {
					count[k] = 0;
					for (int i = 0; i < 4; i++) {
						count[k] += stat[k][i];
					}
					comp.drawString(Integer.toString(count[k]), 150 + k * 135, 340);
				}
			}

			if (this.bonus && count.length > 1) {
				int i = count[0] >= count[1] ? 0 : 1;
				comp.setColor(Color.red);
				comp.drawString("ACTIVELY", 60 + i * 245, 380);
				comp.drawString("PERFECT", 60 + i * 245, 400);
			}
		}
	}

	public void run() {
		while (true) {
			if (this.stating) {
				for (stepid = 0; stepid < 4; stepid++) {
					int max = this.stat[0][stepid] >= this.stat[1][stepid] ?
							this.stat[0][stepid] : this.stat[1][stepid];
					for (step = 1; step < max; step++) {
						pause(200);
					}
					pause(800);
				}
				this.total = true;
				pause(1500);
				if (this.tank[1] != null) {
					this.bonus = true;
					tank[count[0] >= count[1] ? 0 : 1].addScore(1000);
					pause(2000);
				}
				if (this.win) {
					this.bg.close();
					for (Tank tank1 : tank) {
						if (tank1 != null) tank1.addLife(1);
					}
				} else {
					bg.end = true;
					bg.main.showGameMenu();
				}
				return;
			} else {
				pause(1000);
			}
		}
	}

	private void pause(int value) {
		try {
			sleep(value);
		} catch (InterruptedException ignore) {
		}
	}

	public void stating() {
		this.stating = true;
	}

	public boolean getStated() {
		return this.stated;
	}

	public void setWin() {
		this.win = true;
	}
}
