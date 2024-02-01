package tech.octopusdragon.pokemontypegame.gui;

import java.util.ArrayList;
import java.util.List;

import tech.octopusdragon.pokemontypegame.GameState;
import tech.octopusdragon.pokemontypegame.PokemonGame;
import tech.octopusdragon.pokemontypegame.Type;
import tech.octopusdragon.pokemontypegame.TypeGame;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Methods to perform actions in the type game application.
 * @author Alex Gill
 *
 */
public class GameScreenController {	// USE GRIDPANE- IT HAS PERCENT-WIDTH PERCENT-HEIGHT PROPERTIES DEPENDING ON IF ROW OR COLUMN
	
	// --- Variables ---
	Label[] attackingTypeLabels;		// All attacking type labels
	Pane[] attackingTypeWrapperPanes;	// All attacking type wrapper panes
	TypeButton[] attackingTypeButtons;	// All attacking type buttons
	boolean mediumOptionsCondensed;		// Whether medium options is condensed
	
	// --- GUI components ---
	@FXML public VBox optionsBox;
	@FXML public Label streakLabel;
	
	@FXML public GridPane mainContent;
	
	@FXML public GridPane mediumOptionsTopGridPane;
	@FXML public GridPane mediumOptionsBottomGridPane;
	public List<Button> genButtonList;
	@FXML public Button gen1Button;
	@FXML public Button gen2Button;
	@FXML public Button gen3Button;
	@FXML public Button gen4Button;
	@FXML public Button gen5Button;
	@FXML public Button gen6Button;
	@FXML public Button gen7Button;
	@FXML public CheckBox megaCheckBox;
	@FXML public CheckBox regionalCheckBox;
	
	@FXML public TitledPane defendingSide;
	@FXML public HBox defendingInfoBox;
	@FXML public Label defendingLabel;
	@FXML public ImageView defendingPokemonType1ImageView;
	@FXML public ImageView defendingPokemonType2ImageView;
	@FXML public Pane defendingWrapperPane;
	@FXML public ImageView defendingImageView;
	
	@FXML public TitledPane attackingSide;
	@FXML public HBox attackingSideBox;
	@FXML public VBox attackingType1;
	@FXML public Label attackingType1Label;
	@FXML public Pane attackingType1WrapperPane;
	public TypeButton attackingType1Button;
	@FXML public VBox attackingType2;
	@FXML public Label attackingType2Label;
	@FXML public Pane attackingType2WrapperPane;
	public TypeButton attackingType2Button;
	@FXML public VBox attackingType3;
	@FXML public Label attackingType3Label;
	@FXML public Pane attackingType3WrapperPane;
	public TypeButton attackingType3Button;
	@FXML public VBox attackingType4;
	@FXML public Label attackingType4Label;
	@FXML public Pane attackingType4WrapperPane;
	public TypeButton attackingType4Button;
	
	@FXML public Label messageLabel;
	
