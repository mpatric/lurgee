/*
 * @(#)ReversiPositionTest.java		2007/10/05
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import junit.framework.TestCase;

/**
 * Unit tests for {@link ReversiPosition}.
 * @author mpatric
 */
public class ReversiPositionTest extends TestCase {

	public void testShouldSetAndGetXandYValues() throws Exception {
		ReversiPosition position = new ReversiPosition(1, 8);
		assertEquals(1, position.getX());
		assertEquals(8, position.getY());
		position = new ReversiPosition(8, 1);
		assertEquals(8, position.getX());
		assertEquals(1, position.getY());
	}
	
	public void testTwoEquivalentObjectsShouldBeEqualIrrespectiveOfTransientProperties() throws Exception {
		ReversiPosition position1 = new ReversiPosition(3, 8);
		position1.setAnimationFrame(5);
		ReversiPosition position2 = new ReversiPosition(3, 8);
		position2.setAnimationFrame(6);
		assertEquals(position1, position2);
	}
	
	public void testCopiedObjectShouldBeEqualToOriginalIncludingTransientProperties() throws Exception {
		ReversiPosition position1 = new ReversiPosition(3, 8);
		position1.setAnimationFrame(5);
		ReversiPosition position2 = new ReversiPosition(position1);
		assertEquals(position1, position2);
		assertEquals(position1.getAnimationFrame(), position2.getAnimationFrame());
	}
}
