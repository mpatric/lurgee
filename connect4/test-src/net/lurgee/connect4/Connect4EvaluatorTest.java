/*
 * @(#)Connect4EvaluatorTest.java		2007/03/08
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import junit.framework.TestCase;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;

/**
 * Unit tests for {@link Connect4Evaluator}.
 * @author mpatric
 */
public class Connect4EvaluatorTest extends TestCase {

	static final String TEST_BOARD =
		"  1 2 3 4 5 6 7 \n" +
		"6 O - - - - - - \n" + 
		"5 O X - - - - - \n" + 
		"4 X X - X - - - \n" + 
		"3 X O X O - - - \n" + 
		"2 O O O X - - - \n" + 
		"1 O O X O X - X ";
	
	private static final String BOARD_WITH_ODD_AND_EVEN_THREAT_POTENTIAL =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - - - - \n" +
		"5 - - - - - - - \n" +
		"4 - - - - - - - \n" +
		"3 - - - - X O - \n" +
		"2 - O X - X O O \n" +
		"1 O X X X O X O ";
	
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
	
	public void testEvaluationYieldsPositiveScoreForWinningPositionForPlayer() throws Exception {
		board.fromString(TEST_BOARD);
		board.setCurrentPlayer(Connect4Player.getInstance(Colour.YELLOW));
		Connect4Evaluator evaluator = new Connect4Evaluator();
		int score = evaluator.score(board, board, 1, 1);
		assertTrue(score > 0);
	}

	public void testEvaluationYieldsNegativeScoreForWinningPositionForOpponent() throws Exception {
		Connect4Evaluator evaluator = new Connect4Evaluator();
		board.fromString(TEST_BOARD);
		board.setCurrentPlayer(Connect4Player.getInstance(Colour.RED));
		int score = evaluator.score(board, board, 1, 1);
		assertTrue(score < 0);
	}
	
	public void testScoreForStartingPlayerShouldNotBeWorseThanOddThreatMoveThanEvenThreatMove() throws Exception {
		board.fromString(BOARD_WITH_ODD_AND_EVEN_THREAT_POTENTIAL);
		board.setCurrentPlayer(Connect4Player.getInstance(Colour.RED));
		Connect4Evaluator evaluator = new Connect4Evaluator();
		board.playMove(connect4MoveFactory.createMove(3), null, false);
		int evenThreatMoveScore = evaluator.score(board, board, 1, 1);
		board.fromString(BOARD_WITH_ODD_AND_EVEN_THREAT_POTENTIAL);
		board.setCurrentPlayer(Connect4Player.getInstance(Colour.RED));
		board.playMove(connect4MoveFactory.createMove(5), null, false);
		int oddThreatMoveScore = evaluator.score(board, board, 1, 1);
		assertTrue(oddThreatMoveScore <= evenThreatMoveScore); // less than because score is done for YELLOW
	}
}