	@FXML public HBox nextButtonWrapperPane;
	@FXML public Button nextButton;
	
	
	@FXML
	public void initialize() {
		buildScene();
		updateScene();
		
		// Resize medium options grid pane according to window dimensions
		mediumOptionsTopGridPane.setMaxWidth(PokemonTypeGameApplication.GUI_PREF_WIDTH * 1.5);
		mediumOptionsBottomGridPane.setMaxWidth(PokemonTypeGameApplication.GUI_PREF_WIDTH * 1.5);
		Platform.runLater(() -> {
			ReadOnlyDoubleProperty sceneWidthProperty = defendingSide.getScene().widthProperty();
			ReadOnlyDoubleProperty sceneHeightProperty = defendingSide.getScene().heightProperty();
			
			if (PokemonTypeGameApplication.gameMode == GameMode.MEDIUM) { 
				condenseMediumOptions(sceneWidthProperty.get());
				
				sceneWidthProperty.addListener((observable, oldValue, newValue) -> {
					condenseMediumOptions(newValue.intValue());
				});
			}
			
			// Change spacing based on height
			optionsBox.spacingProperty().bind(Bindings.createDoubleBinding(() -> {
				double sceneHeight = sceneHeightProperty.get();
				double newFitHeight = sceneHeight / 100;
				return newFitHeight;
			}, sceneHeightProperty));
		});
		
		// Finish setting up gen buttons
		genButtonList = new ArrayList<Button>();
		genButtonList.add(gen1Button);
		genButtonList.add(gen2Button);
		genButtonList.add(gen3Button);
		genButtonList.add(gen4Button);
		genButtonList.add(gen5Button);
		genButtonList.add(gen6Button);
		genButtonList.add(gen7Button);
		Platform.runLater(() -> {
			for (int i = 0; i < genButtonList.size(); i++) {
				Button genButton = genButtonList.get(i);
				PokemonTypeGameApplication.mediumIncludeGenProperty[i].addListener((observable, oldValue, newValue) -> {
					if (newValue == true) {
						genButton.pseudoClassStateChanged(PokemonTypeGameApplication.GEN_DISABLED_PSEUDO_CLASS, false);
					}
					else {
						genButton.pseudoClassStateChanged(PokemonTypeGameApplication.GEN_DISABLED_PSEUDO_CLASS, true);
					}
				});
			}
			
			// Initial states
			for (int i = 0; i < genButtonList.size(); i++) {
				if (PokemonTypeGameApplication.mediumIncludeGenProperty[i].get() == true) {
					genButtonList.get(i).pseudoClassStateChanged(PokemonTypeGameApplication.GEN_DISABLED_PSEUDO_CLASS, false);
				}
				else {
					genButtonList.get(i).pseudoClassStateChanged(PokemonTypeGameApplication.GEN_DISABLED_PSEUDO_CLASS, true);
				}
			}
			megaCheckBox.setSelected(PokemonTypeGameApplication.mediumIncludeMegaProperty.get());
			regionalCheckBox.setSelected(PokemonTypeGameApplication.mediumIncludeRegionalProperty.get());
		});
		
		
		
		// Resize GUI components on stage resize
		ChangeListener<? super Object> listener = new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				refreshLayout();
			}
		};
		
		Platform.runLater(() -> {
			defendingImageView.fitWidthProperty().bind(defendingImageView.getScene().widthProperty());
			((Stage)defendingImageView.getScene().getWindow()).widthProperty().addListener(listener);
			((Stage)defendingImageView.getScene().getWindow()).heightProperty().addListener(listener);
			mainContent.layoutBoundsProperty().addListener(listener);
		});
		defendingImageView.fitHeightProperty().bind(defendingWrapperPane.heightProperty());
		


		attackingSideBox.spacingProperty().bind(attackingSideBox.heightProperty().divide(8));
		for (int i = 0; i < attackingTypeButtons.length; i++) {
			Pane attackingTypePane = attackingTypeWrapperPanes[i];
			attackingTypeButtons[i].setFitWidth(Double.MAX_VALUE);
			attackingSide.heightProperty().addListener((obs, oldVal, newVa) -> {
				attackingTypePane.layout();
				attackingTypePane.autosize();
			});
		}
		Platform.runLater(() -> {
			for (int i = 0; i < attackingTypeButtons.length; i++) {
				attackingTypeButtons[i].fitHeightProperty().bind(attackingTypeWrapperPanes[i].heightProperty());
				attackingTypeButtons[i].layoutXProperty().bind(attackingTypeButtons[i].fitHeightProperty().divide(2));
				attackingTypeButtons[i].layoutYProperty().bind(attackingTypeButtons[i].fitHeightProperty().divide(2));
			}
		});
		
		nextButton.prefHeightProperty().bind(nextButtonWrapperPane.heightProperty());
		nextButton.prefWidthProperty().bind(nextButtonWrapperPane.heightProperty().multiply(1.5));
		Platform.runLater(() -> {
			nextButton.setStyle(String.format("-fx-font-size: %fpx", nextButton.getHeight() / 2));
		});
	}
	
	
	/**
	 * Updates GUI components that need to change on resize
	 */
	public void refreshLayout() {
		Platform.runLater(() -> {
			defendingWrapperPane.layout();
			defendingWrapperPane.autosize();
			for (int i = 0; i < attackingTypeButtons.length; i++) {
				attackingTypeWrapperPanes[i].layout();
				attackingTypeWrapperPanes[i].autosize();
			}
			nextButton.setStyle(String.format("-fx-font-size: %fpx", nextButton.getHeight() / 2));
		});
	}
	

	/**
	 * Switches to menu scene
	 */
	public void switchToMenuScene(ActionEvent e) {
		PokemonTypeGameApplication.switchToScene(PokemonTypeGameApplication.MENU_FXML_PATH);
	}
	
	
	/**
	 * Submits a guess for the round and updates the scene
	 * @param guess The type to submit as the guess
	 */
	public void makeGuess(Type guess) {
		PokemonTypeGameApplication.makeGuess(guess);
		
		// Change the color of the message based on whether the guess was
		// correct or incorrect
		if (PokemonTypeGameApplication.game.getState() == GameState.CORRECT_GUESS) {
			messageLabel.pseudoClassStateChanged(
					PseudoClass.getPseudoClass("correct"), true);
		}
		else if (PokemonTypeGameApplication.game.getState() == GameState.INCORRECT_GUESS) {
			messageLabel.pseudoClassStateChanged(
					PseudoClass.getPseudoClass("incorrect"), true);
		}
		
		// Enable/disable buttons
		for (TypeButton attackingTypeButton : attackingTypeButtons) {
			attackingTypeButton.setDisable(true);
		}
		nextButton.setDisable(false);
		
		updateScene();
	}
	
	
	/**
	 * Moves to the next round and updates the scene
	 */
	public void nextRound(ActionEvent e) {
		
		// If medium mode, make sure at least one region is selected
		if (PokemonTypeGameApplication.gameMode.equals(GameMode.MEDIUM) && !atLeastOneGenerationSelected()) {
			messageLabel.pseudoClassStateChanged(
					PseudoClass.getPseudoClass("correct"), false);
			messageLabel.pseudoClassStateChanged(
					PseudoClass.getPseudoClass("incorrect"), true);
			messageLabel.setText("Please select at least one region to include.");
			return;
		}
		
		PokemonTypeGameApplication.nextRound();
		
		// Change the color of the message to default
		if (PokemonTypeGameApplication.game.getState() == GameState.HAS_NOT_MADE_GUESS) {
			messageLabel.pseudoClassStateChanged(
					PseudoClass.getPseudoClass("correct"), false);
			messageLabel.pseudoClassStateChanged(
					PseudoClass.getPseudoClass("incorrect"), false);
		}
		
		// Disable/enable buttons
		for (TypeButton attackingTypeButton : attackingTypeButtons) {
			attackingTypeButton.setDisable(false);
		}
		nextButton.setDisable(true);
		
		updateScene();
	}

	
	/**
	 * Toggles generation for medium Pokemon selection
	 * @param generation The generation
	 */
	public void toggleIncludeGen(ActionEvent e) {
		int generation = genButtonList.indexOf(((Button)e.getSource()));
		
		// Prevent changing if changing to false and there aren't at least two
		// generations selected
		if (PokemonTypeGameApplication.mediumIncludeGenProperty[generation].get() == true && !atLeastTwoGenerationsSelected()) {
			return;
		}
		
		PokemonTypeGameApplication.toggleIncludeGen(generation);
	}
	

	/**
	 * Toggles mega evolutions for medium Pokemon selection
	 */
	public void toggleIncludeMega(ActionEvent e) {
		PokemonTypeGameApplication.toggleIncludeMega();
	}
	
	
	/**
	 * Toggles regional forms for medium Pokemon selection
	 */
	public void toggleIncludeRegional() {
		PokemonTypeGameApplication.toggleIncludeRegional();
	}
	
	
	
	/**
	 * @return Whether at least one generation button is selected
	 */
	public boolean atLeastOneGenerationSelected() {
		boolean atLeastOneSelected = false;
		for (BooleanProperty genSelectedProperty : PokemonTypeGameApplication.mediumIncludeGenProperty) {
			if (genSelectedProperty.get() == true) {
				atLeastOneSelected = true;
				break;
			}
		}
		return atLeastOneSelected;
	}
	
	
	/**
	 * @return Whether at least two generations buttons are selected
	 */
	public boolean atLeastTwoGenerationsSelected() {
		int numSelected = 0;
		for (BooleanProperty genSelectedProperty : PokemonTypeGameApplication.mediumIncludeGenProperty) {
			if (genSelectedProperty.get() == true) {
				numSelected++;
			}
		}
		return numSelected >= 2;
	}
	
	
	
	/**
	 * Builds certain aspects of the scene that either cannot be created in
	 * Scene Builder or differ depending on the game mode.
	 */
	private void buildScene() {
		
		// Build medium options box
		switch (PokemonTypeGameApplication.gameMode) {
		case EASY:
		case HARD:
			optionsBox.getChildren().remove(mediumOptionsTopGridPane);
			optionsBox.getChildren().remove(mediumOptionsBottomGridPane);
			break;
		case MEDIUM:
			break;
		}
		
		// Build defending Pokemon type icons
		defendingPokemonType1ImageView.setFitWidth(PokemonTypeGameApplication.POKEMON_TYPE_ICON_SIZE);
		defendingPokemonType2ImageView.setFitWidth(PokemonTypeGameApplication.POKEMON_TYPE_ICON_SIZE);
		switch (PokemonTypeGameApplication.gameMode) {
		case EASY:
		case HARD:
			defendingInfoBox.getChildren().removeAll(
					defendingPokemonType1ImageView,
					defendingPokemonType2ImageView);
			break;
		case MEDIUM:
			break;
		}
		
		// Build attacking type buttons
		attackingType1Button = new TypeButton();
		attackingType1Button.setOnMouseClicked(new TypeButtonClickHandler());
		attackingType1WrapperPane.getChildren().add(attackingType1Button);
		attackingType2Button = new TypeButton();
		attackingType2Button.setOnMouseClicked(new TypeButtonClickHandler());
		attackingType2WrapperPane.getChildren().add(attackingType2Button);
		attackingType3Button = new TypeButton();
		attackingType3Button.setOnMouseClicked(new TypeButtonClickHandler());
		attackingType3WrapperPane.getChildren().add(attackingType3Button);
		attackingType4Button = new TypeButton();
		attackingType4Button.setOnMouseClicked(new TypeButtonClickHandler());
		attackingType4WrapperPane.getChildren().add(attackingType4Button);
		attackingTypeLabels = new Label[] {attackingType1Label, attackingType2Label, attackingType3Label, attackingType4Label};
		attackingTypeWrapperPanes = new Pane[] {attackingType1WrapperPane, attackingType2WrapperPane, attackingType3WrapperPane, attackingType4WrapperPane};
		attackingTypeButtons = new TypeButton[] {attackingType1Button, attackingType2Button, attackingType3Button, attackingType4Button};
		if (PokemonTypeGameApplication.game.getClass().equals(TypeGame.class)) {
			attackingType4.getChildren().clear();
			attackingSideBox.getChildren().remove(attackingType4);
		}
		switch (PokemonTypeGameApplication.gameMode) {
		case EASY:
			mainContent.getRowConstraints().get(0).setPercentHeight(mainContent.getRowConstraints().get(0).getPercentHeight() - 5);
			mainContent.getRowConstraints().get(2).setPercentHeight(mainContent.getRowConstraints().get(2).getPercentHeight() + 5);
			break;
		case MEDIUM:
		case HARD:
			attackingType1.getChildren().remove(attackingType1Label);
			attackingType2.getChildren().remove(attackingType2Label);
			attackingType3.getChildren().remove(attackingType3Label);
			attackingType4.getChildren().remove(attackingType4Label);
			break;
		}
		attackingSide.setMaxHeight(8 * mainContent.getRowConstraints().get(2).getPercentHeight());
		
		// Set text for defending and attacking side titles
		if (PokemonTypeGameApplication.game.getClass().equals(TypeGame.class)) {
			defendingSide.setText("Opponent Pokémon's Type");
		}
		else if (PokemonTypeGameApplication.game.getClass().equals(PokemonGame.class)) {
			defendingSide.setText("Opponent Pokémon");
		}
		attackingSide.setText("Your Pokémon's Attacks' Types");
		
		// Start off with next button disabled
		nextButton.setDisable(true);
	}
	
	
	/**
	 * Updates all elements on the scene to reflect the current round
	 */
	private void updateScene() {
		
		// Update the streak label
		streakLabel.setText(String.format("%d\t%d",
				PokemonTypeGameApplication.game.getStreak(),
				PokemonTypeGameApplication.userdata.readHighestStreak(PokemonTypeGameApplication.gameMode)));
		
		// Update defending side
		defendingLabel.setText(PokemonTypeGameApplication.game.getDefendingName().toUpperCase());
		switch (PokemonTypeGameApplication.gameMode) {
		case EASY:
		case HARD:
			break;
		case MEDIUM:
			Type[] defendingPokemonTypes = ((PokemonGame)PokemonTypeGameApplication.game).getDefendingPokemon().getTypes();
			defendingPokemonType1ImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(PokemonTypeGameApplication.TYPE_IMAGES_DIR + defendingPokemonTypes[0].getImageFilename())));
			if (defendingPokemonTypes.length == 2) {
				defendingPokemonType2ImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(PokemonTypeGameApplication.TYPE_IMAGES_DIR + defendingPokemonTypes[1].getImageFilename())));
				if (!defendingInfoBox.getChildren().contains(defendingPokemonType2ImageView))
					defendingInfoBox.getChildren().add(defendingPokemonType2ImageView);
			}
			else {
				if (defendingInfoBox.getChildren().contains(defendingPokemonType2ImageView))
					defendingInfoBox.getChildren().remove(defendingPokemonType2ImageView);
			}
			break;
		}
		defendingImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(PokemonTypeGameApplication.defendingImagePath())));
		
		// Update message label
		messageLabel.setText(PokemonTypeGameApplication.game.message());
		
		// Update attacking side
		switch (PokemonTypeGameApplication.gameMode) {
		case EASY:
			for (int i = 0; i < PokemonTypeGameApplication.game.numChoices(); i++) {
				attackingTypeLabels[i].setText(PokemonTypeGameApplication.game.getAttackingType(i).getName().toUpperCase());
			}
		case MEDIUM:
		case HARD: 
			for (int i = 0; i < PokemonTypeGameApplication.game.numChoices(); i++) {
				attackingTypeButtons[i].changeType(PokemonTypeGameApplication.game.getAttackingType(i));;
			}
			break;
		}
	}
	
	
	/**
	 * Condenses the medium options menu
	 * @param sceneWidth the width of the scene
	 */
	private void condenseMediumOptions(double sceneWidth) {
		if (mediumOptionsCondensed && sceneWidth >= PokemonTypeGameApplication.GUI_PREF_WIDTH) {
			mediumOptionsTopGridPane.getChildren().removeAll(gen6Button, gen7Button, megaCheckBox, regionalCheckBox);
			if (optionsBox.getChildren().contains(mediumOptionsBottomGridPane))
				optionsBox.getChildren().remove(mediumOptionsBottomGridPane);
			mediumOptionsTopGridPane.add(gen6Button, 5, 0);
			mediumOptionsTopGridPane.add(gen7Button, 6, 0);
			mediumOptionsTopGridPane.add(megaCheckBox, 7, 0);
			mediumOptionsTopGridPane.add(regionalCheckBox, 8, 0);
			mediumOptionsCondensed = false;
		}
		else if (!mediumOptionsCondensed && sceneWidth < PokemonTypeGameApplication.GUI_PREF_WIDTH) {
			mediumOptionsBottomGridPane.getChildren().removeAll(gen6Button, gen7Button, megaCheckBox, regionalCheckBox);
			if (!optionsBox.getChildren().contains(mediumOptionsBottomGridPane))
					optionsBox.getChildren().add(mediumOptionsBottomGridPane);
			mediumOptionsBottomGridPane.add(gen6Button, 0, 0);
			mediumOptionsBottomGridPane.add(gen7Button, 1, 0);
			mediumOptionsBottomGridPane.add(megaCheckBox, 2, 0);
			mediumOptionsBottomGridPane.add(regionalCheckBox, 3, 0);
			mediumOptionsCondensed = true;
		}
	}
	
	
	/**
	 * Determines what happens when the user clicks a type button. Should submit
	 * a guess.
	 * @author Alex Gill
	 *
	 */
	public class TypeButtonClickHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (PokemonTypeGameApplication.game.getState() == GameState.HAS_NOT_MADE_GUESS) {
				makeGuess(((TypeButton)event.getSource()).getType());
				((Node)event.getSource()).getScene().setCursor(Cursor.DEFAULT);
			}
		}
	}
}
