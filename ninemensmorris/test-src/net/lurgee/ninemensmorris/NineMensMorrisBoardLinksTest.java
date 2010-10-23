package net.lurgee.ninemensmorris;

import java.util.Arrays;

import junit.framework.TestCase;

public class NineMensMorrisBoardLinksTest extends TestCase {
	
	public void testShouldReturnSetOfPositionsAPositionLinksTo() throws Exception {
		int [][] expectedLinksTo = {
			{4, 17, 8, 16}, {17, 5, 18, 9}, {10, 18, 6, 19}, {16, 11, 19, 7},
			{13, 0, 12}, {13, 14, 1}, {2, 14, 15}, {12, 3, 15},
			{0, 21, 20}, {21, 1, 22}, {22, 2, 23}, {20, 23, 3},
			{4, 7}, {5, 4}, {5, 6}, {7, 6},
			{0, 3}, {1, 0}, {1, 2}, {3, 2},
			{8, 11}, {9, 8}, {9, 10}, {11, 10}	
		};
		for (int position = 0; position < 24; position++) {
			assertArraysContainSameElements(expectedLinksTo[position], NineMensMorrisBoardLinks.linksTo(position));
		}
	}

	public void testAllLinkedPositionsShouldLinkInBothDirectionsAndThereShouldBe64TotalLinks() throws Exception {
		int totalLinks = 0;
		for (int position = 0; position < 24; position++) {
			int[] linksTo = NineMensMorrisBoardLinks.linksTo(position);
			totalLinks += linksTo.length;
			for (Integer linkedPosition : linksTo) {
				assertTrue(linkedPosition + " should link to " + position, NineMensMorrisBoardLinks.areLinked(linkedPosition, position));
				assertTrue(position + " should link to " + linkedPosition, NineMensMorrisBoardLinks.areLinked(position, linkedPosition));
			}
		}
		assertEquals(64, totalLinks);
	}
	
	public void testPositionsShouldMapToCorrectLines() throws Exception {
		int[][] expectedLinesForPositions = {
			{0, 4}, {1, 5}, {2, 6}, {3, 7}, {0, 8}, {1, 9}, {2, 10}, {3, 11},
			{0, 12}, {1, 13}, {2, 14}, {3, 15}, {8, 11}, {8, 9}, {9, 10}, {10, 11},
			{4, 7}, {4, 5}, {5, 6}, {6, 7}, {12, 15}, {12, 13}, {13, 14}, {14, 15}	
		};
		for (int position = 0; position < 24; position++) {
			int[] linesForPosition = NineMensMorrisBoardLinks.getLinesForPosition(position);
			assertTrue(Arrays.equals(expectedLinesForPositions[position], linesForPosition));
			assertTrue(NineMensMorrisBoardLinks.isInLine(position, expectedLinesForPositions[position][0]));
			assertTrue(NineMensMorrisBoardLinks.isInLine(position, expectedLinesForPositions[position][1]));
		}
	}

	public void testShouldIndicatePositionsNotInLinesCorrectly() throws Exception {
		assertFalse(NineMensMorrisBoardLinks.isInLine(0, 1));
		assertFalse(NineMensMorrisBoardLinks.isInLine(0, 2));
		assertFalse(NineMensMorrisBoardLinks.isInLine(0, 3));
		assertFalse(NineMensMorrisBoardLinks.isInLine(0, 15));
		assertFalse(NineMensMorrisBoardLinks.isInLine(10, 0));
		assertFalse(NineMensMorrisBoardLinks.isInLine(10, 10));
		assertFalse(NineMensMorrisBoardLinks.isInLine(10, 15));
	}
	
	public void testLinesShouldMapToCorrectAdjacentPositions() throws Exception {
		int[][] expectedAdjacentPositionsForLines = {
			{12, 13, 16, 17, 20, 21}, {13, 14, 17, 18, 21, 22}, {14, 15, 18, 19, 22, 23}, {15, 12, 19, 16, 23, 20},
			{4, 8, 3, 1}, {5, 9, 2, 0}, {6, 10, 1, 3}, {7, 11, 0, 2},
			{0, 7, 5}, {1, 6, 4}, {2, 5, 7}, {3, 4, 6},
			{0, 11, 9}, {1, 10, 8}, {2, 9, 11}, {3, 8, 10}
		};
		for (int line = 0; line < 16; line++) {
			assertArraysContainSameElements(expectedAdjacentPositionsForLines[line], NineMensMorrisBoardLinks.getAdjacentPositionsToLine(line));
		}
	}
	
	private void assertArraysContainSameElements(int[] expected, int[] actual) {
		assertEquals(expected.length, actual.length);
		for (int i : expected) {
			boolean found = false;
			for (int j : actual) {
				if (i == j) {
					found = true;
					break;
				}
			}
			assertTrue("Expected element with value " + i + " not found", found);
		}
	}
}
