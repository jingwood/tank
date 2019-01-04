package jingwood.tank.system;

import java.util.Hashtable;

public class ConfigurationManager {
	private static final String DIR_SOUND = "dir_sound";

	private static final ConfigurationManager instance = new ConfigurationManager();

	private static final Hashtable folderList = new Hashtable();

	static {
		folderList.put(DIR_SOUND, "sounds");
	}

	private ConfigurationManager() {
	}

	public static final ConfigurationManager getInstance() {
		return instance;
	}

	public String getSoundFileDir(String name) {
		return folderList.get(DIR_SOUND) + "/" + name;
	}
}
