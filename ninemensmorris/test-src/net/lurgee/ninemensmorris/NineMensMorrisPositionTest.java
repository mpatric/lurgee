/*
 * @(#)NineMensMorrisPositionTest.java		2008/01/21
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Unit tests for {@link NineMensMorrisPosition}.
 * @author mpatric
 */
public class NineMensMorrisPositionTest extends TestCase {
	
	public void testShouldGetAllPossiblePositions() throws Exception {
		for (int i = 0; i < NineMensMorrisBoard.NUMBER_OF_POSITIONS; i++) {
			assertEquals(i, NineMensMorrisPosition.POSITIONS[i].getNumber());
		}
	}

	public void testShouldSetAndGetProperties() throws Exception {
		for (int position = 0; position < 24; position++) {
			assertEquals(position, new NineMensMorrisPosition(position).getNumber());
		}
	}
	
	public void testShouldThrowExceptionForInvalidNumber() throws Exception {
		assertInvalidNumberForPosition(-1);
		assertInvalidNumberForPosition(25);
	}
	
	public void testTwoEquivalentObjectsShouldBeEqual() throws Exception {
		NineMensMorrisPosition position1 = new NineMensMorrisPosition(6);
		NineMensMorrisPosition position2 = new NineMensMorrisPosition(6);
		assertEquals(position1, position2);
	}
	
	public void testCopiedObjectShouldBeEqualToOriginal() throws Exception {
		NineMensMorrisPosition position1 = new NineMensMorrisPosition(4);
		NineMensMorrisPosition position2 = new NineMensMorrisPosition(position1);
		assertEquals(position1, position2);
	}
	
	public void testShouldLookupPositionNumberForEachPositionNameForAll24Positions() throws Exception {
		assertEquals(0, NineMensMorrisPosition.lookupNumberFromName("d6"));
		assertEquals(1, NineMensMorrisPosition.lookupNumberFromName("f4"));
		assertEquals(2, NineMensMorrisPosition.lookupNumberFromName("d2"));
		assertEquals(3, NineMensMorrisPosition.lookupNumberFromName("b4"));
		assertEquals(4, NineMensMorrisPosition.lookupNumberFromName("d7"));
		assertEquals(5, NineMensMorrisPosition.lookupNumberFromName("g4"));
		assertEquals(6, NineMensMorrisPosition.lookupNumberFromName("d1"));
		assertEquals(7, NineMensMorrisPosition.lookupNumberFromName("a4"));
		assertEquals(8, NineMensMorrisPosition.lookupNumberFromName("d5"));
		assertEquals(9, NineMensMorrisPosition.lookupNumberFromName("e4"));
		assertEquals(10, NineMensMorrisPosition.lookupNumberFromName("d3"));
		assertEquals(11, NineMensMorrisPosition.lookupNumberFromName("c4"));
		assertEquals(12, NineMensMorrisPosition.lookupNumberFromName("a7"));
		assertEquals(13, NineMensMorrisPosition.lookupNumberFromName("g7"));
		assertEquals(14, NineMensMorrisPosition.lookupNumberFromName("g1"));
		assertEquals(15, NineMensMorrisPosition.lookupNumberFromName("a1"));
		assertEquals(16, NineMensMorrisPosition.lookupNumberFromName("b6"));
		assertEquals(17, NineMensMorrisPosition.lookupNumberFromName("f6"));
		assertEquals(18, NineMensMorrisPosition.lookupNumberFromName("f2"));
		assertEquals(19, NineMensMorrisPosition.lookupNumberFromName("b2"));
		assertEquals(20, NineMensMorrisPosition.lookupNumberFromName("c5"));
		assertEquals(21, NineMensMorrisPosition.lookupNumberFromName("e5"));
		assertEquals(22, NineMensMorrisPosition.lookupNumberFromName("e3"));
		assertEquals(23, NineMensMorrisPosition.lookupNumberFromName("c3"));
	}
	
