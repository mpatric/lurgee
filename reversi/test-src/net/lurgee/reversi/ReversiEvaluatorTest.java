/*
 * @(#)ReversiEvaluatorTest.java		2007/01/21
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import junit.framework.TestCase;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;

/**
 * Unit tests for {@link ReversiEvaluator}.
 * @author mpatric
 */
public class ReversiEvaluatorTest extends TestCase {

	private static final int DEPTH = 5;
	private static final int ITERATIONS = 5;
	
	static final String TEST_BOARD =
		"  a b c d e f g h \n" +
		"1 O X X X X O + - \n" + 
		"2 X O X X X X O X \n" + 
		"3 X X O O O O X X \n" + 
		"4 X X O O O O O X \n" + 
		"5 X X O O O O O X \n" + 
		"6 X X O O O O O X \n" + 
		"7 X O O O O O O X \n" + 
		"8 + + X X X X O X ";
	
	static final String TEST_BOARD_AFTER_G1_H1_A8_B8 =
		"  a b c d e f g h \n" +
		"1 O X X X X X X X \n" + 
		"2 O O X X X O O X \n" + 
		"3 O X O O O O X X \n" + 
		"4 O X O O O X O X \n" + 
		"5 O X O O X O O X \n" + 
		"6 O X O X O O O X \n" + 
		"7 O X X O O O O X \n" + 
		"8 O X X X X X O X ";
	
	private static final ReversiPlayer BLACK_PLAYER = ReversiPlayer.getInstance(Colour.BLACK);
	private static final ReversiPlayer WHITE_PLAYER = ReversiPlayer.getInstance(Colour.WHITE);
	
	private GameContext gameContext;
	private ReversiEvaluator evaluator;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] {BLACK_PLAYER, WHITE_PLAYER};
		ObjectPool reversiBoardPool = new ObjectPool(ReversiBoard.class);
		ReversiMoveFactory reversiMoveFactory = new ReversiMoveFactory();
		gameContext = new GameContext(players, reversiBoardPool, reversiMoveFactory, false);
		evaluator = new ReversiEvaluator();
	}

	/**
	 * Test to reproduce defect, where consecutive evaluations on this board yield different scores.
	 */
	public void testConsecutiveEvaluationsWithTestBoardShouldYieldSameScoreEachTime() throws Exception {
		int lastScore = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			ReversiBoard startBoard = (ReversiBoard) gameContext.checkOutBoard();
			startBoard.fromString(TEST_BOARD);			
			startBoard.setCurrentPlayer(BLACK_PLAYER);
			ReversiBoard endBoard = (ReversiBoard) gameContext.checkOutBoard();
			endBoard.fromString(TEST_BOARD_AFTER_G1_H1_A8_B8);
			endBoard.setCurrentPlayer(BLACK_PLAYER);
			int score = evaluator.score(startBoard, endBoard, DEPTH, DEPTH);
			if (i > 0) {
				assertEquals("Score doesn't match previous score", lastScore, score);
			}
			lastScore = score;
		}
	}
}
