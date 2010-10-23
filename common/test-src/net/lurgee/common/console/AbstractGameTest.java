/*
 * @(#)AbstractGameTest.java		2007/03/07
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import junit.framework.TestCase;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.TestBoard;
import net.lurgee.sgf.TestMoveFactory;
import net.lurgee.sgf.TestPlayer;

/**
 * Unit tests for {@link AbstractGame}.
 * @author mpatric
 */
public class AbstractGameTest extends TestCase {
	
	private TestGame game;
	private TestPlayer player1;
	private TestPlayer player2;
	private TestCompetitor competitor1;
	private TestCompetitor competitor2;
	private Input input;
	private GameContext gameContext;
	
	@Override
	protected void setUp() throws Exception {
		player1 = TestPlayer.getInstance(1);
		player2 = TestPlayer.getInstance(2);
		Player[] players = new Player[] {player1, player2};
		ObjectPool testBoardPool = new ObjectPool(TestBoard.class);
		TestBoard.infiniteGameTree = false;
		TestBoard.noMoveSituation = false;
		TestMoveFactory testMoveFactory = new TestMoveFactory();
		gameContext = new GameContext(players, testBoardPool, testMoveFactory, false);
		competitor1 = new TestCompetitor(gameContext, player1);
		competitor2 = new TestCompetitor(gameContext, player2);
		input = new Input();
		game = new TestGame(input);
		game.addCompetitor(competitor1);
		game.addCompetitor(competitor2);
	}
	
	public void testShouldGetCompetitorForPlayer() throws Exception {
		assertSame(competitor1, game.getCompetitor(player1));
		assertSame(competitor2, game.getCompetitor(player2));
	}
	
	public void testGetCompetitorShouldThrowExceptionIfPlayerNotInOneOfTheCompetitors() throws Exception {
		TestPlayer player3 = new TestPlayer(3, '3', "Three");
		try {
			game.getCompetitor(player3);
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	public void testShouldGetNextCompetitor() throws Exception {
		assertSame(competitor2, game.getNextCompetitor(competitor1));
		assertSame(competitor1, game.getNextCompetitor(competitor2));
	}
	
	public void testGetNextCompetitorShouldThrowExceptionIfSpecifiedCompetitorNotInGame() throws Exception {
		TestCompetitor competitor3 = new TestCompetitor(gameContext, new TestPlayer(3, '3', "Three"));
		try {
			game.getNextCompetitor(competitor3);
			fail("Expected exception not thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	public void testGetNextCompetitorShouldThrowExceptionIfNoCompetitorsInGame() throws Exception {
		TestGame gameWithNoCompetitors = new TestGame(input);
		try {
			gameWithNoCompetitors.getNextCompetitor(competitor1);
			fail("Expected exception not thrown");
		} catch (IllegalStateException e) {
			// pass
		}
	}
}
