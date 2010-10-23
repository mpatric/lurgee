/*
 * @(#)AbstractSinglePassSearcher.java		2005/11/01
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Abstract class for generating and searching a game tree with one pass (no iterating) to find the best move.
 * @author mpatric
 */
public abstract class AbstractSinglePassSearcher extends AbstractSearcher {
	
	protected static final String MSG_SEARCHER_ABORTED = "Searcher aborted";
	protected static final String MSG_SEARCHER_THRESHOLD_REACHED = "Searcher evaluation threshold reached at depth ";

	protected final Evaluator evaluator;
	protected final boolean abCutoff;
	protected final boolean byeAllowed;
	protected long evaluationCount = 0;
	protected int depthReached = 0;
	protected int iteration = 0;
	public boolean orderOfMovesIsImportant = true; // TODO - this should be injected.. hard coded for now
	
	protected SearchProgressListener[] searchProgressListeners = null;
	/**
	 * Constructor.
	 * @param gameContext A game context object.
	 * @param library A move library for selecting library moves.
	 * @param evaluator An evaluator, used to evaluate leaf node values.
	 * @param abCutoff Are we using alpha-beta cutoff?
	 * @param byeAllowed Is a bye allowed if a player has no moves?
	 */
	public AbstractSinglePassSearcher(GameContext gameContext, Library library, Evaluator evaluator, boolean abCutoff, boolean byeAllowed) {
		super(gameContext, library);
		this.evaluator = evaluator;
		this.abCutoff = abCutoff;
		this.byeAllowed = byeAllowed;
	}
	
	public void setEvaluationCount(long evaluationCount) {
		this.evaluationCount = evaluationCount;
	}

	public long getEvaluationCount() {
		return evaluationCount;
	}
	
	public int getIteration() {
		return iteration;
	}
	
	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
	
	public void addSearchProgressListener(SearchProgressListener searchProgressListener) {
		if (findSearchProgressListener(searchProgressListener) < 0) {
			if (searchProgressListeners == null) {
				searchProgressListeners = new SearchProgressListener[1];
				searchProgressListeners[0] = searchProgressListener;
			} else {
				SearchProgressListener[] newSearchProgressListeners = new SearchProgressListener[searchProgressListeners.length + 1];
				for (int i = 0; i < searchProgressListeners.length; i++) {
					newSearchProgressListeners[i] = searchProgressListeners[i];
				}
				newSearchProgressListeners[newSearchProgressListeners.length - 1] = searchProgressListener;
				this.searchProgressListeners = newSearchProgressListeners;
			}
		}
	}
	
	public void removeSearchProgressListener(SearchProgressListener searchProgressListener) {
		int index = findSearchProgressListener(searchProgressListener);
		if (index >= 0) {
			if (searchProgressListeners.length == 1) {
				searchProgressListeners = null;
			} else {
				SearchProgressListener[] newSearchProgressListeners = new SearchProgressListener[searchProgressListeners.length - 1];
				int j = 0;
				for (int i = 0; i < searchProgressListeners.length; i++) {
					if (i != index) {
						newSearchProgressListeners[j++] = searchProgressListeners[i];
					}
				}
				this.searchProgressListeners = newSearchProgressListeners;
			}
		}
	}

	private int findSearchProgressListener(SearchProgressListener searchProgressListener) {
		if (searchProgressListeners != null) {
			for (int i = 0; i < searchProgressListeners.length; i++) {
				if (searchProgressListeners[i] == searchProgressListener) {
					return i;
				}
			}
		}
		return -1;
	}
	
	@Override
	public Move findMove(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) throws SearchException, RuntimeException {
		setEvaluationCount(0);
		setIteration(1);
		return super.findMove(board, moveRanker, depth, evaluationThreshold);
	}
	
	@Override
	public void setOrderOfMovesIsImportant(boolean orderOfMovesIsImportant) {
		this.orderOfMovesIsImportant = orderOfMovesIsImportant;		
	}
}
