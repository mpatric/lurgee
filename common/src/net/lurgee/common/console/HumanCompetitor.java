/*
 * @(#)HumanCompetitor.java		2005/11/21
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
 * Represents a human competitor in the game. Moves are determined through console input.
 * @author mpatric
 */
public abstract class HumanCompetitor extends AbstractCompetitor {
	
	protected final Input input;

	public HumanCompetitor(GameContext gameContext, Player player) {
		super(gameContext, player);
		input = new Input();
	}

	public abstract Move determineMove(AbstractBoard board);
}
