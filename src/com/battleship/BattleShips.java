package com.battleship;

import java.util.*;

public class BattleShips {
    public static int numRows = 10;
    public static int numCols = 10;
    public static int playerShips;
    public static int computerShips;
    public static String[][] grid = new String[numCols][numRows];
    public static int[][] missedGuesses = new int[numCols][numRows];

    public static void main(String[] args) {
        System.out.println("**** Welcome to Battle Ships Game **** \n");
        System.out.println("Right now, sea is empty \n");

        // Step 1 – Create the map
        createMap();

        // Step 2 – Deploy player’s ships
        deployPlayerShips();

        // Step 3 - Deploy computer's ships
        deployComputerShips();

        // Step 4 Battle
        do {
            Battle();
        } while (BattleShips.playerShips != 0 && BattleShips.computerShips != 0);

        // Step 5 - Game over
        gameOver();
    }

    public static void createMap() {
        // Top part of Map
        System.out.print("  ");
        for (int i = 0; i < numCols; i++)
            System.out.print(i + " ");
        System.out.println();

        // Middle part of Map
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = " ";
                if (j == 0)
                    System.out.print(i + "| " + grid[i][j]);
                else if (j == grid[i].length - 1)
                    System.out.print(grid[i][j] + "|" + i);
                else
                    System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }

        // Bottom part of Map
        System.out.print("  ");
        for (int i = 0; i < numCols; i++)
            System.out.print(i + " ");
        System.out.println();
    }

    public static void deployPlayerShips() {
        Scanner input = new Scanner(System.in);

        System.out.println("\n Deploy your ships:");

        // Deploying five ships for player
        BattleShips.playerShips = 5;
        for (int i = 1; i <= BattleShips.playerShips;) {
            System.out.print("Enter X coordinate for your " + i + " ship: ");
            int x = input.nextInt();
            System.out.print("Enter Y coordinate for your " + i + " ship: ");
            int y = input.nextInt();

            if ((x >= 0 && x < numCols) && (y >= 0 && y < numRows) && (grid[y][x] == " ")) {
                grid[y][x] = "@";
                i++;
            } else if ((x >= 0 && x < numCols) && (y >= 0 && y < numRows) && grid[y][x] == "@")
                System.out.println("You can't place two or more ships on the same location");
            else if ((x < 0 || x >= numCols) || (y < 0 || y >= numRows))
                System.out.println("You can't place ships outside the " + numCols + " by " + numRows + " grid");
        }
        printMap();
    }

    public static void deployComputerShips() {
        System.out.println("\n Computer is deploying ships");
        // Deploying five ships for computer
        BattleShips.computerShips = 5;
        for (int i = 1; i <= BattleShips.computerShips;) {
            int x = (int) (Math.random() * 10);
            int y = (int) (Math.random() * 10);

            if ((x >= 0 && x < numCols) && (y >= 0 && y < numRows) && (grid[y][x] == " ")) {
                grid[x][y] = "\u00A0";
                System.out.println(i + ". ship DEPLOYED");
                i++;
            } /*
               * else if (grid[y][x] == "@") {
               * x = (int) (Math.random() * 10);
               * y = (int) (Math.random() * 10);
               * }
               */
            // I have problem where my ship after deployed get replaced by computer's ship,
            // and I don't know yet how to fix it
        }
        printMap();
    }

    public static void Battle() {
        playerTurn();
        computerTurn();

        printMap();

        System.out.println();
        System.out
                .println("Your ships: " + BattleShips.playerShips + " | Computer ships: " + BattleShips.computerShips);
        System.out.println();
    }

    public static void playerTurn() {
        System.out.println("\n YOUR TURN");
        int x = -1, y = -1;
        do {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter X coordinate: ");
            x = input.nextInt();
            System.out.print("Enter Y coordinate: ");
            y = input.nextInt();

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) // valid guess
            {
                if (grid[y][x] == "\u00A0") // if computer ship is already there; computer loses ship
                {
                    System.out.println("Boom! You sunk the ship!");
                    grid[y][x] = "!"; // Hit mark
                    --BattleShips.computerShips;
                } else if (grid[y][x] == "@") {
                    System.out.println("Oh no, you sunk your own ship :(");
                    grid[y][x] = "x";
                    --BattleShips.playerShips;

                } else if (grid[y][x] == " ") {
                    System.out.println("Sorry, you missed");
                    grid[y][x] = "-";

                } else if (grid[y][x] == "-") {
                    System.out.println("Already Hit!!!");
                }
            } else if ((x < 0 || x >= numCols) || (y < 0 || y >= numRows)) // invalid guess
                System.out.println("You can't place ships outside the " + numRows + " by " + numCols + " grid");
        } while ((x < 0 || x >= numRows) || (y < 0 || y >= numCols)); // keep re-prompting till valid guess
    }

    public static void computerTurn() {
        System.out.println("\n COMPUTER'S TURN");
        // Guess co-ordinates
        int x = -1, y = -1;
        do {
            x = (int) (Math.random() * 10);
            y = (int) (Math.random() * 10);

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) // valid guess
            {
                if (grid[x][y] == "@") // if player ship is already there; player loses ship
                {
                    System.out.println("The Computer sunk one of your ships!");
                    grid[y][x] = "x";
                    --BattleShips.playerShips;

                } else if (grid[y][x] == "x") {
                    System.out.println("The Computer sunk one of its own ships");
                    grid[x][y] = "!";
                } else if (grid[y][x] == " " || grid[y][x] == "-") {
                    System.out.println("Computer missed");
                    // Saving missed guesses for computer
                    if (missedGuesses[y][x] != 1)
                        missedGuesses[y][x] = 1;
                }
            }
        } while ((x < 0 || x >= numRows) || (y < 0 || y >= numCols)); // keep re-prompting till valid guess
    }

    public static void gameOver() {
        System.out
                .println("Your ships: " + BattleShips.playerShips + " | Computer ships: " + BattleShips.computerShips);
        if (BattleShips.playerShips > 0 && BattleShips.computerShips <= 0)
            System.out.println("Hooray! You won the battle :)");
        else
            System.out.println("Sorry, you lost the battle");
        System.out.println();
    }

    public static void printMap() {
        System.out.println();
        // Top part of Map
        System.out.print("   ");
        for (int i = 0; i < numCols; i++)
            System.out.print(i + " ");
        System.out.println();

        // Middle part of Map
        for (int x = 0; x < grid.length; x++) {
            System.out.print(x + "| ");

            for (int y = 0; y < grid[x].length; y++) {
                System.out.print(grid[x][y] + " ");
            }

            System.out.println("|" + x);
        }

        // Bottom part of Map
        System.out.print("   ");
        for (int i = 0; i < numCols; i++)
            System.out.print(i + " ");
        System.out.println();
    }
}