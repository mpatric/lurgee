/*
 * @(#)AbstractGameTest.java		2007/06/12
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import junit.framework.TestCase;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.TestBoard;
import net.lurgee.sgf.TestMove;
import net.lurgee.sgf.TestMoveFactory;
import net.lurgee.sgf.TestPlayer;

/**
 * Unit tests for {@link AbstractGame}.
 * @author mpatric
 */
public class AbstractGameTest extends TestCase {

	private static final TestPlayer PLAYER1 = TestPlayer.getInstance(1);
	private static final TestPlayer PLAYER2 = TestPlayer.getInstance(2);
	private static final Player[] PLAYERS = {PLAYER1, PLAYER2};
	private static final int[] SEARCH_LEVELS = {1, 3, 5, 7};
	private static final int[] SEARCH_THRESHOLDS = {0, 0, 100, 200};
	private GameContext gameContext;
	private TestGame game;
	
	@Override
	protected void setUp() throws Exception {
		ObjectPool testBoardPool = new ObjectPool(TestBoard.class);
		TestBoard.infiniteGameTree = false;
		TestBoard.noMoveSituation = false;
		TestMoveFactory testMoveFactory = new TestMoveFactory();
		gameContext = new GameContext(PLAYERS, testBoardPool, testMoveFactory, false);
		Settings settings = new Settings(SEARCH_LEVELS, SEARCH_THRESHOLDS, PLAYERS, 0, 0);
		game = new TestGame(gameContext, settings);
		game.setup();
	}

	public void testShouldReturnNullForLastBoardIfThereIsntOne() throws Exception {
		assertNull(game.getPreviousBoard());
	}
	
	public void testShouldReturnLastBoard() throws Exception {
		AbstractBoard expectedPreviousBoard = new TestBoard((TestBoard) game.getBoard());
		game.playMove(new TestMove('B'));
		assertEquals(expectedPreviousBoard, game.getPreviousBoard());
	}
	
	public void testShouldKeepBoardTheSameWhenDoingUndoWithInsufficientBoardHistory() throws Exception {
		((TestBoard) game.getBoard()).setState('A');
		((TestBoard) game.getBoard()).setCurrentPlayer(PLAYER2);
		game.playMove(new TestMove('B')); // player 2 plays
		AbstractBoard currentBoard = new TestBoard((TestBoard) game.getBoard());
		game.undo();
		assertEquals(currentBoard, game.getBoard());
	}
	
	public void testShouldUndoBackToFirstBoardForHumanPlayer() throws Exception {
		((TestBoard) game.getBoard()).setState('A');
		((TestBoard) game.getBoard()).setCurrentPlayer(PLAYER1);
		AbstractBoard expectedBoardFromHistory = new TestBoard((TestBoard) game.getBoard());
		game.playMove(new TestMove('B'));
		game.playMove(new TestMove('D'));
		game.undo();
		assertEquals(expectedBoardFromHistory, game.getBoard());
	}
	
	public void testShouldUndoBackToLatestBoardForHumanPlayer() throws Exception {
		((TestBoard) game.getBoard()).setState('A');
		((TestBoard) game.getBoard()).setCurrentPlayer(PLAYER2);
		game.playMove(new TestMove('B'));
		AbstractBoard expectedBoardFromHistory = new TestBoard((TestBoard) game.getBoard());
		game.playMove(new TestMove('D'));
		game.undo();
		assertEquals(expectedBoardFromHistory, game.getBoard());
	}
}
