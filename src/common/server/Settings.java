package common.server;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Settings implements Serializable {

	private static final String SETTINGS_DAT = "settings.dat";
	private static final long serialVersionUID = 7010493017533238086L;
	private static final String PASSWORD_FORMAT = "%03d";
	private static final int PASSWORD_MAX = 999999;
	public static final Logger log = LogManager.getLogger();

	private static Settings settings;

	private int port = 5555;
	private String password;
	private Level level = Level.ERROR;
	private boolean autostart = false;
	private boolean restartOnError = false;

	private void genPassword() {
		Random r = new Random();
		int p = r.nextInt(PASSWORD_MAX);
		password = String.format(PASSWORD_FORMAT, p);
	}

	public int getPort() {
		return port;
	}

	public Level getLevel() {
		return level;
	}

	private static void close(Closeable c) {
		if (c == null) {
			return;
		}
		try {
			c.close();
		} catch (IOException e) {
			log.catching(e);
		}
	}

	public static Settings loadSettigs() {
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		Settings settings;
		try {
			fin = new FileInputStream(SETTINGS_DAT);
			ois = new ObjectInputStream(fin);
			settings = (Settings) ois.readObject();
		} catch (FileNotFoundException e) {
			log.catching(e);
			log.info("Settings not found, creating new...");
			settings = new Settings();
			saveSettings(settings);
		} catch (Exception e) {
			log.error("Could not load settings file");
			log.catching(e);
			settings = new Settings();
		} finally {
			close(ois);
			close(fin);
		}
		Settings.settings = settings;
		return settings;
	}

	public static void saveSettings() {
		saveSettings(settings);
	}

	public static void saveSettings(Settings settings) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			File f = new File(SETTINGS_DAT);
			f.createNewFile();
			fos = new FileOutputStream(f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(settings);
		} catch (IOException e) {
			log.error("Error while saving file");
			log.catching(e);
		} finally {
			close(oos);
			close(fos);
		}
	}

	public static Settings getSettings() {
		return settings;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public boolean isAutostart() {
		return autostart;
	}

	public void setAutostart(boolean autostart) {
		this.autostart = autostart;
	}

	public boolean isRestartOnError() {
		return restartOnError;
	}

	public void setRestartOnError(boolean restartOnError) {
		this.restartOnError = restartOnError;
	}

	public String getPassword() {
		if (password == null) {
			genPassword();
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
