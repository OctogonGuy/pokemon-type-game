# Pokémon Type Game

Welcome to the Pokémon type game! Test your Pokémon knowledge by guessing which is the most effective type.

Built with [JavaFX](https://openjfx.io/).

## Overview

In this game, you will be presented with a defending Pokémon type or Pokémon, and you will have to guess which type of a given selection of attacking types is most effective against it.

There are three game modes:

- **Easy**: In this mode, you are given a defending Pokémon type. You are given 3 attacking types, and you are to select the type that is supereffective.
- **Medium**: In this mode, you are given a defending Pokémon as well as its typing. You are given 4 attacking types, and you are to select the type that is the most effective among them. Unlike Easy mode, this type may not necessarily be supereffective. It may deal a normal amount of damage, but the rest of the types only deal half or less damage. Alternatively, all the types may deal supereffective damage, but the correct answer deals quadruple damage while the rest only deal double. You can also select which region(s) from which you would like to be shown defending Pokémon. Note that your selection will only take place once you finish the current round.
- **Hard**: This mode is the same as Medium but with two key differences. First, the defending Pokémon's typing is not shown. Second, you can no longer select any region(s). Instead, every 5 rounds, you will be shown Pokémon from each region in ascending order. For example, from rounds 1 through 5 you will be shown Pokémon from the Kanto region, from rounds 6 through 10 you will be shown Pokémon from the Johto region, from rounds 11 through 15 you will be shown Pokémon from the Hoenn region, and so forth.

The numbers on the top of the game screen are your streaks. The left number is your current streak, and the right number is your high score.

In the top right corner, you can change the background color, skip a song, or mute the audio.

## Setup & Usage

### Prerequisites

You must have the following installed on your computer to run the application:

- Java JDK (version 25+)

### Installation

Perform the following steps to be able to run the application:

1. Clone the repository
2. Navigate to the repository
   ```
   cd pokemon-type-game
   ```
3. Add execute permissions for `mvnw`
   ```
   chmod +x mvnw
   ```

### Running

In the directory of the repository, run the following:

```
./mvnw javafx:run
```