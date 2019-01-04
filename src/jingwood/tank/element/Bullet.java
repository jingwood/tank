/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element;

import jingwood.tank.battleground.BattleGround;
import jingwood.tank.element.obstacle.Captain;
import jingwood.tank.element.obstacle.Grass;
import jingwood.tank.element.obstacle.Steel;
import jingwood.tank.element.obstacle.Wall;
import jingwood.tank.element.tank.Tank;

import java.awt.*;
import java.util.*;

public class Bullet extends Thread implements MoveableElement {
	private Image image;

	private Toolkit tk = Toolkit.getDefaultToolkit();
	private Point location = new Point();

	private int direction;
	public static final int DT_UP = 1;
	public static final int DT_RIGHT = 2;
	public static final int DT_DOWN = 3;
	public static final int DT_LEFT = 4;

	private int status;
	public static final int ST_NORMAL = 1;
	public static final int ST_BLASTING = 2;
	public static final int ST_DISABLE = 3;

	private int blast = 0;
	private Image[] blasts = new Image[3];

	private int delay;
	private Tank tank;
	private Vector ob;
	private boolean isProp = false;

	public boolean isProp() {
		return isProp;
	}

	public void setProp(boolean prop) {
		isProp = prop;
	}

	public Bullet(Tank tank) {
		this.tank = tank;
		this.ob = tank.getObstacles();
		this.direction = tank.getDirection();
		switch (this.direction) {
			case DT_UP:
				this.image = tk.getImage("images/shotup.gif");
				break;
			case DT_RIGHT:
				this.image = tk.getImage("images/shotright.gif");
				break;
			case DT_DOWN:
				this.image = tk.getImage("images/shotdown.gif");
				break;
			case DT_LEFT:
				this.image = tk.getImage("images/shotleft.gif");
				break;
		}
		this.location.x = tank.getLocation().x + 8;
		this.location.y = tank.getLocation().y + 8;
		switch (tank.getLevel()) {
			case Tank.LV_SINGLE:
				delay = 10;
				break;
			case Tank.LV_FAST:
			case Tank.LV_DOUBLE:
			case Tank.LV_PERFECT:
				delay = 5;
				break;
		}
		for (int i = 0; i < blasts.length; i++) {
			blasts[i] = tk.getImage("images/blast" + (i + 1) + ".gif");
		}
		this.status = ST_NORMAL;
		this.start();
	}

	public void draw(Graphics2D comp, BattleGround bg) {
		switch (this.status) {
			case ST_BLASTING:
				comp.drawImage(this.blasts[this.blast], 20 + this.location.x, 10 + this.location.y, bg);
				break;
			case ST_NORMAL:
				comp.drawImage(this.image, 20 + this.location.x, 10 + this.location.y, bg);
				break;
		}
	}

	public Image getImage() {
		return image;
	}

	public int getOrderLevel() {
		return 2;
	}

	public void run() {
		while (true) {
			if (this.tank.bg.getStatus() != BattleGround.ST_NORMAL &&
					this.tank.bg.getStatus() != BattleGround.ST_WINNING &&
					this.tank.bg.getStatus() != BattleGround.ST_LOSING) {
				status = ST_DISABLE;
				return;
			}

			switch (this.status) {
				case ST_NORMAL:
					this.moving();
					break;
				case ST_BLASTING:
					this.blasting();
					break;
				case ST_DISABLE:
					return;
			}
			pause(delay);
		}
	}

	public void pause(int value) {
		try {
			sleep(value);
		} catch (InterruptedException ignore) {
		}
	}

	public void moving() {
		if (this.status != ST_NORMAL) return;
		synchronized (this.ob) {
			Iterator obs = this.ob.iterator();
			while (obs.hasNext()) {
				Object obn = obs.next();
				if (obn instanceof Wall) {
					Wall wall = (Wall) obn;
					if (wall.checkHitAble(this)) this.status = ST_BLASTING;
				}
				if (obn instanceof Steel) {
					Steel steel = (Steel) obn;
					if (steel.checkHitAble(this)) this.status = ST_BLASTING;
				}
				if (obn instanceof Grass) {
					Grass grass = (Grass) obn;
					if (grass.checkHitAble(this)) this.status = ST_BLASTING;
				}
				if (obn instanceof Tank) {
					Tank tank = (Tank) obn;
					if (this.tank != tank && this.tank.isPlayer() != tank.isPlayer()) {
						if (tank.checkHitAble(this)) this.status = ST_BLASTING;
					}
				}
				if (obn instanceof Captain) {
					Captain captain = (Captain) obn;
					if (captain.checkHitAble(this)) this.status = ST_BLASTING;
				}
			}
		}
		
		if (this.status != ST_NORMAL) return;

		switch (this.direction) {
			case DT_UP:
				if (this.location.y > 1) {
					this.location.y -= 4;
				} else {
					this.location.y -= 8;
					this.blasting();
				}
				break;
			case DT_RIGHT:
				if (this.location.x < 12 * 32 + 16) {
					this.location.x += 4;
				} else {
					this.location.x += 8;
					this.blasting();
				}
				break;
			case DT_DOWN:
				if (this.location.y < 12 * 32 + 16) {
					this.location.y += 4;
				} else {
					this.location.y += 8;
					this.blasting();
				}
				break;
			case DT_LEFT:
				if (this.location.x > 1) {
					this.location.x -= 4;
				} else {
					this.location.x -= 8;
					this.blasting();
				}
				break;
		}
	}

	public void blasting() {
		if (isProp) {
			this.ob.add(new Prop());
		}
		this.status = ST_BLASTING;
		this.location.x -= 8;
		this.location.y -= 8;
		for (int i = 0; i < this.blasts.length; i++) {
			this.blast = i;
			pause(50);
		}
		for (int i = this.blasts.length - 2; i >= 0; i--) {
			this.blast = i;
			pause(50);
		}
		this.status = ST_DISABLE;
		synchronized (ob) {
			ob.removeElement(this);
		}
	}

	public int getStatus() {
		return this.status;
	}

	public Point getLocation() {
		return this.location;
	}

	public int getDirection() {
		return this.direction;
	}

	public Tank getTank() {
		return this.tank;
	}
}