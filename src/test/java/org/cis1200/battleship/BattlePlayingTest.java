package org.cis1200.battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BattlePlayingTest {

    // PlayTurn:
    // assert X works properly
    // assert O works properly

    @Test
    public void testPlayTurn() {
        BoardPlacing test = new BoardPlacing("me");
        test.placeShip(0, 0, Direction.RIGHT, test.getShips()[0]);// carrier of length 5
        test.placeShip(1, 0, Direction.RIGHT, test.getShips()[1]);// battleship of length 4
        test.placeShip(2, 0, Direction.RIGHT, test.getShips()[2]);// cruiser of length 3
        test.placeShip(3, 0, Direction.RIGHT, test.getShips()[3]);// submarine of length 3
        test.placeShip(4, 0, Direction.RIGHT, test.getShips()[4]);// destroyer of length 2
        BoardPlaying game = new BoardPlaying("me", test);
        game.playTurn(0, 0);
        assertEquals(game.getBoard()[0][0], 'X'); // carrier is at (0,0)
        assertEquals(game.getBoard()[0][1], '\u0000'); // didn't check (0,1) yet
        game.playTurn(5, 0);
        assertEquals(game.getBoard()[5][0], 'O'); // miss at (5,0)
    }

    // Ship Test:
    // assert updateShipState works correctly
    // assert isHit works correctly
    // assert isSunk works correctly
    // assert making own ships works correctly

    @Test
    public void testShip() {
        BoardPlacing test = new BoardPlacing("enemy");
        Ship x = new Ship("test_ship", 't', "", 2, test);
        test.placeShip(0, 0, Direction.DOWN, x);
        BoardPlaying game = new BoardPlaying("me", test);
        x.setGuessingBoard(game); // sets Guessing Board to this Ship
        game.playTurn(0, 0); // it's a hit
        x.updateShipState();
        assertTrue(x.isHit());
        assertFalse(x.isSunk());
        game.playTurn(1, 0); // hit and sunk
        assertTrue(x.isHit());
        assertTrue(x.isSunk());
        game.playTurn(2, 0); // still sunk but is a miss
        assertTrue(x.isSunk());
        assertFalse(x.isHit());
        game.playTurn(1, 0); // still sunk and since sunk no more hits
        assertFalse(x.isHit());
        assertTrue(x.isSunk());
    }

    @Test
    public void testShipWithNoGuessingBoardFails() {
        BoardPlacing test = new BoardPlacing("enemy");
        Ship x = new Ship("test_ship", 't', "", 2, test);
        test.placeShip(0, 0, Direction.RIGHT, x);
        BoardPlaying game = new BoardPlaying("me", test);
        game.playTurn(0, 0);
        assertThrows(NullPointerException.class, x::updateShipState);

    }

    @Test
    public void testShipWithNoBoardFails() {
        Ship testShip = new Ship("me", 'm', "", 2);
        BoardPlacing test = new BoardPlacing("me");
        test.placeShip(0, 0, Direction.RIGHT, testShip);
        assertThrows(NullPointerException.class, testShip::updateShipState);
    }

    @Test
    public void testShipVisibility() {
        BoardPlacing test = new BoardPlacing("enemy");
        BoardPlacing test2 = new BoardPlacing("me");
        test.generate();
        BoardPlaying test3 = new BoardPlaying("me", test);
        BoardPlaying test4 = new BoardPlaying("enemy", test2);
        for (Ship s : test.getShips()) {
            assertFalse(s.isVisible(test3.getOwner()));
            assertTrue(s.isVisible(test4.getOwner()));
        }
        for (Ship s : test2.getShips()) {
            assertFalse(s.isVisible(test4.getOwner()));
            assertTrue(s.isVisible(test3.getOwner()));
        }
    }

    // EvaluateTurn:
    // assert GameWon works correctly
    @Test
    public void testEvaluateTurnGameWon() {
        Ship testShip = new Ship("me", 'm', "", 2);
        BoardPlacing test = new BoardPlacing("me", new Ship[] { testShip });
        testShip.setBoard(test);
        assertFalse(test.isImplicitlyReady());
        test.placeShip(0, 0, Direction.DOWN, testShip);
        BoardPlaying game = new BoardPlaying("me", test);
        testShip.setGuessingBoard(game);
        game.playTurn(0, 0);
        game.evaluateTurn();
        assertFalse(game.isGameWon());
        game.playTurn(1, 0);
        game.evaluateTurn();
        assertTrue(game.isGameWon());
    }
}
