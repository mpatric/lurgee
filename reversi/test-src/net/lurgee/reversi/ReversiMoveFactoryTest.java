/*
 * @(#)ReversiMoveFactoryTest.java		2007/10/27
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import junit.framework.TestCase;

/**
 * Unit tests for {@link ReversiMoveFactory}.
 * @author mpatric
 */
public class ReversiMoveFactoryTest extends TestCase {
	
	private ReversiMoveFactory reversiMoveFactory;

	@Override
	protected void setUp() throws Exception {
		reversiMoveFactory = new ReversiMoveFactory();
	}
	
	public void testShouldGetMoveFromStringRepresentation() throws Exception {
		ReversiMove move = reversiMoveFactory.createMove(8, 4); 
		String moveAsString = move.toString();
		ReversiMove move2 = reversiMoveFactory.createMoveFromString(moveAsString);
		assertSame(move, move2);
	}
	
	public void testShouldThrowExceptionWhenGettingMoveForInvalidString() throws Exception {
		try {
			reversiMoveFactory.createMoveFromString("invalid");
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	public void testShouldThrowExceptionWhenGettingMoveForInvalidMoves() throws Exception {
		try {
			reversiMoveFactory.createMoveFromString("a9");
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
		try {
			reversiMoveFactory.createMoveFromString("i8");
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
}
