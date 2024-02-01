package tech.octopusdragon.pokemontypegame.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;

import tech.octopusdragon.pokemontypegame.Pokemon;

/**
 * Methods for interacting with the user's data/progress in the type game.
 * @author Alex Gill
 *
 */
public class Userdata implements Serializable {
	
	private static final long serialVersionUID = -6762807013732896858L;

	private static final String USERDATA_FOLDERNAME = "userdata";
	private static final String USERDATA_FILENAME = "pokemon_type_game_userdata.ser";
	
	private final static String WIDTH_KEY = "width";
	private final static double DEFAULT_WIDTH_VALUE = 500.0;
	private final static String HEIGHT_KEY = "height";
	private final static double DEFAULT_HEIGHT_VALUE = 750.0;
	private final static String MAXIMIZED_KEY = "maximized";
	private final static boolean DEFAULT_MAXIMIZED_VALUE = false;
	private static final String MUTED_KEY = "muted";
	private static final boolean DEFAULT_MUTED_VALUE = false;
	private static final String BACKGROUND_GRADIENT_KEY = "backgroundGradient";
	private static final BackgroundGradient DEFAULT_BACKGROUND_GRADIENT_VALUE = BackgroundGradient.ORANGE;
	private static final String EASY_HIGHEST_STREAK_KEY = "easyHighestStreak";
	private static final String MEDIUM_HIGHEST_STREAK_KEY = "mediumHighestStreak";
	private static final String HARD_HIGHEST_STREAK_KEY = "hardHighestStreak";
	private static final int DEFAULT_HIGHEST_STREAK_VALUE = 0;
	private static final String MEDIUM_GEN_ENABLED_KEY = "medium-gen-%d-enabled";
	private static final boolean DEFAULT_MEDIUM_GEN_ENABLED_VALUE = true;
	private static final String MEDIUM_MEGA_ENABLED_KEY = "medium-mega-enabled";
	private static final boolean DEFAULT_MEDIUM_MEGA_ENABLED_VALUE = false;
	private static final String MEDIUM_REGIONAL_ENABLED_KEY = "medium-regional-enabled";
	private static final boolean DEFAULT_MEDIUM_REGIONAL_ENABLED_VALUE = true;
	
