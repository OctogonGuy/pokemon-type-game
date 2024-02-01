package tech.octopusdragon.pokemontypegame.gui;

/**
 * Colors of potential background gradients.
 * @author Alex Gill
 *
 */
public enum BackgroundGradient {
	
	ORANGE("#e79c29", "#ffaa5B", "#fff2e8"),
	GREEN("#5ab529", "#67c236", "#e2fcd4"),
	RED("#ef3938", "#ff493b", "#ffdede"),
	BLUE("#2a94ce", "#54a8d6", "#d0e8f5"),
	PINK("#f2acc8", "#f5bfd4", "#f9f6f7"),
	LAVENDAR("#bb8fdb", "#c09adb", "#f0edf3"),
	GRAY("#808080", "#989898", "#f8f8f8"),
	BROWN("#856021", "#a88447", "#ede1cc"),
	CYAN("#01f7f7", "#4af0f0", "#e7f9f9"),
	GOLD("#f8c800", "#f2ce3d", "#f9f4e2");
	
	// --- Constants ---
	private static final int STOP_1 = 2;
	private static final int STOP_2 = 3;
	
	
	// --- Variables ---
	private String mainColor;	// The color shown on change color button
	private String primaryColor;	// The color of bars on top and bottom
	private String secondaryColor;	// The color in the middle
	private String gradient;	// The actual background gradient

	
	/**
	 * Instantiates a new background gradient
	 * @param mainColor The color of the change color button
	 * @param primaryColor The color of the bars on top and bottom
	 * @param secondaryColor The color shown in the middle
	 */
	private BackgroundGradient(String mainColor, String primaryColor, String secondaryColor) {
		this.mainColor = mainColor;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		gradient = String.format("to bottom, %s %d%%, %s %d%%, %s %d%%, %s %d%%",
				primaryColor, STOP_1,
				secondaryColor, STOP_2,
				secondaryColor, 100 - STOP_2,
				primaryColor, 100 - STOP_1);
	}
	
	
	/**
	 * @return The color to be shown on change color button
	 */
	public String getMainColor() {
		return mainColor;
	}
	
	
	/**
	 * @return The color of bars on top and bottom
	 */
	public String getPrimaryColor() {
		return primaryColor;
	}
	
	
	/**
	 * @return The color shown in the middle
	 */
	public String getSecondaryColor() {
		return secondaryColor;
	}
	
	
	/**
	 * @return Returns the gradient
	 */
	public String getGradient() {
		return gradient;
	}
}
