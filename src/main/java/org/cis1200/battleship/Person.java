package org.cis1200.battleship;

import javax.swing.*;

public class Person extends JPanel implements Player {
    // this class instantiates a person player

    private BoardPlaying boardPlaying;

    private BoardPlacing opponentBoard;

    public Person(BoardPlaying b, BoardPlacing opponentBoard) {
        boardPlaying = b;
        this.opponentBoard = opponentBoard;
    }

    public int[] hunt() { // returns position of click based on mouseclick
        return null;
    }

    public BoardPlaying getBoardPlaying() {
        return boardPlaying;
    }

    public BoardPlacing getBoardPlacing() {
        return opponentBoard;
    }
}
