/*
 * @(#)IterativeSearcherTest.java		2007/10/17
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Unit tests for {@link IterativeSearcher}.
 * @author mpatric
 */
public class IterativeSearcherTest extends TestCase {
	
	private static final TestPlayer PLAYER1 = TestPlayer.getInstance(1);
	
	private IterativeSearcher searcher;
	private AbstractSinglePassSearcher negamaxSearcher;
	private MoveRanker moveRanker;
	private TestSearchProgressListener searchProgressListener;
	private TestBoard board;
	private GameContext gameContext;

	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] {TestPlayer.getInstance(1), TestPlayer.getInstance(2)};
		ObjectPool testBoardPool = new ObjectPool(TestBoard.class);
		TestMoveFactory testMoveFactory = new TestMoveFactory();
		gameContext = new GameContext(players, testBoardPool, testMoveFactory, false);
		Evaluator evaluator = new TestEvaluator();
		moveRanker = new KillerHeuristicMoveRanker(new TestMoveRanker(), 10);
		negamaxSearcher = new NegamaxSearcher(gameContext, null, evaluator, false, false);
		searchProgressListener = new TestSearchProgressListener();
		negamaxSearcher.addSearchProgressListener(searchProgressListener);
		searcher = new IterativeSearcher(negamaxSearcher, null);
		TestBoard.infiniteGameTree = false;
		TestBoard.noMoveSituation = false;
		board = (TestBoard) gameContext.checkOutBoard();
		board.initialise();
		board.setCurrentPlayer(PLAYER1);
		board.setState('A');
	}
	
	public void testShouldFindBestMoveWithNoAlphaBetaCutoffsToDepthOne() throws Exception {
		TestMove bestMove = (TestMove) searcher.findMove(board, moveRanker, 1);
		assertEquals('B', bestMove.getPosition().getValue());
		assertEquals(-1, negamaxSearcher.getBestMoveScore());
		char[] expectedBranches = {'C', 'B'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {7, 1};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {7, 1};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
		int[] expectedIterations = {1};
		assertTrue(Arrays.equals(expectedIterations, searchProgressListener.getIterations()));
	}
	
	public void testShouldFindBestMoveWithNoAlphaBetaCutoffsToDepthTwo() throws Exception {
		TestMove bestMove = (TestMove) searcher.findMove(board, moveRanker, 2);
		assertEquals('B', bestMove.getPosition().getValue());
		assertEquals(-2, searcher.getBestMoveScore());
		char[] expectedBranches = {'C', 'B', 'B', 'E', 'D', 'C', 'G', 'F'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {7, 1, -1, -2, 2, 3, -6, 6};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {7, 1, -1, -2, 3, -6};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
		int[] expectedIterations = {1, 2};
		assertTrue(Arrays.equals(expectedIterations, searchProgressListener.getIterations()));
	}
	
	public void testShouldFindBestMoveWithNoAlphaBetaCutoffsToDepthThree() throws Exception {
		TestMove bestMove = (TestMove) searcher.findMove(board, moveRanker, 3);
		assertEquals('B', bestMove.getPosition().getValue());
		assertEquals(-3, searcher.getBestMoveScore());
		char[] expectedBranches = {'C', 'B', 'B', 'E', 'D', 'C', 'G', 'F', 'B', 'D', 'J', 'I', 'H', 'E', 'L', 'K', 'C', 'F', 'M', 'G', 'O', 'Q', 'N', 'P'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {7, 1, -1, -2, 2, 3, -6, 6, 3, 6, 7, -3, 0, -2, 2, 3, 8, -8, -4, 9, 6, 8, 4, 8};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {7, 1, -1, -2, 3, -6, 3, 6, 7, 0, -2, 8, -4, 9, 6, 8};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
		int[] expectedIterations = {1, 2, 3};
		assertTrue(Arrays.equals(expectedIterations, searchProgressListener.getIterations()));
	}
	
	public void testShouldStopSearchAtStartOfIterationTwoAsOverThresholdAndDepthIsGreaterThanOneThenReturnBestMoveAtThatTime() throws Exception {
		TestBoard.infiniteGameTree = true;
		TestMove bestMove = (TestMove) searcher.findMove(board, moveRanker, 12, 1);
		assertNotNull(bestMove);
		// depth should be 1 as iteration 2 to depth 2 did not complete
		int[] expectedIterations = {1, 2};
		assertTrue(Arrays.equals(expectedIterations, searchProgressListener.getIterations()));
		assertEquals(2, searchProgressListener.getLastIteration());
		assertEquals(1, searchProgressListener.getLastCompleteIteration());
		assertEquals(1, searchProgressListener.getLastCompleteIterationEndDepth());
	}
	
	public void testShouldStopSearchWhenItExcedesSpecifiedSearchThresholdAndDepthIsGreaterThanOneThenReturnBestMoveAtThatTime() throws Exception {
		TestBoard.infiniteGameTree = true;
		TestMove bestMove = (TestMove) searcher.findMove(board, moveRanker, 12, 50);
		assertNotNull(bestMove);
		int[] expectedIterations = {1, 2, 3, 4, 5, 6};
		assertTrue(Arrays.equals(expectedIterations, searchProgressListener.getIterations()));
		assertEquals(6, searchProgressListener.getLastIteration());
		assertEquals(5, searchProgressListener.getLastCompleteIteration());
		assertEquals(5, searchProgressListener.getLastCompleteIterationEndDepth());
	}
}
