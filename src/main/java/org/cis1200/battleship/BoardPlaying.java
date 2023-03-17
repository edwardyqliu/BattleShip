package org.cis1200.battleship;

public class BoardPlaying {
    private boolean gameWon = false;
    private boolean playerTurn = true;
    private final char[][] guessingBoard = new char[10][10];
    private final BoardPlacing opponentBoard;
    private final String owner;
    private String status;

    public BoardPlaying(String owner, BoardPlacing b) {
        this.owner = owner;
        opponentBoard = b;
        for (Ship s : b.getShips()) {
            s.setGuessingBoard(this);
        }
    }

    public String getStatus() {
        return status;
    }

    public char[][] getBoard() {
        return guessingBoard;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    // player makes turn
    public boolean playTurn(int row, int col) {
        while (playerTurn) {
            if (guessingBoard[row][col] != BattleShipModel.EMPTY) {
                status = "can't replay existing position!";
                return false;
            } else {
                status = owner + " guessed " + row + ", " + col;
                System.out.println(status);
                if (opponentBoard.getBoard()[row][col] != BattleShipModel.EMPTY) {
                    guessingBoard[row][col] = BattleShipModel.HIT;
                } else {
                    guessingBoard[row][col] = BattleShipModel.MISS;
                }
                playerTurn = false;
            }
        }
        playerTurn = true;
        return true;
    }

    public void evaluateTurn() {
        boolean allSunk = true;
        for (Ship s : opponentBoard.getShips()) {
            s.updateShipState();
            if (s.isHit()) {
                status = owner + " hit " + s.getName() + "!";
                if (s.getShipState() == 0) {
                    status = owner + " sunk " + s.getName() + "!";
                }
                System.out.println(status);
            }
            if (!s.isSunk()) {
                allSunk = false;
            }
        }
        if (allSunk) {
            gameWon = true;
            status = (owner + " wins game!");
        }
    }

    public String toString() {
        String str = "";
        for (char[] row : guessingBoard) {
            for (char c : row) {
                str += (c + " ");
            }
            str += "\n";
        }
        return str;
    }
}
