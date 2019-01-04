/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.controller;

public class KeyGroupMap {
	private ControllableElement controllableObject;
	private KeyGroup mappedKeyGroup;

	public KeyGroupMap(ControllableElement controllableObject, KeyGroup mappedKeyGroup) {
		this.controllableObject = controllableObject;
		this.mappedKeyGroup = mappedKeyGroup;
	}

	public ControllableElement getControllableObject() {
		return controllableObject;
	}

	public void setControllableObject(ControllableElement controllableObject) {
		this.controllableObject = controllableObject;
	}

	public KeyGroup getMappedKeyGroup() {
		return mappedKeyGroup;
	}

	public void setMappedKeyGroup(KeyGroup mappedKeyGroup) {
		this.mappedKeyGroup = mappedKeyGroup;
	}
}
