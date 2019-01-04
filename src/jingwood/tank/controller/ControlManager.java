/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.controller;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Iterator;

public class ControlManager {
//	private Vector controllables = new Vector();
	private Hashtable keyGroupList = new Hashtable();
//	private ArrayList keyGroupMapList = new ArrayList();
//	private ArrayList keyActionList = new ArrayList();

	private Hashtable keyGroupMap = new Hashtable();

	private static final ControlManager INSTANCE = new ControlManager();
	public static ControlManager getInstance() {
		return INSTANCE;
	}

	private ControlManager() {
		KeyGroup keyGroup1 = new KeyGroup("Controller 1");
		KeyGroup keyGroup2 = new KeyGroup("Controller 2");

		keyGroup1.addKey(new Key("Up", KeyEvent.VK_W)); // default key, player 1 is key 'w'
		keyGroup1.addKey(new Key("Down", KeyEvent.VK_S)); // default key, player 1 is key 's'
		keyGroup1.addKey(new Key("Left", KeyEvent.VK_A)); // default key, player 1 is key 'a'
		keyGroup1.addKey(new Key("Right", KeyEvent.VK_D)); // default key, player 1 is key 'd'
		keyGroup1.addKey(new Key("Bullet", KeyEvent.VK_Y)); // default key, player 1 is key 'd'

		keyGroup2.addKey(new Key("Up", KeyEvent.VK_UP)); // default key, player 2 is key 'up'
		keyGroup2.addKey(new Key("Down", KeyEvent.VK_DOWN)); // default key, player 2 is key 'down'
		keyGroup2.addKey(new Key("Left", KeyEvent.VK_LEFT)); // default key, player 2 is key 'left'
		keyGroup2.addKey(new Key("Right", KeyEvent.VK_RIGHT)); // default key, player 2 is key 'right'
		keyGroup2.addKey(new Key("Bullet", KeyEvent.VK_NUMPAD1)); // default key, player 2 is key '.'

		keyGroupList.put(keyGroup1.getName(), keyGroup1);
		keyGroupList.put(keyGroup2.getName(), keyGroup2);
	}



	public void registerKeyAction(KeyAction keyAction) {

	}

	public void registerControllableObject(ControllableElement controllableObject,
										   String keyGroupName)
	{
		/**
		 * The same object has been register with same key group name
		 * So, we nothing to do
		 */
		if(keyGroupMap.get(keyGroupName).equals(controllableObject)){
			return;
		}
		keyGroupMap.put(keyGroupName, controllableObject);
	}

	private void callKeyAction(KeyGroup keyGroup, int key) {
		ControllableElement controllableObject = getMappedController(keyGroup.getName());
		if(controllableObject==null)
		{
			/**
			 * No mapped controllable object for key group, so we are nothing to do
			 */
			return;
		}

		System.out.println("key = " + key);
	}

	private ControllableElement getMappedController(String keyGroupName) {
		for( Iterator i = keyGroupMap.keySet().iterator(); i.hasNext(); ) {
			String name = (String) i.next();
			if(name.equals(keyGroupName)){
				return (ControllableElement) keyGroupMap.get(name);
			}
		}
		return null;
	}

	private void performKeyPressed(int key) {
		System.out.println("key = " + key);
		for(Iterator i = keyGroupList.values().iterator(); i.hasNext(); ) {
			KeyGroup keyGroup = (KeyGroup) i.next();
			keyGroup.pressKey(key);
			callKeyAction(keyGroup, key);
		}
	}

	private void performKeyReleased(int key) {
		System.out.println("key = " + key);
		for(Iterator i = keyGroupList.values().iterator(); i.hasNext(); ) {
			KeyGroup keyGroup = (KeyGroup) i.next();
			keyGroup.pressKey(key);
		}
	}
/*
	public boolean RegisterControllableObject(ControllableElement controllableObject,
											  KeyGroup keyGroup) {

	}
*/

/*	public boolean RegisterControllableObjectOrderly(ControllableElement controllableObject) {
	   KeyGroup keyGroup = findNextAvailable();
	   if(keyGroup == null) {
		   return false;
	   }
	   keyGroupMapList.add(new KeyGroupMap(controllableObject, keyGroup));
	   return true;
   }

   public void RegisterKeyAction(KeyAction keyAction) {
	   keyActionList.add(keyAction);
   }

   protected KeyGroup findNextAvailable() {
	   for(Iterator i = keyGroupList.iterator(); i.hasNext(); ){
		   KeyGroup keyGroup = (KeyGroup) i.next();

		   boolean isFound = false;
		   for(Iterator i2 = keyGroupMapList.iterator(); i2.hasNext(); ){
			   KeyGroupMap keyGroupMap = (KeyGroupMap) i2.next();
			   if(keyGroupMap.getMappedKeyGroup()==keyGroup){
				   isFound = true;
				   break;
			   }
		   }

		   if (!isFound) {
			   return keyGroup;
		   }
	   }
	   return null;
   }
   */
	private final EventSourceListener eventSourceListener = new EventSourceListener(this);

	public void attachEventSource(JComponent control) {
		control.addKeyListener(eventSourceListener);
	}

	private class EventSourceListener extends KeyAdapter{
		private ControlManager controlManager;

		public EventSourceListener(ControlManager controlManager) {
			this.controlManager = controlManager;
		}

		public ControlManager getKeyManager() {
			return controlManager;
		}

		public void setKeyManager(ControlManager controlManager) {
			this.controlManager = controlManager;
		}

		public void keyPressed(KeyEvent e) {
			performKeyPressed(e.getKeyCode());
		}

		public void keyReleased(KeyEvent e) {
			performKeyReleased(e.getKeyCode());
		}
	}

}
