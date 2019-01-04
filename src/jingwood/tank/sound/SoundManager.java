/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

public class SoundManager extends Thread {
    private static final SoundManager instance = new SoundManager();

    public static final SoundManager getInstance() {
        return instance;
    }

//	private static final Hashtable soundList = new Hashtable();

    public static final SceneSound SS_GAMESTART = new SceneSound("begin.wav");
    public static final SceneSound SS_GOTLIFE = new SceneSound("addlife.wav");
    public static final SceneSound SS_SHOT = new SceneSound("shot.wav");
    public static final SceneSound SS_TOUCHBORDER = new SceneSound("border.wav");

//	public static final String SS_GAMESTART = "SS_GAMESTART";

    private final ArrayList<SceneSound> playingQueue = new ArrayList<SceneSound>();

    private boolean disabled = true;

    public void playSceneSound(SceneSound ss) {
        if (disabled) return; // disable sound

        if (ss.getClip().isRunning() || playingQueue.contains(ss)) {
            return;
        }

        playingQueue.add(ss);
        System.out.println("clip opened...");
        ss.getClip().start();

        if (!this.isAlive()) {
            this.start();
        }
    }


    public void run() {
        while (playingQueue.size() > 0) {
            for (Iterator i = playingQueue.iterator(); i.hasNext(); ) {
                SceneSound ss = (SceneSound) i.next();
                if (!ss.getClip().isRunning()) {
                    ss.getClip().stop();
                    i.remove();
                    System.out.println("clip stopped...");
                }
            }
        }
    }

    public static void main(String[] args) {
        SoundManager.getInstance().playSceneSound(SoundManager.SS_GAMESTART);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SoundManager.getInstance().playSceneSound(SoundManager.SS_GAMESTART);
    }

}
