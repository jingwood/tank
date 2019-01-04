/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element.obstacle;

import jingwood.tank.WinMain;
import jingwood.tank.element.Bullet;
import jingwood.tank.element.tank.Tank;

import java.awt.*;

public class Grass extends Obstacle {
	private Image grass;
	private int code;

	public Grass() {
		this.grass = WinMain.tk.getImage("images/grass.gif");
		code = 11;
	}

	protected int getGridSize() {
		return 2;
	}

	public Image getImage() {
		return this.grass;
	}

	public int getOrderLevel() {
		return 2;
	}

	public boolean checkMoveAble(Tank tank) {
		return true;
	}

	protected boolean attackEffectFromUp(Bullet shot) {
		if (shot.getTank().getWeedable()) {
			this.addDestructionDown(1);
		}
		return false;
	}

	protected boolean attackEffectFromDown(Bullet shot) {
		if (shot.getTank().getWeedable()) {
			this.addDestructionUp(1);
		}
		return false;
	}

	protected boolean attackEffectFromLeft(Bullet shot) {
		if (shot.getTank().getWeedable()) {
			this.addDestructionRight(1);
		}
		return false;
	}

	protected boolean attackEffectFromRight(Bullet shot) {
		if (shot.getTank().getWeedable()) {
			this.addDestructionLeft(1);
		}
		return false;
	}

	protected boolean moveEffect(Tank tank) {
		return true;
	}

	public int getCode() {
		return this.code;
	}
}