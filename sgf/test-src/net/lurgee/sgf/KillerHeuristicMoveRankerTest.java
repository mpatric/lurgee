/*
 * @(#)KillerHeuristicMoveRankerTest.java		2007/11/08
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Unit tests for {@link KillerHeuristicMoveRanker}.
 * @author mpatric
 */
public class KillerHeuristicMoveRankerTest extends TestCase {
	
	private TestMoveFactory testMoveFactory;
	private AbstractBoard board;
	private TestMoveRanker moveRanker;
	private KillerHeuristicMoveRanker killerHeuristicMoveRanker;
	
	@Override
	protected void setUp() throws Exception {
		testMoveFactory = new TestMoveFactory();
		board = new TestBoard();
		moveRanker = new TestMoveRanker();
		killerHeuristicMoveRanker = new KillerHeuristicMoveRanker(moveRanker, 2);
	}

	public void testShouldRankKillerMovesAndDelegateRankingToMoveRankerForNonKillerMoves() throws Exception {
		Debug.output("testShouldRankKillerMovesAndDelegateRankingToMoveRankerForNonKillerMoves");
		TestMove killerMove = testMoveFactory.getMove('B');
		TestMove nonKillerMove = testMoveFactory.getMove('C');
		killerHeuristicMoveRanker.addIfKillerMove(killerMove, -3, 1);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 3, killerHeuristicMoveRanker.getRank(killerMove, board, 1));
		assertEquals(2, killerHeuristicMoveRanker.getRank(nonKillerMove, board, 1));
		char[] expectedMoves = {'C'};
		assertTrue(Arrays.equals(expectedMoves, moveRanker.getMoves()));
	}

	public void testShouldNotAddAWorseKillerMoveIfNoSpace() throws Exception {
		Debug.output("testShouldNotAddAWorseKillerMoveIfNoSpace");
		TestMove killerMove1 = testMoveFactory.getMove('D');
		TestMove killerMove2 = testMoveFactory.getMove('E');
		TestMove killerMove3 = testMoveFactory.getMove('F');
		killerHeuristicMoveRanker.addIfKillerMove(killerMove1, 3, 2);
		killerHeuristicMoveRanker.addIfKillerMove(killerMove2, -2, 2);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 3, killerHeuristicMoveRanker.getRank(killerMove1, board, 2));
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 2, killerHeuristicMoveRanker.getRank(killerMove2, board, 2));
		killerHeuristicMoveRanker.addIfKillerMove(killerMove3, -4, 2);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 3, killerHeuristicMoveRanker.getRank(killerMove1, board, 2));
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 2, killerHeuristicMoveRanker.getRank(killerMove2, board, 2));
		assertEquals(1, killerHeuristicMoveRanker.getRank(killerMove3, board, 2));
	}
	
	public void testShouldChangeRankOfStoredKillerMoveIfReAddedWithBetterScore() throws Exception {
		Debug.output("testChangesRankOfStoredKillerMoveIfReaddedWithBetterScore");
		TestMove killerMove1 = testMoveFactory.getMove('D');
		TestMove killerMove2 = testMoveFactory.getMove('E');
		killerHeuristicMoveRanker.addIfKillerMove(killerMove1, 3, 2);
		killerHeuristicMoveRanker.addIfKillerMove(killerMove2, -2, 2);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 3, killerHeuristicMoveRanker.getRank(killerMove1, board, 2));
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 2, killerHeuristicMoveRanker.getRank(killerMove2, board, 2));
		assertEquals(2, killerHeuristicMoveRanker.count(2));
		killerHeuristicMoveRanker.addIfKillerMove(killerMove2, 5, 2);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 3, killerHeuristicMoveRanker.getRank(killerMove1, board, 2));
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 5, killerHeuristicMoveRanker.getRank(killerMove2, board, 2));
		assertEquals(2, killerHeuristicMoveRanker.count(2));
		killerHeuristicMoveRanker.addIfKillerMove(killerMove1, 4, 2);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 4, killerHeuristicMoveRanker.getRank(killerMove1, board, 2));
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 5, killerHeuristicMoveRanker.getRank(killerMove2, board, 2));
		assertEquals(2, killerHeuristicMoveRanker.count(2));
	}
	
	public void testShouldNotChangeRankOfStoredKillerMoveIfReAddedWithWorseScore() throws Exception {
		Debug.output("testDoesNotChangeRankOfStoredKillerMoveIfReaddedWithWorseScore");
		TestMove killerMove1 = testMoveFactory.getMove('D');
		TestMove killerMove2 = testMoveFactory.getMove('E');
		killerHeuristicMoveRanker.addIfKillerMove(killerMove1, 3, 2);
		killerHeuristicMoveRanker.addIfKillerMove(killerMove2, -2, 2);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 3, killerHeuristicMoveRanker.getRank(killerMove1, board, 2));
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 2, killerHeuristicMoveRanker.getRank(killerMove2, board, 2));
		assertEquals(2, killerHeuristicMoveRanker.count(2));
		killerHeuristicMoveRanker.addIfKillerMove(killerMove1, 2, 1);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 3, killerHeuristicMoveRanker.getRank(killerMove1, board, 2));
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 2, killerHeuristicMoveRanker.getRank(killerMove2, board, 2));
		assertEquals(2, killerHeuristicMoveRanker.count(2));
		killerHeuristicMoveRanker.addIfKillerMove(killerMove2, -3, 1);
		Debug.output(killerHeuristicMoveRanker.toString());
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 3, killerHeuristicMoveRanker.getRank(killerMove1, board, 2));
		assertEquals(KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 2, killerHeuristicMoveRanker.getRank(killerMove2, board, 2));
		assertEquals(2, killerHeuristicMoveRanker.count(2));
	}
}
