/*
 * @(#)AbstractSinglePassSearcherTest.java		2007/05/07
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import junit.framework.TestCase;

/**
 * Unit tests for {@link AbstractSinglePassSearcher}.
 * @author mpatric
 */
public class AbstractSinglePassSearcherTest extends TestCase {
	
	private boolean findMoveBySearchCalled;
	private TestBoard board = new TestBoard();
	private TestLibrary library = new TestLibrary();
	
	private AbstractSinglePassSearcher stubSearcher = new AbstractSinglePassSearcher(null, null, null, false, false) {
		protected Move findMoveBySearch(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) throws AbortException, RuntimeException {
			findMoveBySearchCalled = true;
			return new TestMove('B');
		}
	};
	
	private AbstractSearcher stubSearcherWithLibrary = new AbstractSinglePassSearcher(null, library, null, false, false) {
		protected Move findMoveBySearch(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) throws AbortException, RuntimeException {
			findMoveBySearchCalled = true;
			return new TestMove('C');
		}
	};
	
	public void testShouldAddListeners() throws Exception {
		SearchProgressListener searchProgressListener1 = new TestSearchProgressListener();
		SearchProgressListener searchProgressListener2 = new TestSearchProgressListener();
		stubSearcher.addSearchProgressListener(searchProgressListener1);
		stubSearcher.addSearchProgressListener(searchProgressListener2);
		assertSame(searchProgressListener1, stubSearcher.searchProgressListeners[0]);
		assertSame(searchProgressListener2, stubSearcher.searchProgressListeners[1]);
	}
	
	public void testShouldNotAddSameListenerTwice() throws Exception {
		SearchProgressListener searchProgressListener = new TestSearchProgressListener();
		stubSearcher.addSearchProgressListener(searchProgressListener);
		stubSearcher.addSearchProgressListener(searchProgressListener);
		assertEquals(1, stubSearcher.searchProgressListeners.length);
	}
	
	public void testShouldRemoveSpecifiedListeners() throws Exception {
		SearchProgressListener searchProgressListener1 = new TestSearchProgressListener();
		SearchProgressListener searchProgressListener2 = new TestSearchProgressListener();
		stubSearcher.addSearchProgressListener(searchProgressListener1);
		stubSearcher.addSearchProgressListener(searchProgressListener2);
		assertEquals(2, stubSearcher.searchProgressListeners.length);
		stubSearcher.removeSearchProgressListener(searchProgressListener1);
		assertEquals(1, stubSearcher.searchProgressListeners.length);
		assertSame(searchProgressListener2, stubSearcher.searchProgressListeners[0]);
		stubSearcher.removeSearchProgressListener(searchProgressListener2);
		assertNull(stubSearcher.searchProgressListeners);
	}
	
	public void testShouldIgnoreRemoveOnNonExistentListener() throws Exception {
		SearchProgressListener searchProgressListener1 = new TestSearchProgressListener();
		SearchProgressListener searchProgressListener2 = new TestSearchProgressListener();
		stubSearcher.addSearchProgressListener(searchProgressListener1);
		assertEquals(1, stubSearcher.searchProgressListeners.length);
		stubSearcher.removeSearchProgressListener(searchProgressListener2);
		assertEquals(1, stubSearcher.searchProgressListeners.length);
	}
	
	public void testShouldThrowExceptionForInvalidDepth() throws Exception {
		try {
			stubSearcher.findMove(board, null, 0);
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	public void testShouldCallFindMoveBySearch() throws Exception {
		assertFalse(findMoveBySearchCalled);
		stubSearcher.findMove(board, null, 1);
		assertTrue(findMoveBySearchCalled);
	}
	
	public void testShouldCallFindMoveOnLibraryAndNotSearcherIfALibraryIsSetAndConditionsForLibraryUseAreCorrect() throws Exception {
		board.setState('A');
		stubSearcherWithLibrary.findMove(board, null, 3);
		assertFalse(findMoveBySearchCalled);
		assertTrue(library.isFindMoveCalled());
	}
	
	public void testShouldCallFindMoveOnSearcherAndNotlibraryIfALibraryIsSetAndConditionsForLibraryUserAreIncorrect() throws Exception {
		Player[] players = new Player[] {TestPlayer.getInstance(1), TestPlayer.getInstance(2)};
		ObjectPool testBoardPool = new ObjectPool(TestBoard.class);
		TestMoveFactory testMoveFactory = new TestMoveFactory(); 
		GameContext gameContext = new GameContext(players, testBoardPool, testMoveFactory, false);
		board.setGameContext(gameContext);
		board.setState('A');
		board.playMove(new TestMove('B'), null, false);
		stubSearcherWithLibrary.findMove(board, null, 3);
		assertTrue(findMoveBySearchCalled);
		assertFalse(library.isFindMoveCalled());
	}
}
