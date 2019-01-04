/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.battleground;

import java.awt.*;
import java.awt.event.*;

public class EditBGMouseMotionListener extends MouseMotionAdapter {
	private int bx = 0;
	private int by = 0;

	public void mouseMoved(MouseEvent evt) {
		EditBattleGround ebg = (EditBattleGround) evt.getSource();
		Point p = evt.getPoint();
		int x = p.x;
		int y = p.y;
		if (x > 20 && x - 16 < ebg.gridWidth * 32 &&
				y > 10 && y - 16 < ebg.gridHeight * 32) {
			x = (x - 16) / 32;
			y = (y - 16) / 32;
			if (this.bx == x && this.by == y) return;
			this.bx = x;
			this.by = y;
			ebg.setMousePoint(x, y);
		}
	}
}