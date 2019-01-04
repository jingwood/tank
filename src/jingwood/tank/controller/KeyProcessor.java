/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.controller;

import jingwood.tank.MainMenu;
import jingwood.tank.WinMain;
import jingwood.tank.element.tank.Tank;
import jingwood.tank.battleground.BattleGround;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyProcessor extends KeyAdapter {
	public void keyPressed(KeyEvent evt) {
		WinMain tank = (WinMain) evt.getSource();
		Component[] comp = tank.getContentPane().getComponents();
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof BattleGround) {
				BattleGroundProcessor((BattleGround) comp[i], evt);
				break;
			} else if (comp[i] instanceof MainMenu) {
				EditPanelProcessor((MainMenu) comp[i], evt);
				break;
			}
		}
	}

	public void keyReleased(KeyEvent evt) {
		WinMain tank = (WinMain) evt.getSource();
		Component[] comp = tank.getContentPane().getComponents();
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof BattleGround) {
				BattleGroundProcessor_KeyReleased((BattleGround) comp[i], evt);
				break;
			}
		}
	}

	public void EditPanelProcessor(MainMenu gm, KeyEvent evt) {
		if (gm.getStatus() == MainMenu.ST_LISTENING && evt.getKeyCode() == 27 /* ESC */) {
			gm.network_stoplisten();
			return;
		}

		if (gm.getStatus() != MainMenu.ST_SELECTING) return;
		switch (evt.getKeyCode()) {
			case 38: /* UP */
				if (gm.getMenuIndex() > 0) {
					gm.setMenuIndex(gm.getMenuIndex() - 1);
					gm.repaint();
				}
				break;
			case 40: /* DOWN */
				if (gm.getMenuIndex() < gm.getMenus()[gm.getMenu()].length - 1) {
					gm.setMenuIndex(gm.getMenuIndex() + 1);
					gm.repaint();
				}
				break;
			case 10: /* ENTER */
				gm.actionMenuPressed();
				break;
		}
	}

	public static final int UP = 'w';
	private boolean upkey = false;
	private boolean rightkey = false;
	private boolean downkey = false;
	private boolean leftkey = false;

	public void BattleGroundProcessor(BattleGround bg, KeyEvent evt) {
		switch (evt.getKeyCode()) {
			case 38:
				bg.tank[0].turnUp();
				bg.tank[0].setDestination(1);
				break;
			case 39:
				bg.tank[0].turnRight();
				bg.tank[0].setDestination(1);
				break;
			case 40:
				bg.tank[0].turnDown();
				bg.tank[0].setDestination(1);
				break;
			case 37:
				bg.tank[0].turnLeft();
				bg.tank[0].setDestination(1);
				break;
			case 10:
				bg.tank[0].setStatus(Tank.ST_NAISSANCING);
				break;
			case 32:
				bg.tank[0].shot();
				break;
		}
	}

	public void BattleGroundProcessor_KeyReleased(BattleGround bg, KeyEvent evt) {

	}
}