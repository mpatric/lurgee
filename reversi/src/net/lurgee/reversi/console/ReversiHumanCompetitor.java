/*
 * @(#)ReversiHumanCompetitor.java		2008/02/02
 *
 * Part of the reversi console application that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.console;

import net.lurgee.common.console.HumanCompetitor;
import net.lurgee.reversi.ReversiBoard;
import net.lurgee.reversi.ReversiMove;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;

/**
 * Represents a human competitor in the game of reversi. Moves are determined through console input.
 * @author mpatric
 */
public class ReversiHumanCompetitor extends HumanCompetitor {

	public ReversiHumanCompetitor(GameContext gameContext, Player player) {
		super(gameContext, player);
	}
	
	@Override
	public Move determineMove(AbstractBoard board) {
		ReversiMove move = null;
		boolean start = true;
		while (true) {
			String line = input.enterString(start ? "Enter move" : null, null);
			if (line != null) {
				try {
					move = (ReversiMove) gameContext.getMoveFactory().createMoveFromString(line.trim());
					if (((ReversiBoard) board).isValidMove(move.getPosition().getX(), move.getPosition().getY())) {
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
