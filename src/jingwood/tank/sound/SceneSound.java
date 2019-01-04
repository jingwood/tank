/*
 * Classic Tank Game
 *
 * MIT License
 * Copyright (c) 2004-2019 Jingwood, All right reserved.
 */

package jingwood.tank.sound;

import jingwood.tank.system.ConfigurationManager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SceneSound {
	private String fileName;
	private Clip clip;

	public SceneSound(String fileName) {
		this.fileName = fileName;

		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(
					ConfigurationManager.getInstance().getSoundFileDir(fileName)));

			DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(),
					(int) (stream.getFrameLength() * stream.getFormat().getFrameSize()));

			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);

			stream.close();

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static void main(String[] args) {
		new SceneSound("begin.wav");
	}

	public Clip getClip() {
		return clip;
	}

	public void Dispose() {
		clip = null;
	}
}
