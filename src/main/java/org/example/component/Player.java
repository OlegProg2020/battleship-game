package org.example.component;

import org.example.board.Board;

/***
 * Сущность игрока.
 * Каждый игрок имеет имя, по которому к нему можно обращаться.
 * За каждым игроком закреплена доска, на которой расположены его корабли.
 */
public class Player {

    /***
     * Имя игрока.
     */
    private final String name;
    /***
     * Доска игрока.
     */
    private Board board;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

}
