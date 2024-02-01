package tech.octopusdragon.pokemontypegame.gui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Methods to perform actions in the type game application.
 * @author Alex Gill
 *
 */
public class MenuController {
	
	// --- GUI components ---
	@FXML public ImageView helpButton;
	@FXML public HBox buttonHbox;
	@FXML public VBox menuVbox;
	
	@FXML
	public void initialize() {
		helpButton.setImage(new Image(PokemonTypeGameApplication.HELP_IMAGE_PATH));
		helpButton.setFitWidth(PokemonTypeGameApplication.OPTION_BUTTON_SIZE);
		
		// Resize menu spacing according to window dimensions.
		Platform.runLater(() -> {
			ReadOnlyDoubleProperty sceneWidthProperty = menuVbox.getScene().widthProperty();
			ReadOnlyDoubleProperty sceneHeightProperty = menuVbox.getScene().heightProperty();
			
			menuVbox.spacingProperty().bind(Bindings.createDoubleBinding(() -> {
				double sceneHeight = sceneHeightProperty.get();
				double newSpacing;
				if (sceneHeight > PokemonTypeGameApplication.MENU_MAX_HEIGHT) {
					newSpacing = PokemonTypeGameApplication.MENU_MAX_HEIGHT / 5;
				}
				else {
					newSpacing = sceneHeight / 5;
				}
				return newSpacing;
			}, sceneHeightProperty));
			
			buttonHbox.spacingProperty().bind(Bindings.createDoubleBinding(() -> {
				double sceneWidth = sceneWidthProperty.get();
				double newSpacing;
				if (sceneWidth > PokemonTypeGameApplication.GUI_PREF_WIDTH) {
					newSpacing = PokemonTypeGameApplication.GUI_PREF_WIDTH / (buttonHbox.getChildren().size() * 2);
				}
				else {
					newSpacing = sceneWidth / (buttonHbox.getChildren().size() * 2);
				}
				return newSpacing;
			}, sceneWidthProperty));
		});
	}

	
	/**
	 * Switches to instructions scene
	 */
	public void switchToInstructionsScene(MouseEvent e) {
		PokemonTypeGameApplication.switchToScene(PokemonTypeGameApplication.INSTRUCTIONS_FXML_PATH);
	}

	
	/**
	 * Switches to easy game scene
	 */
	public void switchToEasyGameScene(ActionEvent e) {
		PokemonTypeGameApplication.playSound();
		PokemonTypeGameApplication.newEasyGame();
		PokemonTypeGameApplication.switchToScene(PokemonTypeGameApplication.GAME_SCREEN_FXML_PATH);
	}

	
	/**
	 * Switches to medium game scene
	 */
	public void switchToMediumGameScene(ActionEvent e) {
		PokemonTypeGameApplication.playSound();
		PokemonTypeGameApplication.newMediumGame();
		PokemonTypeGameApplication.switchToScene(PokemonTypeGameApplication.GAME_SCREEN_FXML_PATH);
	}

	
	/**
	 * Switches to hard game scene
	 */
	public void switchToHardGameScene(ActionEvent e) {
		PokemonTypeGameApplication.playSound();
		PokemonTypeGameApplication.newHardGame();
		PokemonTypeGameApplication.switchToScene(PokemonTypeGameApplication.GAME_SCREEN_FXML_PATH);
	}
}
