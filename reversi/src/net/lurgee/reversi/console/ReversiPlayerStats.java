/*
 * @(#)ReversiPlayerStats.java		2007/11/22
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.console;

import net.lurgee.common.console.PlayerStats;
import net.lurgee.common.console.ResultStats;

/**
 * Collects and provides statistics for a single user for reversi games played.
 * @author mpatric
 */
class ReversiPlayerStats extends PlayerStats {

	public ReversiPlayerStats(int maxDepth) {
		super(maxDepth);
	}

	public int[] getScores() {
		int[] scores = new int[results.size()];
		int i = 0;
		for (ResultStats resultStats : results) {
			scores[i++] = ((ReversiResultStats) resultStats).getScore();
		}
		return scores;
	}

	public int countEarlyWins() {
		int earlyWins = 0;
		for (ResultStats resultStats : results) {
			if (resultStats.isWinner() && ((ReversiResultStats) resultStats).isEarly()) {
				earlyWins++;
			}
		}
		return earlyWins;
	}
	
	public float calculateAverageScore() {
		float averageScore = 0;
		for (ResultStats resultStats : results) {
			averageScore += ((ReversiResultStats) resultStats).getScore();
		}
		return averageScore / results.size();
	}
	
	public int countOpenings(int opening) {
		int openings = 0;
		for (ResultStats resultStats : results) {
			if (((ReversiResultStats) resultStats).getOpening() == opening) {
				openings++;
			}
		}
		return openings;
	}
}