	public void testShouldLookupPositionNumberForEachPositionNameForAll24PositionsByIndex() throws Exception {
		assertEquals(0, NineMensMorrisPosition.lookupPositionFromIndex(4));
		assertEquals(1, NineMensMorrisPosition.lookupPositionFromIndex(13));
		assertEquals(2, NineMensMorrisPosition.lookupPositionFromIndex(19));
		assertEquals(3, NineMensMorrisPosition.lookupPositionFromIndex(10));
		assertEquals(4, NineMensMorrisPosition.lookupPositionFromIndex(1));
		assertEquals(5, NineMensMorrisPosition.lookupPositionFromIndex(14));
		assertEquals(6, NineMensMorrisPosition.lookupPositionFromIndex(22));
		assertEquals(7, NineMensMorrisPosition.lookupPositionFromIndex(9));
		assertEquals(8, NineMensMorrisPosition.lookupPositionFromIndex(7));
		assertEquals(9, NineMensMorrisPosition.lookupPositionFromIndex(12));
		assertEquals(10, NineMensMorrisPosition.lookupPositionFromIndex(16));
		assertEquals(11, NineMensMorrisPosition.lookupPositionFromIndex(11));
		assertEquals(12, NineMensMorrisPosition.lookupPositionFromIndex(0));
		assertEquals(13, NineMensMorrisPosition.lookupPositionFromIndex(2));
		assertEquals(14, NineMensMorrisPosition.lookupPositionFromIndex(23));
		assertEquals(15, NineMensMorrisPosition.lookupPositionFromIndex(21));
		assertEquals(16, NineMensMorrisPosition.lookupPositionFromIndex(3));
		assertEquals(17, NineMensMorrisPosition.lookupPositionFromIndex(5));
		assertEquals(18, NineMensMorrisPosition.lookupPositionFromIndex(20));
		assertEquals(19, NineMensMorrisPosition.lookupPositionFromIndex(18));
		assertEquals(20, NineMensMorrisPosition.lookupPositionFromIndex(6));
		assertEquals(21, NineMensMorrisPosition.lookupPositionFromIndex(8));
		assertEquals(22, NineMensMorrisPosition.lookupPositionFromIndex(17));
		assertEquals(23, NineMensMorrisPosition.lookupPositionFromIndex(15));
	}
	
	public void testShouldReturn0ForInvalidPositionNames() throws Exception {
		assertEquals(0, NineMensMorrisPosition.lookupNumberFromName(null));
		assertEquals(0, NineMensMorrisPosition.lookupNumberFromName(""));
		assertEquals(0, NineMensMorrisPosition.lookupNumberFromName("a"));
		assertEquals(0, NineMensMorrisPosition.lookupNumberFromName("7"));
		assertEquals(0, NineMensMorrisPosition.lookupNumberFromName("a8"));		
		assertEquals(0, NineMensMorrisPosition.lookupNumberFromName("h1"));		
	}
	
	public void testStringRepresentationOfPositionShouldBeLetterNumberFormatForAll24Positions() throws Exception {
		assertEquals("d6", new NineMensMorrisPosition(0).toString());
		assertEquals("f4", new NineMensMorrisPosition(1).toString());
		assertEquals("d2", new NineMensMorrisPosition(2).toString());
		assertEquals("b4", new NineMensMorrisPosition(3).toString());
		assertEquals("d7", new NineMensMorrisPosition(4).toString());
		assertEquals("g4", new NineMensMorrisPosition(5).toString());
		assertEquals("d1", new NineMensMorrisPosition(6).toString());
		assertEquals("a4", new NineMensMorrisPosition(7).toString());
		assertEquals("d5", new NineMensMorrisPosition(8).toString());
		assertEquals("e4", new NineMensMorrisPosition(9).toString());
		assertEquals("d3", new NineMensMorrisPosition(10).toString());
		assertEquals("c4", new NineMensMorrisPosition(11).toString());
		assertEquals("a7", new NineMensMorrisPosition(12).toString());
		assertEquals("g7", new NineMensMorrisPosition(13).toString());
		assertEquals("g1", new NineMensMorrisPosition(14).toString());
		assertEquals("a1", new NineMensMorrisPosition(15).toString());
		assertEquals("b6", new NineMensMorrisPosition(16).toString());
		assertEquals("f6", new NineMensMorrisPosition(17).toString());
		assertEquals("f2", new NineMensMorrisPosition(18).toString());
		assertEquals("b2", new NineMensMorrisPosition(19).toString());
		assertEquals("c5", new NineMensMorrisPosition(20).toString());
		assertEquals("e5", new NineMensMorrisPosition(21).toString());
		assertEquals("e3", new NineMensMorrisPosition(22).toString());
		assertEquals("c3", new NineMensMorrisPosition(23).toString());
	}
	
