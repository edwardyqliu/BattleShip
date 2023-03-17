package org.cis1200.battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BattleShipPlayer extends JPanel {
    private final JLabel status; // current status text
    private final Player player;
    private final BoardPlaying guessingboard;

    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    public BattleShipPlayer(JLabel statusInit, Player p) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
        player = p;
        guessingboard = player.getBoardPlaying();
        status = statusInit;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                if (!guessingboard.isGameWon() && RunBattleShip.getGameState() == 0) {
                    if (player.getClass() == Person.class && RunBattleShip.isPlayer()) {
                        if (p.y < 400 && p.x < 400 && p.y > 0 && p.x > 0) {
                            if (guessingboard.playTurn(p.y / 40, p.x / 40)) {
                                guessingboard.evaluateTurn();
                                RunBattleShip.setPlayer(false);
                            }
                        }
                    }
                    status.setText(guessingboard.getStatus());
                } else if (guessingboard.isGameWon()) {
                    if (RunBattleShip.getGameState() == 0) {
                        RunBattleShip.setGameState(1);
                        RunBattleShip.setPlayer(true);
                    }
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!guessingboard.isGameWon() && RunBattleShip.getGameState() == 0) {
            if (!RunBattleShip.isPlayer()) {
                if (player.getClass() != Person.class) {
                    int[] guess = player.hunt();
                    guessingboard.playTurn(guess[0], guess[1]);
                    guessingboard.evaluateTurn();
                    RunBattleShip.setPlayer(true);
                }
                status.setText(guessingboard.getStatus());
            }
        } else if (guessingboard.isGameWon()) {
            if (RunBattleShip.getGameState() == 0) {
                RunBattleShip.setGameState(2);
            }
        }
        // Draws board grid
        for (int i = 0; i < 11; i++) {
            g.drawLine(i * 40, 0, i * 40, 400);
            g.drawLine(0, i * 40, 400, i * 40);
        }

        // Draws X's and O's
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int state = guessingboard.getBoard()[i][j];
                if (state == 'X') {
                    g.drawOval(10 + 40 * j, 10 + 40 * i, 20, 20);
                } else if (state == 'O') {
                    g.drawLine(10 + 40 * j, 10 + 40 * i, 30 + 40 * j, 30 + 40 * i);
                    g.drawLine(10 + 40 * j, 30 + 40 * i, 30 + 40 * j, 10 + 40 * i);
                }
            }
        }
        for (Ship s : player.getBoardPlacing().getShips()) {
            if (player.getClass() == Person.class) {
                s.draw(g, player.getBoardPlaying().getOwner());
            } else {
                s.draw(g, player.getBoardPlacing().getOwner());
            }
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public BoardPlaying getBoard() {
        return guessingboard;
    }
}
