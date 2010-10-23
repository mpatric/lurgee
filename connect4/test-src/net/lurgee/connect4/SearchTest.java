/*
 * @(#)SearchTest.java		2007/03/07
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import junit.framework.TestCase;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.MoveFactory;
import net.lurgee.sgf.Debug;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.NegamaxSearcher;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Unit tests for searching connect-four boards.
 * @author mpatric
 */
public class SearchTest extends TestCase implements SearchProgressListener {
	
	private static final String BOARD_WITH_WINNING_POSITION_FOR_BOTH =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - - - - \n" +
		"5 - X - - - - - \n" +
		"4 - X - O - - - \n" +
		"3 - X - O - X - \n" +
		"2 - O - O - X - \n" +
		"1 - O - X - X - ";
	
	private static final String BOARD_WITH_WINNING_POSITION_FOR_RED =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - - - - \n" +
		"5 - - - - - - - \n" +
		"4 - - X - - - - \n" +
		"3 - - O O O X - \n" +
		"2 - O X X X O - \n" +
		"1 - X O O O X - ";
	
	private static final String BOARD_WITH_WINNING_POSITION_FOR_YELLOW =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - - - - \n" +
		"5 - - - - O - - \n" +
		"4 - - - O X - - \n" +
		"3 - - - X X - - \n" + 
		"2 O O - X X X - \n" +
		"1 O O X X O O - ";
	
	private static final String BOARD_WHERE_YELLOW_CANT_WIN =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - - - - \n" +
		"5 - - - - - - - \n" +
		"4 - - X - - - - \n" +
		"3 - O O O X - - \n" +
		"2 O X O X O - - \n" +
		"1 X X X O O - - ";
	
	private static final String BOARD_WHERE_RED_CANT_WIN =
		"  1 2 3 4 5 6 7 \n" +
		"6 - - - - - - - \n" +
		"5 - - - O - - - \n" +
		"4 - O - O - - - \n" +
		"3 - X X X - - - \n" +
		"2 - O O X X X - \n" +
		"1 O X O O O X - ";
	
	private static final int MAX_SEARCH_DEPTH = 5;
	
	private static final Connect4Player redPlayer = Connect4Player.getInstance(Colour.RED);
	private static final Connect4Player yellowPlayer = Connect4Player.getInstance(Colour.YELLOW);
	
