package tech.octopusdragon.pokemontypegame.gui;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Methods to perform actions in the type game application.
 * @author Alex Gill
 *
 */
public class OptionsBarController {
	
	// --- Variables ---
	
	// --- GUI components ---
	@FXML public Rectangle changeBackgroundColorButton;
	@FXML public ImageView fastForwardButton;
	@FXML public ImageView muteButton;
	
	@FXML
	public void initialize() {
		changeBackgroundColorButton.fillProperty().bind(Bindings.createObjectBinding(() -> {
			return Color.web(PokemonTypeGameApplication.backgroundGradientProperty.get().getMainColor());
		}, PokemonTypeGameApplication.backgroundGradientProperty));
		changeBackgroundColorButton.setWidth(PokemonTypeGameApplication.OPTION_BUTTON_SIZE);
		changeBackgroundColorButton.setHeight(PokemonTypeGameApplication.OPTION_BUTTON_SIZE);
		fastForwardButton.setImage(new Image(PokemonTypeGameApplication.FAST_FORWARD_IMAGE_PATH));
		fastForwardButton.setFitWidth(PokemonTypeGameApplication.OPTION_BUTTON_SIZE);
		muteButton.imageProperty().bind(Bindings.createObjectBinding(() -> {
			if (PokemonTypeGameApplication.mutedProperty.get())
				return new Image(PokemonTypeGameApplication.MUTE_IMAGE_PATH);
			else
				return new Image(PokemonTypeGameApplication.UNMUTE_IMAGE_PATH);
		}, PokemonTypeGameApplication.mutedProperty));
		muteButton.setFitWidth(PokemonTypeGameApplication.OPTION_BUTTON_SIZE);
	}

	/**
	 * Toggles the muted property and stores the new value in the userdata file.
	 */
	public void toggleMute(MouseEvent e) {
		PokemonTypeGameApplication.toggleMute();
	}
	
	
	/**
	 * Advances to the next song
	 */
	public void nextSong(MouseEvent e) {
		PokemonTypeGameApplication.nextSong();
	}
	
	
	/**
	 * Rotates to the next selection of background gradient
	 */
	public void rotateBackgroundGradient(MouseEvent e) {
		PokemonTypeGameApplication.rotateBackgroundGradient();
	}
}
