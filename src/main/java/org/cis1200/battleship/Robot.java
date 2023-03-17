package org.cis1200.battleship;

import java.util.*;

public class Robot implements Player {
    private final LinkedList<int[]> nextGuesses = new LinkedList<>();
    private final char[][] guessingBoard;
    private int rowGuess;
    private int colGuess;

    private final BoardPlaying boardPlaying;
    private final BoardPlacing opponentBoard;

    public Robot(BoardPlaying b, BoardPlacing board) {
        boardPlaying = b;
        guessingBoard = b.getBoard();
        opponentBoard = board;
    }

    public BoardPlaying getBoardPlaying() {
        return boardPlaying;
    }

    public BoardPlacing getBoardPlacing() {
        return opponentBoard;
    }

    private boolean contains(int[] check) {
        for (int[] l : nextGuesses) {
            if (Arrays.equals(l, check)) {
                return true;
            }
        }
        return false;
    }

    public int[] hunt() {
        if (guessingBoard[rowGuess][colGuess] == BattleShipModel.HIT) { // after checking, it turns
                                                                        // out current guess is hit
            HashSet<int[]> potentialNextGuesses = new HashSet<>(); // mark all adjacent guesses as
                                                                   // possible guesses
            potentialNextGuesses.add(new int[] { rowGuess + 1, colGuess });
            potentialNextGuesses.add(new int[] { rowGuess - 1, colGuess });
            potentialNextGuesses.add(new int[] { rowGuess, colGuess + 1 });
            potentialNextGuesses.add(new int[] { rowGuess, colGuess - 1 });

            for (int[] guess : potentialNextGuesses) { // for every potential guess
                if ((guess[0] > -1 && guess[0] < 10) && guess[1] > -1 && guess[1] < 10) {
                    if (guessingBoard[guess[0]][guess[1]] == BattleShipModel.EMPTY) { // if
                                                                                      // candidate
                                                                                      // guesses
                        // aren't already guessed
                        if (!contains(guess)) {
                            nextGuesses.add(guess); // add that to list of potential guess
                        }
                    }
                }
            }
        }
        // play that guess
        // play that guess
        if (nextGuesses.isEmpty()) { // if there are no guesses to be tried
            guessRandom(); // make an (educated) random guess
        } else { // if there is a list of potential guesses
            int[] huntGuess = nextGuesses.getFirst(); // take the first guess
            nextGuesses.removeFirst(); // remove it from the list
            rowGuess = huntGuess[0]; // guess as row
            colGuess = huntGuess[1]; // guess as col

        }
        return new int[] { rowGuess, colGuess }; // play that guess

    }

    public void guessRandom() {
        int guessrow, guesscol;
        while (true) {
            guessrow = (int) (Math.random() * 10);
            guesscol = (int) (Math.random() * 10);
            if ((guessrow + guesscol) % 2 == 0) { // if row + col is divisible by 2, it is every
                                                  // other square
                if (guessingBoard[guessrow][guesscol] == BattleShipModel.EMPTY) { // if guesses
                                                                                  // aren't already
                                                                                  // guessed
                    if (!contains(new int[] { guessrow, guesscol })) {
                        break;
                    }
                }
            }
        }
        rowGuess = guessrow;
        colGuess = guesscol;
    }

    public int getRowGuess() {
        return rowGuess;
    }

    public int getColGuess() {
        return colGuess;
    }
}
