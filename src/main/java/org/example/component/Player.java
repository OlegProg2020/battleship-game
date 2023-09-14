package org.example.component;

import org.example.board.Board;

public class Player {

    private final String name;
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
