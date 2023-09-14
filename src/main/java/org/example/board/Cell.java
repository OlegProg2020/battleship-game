package org.example.board;

import org.example.ship.Ship;

public class Cell {

    private final Ship ship;
    private Status status;

    public Cell() {
        this.ship = null;
        this.status = Status.EMPTY;
    }

    public Cell(Ship ship) {
        this.ship = ship;
        this.status = Status.SHIP;
    }

    public Status attack() {
        if (this.status == Status.EMPTY) {
            this.status = Status.MISS;
        } else if (this.status == Status.SHIP) {
            //noinspection DataFlowIssue
            ship.hit();
            this.status = Status.HIT;
        }
        return this.status;
    }

    public Status getStatus() {
        return this.status;
    }

    public enum Status {
        EMPTY("-"), SHIP("-", "●"),
        HIT("X"), MISS("○");
        private final String publicValue;
        private final String privateValue;

        Status(String value) {
            this.publicValue = value;
            this.privateValue = value;
        }

        Status(String publicValue, String privateValue) {
            this.publicValue = publicValue;
            this.privateValue = privateValue;
        }

        public String getPublicValue() {
            return this.publicValue;
        }

        public String getPrivateValue() {
            return this.privateValue;
        }
    }

}
