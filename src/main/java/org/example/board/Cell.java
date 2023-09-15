package org.example.board;

import org.example.ship.Ship;

import static org.example.board.Cell.Status.*;

/***
 * Ячейка доски, на которой расположены корабли.
 * Ячейка может содержать корабль, а может быть пустой, тогда поле ship должно
 * иметь значение null.
 * Задумка в том, что на доске может быть несколько ячеек, которые имеют ссылку на
 * один и тот же корабль (если корабль 2-ух и более палубный). Когда квадрат (ячейку)
 * атакуют, корабль, на который ссылается ячейка, получает урон.
 * Ячейка также имеет статус, указывающий на текущее состояние ячейки.
 */
public class Cell {

    /***
     * Корабль, который находится (возможно частично) в этой ячейке.
     */
    private final Ship ship;
    /***
     * Статус ячейки.
     * @see Status
     */
    private Status status;

    /***
     * Создает ячейку, в которой нет кораблей.
     */
    public Cell() {
        this.ship = null;
        this.status = EMPTY;
    }

    /***
     * Создает ячейку, в которой находится (возможно частично) корабль.
     * @param ship корабль, который находится в данном квадрате.
     */
    public Cell(Ship ship) {
        this.ship = ship;
        this.status = SHIP;
    }

    /***
     * Метод атаки текущего квадрата.
     * Если в ячейке находился корабль, то после атаки будет возвращено одно из
     * двух значений: HIT - если корабль поврежден в результате атаки, SUNKEN -
     * если корабль потоплен.
     * Если в ячейке не было корабля, то статус станет MISS - промах.
     * Если же удар уже был нанесен, то статус не изменится.
     * @return результат атаки.
     */
    public Status attack() {
        if (this.status == EMPTY) {
            this.status = MISS;
        } else if (this.status == SHIP) {
            //noinspection DataFlowIssue
            this.ship.hit(); // если в квадрате находится корабль, то наносим по нему удар.
            if (this.ship.isSunk()) { // если корабль потоплен в результате удара.
                this.status = SUNKEN;
            } else {
                this.status = HIT;
            }
        }
        return this.status;
    }

    public Status getStatus() {
        return this.status;
    }

    /***
     * Перечисление для отображения статуса ячейки.
     * EMPTY - квадрат пуст, SHIP - в квадрате находится корабль (возможно
     * частично), HIT - по кораблю в квадрате был нанесен удар,
     * MISS - квадрат был атакован, но корабля там не было (промах),
     * SUNKEN - в результате удара по квадрату корабль был потоплен.
     */
    public enum Status {
        EMPTY("-"), SHIP("-", "●"),
        HIT("*"), MISS("○"),
        SUNKEN("X");

        /***
         * Открытое значение статуса. Используется для отображения доски противнику.
         * При этом скрываются корабли (только если по ним не был нанесен удар в данном
         * квадрате).
         */
        private final String publicValue;
        /***
         * Закрытое значение. Используется для отображения своих кораблей
         * игроку-владельцу флота.
         */
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
