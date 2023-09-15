package org.example.component;

import org.example.board.Board;
import org.example.board.Cell;
import org.example.exception.CoordinateIsInvalidException;
import org.example.exception.InvalidCoordinatesCountException;
import org.example.exception.ShipPlacementInvalidException;
import org.example.ship.*;

import java.util.Scanner;

public class Game {

    private final Scanner input;
    private Player player1;
    private Player player2;
    private int turn;

    public Game(Scanner input) {
        this.turn = 0;
        this.input = input;
        createPlayers();
    }

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

    private void createPlayers() {
        System.out.println("Укажите имя первого игрока: ");
        this.player1 = new Player(this.input.nextLine());
        System.out.println("Введите имя второго игрока: ");
        this.player2 = new Player(this.input.nextLine());
    }

    private void defineAndDisplayWinner() {
        if (this.player1.getBoard().getLives() == 0) {
            System.out.println("Победитель: !!! " + this.player2.getName() + " !!!");
        } else {
            System.out.println("Победитель: !!! " + this.player1.getName() + " !!!");
        }
    }

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

    private String[] getCoordinatesFromInput(int count) {
        String[] coordinates = new String[count];
        for (int i = 0; i < count; i++) {
            coordinates[i] = this.input.next();
        }
        return coordinates;
    }

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
