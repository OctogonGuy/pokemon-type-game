package tech.octopusdragon.pokemontypegame.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import tech.octopusdragon.pokemontypegame.Game;
import tech.octopusdragon.pokemontypegame.Pokemon;
import tech.octopusdragon.pokemontypegame.PokemonGame;
import tech.octopusdragon.pokemontypegame.Type;
import tech.octopusdragon.pokemontypegame.TypeGame;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


/**
 * Allows the user to play a game in which the player is presented with a
 * Pokémon type or Pokémon and has to select which type is most effective
 * against it.
 * @author Alex Gill
 *
 */
public class PokemonTypeGameApplication extends Application {
	
	// --- Constants ---
	public final static double GUI_PREF_WIDTH = 800.0;
	public final static double MENU_MAX_HEIGHT = 550.0;
	public final static double MIN_WIDTH = 475.0;
	public final static double MIN_HEIGHT = 615.0;
	public final static double OPTION_BUTTON_SIZE = 45;
	public final static double POKEMON_TYPE_ICON_SIZE = 27.5;
	
	public final static String TYPE_IMAGES_DIR = "resources/img/type/";
	public final static String POKEMON_IMAGES_DIR = "resources/img/pkmn/";
	public final static String HELP_IMAGE_PATH = "resources/img/help.png";
	public final static String FAST_FORWARD_IMAGE_PATH = "resources/img/fast_forward.png";
	public final static String MUTE_IMAGE_PATH = "resources/img/mute.png";
	public final static String UNMUTE_IMAGE_PATH = "resources/img/unmute.png";
	public final static String ICON_PATH = "resources/img/icon.png";
	
	public final static String MENU_FXML_PATH = "Menu.fxml";
	public final static String GAME_SCREEN_FXML_PATH = "GameScreen.fxml";
	public final static String INSTRUCTIONS_FXML_PATH = "Instructions.fxml";
	
	private final static String SONG_DIR = "resources/audio/music/";
	private final static String PRESS_BUTTON_SOUND_DIR = "resources/audio/press_button.mp3";
	
	public final static String SONG_LIST_PATH = "resources/song_list.txt";
	public final static String HOW_TO_PLAY_PATH = "resources/how_to_play.txt";
	private final static String CSS_PATH = "resources/styles.css";
	
	public final static PseudoClass GEN_DISABLED_PSEUDO_CLASS = PseudoClass.getPseudoClass("gen-disabled");
	
