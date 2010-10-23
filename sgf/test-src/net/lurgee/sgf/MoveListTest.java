/*
 * @(#)MoveListTest.java		2005/11/18
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Unit tests for {@link MoveList}.
 * @author mpatric
 */
public class MoveListTest extends TestCase {
	
	private static final TestMove MOVE_A = new TestMove('A');
	private static final TestMove MOVE_B = new TestMove('B');
	private static final TestMove MOVE_C = new TestMove('C');
	private static final TestMove MOVE_D = new TestMove('D');
	private static final TestMove MOVE_E = new TestMove('E');
	
	private MoveList moveListWithoutRandomSelect;
	private MoveList moveListWithRandomSelect;
	
	@Override
	protected void setUp() throws Exception {
		moveListWithoutRandomSelect = new MoveList(4, false);
		moveListWithRandomSelect = new MoveList(4, true);
	}
	
	public void testShouldGetAllMovesAddedToList() throws Exception {
		moveListWithoutRandomSelect.add(MOVE_A);
		moveListWithoutRandomSelect.add(MOVE_C);
		moveListWithoutRandomSelect.add(MOVE_E);
		List<Move> expectedList = new ArrayList<Move>();
		expectedList.add(MOVE_A);
		expectedList.add(MOVE_C);
		expectedList.add(MOVE_E);
		assertEquals(expectedList, moveListWithoutRandomSelect.getMoves());
	}
	
	public void testShouldReturnCorrectMoveFromList() throws Exception {
		moveListWithoutRandomSelect.add(MOVE_A);
		moveListWithoutRandomSelect.add(MOVE_C);
		moveListWithoutRandomSelect.add(MOVE_E);
		assertEquals(MOVE_A, moveListWithoutRandomSelect.getMove(0));
		assertEquals(MOVE_C, moveListWithoutRandomSelect.getMove(1));
		assertEquals(MOVE_E, moveListWithoutRandomSelect.getMove(2));
	}
	
	public void testShouldGetCorrectSizeOfMoveList() throws Exception {
		moveListWithoutRandomSelect.add(MOVE_A);
		moveListWithoutRandomSelect.add(MOVE_C);
		moveListWithoutRandomSelect.add(MOVE_E);
		assertEquals(3, moveListWithoutRandomSelect.size());
	}
	
	public void testShouldNotOrderUnrankedMovesWhenAddedToMoveList() {
		moveListWithoutRandomSelect.add(MOVE_A);
		moveListWithoutRandomSelect.add(MOVE_B);
		moveListWithoutRandomSelect.add(MOVE_C);
		moveListWithoutRandomSelect.add(MOVE_D);
		moveListWithoutRandomSelect.add(MOVE_E);
		assertEquals(MOVE_A, moveListWithoutRandomSelect.getMove(0));
		assertEquals(MOVE_B, moveListWithoutRandomSelect.getMove(1));
		assertEquals(MOVE_C, moveListWithoutRandomSelect.getMove(2));
		assertEquals(MOVE_D, moveListWithoutRandomSelect.getMove(3));
		assertEquals(MOVE_E, moveListWithoutRandomSelect.getMove(4));
	}

	public void testShouldOrderRankedMovesByRankWhenAddedToMoveList() {
		moveListWithoutRandomSelect.add(MOVE_A, 3);
		moveListWithoutRandomSelect.add(MOVE_B, 1);
		moveListWithoutRandomSelect.add(MOVE_C, 2);
		moveListWithoutRandomSelect.add(MOVE_D, 5);
		moveListWithoutRandomSelect.add(MOVE_E, 4);
		assertEquals(MOVE_D, moveListWithoutRandomSelect.getMove(0));
		assertEquals(MOVE_E, moveListWithoutRandomSelect.getMove(1));
		assertEquals(MOVE_A, moveListWithoutRandomSelect.getMove(2));
		assertEquals(MOVE_C, moveListWithoutRandomSelect.getMove(3));
		assertEquals(MOVE_B, moveListWithoutRandomSelect.getMove(4));
	}
	
	public void testShouldClearMoveList() throws Exception {
		moveListWithoutRandomSelect.add(MOVE_A);
		moveListWithoutRandomSelect.add(MOVE_B);
		moveListWithoutRandomSelect.add(MOVE_C);
		moveListWithoutRandomSelect.clear();
		assertEquals(0, moveListWithoutRandomSelect.size());
	}
	
