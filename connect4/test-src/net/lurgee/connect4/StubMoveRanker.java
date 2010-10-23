/*
 * @(#)MoveRankerForSearchTesting.java		2007/03/07
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
 * Move ranker for unit tests.
 * @author mpatric
 */
public class StubMoveRanker implements MoveRanker {

	private static final int MID_BIAS_COLUMN_RANKS[] = {1,4,7,7,7,4,1};
	public static final int RANKER_FORWARD = 1;
	public static final int RANKER_BACKWARD = 2;
	public static final int RANKER_MIDDLE_BIASED = 3;
	
	private final int ranker;

	public StubMoveRanker(int ranker) {
		this.ranker = ranker;
	}
	
	public int getRank(Move move, AbstractBoard board, int depth) {
		if (move instanceof Connect4Move) {
			int x = ((Connect4Move) move).getX();
			if (ranker == RANKER_FORWARD) {
				return x;
			} else if (ranker == RANKER_BACKWARD) {
				return Connect4Board.X_DIMENSION - x + 1;
			} else if (ranker == RANKER_MIDDLE_BIASED) {
				return MID_BIAS_COLUMN_RANKS[x - 1];
			}
		}
		return 0;
	}

	public void onNodeEvaluation(Move move, AbstractBoard board, int score, Player player, int depth) {
	}
	
	public void reset() {
	}
}
