/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.controller;

public abstract class KeyAction {
	private Key key;

	protected KeyAction(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public abstract void performKeyUp();
}
