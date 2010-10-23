/*
 * @(#)TestMove.java		2005/11/16
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Move factory used for unit tests.
 * @author mpatric
 */
public class TestMoveFactory implements MoveFactory {

	public TestMove getMove(char positionValue) {
		return new TestMove(positionValue);
	}
	
	public Move createMoveFromString(String moveAsString) {
		try {
			return new TestMove(moveAsString.charAt(0));
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Cannot convert string to a TestMove");
		}
	}
}
