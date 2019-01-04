/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element.obstacle;

import jingwood.tank.battleground.BattleGround;
import jingwood.tank.element.Bullet;
import jingwood.tank.element.tank.Tank;

import java.awt.*;

public class Snow extends Obstacle {
	private Image snow;
	private int code;
	private Point location = new Point();

	private java.util.Random rand = new java.util.Random();

	public Snow() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		this.snow = tk.getImage("images/snow.gif");
		code = 13;
	}

	public Image getImage() {
		return this.snow;
	}

	public int getOrderLevel() {
		return 0;
	}

	public void draw(Graphics2D comp, BattleGround bg) {
		// get location from this and subclass
		final Point location = getLocation();

		// draw image
		comp.drawImage(this.snow, 20 + location.x, 10 + location.y, bg);
	}

	//protected int grid = 4;
	protected int getGridSize() {
		return 1;
	}

	public int getCode() {
		return this.code;
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

	protected boolean moveEffect(Tank tank) {
		int tx = tank.getLocation().x;
		int ty = tank.getLocation().y;
		int x = this.location.x;
		int y = this.location.y;

		if (ty < y + 32 && ty > y - 32 && tx > x - 32 && tx < x + 32) {
			if (tank.getDestination() > 0 && rand.nextInt(50) >= 40) tank.setDestination(1);
		}

		return true;
	}
}