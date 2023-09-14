package org.example.ship;

public abstract class Ship {

    private final int length;
    private int hits;

    protected Ship(int length) {
        this.length = length;
        this.hits = 0;
    }

    public void hit() {
        this.hits++;
    }

    public boolean isSunk() {
        return hits == length;
    }

    public int getLength() {
        return this.length;
    }

}
