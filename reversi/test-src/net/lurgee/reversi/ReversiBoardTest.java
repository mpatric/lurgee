/*
 * @(#)ReversiBoardTest.java		2005/11/21
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import java.util.ArrayList;

import junit.framework.TestCase;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.Position;

/**
 * Unit tests for {@link ReversiBoard}.
 * @author mpatric
 */
public class ReversiBoardTest extends TestCase {
	
	private static final ReversiPlayer blackPlayer = ReversiPlayer.getInstance(Colour.BLACK);
	private static final ReversiPlayer whitePlayer = ReversiPlayer.getInstance(Colour.WHITE);
	
	static final String INITIAL_BOARD_FOR_COMPARISON =
		"  a b c d e f g h \n" +
		"1 - - - - - - - - \n" +
		"2 - - - - - - - - \n" +
		"3 - - - - - - - - \n" +
		"4 - - - X O - - - \n" +
		"5 - - - O X - - - \n" +
		"6 - - - - - - - - \n" +
		"7 - - - - - - - - \n" +
		"8 - - - - - - - - ";
	
	static final String FINAL_BOARD_FOR_COMPARISON =
		"  a b c d e f g h \n" +
		"1 - - - - - - - O \n" +
		"2 - - - - - - - - \n" +
		"3 - - - - - - - - \n" +
		"4 - - - X - - - - \n" +
		"5 - - - X O - - - \n" +
		"6 - - - - - - - - \n" +
		"7 - - - - - - - - \n" +
		"8 X - - - - - - - ";
	
	static final String EXPECTED_DELTA_BOARD =
		"  a b c d e f g h \n" +
		"1 - - - - - - - O \n" +
		"2 - - - - - - - - \n" +
		"3 - - - - - - - - \n" +
		"4 - - - - - - - - \n" +
		"5 - - - X O - - - \n" +
		"6 - - - - - - - - \n" +
		"7 - - - - - - - - \n" +
		"8 X - - - - - - - ";
	
	static final String DIRTY_BOARD_FOR_COMPARISON =
		"  a b c d e f g h \n" +
		"1 - - - - - - - X \n" +
		"2 - O - - - - X - \n" +
		"3 - - - - - X - - \n" +
		"4 - - - - X - - - \n" +
		"5 - - - X - - - - \n" +
		"6 - - X - - - - - \n" +
		"7 - X - - - - O - \n" +
		"8 X - - - - - - - ";
	
	static final String BOARD_FOR_TESTING_COUNTS =
		"  a b c d e f g h \n" +
		"1 - - - - - - X O \n" + 
		"2 - - - O O O O X \n" + 
		"3 - - - O X O O X \n" + 
		"4 - - - X X O O X \n" + 
		"5 - - - O X O O X \n" + 
		"6 - - - - - - O X \n" + 
		"7 - - - - - O X X \n" + 
		"8 - - - - O X X X ";
	
