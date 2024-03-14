package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q",
     * the same finalWorldFrame should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * finalWorldFrame. However, the behavior is slightly different.
     * After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same finalWorldFrame back again,
     * since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the finalWorldFrame
     */
    public TETile[][] playWithInputString(String input) {
        char playerOption = input.charAt(0);

        // If player's option is 'N' or 'n', which means start a new game
        if (playerOption == 'N' || playerOption == 'n') {
            char theLastCharacter = input.charAt(input.length() - 1);

            // If the last Character is not 'S' or 's', then the input is invalid
            if (theLastCharacter != 'S' && theLastCharacter != 's') {
                throw new IllegalArgumentException(
                        "The last character of input must be 's' or 'S'");
            }

            // Find the seed and use it to make the RANDOM
            String seedString = input.substring(1, input.length() - 2);
            final long seed = Long.parseLong(seedString);
            final Random random = new Random(seed);

            ter.initialize(WIDTH, HEIGHT);

            // Initialize tiles
            TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    finalWorldFrame[x][y] = Tileset.NOTHING;
                }
            }

            // Generate a random position as the start position
            MapGenerator.Position currentPosition = MapGenerator.randomPosition(
                    finalWorldFrame, random);

            for (int i = 0; i < 200; ++i) {
                // Generate a random number.
                int generateOption = RandomUtils.uniform(random, 3);

                if (generateOption == 0) {
                    /* If the random number is 0, generate a random hallway with
                       random direction and random length
                       Then use the end position to generate another random structure
                     */
                    String direction = MapGenerator.randomDirectionForHallway(random);
                    int length = MapGenerator.randomSafeLength(
                            finalWorldFrame, currentPosition, direction, random);
                    currentPosition = MapGenerator.generateHallway(
                            finalWorldFrame, currentPosition, direction, length, random);
                } else if (generateOption == 1) {
                    /* If the random number is 1, generate a random hallway turn
                       with random direction
                       Then use the end position to generate another random structure
                     */
                    String direction = MapGenerator.randomDirectionForHallwayTurn(random);
                    currentPosition = MapGenerator.safelyGenerateHallwayTurn(
                            finalWorldFrame, currentPosition, direction, random);
                } else {
                    // If the random number is 2, generate a random house with random size
                    MapGenerator.safelyGenerateRandomHouse(
                            finalWorldFrame, currentPosition, random);
                }
            }
            return finalWorldFrame;
        }
        return null;
    }
}
