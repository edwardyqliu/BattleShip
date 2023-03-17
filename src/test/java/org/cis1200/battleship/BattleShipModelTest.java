package org.cis1200.battleship;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class BattleShipModelTest {
    // Robot tests:
    // guessRandom: for 50 random Guesses ensure that they are all % 5
    // hunt: for 100 Guesses, do not guess again
    @Test
    public void testGuessRandom() {
        HashSet<String> testset = new HashSet<>();
        BoardPlacing test = new BoardPlacing("enemy");
        BoardPlaying game = new BoardPlaying("me", test);

        Robot testBot = new Robot(game, test);

        for (int i = 0; i < 50; i++) {
            testBot.guessRandom();
            int row = testBot.getRowGuess();
            int col = testBot.getColGuess();
            game.playTurn(row, col);
            assertFalse(testset.contains(Arrays.toString((new int[] { row, col }))));
            testset.add(Arrays.toString(new int[] { row, col }));
        }
    }

    @Test
    public void testHunt() {
        HashSet<String> testset = new HashSet<>();
        Ship x = new Ship("test_ship", 't', "", 10);
        BoardPlacing test = new BoardPlacing("enemy", new Ship[] { x });
        for (int i = 0; i < 10; i++) {
            test.placeShip(0, i, Direction.DOWN, x);
        }
        x.setBoard(test);
        BoardPlaying game = new BoardPlaying("me", test);
        Robot testBot = new Robot(game, test);
        for (int i = 0; i < 100; i++) {
            testBot.hunt();
            int row = testBot.getRowGuess();
            int col = testBot.getColGuess();
            game.playTurn(row, col);
            assertFalse(testset.contains(Arrays.toString((new int[] { row, col }))));
            testset.add(Arrays.toString(new int[] { row, col }));
        }
    }

    // Battleship tests:
    // for 20 games with robot:
    // a) assert no TimeoutExceptions
    // b) assert no other Exceptions are thrown
    // c) assert that winner truly won:
    // 1) All enemy ships are sunk
    // 2) Game state is always correctly displayed
    @Test
    public void testBattleShip() {
        for (int i = 0; i < 20; i++) {
            assertTimeoutPreemptively(
                    Duration.ofSeconds(20), () -> {
                        BoardPlacing test1 = new BoardPlacing("player1");
                        BoardPlacing test2 = new BoardPlacing("player2");
                        test1.generate();
                        test2.generate();
                        BoardPlaying game1 = new BoardPlaying("player1", test2);
                        BoardPlaying game2 = new BoardPlaying("player2", test1);
                        Robot robot1 = new Robot(game1, test1);
                        Robot robot2 = new Robot(game2, test2);
                        BattleShipModel.runGame(robot1, robot2);
                        // if player 1 wins
                        if (BattleShipModel.getGameState() == 1) {
                            for (Ship s : test2.getShips()) {
                                assertTrue(s.isSunk());
                            }
                            boolean flag = false;
                            for (Ship s : test1.getShips()) {
                                if (!s.isSunk()) {
                                    flag = true;
                                }
                            }
                            assertTrue(flag);
                            assertTrue(game1.isGameWon());
                            // if player 2 wins
                        } else if (BattleShipModel.getGameState() == 2) {
                            for (Ship s : test1.getShips()) {
                                assertTrue(s.isSunk());
                            }
                            boolean flag = false;
                            for (Ship s : test2.getShips()) {
                                if (!s.isSunk()) {
                                    flag = true;
                                }
                            }
                            assertTrue(flag);
                            assertTrue(game2.isGameWon());
                        }
                    }
            );
        }
    }
}
