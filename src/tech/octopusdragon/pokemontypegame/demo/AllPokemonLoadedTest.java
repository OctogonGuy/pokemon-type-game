package tech.octopusdragon.pokemontypegame.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import tech.octopusdragon.pokemontypegame.Pokemon;

/**
 * Test to see if all Pokemon and can be loaded properly from the text file
 * @author Alex Gill
 *
 */
public class AllPokemonLoadedTest {

	public static void main(String[] args) {
		System.out.println("Loading Pokémon...");
		List<Pokemon> allPokemonList = Pokemon.getAll();
		Map<Integer, List<Pokemon>> allPokemonMap = new HashMap<>();
		for (Pokemon pokemon : allPokemonList) {
			//System.out.println(allPokemonList.indexOf(pokemon)+1 + "\t" + pokemon.getNumber() + "\t" + pokemon + "\t" + pokemon.getGeneration());
			if (allPokemonMap.get(pokemon.getNumber()) == null) {
				allPokemonMap.put(pokemon.getNumber(), new ArrayList<Pokemon>());
			}
			
			allPokemonMap.get(pokemon.getNumber()).add(pokemon);
		}
		
		
		System.out.println(allPokemonMap.size() + " Pokémon found");
		System.out.println(allPokemonList.size() + " including forms");
		
		
		System.out.println("Loading images...");
		Pokemon curPokemon = null;
		String curPath = null;
		try {
			for (Pokemon pokemon : allPokemonList) {
				curPokemon = pokemon;
				curPath = "resources/img/pkmn/" + pokemon.getImageFilename();
				ImageIO.read(new File(curPath));
			}
		} catch (IOException e) {
			System.out.println("Error with: ");
			System.out.println(curPokemon);
			System.out.println(curPath);
			e.printStackTrace();
		}
		System.out.println("Successfully loaded all images!");
	}

}
