package tech.octopusdragon.pokemontypegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Contains the flow and logic for a game in which the player guesses the
 * supereffective type against a defending Pokémon among a selection of types.
 * @author Alex Gill
 *
 */
public class PokemonGame implements Game {
	
	// --- Static constants ---
	public static final int NUM_CHOICES = 4;	// Number of type choices
	public int numChoices() { return NUM_CHOICES; }
	
	
	// --- Static variables ---
	public static Map<Integer, List<Pokemon>> allPokemon;	// All Pokemon
	
	
	// --- Instance variables ---
	private Pokemon defendingPokemon;	// Current defending Pokémon
	private Type[] attackingTypes;		// Current attacking types
	private Type guess;					// Holds the player's guess
	private Type answer;				// Holds the correct answer
	private GameState state;			// Current state of the game
	private int streak;					// Number of correct guesses in a row
	
	/**
	 * Starts a new game
	 */
	public PokemonGame() {
		
		// Instantiate a map of all Pokemon
		allPokemon = new HashMap<>();
		for (Pokemon pokemon : Pokemon.getAll()) {
			if (allPokemon.get(pokemon.getNumber()) == null) {
				allPokemon.put(pokemon.getNumber(), new ArrayList<Pokemon>());
			}
			allPokemon.get(pokemon.getNumber()).add(pokemon);
		}
		
		attackingTypes = new Type[NUM_CHOICES];
		streak = 0;
		
		// Generate initial types for the first round
		nextRound();
	}
	
	
	@Override
	public void nextRound() {
		
		// Generate a defending type
		generateDefendingPokemon();
		
		// Generate attacking types
		generateAttackingTypes();
		
		// User has not made guess yet
		guess = null;
		state = GameState.HAS_NOT_MADE_GUESS;
	}
	
	
	/**
	 * Moves to the next round but with restrictions on the defending Pokemon
	 * @param includeMega Whether to include mega evolutions
	 * @param includeRegional Whether to include regional forms
	 * @param generations The generations to include
	 */
	public void nextRound(boolean includeMega, boolean includeRegional, int...generations) {
		
		// Generate a defending type
		generateDefendingPokemon(includeMega, includeRegional, generations);
		
		// Generate attacking types
		generateAttackingTypes();
		
		// User has not made guess yet
		guess = null;
		state = GameState.HAS_NOT_MADE_GUESS;
	}
	
	
	@Override
	public String getDefendingName() {
		return defendingPokemon.getName();
	}
	
	
	@Override
	public String getDefendingImageFilename() {
		return defendingPokemon.getImageFilename();
	}
	
	
	@Override
	public boolean makeGuess(Type guess) {
		this.guess = guess;
		
		// Determine whether the user has guessed correctly
		boolean correct;
		if (guess == answer)
			correct = true;
		else
			correct = false;
		
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
	
	
	/**
	 * Generates a new defending Pokémon from all Pokémon
	 */
	private void generateDefendingPokemon() {

		// Create Random object for generating random values
		Random rand = new Random();
		
		// Randomly select the defending Pokémon
		List<Pokemon> forms = allPokemon.get(rand.nextInt(allPokemon.size()));
		
		defendingPokemon = forms.get(rand.nextInt(forms.size()));
	}
	
	
	/**
	 * Generates a new defending Pokémon with restrictions
	 * @param includeMega Whether to include mega evolutions
	 * @param includeRegional Whether to include regional forms
	 * @param generations Generations to include
	 */
	private void generateDefendingPokemon(boolean includeMega, boolean includeRegional, int...generations) {
		List<Integer> generationsList = new ArrayList<Integer>();
		for (int generation : generations) {
			generationsList.add(generation);
		}
		
		// Create temporary map of restricted map
		Map<Integer, List<Pokemon>> curPokemon = new HashMap<>();
		
		// Add Pokemon with restrictions
		for (Pokemon pokemon : Pokemon.getAll()) {
			if ((pokemon.getIsMega() && !includeMega) ||
					(pokemon.getIsRegional() && !includeRegional) ||
					(!generationsList.contains(pokemon.getGeneration()))) {
				continue;
			}
			
			if (curPokemon.get(pokemon.getNumber()) == null) {
				curPokemon.put(pokemon.getNumber(), new ArrayList<Pokemon>());
			}
			curPokemon.get(pokemon.getNumber()).add(pokemon);
		}
		
		// Convert the map to a list
		List<List<Pokemon>> curPokemonList = new ArrayList<List<Pokemon>>(curPokemon.values());

		// Create Random object for generating random values
		Random rand = new Random();
		
		// Randomly select the defending Pokémon
		List<Pokemon> forms = curPokemonList.get(rand.nextInt(curPokemon.size()));
		defendingPokemon = forms.get(rand.nextInt(forms.size()));
	}
	
	
	/**
	 * Generates attacking types based on the current defending Pokémon.
	 */
	private void generateAttackingTypes() {

		// Create Random object for generating random values
		Random rand = new Random();
		
		// Create temporary list of attacking types
		List<Type> attackingTypesList = new ArrayList<Type>();
		
		// Randomly select four types to start with
		List<Type> typeChoices = new ArrayList<Type>(
				Arrays.asList(Type.values()));
		for (int i = 0; i < NUM_CHOICES; i++)
			attackingTypesList.add(typeChoices.remove(
					rand.nextInt(typeChoices.size())));
		
		// While multiple types do the most damage, take turns replacing each
		// until that is no longer the case
		while (multipleTypesInHighestTier(attackingTypesList)) {
			int index = randomHighestTierIndex(attackingTypesList);
			attackingTypesList.set(index, typeChoices.remove(
					rand.nextInt(typeChoices.size())));
		}
		
		// Set the correct answer to the highest damaging type
		answer = attackingTypesList.get(
				randomHighestTierIndex(attackingTypesList));
		
		// Shuffle the list so the weakness is not always first
		Collections.shuffle(attackingTypesList);
		
		// Set the values of the attacking types array
		for (int i = 0; i < NUM_CHOICES; i++)
			attackingTypes[i] = attackingTypesList.get(i);
	}
	
	
	/**
	 * Returns whether multiple types in a list do the most damage against the
	 * defending Pokémon.
	 * @param typeList The list of types
	 * @return Whether multiple types in the list do the most damage
	 */
	private boolean multipleTypesInHighestTier(List<Type> typeList) {
		DamageTier highestTier = defendingPokemon
				.getDamageTier(typeList.get(0));
		boolean multipleHighest = false;
		
		for (int i = 1; i < NUM_CHOICES; i++) {
			if (defendingPokemon.getDamageTier(typeList.get(i))
					.compareTo(highestTier) == 0) {
				multipleHighest = true;
			}
			else if (defendingPokemon.getDamageTier(typeList.get(i))
					.compareTo(highestTier) > 0) {
				highestTier = defendingPokemon.getDamageTier(typeList.get(i));
				multipleHighest = false;
			}
		}
		
		return multipleHighest;
	}
	
	
	/**
	 * Returns a random index among the indexes of the types sharing the highest
	 * damage tier.
	 * @return The index
	 */
	private int randomHighestTierIndex(List<Type> typeList) {
		DamageTier highestTier = defendingPokemon
				.getDamageTier(typeList.get(0));
		List<Integer> highestTierIndexes = new ArrayList<Integer>();
		highestTierIndexes.add(0);
		
		for (int i = 1; i < NUM_CHOICES; i++) {
			if (defendingPokemon.getDamageTier(typeList.get(i))
					.compareTo(highestTier) == 0) {
				highestTierIndexes.add(i);
			}
			else if (defendingPokemon.getDamageTier(typeList.get(i))
					.compareTo(highestTier) > 0) {
				highestTier = defendingPokemon.getDamageTier(typeList.get(i));
				highestTierIndexes.clear();
				highestTierIndexes.add(i);
			}
		}
		
		// Create Random object for generating random numbers
		Random rand = new Random();
		
		return highestTierIndexes.get(rand.nextInt(highestTierIndexes.size()));
	}
	
	
	/**
	 * Returns the defending Pokémon.
	 * @return The defending Pokémon
	 */
	public Pokemon getDefendingPokemon() {
		return defendingPokemon;
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
		
		// Get string values of damage multipliers of guess and answer
		String guessDamage = "";
		switch (defendingPokemon.getDamageTier(guess)) {
		case NO_DAMAGE:
			guessDamage = "0x";
			break;
			
		case QUARTER_DAMAGE:
			guessDamage = "¼x";
			break;
			
		case HALF_DAMAGE:
			guessDamage = "½x";
			break;
			
		case NORMAL_DAMAGE:
			guessDamage = "1x";
			break;
			
		case DOUBLE_DAMAGE:
			guessDamage = "2x";
			break;
			
		case QUADRUPLE_DAMAGE:
			guessDamage = "4x";
			break;
		};
		String answerDamage = "";
		switch (defendingPokemon.getDamageTier(answer)) {
		case NO_DAMAGE:
			answerDamage = "0x";
			break;
			
		case QUARTER_DAMAGE:
			answerDamage = "¼x";
			break;
			
		case HALF_DAMAGE:
			answerDamage = "½x";
			break;
			
		case NORMAL_DAMAGE:
			answerDamage = "1x";
			break;
			
		case DOUBLE_DAMAGE:
			answerDamage = "2x";
			break;
			
		case QUADRUPLE_DAMAGE:
			answerDamage = "4x";
			break;
		};
		
		// If the user has not made a guess yet, write a prompt.
		if (state == GameState.HAS_NOT_MADE_GUESS) {
			sb.append("Select the type that is the most effective against the "
					+ "opponent's Pokémon.");
		}
		
		// If the user guessed correctly, congratulate the user.
		else if (state == GameState.CORRECT_GUESS) {
			sb.append(String.format("Correct! Of the given types, %s is the "
					+ "most effective, dealing %s damage to %s.",
					answer,
					answerDamage,
					defendingPokemon));
		}
		
		// If the user did not guess correctly, correct the user.
		else if (state == GameState.INCORRECT_GUESS) {
			sb.append("Incorrect. As ");
			switch (Character.toUpperCase(
					defendingPokemon.getTypes()[0].toString().charAt(0))) {
			case 'A':
			case 'E':
			case 'I':
			case 'O':
			case 'U':
				sb.append("an");
				break;
			default:
				sb.append("a");
			}
			sb.append(" ");
			if (defendingPokemon.getTypes().length == 2) {
				sb.append(String.format("%s/%s",
						defendingPokemon.getTypes()[0],
						defendingPokemon.getTypes()[1]));
			}
			else {
				sb.append(defendingPokemon.getTypes()[0]);
			}
			sb.append(" type, ");
			sb.append(String.format("%s deals %s damage to %s. The correct "
					+ "answer, %s, deals %s damage.",
					guess,
					guessDamage,
					defendingPokemon,
					answer,
					answerDamage));
		}
		
		return sb.toString();
	}

}