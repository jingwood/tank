/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element;

import jingwood.tank.battleground.BattleGround;

import java.awt.*;

public interface Element {
	void draw(Graphics2D g, BattleGround bg);

	Image getImage();

	int getOrderLevel();
}
