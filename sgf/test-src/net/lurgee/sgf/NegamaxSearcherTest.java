/*
 * @(#)NegamaxSearcherTest.java		2005/11/17
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Unit tests for {@link NegamaxSearcher}.
 * @author mpatric
 */
public class NegamaxSearcherTest extends TestCase {

	private static final boolean NO_AB_CUTOFF = false;
	private static final boolean AB_CUTOFF = true;
	private static final boolean BYE_NOT_ALLOWED = false;
	private static final boolean BYE_ALLOWED = true;	
	private static final TestPlayer PLAYER1 = TestPlayer.getInstance(1);
	private static final int TREE_DEPTH = 3;
	
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
	
	public void testShouldFindBestMoveOnSubTreeWithDepthOneWithoutAlphaBetaPruning() throws Exception {
		Debug.output("testShouldFindBestMoveOnSubTreeWithDepthOneWithoutAlphaBetaPruning");
		board.setState('D');
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, NO_AB_CUTOFF, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, 1));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('J', move.getPosition().getValue());
		assertEquals(-3, gameSearcher.getBestMoveScore());
		char[] expectedBranches = {'H', 'I', 'J'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {7, 6, 3};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {7, 6, 3};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
	}
	
	public void testShouldFindBestMoveWithoutAlphaBetaPruning() throws Exception {
		Debug.output("testShouldFindBestMoveWithoutAlphaBetaPruning");
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, NO_AB_CUTOFF, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, TREE_DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('B', move.getPosition().getValue());
		assertEquals(-3, gameSearcher.getBestMoveScore());
		char[] expectedBranches = {'B', 'D', 'H', 'I', 'J', 'E', 'K', 'L', 'C', 'F', 'M', 'G', 'N', 'O', 'P', 'Q'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {7, 6, 3, -3, -2, 0, 2, 3, 8, -8, 6, -4, 8, 9, 4, 8};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {7, 6, 3, -2, 0, 8, 6, -4, 8, 9};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
	}
	
	public void testShouldFindBestMove() throws Exception {
		Debug.output("testShouldFindBestMove");
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, AB_CUTOFF, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, TREE_DEPTH));
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
	
	public void testShouldFindBestMoveWithOrderingUsingMoveRankerToRankMoves() throws Exception {
		Debug.output("testShouldFindBestMoveWithOrdering");
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, AB_CUTOFF, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMoveRanker moveRanker = new TestMoveRanker();
		TestMove move = (TestMove) (gameSearcher.findMove(board, moveRanker, TREE_DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('B', move.getPosition().getValue());
		assertEquals(-3, gameSearcher.getBestMoveScore());
		char[] expectedBranches = {'C', 'G', 'O', 'Q', 'N', 'P', 'F', 'M', 'B', 'E', 'L', 'K', 'D', 'J', 'I', 'H'};
		assertTrue(Arrays.equals(expectedBranches, searchProgressListener.getBranches()));
		int[] expectedNodeScores = {-4, 9, 6, 8, 4, 8, -8, 8, 0, -2, 2, 3, 6, 7, -3, 3};
		assertTrue(Arrays.equals(expectedNodeScores, searchProgressListener.getNodeScores()));
		int[] expectedLeafScores = {-4, 9, 6, 8, 8, 0, -2, 3, 6, 7};
		assertTrue(Arrays.equals(expectedLeafScores, searchProgressListener.getLeafScores()));
		char[] expectedRankingMoves = {'B', 'C', 'F', 'G', 'N', 'O', 'P', 'Q', 'M', 'D', 'E', 'K', 'L', 'H', 'I', 'J'};
		assertTrue(Arrays.equals(expectedRankingMoves, moveRanker.getMoves()));
		int[] expectedRanks = {1, 2, 1, 2, 2, 3, 1, 2, 1, 1, 2, 1, 2, 1, 2, 3};
		assertTrue(Arrays.equals(expectedRanks, moveRanker.getRanks()));
		int[] expectedRankingDepths = {1, 1, 2, 2, 3, 3, 3, 3, 3, 2, 2, 3, 3, 3, 3, 3};
		assertTrue(Arrays.equals(expectedRankingDepths, moveRanker.getDepths()));
	}
	
	public void testShouldFindBestMoveWithABoardWithANoMoveSituationWithByesAllowed() throws Exception {
		Debug.output("testShouldFindBestMoveWithABoardWithANoMoveSituationWithByesAllowed");
		TestBoard.noMoveSituation = true;
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, AB_CUTOFF, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, TREE_DEPTH));
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
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, AB_CUTOFF, BYE_NOT_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (gameSearcher.findMove(board, null, TREE_DEPTH));
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
		AbstractSearcher searcher = new NegamaxSearcher(gameContext, library, evaluator, AB_CUTOFF, BYE_ALLOWED);
		searcher.addSearchProgressListener(searchProgressListener);
		TestMove move = (TestMove) (searcher.findMove(board, null, TREE_DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + searcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('B', move.getPosition().getValue());
		assertTrue(library.isFindMoveCalled());
		assertEquals(0, searchProgressListener.getBranches().length);
		assertEquals(0, searchProgressListener.getNodeScores().length);
		assertEquals(0, searchProgressListener.getLeafScores().length);
	}
	
	public void testShouldNotifyMoveRankerOfNodeEvaluation() throws Exception {
		Debug.output("testShouldNotifyMoveRankerOfNodeEvaluation");
		AbstractSearcher searcher = new NegamaxSearcher(gameContext, null, evaluator, NO_AB_CUTOFF, BYE_ALLOWED);
		searcher.addSearchProgressListener(searchProgressListener);
		searcher.setOrderOfMovesIsImportant(false);
		TestMoveRanker moveRanker = new TestMoveRanker();
		searcher.findMove(board, moveRanker, TREE_DEPTH);
		assertEquals(16, moveRanker.getNodes());
	}
	
	public void testShouldEndSearchIfThresholdReached() throws Exception {
		Debug.output("testShouldEndSearchIfThresholdReached");
		TestBoard.infiniteGameTree = true;
		AbstractSearcher searcher = new NegamaxSearcher(gameContext, null, evaluator, NO_AB_CUTOFF, BYE_ALLOWED);
		searcher.addSearchProgressListener(searchProgressListener);
		TestMoveRanker moveRanker = new TestMoveRanker();
		long startTimeMs = System.currentTimeMillis();
		int searchDepth = 14;
		try {
			searcher.findMove(board, moveRanker, searchDepth, 20);
			fail("Expected exception not thrown");
		} catch (SearchThresholdReachedException e) {
			// pass
			assertEquals(1, searchProgressListener.getIterations()[0]);
			assertEquals(1, searchProgressListener.getLastIteration());
			assertEquals(searchDepth, searchProgressListener.getLastIterationEndDepth());
			assertEquals(0, searchProgressListener.getLastCompleteIteration());
		} finally {
			long endTimeMs = System.currentTimeMillis();
			Debug.output("Search time: " + (endTimeMs - startTimeMs) + "ms");
		}
	}
	
	public void testShouldUseKillerHeuristicRanksOnSubsequentSearchIfMoveRankerNotReset() throws Exception {
		Debug.output("testShouldUseKillerHeuristicRanksOnSubsequentSearchIfMoveRankerNotReset");
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, AB_CUTOFF, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		gameSearcher.setOrderOfMovesIsImportant(false);
		TestMoveRanker moveRanker = new TestMoveRanker();
		KillerHeuristicMoveRankerForTesting killerHeuristicMoveRanker = new KillerHeuristicMoveRankerForTesting(moveRanker, 2);
		TestMove move = (TestMove) (gameSearcher.findMove(board, killerHeuristicMoveRanker, TREE_DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		Debug.output("Killer heuristics: \n" + killerHeuristicMoveRanker.toString());
		killerHeuristicMoveRanker.clearStats();
		move = (TestMove) (gameSearcher.findMoveWithoutResetting(board, killerHeuristicMoveRanker, TREE_DEPTH));
		Debug.output("BEST move = " + move.getPosition() + " score = " + gameSearcher.getBestMoveScore() + " player = " + PLAYER1);
		assertEquals('B', move.getPosition().getValue());
		assertEquals(-3, gameSearcher.getBestMoveScore());
		char[] expectedRankingMoves = {'B', 'C', 'D', 'E', 'H', 'I', 'J', 'K', 'L', 'F', 'G', 'M'};
		assertTrue(Arrays.equals(expectedRankingMoves, killerHeuristicMoveRanker.getMoves()));
		int[] expectedRanks = {
			KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 3,
			KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE - 8,
			KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 3,
			2, 1, 2, 3,
			KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 2,
			2,
			KillerHeuristicMoveRanker.KILLER_MOVE_SCORE_BASE + 8,
			2, 1
		};
		assertTrue(Arrays.equals(expectedRanks, killerHeuristicMoveRanker.getRanks()));
	}
	
	public void testShouldReportActualDepthSearchedToWhenDepthIsMoreThanRemainingMovesInGame() throws Exception {
		Debug.output("testShouldReportActualDepthSearchedToWhenDepthIsMoreThanRemainingMovesInGame");
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, AB_CUTOFF, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		gameSearcher.findMove(board, null, TREE_DEPTH + 1);
		assertEquals(TREE_DEPTH, searchProgressListener.getLastIterationEndDepth());
	}
	
	public void testShouldReportIterationOneForSinglePassSearcher() throws Exception {
		Debug.output("testShouldReportStartAndEndOfIterationOneForSinglePassSearcher");
		AbstractSearcher gameSearcher = new NegamaxSearcher(gameContext, null, evaluator, AB_CUTOFF, BYE_ALLOWED);
		gameSearcher.addSearchProgressListener(searchProgressListener);
		gameSearcher.findMove(board, null, TREE_DEPTH);
		assertEquals(1, searchProgressListener.getLastIteration());
	}

	class KillerHeuristicMoveRankerForTesting extends KillerHeuristicMoveRanker {
		
		private ArrayList<Integer> ranks = new ArrayList<Integer>();
		private ArrayList<Character> moves = new ArrayList<Character>();

		public KillerHeuristicMoveRankerForTesting(MoveRanker moveRanker, int killerMovesPerLevel) {
			super(moveRanker, killerMovesPerLevel);
		}
		
		@Override
		public int getRank(Move move, AbstractBoard board, int depth) {
			int rank = super.getRank(move, board, depth);
			moves.add(((TestMove) move).getPosition().getValue());
			ranks.add(rank);
			return rank;
		}
		
		public void clearStats() {
			moves.clear();
			ranks.clear();
		}

		public int[] getRanks() {
			return ArrayUtils.integerArrayListToArray(ranks);
		}

		public char[] getMoves() {
			return ArrayUtils.characterArrayListToArray(moves);
		}
	}
}
