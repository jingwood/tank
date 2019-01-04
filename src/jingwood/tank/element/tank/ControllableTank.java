package jingwood.tank.element.tank;

import jingwood.tank.battleground.BattleGround;
import jingwood.tank.controller.ControllableElement;
import jingwood.tank.controller.Key;

public class ControllableTank extends Tank implements ControllableElement {
	public ControllableTank(int tankid, int x, int y, int direction, BattleGround bg) {
		super(tankid, x, y, direction, bg);
	}

	public void performKeyAction(Key key) {
	}

	public boolean doTurnUp() {
		return false;
	}

	public boolean doTurnDown() {
		return false;
	}

	public boolean doTurnLeft() {
		return false;
	}

	public boolean doTurnRight() {
		return false;
	}

	public boolean doShot() {
		return false;
	}

}
