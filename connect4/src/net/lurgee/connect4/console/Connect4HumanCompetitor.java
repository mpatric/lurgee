/*
 * @(#)Connect4HumanCompetitor.java		2008/02/02
 *
 * Part of the connect4 console application that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4.console;

import net.lurgee.common.console.HumanCompetitor;
import net.lurgee.connect4.Connect4Board;
import net.lurgee.connect4.Connect4Move;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;

/**
 * Represents a human competitor in the game of connect-four. Moves are determined through console input.
 * @author mpatric
 */
public class Connect4HumanCompetitor extends HumanCompetitor {

	public Connect4HumanCompetitor(GameContext gameContext, Player player) {
		super(gameContext, player);
	}

	@Override
	public Move determineMove(AbstractBoard board) {
		Connect4Move move = null;
		boolean start = true;
		while (true) {
			String line = input.enterString(start ? "Enter move" : null, null);
			if (line != null) {
				try {
					move = (Connect4Move) gameContext.getMoveFactory().createMoveFromString(line.trim());
					if (((Connect4Board) board).isValidMove(move.getX())) {
						return move;
					}
				} catch (IllegalArgumentException e) {
					// invalid move
				}
			}
			output.println("Invalid move");
			start = false;
		}
	}
}
