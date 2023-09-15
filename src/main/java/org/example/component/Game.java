package org.example.component;

import org.example.board.Board;
import org.example.board.Cell;
import org.example.exception.CoordinateIsInvalidException;
import org.example.exception.InvalidCoordinatesCountException;
import org.example.exception.ShipPlacementInvalidException;
import org.example.ship.*;

import java.util.Scanner;

/***
 * Игра морской бой.
 * Содержит двух игроков. Позволяет указать откуда читать данные. Определяет чья очередь атаковать.
 */
public class Game {

    /***
     * Входной поток в виде сканера.
     */
    private final Scanner input;
    /***
     * Первый игрок.
     */
    private Player player1;
    /***
     * Второй игрок.
     */
    private Player player2;
    /***
     * Очередь делать ход. 0 - первого игрока, 1 - второго.
     */
    private int turn;

    public Game(Scanner input) {
        this.turn = 0;
        this.input = input;
        createPlayers();
    }

    /***
     * Запускает игру.
     */
    public void play() {
        Board board1 = new Board();
        Board board2 = new Board();
        System.out.println(player1.getName() + ", ваш черед расставлять корабли!");
        player1.setBoard(placeShips(board1));
        System.out.println(player2.getName() + ", ваш черед расставлять корабли!");
        player2.setBoard(placeShips(board2));

        while (board1.getLives() > 0 && board2.getLives() > 0) {
            if (turn == 0) {
                System.out.println(player1.getName() + ", твой ход!");
                animateAttackAndUpdateTurn(board2);
            } else {
                System.out.println(player2.getName() + ", твой ход!");
                animateAttackAndUpdateTurn(board1);
            }
        }
        defineAndDisplayWinner();
    }

    /***
     * Предлагает игрокам представиться. Для ввода использует входной поток this.input.
     */
    private void createPlayers() {
        System.out.println("Укажите имя первого игрока: ");
        this.player1 = new Player(this.input.nextLine());
        System.out.println("Введите имя второго игрока: ");
        this.player2 = new Player(this.input.nextLine());
    }

    /***
     * Определяет победителя в конце игры.
     */
    private void defineAndDisplayWinner() {
        if (this.player1.getBoard().getLives() <= 0) {
            System.out.println("Победитель: !!! " + this.player2.getName() + " !!!");
        } else {
            System.out.println("Победитель: !!! " + this.player1.getName() + " !!!");
        }
    }

    /***
     * Выводит сообщение о результате атаки.
     * Если было попадание по кораблю противника, то игрок делает еще ход, пока не промахнется.
     * Если был совершен промах, то передает очередь следующему игроку.
     * @param board доска которую нужно атаковать.
     */
    private void animateAttackAndUpdateTurn(Board board) {
        System.out.println(board.displayBoard(true));
        switch (attackWithRetries(board)) {
            case HIT -> {
                System.out.println("Есть попадание!");
            }
            case SUNKEN -> {
                System.out.println("Уничтожен!");
            }
            default -> {
                System.out.println("Промах!");
                this.turn = (this.turn + 1) % 2;
            }
        }
    }

    /***
     * Предлагает игроку указать координаты для атаки, пока он не введет их без ошибок.
     * @param board доска которую нужно атаковать.
     * @return результат атаки квадрата: HIT, MISS или SUNKEN.
     */
    private Cell.Status attackWithRetries(Board board) {
        System.out.println("Введите координаты точки для атаки: ");
        while (true) {
            try {
                return board.attack(getCoordinatesFromInput(1)[0]);
            } catch (CoordinateIsInvalidException e) {
                System.out.print("Введено неверное значение координаты. Повторите попытку: ");
            }
        }
    }

    /***
     * Предлагает игроку разместить все корабли на доске.
     * Это: 1 линкор (4 клетки), два крейсера (3 клетки), три эсминца (2 клетки),
     * четыре торпедных катера (1 клетка).
     * @param board доска которую нужно атаковать.
     * @return доску с размещенными кораблями.
     */
    private Board placeShips(Board board) {
        System.out.println(board.displayBoard(false));
        System.out.print("Введите координаты 4-ёх точек для постановки линкора: ");
        addShipToBoardWithRetries(board, new Battleship());

        for (int i = 1; i <= 2; i++) {
            System.out.println(board.displayBoard(false));
            System.out.print("Введите координаты 3-ёх точек для постановки " + i + "-го крейсера: ");
            addShipToBoardWithRetries(board, new Cruiser());
        }

        for (int i = 1; i <= 3; i++) {
            System.out.println(board.displayBoard(false));
            System.out.print("Введите координаты 2-ух точек для постановки " + i + "-го эсминца: ");
            addShipToBoardWithRetries(board, new Destroyer());
        }

        for (int i = 1; i <= 4; i++) {
            System.out.println(board.displayBoard(false));
            System.out.print("Введите координаты точки для постановки " + i + "-го торпедного катера: ");
            addShipToBoardWithRetries(board, new TorpedoBoat());
        }
        System.out.println(board.displayBoard(false));
        return board;
    }

    /***
     * Запрашивает указанное количество точек из входного канала.
     * @param count количество точек.
     * @return массив координат вида ["a7", "a8", "a9"].
     */
    private String[] getCoordinatesFromInput(int count) {
        String[] coordinates = new String[count];
        for (int i = 0; i < count; i++) {
            coordinates[i] = this.input.next();
        }
        return coordinates;
    }

    /***
     * Предлагает игроку добавить корабль на доску, пока не будут введены валидные координаты.
     * @param board доска, на которую нужно добавить корабль.
     * @param ship корабль, который нужно добавить.
     */
    private void addShipToBoardWithRetries(Board board, Ship ship) {
        boolean valid = false;
        while (!valid) {
            try {
                board.addShip(ship, getCoordinatesFromInput(ship.getLength()));
                valid = true;
            } catch (InvalidCoordinatesCountException e) {
                System.out.print("Введено неверное количество координат. Повторите попытку: ");
            } catch (CoordinateIsInvalidException e) {
                System.out.print("Введено неверное значение координаты. Повторите попытку: ");
            } catch (ShipPlacementInvalidException e) {
                System.out.print("Корабль не может быть поставлен по переданным координатам. Повторите попытку: ");
            }
        }
    }

}
