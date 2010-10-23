/*
 * @(#)ReversiGameStats.java		2007/02/24
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.console;

import net.lurgee.common.console.GameStats;
import net.lurgee.common.console.PlayerStats;
import net.lurgee.reversi.ReversiPlayer;

/**
 * Collects and provides statistics regarding reversi games played.
 * @author mpatric
 */
public class ReversiGameStats extends GameStats {

	protected PlayerStats createPlayerStats(int maxDepth) {
		return new ReversiPlayerStats(maxDepth);
	}
	
	/**
	 * Add stats for a player for a single game.
	 * @param player The player for whom the stats apply.
	 * @param starter Flag indicating whether this player started the game.
	 * @param winner Flag indicating whether this player won the game.
	 * @param movesPlayed The number of moves played by this player.
	 * @param score The score achieved in the game.
	 * @param early Flag indicating whether this player won the game early.
	 * @param opening The library opening played. 
	 */
	public void addStat(ReversiPlayer player, boolean starter, boolean winner, int movesPlayed, int score, boolean early, int opening) {
		playerStatsMap.get(player).addResult(new ReversiResultStats(starter, winner, movesPlayed, score, early, opening));
	}
	
	public int countEarlyWins(ReversiPlayer player) {
		return ((ReversiPlayerStats) playerStatsMap.get(player)).countEarlyWins();
	}
	
	public float calculateAverageScore(ReversiPlayer player) {
		return ((ReversiPlayerStats) playerStatsMap.get(player)).calculateAverageScore();
	}
	
	public int countOpenings(ReversiPlayer player, int opening) {
		return ((ReversiPlayerStats) playerStatsMap.get(player)).countOpenings(opening);
	}
	
	public String listScores(ReversiPlayer player1, ReversiPlayer player2) {
		ReversiPlayerStats playerStats1 = (ReversiPlayerStats) playerStatsMap.get(player1);
		ReversiPlayerStats playerStats2 = (ReversiPlayerStats) playerStatsMap.get(player2);
		int[] scores1 = playerStats1.getScores();
		int[] scores2 = playerStats2.getScores();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < scores1.length && i < scores2.length; i++) {
			sb.append(scores1[i]).append("-").append(scores2[i]).append(" ");
		}
		return sb.toString();
	}
}