	private GameContext gameContext;
	private ReversiMoveFactory reversiMoveFactory;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] {blackPlayer, whitePlayer};
		ObjectPool reversiBoardPool = new ObjectPool(ReversiBoard.class);
		reversiMoveFactory = new ReversiMoveFactory();
		gameContext = new GameContext(players, reversiBoardPool, reversiMoveFactory, false);
	}
	
	public void testTwoFreshlyInitialisedBoardsShouldConvertToTheSameString() {
		ReversiBoard board1 = (ReversiBoard) gameContext.checkOutBoard();
		board1.initialise();
		ReversiBoard board2 = (ReversiBoard) gameContext.checkOutBoard();
		board2.initialise();
		assertEquals(board1.toString(), board2.toString());
		gameContext.checkInBoards(board1, board2);
	}
	
	public void testShouldInitialiseABoardFromAStringCorrectly() throws Exception {
		ReversiBoard board1 = (ReversiBoard) gameContext.checkOutBoard();
		board1.initialise();
		board1.setCurrentPlayer(blackPlayer);
		ReversiBoard board2 = (ReversiBoard) gameContext.checkOutBoard();
		board2.fromString(board1.toString());
		board2.setCurrentPlayer(blackPlayer);
		assertEquals(board1, board2);
		gameContext.checkInBoards(board1, board2);
	}
	
	public void testInitialisedBoardShouldHaveCorrectPiecesPlacedAndCorrectNumberOfUsedAndFreeSquares() throws Exception {
		ReversiBoard board1 = (ReversiBoard) gameContext.checkOutBoard();
		board1.initialise();
		assertEquals(blackPlayer, board1.getCurrentPlayer());
		assertEquals(4, board1.getCount(Colour.ANY));
		assertEquals(60, board1.getCount(Colour.NONE));
		assertEquals(2, board1.getCount(Colour.BLACK));
		assertEquals(2, board1.getCount(Colour.WHITE));
		assertEquals(board1.getSquare(4, 4), Colour.WHITE);
		assertEquals(board1.getSquare(5, 5), Colour.WHITE);
		assertEquals(board1.getSquare(4, 5), Colour.BLACK);
		assertEquals(board1.getSquare(5, 4), Colour.BLACK);
		gameContext.checkInBoard(board1);
	}
	
	public void testShouldPlayAValidMoveSuccessfully() throws Exception {
		ReversiBoard board1 = (ReversiBoard) gameContext.checkOutBoard();
		board1.initialise();
		Move move = reversiMoveFactory.createMove(4, 3); 
		board1.setCurrentPlayer(blackPlayer);
		int result = board1.playMove(move, null, false);
		assertEquals(1, result);
		assertEquals(Colour.BLACK, board1.getSquare(4, 3));
		assertEquals(Colour.BLACK, board1.getSquare(4, 4));
		gameContext.checkInBoard(board1);
	}
	
	public void testShouldPlayASeriesOfValidMovesSuccessfully() throws Exception {
		ReversiBoard board1 = (ReversiBoard) gameContext.checkOutBoard();
		board1.initialise();
		Move move = reversiMoveFactory.createMove(4, 3);
		board1.setCurrentPlayer(blackPlayer);
		int result = board1.playMove(move, null, false);
		assertEquals(1, result);
		move = reversiMoveFactory.createMove(3, 5);
		result = board1.playMove(move, null, false);
		assertEquals(result, 1);
		move = reversiMoveFactory.createMove(5, 3);
		ArrayList<Position> flips = new ArrayList<Position>();
		board1.setCurrentPlayer(whitePlayer);
		result = board1.playMove(move, flips, false);
		assertEquals(2, result);
		assertEquals(Colour.WHITE, board1.getSquare(5, 3));
		assertEquals(Colour.WHITE, board1.getSquare(4, 4));
		assertEquals(Colour.WHITE, board1.getSquare(5, 4));
		assertEquals(flips.size(), 2);
		assertTrue(flips.contains(new ReversiPosition(4, 4)));
		assertTrue(flips.contains(new ReversiPosition(5, 4)));
		assertEquals(6, board1.getCount(Colour.WHITE));
		assertEquals(1, board1.getCount(Colour.BLACK));
		gameContext.checkInBoard(board1);
	}
	
	public void testCreationOfADeltaOfOneBoardToAnother() throws Exception {
		ReversiBoard board1 = (ReversiBoard) gameContext.checkOutBoard();
		board1.fromString(INITIAL_BOARD_FOR_COMPARISON);
		board1.setCurrentPlayer(blackPlayer);
		ReversiBoard board2 = (ReversiBoard) gameContext.checkOutBoard();
		board2.fromString(FINAL_BOARD_FOR_COMPARISON);
		board2.setCurrentPlayer(blackPlayer);
		ReversiBoard deltaBoard = (ReversiBoard) gameContext.checkOutBoard();
		ReversiDifferenceBoard differenceBoard = board2.compare(board1, deltaBoard);
		ReversiBoard expectedDeltaBoard = (ReversiBoard) gameContext.checkOutBoard();
		expectedDeltaBoard.fromString(EXPECTED_DELTA_BOARD);
		expectedDeltaBoard.setCurrentPlayer(blackPlayer);
		assertEquals("Board counts don't match", expectedDeltaBoard.getCount(Colour.ANY), differenceBoard.getCount(Colour.ANY));
		assertEquals("Board counts don't match", expectedDeltaBoard.getCount(Colour.BLACK), differenceBoard.getCount(Colour.BLACK));
		assertEquals("Board counts don't match", expectedDeltaBoard.getCount(Colour.WHITE), differenceBoard.getCount(Colour.WHITE));
		for (int y = 1; y < ReversiBoard.Y_DIMENSION; y++) {
			assertEquals("Boards don't match", expectedDeltaBoard.getSquares(y), differenceBoard.getSquares(y));
		}
		gameContext.checkInBoards(board1, board2, deltaBoard, expectedDeltaBoard);
	}
	
	public void testCreationOfADeltaOfOneBoardToAnotherUsingADirtyBoardAsDestination() throws Exception {
		ReversiBoard board1 = (ReversiBoard) gameContext.checkOutBoard();
		board1.fromString(INITIAL_BOARD_FOR_COMPARISON);
		board1.setCurrentPlayer(blackPlayer);
		ReversiBoard board2 = (ReversiBoard) gameContext.checkOutBoard();
		board2.fromString(FINAL_BOARD_FOR_COMPARISON);
		board2.setCurrentPlayer(blackPlayer);
		ReversiBoard deltaBoard = (ReversiBoard) gameContext.checkOutBoard();
		deltaBoard.fromString(DIRTY_BOARD_FOR_COMPARISON);
		deltaBoard.setCurrentPlayer(blackPlayer);
		ReversiDifferenceBoard differenceBoard = board2.compare(board1, deltaBoard);
		ReversiBoard expectedDeltaBoard = (ReversiBoard) gameContext.checkOutBoard();
		expectedDeltaBoard.fromString(EXPECTED_DELTA_BOARD);
		expectedDeltaBoard.setCurrentPlayer(blackPlayer);
		assertEquals("Board counts don't match", expectedDeltaBoard.getCount(Colour.ANY), differenceBoard.getCount(Colour.ANY));
		assertEquals("Board counts don't match", expectedDeltaBoard.getCount(Colour.BLACK), differenceBoard.getCount(Colour.BLACK));
		assertEquals("Board counts don't match", expectedDeltaBoard.getCount(Colour.WHITE), differenceBoard.getCount(Colour.WHITE));
		for (int y = 1; y < ReversiBoard.Y_DIMENSION; y++) {
			assertEquals("Boards don't match", expectedDeltaBoard.getSquares(y), differenceBoard.getSquares(y));
		}
		gameContext.checkInBoards(board1, board2, deltaBoard, expectedDeltaBoard);
	}
	
	public void testShouldReturnCorrectPieceCountsForTestBoard() throws Exception {
		ReversiBoard board = (ReversiBoard) gameContext.checkOutBoard();
		board.fromString(BOARD_FOR_TESTING_COUNTS);
		board.setCurrentPlayer(blackPlayer);
		assertEquals("Black piece count incorrect", 16, board.getCount(Colour.BLACK));
		assertEquals("White piece count incorrect", 15, board.getCount(Colour.WHITE));
		gameContext.checkInBoard(board);
	}
	
	public void testShouldReturnCorrectAdjacentCountsForTestBoard() throws Exception {
		ReversiBoard board = (ReversiBoard) gameContext.checkOutBoard();
		board.fromString(BOARD_FOR_TESTING_COUNTS);
		board.setCurrentPlayer(blackPlayer);
		assertEquals("Black adjacent count incorrect", 14, board.getAdjacentCount(Colour.BLACK));
		assertEquals("White adjacent count incorrect", 7, board.getAdjacentCount(Colour.WHITE));
		gameContext.checkInBoard(board);
	}
}