	private final static int HARD_MODE_INTERVAL = 5;
	
	
	// --- Variables ---
	public static Game game;	// The current game
	public static GameMode gameMode;	// Game mode of the current game
	public static Userdata userdata;	// The userdata
	private static MediaPlayer musicPlayer;		// Music player
	private static MediaPlayer soundPlayer;		// Sound effect player
	private static Stack<String> shuffleList;		// List of song paths
	public static BooleanProperty mutedProperty;	// Muted property
	public static ObjectProperty<BackgroundGradient> backgroundGradientProperty;	// Current bg
	
	
	// --- Properties ---
	public static BooleanProperty[] mediumIncludeGenProperty;
	public static BooleanProperty mediumIncludeMegaProperty;
	public static BooleanProperty mediumIncludeRegionalProperty;
	
	
	// --- GUI components ---
	private static Stage stage;
	private static Scene scene;
	private static Parent root;
	
	
	@Override
	public void init() {
		// Create the userdata
		userdata = Userdata.load();
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		// Instantiate mute property and write to file on change.
		mutedProperty = new SimpleBooleanProperty(userdata.readMuted());
		
		// Instantiate background gradient property and write to file on change.
		backgroundGradientProperty = new SimpleObjectProperty<BackgroundGradient>(userdata.readBackgroundGradient());
		backgroundGradientProperty.addListener((observable, oldValue, newValue) -> {
			changeBackgroundGradient(newValue);
		});
		
		// Create a list of songs and play the first song
		newShuffleList();
		nextSong();
		
		// Set up the sound effect
		soundPlayer = new MediaPlayer(new Media(getClass().getClassLoader().getResource(PRESS_BUTTON_SOUND_DIR).toExternalForm()));
		soundPlayer.muteProperty().bind(mutedProperty);
		soundPlayer.setOnEndOfMedia(() -> {
			soundPlayer.stop();
		});
		
		// Initialize properties
		mediumIncludeGenProperty = new BooleanProperty[Pokemon.getHighestGeneration()];
		for (int i = 0; i < mediumIncludeGenProperty.length; i++) {
			final int gen = i + 1;
			mediumIncludeGenProperty[i] = new SimpleBooleanProperty(userdata.readMediumGenEnabled(gen));
			mediumIncludeGenProperty[i].addListener((observable, oldValue, newValue) -> {
				userdata.writeMediumGenEnabled(gen, newValue);
			});
		}
		mediumIncludeMegaProperty = new SimpleBooleanProperty(userdata.readMediumMegaEnabled());
		mediumIncludeMegaProperty.addListener((observable, oldValue, newValue) -> {
			userdata.writeMediumMegaEnabled(newValue);
		});
		mediumIncludeRegionalProperty = new SimpleBooleanProperty(userdata.readMediumRegionalEnabled());
		mediumIncludeRegionalProperty.addListener((observable, oldValue, newValue) -> {
			userdata.writeMediumRegionalEnabled(newValue);
		});
		
		// Set the scene
		primaryStage.setTitle("Pokémon Type Game");
		primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(ICON_PATH)));
		primaryStage.setWidth(userdata.readWidth());
		primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
			if (!primaryStage.isMaximized())
			userdata.writeWidth(newValue.doubleValue());
		});
		primaryStage.setHeight(userdata.readHeight());
		primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
			if (!primaryStage.isMaximized())
				userdata.writeHeight(newValue.doubleValue());
		});
		primaryStage.setMaximized(userdata.readMaximized());
		primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
			userdata.writeMaximized(newValue);
		});
		switchToScene(MENU_FXML_PATH);
		scene.getStylesheets().add(PokemonTypeGameApplication.class.getClassLoader().getResource(CSS_PATH).toExternalForm());
		stage.setScene(scene);
		primaryStage.show();
		setStageMinSize(primaryStage, MIN_WIDTH, MIN_HEIGHT);
	}
	
	
	@Override
	public void stop() {
		// Save the userdata upon close
		userdata.save();
	}
	
	
	
	/**
	 * Plays the next song in the list of songs.
	 */
	public static void nextSong() {
		if (musicPlayer != null)
			musicPlayer.stop();
		if (shuffleList.isEmpty())
			newShuffleList();
		String filename = shuffleList.peek();
		Media song = null;
		try {
			song = new Media(PokemonTypeGameApplication.class.getClassLoader().getResource(shuffleList.pop()).toExternalForm());
		}catch (Exception e) {
			System.out.println(filename);
			e.printStackTrace();
		}
		musicPlayer = new MediaPlayer(song);
		musicPlayer.setOnEndOfMedia(() -> {
			nextSong();
		});
		musicPlayer.muteProperty().bind(mutedProperty);
		musicPlayer.play();
	}
	
	
	
	/**
	 * Creates a new list of songs that are in a random order.
	 */
	private static void newShuffleList() {
		shuffleList = new Stack<String>();

		// Open the song list file
		InputStream inputStream = PokemonTypeGameApplication.class.getClassLoader().getResourceAsStream(SONG_LIST_PATH);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputStreamReader);
		Scanner inputFile = new Scanner(in);
		
		// Read the song filenames from the file
		String line;
		while (inputFile.hasNext()) {
			line = inputFile.nextLine();
			shuffleList.add(SONG_DIR + line);
		}
		
		// Close the song list file
		inputFile.close();
        
		// Shuffle the songs
		Collections.shuffle(shuffleList);
	}
	
	
	
	/**
	 * Toggles the mute property and writes the new value to the userdata file
	 */
	public static void toggleMute() {
		mutedProperty.set(!mutedProperty.get());
		userdata.writeMuted(mutedProperty.get());
	}
	
	
	/**
	 * Plays the button press sound effect
	 */
	public static void playSound() {
		soundPlayer.play();
	}
	
	
	
	/**
	 * Switches to the a new scene with fxml root.
	 * @param fxmlPath The path to the fxml
	 */
	public static void switchToScene(String fxmlPath) {
		try {
			root = FXMLLoader.load(PokemonTypeGameApplication.class.getResource(fxmlPath));
			if (scene == null) {
				scene = new Scene(root);
			}
			else {
				scene.setRoot(root);
			}
			changeBackgroundGradient(backgroundGradientProperty.get());
		} catch (IOException e) {
			System.out.println("Unable to load FXML and set the scene.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	

	/**
	 * Rotates to the next selection of background gradient and stores the value
	 * in the userdata file
	 */
	public static void rotateBackgroundGradient() {
		List<BackgroundGradient> values = Arrays.asList(BackgroundGradient.values());
		int curGradientIndex = values.indexOf(backgroundGradientProperty.get());
		int nextGradientIndex = curGradientIndex == values.size() - 1 ? 0 : curGradientIndex + 1;
		backgroundGradientProperty.set(values.get(nextGradientIndex));

		userdata.writeBackgroundGradient(backgroundGradientProperty.get());
	}
	
	
	/**
	 * Changes the background gradient
	 * @param gradient The background gradient to change to
	 */
	private static void changeBackgroundGradient(BackgroundGradient gradient) {
		root.setStyle(String.format("-fx-background-color: linear-gradient(%s);",
				gradient.getGradient()));
	}
	
	
	/**
	 * Starts a new easy game
	 */
	public static void newEasyGame() {
		game = new TypeGame();
		gameMode = GameMode.EASY;
	}
	
	
	/**
	 * Starts a new medium game
	 */
	public static void newMediumGame() {
		game = new PokemonGame();
		gameMode = GameMode.MEDIUM;
		nextRound();
	}
	
	
	/**
	 * Starts a new hard game
	 */
	public static void newHardGame() {
		game = new PokemonGame();
		gameMode = GameMode.HARD;
		nextRound();
	}
	
	
	/**
	 * Submits a guess for the round of the current game and updates the
	 * userdata file if the player achieved a new highest streak for the current
	 * mode.
	 * @param guess The guess
	 */
	public static void makeGuess(Type guess) {
		game.makeGuess(guess);
		if (game.getStreak() > userdata.readHighestStreak(gameMode)) {
			userdata.writeHighestStreak(gameMode, game.getStreak());
		}
	}
	
	
	/**
	 * Moves to the next round
	 */
	public static void nextRound() {
		switch (gameMode) {
		case EASY:
			game.nextRound();
			break;
		case MEDIUM:
			List<Integer> generationsToIncludeList = new ArrayList<Integer>();
			for (int i = 0; i < mediumIncludeGenProperty.length; i++) {
				if (mediumIncludeGenProperty[i].get() == true) {
					generationsToIncludeList.add(i + 1);
				}
			}
			int[] generationsToInclude = new int[generationsToIncludeList.size()];
			for (int i = 0; i < generationsToInclude.length; i++) {
				generationsToInclude[i] = generationsToIncludeList.get(i);
			}
			((PokemonGame)game).nextRound(mediumIncludeMegaProperty.get(), mediumIncludeRegionalProperty.get(), generationsToInclude);
			break;
		case HARD:
			if (game.getStreak() / HARD_MODE_INTERVAL + 1 > Pokemon.getHighestGeneration()) {
				game.nextRound();
			}
			else {
				((PokemonGame)game).nextRound(false, true, game.getStreak() / HARD_MODE_INTERVAL + 1);
			}
			break;
		}
	}
	
	
	/**
	 * Toggles generation for medium Pokemon selection
	 * @param generation The generation
	 */
	public static void toggleIncludeGen(int generation) {
		mediumIncludeGenProperty[generation].set(!mediumIncludeGenProperty[generation].get());
	}
	
	
	/**
	 * Toggles mega evolutions for medium Pokemon selection
	 */
	public static void toggleIncludeMega() {
		mediumIncludeMegaProperty.set(!mediumIncludeMegaProperty.get());
	}
	
	
	/**
	 * Toggles regional forms for medium Pokemon selection
	 */
	public static void toggleIncludeRegional() {
		mediumIncludeRegionalProperty.set(!mediumIncludeRegionalProperty.get());
	}
	
	
	/**
	 * Returns the image path of the defending type or Pokemon
	 * @return The image path
	 */
	public static String defendingImagePath() {
		String path = "";
		if (game.getClass().equals(TypeGame.class)) {
			path = TYPE_IMAGES_DIR + game.getDefendingImageFilename();
		} else if (game.getClass().equals(PokemonGame.class)) {
			path = POKEMON_IMAGES_DIR + game.getDefendingImageFilename();
		}
		return path;
	}
	
	
	/**
	 * Returns the image path of the attacking type at the given index
	 * @param index The index
	 * @return The image path
	 */
	public static String attackingTypeImagePath(int index) {
		return TYPE_IMAGES_DIR + game.getAttackingType(index).getImageFilename();
	}
	
	
	
	/**
	 * Sets the minimum size for a JavaFX Stage. It accounts for window
	 * decorations.
	 * @param width The preferred minimum width of the Stage
	 * @param height The preffered minimum height of the Stage
	 */
	private static void setStageMinSize(Stage stage,
									   double width, double height) {
		
		// Calculate the top, bottom, left, and right window insets
		double leftInsets = stage.getScene().getX();
		double topInsets = stage.getScene().getY();
		double rightInsets = stage.getScene().getWindow().getWidth() -
						stage.getScene().getWidth() - stage.getScene().getX();
		double bottomInsets = stage.getScene().getWindow().getHeight() -
						stage.getScene().getHeight() - stage.getScene().getY();
		
		// Set the stage's minimum width and height, accounting for the insets/
		// decorations
		stage.setMinWidth(width + leftInsets + rightInsets);
		stage.setMinHeight(height + topInsets + bottomInsets);
	}
	

	
	public static void main(String[] args) {
		launch(args);
	}

}
