/*
 * @(#)MoveRanker.java		2006/05/30
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Interface that a class representing a move ranker in the game must implement. A move ranker is used to order moves
 * in move lists, with moves of a higher rank being explored before those of a lower rank. The better the ranking
 * system, the more efficient alpha-beta cutoffs will be. Setting ranks through this interface facilitates iterative
 * deepening, with ranks being adjusted after each iteration.
 * @author mpatric
 */
public interface MoveRanker {

	/**
	 * Determine a rank for the specified move.
	 * @param move The move to rank.
	 * @param board The current board.
	 * @return A rank for the specified move.
	 */
	int getRank(Move move, AbstractBoard board, int depth);

	/**
	 * Called when a node is evaluated to allow ranker to update ranks. Move rankers that rank moves in a static
	 * way may not need to implement this.
	 * @param move The move that was played which resulted in the provided board state and score.
	 * @param board The board. 
	 * @param score The score for the provided move.
	 * @param player The player that played the move.
	 * @param depth The depth in the search tree at which this move was played.
	 */
	void onNodeEvaluation(Move move, AbstractBoard board, int score, Player player, int depth);
	
	/**
	 * Clear stored ranks.
	 */
	void reset();
}
