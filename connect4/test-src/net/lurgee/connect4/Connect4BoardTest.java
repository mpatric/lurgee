/*
 * @(#)Connect4BoardTest.java		2007/03/07
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import net.lurgee.sgf.Debug;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.Position;

/**
 * Unit tests for {@link net.lurgee.connect4.Connect4Board}.
 * @author mpatric
 */
public class Connect4BoardTest extends TestCase {

	private static final Connect4Player redPlayer = Connect4Player.getInstance(Colour.RED);
	private static final Connect4Player yellowPlayer = Connect4Player.getInstance(Colour.YELLOW);
	
	private static final String BOARD_WITH_VARYING_TOPS =
		"  1 2 3 4 5 6 7 \n" +
		"6 O - - - - - - \n" + 
		"5 O O - - - - - \n" +
		"4 X X O - - - - \n" +
		"3 X O O O - - - \n" +
		"2 X X X O O - - \n" +
		"1 X O O X O O - ";
	
	private static final String BOARD_FOR_CHECKING_VALID_MOVES =
		"  1 2 3 4 5 6 7 \n" +
		"6 - O - X - - - \n" + 
		"5 - X O O - - - \n" +
		"4 O O X X X O - \n" +
		"3 X X O O O X - \n" +
		"2 O O X X X O - \n" +
		"1 X X O O O X - ";
	
	private static final String BOARD_WITH_ONE_FULL_COLUMN =
		"  1 2 3 4 5 6 7 \n" +
		"6 O - - - - - - \n" +
		"5 O X - - - - - \n" +
		"4 X X - X - - - \n" +
		"3 X O - O - - - \n" +
		"2 O O O X - - - \n" +
		"1 O O X O X - X ";
	
	private static final String BOARD_WITH_GROUPS =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - - - - \n" +
		"5 X - - - - - - \n" +
		"4 O - - - - - - \n" +
		"3 X - O O - - - \n" +
		"2 X X O X X X - \n" +
		"1 X X O X O X - ";
	
	private static final String BOARD_WITH_SEVERAL_GROUPS_OF_FOUR =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - X - - \n" + 
		"5 - X - - X - O \n" +
		"4 - X - X O - O \n" +
		"3 O O O O O - O \n" +
		"2 X X O X X - O \n" +
		"1 X O X X O O X ";
	
	private static final String BOARD_WITH_ONE_GROUP_OF_FOUR =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - - - - \n" + 
		"5 O - X - - - - \n" +
		"4 X O X - - - - \n" +
		"3 X X O X - - - \n" +
		"2 O O X O - - - \n" +
		"1 X O O O - - - ";
	
