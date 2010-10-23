/*
 * @(#)NegascoutSearcher.java		2006/05/26
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Class for generating and searching a game tree to find the best move using a negascout algorithm.
 * <p/>
 * Note: {@link AbstractSinglePassSearcher#abCutoff abCutoff} is ignored and assumed to always be <code>true</code> for this
 * class for a slight performance improvement (so a check does not need to be done on every loop in
 * {@link #search(AbstractBoard, AbstractBoard, MoveRanker, int, int, int, int, long) search}.  
 * @author mpatric
 */
public class NegascoutSearcher extends NegamaxSearcher {

	/**
	 * Constructor.
	 * @param gameContext A game context object.
	 * @param library A move library for selecting library moves.
	 * @param evaluator An evaluator, used to evaluate leaf node values.
	 * @param byeAllowed Should the search continue when there are board positions where a player cannot play?
	 */
	public NegascoutSearcher(GameContext gameContext, Library library, Evaluator evaluator, boolean byeAllowed) {
		super(gameContext, library, evaluator, true, byeAllowed);
	}
	
	/**
	 * Calls {@link #searchChildNode(AbstractBoard, AbstractBoard, MoveRanker, int, int, int, int, int, boolean, long) search}
	 * for a child node, re-searching if necessary.
	 * @param startBoard The board before any moves were made.
	 * @param board The current board.
	 * @param depth The maximum depth that this search is being done to.
	 * @param d Maximum depth minus the current depth (so when d is 0, maximum depth is reached).
	 * @param alpha Alpha value for alpha-beta cutoff.
	 * @param beta Beta value for alpha-beta cutoff.
	 * @param b null window value.
	 * @param firstChild Flag indicating whether the search is currently on the first child of a node.
	 * @param evaluationThreshold The number of evaluations at which the search is ended.
	 * @return The score for the child node.
	 */
	@Override
	protected int searchChildNode(AbstractBoard startBoard, AbstractBoard board, MoveRanker moveRanker, int depth, int d, int alpha, int beta, int b, boolean firstChild, long evaluationThreshold) {
		int score = -(search(startBoard, board, moveRanker, depth, d - 1, -b, -alpha, evaluationThreshold));
		if (d > 1 && alpha < score && score < beta && ! firstChild) {
			// re-search
			score = -(search(startBoard, board, moveRanker, depth, d - 1, -beta, -score, evaluationThreshold));
		}
		return score;
	}
	
	@Override
	public String toString() {
		return "Negascout";
	}
}
