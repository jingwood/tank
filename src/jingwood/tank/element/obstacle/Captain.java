/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element.obstacle;

import jingwood.tank.battleground.BattleGround;
import jingwood.tank.WinMain;
import jingwood.tank.element.Bullet;
import jingwood.tank.element.Element;
import jingwood.tank.element.tank.Tank;

import java.awt.*;

public class Captain implements Element {
	private Image image;
	private Image broken;
	private Image[] blasts = new Image[3];
	private Point location = new Point();
	private int blast = 0;
	private boolean isblast = false;
	private BattleGround bg;

	public Captain(BattleGround bg) {
		this.bg = bg;
		this.image = WinMain.tk.getImage("images/captain.gif");
		this.broken = WinMain.tk.getImage("images/captain2.gif");
		for (int i = 0; i < blasts.length; i++) {
			blasts[i] = WinMain.tk.getImage("images/bigblast" + (i + 1) + ".gif");
		}
	}

	public void draw(Graphics2D comp, BattleGround bg) {
		if (this.isblast) {
			comp.drawImage(this.blasts[this.blast], 20 + this.location.x, 10 + this.location.y, bg);
			if (this.blast < 2) {
				this.blast++;
			} else {
				this.isblast = false;
			}
		}
		if (this.blast == 0) {
			comp.drawImage(this.image, 20 + this.location.x, 10 + this.location.y, bg);
		} else {
			comp.drawImage(this.broken, 20 + this.location.x, 10 + this.location.y, bg);
		}
	}

	public Image getImage() {
		return image;
	}

	public int getOrderLevel() {
		return 0;
	}

	public Point getLocation() {
		return this.location;
	}

	public void setLocation(int x, int y) {
		this.location.x = x * 32;
		this.location.y = y * 32;
	}

	public boolean checkMoveAble(Tank tank) {
		int tx = tank.getLocation().x;
		int ty = tank.getLocation().y;
		int x = this.location.x;
		int y = this.location.y;

		switch (tank.getDirection()) {
			case Tank.UP:
				if (ty <= y + 32 && ty > y - 32 && tx > x - 32 && tx < x + 32) return false;
				break;
			case Tank.DOWN:
				if (ty >= y - 32 && ty < y + 32 && tx > x - 32 && tx < x + 32) return false;
				break;
			case Tank.LEFT:
				if (tx <= x + 32 && tx > x - 32 && ty > y - 32 && ty < y + 32) return false;
				break;
			case Tank.RIGHT:
				if (tx >= x - 32 && tx < x + 32 && ty > y - 32 && ty < y + 32) return false;
				break;
		}
		return true;
	}

	public boolean checkHitAble(Bullet shot) {
		int tx = shot.getLocation().x;
		int ty = shot.getLocation().y;
		int x = this.location.x;
		int y = this.location.y;

		switch (shot.getDirection()) {
			case Bullet.DT_UP:
				if (ty <= y + 24 && ty > y - 24 && tx > x - 16 && tx < x + 32) {
					this.isblast = true;
					bg.lose();
					return true;
				}
				break;
			case Bullet.DT_DOWN:
				if (ty >= y - 8 && ty < y + 24 && tx > x - 16 && tx < x + 32) {
					this.isblast = true;
					bg.lose();
					return true;
				}
				break;
			case Bullet.DT_LEFT:
				if (tx <= x + 24 && tx > x - 24 && ty > y - 16 && ty < y + 32) {
					this.isblast = true;
					bg.lose();
					return true;
				}
				break;
			case Bullet.DT_RIGHT:
				if (tx >= x - 8 && tx < x + 24 && ty > y - 16 && ty < y + 32) {
					this.isblast = true;
					bg.lose();
					return true;
				}
				break;
		}
		return false;
	}
}