/*
 * @(#)SearchComparisonTest.java		2007/01/21
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import junit.framework.TestCase;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Debug;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.NegamaxSearcher;
import net.lurgee.sgf.NegascoutSearcher;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Unit tests comparing consistency between {@link NegamaxSearcher} and {@link NegascoutSearcher}.
 * @author mpatric
 */
public class SearchComparisonTest extends TestCase implements SearchProgressListener {
	
	private static final int MAX_SEARCH_DEPTH = 5;

	private static final String BOARD_FOR_TESTING_EVALUATIONS =
		"  a b c d e f g h \n" +
		"1 O X X X X O + - \n" + 
		"2 X O X X X X O X \n" + 
		"3 X X O O O O X X \n" + 
		"4 X X O O O O O X \n" + 
		"5 X X O O O O O X \n" + 
		"6 X X O O O O O X \n" + 
		"7 X O O O O O O X \n" + 
		"8 + + X X X X O X ";
	
	private static final ReversiPlayer blackPlayer = ReversiPlayer.getInstance(Colour.BLACK);
	
	private NegamaxSearcher negamaxSearcher;
	private NegamaxSearcher abNegamaxSearcher;
	private NegascoutSearcher negascoutSearcher;
	private ReversiMoveRanker moveRanker;
	private GameContext gameContext;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] {ReversiPlayer.getInstance(Colour.BLACK), ReversiPlayer.getInstance(Colour.WHITE)};
		ObjectPool reversiBoardPool = new ObjectPool(ReversiBoard.class);
		ReversiMoveFactory reversiMoveFactory = new ReversiMoveFactory();
		gameContext = new GameContext(players, reversiBoardPool, reversiMoveFactory, false);
		ReversiEvaluator evaluator = new ReversiEvaluator();
		negamaxSearcher = new NegamaxSearcher(gameContext, null, evaluator, true, true);
		abNegamaxSearcher = new NegamaxSearcher(gameContext, null, evaluator, true, true);
		negascoutSearcher = new NegascoutSearcher(gameContext, null, evaluator, true);
		negamaxSearcher.addSearchProgressListener(this);
		abNegamaxSearcher.addSearchProgressListener(this);
		negascoutSearcher.addSearchProgressListener(this);
		moveRanker = new ReversiMoveRanker();
	}
	
	public void testNonAlphaBetaNegamaxAndAlphaBetaNegamaxShouldSelectSameMoveWithSameScore() throws Exception {
		for (int depth = 1; depth <= MAX_SEARCH_DEPTH; depth++) {
			compareNonAlphaBetaNegamaxAndAlphaBetaNegamax(depth);
		}
	}
	
	public void testAlphaBetaNegamaxAndNegascoutShouldSelectSameMoveWithSameScore() throws Exception {
		for (int depth = 1; depth <= MAX_SEARCH_DEPTH; depth++) {
			compareAlphaBetaNegamaxAndNegascout(depth);
		}
	}
	
	private void compareNonAlphaBetaNegamaxAndAlphaBetaNegamax(int depth) {
		Debug.output("testAlphaBetaNegamaxAndAlphaBetaNegamaxShouldSelectSameMoveWithSameScore with depth " + depth);
		ReversiBoard board = (ReversiBoard) gameContext.checkOutBoard(); 
		board.fromString(BOARD_FOR_TESTING_EVALUATIONS);
		board.setCurrentPlayer(blackPlayer);
		Debug.output("negamax:");
		ReversiMove negamaxMove = (ReversiMove) negamaxSearcher.findMove(board, moveRanker, depth);
		board.clear();
		board.fromString(BOARD_FOR_TESTING_EVALUATIONS);
		board.setCurrentPlayer(blackPlayer);
		Debug.output("ab-negamax:");
		ReversiMove abNegamaxMove = (ReversiMove) abNegamaxSearcher.findMove(board, moveRanker, depth);
		assertEquals("Scores don't match!", negamaxSearcher.getBestMoveScore(), abNegamaxSearcher.getBestMoveScore());
		assertEquals("Different moves!", negamaxMove, abNegamaxMove);
		gameContext.checkInBoard(board);
	}

	private void compareAlphaBetaNegamaxAndNegascout(int depth) {
		Debug.output("testAlphaBetaNegamaxAndNegascoutShouldSelectSameMoveWithSameScore");
		ReversiBoard board = (ReversiBoard) gameContext.checkOutBoard();
		board.fromString(BOARD_FOR_TESTING_EVALUATIONS);
		board.setCurrentPlayer(blackPlayer);
		Debug.output("ab-negamax:");
		ReversiMove negamaxMove = (ReversiMove) negamaxSearcher.findMove(board, moveRanker, depth);
		board.clear();
		board = (ReversiBoard) gameContext.checkOutBoard();
		board.fromString(BOARD_FOR_TESTING_EVALUATIONS);
		board.setCurrentPlayer(blackPlayer);
		Debug.output("negascout:");
		ReversiMove negascoutMove = (ReversiMove) negascoutSearcher.findMove(board, moveRanker, depth);
		assertEquals("Scores don't match!", negamaxSearcher.getBestMoveScore(), negascoutSearcher.getBestMoveScore());
		assertEquals("Different moves!", negamaxMove, negascoutMove);
		gameContext.checkInBoard(board);
	}
	
	public void onIterationStart(int iteration) {
		Debug.output(0, "ITERATION " + iteration + " START");
	}
	
	public void onIterationEnd(int iteration, Move move, int score, int depth, boolean thresholdReached) {
		if (thresholdReached) {
			Debug.output(0, "ITERATION " + iteration + " THRESHOLD REACHED");
		} else {
			Debug.output(0, "ITERATION " + iteration + " END");
		}
	}
	
	public void onBranch(Move move, AbstractBoard board, Player player, int depth) {
		Debug.output(depth + 1, "BRANCH move = " + move + " player = " + player + " depth = " + depth);
	}

	public void onNodeEvaluation(Move move, int score, Player player, int depth) {
		Debug.output(depth + 1, "NODE move = " + move + " score = " + score + " player = " + player + " depth = " + depth);		
	}
	
	public void onLeafEvaluation(int score, Player player, int depth) {
		Debug.output(depth + 2, "LEAF score = " + score + " player = " + player + " depth = " + depth);
	}
}
