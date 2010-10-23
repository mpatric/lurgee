/*
 * @(#)AbstractCompetitor.java		2005/11/21
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;

/**
 * Abstract base class for a competitor in the game. Wraps a player object to add generic operation for a human
 * competitor or a computer competitor.
 * @author mpatric
 */
public abstract class AbstractCompetitor {
	
	protected final Output output;
	protected final Player player;
	protected final GameContext gameContext;
	protected int movesPlayed = 0;

	public AbstractCompetitor(GameContext gameContext, Player player) {
		output = new Output();
		this.gameContext = gameContext;
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setMovesPlayed(int movesPlayed) {
		this.movesPlayed = movesPlayed;
	}
	
	public int getMovesPlayed() {
		return movesPlayed;
	}

	/**
	 * Abstract method to determine a move for this competitor. Concrete subclasses must define this method.
	 * @param board The current board.
	 */
	public abstract Move determineMove(AbstractBoard board);
}
