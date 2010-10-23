/*
 * @(#)ReversiResultStats.java		2007/11/22
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.console;

import net.lurgee.common.console.ResultStats;

/**
 * Collects and provides statistics for a single user for a single reversi game.
 * @author mpatric
 */
class ReversiResultStats extends ResultStats {
	
	private int score;
	private boolean early;
	private int opening;

	public ReversiResultStats(boolean starter, boolean winner, int movesPlayed, int score, boolean early, int opening) {
		super(starter, winner, movesPlayed);
		this.score = score;
		this.early = early;
		this.opening = opening;
	}

	public int getScore() {
		return score;
	}

	public boolean isEarly() {
		return early;
	}

	public int getOpening() {
		return opening;
	}
}
