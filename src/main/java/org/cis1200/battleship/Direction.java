package org.cis1200.battleship;

// there only needs to be two directions as DOWN & RIGHT are functionally the
// same as UP & LEFT
public enum Direction {
    DOWN, RIGHT;

    public static Direction randomDirection() {
        Direction[] directions = values();
        return directions[(int) (Math.random() * 2)];
    }
}
