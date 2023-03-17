package org.cis1200.battleship;

import java.util.*;

public class BoardPlacing {
    private char[][] board = new char[10][10];
    private final String owner;
    private Ship[] ships = new Ship[] {
        new Ship("carrier", 'c', "files/Carrier.png", 5, this),
        new Ship("battleship", 'b', "files/Battleship.png", 4, this),
        new Ship("cruiser", 'r', "files/Cruiser.png", 3, this),
        new Ship("submarine", 's', "files/Submarine.png", 3, this),
        new Ship("destroyer", 'd', "files/Destroyer.png", 2, this)
    };

    private final LinkedList<char[][]> moves = new LinkedList<>(); // order matters, getFirst,
                                                                   // getLast
    private final LinkedList<Ship> shipsPut = new LinkedList<>(); // order matters, getFirst,
                                                                  // getLast
    private final ArrayList<Ship> shipsNeedingPlacement = new ArrayList<>(List.of(ships)); // List
                                                                                           // is
                                                                                           // best
    // b/c ships
    // array
    private char[][] valid;

    private String status = "";

    public String getStatus() {
        return status;
    }

    public BoardPlacing(String owner) {
        this.owner = owner;
        moves.add(copy(getBoard()));
        valid = copy(moves.getLast());
    }

    public BoardPlacing(String owner, Ship[] ships) {
        this.owner = owner;
        this.ships = ships;
        moves.add(copy(getBoard()));
        valid = copy(moves.getLast());
    }

    public String getOwner() {
        return owner;
    }

    public char[][] getBoard() {
        return board;
    }

    public Ship[] getShips() {
        return ships;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public boolean isImplicitlyReady() {
        return shipsNeedingPlacement.isEmpty();
    }

    private char[][] copy(char[][] res) { // helper method to prevent aliasing boards
        return Arrays.stream(res).map(char[]::clone).toArray(char[][]::new);
    }

    private boolean helperIsInBounds(int row, int col, int posHead) {
        return ((row > -1 && row < 10) &&
                (col > -1 && col < 10) &&
                (posHead > -1 && posHead < 10));
    }

    private boolean isInBounds(int row, int col, Direction d, int l) {
        switch (d) {
            default:
            case RIGHT:
                int poscolHead = col + l - 1;
                return helperIsInBounds(row, col, poscolHead);
            case DOWN:
                int posrowHead = row + l - 1;
                return helperIsInBounds(row, col, posrowHead);
        }
    }

    private void helperPlaceShip(int row, int col, Direction d, Ship s) {
        int length = s.getShipLength();
        char[][] potential = copy(moves.getLast());
        if (!isInBounds(row, col, d, length)) {
            status = ("Placement out of bounds");
            throw new IllegalArgumentException();
        }
        switch (d) {
            default:
            case RIGHT:
                for (int c = col; c < col + length; c++) {
                    if (getBoard()[row][c] != BattleShipModel.EMPTY) {
                        setBoard(moves.getLast());
                        status = ("Space already occupied");
                        throw new IllegalArgumentException();
                    }
                    potential[row][c] = s.getId();
                    s.getShipPos()[c - col] = new int[] { row, c };
                }
                break;
            case DOWN:
                for (int r = row; r < row + length; r++) {
                    if (getBoard()[r][col] != BattleShipModel.EMPTY) {
                        setBoard(moves.getLast());
                        status = ("Space already occupied");
                        throw new IllegalArgumentException();
                    }
                    potential[r][col] = s.getId();
                    s.getShipPos()[r - row] = new int[] { r, col };
                }
                break;
        }
        moves.add(potential);
        setBoard(potential);
        shipsPut.add(s);
        shipsNeedingPlacement.remove(s);
    }

    public boolean placeShip(int row, int col, Direction d, Ship s) { // tries to place ship
        try {
            helperPlaceShip(row, col, d, s);
            s.setD(d);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isEmpty() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (getBoard()[i][j] != BattleShipModel.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public void undo() {
        if (moves.size() > 1) {
            moves.remove(moves.getLast());
            valid = moves.getLast();
            board = valid;

            shipsNeedingPlacement.add(0, shipsPut.getLast());
            shipsPut.getLast().setShipPos(new int[shipsPut.getLast().getShipLength()][2]);
            shipsPut.removeLast();
        }
    }

    public void reset() {
        while (moves.size() > 1) {
            undo();
        }
    }

    public void generate() {
        while (!isImplicitlyReady()) {
            int row = (int) (Math.random() * 10);
            int col = (int) (Math.random() * 10);
            Direction d = Direction.randomDirection();
            Ship s = shipsNeedingPlacement.get(0);
            placeShip(row, col, d, s);
        }
    }

    public ArrayList<Ship> getShipsNeedingPlacement() {
        return shipsNeedingPlacement;
    }

    public LinkedList<Ship> getShipsPut() {
        return shipsPut;
    }

    public String toString() {
        String str = "";
        for (char[] row : board) {
            for (char c : row) {
                str += (c + " ");
            }
            str += "\n";
        }
        return str;
    }
}
