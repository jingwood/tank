/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.controller;

import jingwood.tank.element.Element;

public interface ControllableElement extends Element {
	void performKeyAction(Key key);

	/**
	 * For any removable objects
	 */
	boolean doTurnUp();
	boolean doTurnDown();
	boolean doTurnLeft();
	boolean doTurnRight();


	/**
	 * For only Tanks
	 */
	boolean doShot();
}
