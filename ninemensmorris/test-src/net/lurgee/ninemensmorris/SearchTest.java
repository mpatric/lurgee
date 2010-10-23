/*
 * @(#)SearchTest.java		2008/02/23
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import junit.framework.TestCase;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.AbstractSearcher;
import net.lurgee.sgf.Debug;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.KillerHeuristicMoveRanker;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveFactory;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.NegamaxSearcher;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Unit tests for searching ninemensmorris boards.
 * @author mpatric
 */
public class SearchTest extends TestCase implements SearchProgressListener {
	
	private static final String BOARD_FOR_TESTING_EVALUTION =
		"  a  b  c  d  e  f  g" + 
		"7 +--------X--------+" + 
		"  |        |        |" + 
		"6 |  +-----O-----+  |" + 
		"  |  |     |     |  |" + 
		"5 |  |  +--X--+  |  |" + 
		"  |  |  |     |  |  |" + 
		"4 X--X--+     O--O--O" + 
		"  |  |  |     |  |  |" + 
		"3 |  |  X--X--X  |  |" + 
		"  |  |     |     |  |" + 
		"2 |  +-----+-----O  |" + 
		"  |        |        |" + 
		"1 +--------+--------+";
	
	private static final int MAX_SEARCH_DEPTH = 5;
	
	private static final NineMensMorrisPlayer whitePlayer = NineMensMorrisPlayer.getInstance(Colour.WHITE);
	private static final NineMensMorrisPlayer blackPlayer = NineMensMorrisPlayer.getInstance(Colour.BLACK);
	
	private AbstractSearcher negamaxSearcher;
	private MoveRanker moveRanker;
	private GameContext gameContext;
	private NineMensMorrisBoard board;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] { whitePlayer, blackPlayer };
		ObjectPool ninemensmorrisBoardPool = new ObjectPool(NineMensMorrisBoard.class);
		MoveFactory ninemensmorrisMoveFactory = new NineMensMorrisMoveFactory();
		gameContext = new GameContext(players, ninemensmorrisBoardPool, ninemensmorrisMoveFactory, false);
		board = (NineMensMorrisBoard) gameContext.checkOutBoard();
		NineMensMorrisEvaluator evaluator = new NineMensMorrisEvaluator();
		moveRanker = new KillerHeuristicMoveRanker(new NineMensMorrisMoveRanker(), 4);
		negamaxSearcher = new NegamaxSearcher(gameContext, null, evaluator, true, false);
		negamaxSearcher.addSearchProgressListener(this);
	}
	
	@Override
	protected void tearDown() throws Exception {
		gameContext.checkInBoard(board);
	}

	public void testWhiteShouldCaptureOpponentPiece() throws Exception {
		for (int depth = 1; depth <= MAX_SEARCH_DEPTH; depth++) {
			assertExpectedMoveSelectedWithoutWorryingAboutCapture(depth, BOARD_FOR_TESTING_EVALUTION, whitePlayer, "c3c4");
		}
	}
	
	private void assertExpectedMoveSelectedWithoutWorryingAboutCapture(int depth, String boardAsString, NineMensMorrisPlayer player, String expectedMove) {
		Debug.output("Checking depth " + depth);
		board.fromString(boardAsString);
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		board.setCurrentPlayer(player);
		NineMensMorrisMove move = (NineMensMorrisMove) negamaxSearcher.findMove(board, moveRanker, depth);
		Debug.output(board.getCurrentPlayer() + " plays " + move + " which has score " + negamaxSearcher.getBestMoveScore());
		assertEquals("expected move not selected for depth " + depth, expectedMove, move.toString().substring(0, 4));
	}

	public void onIterationStart(int iteration) {
		Debug.output(0, "ITERATION " + iteration + " START");
	}
	
	public void onIterationEnd(int iteration, Move move, int score, int depth, boolean thresholdReached) {
		if (thresholdReached) {
			Debug.output(0, "ITERATION " + iteration + " THRESHOLD REACHED");
		} else {
			Debug.output(0, "ITERATION " + iteration + " END move = " + move + " score = " + score);
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
