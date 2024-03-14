package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class MapGenerator {
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static class Position {
        int x;
        int y;

        public Position(int X, int Y) {
            x = X;
            y = Y;
        }
    }

    /* Generate a house in position "pos" which has "x" to "x + width - 1" wall tiles and
       "y" to "y + height - 1" wall tiles so as the opposite position. The area
       surrounded by wall is filled with floor.
       When meet a floor or a wall, this generator will leave it alone instead of covering it
       for the convenience of generating hallway or hallway turn.
     */

    public static void generateHouse(TETile[][] world, Position pos, int width, int height) {
        if (width < 3 || height < 3) {
            throw new IllegalArgumentException(
                    "Width and height of the house must grater than or equal to 3");
        }
        int x = pos.x;
        int y = pos.y;

        // Fill the bottom and the top
        for (int i = x; i < x + width; ++i) {
            TETile bottomTile = world[i][y];
            TETile topTile = world[i][y + height - 1];
            if (!bottomTile.equals(Tileset.WALL) && !bottomTile.equals(Tileset.FLOOR)) {
                world[i][y] = Tileset.WALL;
            }
            if (!topTile.equals(Tileset.WALL) && !topTile.equals(Tileset.FLOOR)) {
                world[i][y + height - 1] = Tileset.WALL;
            }
        }

        // Fill the left and the right
        for (int i = y + 1; i < y + height - 1; ++i) {
            TETile leftTile = world[x][i];
            TETile rightTile = world[x + width - 1][i];
            if (!leftTile.equals(Tileset.WALL) && !leftTile.equals(Tileset.FLOOR)) {
                world[x][i] = Tileset.WALL;
            }
            if (!rightTile.equals(Tileset.WALL) && !rightTile.equals(Tileset.FLOOR)) {
                world[x + width - 1][i] = Tileset.WALL;
            }
        }

        // Fill the room
        for (int i = x + 1; i < x + width - 1; ++i) {
            for (int j = y + 1; j < y + height - 1; ++j) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    // Generate a unit house which has width, height 3 and room size 1
    public static void generateUnitHouse(TETile[][] world, Position pos) {
        generateHouse(world, pos, 3, 3);
    }

    /* Generate a hallway which go "direction" from pos and has "length" length,
       return the position down the last room
     */
    public static Position generateHallway(
            TETile[][] world, Position pos, String direction, int length, Random random) {
        int x = pos.x;
        int y = pos.y;

        /* If its length less than or equal to 1, the length is invalid.
           The function just do nothing and return the original position
         */
        if (length <= 1) {
            return randomPosition(world, RANDOM);
        }

        switch (direction) {
            case "up": {
                for (int i = 0; i < length; ++i) {
                    Position newPos = new Position(x, y + i);
                    generateUnitHouse(world, newPos);
                }
                return new Position(x, y + length);
            }
            case "down": {
                for (int i = 0; i < length; ++i) {
                    Position newPos = new Position(x, y - i);
                    generateUnitHouse(world, newPos);
                }
                return new Position(x, y - length);
            }
            case "left": {
                for (int i = 0; i < length; ++i) {
                    Position newPos = new Position(x - i, y);
                    generateUnitHouse(world, newPos);
                }
                return new Position(x - length, y);
            }
            case "right": {
                for (int i = 0; i < length; ++i) {
                    Position newPos = new Position(x + i, y);
                    generateUnitHouse(world, newPos);
                }
                return new Position(x + length, y);
            }
            default: throw new IllegalArgumentException("Invalid direction");
        }
    }

    /* Generate a hallway turn on "pos" in terms of "direction",
       which has "L" shape with total room size 3
       Return the position left the last room(for generating another hallway convenience)
     */
    public static Position generateHallwayTurn(TETile[][] world, Position pos, String direction) {
        int x = pos.x;
        int y = pos.y;
        Position newLeftPos = new Position(x - 1, y);
        Position newRightPos = new Position(x + 1, y);


        switch (direction) {
            case "left_up": {
                Position newUpPos = new Position(x + 1, y + 1);
                generateUnitHouse(world, pos);
                generateUnitHouse(world, newRightPos);
                generateUnitHouse(world, newUpPos);
                return new Position(x + 1, y + 2);
            }
            case "right_up": {
                Position newUpPos = new Position(x - 1, y + 1);
                generateUnitHouse(world, pos);
                generateUnitHouse(world, newLeftPos);
                generateUnitHouse(world, newUpPos);
                return new Position(x - 1, y + 2);
            }
            case "left_down": {
                Position newDownPos = new Position(x + 1, y - 1);
                generateUnitHouse(world, pos);
                generateUnitHouse(world, newRightPos);
                generateUnitHouse(world, newDownPos);
                return new Position(x + 1, y - 2);
            }
            case "right_down": {
                Position newDownPos = new Position(x - 1, y - 1);
                generateUnitHouse(world, pos);
                generateUnitHouse(world, newLeftPos);
                generateUnitHouse(world, newDownPos);
                return new Position(x - 1, y - 2);
            }
            default: throw new IllegalArgumentException("Invalid direction");
        }
    }

    // Generate a random position in a world
    public static Position randomPosition(TETile[][] world, Random random) {
        int width = world.length;
        int height = world[0].length;

        int x = RandomUtils.uniform(random, width);
        int y = RandomUtils.uniform(random, height);
        return new Position(x, y);
    }

    /* Generate a random length for a hallway in a world
       that will never cause "ArrayIndexOutOfBounds" exception
     */
    public static int randomSafeLength(
            TETile[][] world, Position startPos, String direction, Random random) {
        int x = startPos.x;
        int y = startPos.y;
        int width = world.length;
        int height = world[0].length;

        int xBound = width - x - 2;
        int yBound = height - y - 2;

        // If the space is too small to generate a hallway, it will return length 0
        if (xBound <= 0 || yBound <= 0 || x <= 0 || y <= 0) {
            return 0;
        }

        switch (direction) {
            case "up": return RandomUtils.uniform(random, yBound);

            case "down": return RandomUtils.uniform(random, y);

            case "left": return RandomUtils.uniform(random, x);

            case "right": return RandomUtils.uniform(random, xBound);

            default: throw new IllegalArgumentException("Invalid direction");
        }

    }

    // Generate a random direction for hallway
    public static String randomDirectionForHallway(Random random) {
        int ran = RandomUtils.uniform(random, 4);

        switch (ran) {
            case 0: return "left";

            case 1: return "right";

            case 2: return "up";

            case 3: return "down";

            default: throw new IllegalArgumentException("Maybe the uniform function is broken, "
                        + "but I don't think this will happen");
        }
    }

    // Generate a random direction for hallway turn
    public static String randomDirectionForHallwayTurn(Random random) {
        int ran = RandomUtils.uniform(random, 4);

        switch (ran) {
            case 0: return "left_up";

            case 1: return "right_up";

            case 2: return "left_down";

            case 3: return "right_down";

            default: throw new IllegalArgumentException("Maybe the uniform function is broken, "
                        + "but I don't think this will happen");
        }
    }

    /* Generate a hallway turn that will never cause "ArrayIndexOutOfBounds" exception
       If the space is too small to generate a hallway turn, it will return a random position
     */

    public static Position safelyGenerateHallwayTurn(
            TETile[][] world, Position pos, String direction, Random random) {
        int x = pos.x;
        int y = pos.y;
        int width = world.length;
        int height = world[0].length;

        // When the space is too small to generate a hallway turn
        if (x >= width - 2 || y >= height - 2) {
            return randomPosition(world, random);
        }

        switch (direction) {
            case "left_up": {
                if (x >= width - 4 || y >= height - 4) {
                    return pos;
                }
                return generateHallwayTurn(world, pos, direction);
            }
            case "right_up": {
                if (x <= 4 || y >= height - 4) {
                    return pos;
                }
                return generateHallwayTurn(world, pos, direction);
            }
            case "left_down": {
                if (x >= width - 4 || y < 4) {
                    return pos;
                }
                return generateHallwayTurn(world, pos, direction);
            }
            case "right_down": {
                if (x <= 4 || y <= 4) {
                    return pos;
                }
                return generateHallwayTurn(world, pos, direction);
            }
            default: {
                throw new IllegalArgumentException("Invalid direction");
            }
        }
    }

    // Generate a random size house that will never cause "ArrayIndexOutOfBounds" exception
    public static void safelyGenerateRandomHouse(TETile[][] world, Position pos, Random random) {
        int x = pos.x;
        int y = pos.y;
        int width = world.length;
        int height = world[0].length;

        int xDifference = width - x;
        int yDifference = height - y;
        // The empty space is too small to contain a house
        if (xDifference <= 3 || yDifference <= 3) {
            return;
        }

        // Limit the size of the house
        if (xDifference > 10) {
            xDifference = 10;
        }
        if (yDifference > 10) {
            yDifference = 10;
        }

        int randomHouseWidth = RandomUtils.uniform(random, 3, xDifference);
        int randomHouseHeight = RandomUtils.uniform(random, 3, yDifference);

        generateHouse(world, pos, randomHouseWidth, randomHouseHeight);
    }

}
