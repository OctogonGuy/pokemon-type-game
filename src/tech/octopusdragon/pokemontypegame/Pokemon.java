package tech.octopusdragon.pokemontypegame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Represents a Pokémon. Each Pokémon has a name, a National Pokédex number, and
 * one or two types.
 * @author Alex Gill
 *
 */
public class Pokemon {

	// --- Static constants ---
	private static final String POKEMON_LIST_PATH = "resources/pokemon.csv";
	

	// --- Static variables ---
	// All Pokémon. Each list contains all forms for a species
	private static Map<Integer, List<Pokemon>> pokemon;

	
	// --- Instance variables ---
	private Type[] types;		// The Pokémon's type(s)
	private int number;			// The Pokémon's National Pokédex number
	private String name;		// The Pokémon's name
	private int generation;		// The generation of the Pokémon
	private boolean isMega;		// Whether the Pokémon is a mega evolution
	private boolean isRegional;	// Whether the Pokémon is a regional form
	private String imageFilename;	// Filename of image
	
	
	/**
	 * Instantiates a map with all Pokémon and their National Pokédex numbers.
	 */
	static {
		pokemon = readPokemonList();
	}
	
	
	/**
	 * Constructor
	 */
	private Pokemon(int number, String name, Type... types) {
		this.number = number;
		this.name = name;
		this.types = types;
	}
	
	
	/**
	 * This constructor instantiates a Pokémon by reading its information from
	 * a line of text
	 */
	private Pokemon(String line) {
		
		// Get a list of tokens
		String[] tokens = line.split(",");
		for (int i = 0; i < tokens.length; i++)
			tokens[i] = tokens[i].trim();
		
		// Throw an exception if there are are an incorrect number of tokens.
		if (tokens.length != 8)
			throw new IllegalArgumentException("Incorrect number of tokens");
		
		// Parse the number
		number = Integer.parseInt(tokens[0]);
		
		// Parse the name
		name = tokens[4];
		
		// Parse the type(s).
		int numTypes = tokens[6].isEmpty() ? 1 : 2;
		types = new Type[numTypes];
		for (int i = 0; i < types.length; i++) {
			types[i] = Type.valueOf(tokens[5 + i].toUpperCase());
		}
		
		// Parse the generation
		generation = Integer.parseInt(tokens[7]);
		
		// Get whether the Pokémon is a mega evolution
		isMega = !tokens[2].isEmpty();
		
		// Get whether the Pokémon is a regional form
		isRegional = !tokens[3].isEmpty();
		
		// Construct filename
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%03d", number));
		if (!tokens[2].isEmpty()) {
			sb.append("-Mega");
		}
		if (!tokens[3].isEmpty()) {
			sb.append("-Alola");
		}
		if (!tokens[1].isEmpty()) {
			sb.append("-" + tokens[1]);
		}
		sb.append(".png");
		imageFilename = sb.toString();
	}
	
	
	/**
	 * Returns the Pokémon's National Pokédex number.
	 * @return The Pokémon's National Pokédex number
	 */
	public int getNumber() {
		return number;
	}
	
	
	/**
	 * Returns the Pokémon's name.
	 * @return The Pokémon's name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * 
	 * @return the Pokémon's generation
	 */
	public int getGeneration() {
		return generation;
	}
	
	
	/**
	 * 
	 * @return whether the Pokémon is a mega evolution
	 */
	public boolean getIsMega() {
		return isMega;
	}
	
	
	/**
	 * 
	 * @return whether the Pokémon is a regional form
	 */
	public boolean getIsRegional() {
		return isRegional;
	}
	
	
	/**
	 * Returns an array of the Pokémon's type(s).
	 * @return An array of the Pokémon's type(s)
	 */
	public Type[] getTypes() {
		return types.clone();
	}
	
	
	/**
	 * @return The image filename of this Pokémon
	 */
	public String getImageFilename() {
		return imageFilename;
	}
	
	
	/**
	 * Returns a list of 4x weaknesses of the Pokémon.
	 * @return A list of 4x weaknesses
	 */
	public List<Type> get4xWeaknesses() {
		List<Type> quadWeaknesses = new ArrayList<Type>();
		
		if (types.length == 1)
			return quadWeaknesses;
		
		for (Type type: Type.values()) {
			if (types[0].getWeaknesses().contains(type) &&
				types[1].getWeaknesses().contains(type)) {
				quadWeaknesses.add(type);
			}
		}
		return quadWeaknesses;
	}
	
	
	/**
	 * Returns a list of 2x weaknesses of the Pokémon.
	 * @return A list of 2x weaknesses
	 */
	public List<Type> get2xWeaknesses() {
		List<Type> doubWeaknesses = new ArrayList<Type>();
		
		if (types.length == 1)
			return types[0].getWeaknesses();
		
		for (Type type: Type.values()) {
			if (types[0].getWeaknesses().contains(type) &&
				types[1].getNormals().contains(type) ||
				types[0].getNormals().contains(type) &&
				types[1].getWeaknesses().contains(type)) {
				doubWeaknesses.add(type);
			}
		}
		return doubWeaknesses;
	}
	
	
	/**
	 * Returns a list of 1/2 resistances of the Pokémon.
	 * @return A list of 1/2 resistances
	 */
	public List<Type> get2xResistances() {
		List<Type> doubResistances = new ArrayList<Type>();
		
		if (types.length == 1)
			return types[0].getResistances();
		
		for (Type type: Type.values()) {
			if (types[0].getResistances().contains(type) &&
				types[1].getNormals().contains(type) ||
				types[0].getNormals().contains(type) &&
				types[1].getResistances().contains(type)) {
				doubResistances.add(type);
			}
		}
		return doubResistances;
	}
	
	
	/**
	 * Returns a list of 1/4 resistances of the Pokémon.
	 * @return A list of 1/4 resistances
	 */
	public List<Type> get4xResistances() {
		List<Type> quadResistances = new ArrayList<Type>();
		
		if (types.length == 1)
			return quadResistances;
		
		for (Type type: Type.values()) {
			if (types[0].getResistances().contains(type) &&
				types[1].getResistances().contains(type)) {
				quadResistances.add(type);
			}
		}
		return quadResistances;
	}
	
	
	/**
	 * Returns a list of immunities of the Pokémon.
	 * @return A list of immunities
	 */
	public List<Type> getImmunities() {
		List<Type> immunities = new ArrayList<Type>();
		for (Type type: types) {
			for (Type immunity: type.getImmunities()) {
				if (!immunities.contains(immunity))
					immunities.add(immunity);
			}
		}
		return immunities;
	}
	
	
	/**
	 * Returns the damage tier of the specified attacking type against this
	 * Pokémon.
	 * @param type The type
	 * @return The damage tier
	 */
	public DamageTier getDamageTier(Type type) {
		
		if (get4xWeaknesses().contains(type))
			return DamageTier.QUADRUPLE_DAMAGE;
		
		else if (get2xWeaknesses().contains(type))
			return DamageTier.DOUBLE_DAMAGE;
		
		else if (get2xResistances().contains(type))
			return DamageTier.HALF_DAMAGE;
		
		else if (get4xResistances().contains(type))
			return DamageTier.QUARTER_DAMAGE;
		
		else if (getImmunities().contains(type))
			return DamageTier.NO_DAMAGE;
		
		else
			return DamageTier.NORMAL_DAMAGE;
	}
	
	
	/**
	 * Returns the Pokémon at the given National Pokédex number. If there are
	 * multiple forms, returns a random form.
	 * @param number The National Pokédex number
	 * @return The Pokémon
	 */
	public static Pokemon get(int number) {
		Random rand = new Random();
		List<Pokemon> forms = pokemon.get(number);
		return forms.get(rand.nextInt(forms.size()));
	}
	
	
	/**
	 * Returns a list of all Pokémon.
	 * @return The Pokémon
	 */
	public static List<Pokemon> getAll() {
		List<Pokemon> pokemonList = new ArrayList<Pokemon>();
		for (List<Pokemon> formsList : pokemon.values()) {
			for (Pokemon form : formsList) {
				pokemonList.add(form);
			}
		}
		return pokemonList;
	}
	
	
	/**
	 * 
	 * @return The highest generation of a Pokemon
	 */
	public static int getHighestGeneration() {
		int highestGeneration = 0;
		for (Pokemon pokemon : Pokemon.getAll()) {
			if (pokemon.getGeneration() > highestGeneration) {
				highestGeneration = pokemon.getGeneration();
			}
		}
		return highestGeneration;
	}
	
	
	/**
	 * Reads a map of Pokémon objects from a text file.
	 * @return The map of Pokémon
	 */
	private static Map<Integer, List<Pokemon>> readPokemonList() {
		
		// Create a map with all the Pokemon in it.
		Map<Integer, List<Pokemon>> pokemonMap = new HashMap<>();
		
		// Open the file with the list of Pokemon
		BufferedReader inputStream = new BufferedReader(
				new InputStreamReader(
				Pokemon.class.getClassLoader().getResourceAsStream(POKEMON_LIST_PATH)));
		
		// Read each Pokemon from the file
		String line = null;
		int lineNum = 0;
		try {
			inputStream.readLine();	// Skip first line
			lineNum++;
			
			line = inputStream.readLine();
			while (line != null) {
				Pokemon newPokemon = new Pokemon(line);
				
				if (pokemonMap.get(newPokemon.getNumber()) == null) {
					pokemonMap.put(newPokemon.getNumber(), new ArrayList<Pokemon>());
				}
				
				pokemonMap.get(newPokemon.getNumber()).add(newPokemon);
				
				line = inputStream.readLine();
				lineNum++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error on line " + lineNum);
			System.out.println(line);
			e.printStackTrace();
		}
		
		// Close the file
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Return the map
		return pokemonMap;
	}
	
	
	@Override
	public String toString() {
		return name;
	}
}