	private NegamaxSearcher negamaxSearcher;
	private MoveRanker forwardMoveRanker;
	private MoveRanker backwardMoveRanker;
	private GameContext gameContext;
	private Connect4Board board;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] { redPlayer, yellowPlayer };
		ObjectPool connect4BoardPool = new ObjectPool(Connect4Board.class);
		MoveFactory connect4MoveFactory = new Connect4MoveFactory();
		gameContext = new GameContext(players, connect4BoardPool, connect4MoveFactory, false);
		board = (Connect4Board) gameContext.checkOutBoard();
		Connect4Evaluator evaluator = new Connect4Evaluator();
		forwardMoveRanker = new StubMoveRanker(StubMoveRanker.RANKER_FORWARD);
		backwardMoveRanker = new StubMoveRanker(StubMoveRanker.RANKER_BACKWARD);
		negamaxSearcher = new NegamaxSearcher(gameContext, null, evaluator, true, false);
		negamaxSearcher.addSearchProgressListener(this);
	}
	
	@Override
	protected void tearDown() throws Exception {
		gameContext.checkInBoard(board);
	}
	
	public void testShouldChooseLowestWinningMove() throws Exception {
		Debug.output("testShouldChooseLowestWinningMove");
		for (int depth = 2; depth <= MAX_SEARCH_DEPTH; depth++) {
			assertSpecificMoveIsSelectedIrrespectiveOfRanker(BOARD_WITH_WINNING_POSITION_FOR_BOTH, yellowPlayer, 6, depth);
		}
	}

	public void testShouldChooseWinningMoveOverBlockingOpponentsWinningMove() throws Exception {
		Debug.output("testShouldChooseWinningMoveOverBlockingOpponentsWinningMove");
		for (int depth = 2; depth <= MAX_SEARCH_DEPTH; depth++) {
			assertSpecificMoveIsSelectedIrrespectiveOfRanker(BOARD_WITH_WINNING_POSITION_FOR_BOTH, redPlayer, 4, depth);
		}
	}

	public void testShouldChooseWinningMoveIfAvailableForRed() throws Exception {
		Debug.output("testShouldChooseWinningMoveIfAvailableForRed");
		for (int depth = 2; depth <= MAX_SEARCH_DEPTH; depth++) {
			assertSpecificMoveIsSelectedIrrespectiveOfRanker(BOARD_WITH_WINNING_POSITION_FOR_RED, redPlayer, 2, depth);
		}
	}
	
	public void testShouldChooseMovePreventingOpponentFromWinningIfPlayerCantWinThemselfForRed() throws Exception {
		Debug.output("testShouldChooseMovePreventingOpponentFromWinningIfPlayerCantWinThemselfForRed");
		for (int depth = 5; depth <= 5; depth++) {
			assertSpecificMoveIsSelectedIrrespectiveOfRanker(BOARD_WITH_WINNING_POSITION_FOR_RED, yellowPlayer, 2, depth);
		}
	}
	
	public void testShouldChooseWinningMoveIfAvailableForYellow() throws Exception {
		Debug.output("testShouldChooseWinningMoveIfAvailableForYellow");
		for (int depth = 1; depth <= MAX_SEARCH_DEPTH; depth++) {
			assertSpecificMoveIsSelectedIrrespectiveOfRanker(BOARD_WITH_WINNING_POSITION_FOR_YELLOW, yellowPlayer, 3, depth);
		}
	}
	
	public void testShouldChooseMovePreventingOpponentFromWinningIfPlayerCantWinThemselfForYellow() throws Exception {
		Debug.output("testShouldChooseMovePreventingOpponentFromWinningIfPlayerCantWinThemselfForYellow");
		for (int depth = 2; depth <= MAX_SEARCH_DEPTH; depth++) {
			assertSpecificMoveIsSelectedIrrespectiveOfRanker(BOARD_WITH_WINNING_POSITION_FOR_YELLOW, redPlayer, 3, depth);
		}
	}
	
	public void testShouldBlockImmediateWinWhichSetsUpAWinForTheOpponentForYellow() throws Exception {
		Debug.output("testShouldBlockImmediateWinWhichSetsUpAWinForTheOpponentForYellow");
		for (int depth = 2; depth <= MAX_SEARCH_DEPTH; depth++) {
			assertSpecificMoveIsSelectedIrrespectiveOfRanker(BOARD_WHERE_YELLOW_CANT_WIN, yellowPlayer, 1, depth);
		}
	}
	
	public void testShouldBlockImmediateWinWhichSetsUpAWinForTheOpponentForRed() throws Exception {
		Debug.output("testShouldBlockImmediateWinWhichSetsUpAWinForTheOpponentForRed");
		for (int depth = 2; depth <= MAX_SEARCH_DEPTH; depth++) {
			assertSpecificMoveIsSelectedIrrespectiveOfRanker(BOARD_WHERE_RED_CANT_WIN, yellowPlayer, 5, depth);
		}
	}
	
	private int assertSpecificMoveIsSelectedIrrespectiveOfRanker(String boardLayout, Connect4Player player, int expectedMove, int searchDepth) {
		board.fromString(boardLayout);
		board.setCurrentPlayer(player);
		Connect4Move move = (Connect4Move) negamaxSearcher.findMove(board, null, searchDepth);
		assertEquals("expected move not chosen with depth " + searchDepth, expectedMove, move.getX());
		Debug.output(board.getCurrentPlayer() + " plays " + move + " which has score " + negamaxSearcher.getBestMoveScore());
		board.fromString(boardLayout);
		board.setCurrentPlayer(player);
		move = (Connect4Move) negamaxSearcher.findMove(board, forwardMoveRanker, searchDepth);
		assertEquals("expected move not chosen with depth " + searchDepth, expectedMove, move.getX());
		Debug.output(board.getCurrentPlayer() + " plays " + move + " which has score " + negamaxSearcher.getBestMoveScore());
		board.fromString(boardLayout);
		board.setCurrentPlayer(player);
		move = (Connect4Move) negamaxSearcher.findMove(board, backwardMoveRanker, searchDepth);
		assertEquals("expected move not chosen with depth " + searchDepth, expectedMove, move.getX());
		Debug.output(board.getCurrentPlayer() + " plays " + move + " which has score " + negamaxSearcher.getBestMoveScore());
		return move.getX();
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
