/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.battleground;

import jingwood.tank.element.obstacle.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

/*
 *.STG Stage file reader
 *
 * The .stg file stores a battleground stage as a single file.
 *
 * A battleground stage is a 13x13 two-dimensional array that stores
 * the objects on the stage.
 *
 *         0......6.....12
 *         .
 *         .  ----> ----
 *         .  -> ----->
 *         6
 *         .  store order
 *         .
 *         .
 *         12
 *
 * Every object has a code that specifies the object type, e.g.
 * 1 is normal wall and 6 is steel wall. (see below for all codes)
 *
 * All location on the stage are allowed to put objects but when .stg
 * file is loaded, the following 3 locations will be reset to spawn
 * enemy tanks.
 *
 *          (0,0)      (6,0)       (12,0)
 *
 * Player's base will be automatically put at (6,12) with 5 walls around it.
 *
 *              Wall   Wall   Wall
 *              Wall  (6,12)  Wall
 *
 * If loading is failed, null will be returned.
 */

public class STGReader {
	public static Vector getObstacles(File file, BattleGround bg) {
		if (file.exists()) {
			try {
				final FileInputStream fin = new FileInputStream(file);
				final Vector ob = new Vector();

				for (int n = 0; n < 13; n++) {
					for (int m = 0; m < 13; m++) {
						final int code = fin.read();
						if ((m == 0 || m == 6 || m == 12) && n == 0) {
							continue;
						}
						if (m == 5 && n == 11 && code == 0) {
							Wall wall = new Wall(Wall.WALL_DOWN);
							wall.setLocation(m, n);
							wall.addDestructionLeft(2);
							ob.addElement(wall);
							continue;
						}
						if (m == 6 && n == 11 && code == 0) {
							Wall wall = new Wall(Wall.WALL_DOWN);
							wall.setLocation(m, n);
							ob.addElement(wall);
							continue;
						}
						if (m == 7 && n == 11 && code == 0) {
							Wall wall = new Wall(Wall.WALL_DOWN);
							wall.setLocation(m, n);
							wall.addDestructionRight(2);
							ob.addElement(wall);
							continue;
						}
						if (m == 5 && n == 12 && code == 0) {
							Wall wall = new Wall(Wall.WALL_RIGHT);
							wall.setLocation(m, n);
							ob.addElement(wall);
							continue;
						}
						if (m == 7 && n == 12 && code == 0) {
							Wall wall = new Wall(Wall.WALL_LEFT);
							wall.setLocation(m, n);
							ob.addElement(wall);
							continue;
						}
						if (m == 6 && n == 12) {
							Captain captain = new Captain(bg);
							captain.setLocation(m, n);
							ob.add(captain);
							continue;
						}
						switch (code) {
							case 1:
								final Wall wall = new Wall();
								wall.setLocation(m, n);
								ob.addElement(wall);
								break;
							case 2:
								final Wall wallUp = new Wall(Wall.WALL_UP);
								wallUp.setLocation(m, n);
								ob.addElement(wallUp);
								break;
							case 3:
								final Wall wallRight = new Wall(Wall.WALL_RIGHT);
								wallRight.setLocation(m, n);
								ob.addElement(wallRight);
								break;
							case 4:
								final Wall wallDown = new Wall(Wall.WALL_DOWN);
								wallDown.setLocation(m, n);
								ob.addElement(wallDown);
								break;
							case 5:
								final Wall wallLeft = new Wall(Wall.WALL_LEFT);
								wallLeft.setLocation(m, n);
								ob.addElement(wallLeft);
								break;
							case 6:
								final Steel steel = new Steel();
								steel.setLocation(m, n);
								ob.add(steel);
								break;
							case 7:
								final Steel steelUp = new Steel(Steel.STEEL_UP);
								steelUp.setLocation(m, n);
								ob.add(steelUp);
								break;
							case 8:
								final Steel steelRight = new Steel(Steel.STEEL_RIGHT);
								steelRight.setLocation(m, n);
								ob.add(steelRight);
								break;
							case 9:
								final Steel steelDown = new Steel(Steel.STEEL_DOWN);
								steelDown.setLocation(m, n);
								ob.add(steelDown);
								break;
							case 10:
								final Steel steelLeft = new Steel(Steel.STEEL_LEFT);
								steelLeft.setLocation(m, n);
								ob.add(steelLeft);
								break;
							case 11:
								Grass grass = new Grass();
								grass.setLocation(m, n);
								ob.add(grass);
								break;
							case 12:
								Water water = new Water();
								water.setLocation(m, n);
								ob.add(water);
								break;
							case 13:
								Snow snow = new Snow();
								snow.setLocation(m, n);
								ob.add(snow);
								break;
						}
					}
				}
				fin.close();
				return ob;
			} catch (IOException ignore) {
			}
		} else {
			System.err.println("ERROR - Load stage file failed: " + file.getPath());
			return null;
		}
		return null;
	}
}