	private Properties userdata;
	
	
	/**
	 * Creates an empty userdata object
	 */
	public Userdata() {
		userdata = new Properties();
	}
	
	
	/**
	 * Loads userdata from the userdata file
	 * @return A new userdata object with the data loaded into it
	 */
	public static Userdata load() {
		Userdata object = null;
		try {
			FileInputStream fileIn = new FileInputStream(USERDATA_FOLDERNAME + "/" + USERDATA_FILENAME);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			object = (Userdata) objectIn.readObject();
			objectIn.close();
		} catch (IOException | ClassNotFoundException e) {
			object = new Userdata();
		}
		return object;
	}
	
	
	/**
	 * Saves this userdata to the userdata file
	 */
	public void save() {
		
		// Make sure the folder exists
		File folder = new File(USERDATA_FOLDERNAME);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(USERDATA_FOLDERNAME + "/" + USERDATA_FILENAME);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(this);
			objectOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Reads and returns a property value
	 * @param key The property key
	 * @param defaultValue A property value to write if the property is not found
	 * @return The property value
	 */
	private String readProperty(String key, String defaultValue) {
		
		// Get and return the property value
		String value = userdata.getProperty(key);
		
		// Write and return default if property is not found
		if (value == null) {
			writeProperty(key, defaultValue);
			return defaultValue;
		}
		
		// Return the property value;
		return userdata.getProperty(key);
	}
	
	
	/**
	 * Writes a property
	 * @param key The property key
	 * @param value The property value
	 */
	private void writeProperty(String key, String value) {
		
		// Write the property
		userdata.setProperty(key, value);
	}
	
	
	
	
	/**
	 * Reads and returns the window width property value
	 * @return The window width property value
	 */
	public double readWidth() {
		return Double.parseDouble(readProperty(WIDTH_KEY, Double.toString(DEFAULT_WIDTH_VALUE)));
	}
	
	
	/**
	 * Writes the window width property
	 * @param value The window width property value
	 */
	public void writeWidth(double value) {
		writeProperty(WIDTH_KEY, Double.toString(value));
	}
	
	
	/**
	 * Reads and returns the window height property value
	 * @return The window height property value
	 */
	public double readHeight() {
		return Double.parseDouble(readProperty(HEIGHT_KEY, Double.toString(DEFAULT_HEIGHT_VALUE)));
	}
	
	
	/**
	 * Writes the window height property
	 * @param value The window height property value
	 */
	public void writeHeight(double value) {
		writeProperty(HEIGHT_KEY, Double.toString(value));
	}
	
	
	/**
	 * Reads and returns the maximized property value
	 * @return The maximized property value
	 */
	public boolean readMaximized() {
		return Boolean.parseBoolean(readProperty(MAXIMIZED_KEY, Boolean.toString(DEFAULT_MAXIMIZED_VALUE)));
	}
	
	
	/**
	 * Writes the maximized property
	 * @param value The maximized property value
	 */
	public void writeMaximized(boolean value) {
		writeProperty(MAXIMIZED_KEY, Boolean.toString(value));
	}
	
	
	/**
	 * Reads and returns the muted property value
	 * @return The muted property value
	 */
	public boolean readMuted() {
		return Boolean.parseBoolean(readProperty(MUTED_KEY, Boolean.toString(DEFAULT_MUTED_VALUE)));
	}
	
	
	/**
	 * Writes the muted property
	 * @param value The muted property value
	 */
	public void writeMuted(boolean value) {
		writeProperty(MUTED_KEY, Boolean.toString(value));
	}
	
	
	
	
	/**
	 * Reads and returns the background gradient property value
	 * @return The background gradient property value
	 */
	public BackgroundGradient readBackgroundGradient() {
		return BackgroundGradient.valueOf(readProperty(BACKGROUND_GRADIENT_KEY, DEFAULT_BACKGROUND_GRADIENT_VALUE.toString()));
	}
	
	
	/**
	 * Writes the background gradient property
	 * @param value The background gradient property value
	 */
	public void writeBackgroundGradient(BackgroundGradient value) {
		writeProperty(BACKGROUND_GRADIENT_KEY, value.toString());
	}
	
	
	
	
	/**
	 * Reads and returns the highest streak for the given game mode
	 * @param gameMode The game mode
	 * @return The highest streak property value
	 */
	public int readHighestStreak(GameMode gameMode) {
		int highestStreak = 0;
		
		switch (gameMode) {
		case EASY:
			highestStreak = readEasyHighestStreak();
			break;
		case MEDIUM:
			highestStreak = readMediumHighestStreak();
			break;
		case HARD:
			highestStreak = readHardHighestStreak();
			break;
		}
		
		return highestStreak;
	}
	
	
	/**
	 * Writes the highest streak for the given game mode
	 * @param gameMode The game mode
	 * @param value The highest streak property value
	 */
	public void writeHighestStreak(GameMode gameMode, int value) {
		switch (gameMode) {
		case EASY:
			writeEasyHighestStreak(value);
			break;
		case MEDIUM:
			writeMediumHighestStreak(value);
			break;
		case HARD:
			writeHardHighestStreak(value);
			break;
		}
	}
	
	
	
	
	/**
	 * Reads and returns the easy highest streak
	 * @return The easy highest streak property value
	 */
	private int readEasyHighestStreak() {
		return Integer.parseInt(readProperty(EASY_HIGHEST_STREAK_KEY, Integer.toString(DEFAULT_HIGHEST_STREAK_VALUE)));
	}
	
	
	/**
	 * Writes the easy highest streak
	 * @param value The easy highest streak property value
	 */
	private void writeEasyHighestStreak(int value) {
		writeProperty(EASY_HIGHEST_STREAK_KEY, Integer.toString(value));
	}
	
	
	
	
	/**
	 * Reads and returns the medium highest streak
	 * @return The medium highest streak property value
	 */
	private int readMediumHighestStreak() {
		return Integer.parseInt(readProperty(MEDIUM_HIGHEST_STREAK_KEY, Integer.toString(DEFAULT_HIGHEST_STREAK_VALUE)));
	}
	
	
	/**
	 * Writes the medium highest streak
	 * @param value The medium highest streak property value
	 */
	private void writeMediumHighestStreak(int value) {
		writeProperty(MEDIUM_HIGHEST_STREAK_KEY, Integer.toString(value));
	}
	
	
	
	
	/**
	 * Reads and returns the hard highest streak
	 * @return The hard highest streak property value
	 */
	private int readHardHighestStreak() {
		return Integer.parseInt(readProperty(HARD_HIGHEST_STREAK_KEY, Integer.toString(DEFAULT_HIGHEST_STREAK_VALUE)));
	}
	
	
	/**
	 * Writes the hard highest streak
	 * @param value The hard highest streak property value
	 */
	private void writeHardHighestStreak(int value) {
		writeProperty(HARD_HIGHEST_STREAK_KEY, Integer.toString(value));
	}
	
	
	
	
	/**
	 * Reads the medium generation enabled value for the given generation
	 * @param generation The generation
	 * @return The generation enabled value
	 * @throws IllegalArgumentException if the generation does not exist
	 */
	public boolean readMediumGenEnabled(int generation) throws IllegalArgumentException {
		if (generation <= 0 || generation > Pokemon.getHighestGeneration()) {
			throw new IllegalArgumentException(String.format("Must be a number between %d and %d inclusive", 1, Pokemon.getHighestGeneration()));
		}
		return Boolean.parseBoolean(readProperty(String.format(MEDIUM_GEN_ENABLED_KEY, generation), Boolean.toString(DEFAULT_MEDIUM_GEN_ENABLED_VALUE)));
	}
	
	
	/**
	 * Writes the medium generation enabled value for the given generation
	 * @param generation The generation
	 * @param value The generation enabled value
	 * @throws IllegalArgumentException if the generation does not exist
	 */
	public void writeMediumGenEnabled(int generation, boolean value) throws IllegalArgumentException {
		if (generation <= 0 || generation > Pokemon.getHighestGeneration()) {
			throw new IllegalArgumentException(String.format("Must be a number between %d and %d inclusive", 1, Pokemon.getHighestGeneration()));
		}
		writeProperty(String.format(MEDIUM_GEN_ENABLED_KEY, generation), Boolean.toString(value));
	}
	
	
	
	
	/**
	 * Reads and returns the medium mega enabled value
	 * @return The medium mega enabled value
	 */
	public boolean readMediumMegaEnabled() {
		return Boolean.parseBoolean(readProperty(MEDIUM_MEGA_ENABLED_KEY, Boolean.toString(DEFAULT_MEDIUM_MEGA_ENABLED_VALUE)));
	}

	
	/**
	 * Writes the medium mega enabled value
	 * @param value The medium mega enabled value
	 */
	public void writeMediumMegaEnabled(boolean value) {
		writeProperty(MEDIUM_MEGA_ENABLED_KEY, Boolean.toString(value));
	}
	
	
	
	
	/**
	 * Reads and returns the medium regional enabled value
	 * @return The medium regional enabled value
	 */
	public boolean readMediumRegionalEnabled() {
		return Boolean.parseBoolean(readProperty(MEDIUM_REGIONAL_ENABLED_KEY, Boolean.toString(DEFAULT_MEDIUM_REGIONAL_ENABLED_VALUE)));
	}
	
	
	/**
	 * Writes the medium regional enabled value
	 * @param value The medion regional enabled value
	 */
	public void writeMediumRegionalEnabled(boolean value) {
		writeProperty(MEDIUM_REGIONAL_ENABLED_KEY, Boolean.toString(value));
	}
}
