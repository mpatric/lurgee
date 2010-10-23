/*
 * @(#)Evaluator.java		2005/11/01
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Interface that a class representing an evaluator in the game must implement.
 * @author mpatric
 */
public interface Evaluator {

	/**
	 * Determine the score of the given board for the current player. The initial board before any moves were made is
	 * also passed in so that deltas can be done (for some games, some pieces may have more significance if they've
	 * just been moved).
	 * @param startBoard The board before any moves were made.
	 * @param board The board to score.
	 * @param currentDepth The depth of the leaf in the tree where this scoring is being done.
	 * @param searchDepth The depth being used for searches - will be the same as currentDepth unless an early evaluation is done for a 'no-bye' situation.
	 * @return A score for the board for the specified player.
	 */
	int score(AbstractBoard startBoard, AbstractBoard board, int currentDepth, int searchDepth);
}
