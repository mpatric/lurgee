/*
 * @(#)NineMensMorrisMoveRankerTest.java		2008/02/07
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import junit.framework.TestCase;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;

/**
 * Unit tests for {@link NineMensMorrisMove} and subclasses.
 * @author mpatric
 */
public class NineMensMorrisMoveRankerTest extends TestCase {

	private static final String TEST_BOARD_WITH_HOLES = 
		"  a  b  c  d  e  f  g\n" +
		"7 +--------+--------+\n" +
		"  |        |        |\n" +
		"6 |  +-----X-----X  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  +--+--+  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 +--+--+     +--+--+\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  +--+--+  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  O-----O-----+  |\n" +
		"  |        |        |\n" +
		"1 +--------+--------+";
	
	private GameContext gameContext;
	private NineMensMorrisBoard board;
	private NineMensMorrisMoveRanker moveRanker = new NineMensMorrisMoveRanker();
	private NineMensMorrisMoveFactory moveFactory;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = {NineMensMorrisPlayer.getInstance(Colour.WHITE), NineMensMorrisPlayer.getInstance(Colour.BLACK)};
		ObjectPool boardPool = new ObjectPool(NineMensMorrisBoard.class);
		moveFactory = new NineMensMorrisMoveFactory();
		gameContext = new GameContext(players, boardPool, moveFactory, false);
		board = (NineMensMorrisBoard) gameContext.checkOutBoard();
		board.initialise();
	}
	
	public void testShouldGiveHigherRankToPlacementMovesOnPositionsWithHigherValency() throws Exception {
		int rankForPositionA7 = moveRanker.getRank(moveFactory.createPlacementMove(NineMensMorrisPosition.lookupNumberFromName("a7")), board, 1);
		int rankForPositionD7 = moveRanker.getRank(moveFactory.createPlacementMove(NineMensMorrisPosition.lookupNumberFromName("d7")), board, 1);
		int rankForPositionD6 = moveRanker.getRank(moveFactory.createPlacementMove(NineMensMorrisPosition.lookupNumberFromName("d6")), board, 1);
		assertTrue(rankForPositionA7 < rankForPositionD7);
		assertTrue(rankForPositionD7 < rankForPositionD6);
	}
	
	public void testShouldGiveHigherRankToShiftMovesToPositionsWithHigherValency() throws Exception {
		int rankForShiftFromD7ToA7 = moveRanker.getRank(moveFactory.createShiftMove(NineMensMorrisPosition.lookupNumberFromName("d7"), NineMensMorrisPosition.lookupNumberFromName("a7")), board, 1);
		int rankForShiftFromD7ToD5 = moveRanker.getRank(moveFactory.createShiftMove(NineMensMorrisPosition.lookupNumberFromName("d7"), NineMensMorrisPosition.lookupNumberFromName("d5")), board, 1);
		int rankForShiftFromD7ToD6 = moveRanker.getRank(moveFactory.createShiftMove(NineMensMorrisPosition.lookupNumberFromName("d7"), NineMensMorrisPosition.lookupNumberFromName("d6")), board, 1);
		assertTrue(rankForShiftFromD7ToA7 < rankForShiftFromD7ToD5);
		assertTrue(rankForShiftFromD7ToD5 < rankForShiftFromD7ToD6);
	}

	public void testShouldGiveHigherRankToPlacementMovesOnHoles() throws Exception {
		board.fromString(TEST_BOARD_WITH_HOLES);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		int rankForPositionB6 = moveRanker.getRank(moveFactory.createPlacementMove(NineMensMorrisPosition.lookupNumberFromName("b6")), board, 1);
		int rankForPositionF2 = moveRanker.getRank(moveFactory.createPlacementMove(NineMensMorrisPosition.lookupNumberFromName("f2")), board, 1);
		int rankForPositionC5 = moveRanker.getRank(moveFactory.createPlacementMove(NineMensMorrisPosition.lookupNumberFromName("c5")), board, 1);
		int rankForPositionB4 = moveRanker.getRank(moveFactory.createPlacementMove(NineMensMorrisPosition.lookupNumberFromName("b4")), board, 1);
		assertTrue(rankForPositionF2 < rankForPositionB6);
		assertTrue(rankForPositionC5 < rankForPositionF2);
		assertTrue(rankForPositionB4 < rankForPositionF2);
	}
}
