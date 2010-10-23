/*
 * @(#)Connect4PositionTest.java		2007/10/05
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import junit.framework.TestCase;

/**
 * Unit tests for {@link Connect4Position}.
 * @author mpatric
 */
public class Connect4PositionTest extends TestCase {

	public void testTwoEquivalentObjectsShouldBeEqual() throws Exception {
		Connect4Position position1 = new Connect4Position(3, 8);
		Connect4Position position2 = new Connect4Position(3, 8);
		assertEquals(position1, position2);
	}
	
	public void testCopiedObjectShouldBeEqualToOriginal() throws Exception {
		Connect4Position position1 = new Connect4Position(3, 8);
		Connect4Position position2 = new Connect4Position(position1);
		assertEquals(position1, position2);
	}
}
