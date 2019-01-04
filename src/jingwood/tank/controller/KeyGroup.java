/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.controller;

import java.util.Hashtable;

public class KeyGroup {

	private String name;
	private Hashtable keys = new Hashtable();

	public KeyGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addKey(Key key) {
		keys.put(key.getKeyCode(), key);
	}

	public Hashtable getKeys() {
		return keys;
	}

	public void pressKey(int keyCode) {
		Key key = (Key) keys.get(keyCode);
		key.setPress(true);
	}

	public void releaseKey(int keyCode){
		Key key = (Key) keys.get(keyCode);
		key.setPress(false);
	}
}
