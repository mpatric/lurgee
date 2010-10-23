/*
 * @(#)TestLibrary.java		2006/01/28
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Move library used for unit tests.
 * @author mpatric
 */
public class TestLibrary implements Library {

	private boolean findMoveCalled = false;
	
	public boolean isFindMoveCalled() {
		return findMoveCalled;
	}

	public boolean shouldUseLibrary(AbstractBoard board, int depth) {
		if (board.countMovesMade() == 0) {
			return true;
		}
		return false;
	}

	public Move findMove(AbstractBoard board, int depth) {
		findMoveCalled = true;
		if (((TestBoard) board).getState() == 'A') {
			return new TestMove('B');
		}
		return new TestMove('D');
	}
}
