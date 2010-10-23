/*
 * @(#)NineMensMorrisMoveFactoryTest.java		2008/01/25
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
public class NineMensMorrisMoveFactoryTest extends TestCase {

	private static final int UNDEFINED = -1;
	private static final int A7 = NineMensMorrisPosition.lookupNumberFromName("a7");
	private static final int D7 = NineMensMorrisPosition.lookupNumberFromName("d7");
	private static final int D5 = NineMensMorrisPosition.lookupNumberFromName("d5");
	
	private NineMensMorrisMoveFactory factory = new NineMensMorrisMoveFactory();
	
	public void testShouldCreatePlacementMove() throws Exception {
		NineMensMorrisMove move = factory.createPlacementMove(A7);
		assertEquals(A7, move.getPosition());
		assertEquals(UNDEFINED, move.getToPosition());
		assertEquals(UNDEFINED, move.getCapturePosition());
	}

	public void testShouldCreateShiftMove() throws Exception {
		NineMensMorrisMove move = factory.createShiftMove(A7, D7);
		assertEquals(A7, move.getFromPosition());
		assertEquals(D7, move.getToPosition());
		assertEquals(UNDEFINED, move.getCapturePosition());
	}
	
	public void testShouldCreatePlacementMoveWithCapture() throws Exception {
		NineMensMorrisMove move = factory.createPlacementMove(A7, D5);
		assertEquals(A7, move.getPosition());
		assertEquals(UNDEFINED, move.getToPosition());
		assertEquals(D5, move.getCapturePosition());
	}
	
	public void testShouldCreateShiftMoveWithCapture() throws Exception {
		NineMensMorrisMove move = factory.createShiftMove(A7, D7, D5);
		assertEquals(A7, move.getFromPosition());
		assertEquals(D7, move.getToPosition());
		assertEquals(D5, move.getCapturePosition());
	}
	
	public void testShouldCreatePlacementMoveFromString() throws Exception {
		NineMensMorrisMove move = factory.createMoveFromString("a7");
		assertEquals(A7, move.getFromPosition());
		assertEquals(UNDEFINED, move.getToPosition());
		assertEquals(UNDEFINED, move.getCapturePosition());
	}
	
	public void testShouldCreateShiftMoveFromString() throws Exception {
		NineMensMorrisMove move = factory.createMoveFromString("a7d7");
		assertEquals(A7, move.getFromPosition());
		assertEquals(D7, move.getToPosition());
		assertEquals(UNDEFINED, move.getCapturePosition());
	}
	
	public void testShouldCreatePlacementMoveWithCaptureFromString() throws Exception {
		NineMensMorrisMove move = factory.createMoveFromString("a7 -d5");
		assertEquals(A7, move.getFromPosition());
		assertEquals(UNDEFINED, move.getToPosition());
		assertEquals(D5, move.getCapturePosition());
	}
	
	public void testShouldCreateShiftMoveWithCaptureFromString() throws Exception {
		NineMensMorrisMove move = factory.createMoveFromString("a7d7 -d5");
		assertEquals(A7, move.getFromPosition());
		assertEquals(D7, move.getToPosition());
		assertEquals(D5, move.getCapturePosition());
	}
	
	public void testKeysShouldBeUniqueForAllMoves() throws Exception {
		for (int i = 0; i < 24; i++) {
			factory.createPlacementMove(i);
		}
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				factory.createShiftMove(i, j);
			}
		}
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				factory.createPlacementMove(i, j);
			}
		}
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				for (int k = 0; k < 24; k++) {
					factory.createShiftMove(i, j, k);
				}
			}
		}
		assertEquals(15000, factory.getMoveMap().size());
	}
}
