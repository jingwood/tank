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

public class Steel extends Obstacle implements HitableElement {
	private Image steel;
	private int code;

	public static String STEEL_UP = "up";
	public static String STEEL_RIGHT = "right";
	public static String STEEL_DOWN = "down";
	public static String STEEL_LEFT = "left";

	public Steel() {
		this.steel = WinMain.tk.getImage("images/steel.gif");
		code = 6;
	}

	public Steel(String part) {
		this.steel = WinMain.tk.getImage("images/steel" + part + ".gif");

		if (part == STEEL_UP) {
			this.down_destruction = 1;
			this.code = 7;
		}
		if (part == STEEL_RIGHT) {
			this.left_destruction = 1;
			this.code = 8;
		}
		if (part == STEEL_DOWN) {
			this.up_destruction = 1;
			this.code = 9;
		}
		if (part == STEEL_LEFT) {
			this.right_destruction = 1;
			this.code = 10;
		}
	}

	public Image getImage() {
		return this.steel;
	}

	public int getOrderLevel() {
		return 0;
	}

	//protected int grid = 4;
	protected int getGridSize() {
		return 2;
	}

	public int getCode() {
		return this.code;
	}

	protected boolean attackEffectFromUp(Bullet shot) {
		if (shot.getTank().getLevel() == Tank.LV_PERFECT) {
			this.addDestructionDown(1);
		}
		return true;
	}

	protected boolean attackEffectFromDown(Bullet shot) {
		if (shot.getTank().getLevel() == Tank.LV_PERFECT) {
			this.addDestructionUp(1);
		}
		return true;
	}

	protected boolean attackEffectFromLeft(Bullet shot) {
		if (shot.getTank().getLevel() == Tank.LV_PERFECT) {
			this.addDestructionRight(1);
		}
		return true;
	}

	protected boolean attackEffectFromRight(Bullet shot) {
		if (shot.getTank().getLevel() == Tank.LV_PERFECT) {
			this.addDestructionLeft(1);
		}
		return true;
	}

	protected boolean moveEffect(Tank tank) {
		return false;
	}
}