	public void testShouldLookupPositionNumberForAllValidCoordinates() throws Exception {
		assertEquals(0, NineMensMorrisPosition.lookupPositionFromCoordinates(3, 1));
		assertEquals(1, NineMensMorrisPosition.lookupPositionFromCoordinates(5, 3));
		assertEquals(2, NineMensMorrisPosition.lookupPositionFromCoordinates(3, 5));
		assertEquals(3, NineMensMorrisPosition.lookupPositionFromCoordinates(1, 3));
		assertEquals(4, NineMensMorrisPosition.lookupPositionFromCoordinates(3, 0));
		assertEquals(5, NineMensMorrisPosition.lookupPositionFromCoordinates(6, 3));
		assertEquals(6, NineMensMorrisPosition.lookupPositionFromCoordinates(3, 6));
		assertEquals(7, NineMensMorrisPosition.lookupPositionFromCoordinates(0, 3));
		assertEquals(8, NineMensMorrisPosition.lookupPositionFromCoordinates(3, 2));
		assertEquals(9, NineMensMorrisPosition.lookupPositionFromCoordinates(4, 3));
		assertEquals(10, NineMensMorrisPosition.lookupPositionFromCoordinates(3, 4));
		assertEquals(11, NineMensMorrisPosition.lookupPositionFromCoordinates(2, 3));
		assertEquals(12, NineMensMorrisPosition.lookupPositionFromCoordinates(0, 0));
		assertEquals(13, NineMensMorrisPosition.lookupPositionFromCoordinates(6, 0));
		assertEquals(14, NineMensMorrisPosition.lookupPositionFromCoordinates(6, 6));
		assertEquals(15, NineMensMorrisPosition.lookupPositionFromCoordinates(0, 6));
		assertEquals(16, NineMensMorrisPosition.lookupPositionFromCoordinates(1, 1));
		assertEquals(17, NineMensMorrisPosition.lookupPositionFromCoordinates(5, 1));
		assertEquals(18, NineMensMorrisPosition.lookupPositionFromCoordinates(5, 5));
		assertEquals(19, NineMensMorrisPosition.lookupPositionFromCoordinates(1, 5));
		assertEquals(20, NineMensMorrisPosition.lookupPositionFromCoordinates(2, 2));
		assertEquals(21, NineMensMorrisPosition.lookupPositionFromCoordinates(4, 2));
		assertEquals(22, NineMensMorrisPosition.lookupPositionFromCoordinates(4, 4));
		assertEquals(23, NineMensMorrisPosition.lookupPositionFromCoordinates(2, 4));
	}
	
	public void testShouldThrowExceptionForInvalidCoordinates() throws Exception {
		assertInvalidCoordinate(0, 1);
		assertInvalidCoordinate(0, 2);
		assertInvalidCoordinate(0, 4);
		assertInvalidCoordinate(0, 5);
		assertInvalidCoordinate(1, 0);
		assertInvalidCoordinate(1, 2);
		assertInvalidCoordinate(1, 4);
		assertInvalidCoordinate(1, 6);
		assertInvalidCoordinate(2, 0);
		assertInvalidCoordinate(2, 1);
		assertInvalidCoordinate(2, 5);
		assertInvalidCoordinate(2, 6);
		assertInvalidCoordinate(3, 3);
		assertInvalidCoordinate(4, 0);
		assertInvalidCoordinate(4, 1);
		assertInvalidCoordinate(4, 5);
		assertInvalidCoordinate(4, 6);
		assertInvalidCoordinate(5, 0);
		assertInvalidCoordinate(5, 2);
		assertInvalidCoordinate(5, 4);
		assertInvalidCoordinate(5, 6);
		assertInvalidCoordinate(6, 1);
		assertInvalidCoordinate(6, 2);
		assertInvalidCoordinate(6, 4);
		assertInvalidCoordinate(6, 5);
	}
	
