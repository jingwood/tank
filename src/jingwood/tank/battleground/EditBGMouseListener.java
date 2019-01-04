/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.battleground;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditBGMouseListener extends MouseAdapter {
	public void mousePressed(MouseEvent evt) {
		EditBattleGround ebg = (EditBattleGround) evt.getSource();
		Point point = evt.getPoint();
		if (!(point.x > 20 && point.x - 16 < 32 * ebg.gridWidth
				&& point.y > 10 && point.y - 16 < 32 * ebg.gridHeight))
			return;
		if (evt.getButton() == MouseEvent.BUTTON1) {
			Point p = ebg.getMousePoint();
			ebg.getObstacles()[p.x / 32][p.y / 32] = ebg.getCurrent();
		}
		if (evt.getButton() == MouseEvent.BUTTON3) {
			ebg.nextObstacle();
		}
	}
}