/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element.obstacle;

import jingwood.tank.element.tank.Tank;

public interface Obstructive {
	boolean checkMoveAble(Tank tank);
}
