/*
 * @(#)AbstractCompetitorTest.java		2007/03/07
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import junit.framework.TestCase;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.MoveFactory;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.TestBoard;
import net.lurgee.sgf.TestMoveFactory;
import net.lurgee.sgf.TestPlayer;

/**
 * Unit tests for {@link AbstractCompetitor}.
 * @author mpatric
 */
public class AbstractCompetitorTest extends TestCase {
	
	private TestPlayer player;
	private TestCompetitor competitor;
	private GameContext gameContext;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = new Player[] {TestPlayer.getInstance(1), TestPlayer.getInstance(2)};
		ObjectPool testBoardPool = new ObjectPool(TestBoard.class);
		MoveFactory testMoveFactory = new TestMoveFactory();
		gameContext = new GameContext(players, testBoardPool, testMoveFactory, false);
		player = TestPlayer.getInstance(1);
		competitor = new TestCompetitor(gameContext, player);
	}
	
	public void testShouldSetPlayer() throws Exception {
		assertSame(player, competitor.getPlayer());
	}
}
