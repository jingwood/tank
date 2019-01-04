/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.controller;

public class Key {
	private boolean isPress = false;
	private int key;
	private String name;

	public Key(String name, int c) {
		this.name = name;
		key = c;
	}

	public String getName() {
		return name;
	}

	public boolean isPress() {
		return isPress;
	}

	public void setPress(boolean press) {
		isPress = press;
	}

	public boolean isKey(int key) {
		return this.key == key;
	}

	public int getKeyCode() {
		return key;
	}
}
