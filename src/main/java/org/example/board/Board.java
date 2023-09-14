package org.example.board;

import org.example.exception.CoordinateIsInvalidException;
import org.example.exception.InvalidCoordinatesCountException;
import org.example.exception.ShipPlacementInvalidException;
import org.example.ship.Ship;

public class Board {

    private static final int SIZE = 10;
    private Cell[][] grid;
    private int lives;

    public Board() {
        initializeGrid();
        this.lives = 0;
    }

    public boolean attack(String coordinates) throws CoordinateIsInvalidException {
        int[][] numbers = convertCoordinatesToNumbers(coordinates);
        if (this.grid[numbers[0][0]][numbers[0][1]].attack() == Cell.Status.HIT) {
            this.lives--;
            return true;
        } else {
            return false;
        }
    }

    public void addShip(Ship ship, String... coordinates)
            throws InvalidCoordinatesCountException, CoordinateIsInvalidException,
            ShipPlacementInvalidException {
        if (ship.getLength() != coordinates.length) {
            throw new InvalidCoordinatesCountException();
        }
        int[][] numbers = convertCoordinatesToNumbers(coordinates);
        if (isShipPlacementValid(numbers) && isShipPlacementPossible(numbers)) {
            for (int[] number : numbers) {
                this.grid[number[0]][number[1]] = new Cell(ship);
            }
            this.lives += ship.getLength();
        } else {
            throw new ShipPlacementInvalidException();
        }
    }

    private int[][] convertCoordinatesToNumbers(String... coordinates)
            throws CoordinateIsInvalidException {
        int[][] numbers = new int[coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            numbers[i][0] = coordinates[i].charAt(0) - 97;
            numbers[i][1] = coordinates[i].charAt(1) - 48;

            if (numbers[i][0] < 0 || numbers[i][0] > 9
                    || numbers[i][1] < 0 || numbers[i][1] > 9) {
                throw new CoordinateIsInvalidException();
            }
        }
        return numbers;
    }

    private boolean isShipPlacementValid(int[][] coordinates) {
        int[] firstCoordinates = coordinates[0];
        for (int i = 1; i < coordinates.length; i++) {
            if (!(coordinates[i][0] == firstCoordinates[0] &&
                    (coordinates[i][1] == firstCoordinates[1] + i || coordinates[i][1] == firstCoordinates[1] - i)
                    || coordinates[i][1] == firstCoordinates[1] &&
                    (coordinates[i][0] == firstCoordinates[0] + i || coordinates[i][0] == firstCoordinates[0] - i))
            ) {
                return false;
            }
        }
        return true;
    }

    private boolean isShipPlacementPossible(int[][] coordinates) {
        int startI = Math.max(coordinates[0][0] - 1, 0);
        int startJ = Math.max(coordinates[0][1] - 1, 0);
        int finishI = Math.min(coordinates[coordinates.length - 1][0] + 1, SIZE - 1);
        int finishJ = Math.min(coordinates[coordinates.length - 1][1] + 1, SIZE - 1);
        for (int i = startI; i < finishI; i++) {
            for (int j = startJ; j < finishJ; j++) {
                if (this.grid[i][j].getStatus() != Cell.Status.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initializeGrid() {
        this.grid = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public String displayBoard(boolean hideShips) {
        StringBuilder builder = new StringBuilder();
        builder.append("  0 1 2 3 4 5 6 7 8 9\n");
        for (int i = 0; i < SIZE; i++) {
            builder.append(((char) (i + 97))).append(" ");
            for (int j = 0; j < SIZE; j++) {
                if (hideShips) {
                    builder.append(this.grid[i][j].getStatus().getPublicValue());
                } else {
                    builder.append(this.grid[i][j].getStatus().getPrivateValue());
                }
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public int getLives() {
        return lives;
    }

}
