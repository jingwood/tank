/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element.obstacle;

import jingwood.tank.WinMain;
import jingwood.tank.battleground.BattleGround;
import jingwood.tank.element.Bullet;
import jingwood.tank.element.tank.Tank;

import java.awt.*;

public class Water extends Obstacle implements Runnable {
	private Image[] water = new Image[6];
	private int code;

	//private Point location = new Point();
	private int step;

	public Water() {
		for (int i = 0; i < 6; i++) {
			this.water[i] = WinMain.tk.getImage("images/water" + (i + 1) + ".gif");
		}
		this.code = 12;
		this.step = WinMain.rand.nextInt(6);

		thread = new Thread(this);
		thread.start();
	}

	protected int getGridSize() {
		return 1;
	}

	public Image getImage() {
		return this.water[this.step];
	}

	public int getOrderLevel() {
		return 0;
	}

	public void draw(Graphics2D comp, BattleGround bg) {
		final Point location = getLocation();

		comp.drawImage(this.getImage(), 20 + location.x, 10 + location.y, bg);
	}

	public int getCode() {
		return this.code;
	}

	public boolean moveEffect(Tank tank) {
		return false;
	}

	protected boolean attackEffectFromUp(Bullet shot) {
		return false;
	}

	protected boolean attackEffectFromDown(Bullet shot) {
		return false;
	}

	protected boolean attackEffectFromLeft(Bullet shot) {
		return false;
	}

	protected boolean attackEffectFromRight(Bullet shot) {
		return false;
	}

	private Thread thread;

	public void run() {
		while (thread != null) {
			if (++this.step >= 6) {
				this.step = 0;
			}
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}