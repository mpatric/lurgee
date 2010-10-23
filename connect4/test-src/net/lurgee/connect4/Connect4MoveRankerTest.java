/*
 * @(#)Connect4MoveFactoryTest.java		2007/10/27
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import junit.framework.TestCase;

/**
 * Unit tests for {@link Connect4MoveRanker}.
 * @author mpatric
 */
public class Connect4MoveRankerTest extends TestCase {
	
	private static final String BOARD_WITH_VARYING_TOPS =
		"  1 2 3 4 5 6 7 \n" +
		"6 O - - - - - - \n" + 
		"5 O O - - - - - \n" +
		"4 X X O - - - - \n" +
		"3 X O O O - - - \n" +
		"2 X X X O O - - \n" +
		"1 X O O X O O - ";
	
	@SuppressWarnings("deprecation")
	public void testShouldReturnCorrectStaticRanks() throws Exception {
		Connect4MoveRanker connect4MoveRanker = new Connect4MoveRanker();
		Connect4Board connect4Board = new Connect4Board();
		connect4Board.fromString(BOARD_WITH_VARYING_TOPS);
		assertEquals(4, connect4MoveRanker.getRank(new Connect4Move(2), connect4Board, 1));
		assertEquals(8, connect4MoveRanker.getRank(new Connect4Move(3), connect4Board, 1));
		assertEquals(13, connect4MoveRanker.getRank(new Connect4Move(4), connect4Board, 1));
		assertEquals(11, connect4MoveRanker.getRank(new Connect4Move(5), connect4Board, 1));
		assertEquals(6, connect4MoveRanker.getRank(new Connect4Move(6), connect4Board, 1));
		assertEquals(3, connect4MoveRanker.getRank(new Connect4Move(7), connect4Board, 1));
	}
	
	@SuppressWarnings("deprecation")
	public void testShouldRankOfZeroForFullColumn() throws Exception {
		Connect4MoveRanker connect4MoveRanker = new Connect4MoveRanker();
		Connect4Board connect4Board = new Connect4Board();
		connect4Board.fromString(BOARD_WITH_VARYING_TOPS);
		assertEquals(0, connect4MoveRanker.getRank(new Connect4Move(1), connect4Board, 1));
	}
}
