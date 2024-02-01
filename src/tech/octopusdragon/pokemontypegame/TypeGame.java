package tech.octopusdragon.pokemontypegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Contains the flow and logic for a game in which the player guesses the
 * supereffective type against a defending type among a selection of types.
 * @author Alex Gill
 *
 */
public class TypeGame implements Game {
	
	// --- Static constants ---
	public static final int NUM_CHOICES = 3;	// Number of type choices
	public int numChoices() { return NUM_CHOICES; }
	
	
	// --- Instance variables ---
	private Type defendingType;		// Current defending type
	private Type[] attackingTypes;	// Current attacking types
	private Type guess;				// Holds the player's guess
	private GameState state;		// Current state of the game
	private int streak;				// Number of correct guesses in a row
	
	/**
	 * Starts a new game
	 */
	public TypeGame() {
		
		attackingTypes = new Type[NUM_CHOICES];
		streak = 0;
		
		// Generate initial types for the first round
		nextRound();
	}
	
	
	@Override
	public void nextRound() {
		
		// Generate a defending type
		generateDefendingType();
		
		// Generate attacking types
		generateAttackingTypes();
		
		// User has not made guess yet
		guess = null;
		state = GameState.HAS_NOT_MADE_GUESS;
	}
	
	
	@Override
	public boolean makeGuess(Type guess) {
		
		// Determine whether the user has guessed correctly
		boolean correct = false;
		if (defendingType.getWeaknesses().contains(guess)) {
			correct = true;
		}
		
		// User has made guess and update the game state
		this.guess = guess;
		if (correct)
			state = GameState.CORRECT_GUESS;
		else
			state = GameState.INCORRECT_GUESS;
		
		// Increment the streak if the guess was correct
		if (correct) {
			streak++;
		}
		// Otherwise, start over at 0
		else {
			streak = 0;
		}
		
		// Return whether the guess was correct
		return correct;
	}
	
	
	@Override
	public String getDefendingName() {
		return defendingType.getName();
	}
	
	
	@Override
	public String getDefendingImageFilename() {
		return defendingType.getImageFilename();
	}
	
	
	/**
	 * Generates a new defending type.
	 */
	private void generateDefendingType() {

		// Create Random object for generating random values
		Random rand = new Random();
		
		// Randomly select the defending type
		defendingType = Type.values()[rand.nextInt(Type.values().length)];
	}
	
	
	/**
	 * Generates attacking types based on the current defending type. Picks one
	 * supereffective type and two non-supereffective types and randomizes the
	 * order of them.
	 */
	private void generateAttackingTypes() {

		// Create Random object for generating random values
		Random rand = new Random();
		
		// Create temporary list of attacking types in order to shuffle them
		List<Type> attackingTypesList = new ArrayList<Type>();
		
		// Randomly select one weakness to be an attacking type
		Type correctAnswer = defendingType.getWeaknesses().get(
				rand.nextInt(defendingType.getWeaknesses().size()));
		attackingTypesList.add(correctAnswer);
		
		// Randomly select the rest of the attacking types as non-weaknesses
		List<Type> incorrectAsnwers = new ArrayList<Type>(
				Arrays.asList(Type.values()));
		for (Type weakness: defendingType.getWeaknesses())
			incorrectAsnwers.remove(weakness);
		for (int i = 1; i < NUM_CHOICES; i++)
			attackingTypesList.add(incorrectAsnwers.remove(
					rand.nextInt(incorrectAsnwers.size())));
		
		// Shuffle the list so the weakness is not always first
		Collections.shuffle(attackingTypesList);
		
		// Set the values of the attacking types array
		for (int i = 0; i < NUM_CHOICES; i++)
			attackingTypes[i] = attackingTypesList.get(i);
	}
	
	
	/**
	 * Returns the defending type.
	 * @return The defending type
	 */
	public Type getDefendingType() {
		return defendingType;
	}
	
	
	@Override
	public Type[] getAttackingTypes() {
		return attackingTypes.clone();
	}
	
	
	@Override
	public Type getAttackingType(int index) {
		return attackingTypes[index];
	}
	
	
	@Override
	public GameState getState() {
		return state;
	}
	
	
	@Override
	public int getStreak() {
		return streak;
	}
	
	
	@Override
	public String message() {
		StringBuilder sb = new StringBuilder();
		
		// If the user has not made a guess yet, write a prompt.
		if (state == GameState.HAS_NOT_MADE_GUESS) {
			sb.append("Select the type that is supereffective against the "
					+ "opponent's Pokémon.");
		}
		
		// If the user guessed correctly, congratulate the user.
		else if (state == GameState.CORRECT_GUESS) {
			sb.append(String.format("Correct! %s is weak to %s.",
					defendingType,
					guess));
			
			// Include other weaknesses.
			List<Type> otherWeaknesses = defendingType.getWeaknesses();
			otherWeaknesses.remove(guess);
			if (otherWeaknesses.size() > 0) {
				sb.append(String.format(" %s is also weak to ",
						defendingType));
				for (int i = 0; i < otherWeaknesses.size() - 1; i++) {
					sb.append(otherWeaknesses.get(i));
					if (otherWeaknesses.size() > 2)
						sb.append(",");
					sb.append(" ");
				}
				if (otherWeaknesses.size() > 1)
					sb.append("and ");
				sb.append(String.format("%s.",
						otherWeaknesses.get(otherWeaknesses.size() - 1)));
			}
		}
		
		// If the user did not guess correctly, correct the user.
		else if (state == GameState.INCORRECT_GUESS) {
			sb.append(String.format("Incorrect. %s is weak to ",
					defendingType));
			List<Type> weaknesses = defendingType.getWeaknesses();
			for (int i = 0; i < weaknesses.size() - 1; i++) {
				sb.append(weaknesses.get(i));
				if (weaknesses.size() > 2)
					sb.append(",");
				sb.append(" ");
			}
			if (weaknesses.size() > 1)
				sb.append("and ");
			sb.append(String.format("%s.",
					weaknesses.get(weaknesses.size() - 1)));
		}
		
		return sb.toString();
	}
}
