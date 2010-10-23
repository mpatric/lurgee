/*
 * @(#)AbstractSearcher.java		2007/10/17
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Abstract class for generating and searching a game tree to find the best move.
 * @author mpatric
 */
public abstract class AbstractSearcher {
	
	public static final int NO_EVALUATION_THRESHOLD = 0;
	
	protected final GameContext gameContext;
	protected final Library library;

	protected boolean aborted = false;
	protected Move bestMove = null;
	protected int bestMoveScore = 0;
	public AbstractSearcher(GameContext gameContext, Library library) {
		this.gameContext = gameContext;
		this.library = library;
	}
	
	/**
	 * The same as {@link #findMove(AbstractBoard, MoveRanker, int, long) findMove} but with no evaluation threshold.
	 */
	public Move findMove(AbstractBoard board, MoveRanker moveRanker, int depth) throws SearchException, RuntimeException {
		return findMove(board, moveRanker, depth, NO_EVALUATION_THRESHOLD);
	}

	/**
	 * Find the next move for the current player, either using {@link #library library} (if it is set), or by a
	 * search by calling {@link #findMoveBySearch(AbstractBoard, MoveRanker, int, long) findMoveBySearch}.
	 * @param board The board used for the operations.
	 * @param moveRanker A move ranker for ranking moves for ordering.
	 * @param depth The current search depth.
	 * @param evaluationThreshold The number of evaluations at which the search is ended.
	 * @return The best move, as determined by the search.
	 * @throws AbortException The search is aborted prematurely.
	 * @throws SearchThresholdReachedException The search was ended as the evaluation threshold was reached.
	 * @throws RuntimeException Fatal error.
	 */
	public Move findMove(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) throws SearchException, RuntimeException {
		if (moveRanker != null) {
			moveRanker.reset();
		}
		return findMoveWithoutResetting(board, moveRanker, depth, evaluationThreshold);
	}

	/**
	 * The same as {@link #findMoveWithoutResetting(AbstractBoard, MoveRanker, int, long) findMoveWithoutResetting}
	 * but with no evaluation threshold. 
	 */
	protected Move findMoveWithoutResetting(AbstractBoard board, MoveRanker moveRanker, int depth) {
		return findMoveWithoutResetting(board, moveRanker, depth, NO_EVALUATION_THRESHOLD);
	}
	
	/**
	 * The same as {@link #findMove(AbstractBoard, MoveRanker, int, long) findMove} but does not reset the state of the
	 * moveRanker or the evaluation count. Needed to be separate from {@link #findMove(AbstractBoard, MoveRanker, int, long) findMove}
	 * primarily for iterative deepening.
	 */
	protected Move findMoveWithoutResetting(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) {
		if (depth < 1) {
			throw new IllegalArgumentException("Invalid depth");
		}
		aborted = false;
		bestMove = null;
		bestMoveScore = 0;
		if (library != null && library.shouldUseLibrary(board, depth)) {
			return library.findMove(board, depth);
		}
		Move move = findMoveBySearch(board, moveRanker, depth, evaluationThreshold);
		return move;
	}
	
	public void abortSearch() {
		aborted = true;
	}

	public boolean isAborted() {
		return aborted;
	}
	
	public int getBestMoveScore() {
		return bestMoveScore;
	}
	
	/**
	 * Abstract method to generate a game tree and search for the best node, which is returned as a {@link Move}.
	 * Concrete subclasses must define this method.
	 * @param board The board used for the operations.
	 * @param moveRanker A move ranker for ranking moves for ordering.
	 * @param depth The current search depth.
	 * @param evaluationThreshold The number of evaluations at which the search is ended.
	 * @return move The best move, as determined by the search.
	 * @throws AbortException The search is aborted prematurely.
	 * @throws SearchThresholdReachedException The search was ended as the evaluation threshold was reached.
	 * @throws RuntimeException Fatal error.
	 */
	protected abstract Move findMoveBySearch(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) throws SearchException, RuntimeException;
	
	/**
	 * Add a {@link SearchProgressListener} to call back to during the search process.
	 * @param searchProgressListener The listener to add.
	 */
	public abstract void addSearchProgressListener(SearchProgressListener searchProgressListener);
	
	/**
	 * Remove a {@link SearchProgressListener} previously added with {@link #addSearchProgressListener(SearchProgressListener) addSearchProgressListener}.
	 * @param searchProgressListener The listener to remove.
	 */
	public abstract void removeSearchProgressListener(SearchProgressListener searchProgressListener);

	public abstract void setOrderOfMovesIsImportant(boolean orderOfMovesIsImportant);
}
