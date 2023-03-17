package org.cis1200.battleship;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Invariants:
// Each ship must have unique name, ID & image_path
public class Ship {
    private final String name;
    private final char id;
    private final int shipLength;
    private BoardPlacing board = null;

    private char[][] guessingBoard = null;

    private BufferedImage img;

    private Direction d;

    private int[][] shipPos; // relative to board
    private int shipState; // relative to self
    private int shipStateHelper;

    public Ship(String name, char id, String imagePath, int shipLength, BoardPlacing board) {
        this.name = name;
        this.id = id;
        this.shipLength = shipLength;
        shipState = shipLength;
        shipStateHelper = shipLength;
        this.board = board;
        shipPos = new int[shipLength][2]; // Position on board [[0,0][1,2],[3,4],[5,6]]
        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public Ship(String name, char id, String imagePath, int shipLength) {
        this.name = name;
        this.id = id;
        this.shipLength = shipLength;
        shipStateHelper = shipLength;
        shipPos = new int[shipLength][2]; // Position on board [[0,0][1,2],[3,4],[5,6]]
        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public char getId() {
        return id;
    }

    public int getShipLength() {
        return shipLength;
    }

    public void setD(Direction d) {
        this.d = d;
    }

    public int[][] getShipPos() {
        return shipPos;
    }

    public void setShipPos(int[][] shipPos) {
        this.shipPos = shipPos;
    }

    public int getShipState() {
        return shipState;
    }

    public void updateShipState() {
        int result = 0;
        for (int i = 0; i < shipLength; i++) {
            if (guessingBoard[getShipPos()[i][0]][getShipPos()[i][1]] == BattleShipModel.HIT) {
                result += 1;
            }
        }
        shipState = shipLength - result; // returns how many elements of ship are still afloat
    }

    public boolean isHit() { // if floating parts doesn't match ship state presently, ship must have
                             // been hit
        updateShipState();
        if (shipStateHelper != getShipState()) {
            shipStateHelper = getShipState();
            return true;
        }
        return false;
    }

    public boolean isSunk() { // no more floating parts means ship is sunk
        return getShipState() == 0;
    }

    public boolean isVisible(String person) { // ship should only be seen by owner of board or if
                                              // ship is sunk
        if (person.equals(board.getOwner())) {
            return true;
        } else {
            return isSunk();
        }

    }

    public BufferedImage addRotation(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(h, w, src.getType());
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                dest.setRGB(y, w - x - 1, src.getRGB(x, y));
            }
        }
        return dest;
    }

    public void draw(Graphics g, String person) {
        BufferedImage output = addRotation(img);
        if (isVisible(person) && d == Direction.RIGHT) {
            g.drawImage(
                    img, 10 + 40 * getShipPos()[0][1], 40 * getShipPos()[0][0],
                    img.getWidth() * shipLength / 25, img.getHeight() / 7, null
            );
        } else if (isVisible(person)) {
            g.drawImage(
                    output, 40 * getShipPos()[0][1], 40 * getShipPos()[0][0],
                    output.getWidth() / 7, output.getHeight() * shipLength / 25, null
            );
        }
    }

    public void preview(Graphics g, int posx, int posy, String person) {
        BufferedImage output = addRotation(img);
        if (isVisible(person) && d == Direction.RIGHT) {
            g.drawImage(
                    img, posx, posy,
                    img.getWidth() * shipLength / 25, img.getHeight() / 7, null
            );
        } else if (isVisible(person)) {
            g.drawImage(
                    output, posx, posy,
                    output.getWidth() / 7, output.getHeight() * shipLength / 25, null
            );
        }
    }

    // necessary declarations if adding ships not in base game
    public void setBoard(BoardPlacing b) {
        this.board = b;
    }

    public void setGuessingBoard(BoardPlaying b) {
        this.guessingBoard = b.getBoard();
    }
}
