package org.example;

import org.example.component.Game;

import java.util.Scanner;

/***
 * Пример запуска игры
 */
public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            Game game = new Game(in);
            game.play();
        }
    }
}