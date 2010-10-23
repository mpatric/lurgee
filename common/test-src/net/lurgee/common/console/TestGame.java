/*
 * @(#)TestGame.java		2007/05/25
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import net.lurgee.sgf.TestBoard;

/**
 * Test game for unit tests.
 * @author mpatric
 */
public class TestGame extends AbstractGame {

	private static final String GAME_NAME = "Test game";
	
	private int initCount = 0;
	
	public TestGame(Input input) {
		this.input = input;
		this.board = new TestBoard();
	}

	protected String getGameName() {
		return GAME_NAME;
	}

	protected void init() {
		initCount++;
	}
	
	protected void done() {
	}

	protected void printCurrentState() {
	}

	protected void printEndGameState(AbstractCompetitor startCompetitor) {
	}

	protected void printStats(long startTime, long endTime) {
	}

	public int getInitCount() {
		return initCount;
	}
}
