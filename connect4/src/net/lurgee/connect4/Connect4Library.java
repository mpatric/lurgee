/*
 * @(#)Connect4Library.java		2007/06/03
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import java.util.Random;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Library;
import net.lurgee.sgf.Move;

/**
 * Connect-four opening move library. Used by a {@link net.lurgee.sgf.AbstractSinglePassSearcher searcher} to select the 1st move of
 * the game for each player only. If playing first, the middle column is always selected. If playing second, one of the
 * four outer columns is selected.
 * @author mpatric
 */
public class Connect4Library implements Library {

	private static final Random random = new Random();
	
	private final GameContext gameContext;

	public Connect4Library(GameContext gameContext) {
		this.gameContext = gameContext;
	}
	
	public boolean shouldUseLibrary(AbstractBoard board, int depth) {
		if (board.countMovesMade() < 2) return true;
		return false;
	}

	public Move findMove(AbstractBoard board, int depth) {
		Connect4Move connect4Move = null;
		Connect4MoveFactory moveFactory = (Connect4MoveFactory) gameContext.getMoveFactory();
		if (board.countMovesMade() == 0) {
			connect4Move = moveFactory.createMove(4);
		} else {
			while (connect4Move == null) {
				int r = (random.nextInt()) % 5; // -2 to 2 (5 options)
				if (r != 0) {
					if (r == -2 && ((Connect4Board) board).getTop(1) == 0) {
						connect4Move = moveFactory.createMove(1);
					} else if (r == -1 && ((Connect4Board) board).getTop(2) == 0) {
						connect4Move = moveFactory.createMove(2);
					} else if (r == 1 && ((Connect4Board) board).getTop(6) == 0) {
						connect4Move = moveFactory.createMove(6);
					} else if (r == 2 && ((Connect4Board) board).getTop(7) == 0) {
						connect4Move = moveFactory.createMove(7);
					}
				}
			}
		}
		return connect4Move;
	}
}
