/*
 * @(#)ReversiMoveRanker.java		2006/05/30
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.Player;

/**
 * Reversi move ranker, which simply ranks moves based on their board position from a static rank table.
 * @author mpatric
 */
public class ReversiMoveRanker implements MoveRanker {

	private static final short BOARD_RANKS[][] = {
		{50,10,40,30,30,40,10,50},
		{10,00,20,20,20,20,00,10},
		{40,20,20,20,20,20,20,40},
		{30,20,20,00,00,20,20,30},
		{30,20,20,00,00,20,20,30},
		{40,20,20,20,20,20,20,40},
		{10,00,20,20,20,20,00,10},
		{50,10,40,30,30,40,10,50}
	};
	
	public int getRank(Move move, AbstractBoard board, int depth) {
		return BOARD_RANKS[((ReversiMove) move).getPosition().getY() - 1][((ReversiMove) move).getPosition().getX() - 1];
	}

	public void onNodeEvaluation(Move move, AbstractBoard board, int score, Player player, int depth) {
	}
	
	public void reset() {
	}
}
