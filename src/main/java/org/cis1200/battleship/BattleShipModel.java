package org.cis1200.battleship;

public class BattleShipModel {
    public static final char MISS = 'O';
    public static final char HIT = 'X';
    public static final char EMPTY = '\u0000';
    private static boolean player1 = true; // true if it is player1 turn, false if it is player2
                                           // turn
    private static int gameState = 0; // 0 if game still playing, 1 if player1 wins, 2 if player2
                                      // wins.

    public static int getGameState() {
        return gameState;
    }

    public static void runGame(Player r1, Player r2) {
        BoardPlaying p1 = r1.getBoardPlaying();
        BoardPlaying p2 = r2.getBoardPlaying();
        while (!p1.isGameWon() && !p2.isGameWon()) {
            if (player1) {
                int[] r1Guess = r1.hunt();
                p1.playTurn(r1Guess[0], r1Guess[1]);
                p1.evaluateTurn();
            } else {
                int[] r2Guess = r2.hunt();
                p2.playTurn(r2Guess[0], r2Guess[1]);
                p2.evaluateTurn();
            }
            player1 = !player1;
        }
        if (p1.isGameWon()) {
            // System.out.println(p1.getOwner()+" won!");
            gameState = 1;
        } else {
            // System.out.println(p2.getOwner()+" won!");
            gameState = 2;
        }
    }

    public static void main(String[] args) {
        String player = "me";
        String bot = "bot";
        // Placing phase
        BoardPlacing bPlacing = new BoardPlacing(player);
        bPlacing.generate(); // place some stuff
        BoardPlacing bPlacing2 = new BoardPlacing(bot);
        bPlacing2.generate(); // will always be ready

        // Playing phase, creates new guessing board & opponent board
        BoardPlaying bPlaying = new BoardPlaying(player, bPlacing2);
        BoardPlaying bPlaying2 = new BoardPlaying(bot, bPlacing);
        Robot robot1 = new Robot(bPlaying, bPlacing2); // player
        Robot robot2 = new Robot(bPlaying2, bPlacing); // robot
        // boards are set and ready, now play!
        BattleShipModel.runGame(robot1, robot2);
        // print final state of game
        System.out.println("bot guesses: " + "\n" + bPlaying2);
        System.out.println("my board: " + "\n" + bPlacing);
        System.out.println("my guesses: " + "\n" + bPlaying);
        System.out.println("bot board: " + "\n" + bPlacing2);
        System.out.println(" gamestate: " + gameState);
    }
}
