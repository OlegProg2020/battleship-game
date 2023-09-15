package org.example.board;

import org.example.exception.CoordinateIsInvalidException;
import org.example.exception.InvalidCoordinatesCountException;
import org.example.exception.ShipPlacementInvalidException;
import org.example.ship.Ship;

import static org.example.board.Cell.Status.*;

/***
 * Доска, на которой размещаются корабли игрока.
 * Доска состоит из двумерного массива ячеек.
 * Также доска содержит количество жизней - число неподбитых частей кораблей,
 * расположенных на доске.
 */
public class Board {

    /***
     * Размер доски.
     */
    private static final int SIZE = 10;
    /***
     * Двумерный массив квадратов доски, на которых будут располагаться корабли.
     */
    private Cell[][] grid;
    /***
     * Суммарное количество оставшихся жизней кораблей на доске.
     */
    private int lives;

    /***
     * Создает доску без кораблей и количеством жизней равным 0.
     */
    public Board() {
        initializeGrid();
        this.lives = 0;
    }

    /***
     * Метод атаки квадрата на доске по координатам.
     * @param coordinates координаты для нанесения удара.
     * @return результат ударsа по квадрату: MISS, HIT или SUNKEN.
     * @throws CoordinateIsInvalidException
     */
    public Cell.Status attack(String coordinates) throws CoordinateIsInvalidException {
        int[][] numbers = convertCoordinatesToNumbers(coordinates);

        Cell.Status before = this.grid[numbers[0][0]][numbers[0][1]].getStatus();
        Cell.Status after = this.grid[numbers[0][0]][numbers[0][1]].attack();
        if (before == SHIP && (after == HIT || after == SUNKEN)) {
            this.lives--;
        }
        return after;
    }

    /***
     * Метод добавления корабля на доску по заданным координатам.
     * Увеличивает суммарное количество жизней на доске на длину корабля.
     * @param ship корабль для добавления.
     * @param coordinates массив координат для постановки. Пример: ["a7", "a8", "a9"].
     * @throws InvalidCoordinatesCountException
     * @throws CoordinateIsInvalidException
     * @throws ShipPlacementInvalidException
     */
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

    /***
     * Преобразует массив координат вида ["a7", "a8", "a9"] в массив вида
     * [[0, 7], [0, 8], [0, 9]].
     * @param coordinates массив координат точек на доске. Пример: ["a7", "a8", "a9"].
     * @return массив чисел. Пример: [[0, 7], [0, 8], [0, 9]].
     * @throws CoordinateIsInvalidException
     */
    private int[][] convertCoordinatesToNumbers(String... coordinates)
            throws CoordinateIsInvalidException {
        int[][] numbers = new int[coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            try {
                numbers[i][0] = coordinates[i].charAt(0) - 97;
                numbers[i][1] = coordinates[i].charAt(1) - 48;
            } catch (StringIndexOutOfBoundsException e) {
                throw new CoordinateIsInvalidException();
            }

            if (numbers[i][0] < 0 || numbers[i][0] > 9
                    || numbers[i][1] < 0 || numbers[i][1] > 9) {
                throw new CoordinateIsInvalidException();
            }
        }
        return numbers;
    }

    /***
     * Метод проверяет, что координаты представляют собой прямую линию.
     * @param coordinates координаты точек.
     * @return true - координаты представляют собой прямую линию, иначе - false.
     */
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

    /***
     * Проверяет, что на рядом стоящих и переданных клетках нет кораблей.
     * @param coordinates координаты точек для постановки корабля.
     * @return true - клетки свободны и рядом нет кораблей, иначе - false/
     */
    private boolean isShipPlacementPossible(int[][] coordinates) {
        int startI = Math.max(coordinates[0][0] - 1, 0);
        int startJ = Math.max(coordinates[0][1] - 1, 0);
        int finishI = Math.min(coordinates[coordinates.length - 1][0] + 1, SIZE - 1);
        int finishJ = Math.min(coordinates[coordinates.length - 1][1] + 1, SIZE - 1);
        for (int i = startI; i <= finishI; i++) {
            for (int j = startJ; j <= finishJ; j++) {
                if (this.grid[i][j].getStatus() != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /***
     * Заполняет доску пустыми ячейками.
     */
    private void initializeGrid() {
        this.grid = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    /***
     * Метод для отображения доски.
     * @param hideShips параметр для скрытия кораблей. true - скрыть корабли в точках,
     * где они не подбиты, false - отобразить все корабли, даже целые.
     * @return отформатированная для отображения сетка доски.
     */
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
