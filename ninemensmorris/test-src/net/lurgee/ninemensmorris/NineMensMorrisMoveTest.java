/*
 * @(#)NineMensMorrisMoveTest.java		2008/01/21
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import junit.framework.TestCase;

/**
 * Unit tests for {@link NineMensMorrisMove} and subclasses.
 * @author mpatric
 */
public class NineMensMorrisMoveTest extends TestCase {
	
	private static final int UNDEFINED = -1;

	@SuppressWarnings("deprecation")
	public void testTwoEquivalentObjectsShouldBeEqual() throws Exception {
		NineMensMorrisMove placementMove1 = new NineMensMorrisMove(8, UNDEFINED, UNDEFINED);
		NineMensMorrisMove placementMove2 = new NineMensMorrisMove(8, UNDEFINED, UNDEFINED);
		assertEquals(placementMove1, placementMove2);
		NineMensMorrisMove shiftMove1 = new NineMensMorrisMove(8, 9, UNDEFINED);
		NineMensMorrisMove shiftMove2 = new NineMensMorrisMove(8, 9, UNDEFINED);
		assertEquals(shiftMove1, shiftMove2);
		NineMensMorrisMove placementMoveWithCapture1 = new NineMensMorrisMove(8, UNDEFINED, 3);
		NineMensMorrisMove placementMoveWithCapture2 = new NineMensMorrisMove(8, UNDEFINED, 3);
		assertEquals(placementMoveWithCapture1, placementMoveWithCapture2);
		NineMensMorrisMove shiftMoveWithCapture1 = new NineMensMorrisMove(8, 9, 3);
		NineMensMorrisMove shiftMoveWithCapture2 = new NineMensMorrisMove(8, 9, 3);
		assertEquals(shiftMoveWithCapture1, shiftMoveWithCapture2);
	}
	
	@SuppressWarnings("deprecation")
	public void testCopiedObjectsShouldBeEqual() throws Exception {
		NineMensMorrisMove placementMove1 = new NineMensMorrisMove(8, UNDEFINED, UNDEFINED);
		NineMensMorrisMove placementMove2 = new NineMensMorrisMove(placementMove1);
		assertEquals(placementMove1, placementMove2);
		NineMensMorrisMove shiftMove1 = new NineMensMorrisMove(8, 9, UNDEFINED);
		NineMensMorrisMove shiftMove2 = new NineMensMorrisMove(shiftMove1);
		assertEquals(shiftMove1, shiftMove2);
		NineMensMorrisMove placementMoveWithCapture1 = new NineMensMorrisMove(8, UNDEFINED, 3);
		NineMensMorrisMove placementMoveWithCapture2 = new NineMensMorrisMove(placementMoveWithCapture1);
		assertEquals(placementMoveWithCapture1, placementMoveWithCapture2);
		NineMensMorrisMove shiftMoveWithCapture1 = new NineMensMorrisMove(8, 9, 3);
		NineMensMorrisMove shiftMoveWithCapture2 = new NineMensMorrisMove(shiftMoveWithCapture1);
		assertEquals(shiftMoveWithCapture1, shiftMoveWithCapture2);
	}
	
	@SuppressWarnings("deprecation")
	public void testShouldSetAndGetPositionForPlacementMoves() throws Exception {
		NineMensMorrisMove move = new NineMensMorrisMove(8, UNDEFINED, UNDEFINED);
		assertEquals(8, move.getPosition());
		assertEquals(UNDEFINED, move.getToPosition());
		assertEquals(UNDEFINED, move.getCapturePosition());
		assertTrue(move.isPlacement());
		assertFalse(move.hasCapture());
	}
	
	@SuppressWarnings("deprecation")
	public void testShouldSetAndGetToAndFromPositionsForShiftMoves() throws Exception {
		NineMensMorrisMove move = new NineMensMorrisMove(8, 9, UNDEFINED);
		assertEquals(8, move.getFromPosition());
		assertEquals(9, move.getToPosition());
		assertEquals(UNDEFINED, move.getCapturePosition());
		assertFalse(move.isPlacement());
		assertFalse(move.hasCapture());
	}
	
	@SuppressWarnings("deprecation")
	public void testShouldSetAndGetPositionAndCapturePositionForPlacementWithCaptureMoves() throws Exception {
		NineMensMorrisMove move = new NineMensMorrisMove(8, UNDEFINED, 3);
		assertEquals(8, move.getPosition());
		assertEquals(UNDEFINED, move.getToPosition());
		assertEquals(3, move.getCapturePosition());
		assertTrue(move.isPlacement());
		assertTrue(move.hasCapture());
	}
	
	@SuppressWarnings("deprecation")
	public void testShouldSetAndGetToAndFromPositionsAndCapturePositionForShiftMovesWithCapture() throws Exception {
		NineMensMorrisMove move = new NineMensMorrisMove(8, 9, 3);
		assertEquals(8, move.getFromPosition());
		assertEquals(9, move.getToPosition());
		assertEquals(3, move.getCapturePosition());
		assertFalse(move.isPlacement());
		assertTrue(move.hasCapture());
	}
	
	@SuppressWarnings("deprecation")
	public void testShouldReturnCorrectStringRepresentationOfMoves() throws Exception {
		int positionF2 = NineMensMorrisPosition.lookupNumberFromName("f2");
		int positionC4 = NineMensMorrisPosition.lookupNumberFromName("c4");
		int positionB4 = NineMensMorrisPosition.lookupNumberFromName("b4");
		int positionG1 = NineMensMorrisPosition.lookupNumberFromName("g1");
		assertEquals("f2", new NineMensMorrisMove(positionF2, UNDEFINED, UNDEFINED).toString());
		assertEquals("c4b4", new NineMensMorrisMove(positionC4, positionB4, UNDEFINED).toString());
		assertEquals("f2 -g1", new NineMensMorrisMove(positionF2, UNDEFINED, positionG1).toString());
		assertEquals("c4b4 -g1", new NineMensMorrisMove(positionC4, positionB4, positionG1).toString());
	}
}
