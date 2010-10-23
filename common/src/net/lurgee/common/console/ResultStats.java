/*
 * @(#)ResultStats.java		2007/11/22
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

/**
 * Collects and provides statistics for a single user for a single game.
 * @author mpatric
 */
public class ResultStats {
	
	private final boolean starter;
	private final boolean winner;
	private final int movesPlayed;

	public ResultStats(boolean starter, boolean winner, int movesPlayed) {
		this.starter = starter;
		this.winner = winner;
		this.movesPlayed = movesPlayed;
	}

	public boolean isStarter() {
		return starter;
	}

	public boolean isWinner() {
		return winner;
	}
	
	public int getMovesPlayed() {
		return movesPlayed;
	}
}
