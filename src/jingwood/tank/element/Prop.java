/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element;

import jingwood.tank.WinMain;
import jingwood.tank.element.tank.Tank;
import jingwood.tank.battleground.BattleGround;

import java.awt.*;

public class Prop extends Thread implements Element {
	private int status = 0;
	private static final int ST_NORMAL = 0;
	private static final int ST_DISABLE = 1;

	private Image prop;
	private Point location = new Point();

	private int id;
	public static final int PROP_STAR = 1;
	public static final int PROP_HANDGUN = 2;
	public static final int PROP_LANDMINE = 3;
	public static final int PROP_TIMING = 4;
	public static final int PROP_SHOVEL = 5;
	public static final int PROP_BOAT = 6;
	public static final int PROP_ANIMA = 7;

	public Prop() {
		this(WinMain.rand.nextInt(7) + 1);
	}

	public Prop(final int id) {
		String image = null;
		switch (id) {
			case PROP_STAR:
			case PROP_HANDGUN:
			case PROP_LANDMINE:
			case PROP_TIMING:
			case PROP_SHOVEL:
			case PROP_BOAT:
			case PROP_ANIMA:
				image = "respentstar";
				break;
		}
		this.id = id;
		prop = WinMain.tk.getImage("images/" + image + ".gif");
		this.location.x = (WinMain.rand.nextInt(12) + 1) * 32 - 16;
		this.location.y = (WinMain.rand.nextInt(12) + 1) * 32 - 16;
		start();
		status = ST_NORMAL;
	}

	public void run() {
		pause(15000);
		status = ST_DISABLE;
	}

	public Point getLocation() {
		return this.location;
	}

	public Image getImage() {
		return this.prop;
	}

	public int getOrderLevel() {
		return 5;
	}

	public void draw(Graphics2D comp, BattleGround bg) {
		comp.drawImage(getImage(), 20 + this.location.x, 10 + this.location.y, bg);
	}

	public void checkSupport(Tank tank) {
		int tx = tank.getLocation().x;
		int ty = tank.getLocation().y;
		int x = this.location.x;
		int y = this.location.y;

		switch (tank.getDirection()) {
			case Tank.UP:
				if (ty <= y + 32 && ty > y - 32 && tx > x - 32 && tx < x + 32) supportPorcessor(tank);
				break;
			case Tank.DOWN:
				if (ty >= y - 32 && ty < y + 32 && tx > x - 32 && tx < x + 32) supportPorcessor(tank);
				break;
			case Tank.LEFT:
				if (tx <= x + 32 && tx > x - 32 && ty > y - 32 && ty < y + 32) supportPorcessor(tank);
				break;
			case Tank.RIGHT:
				if (tx >= x - 32 && tx < x + 32 && ty > y - 32 && ty < y + 32) supportPorcessor(tank);
				break;
		}
		return;
	}

	public void supportPorcessor(Tank tank) {
		boolean support = false;
		switch (id) {
			case PROP_STAR:
				tank.setLevel(tank.getLevel() + 1);
				support = true;
				break;
			case PROP_HANDGUN:
				tank.setLevel(Tank.LV_PERFECT);
				support = true;
				break;
			case PROP_LANDMINE:
				support = true;
				break;
			case PROP_TIMING:
				support = true;
				break;
			case PROP_SHOVEL:
				support = true;
				break;
			case PROP_BOAT:
				support = true;
				break;
			case PROP_ANIMA:
				tank.addLife(1);
				break;
		}
	}

	public void pause(int value) {
		try {
			Thread.sleep(value);
		} catch (InterruptedException ignore) {
		}
	}
}