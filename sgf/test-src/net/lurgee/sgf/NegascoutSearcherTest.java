/*
 * @(#)NegascoutSearcherTest.java		2006/05/26
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Unit tests for {@link NegascoutSearcher}.
 * @author mpatric
 */
public class NegascoutSearcherTest extends TestCase {
	
	private static final boolean BYE_NOT_ALLOWED = false;
	private static final boolean BYE_ALLOWED = true;	
	private static final TestPlayer PLAYER1 = TestPlayer.getInstance(1);
	private static final int DEPTH = 3;

	private TestEvaluator evaluator = new TestEvaluator();
	private TestBoard board;
	private TestSearchProgressListener searchProgressListener;
	private GameContext gameContext;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] {TestPlayer.getInstance(1), TestPlayer.getInstance(2)};
		ObjectPool testBoardPool = new ObjectPool(TestBoard.class);
		TestMoveFactory testMoveFactory = new TestMoveFactory();
		gameContext = new GameContext(players, testBoardPool, testMoveFactory, false);
		TestBoard.infiniteGameTree = false;
		TestBoard.noMoveSituation = false;
		board = (TestBoard) gameContext.checkOutBoard();
		board.initialise();
		board.setCurrentPlayer(PLAYER1);
		board.setState('A');
		searchProgressListener = new TestSearchProgressListener();
	}
	
	@Override
	protected void tearDown() throws Exception {
		gameContext.checkInBoard(board);
	}

	public void testShouldFindBestMove() throws Exception {
		Debug.output("testShouldFindBestMove");
		AbstractSearcher gameSearcher = new NegascoutSearcher(gameContext, null, evaluator, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('B', move.getPosition().getValue());
		assertEquals(-3, gameSearcher.getBestMoveScore());
		char[] expectedBranches = {'B', 'D', 'H', 'I', 'J', 'E', 'K', 'C', 'F', 'M'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {7, 6, 3, -3, -2, 2, 3, 8, -8, 8};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {7, 6, 3, -2, 8};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
	}
	
	public void testShouldFindBestMoveOnSubtreeStartingWithBlackToPlay() throws Exception {
		Debug.output("testShouldFindBestMoveOnSubtreeStartingWithBlackToPlay");
		board.setState('B');
		AbstractSearcher gameSearcher = new NegascoutSearcher(gameContext, null, evaluator, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('E', move.getPosition().getValue());
		assertEquals(0, gameSearcher.getBestMoveScore());
		char[] expectedBranches = {'D', 'H', 'I', 'J', 'E', 'K', 'L', 'K', 'L'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {-7, -6, -3, 7, 2, 0, 2, 0, 0};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {-7, -6, -3, 2, 0, 2, 0};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
	}
	
	public void testShouldFindBestMoveWithOrdering() throws Exception {
		Debug.output("testShouldFindBestMoveWithOrdering");
		AbstractSearcher gameSearcher = new NegascoutSearcher(gameContext, null, evaluator, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('B', move.getPosition().getValue());
		assertEquals(-3, gameSearcher.getBestMoveScore());
	}
	
	public void testShouldFindBestMoveWithABoardWithANoMoveSituationWithByesAllowed() throws Exception {
		Debug.output("testShouldFindBestMoveWithABoardWithANoMoveSituationWithByesAllowed");
		TestBoard.noMoveSituation = true;
		AbstractSearcher gameSearcher = new NegascoutSearcher(gameContext, null, evaluator, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('R', move.getPosition().getValue());
		assertEquals(-6, gameSearcher.getBestMoveScore());
		char[] expectedBranches = {'R', 'R', 'S', 'T', 'C', 'F', 'M'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {6, 7, -6, 6, 8, -8, 8};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {6, 7, 8};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
		int[] expectedLeafDepths = {3, 3, 3};
		assertTrue(Arrays.equals(expectedLeafDepths, searchProgressListener.getLeafDepths()));
	}
	
	public void testShouldFindBestMoveWithABoardWithANoMoveSituationWithByesNotAllowed() throws Exception {
		Debug.output("testShouldFindBestMoveWithABoardWithANoMoveSituationWithByesNotAllowed");
		TestBoard.noMoveSituation = true;
		AbstractSearcher gameSearcher = new NegascoutSearcher(gameContext, null, evaluator, BYE_NOT_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('R', move.getPosition().getValue());
		assertEquals(-5, gameSearcher.getBestMoveScore());
		char[] expectedBranches = {'R', 'C', 'F', 'M'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {5, 8, -8, 8};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {5, 8};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
		int[] expectedLeafDepths = {1, 3};
		assertTrue(Arrays.equals(expectedLeafDepths, searchProgressListener.getLeafDepths()));
	}
	
	public void testShouldFindBestMoveWithMoveLibrary() throws Exception {
		Debug.output("testShouldFindBestMoveWithMoveLibrary");
		TestLibrary library = new TestLibrary();
		AbstractSearcher gameSearcher = new NegascoutSearcher(gameContext, library, evaluator, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('B', move.getPosition().getValue());
		assertTrue(library.isFindMoveCalled());
		assertEquals(0, searchProgressListener.getBranches().length);
		assertEquals(0, searchProgressListener.getNodeScores().length);
		assertEquals(0, searchProgressListener.getLeafScores().length);
	}
}
