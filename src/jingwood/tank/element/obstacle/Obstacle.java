/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element.obstacle;

import jingwood.tank.battleground.BattleGround;
import jingwood.tank.element.HitableElement;
import jingwood.tank.element.Bullet;
import jingwood.tank.element.tank.Tank;

import java.awt.*;

public abstract class Obstacle implements Obstructive, HitableElement {
	protected int up_destruction = 0;
	protected int right_destruction = 0;
	protected int down_destruction = 0;
	protected int left_destruction = 0;

	protected Point location = new Point();

	public void setLocation(int x, int y) {
		this.location.x = x * 32;
		this.location.y = y * 32;
	}

	public Point getLocation() {
		return this.location;
	}

	//protected int grid = 4;
	protected abstract int getGridSize();

	public abstract int getCode();

	public void addDestructionUp(int value) {
		if (this.up_destruction < getGridSize()) this.up_destruction += value;
	}

	public void addDestructionRight(int value) {
		if (this.right_destruction < getGridSize()) this.right_destruction += value;
	}

	public void addDestructionDown(int value) {
		if (this.down_destruction < getGridSize()) this.down_destruction += value;
	}

	public void addDestructionLeft(int value) {
		if (this.left_destruction < getGridSize()) this.left_destruction += value;
	}

	public boolean checkMoveAble(Tank tank) {
		if (this.up_destruction + this.down_destruction >= getGridSize()
				|| this.left_destruction + this.right_destruction >= getGridSize())
			return true;

		final int tx = tank.getLocation().x;
		final int ty = tank.getLocation().y;
		final int x = this.location.x;
		final int y = this.location.y;

		final int gridPixel = 32 / getGridSize();
		switch (tank.getDirection()) {
			case Tank.UP:
				if (ty <= y + 32 - this.down_destruction * gridPixel
						&& ty > y - 32 + this.up_destruction * gridPixel
						&& tx > x - 32 + this.left_destruction * gridPixel
						&& tx < x + 32 - this.right_destruction * gridPixel)
					return moveEffect(tank);
				break;
			case Tank.DOWN:
				if (ty >= y - 32 + this.up_destruction * gridPixel
						&& ty < y + 32 - this.down_destruction * gridPixel
						&& tx > x - 32 + this.left_destruction * gridPixel
						&& tx < x + 32 - this.right_destruction * gridPixel)
					return moveEffect(tank);
				break;
			case Tank.LEFT:
				if (tx <= x + 32 - this.right_destruction * gridPixel
						&& tx > x - 32 + this.left_destruction * gridPixel
						&& ty > y - 32 + this.up_destruction * gridPixel
						&& ty < y + 32 - this.down_destruction * gridPixel)
					return moveEffect(tank);
				break;
			case Tank.RIGHT:
				if (tx >= x - 32 + this.left_destruction * gridPixel
						&& tx < x + 32 - this.right_destruction * gridPixel
						&& ty > y - 32 + this.up_destruction * gridPixel
						&& ty < y + 32 - this.down_destruction * gridPixel)
					return moveEffect(tank);
				break;
		}
		return true;
	}

	protected abstract boolean attackEffectFromUp(Bullet shot);

	protected abstract boolean attackEffectFromDown(Bullet shot);

	protected abstract boolean attackEffectFromLeft(Bullet shot);

	protected abstract boolean attackEffectFromRight(Bullet shot);

	protected abstract boolean moveEffect(Tank tank);

	public boolean checkHitAble(Bullet shot) {
		final int gridSize = getGridSize();

		if (this.up_destruction + this.down_destruction >= gridSize
				|| this.left_destruction + this.right_destruction >= gridSize)
			return false;

		int tx = shot.getLocation().x;
		int ty = shot.getLocation().y;
		int x = this.location.x;
		int y = this.location.y;

		switch (shot.getDirection()) {
			case Bullet.DT_UP:
				if (ty <= y + 24 - this.down_destruction * 8 && ty > y - 24 + this.up_destruction * 8
						&& tx > x - 16 + this.left_destruction * 8 && tx < x + 32 - this.right_destruction * 8) {
					return attackEffectFromUp(shot);
				}
				break;
			case Bullet.DT_DOWN:
				if (ty >= y - 8 + this.up_destruction * 8 && ty < y + 24 - this.down_destruction * 8
						&& tx > x - 16 + this.left_destruction * 8 && tx < x + 32 - this.right_destruction * 8) {
					return attackEffectFromDown(shot);
				}
				break;
			case Bullet.DT_LEFT:
				if (tx <= x + 24 - this.right_destruction * 8 && tx > x - 24 + this.left_destruction * 8
						&& ty > y - 16 + this.up_destruction * 8 && ty < y + 32 - this.down_destruction * 8) {
					return attackEffectFromLeft(shot);
				}
				break;
			case Bullet.DT_RIGHT:
				if (tx >= x - 8 + this.left_destruction * 8 && tx < x + 24 - this.right_destruction * 8
						&& ty > y - 16 + this.up_destruction * 8 && ty < y + 32 - this.down_destruction * 8) {
					return attackEffectFromRight(shot);
				}
				break;
		}
		return false;
	}

	public void draw(Graphics2D comp, BattleGround bg) {
		final Image image = getImage();

		if (this.up_destruction + this.down_destruction >= getGridSize()
				|| this.left_destruction + this.right_destruction >= getGridSize())
			return;

		// get location x and y
		final int x = getLocation().x + 20;
		final int y = getLocation().y + 10;

		final int width = image.getWidth(bg);
		final int height = image.getHeight(bg);
		final int widthGridPixel = width / getGridSize();

		// draw image
		comp.drawImage(image, x, y, bg);

		comp.setColor(Color.black);

		// erasure the destruct image from top
		if (this.up_destruction > 0) {
			comp.fillRect(x, y, width, this.up_destruction * widthGridPixel);
		}

		// erasure the destruct image from right
		if (this.right_destruction > 0) {
			for (int i = this.right_destruction; i > 0; i--) {
				comp.fillRect(x + width - i * widthGridPixel, y, widthGridPixel, height);
			}
		}

		// erasure the destruct image from down
		if (this.down_destruction > 0) {
			for (int i = this.down_destruction; i > 0; i--) {
				comp.fillRect(x, y + height - i * widthGridPixel, width, widthGridPixel);
			}
		}

		// erasure the destruct image from left
		if (this.left_destruction > 0) {
			comp.fillRect(x, y, this.left_destruction * widthGridPixel, height);
		}

	}
}
