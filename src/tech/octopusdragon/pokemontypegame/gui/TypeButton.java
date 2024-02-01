package tech.octopusdragon.pokemontypegame.gui;

import tech.octopusdragon.pokemontypegame.Type;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A button with the image of a Pokémon type. It can be clicked in order to
 * perform an action.
 * @author Alex Gill
 *
 */
public class TypeButton extends Group {
	
	// --- Constants ---
	// The ratio of the button frame's radius to the button's radius
	private static final double FRAME_BUTTON_RATIO = 0.8;
	
	
	// --- Variables ---
	private Type type;			// The type associated with this button
	private boolean disabled;	// Whether the button is disabled
	
	
	// --- Properties ---
	private DoubleProperty fitWidthProperty;
	private DoubleProperty fitHeightProperty;
	
	
	// --- GUI Components ---
	private ImageView typeImageView;	// Image view showing type icon
	private InnerShadow shadow;			// Image view shadow
	private Circle outerFrame;
	private InnerShadow outerFrameShadow;
	private Circle innerFrame;
	private InnerShadow innerFrameShadow;
	
	
	/**
	 * Constructs a button with no image.
	 * @param size The width and height of the button
	 */
	public TypeButton() {
		super();
		
		// Set variables
		disabled = false;
		fitWidthProperty = new SimpleDoubleProperty();
		fitHeightProperty = new SimpleDoubleProperty();
		
		
		// Create outer ring of button frame
		outerFrame = new Circle();
		outerFrame.setFill(Color.GRAY);
		this.getChildren().add(outerFrame);
		
		// Create an inner shadow to show depth.
        outerFrameShadow = new InnerShadow();
        outerFrameShadow.setColor(new Color(0.0, 0.0, 0.0, 0.33));
        outerFrame.setEffect(outerFrameShadow);
        
		// Create outer ring of button frame
		innerFrame = new Circle();
		innerFrame.setFill(Color.GRAY);
		this.getChildren().add(innerFrame);
		
		// Create an inner shadow to show depth.
        innerFrameShadow = new InnerShadow();
        innerFrameShadow.setColor(new Color(0.0, 0.0, 0.0, 0.33));
        innerFrame.setEffect(innerFrameShadow);
		
		// Create image view that shows type
		typeImageView = new ImageView();
		typeImageView.setPreserveRatio(true);
		this.getChildren().add(typeImageView);
        
		// Create an inner shadow to show depth and pressing when clicked.
        shadow = new InnerShadow();
		shadow.setColor(new Color(0.0, 0.0, 0.0, 0.33));
		typeImageView.setEffect(shadow);
		
		
		// Change sizes on width or height change
		fitWidthProperty().addListener((observable, oldValue, newValue) -> {
			resize();
		});
		fitHeightProperty().addListener((observable, oldValue, newValue) -> {
			resize();
		});
		
		
		// Change the cursor to a hand when hovered over.
		this.setOnMouseEntered(event -> {
			if (!disabled)
				this.getScene().setCursor(Cursor.HAND);
		});
		this.setOnMouseExited(event -> {
			if (!disabled)
				this.getScene().setCursor(Cursor.DEFAULT);
		});
	}
	
	
	/**
	 * Returns the type associated with this button.
	 * @return The type
	 */
	public Type getType() {
		return type;
	}
	
	
	/**
	 * Enables the button and allows it to be clicked.
	 */
	public void enable() {
		disabled = false;
	}
	
	
	/**
	 * Disables the button and does not allow it to be clicked.
	 */
	public void disable() {
		disabled = true;
	}
	
	
	/**
	 * Sets the type associated with this button and updates its image.
	 * @param type The type
	 */
	public void changeType(Type type) {
		this.type = type;
		typeImageView.setImage(new Image(PokemonTypeGameApplication.TYPE_IMAGES_DIR + type.getImageFilename()));
	}
	
	
	
	
	/**
	 * @return the fit width
	 */
	public double getFitWidth() {
		return fitWidthProperty.get();
	}
	
	
	/**
	 * @param value the new fit width
	 */
	public void setFitWidth(double value) {
		fitWidthProperty.set(value);
	}
	
	
	/**
	 * @return the fit width property
	 * @return
	 */
	public DoubleProperty fitWidthProperty() {
		return fitWidthProperty;
	}
	
	
	
	
	/**
	 * @return the fit height
	 */
	public double getFitHeight() {
		return fitHeightProperty.get();
	}
	
	
	/**
	 * @param value the new fit height
	 */
	public void setFitHeight(double value) {
		fitHeightProperty.set(value);
	}
	
	
	/**
	 * @return the fit height property
	 * @return
	 */
	public DoubleProperty fitHeightProperty() {
		return fitHeightProperty;
	}
	
	
	/**
	 * Resizes the component
	 */
	private void resize() {
		double size = fitWidthProperty.get() < fitHeightProperty.get() ? fitWidthProperty.get() : fitHeightProperty.get();
		
		// Change outer frame size
		outerFrame.setRadius(size / 2);
		
		// Change outer frame shadow
        outerFrameShadow.setOffsetX(size / 6);
        outerFrameShadow.setOffsetY(-size / 6);
        outerFrameShadow.setRadius(size / 4);
		
		// Change inner frame size
		innerFrame.setRadius((size + size * FRAME_BUTTON_RATIO) / 2 / 2);
		
		// Change inner frame shadow
        innerFrameShadow.setOffsetX(-size / 6);
        innerFrameShadow.setOffsetY(size / 6);
        innerFrameShadow.setRadius(size / 4);
		
		// Change image view coordinates
		typeImageView.setX(-size * FRAME_BUTTON_RATIO / 2);
		typeImageView.setY(-size * FRAME_BUTTON_RATIO / 2);
		typeImageView.setFitWidth(size * FRAME_BUTTON_RATIO);
		typeImageView.setFitHeight(size * FRAME_BUTTON_RATIO);
		
		// Change image shadow
		shadow.setOffsetX(size / 6);
		shadow.setOffsetY(-size / 6);
		shadow.setRadius(size / 4);
		
		
		// Flip shadow to opposite direction when clicked on.
		this.setOnMousePressed(event -> {
			if (!disabled) {
				shadow.setOffsetX(-size / 6);
				shadow.setOffsetY(size / 6);
			}
		});
		this.setOnMouseReleased(event -> {
			if (!disabled) {
				shadow.setOffsetX(size / 6);
				shadow.setOffsetY(-size / 6);
			}
		});
	}
}
