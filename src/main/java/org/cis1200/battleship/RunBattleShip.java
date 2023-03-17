package org.cis1200.battleship;

import javax.swing.*;
import java.awt.*;

public class RunBattleShip implements Runnable {
    private static boolean player = true;
    private static int gameState = 0;
    private final BattleShipPlacer placingboard;
    private final BattleShipPlayer board;
    private final BattleShipPlayer board2;
    private final JPanel statusPanel;
    private final JLabel status2;
    private final JLabel status3;
    private final String infoMessage = "<html><br>Welcome to Battleship!<br>" +
            "<br> KEYBOARD CONTROLS: <br>" +
            "Press R to Rotate Ship when placing. <br> "
            +
            "Click within the grid to place ship. <br>"
            +
            "Click within the grid to guess enemy ship locations. <br><br>"
            +
            "SHIP SETTINGS:<br>" +
            "Default: Carrier Length 5, Battleship Length 4, Cruiser Length 3, " +
            "Submarine Length 3, Destroyer Length 2<br><br>"+
            "If you think you can place your ships better, press Undo! <br>" +
            "If you wish to start your placement over, press Reset – " +
            "you can take all the time you need. <br>"
            +
            "Otherwise, press Ready! You and your opponent (the computer) " +
            "will then duke it out.<br>"
            +
            "If you wish to quit, press Quit. Good luck!</html>";

    public RunBattleShip() {
        final JLabel status = new JLabel("Setting up...");
        status2 = new JLabel("Player Board");
        status3 = new JLabel("Bot Board");
        statusPanel = new JPanel();
        statusPanel.add(status);

        gameState = 0;
        player = true;

        BoardPlacing bPlacing = new BoardPlacing("me");
        BoardPlacing bPlacing2 = new BoardPlacing("enemy");
        bPlacing2.generate();

        BoardPlaying bPlaying = new BoardPlaying("me", bPlacing2);
        BoardPlaying bPlaying2 = new BoardPlaying("enemy", bPlacing);

        Player me = new Person(bPlaying, bPlacing2);
        Player enemy = new Robot(bPlaying2, bPlacing);

        placingboard = new BattleShipPlacer(status, bPlacing);
        board = new BattleShipPlayer(status2, me);
        board2 = new BattleShipPlayer(status3, enemy);
    }

    public static boolean isPlayer() {
        return player;
    }

    public static void setPlayer(boolean player) {
        RunBattleShip.player = player;
    }

    public static int getGameState() {
        return gameState;
    }

    public static void setGameState(int gameState) {
        RunBattleShip.gameState = gameState;
    }

    private class Panel1 extends JPanel {
        JPanel contentPane;

        public Panel1() {
            contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());
            // Buttons
            final JButton undo = new JButton("undo");
            final JButton reset = new JButton("reset");
            final JButton ready = new JButton("ready");
            final JButton quit = new JButton("quit");

            final JPanel player = new JPanel();
            contentPane.add(statusPanel, BorderLayout.PAGE_START);
            player.add(placingboard, BorderLayout.CENTER);
            contentPane.add(player, BorderLayout.CENTER);

            undo.addActionListener(e -> placingboard.getBoard().undo());
            ready.addActionListener(e -> {
                if (placingboard.getBoard().isImplicitlyReady()) {
                    CardLayout cardLayout = (CardLayout) contentPane.getParent().getLayout();
                    cardLayout.show(contentPane.getParent(), "Panel 2");
                    contentPane.getParent().setLayout(cardLayout);
                }
            });

            reset.addActionListener(e -> {
                placingboard.getBoard().reset();
            });

            quit.addActionListener(e -> {
                for (Frame f : Frame.getFrames()) {
                    f.dispose();
                }
            });

            final JPanel control_panel = new JPanel();
            control_panel.setBackground(Color.GRAY);

            control_panel.add(undo);
            control_panel.add(reset);
            control_panel.add(ready);
            control_panel.add(quit);

            contentPane.add(control_panel, BorderLayout.SOUTH);

        }
    }

    private class Panel2 extends JPanel {

        JPanel contentPane;

        public Panel2() {
            contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());

            JPanel player = new JPanel();
            player.setLayout(new BorderLayout());
            JPanel enemy = new JPanel();
            enemy.setLayout(new BorderLayout());

            player.add(status2, BorderLayout.NORTH);
            player.add(board, BorderLayout.CENTER);
            enemy.add(status3, BorderLayout.NORTH);
            enemy.add(board2, BorderLayout.CENTER);
            contentPane.add(player, BorderLayout.WEST);
            contentPane.add(enemy, BorderLayout.EAST);

            // Buttons
            final JButton new_game = new JButton("new game");
            final JButton quit = new JButton("quit");
            final JPanel control_panel = new JPanel();
            control_panel.setBackground(Color.GRAY);

            new_game.addActionListener(e -> {
                CardLayout cardLayout = (CardLayout) contentPane.getParent().getLayout();
                cardLayout.show(contentPane.getParent(), "Panel 1");
                contentPane.getParent().setLayout(cardLayout);
                contentPane.getTopLevelAncestor().setVisible(false);
                RunBattleShip test = new RunBattleShip();

                test.run();
            });

            quit.addActionListener(e -> {
                for (Frame f : Frame.getFrames()) {
                    f.dispose();
                }
            });
            control_panel.add(new_game);
            control_panel.add(quit);
            contentPane.add(control_panel, BorderLayout.PAGE_END);
        }

    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Battleship");
        frame.setLocation(300, 300);
        frame.setLayout(new BorderLayout());
        CardLayout c1 = new CardLayout();
        JPanel contentPane = new JPanel();
        Panel1 card1 = new Panel1();
        Panel2 card2 = new Panel2();
        contentPane.setLayout(c1);
        c1.show(contentPane, "Panel 1");
        contentPane.add(card1.contentPane, "Panel 1");
        contentPane.add(card2.contentPane, "Panel 2");

        JOptionPane.showMessageDialog(
                null, infoMessage, "Instructions", JOptionPane.INFORMATION_MESSAGE
        );
        frame.add(contentPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