	public void testShouldLookupCoordinatesForAllPositions() throws Exception {
		assertTrue(Arrays.equals((new int[] {3, 1}), NineMensMorrisPosition.lookupCoordinatesFromPosition(0)));
		assertTrue(Arrays.equals((new int[] {5, 3}), NineMensMorrisPosition.lookupCoordinatesFromPosition(1)));
		assertTrue(Arrays.equals((new int[] {3, 5}), NineMensMorrisPosition.lookupCoordinatesFromPosition(2)));
		assertTrue(Arrays.equals((new int[] {1, 3}), NineMensMorrisPosition.lookupCoordinatesFromPosition(3)));
		assertTrue(Arrays.equals((new int[] {3, 0}), NineMensMorrisPosition.lookupCoordinatesFromPosition(4)));
		assertTrue(Arrays.equals((new int[] {6, 3}), NineMensMorrisPosition.lookupCoordinatesFromPosition(5)));
		assertTrue(Arrays.equals((new int[] {3, 6}), NineMensMorrisPosition.lookupCoordinatesFromPosition(6)));
		assertTrue(Arrays.equals((new int[] {0, 3}), NineMensMorrisPosition.lookupCoordinatesFromPosition(7)));
		assertTrue(Arrays.equals((new int[] {3, 2}), NineMensMorrisPosition.lookupCoordinatesFromPosition(8)));
		assertTrue(Arrays.equals((new int[] {4, 3}), NineMensMorrisPosition.lookupCoordinatesFromPosition(9)));
		assertTrue(Arrays.equals((new int[] {3, 4}), NineMensMorrisPosition.lookupCoordinatesFromPosition(10)));
		assertTrue(Arrays.equals((new int[] {2, 3}), NineMensMorrisPosition.lookupCoordinatesFromPosition(11)));
		assertTrue(Arrays.equals((new int[] {0, 0}), NineMensMorrisPosition.lookupCoordinatesFromPosition(12)));
		assertTrue(Arrays.equals((new int[] {6, 0}), NineMensMorrisPosition.lookupCoordinatesFromPosition(13)));
		assertTrue(Arrays.equals((new int[] {6, 6}), NineMensMorrisPosition.lookupCoordinatesFromPosition(14)));
		assertTrue(Arrays.equals((new int[] {0, 6}), NineMensMorrisPosition.lookupCoordinatesFromPosition(15)));
		assertTrue(Arrays.equals((new int[] {1, 1}), NineMensMorrisPosition.lookupCoordinatesFromPosition(16)));
		assertTrue(Arrays.equals((new int[] {5, 1}), NineMensMorrisPosition.lookupCoordinatesFromPosition(17)));
		assertTrue(Arrays.equals((new int[] {5, 5}), NineMensMorrisPosition.lookupCoordinatesFromPosition(18)));
		assertTrue(Arrays.equals((new int[] {1, 5}), NineMensMorrisPosition.lookupCoordinatesFromPosition(19)));
		assertTrue(Arrays.equals((new int[] {2, 2}), NineMensMorrisPosition.lookupCoordinatesFromPosition(20)));
		assertTrue(Arrays.equals((new int[] {4, 2}), NineMensMorrisPosition.lookupCoordinatesFromPosition(21)));
		assertTrue(Arrays.equals((new int[] {4, 4}), NineMensMorrisPosition.lookupCoordinatesFromPosition(22)));
		assertTrue(Arrays.equals((new int[] {2, 4}), NineMensMorrisPosition.lookupCoordinatesFromPosition(23)));
	}
	
	private void assertInvalidNumberForPosition(int i) {
		try {
			new NineMensMorrisPosition(i);
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	private void assertInvalidCoordinate(int x, int y) {
		try {
			NineMensMorrisPosition.lookupPositionFromCoordinates(x, y);
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid co-ordinate", e.getMessage());
		}
	}
}
