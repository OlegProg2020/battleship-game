package org.example.ship;

/***
 * Абстрактный класс корабля
 */
public abstract class Ship {

    /***
     * Количество клеток, которые занимает корабль
     */
    private final int length;
    /***
     * Количество попаданий по кораблю
     */
    private int hits;

    protected Ship(int length) {
        this.length = length;
        this.hits = 0;
    }

    /***
     * Фиксирует попадание в корабль
     */
    public void hit() {
        this.hits++;
    }

    /***
     * Проверяет, потоплен ли корабль
     * @return true - если корабль потоплен, false - если нет
     */
    public boolean isSunk() {
        return hits >= length;
    }

    public int getLength() {
        return this.length;
    }

}
