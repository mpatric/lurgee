/*
 * @(#)Settings.java		2007/06/09
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import net.lurgee.sgf.Player;

/**
 * Contains common settings for all games.
 * @author mpatric
 */
public class Settings {

	private final int[] searchLevels;
	private final int[] searchThresholds;
	private int levelIndex;
	private final Player[] players;
	private int playerIndex;
	private boolean soundOn = true;
	
	public Settings(int[] searchLevels, int[] searchThresholds, Player[] players, int playerIndex, int levelIndex) {
		this.searchLevels = searchLevels;
		this.searchThresholds = searchThresholds;
		this.players = players;
		if (playerIndex == 0) {
			setPlayerIndex(1);
		} else {
			setPlayerIndex(playerIndex);
		}
		if (levelIndex == 0) {
			setLevelIndex((searchLevels.length + 1) / 2);
		} else {
			setLevelIndex(levelIndex);
		}
	}

	public void setLevelIndex(int levelIndex) {
		if (levelIndex < 1 || levelIndex > searchLevels.length) {
			throw new IllegalArgumentException("Invalid level");
		}
		this.levelIndex = levelIndex;
	}
	
	public int getLevelIndex() {
		return levelIndex;
	}

	public int getSearchLevel() {
		return searchLevels[levelIndex - 1];
	}
	
	public int getSearchThresholds() {
		if (levelIndex > searchThresholds.length) {
			return 0;
		} else {
			return searchThresholds[levelIndex - 1];
		}
	}

	public void setPlayerIndex(int playerIndex) {
		if (playerIndex < 1 || playerIndex > players.length) {
			throw new IllegalArgumentException("Invalid player");
		}
		this.playerIndex = playerIndex;
	}
	
	public int getPlayerIndex() {
		return playerIndex;
	}

	public Player getPlayer() {
		return players[playerIndex - 1];
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}
}
