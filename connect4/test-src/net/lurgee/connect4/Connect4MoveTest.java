/*
 * @(#)Connect4MoveTest.java		2007/06/07
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import junit.framework.TestCase;

/**
 * Unit tests for {@link Connect4Move}.
 * @author mpatric
 */
public class Connect4MoveTest extends TestCase {

	@SuppressWarnings("deprecation")
	public void testTwoEquivalentObjectsShouldBeEqual() throws Exception {
		Connect4Move move = new Connect4Move(5);
		Connect4Move move2 = new Connect4Move(5);
		assertEquals(move, move2);
	}
}