	public void testShouldSelectAllRandomNumbersInProvidedRange() throws Exception {
		int numberOfOptions = 5;
		int[] selections = new int[numberOfOptions];
		for (int i = 0; i < selections.length; i++) {
			selections[i] = 0;
		}
		for (int i = 1; i <= 100 * numberOfOptions; i++) {
			selections[moveListWithRandomSelect.selectRandom(numberOfOptions)]++;
		}
		for (int selection : selections) {
			assertTrue(selection > 0);
		}
	}
	
	public void testShouldOrderEqualScoresInOrderDeterminedBySelectRandomMethodWhenMixedWithOtherScores() throws Exception {
		MoveList stubMoveListWhichAlwaysReturnsLastItem = new MoveList(4, true) {
			@Override
			protected int selectRandom(int numberOfChoices) {
				return numberOfChoices - 1;
			}
		};
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_A, 2);
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_B, 1);
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_C, 2);
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_D, 3);
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_E, 2);
		assertEquals(MOVE_D, stubMoveListWhichAlwaysReturnsLastItem.getMove(0));
		assertEquals(MOVE_A, stubMoveListWhichAlwaysReturnsLastItem.getMove(1));
		assertEquals(MOVE_C, stubMoveListWhichAlwaysReturnsLastItem.getMove(2));
		assertEquals(MOVE_E, stubMoveListWhichAlwaysReturnsLastItem.getMove(3));
		assertEquals(MOVE_B, stubMoveListWhichAlwaysReturnsLastItem.getMove(4));

		MoveList stubMoveListWhichAlwaysReturnsFirstItem = new MoveList(4, true) {
			@Override
			protected int selectRandom(int numberOfChoices) {
				return 0;
			}
		};
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_A, 2);
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_B, 1);
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_C, 2);
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_D, 3);
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_E, 2);
		assertEquals(MOVE_D, stubMoveListWhichAlwaysReturnsFirstItem.getMove(0));
		assertEquals(MOVE_E, stubMoveListWhichAlwaysReturnsFirstItem.getMove(1));
		assertEquals(MOVE_C, stubMoveListWhichAlwaysReturnsFirstItem.getMove(2));
		assertEquals(MOVE_A, stubMoveListWhichAlwaysReturnsFirstItem.getMove(3));
		assertEquals(MOVE_B, stubMoveListWhichAlwaysReturnsFirstItem.getMove(4));
	}
	
	public void testShouldOrderEqualScoresInOrderDeterminedBySelectRandomMethodWhenAllScoresAreEqual() throws Exception {
		MoveList stubMoveListWhichAlwaysReturnsLastItem = new MoveList(4, true) {
			@Override
			protected int selectRandom(int numberOfChoices) {
				return numberOfChoices - 1;
			}
		};
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_A, 2);
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_B, 2);
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_C, 2);
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_D, 2);
		stubMoveListWhichAlwaysReturnsLastItem.add(MOVE_E, 2);
		assertEquals(MOVE_A, stubMoveListWhichAlwaysReturnsLastItem.getMove(0));
		assertEquals(MOVE_B, stubMoveListWhichAlwaysReturnsLastItem.getMove(1));
		assertEquals(MOVE_C, stubMoveListWhichAlwaysReturnsLastItem.getMove(2));
		assertEquals(MOVE_D, stubMoveListWhichAlwaysReturnsLastItem.getMove(3));
		assertEquals(MOVE_E, stubMoveListWhichAlwaysReturnsLastItem.getMove(4));
		
		MoveList stubMoveListWhichAlwaysReturnsFirstItem = new MoveList(4, true) {
			@Override
			protected int selectRandom(int numberOfChoices) {
				return 0;
			}
		};
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_A, 2);
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_B, 2);
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_C, 2);
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_D, 2);
		stubMoveListWhichAlwaysReturnsFirstItem.add(MOVE_E, 2);
		assertEquals(MOVE_E, stubMoveListWhichAlwaysReturnsFirstItem.getMove(0));
		assertEquals(MOVE_D, stubMoveListWhichAlwaysReturnsFirstItem.getMove(1));
		assertEquals(MOVE_C, stubMoveListWhichAlwaysReturnsFirstItem.getMove(2));
		assertEquals(MOVE_B, stubMoveListWhichAlwaysReturnsFirstItem.getMove(3));
		assertEquals(MOVE_A, stubMoveListWhichAlwaysReturnsFirstItem.getMove(4));
	}
}
