/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element.obstacle;

import jingwood.tank.WinMain;
import jingwood.tank.element.Bullet;
import jingwood.tank.element.HitableElement;
import jingwood.tank.element.tank.Tank;

import java.awt.*;

public class Wall extends Obstacle implements HitableElement {
	private Image wall;
	private int code;

	public static final String WALL_UP = "up";
	public static final String WALL_RIGHT = "right";
	public static final String WALL_DOWN = "down";
	public static final String WALL_LEFT = "left";

	public Wall() {
		this.wall = WinMain.tk.getImage("images/wall.gif");
		code = 1;
	}

	public Wall(String part) {
		this.wall = WinMain.tk.getImage("images/wall" + part + ".gif");

		if (part == WALL_UP) {
			this.down_destruction = 2;
			this.code = 2;
		} else if (part == WALL_RIGHT) {
			this.left_destruction = 2;
			this.code = 3;
		} else if (part == WALL_DOWN) {
			this.up_destruction = 2;
			this.code = 4;
		} else if (part == WALL_LEFT) {
			this.right_destruction = 2;
			this.code = 5;
		}
	}

	public Image getImage() {
		return this.wall;
	}

	public int getOrderLevel() {
		return 0;
	}

	protected int getGridSize() {
		return 4;
	}

	public int getCode() {
		return this.code;
	}

	protected boolean attackEffectFromUp(Bullet shot) {
		if (shot.getTank().getLevel() == Tank.LV_PERFECT) {
			this.addDestructionDown(2);
		} else {
			this.addDestructionDown(1);
		}
		return true;
	}

	protected boolean attackEffectFromDown(Bullet shot) {
		if (shot.getTank().getLevel() == Tank.LV_PERFECT) {
			this.addDestructionUp(2);
		} else {
			this.addDestructionUp(1);
		}
		return true;
	}

	protected boolean attackEffectFromLeft(Bullet shot) {
		if (shot.getTank().getLevel() == Tank.LV_PERFECT) {
			this.addDestructionRight(2);
		} else {
			this.addDestructionRight(1);
		}
		return true;
	}

	protected boolean attackEffectFromRight(Bullet shot) {
		if (shot.getTank().getLevel() == Tank.LV_PERFECT) {
			this.addDestructionLeft(2);
		} else {
			this.addDestructionLeft(1);
		}
		return true;
	}

	protected boolean moveEffect(Tank tank) {
		return false;
	}
}