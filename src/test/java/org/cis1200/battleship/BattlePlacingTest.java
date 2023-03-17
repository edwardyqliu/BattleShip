package org.cis1200.battleship;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class BattlePlacingTest {
    @Test
    public void testPlaceShip() {
        BoardPlacing testBoard = new BoardPlacing("me");
        assertTrue(testBoard.placeShip(0, 0, Direction.DOWN, testBoard.getShips()[0])); // carrier
        assertTrue(
                testBoard.placeShip(3, 3, Direction.RIGHT, testBoard.getShips()[1])
        ); // battleship
        assertFalse(testBoard.isImplicitlyReady());
        assertTrue(testBoard.placeShip(4, 6, Direction.RIGHT, testBoard.getShips()[2])); // cruiser
        assertTrue(testBoard.placeShip(7, 9, Direction.DOWN, testBoard.getShips()[3])); // submarine
        assertFalse(
                testBoard.placeShip(4, 6, Direction.RIGHT, testBoard.getShips()[4]),
                "Space already occupied"
        ); // destroyer overlaps with cruiser
        assertTrue(testBoard.placeShip(7, 8, Direction.DOWN, testBoard.getShips()[4])); // destroyer
        assertTrue(testBoard.isImplicitlyReady());

    }

    @Test
    public void testGenerateBoard() {
        BoardPlacing test = new BoardPlacing("me");
        test.generate();
        assertTrue(test.isImplicitlyReady());
        assertEquals(test.getShipsNeedingPlacement(), Collections.emptyList());
    }

    @Test
    public void testUndo() { // undo should make playing not ready
        BoardPlacing test = new BoardPlacing("me");
        test.generate();
        assertTrue(test.isImplicitlyReady());
        test.undo();
        assertFalse(test.isImplicitlyReady());
        assertEquals(1, test.getShipsNeedingPlacement().size());// undo : 0 --> 1 ship
        assertEquals(test.getShipsNeedingPlacement().get(0), test.getShips()[4]); // last ship put
                                                                                  // is destroyer
    }

    @Test
    public void testReset() {
        BoardPlacing test = new BoardPlacing("me");
        test.generate();
        assertTrue(test.isImplicitlyReady());
        test.reset();
        assertFalse(test.isImplicitlyReady()); // reset should make playing not ready
        assertTrue(test.isEmpty()); // board should be empty
        assertEquals(5, test.getShipsNeedingPlacement().size()); // reset should return all ships
    }

    @Test
    public void testUndoAndReset() { // sometimes undo & reset create funky behavior
        BoardPlacing test = new BoardPlacing("me");
        test.generate();
        test.undo(); // still have one ship yet to be put down
        test.reset();
        assertEquals(test.getShipsNeedingPlacement().get(0), test.getShips()[0]); // back to
                                                                                  // original state
    }

    @Test
    public void testResetMultipleTimes() { // should not throw exception
        BoardPlacing test = new BoardPlacing("me");
        test.generate();
        test.reset();
        test.reset();
        assertDoesNotThrow(test::getShipsNeedingPlacement);
    }

    @Test
    public void testGenerateMultipleTimes() { // should not change board
        BoardPlacing test = new BoardPlacing("me");
        test.generate();
        BoardPlacing test2 = new BoardPlacing("him");
        test2.setBoard(test.getBoard());
        test.generate();
        assertEquals(Arrays.deepToString(test.getBoard()), Arrays.deepToString(test2.getBoard()));
    }

}
