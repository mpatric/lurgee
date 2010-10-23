/*
 * @(#)GameStats.java		2007/11/22
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import java.util.HashMap;

import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Collects and provides statistics for games played.
 * @author mpatric
 */
public class GameStats {

	private static final String NA = "n/a";
	
	protected HashMap<Player, PlayerStats> playerStatsMap = new HashMap<Player, PlayerStats>();
	
	public SearchProgressListener registerPlayer(Player player, int maxDepth) {
		PlayerStats playerStats = createPlayerStats(maxDepth);
		playerStatsMap.put(player, playerStats);
		return playerStats;
	}
	
	protected PlayerStats createPlayerStats(int maxDepth) {
		return new PlayerStats(maxDepth);
	}
	
	/**
	 * Add stats for a player for a single game.
	 * @param player The player for whom the stats apply.
	 * @param starter Flag indicating whether this player started the game.
	 * @param winner Flag indicating whether this player won the game.
	 */
	public void addStat(Player player, boolean starter, boolean winner, int movesPlayed) {
		playerStatsMap.get(player).addResult(new ResultStats(starter, winner, movesPlayed));
	}
	
	public int getGameCount() {
		for (PlayerStats playerStats : playerStatsMap.values()) {
			return playerStats.getGameCount();
		}
		return 0;
	}
	
	public String getMovesConsidered(Player player) {
		int movesConsidered = playerStatsMap.get(player).getMovesConsidered();
		if (movesConsidered > 0) {
			return Integer.toString(movesConsidered);
		} else {
			return NA;
		}
	}
	
	public String getEvaluationsDone(Player player) {
		int evaluationsDone = playerStatsMap.get(player).getEvaluationsDone();
		if (evaluationsDone > 0) {
			return Integer.toString(evaluationsDone);
		} else {
			return NA;
		}
	}
	
	public String getMovesConsideredInIncompleteIterations(Player player) {
		int movesConsidered = playerStatsMap.get(player).getMovesConsideredInIncompleteIterations();
		return Integer.toString(movesConsidered);
	}
	
	public String getEvaluationsDoneInIncompleteIterations(Player player) {
		int evaluationsDone = playerStatsMap.get(player).getEvaluationsDoneInIncompleteIterations();
		return Integer.toString(evaluationsDone);
	}
	
	public String getSearchDepths(Player player) {
		return playerStatsMap.get(player).getCompleteSearchDepths();
	}
	
	public int countWins(Player player) {
		return playerStatsMap.get(player).countWins();
	}
	
	public int countStartWins(Player player) {
		return playerStatsMap.get(player).countStartWins();
	}

	public int countMovesPlayed(Player player) {
		return playerStatsMap.get(player).countMovesPlayed();
	}
}
