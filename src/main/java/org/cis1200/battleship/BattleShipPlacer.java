package org.cis1200.battleship;

import java.awt.event.KeyAdapter;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BattleShipPlacer extends JPanel {
    private final BoardPlacing board;
    private final ArrayList<Ship> ships;
    private final JLabel status;
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    private int index = -1;

    private Point mouse;
    private Direction d = Direction.RIGHT;

    public BattleShipPlacer(JLabel statusInit, BoardPlacing board) {
        setFocusable(true);
        this.board = board;
        this.ships = board.getShipsNeedingPlacement();
        status = statusInit;
        mouse = MouseInfo.getPointerInfo().getLocation();

        // Rotate
        addKeyListener(new KeyAdapter() { // If R key is pressed rotate
            @Override
            public void keyPressed(KeyEvent k) {
                if (k.getKeyCode() == KeyEvent.VK_R) {
                    index = (index + 1) % 2; // incr index
                    d = Direction.values()[index];
                    System.out.println(d);
                }
            }
        });

        // Select grid
        addMouseListener(new MouseAdapter() { // mouse click
            @Override
            public void mouseReleased(MouseEvent e) {
                if (ships.size() > 0) {
                    Point p = e.getPoint();
                    if (p.y < 400 && p.y > 0 && p.x < 400 & p.x > 0) {
                        if (!board.placeShip(p.y / 40, p.x / 40, d, ships.get(0))
                                && !ships.isEmpty()) {
                            status.setText(board.getStatus());
                        } else if (!ships.isEmpty()) {
                            status.setText("Setting placement for " + ships.get(0).getName());
                        } else {
                            status.setText("Ready");
                        }
                    }

                }
            }
        });

        // For preview
        addMouseMotionListener(new MouseMotionAdapter() { // tracks mouse movement on preview
            @Override
            public void mouseMoved(MouseEvent e) {
                mouse = e.getPoint();
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        requestFocusInWindow();
        super.paintComponent(g);
        // Draws board grid
        for (int i = 0; i < 11; i++) {
            g.drawLine(i * 40, 0, i * 40, 400);
            g.drawLine(0, i * 40, 400, i * 40);
        }
        if (board.getShipsNeedingPlacement().size() > 0) {
            board.getShipsNeedingPlacement().get(0).setD(d);
            if (mouse.x < 400 && mouse.x > 0 && mouse.y < 400 && mouse.y > 0) {
                board.getShipsNeedingPlacement().get(0).preview(
                        g, 40 * (int) (mouse.x / 40), 40 * (int) (mouse.y / 40), board.getOwner()
                );
            }
        }

        for (Ship s : board.getShipsPut()) {
            s.draw(g, board.getOwner());
        }

        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public BoardPlacing getBoard() {
        return board;
    }
}
