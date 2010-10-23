/*
 * @(#)Connect4MoveFactoryTest.java		2007/10/27
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import junit.framework.TestCase;

/**
 * Unit tests for {@link Connect4MoveFactory}.
 * @author mpatric
 */
public class Connect4MoveFactoryTest extends TestCase {
	
	private Connect4MoveFactory connect4MoveFactory;

	@Override
	protected void setUp() throws Exception {
		connect4MoveFactory = new Connect4MoveFactory();
	}
	
	public void testShouldGetMoveFromStringRepresentation() throws Exception {
		Connect4Move move = connect4MoveFactory.createMove(5); 
		String moveAsString = move.toString();
		Connect4Move move2 = connect4MoveFactory.createMoveFromString(moveAsString);
		assertSame(move, move2);
	}
	
	public void testShouldThrowExceptionWhenGettingMoveForInvalidString() throws Exception {
		try {
			connect4MoveFactory.createMoveFromString("invalid");
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	public void testShouldThrowExceptionWhenGettingMoveForInvalidMoves() throws Exception {
		try {
			connect4MoveFactory.createMoveFromString("0");
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
		try {
			connect4MoveFactory.createMoveFromString("8");
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
}
