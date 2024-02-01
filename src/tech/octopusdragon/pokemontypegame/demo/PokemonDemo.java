package tech.octopusdragon.pokemontypegame.demo;

import java.util.*;

import tech.octopusdragon.pokemontypegame.*;

/**
 * This tests out the Pokemon and PokemonGame classes by starting a game in
 * which the user guesses a Pokémon type matchup.
 * @author Alex Gill
 *
 */
public class PokemonDemo {
	
	static PokemonGame game;	// The game

	public static void main(String[] args) {
		
		// Start the game
		game = new PokemonGame();
		
		// Create a Scanner for keyboard input.
		Scanner keyboard = new Scanner(System.in);
		
		// Print a welcome message.
		System.out.println("Welcome to the Pokémon type game!");
		
		// Continue asking the user questions until the quit command is issued.
		boolean keepGoing = true;
		outer: while (keepGoing) {
			
			// Start a new round
			game.nextRound();
			
			// Prompt user for input until valid input is received.
			boolean validInput = false;
			while (!validInput) {
				try {
					// Ask the user for the most effective type
					System.out.printf("Your opponent is a(n) %s.\n",
							game.getDefendingPokemon());
					// Ask the user for a super-effective type.
					System.out.print("Your options are ");
					for (int i = 0; i < game.getAttackingTypes().length-1; i++)
						System.out.print(game.getAttackingType(i) + ", ");
					System.out.println("and "
						+ game.getAttackingType(
								game.getAttackingTypes().length -1)
						+ ".");
					System.out.print("Enter the type that is most effective "
							 + "against it (or 'q' to quit): ");
					
					// Get user input.
					String input = keyboard.nextLine();
					
					// If the user decides to quit, end the loop.
					if (input.equalsIgnoreCase("q")) {
						keepGoing = false;
						continue outer;
					}
					else {
						Type guess = Type.valueOf(input.trim().toUpperCase());
						
						if (!Arrays.asList(game.getAttackingTypes())
								.contains(guess))
							throw new IllegalArgumentException();
							
						game.makeGuess(guess);
						validInput = true;
					}
				}
				catch (IllegalArgumentException e) {
					System.out.println("Invalid input.");
				}
			}
			
			// Print an informational message based on whether the user guessed
			// correctly or incorrectly
			System.out.println(game.message());
			
			// Print the streak
			System.out.printf("Current streak: %d\n", game.getStreak());
			
			// Print a bar for visual clarity.
			System.out.println("---");
		}
		
		// Print a farewell message.
		System.out.println("Alola!");
		
		// Close the Scanner.
		keyboard.close();
	}

}
