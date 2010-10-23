/*
 * @(#)AbstractBoardTest.java		2007/05/07
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import junit.framework.TestCase;

/**
 * Unit tests for {@link AbstractBoard}.
 * @author mpatric
 */
public class AbstractBoardTest extends TestCase {
	
	private static final TestPlayer PLAYER1 = TestPlayer.getInstance(1);
	private static final TestPlayer PLAYER2 = TestPlayer.getInstance(2);
	
	private TestBoard board;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = {PLAYER1, PLAYER2};
		ObjectPool testBoardPool = new ObjectPool(TestBoard.class);
		TestBoard.infiniteGameTree = false;
		TestBoard.noMoveSituation = false;
		MoveFactory testMoveFactory = new TestMoveFactory();
		GameContext gameContext = new GameContext(players, testBoardPool, testMoveFactory, false);
		board = (TestBoard) gameContext.checkOutBoard();
	}
	
	public void testShouldSetTheCurrentPlayer() throws Exception {
		board.setCurrentPlayer(PLAYER2);
		assertEquals(PLAYER2, board.getCurrentPlayer());
	}
	
	public void testShouldThrowAnExceptionWhenSettingCurrentPlayerForPlayerNotInBoardsList() throws Exception {
		TestPlayer newPlayer = new TestPlayer(3, '3', "Three");
		try {
			board.setCurrentPlayer(newPlayer);
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	public void testShouldChangeCurrentPlayerWhenSuccessfullyPlayingAMove() throws Exception {
		board.initialise();
		Player startPlayer = board.getCurrentPlayer();
		int result = board.playMove(new TestMove('A'), null, false);
		Player currentPlayer = board.getCurrentPlayer();
		assertTrue(result != 0);
		assertFalse(startPlayer.equals(currentPlayer));
	}
	
	public void testShouldCallMakeMoveWhenPlayingAMove() throws Exception {
		assertFalse(board.isMakeMoveCalled());
		board.playMove(new TestMove('A'), null, false);
		assertTrue(board.isMakeMoveCalled());
	}
	
	public void testShouldChooseFirstPlayerWhenNoCurrentPlayerIsSet() throws Exception {
		board.nextPlayer();
		assertEquals(PLAYER1, board.getCurrentPlayer());
	}
	
	public void testShouldChooseSecondPlayerWhenFirstPlayerIsCurrentPlayer() throws Exception {
		board.setCurrentPlayer(PLAYER1);
		board.nextPlayer();
		assertEquals(PLAYER2, board.getCurrentPlayer());
	}
	
	public void testShouldChooseFirstPlayerWhenSecondPlayerIsCurrentPlayer() throws Exception {
		board.setCurrentPlayer(PLAYER2);
		board.nextPlayer();
		assertEquals(PLAYER1, board.getCurrentPlayer());
	}
	
	public void testShouldThrowExceptionWhenNoPlayersSetup() throws Exception {
		Player[] players = {};
		ObjectPool testBoardPool = new ObjectPool(TestBoard.class);
		MoveFactory testMoveFactory = new TestMoveFactory();
		GameContext gameContext = new GameContext(players, testBoardPool, testMoveFactory, false);
		board.setGameContext(gameContext);
		try {
			board.nextPlayer();
			fail("Expected exception not thrown");
		} catch (IllegalStateException e) {
			// pass
		}
	}
	
	public void testShouldCopyBoardWhichIsEqualToOriginal() throws Exception {
		board.setCurrentPlayer(PLAYER2);
		board.setState('F');
		TestBoard board2 = new TestBoard();
		board2.copy(board);
		assertEquals(board, board2);
	}
	
	public void testShouldReturnNullIfNoLastMove() throws Exception {
		board.initialise();
		assertNull(board.getLastMovePlayed());
	}
	
	public void testShouldReturnLastMove() throws Exception {
		board.initialise();
		TestMove actualLastMove = new TestMove('A');
		board.playMove(actualLastMove, null, false);
		assertEquals(actualLastMove, board.getLastMovePlayed());
	}
}

