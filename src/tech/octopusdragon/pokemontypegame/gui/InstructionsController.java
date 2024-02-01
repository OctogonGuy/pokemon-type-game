package tech.octopusdragon.pokemontypegame.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

/**
 * Methods to perform actions in the type game application.
 * @author Alex Gill
 *
 */
public class InstructionsController {
	
	// --- GUI components ---
	@FXML public ScrollPane bodyScrollPane;
	@FXML public Label bodyLabel;
	
	@FXML
	public void initialize() {
		bodyScrollPane.setMaxWidth(PokemonTypeGameApplication.GUI_PREF_WIDTH);
		Platform.runLater(() -> {
			bodyScrollPane.applyCss();
			updateColors();
			PokemonTypeGameApplication.backgroundGradientProperty.addListener((observable, oldValue, newValue) -> {
				updateColors();
			});
		});
		
		// Load instructions from text file
		try {
			InputStream fstream = InstructionsController.class.getClassLoader().getResourceAsStream(PokemonTypeGameApplication.HOW_TO_PLAY_PATH);
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(fstream, "UTF-8"));
			StringBuilder howToPlay = new StringBuilder();
			String line;
			while ((line = inputFile.readLine()) != null) {
				howToPlay.append(line + "\n\n");
			}
			howToPlay.deleteCharAt(howToPlay.length() - 1);
			fstream.close();
			bodyLabel.setText(howToPlay.toString());
		} catch (IOException e) {
			System.out.println("Error reading how to play file.");
			e.printStackTrace();
		}
	}

	/**
	 * Switches to menu scene
	 */
	public void switchToMenuScene(ActionEvent e) {
		PokemonTypeGameApplication.switchToScene(PokemonTypeGameApplication.MENU_FXML_PATH);
	}
	
	/**
	 * Updates the colors of the instructions scene to reflect the current
	 * background gradient.
	 */
	public void updateColors() {
		bodyScrollPane.lookup(".thumb").
		setStyle(String.format("-fx-background-color: %s;", PokemonTypeGameApplication.backgroundGradientProperty.get().getMainColor()));
		bodyScrollPane.lookup(".track-background").setStyle(String.format("-fx-background-color: %s;", PokemonTypeGameApplication.backgroundGradientProperty.get().getSecondaryColor()));
		bodyScrollPane.lookup(".increment-button").setStyle(String.format("-fx-background-color: %s;", PokemonTypeGameApplication.backgroundGradientProperty.get().getMainColor()));
		bodyScrollPane.lookup(".increment-arrow").setStyle(String.format("-fx-background-color: %s;", PokemonTypeGameApplication.backgroundGradientProperty.get().getSecondaryColor()));
		bodyScrollPane.lookup(".decrement-button").setStyle(String.format("-fx-background-color: %s;", PokemonTypeGameApplication.backgroundGradientProperty.get().getMainColor()));
		bodyScrollPane.lookup(".decrement-arrow").setStyle(String.format("-fx-background-color: %s;", PokemonTypeGameApplication.backgroundGradientProperty.get().getSecondaryColor()));
	}
}
