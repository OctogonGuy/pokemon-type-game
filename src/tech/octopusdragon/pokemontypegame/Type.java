package tech.octopusdragon.pokemontypegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a Pokémon type. Each type has a set of weaknesses, resistances,
 * and immunities. Each Type object has these as well as an image representing
 * the type.
 * @author Alex Gill
 *
 */
public enum Type {
	
	NORMAL,
	FIRE,
	WATER,
	GRASS,
	ELECTRIC,
	ICE,
	FIGHTING,
	POISON,
	GROUND,
	FLYING,
	PSYCHIC,
	BUG,
	ROCK,
	GHOST,
	DRAGON,
	DARK,
	STEEL,
	FAIRY;
	
	private Type[] weaknesses;	// List of weaknesses
	private Type[] resistances;	// List of resistances
	private Type[] immunities;	// List of immunities
	
	
	/**
	 * The static method is called whenever this class is first accessed and
	 * initializes all the types and their instance variables.
	 */
	static {
		NORMAL.weaknesses = new Type[] {FIGHTING};
		NORMAL.resistances = new Type[] {};
		NORMAL.immunities = new Type[] {GHOST};
		
		FIRE.weaknesses = new Type[] {WATER, GROUND, ROCK};
		FIRE.resistances = new Type[] {FIRE, GRASS, ICE, BUG, STEEL, FAIRY};
		FIRE.immunities = new Type[] {};
		
		WATER.weaknesses = new Type[] {GRASS, ELECTRIC};
		WATER.resistances = new Type[] {FIRE, WATER, ICE, STEEL};
		WATER.immunities = new Type[] {};
		
		GRASS.weaknesses = new Type[] {FIRE, ICE, POISON, FLYING, BUG};
		GRASS.resistances = new Type[] {WATER, GRASS, ELECTRIC, GROUND};
		GRASS.immunities = new Type[] {};
		
		ELECTRIC.weaknesses = new Type[] {GROUND};
		ELECTRIC.resistances = new Type[] {ELECTRIC, FLYING, STEEL};
		ELECTRIC.immunities = new Type[] {};
		
		ICE.weaknesses = new Type[] {FIRE, FIGHTING, ROCK, STEEL};
		ICE.resistances = new Type[] {ICE};
		ICE.immunities = new Type[] {};
		
		FIGHTING.weaknesses = new Type[] {FLYING, PSYCHIC, FAIRY};
		FIGHTING.resistances = new Type[] {BUG, ROCK, DARK};
		FIGHTING.immunities = new Type[] {};
		
		POISON.weaknesses = new Type[] {GROUND, PSYCHIC};
		POISON.resistances = new Type[] {GRASS, FIGHTING, POISON, BUG, FAIRY};
		POISON.immunities = new Type[] {};
		
		GROUND.weaknesses = new Type[] {WATER, GRASS, ICE};
		GROUND.resistances = new Type[] {POISON, ROCK};
		GROUND.immunities = new Type[] {ELECTRIC};
		
		FLYING.weaknesses = new Type[] {ELECTRIC, ICE, ROCK};
		FLYING.resistances = new Type[] {GRASS, FIGHTING, BUG};
		FLYING.immunities = new Type[] {GROUND};
		
		PSYCHIC.weaknesses = new Type[] {BUG, GHOST, DARK};
		PSYCHIC.resistances = new Type[] {FIGHTING, PSYCHIC};
		PSYCHIC.immunities = new Type[] {};
		
		BUG.weaknesses = new Type[] {FIRE, FLYING, ROCK};
		BUG.resistances = new Type[] {GRASS, FIGHTING, GROUND};
		BUG.immunities = new Type[] {};
		
		ROCK.weaknesses = new Type[] {WATER, GRASS, FIGHTING, GROUND, STEEL};
		ROCK.resistances = new Type[] {NORMAL, FIRE, POISON, FIGHTING};
		ROCK.immunities = new Type[] {};
		
		GHOST.weaknesses = new Type[] {GHOST, DARK};
		GHOST.resistances = new Type[] {POISON, BUG};
		GHOST.immunities = new Type[] {NORMAL, FIGHTING};
		
		DRAGON.weaknesses = new Type[] {ICE, DRAGON, FAIRY};
		DRAGON.resistances = new Type[] {FIRE, WATER, GRASS, ELECTRIC};
		DRAGON.immunities = new Type[] {};
		
		DARK.weaknesses = new Type[] {FIGHTING, BUG, FAIRY};
		DARK.resistances = new Type[] {GHOST, DARK};
		DARK.immunities = new Type[] {PSYCHIC};
		
		STEEL.weaknesses = new Type[] {FIRE, FIGHTING, GROUND};
		STEEL.resistances = new Type[] {NORMAL, GRASS, ICE, FLYING, PSYCHIC, BUG, ROCK, DRAGON, STEEL, FAIRY};
		STEEL.immunities = new Type[] {POISON};
		
		FAIRY.weaknesses = new Type[] {POISON, STEEL};
		FAIRY.resistances = new Type[] {FIGHTING, BUG, DARK};
		FAIRY.immunities = new Type[] {DRAGON};
		
	}
	
	
	/**
	 * @return The name of the type as a string
	 */
	public String getName() {
		return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1).toLowerCase();
	}
	
	
	/**
	 * @return The filename of this type's image
	 */
	public String getImageFilename() {
		return this.toString().toLowerCase() + ".png";
	}
	
	
	/**
	 * Returns a list of weaknesses of the type.
	 * @return A list of weaknesses
	 */
	public List<Type> getWeaknesses() {
		return new ArrayList<Type>(Arrays.asList(weaknesses));
	}
	
	
	/**
	 * Returns a list of resistances of the type.
	 * @return A list of resistances
	 */
	public List<Type> getResistances() {
		return new ArrayList<Type>(Arrays.asList(resistances));
	}
	
	
	/**
	 * Returns a list of immunities of the type.
	 * @return A list of immunities
	 */
	public List<Type> getImmunities() {
		return new ArrayList<Type>(Arrays.asList(immunities));
	}
	
	
	/**
	 * Returns a list of types that deal normal damage to this type.
	 * @return A list of types that deal normal damage
	 */
	public List<Type> getNormals() {
		List<Type> normalDamageTypes = new ArrayList<Type>();
		for (Type type: Type.values())
			normalDamageTypes.add(type);
		
		for (Type weakness: getWeaknesses())
			normalDamageTypes.remove(weakness);
		
		for (Type resistance: getResistances())
			normalDamageTypes.remove(resistance);
		
		for (Type immunity: getImmunities())
			normalDamageTypes.remove(immunity);
		
		return normalDamageTypes;
	}
}
