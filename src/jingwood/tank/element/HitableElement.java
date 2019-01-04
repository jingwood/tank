/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.element;

public interface HitableElement extends Element {
	boolean checkHitAble(Bullet shot);
}
