/*
 * @(#)ReversiMoveTest.java		2007/05/07
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import junit.framework.TestCase;

/**
 * Unit tests for {@link ReversiMove}.
 * @author mpatric
 */
public class ReversiMoveTest extends TestCase {

	@SuppressWarnings("deprecation")
	public void testTwoEquivalentObjectsShouldBeEqual() throws Exception {
		ReversiMove move = new ReversiMove(5, 2);
		ReversiMove move2 = new ReversiMove(5, 2);
		assertEquals(move, move2);
	}
}
