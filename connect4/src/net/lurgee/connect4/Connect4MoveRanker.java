/*
 * @(#)Connect4MoveRanker.java		2007/04/07
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.Player;

/**
 * Connect-four move ranker. Ranks columns with an opponent active threat the lowest, then columns with
 * a player's own active threat, then other columns. Also, it adds an amount inversely proportional to the
 * height of the column as lower moves tend to be better than higher moves, perhaps - anyway it produces
 * a better piece distribution.
 * @author mpatric
 */
public class Connect4MoveRanker implements MoveRanker {
	
	private static final short BOARD_RANKS[][] = {
		{3, 4, 5, 7, 5, 4, 3},
		{4, 6, 8, 10, 8, 6, 4},
		{5, 8, 11, 13, 11, 8, 5},
		{5, 8, 11, 13, 11, 8, 5},
		{4, 6, 8, 10, 8, 6, 4},
		{3, 4, 5, 7, 5, 4, 3}
	};
	
	public int getRank(Move move, AbstractBoard board, int depth) {
		int x = ((Connect4Move) move).getX();
		int y = ((Connect4Board) board).getTop(x);
		if (y == Connect4Board.Y_DIMENSION) {
			return 0;
		} else {
			return BOARD_RANKS[y][x - 1];
		}
	}
	
	public void onNodeEvaluation(Move move, AbstractBoard board, int score, Player player, int depth) {
	}

	public void reset() {
	}
}
