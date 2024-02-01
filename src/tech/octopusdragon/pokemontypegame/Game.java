package tech.octopusdragon.pokemontypegame;

/**
 * An interface that should be implemented to make a game in which the player
 * guesses a supereffective type against a defending type or Pokémon.
 * @author Alex Gill
 *
 */
public interface Game {
	
	/**
	 * @return the number of choices the user has for attacking types
	 */
	public int numChoices();

	/**
	 * Advances to the next round, generating a new set of types.
	 */
	public void nextRound();
	
	/**
	 * Submits a guess.
	 * @param guess The type to guess is supereffective against the defending
	 * 		  type or Pokémon
	 * @return Whether the guess was correct
	 */
	public boolean makeGuess(Type guess);
	
	
	/**
	 * @return the name of the defending Pokémon or type
	 */
	public String getDefendingName();
	
	
	/**
	 * @return the filename of the image of the defending Pokémon or type
	 */
	public String getDefendingImageFilename();
	
	
	/**
	 * Returns the attacking types.
	 * @return The attacking types
	 */
	public Type[] getAttackingTypes();
	
	
	/**
	 * Returns the attacking type at the given index.
	 * @param index The index
	 * @return The attacking type
	 */
	public Type getAttackingType(int index);
	
	
	/**
	 * Returns the current state of the game.
	 * @return The current state of the game
	 */
	public GameState getState();
	
	
	/**
	 * Returns the number of correct guesses in a row.
	 * @return The number of correct guesses in a row
	 */
	public int getStreak();
	
	
	/**
	 * Returns an informational message depending on the state of the game. If
	 * the user has not made a guess yet, returns a prompt. If the user guessed
	 * correctly, congratulates the user. If the user did not guess correctly,
	 * corrects the user.
	 * @return The message
	 */
	public String message();
}