	private GameContext gameContext;
	private Connect4Board board;
	private Connect4MoveFactory connect4MoveFactory;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] { Connect4Player.getInstance(Colour.RED), Connect4Player.getInstance(Colour.YELLOW) };
		ObjectPool connect4BoardPool = new ObjectPool(Connect4Board.class);
		connect4MoveFactory = new Connect4MoveFactory();
		gameContext = new GameContext(players, connect4BoardPool, connect4MoveFactory, false);
		board = (Connect4Board) gameContext.checkOutBoard();
	}
	
	@Override
	protected void tearDown() throws Exception {
		gameContext.checkInBoard(board);
	}
	
	public void testTwoFreshlyInitialisedBoardsShouldBeEqualAndConvertToTheSameString() {
		board.initialise();
		Connect4Board board2 = (Connect4Board) gameContext.checkOutBoard();
		board2.initialise();
		assertEquals("boards should be equal", board, board2);
		assertEquals("strings from boards should be equal", board.toString(), board2.toString());
		gameContext.checkInBoard(board2);
	}
	
	public void testInitialisedBoardShouldHaveNoPiecesPlacedAndCorrectNumberOfFreeSquares() throws Exception {
		board.initialise();
		assertEquals(redPlayer, board.getCurrentPlayer());
		assertEquals("empty square count is wrong", 6 * 7, board.countMaxMovesLeft());
		assertEquals("occupied square count is wrong", 0, board.countMovesMade());
	}
	
	public void testShouldSetTheTops() throws Exception {
		board.fromString(BOARD_WITH_VARYING_TOPS);
		board.setCurrentPlayer(redPlayer);
		assertEquals(6, board.getTop(1));
		assertEquals(5, board.getTop(2));
		assertEquals(4, board.getTop(3));
		assertEquals(3, board.getTop(4));
		assertEquals(2, board.getTop(5));
		assertEquals(1, board.getTop(6));
		assertEquals(0, board.getTop(7));
	}
	
	public void testCopiedBoardIsEqualToOriginal() throws Exception {
		board.fromString(BOARD_WITH_VARYING_TOPS);
		board.setCurrentPlayer(redPlayer);
		Connect4Board board2 = (Connect4Board) gameContext.checkOutBoard();
		board2.copy(board);
		assertEquals("copied board not equal to original", board, board2);
		gameContext.checkInBoard(board2);
	}

	public void testShouldPlayAValidMoveSuccessfully() throws Exception {
		board.initialise();
		Move move = connect4MoveFactory.createMove(4);
		List<Position> changes = new ArrayList<Position>();
		int result = board.playMove(move, changes, false);
		assertEquals("result of playing move wrong", 1, result);
		assertEquals("target square wrong colour after playing move", Colour.RED, board.getSquare(4, 1));
		assertEquals("occupied square count wrong", 1, board.countMovesMade());
		assertEquals("list of changes wrong length", 1, changes.size());
		assertTrue("list of changes does not include expected position", changes.contains(new Connect4Position(4, 1)));
	}
	
	public void testShouldPlayASeriesOfValidMovesSuccessfully() throws Exception {
		Debug.output("testShouldPlayASeriesOfValidMovesSuccessfully");
		board.initialise();
		Move move = connect4MoveFactory.createMove(4);
		int result = board.playMove(move, null, false);
		Debug.output("Playing Red at " + move + "\n" + board.toString());
		assertEquals("result of playing move wrong", 1, result);
		result = board.playMove(move, null, false);
		Debug.output("Playing Yellow at " + move + "\n" + board.toString());
		assertEquals("result of playing move wrong", 1, result);
		result = board.playMove(move, null, false);
		Debug.output("Playing Red at " + move + "\n" + board.toString());
		assertEquals("result of playing move wrong", 1, result);
		result = board.playMove(move, null, false);
		Debug.output("Playing Yellow at " + move + "\n" + board.toString());
		assertEquals("result of playing move wrong", 1, result);
		board.setCurrentPlayer(yellowPlayer);
		result = board.playMove(move, null, false);
		Debug.output("Playing Yellow at " + move + "\n" + board.toString());
		assertEquals("result of playing move wrong", 1, result);
		board.setCurrentPlayer(yellowPlayer);
		result = board.playMove(move, null, false);
		Debug.output("Playing Yellow at " + move + "\n" + board.toString());
		assertEquals("result of playing move wrong", 1, result);
		assertEquals("square wrong colour", Colour.RED, board.getSquare(4, 1));
		assertEquals("square wrong colour", Colour.YELLOW, board.getSquare(4, 2));
		assertEquals("square wrong colour", Colour.RED, board.getSquare(4, 3));
		assertEquals("square wrong colour", Colour.YELLOW, board.getSquare(4, 4));
		assertEquals("square wrong colour", Colour.YELLOW, board.getSquare(4, 5));
		assertEquals("square wrong colour", Colour.YELLOW, board.getSquare(4, 6));
		assertEquals("occupied square count wrong", 6, board.countMovesMade());
	}
	
	public void testShouldDetermineValidMoves() throws Exception {
		board.fromString(BOARD_FOR_CHECKING_VALID_MOVES);
		board.setCurrentPlayer(redPlayer);
		assertTrue("should be a valid move", board.isValidMove(1));
		assertFalse("should be an invalid move", board.isValidMove(2));
		assertTrue("should be a valid move", board.isValidMove(3));
		assertFalse("should be an invalid move", board.isValidMove(4));
		assertTrue("should be a valid move", board.isValidMove(5));
		assertTrue("should be a valid move", board.isValidMove(6));
		assertTrue("should be a valid move", board.isValidMove(7));
	}
	
	public void testShouldReturnValidMovesOnBoardWithOneFullColumn() throws Exception {
		board.fromString(BOARD_WITH_ONE_FULL_COLUMN);
		board.setCurrentPlayer(redPlayer);
		List<Move> moves = board.getValidMoves(null, 1);
		int count = moves.size();
		assertEquals("number of valid moves wrong", 6, count);
		assertTrue("valid move list missing a move", moves.contains(connect4MoveFactory.createMove(2)));
		assertTrue("valid move list missing a move", moves.contains(connect4MoveFactory.createMove(3)));
		assertTrue("valid move list missing a move", moves.contains(connect4MoveFactory.createMove(4)));
		assertTrue("valid move list missing a move", moves.contains(connect4MoveFactory.createMove(5)));
		assertTrue("valid move list missing a move", moves.contains(connect4MoveFactory.createMove(6)));
		assertTrue("valid move list missing a move", moves.contains(connect4MoveFactory.createMove(7)));
		gameContext.checkInBoard(board);
	}
	
	public void testShouldCountUnbrokenGroupsOfFourWhenGroupOfTwoBecomesThreeThenFourThenFiveThenSix() throws Exception {
		Debug.output("testShouldCountUnbrokenGroupsWhenGroupOfTwoBecomesThreeThenFourThenFiveThenSix");
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(2), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 4, 0, 0, 0);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(3), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 5, 2, 0, 0);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(4), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 8, 1, 2, 0);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(5), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 9, 1, 2, 1);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(6), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 11, 0, 2, 2);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(7), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 13, 0, 1, 3);
		gameContext.checkInBoard(board);
	}
	
	public void testShouldCountUnbrokenGroupsOfFourWhenGroupOfTwoAndSingletonBecomeGroupOfFour() throws Exception {
		Debug.output("testShouldCountUnbrokenGroupsWhenGroupOfTwoAndSingletonBecomeGroupOfFour");
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(2), null, false);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(3), null, false);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(5), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 7, 2, 1, 0);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(4), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 9, 1, 2, 1);
	}
	
	public void testShouldCountUnbrokenGroupsOfFourWhenGroupOfTwoAndGroupOfThreeBecomeGroupOfSix() throws Exception {
		Debug.output("testShouldCountGroupsWhenGroupOfTwoAndGroupOfThreeBecomeGroupOfSix");
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(2), null, false);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(3), null, false);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(5), null, false);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(6), null, false);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(7), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 10, 1, 3, 0);
		board.setCurrentPlayer(redPlayer);
		board.playMove(connect4MoveFactory.createMove(4), null, false);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 13, 0, 1, 3);
	}
	
	public void testShouldCountUnbrokenGroupsOfFour() throws Exception {
		Debug.output("testShouldCountGroups");
		board.fromString(BOARD_WITH_GROUPS);
		board.setCurrentPlayer(redPlayer);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, redPlayer, 7, 4, 1, 0);
		assertUnbrokenGroupOfFourCountsAreCorrect(board, yellowPlayer, 9, 3, 1, 0);
	}
	
	public void testShouldFindOneGroupOfFourOnBoardContainingSeveral() throws Exception {
		StubConnect4Board board2 = new StubConnect4Board(gameContext);
		board2.fromString(BOARD_WITH_SEVERAL_GROUPS_OF_FOUR);
		board2.setCurrentPlayer(redPlayer);
		board2.setWinner(redPlayer);
		Connect4Position[] winningPositions = board2.getWinningGroup();
		assertNotNull(winningPositions[0]);
		assertNotNull(winningPositions[1]);
		assertNotNull(winningPositions[2]);
		assertNotNull(winningPositions[3]);
	}
	
	public void testShouldGetPiecesComprisingOnlyGroupOfFour() throws Exception {
		StubConnect4Board board2 = new StubConnect4Board(gameContext);
		board2.fromString(BOARD_WITH_ONE_GROUP_OF_FOUR);
		board2.setCurrentPlayer(redPlayer);
		board2.setWinner(redPlayer);
		Connect4Position[] winningPositions = board2.getWinningGroup();
		Connect4Position[] expectedWinningPositions = {null, null, null, null};
		expectedWinningPositions[0] = new Connect4Position(4,2);
		expectedWinningPositions[1] = new Connect4Position(3,3);
		expectedWinningPositions[2] = new Connect4Position(2,4);
		expectedWinningPositions[3] = new Connect4Position(1,5);
		assertTrue(Arrays.equals(expectedWinningPositions, winningPositions));
	}
	
	public void testShouldReturnNullIfNoWinner() throws Exception {
		board.initialise();
		Connect4Position[] winningPositions = board.getWinningGroup();
		assertNull(winningPositions);
	}
	
	public void testShouldUsedStoredWinningGroupIfAvailable() throws Exception {
		StubConnect4Board board2 = new StubConnect4Board(gameContext);
		Connect4Position[] winningGroup = {
				new Connect4Position(1,2),
				new Connect4Position(2,3),
				new Connect4Position(3,4),
				new Connect4Position(4,5)
		};
		board2.setWinningGroup(winningGroup);
		board2.setWinner(redPlayer);
		assertSame(winningGroup, board2.getWinningGroup());
	}
	
	private void assertUnbrokenGroupOfFourCountsAreCorrect(Connect4Board board, Connect4Player player, int ones, int twos, int threes, int fours) {
		int colour = player.getColour();
		int boardOnes = board.countUnbrokenGroupsOfFour(colour, 1);
		int boardTwos = board.countUnbrokenGroupsOfFour(colour, 2);
		int boardThrees = board.countUnbrokenGroupsOfFour(colour, 3);
		int boardFours = board.countUnbrokenGroupsOfFour(colour, 4);
		Debug.output("1s:" + boardOnes + ", 2s: " + boardTwos + ", 3s: " + boardThrees + ", 4s:" + boardFours + " for " + player + " on board:\n" + board.toString());
		assertEquals("ones group count wrong", ones, boardOnes);
		assertEquals("twos group count wrong", twos, boardTwos);
		assertEquals("threes group count wrong", threes, boardThrees);
		assertEquals("four and more group count wrong", fours, boardFours);
	}

	@SuppressWarnings("deprecation")
	class StubConnect4Board extends Connect4Board {
		public StubConnect4Board(GameContext gameContext) {
			super();
			setGameContext(gameContext);
		}

		private void setWinner(Player winner) {
			this.winner = winner;
		}
		
		private void setWinningGroup(Connect4Position[] winningGroup) {
			this.winningGroup = winningGroup;
		}
	}
}
