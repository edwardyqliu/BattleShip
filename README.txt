=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Array
I used 2 2D Arrays to model the boards for placing Ships and guessing Ships respectively. I felt
that there needed to be 2 separate arrays to achieve this because it would be easier to test these
components separatedly and paint the 2 boards in the viewing phase.

  2. Collections
I used Collections for my implementation of Board Placing so that it would be possible to program and perform
reset/ undo functions when placing the board.

  3. JUnit Testable Component
I made tests for the model of the game. These tests tested the ship class, the placing board, the playing board and
the model as a whole. These tests also tested the edge cases when ships were put in invalid positions, or when undo is
pressed too many times.
  4. AI
I made a Bot class that plays against the player in the playing phase. It takes in a guessing board and puts out its
guess (int row, int col). The bot operates in two phases: the guessing phase and the targeting phase. While keeping
track of past guesses at all times, the bot makes random guesses until it hits a ship, then it guesses adjacent squares
recursively until there are no more valid guesses left. For extra challenge, place your ships next to each other and
try to beat the AI!
=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Direction – enum class that also has random direction function (useful when generating board)
Ship – ship class that contains information about ship as well as functions to draw each ship
BoardPlacing – board for the placing phase of the game
BoardPlaying –board for the playing phase of the game
Player – interface for humans and robots since they are both players
Person – class for human players
Robot – class for robot, contains the AI
BattleShipModel – just for demonstrating the model, helps run test cases of model
BattleShipPlacer – JPanel class for drawing BoardPlacing, contains controllers
BattleShipPlayer – JPanel class for drawing BoardPlaying, contains controllers
RunBattleShip – Top level frame for running Battleship


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

Yes, the major roadblock was dealing with the 2 phases of Battleship: placing & playing.
It made everything from making the model to running the GUI twice as complex.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think there is a very good separation of functionality, but I would put the controller into the Person interface
next time. Encapsulation was good because I tried to avoid referring to game boards directly
as 2D arrays between classes. I think I'm most happy with my BoardPlacing class and the undo functions,
because it works well. If I could refactor, I would also reduce the number of classes used and try to
fit the Game Model into one board instead of two.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

Ship images: https://www.freepik.com/free-vector/set-silhouettes-naval-ships_11052928.htm#query=navy%20battleship&position=0&from_view=keyword
Array Copy method help: https://www.baeldung.com/java-array-copy
Robot AI implementation basis: https://towardsdatascience.com/coding-an-intelligent-battleship-agent-bf0064a4b319