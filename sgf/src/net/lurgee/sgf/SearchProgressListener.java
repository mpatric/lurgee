/*
 * @(#)SearchProgressListener.java		2005/11/18
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Interface that a class representing a search progress listener, which is provided with feedback during the search process
 * done by a {@link AbstractSinglePassSearcher searcher}, must implement.
 * @author mpatric
 */
public interface SearchProgressListener {

	/**
	 * Called when a new iteration is being started. Is only called once per search for searchers that extend
	 * {@link AbstractSinglePassSearcher}, but may be called multiple times by searchers that do multiple iterations.
	 */
	void onIterationStart(int iteration);
	
	/**
	 * Called when an iteration ends. Is only called once per search for searchers that extend
	 * {@link AbstractSinglePassSearcher}, but may be called multiple times by searchers that do multiple iterations.
	 * @param iteration The iteration number.
	 * @param depth The maximum search depth reached in this iteration.
	 * @param thresholdReached If true, indicates that the search in this iteration did not complete as it reached its threshold.
	 */
	void onIterationEnd(int iteration, Move move, int score, int depth, boolean thresholdReached);
	
	/**
	 * Called when a new branch is created by a searcher while finding a move.
	 * @param move The move made to get the board into the current state. May be null if this board state followed a bye.
	 * @param board The state of the board for this branch.
	 * @param player The player for whom the new branch is for (NOT the player for whom the best move is being sought).
	 * @param depth The depth into the tree.
	 */
	void onBranch(Move move, AbstractBoard board, Player player, int depth);

	/**
	 * Called when a node is evaluated by a searcher while finding a move.
	 * @param move The move represented by this node, may be null for a bye. The score for node is set on this move (if not null).
	 * @param score The score for the evaluation.
	 * @param player The player for whom the node was evaluated (NOT the player for whom the best move is being sought).
	 * @param depth The depth into the tree.
	 */
	void onNodeEvaluation(Move move, int score, Player player, int depth);

	/**
	 * Called when a leaf is evaluated by a searcher while finding a move.
	 * @param score The score for the evaluation.
	 * @param player The player for whom the leaf was evaluated (NOT the player for whom the best move is being sought).
	 * @param depth The depth into the tree.
	 */
	void onLeafEvaluation(int score, Player player, int depth);
}
