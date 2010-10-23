/*
 * @(#)IterativeSearcher.java		2007/10/17
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;


/**
 * Class for generating and searching a game tree to find the best move with iterative deepening using an underlying
 * single-pass searcher (such as {@link NegamaxSearcher} or {@link NegascoutSearcher}).
 * @author mpatric
 */
public class IterativeSearcher extends AbstractSearcher {

	private final AbstractSinglePassSearcher searcher;
	private int currentDepth;

	public IterativeSearcher(AbstractSinglePassSearcher searcher, Library library) {
		super(searcher.gameContext, library);
		this.searcher = searcher;
	}
	
	@Override
	public Move findMove(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) throws SearchException, RuntimeException {
		searcher.setEvaluationCount(0);
		searcher.setIteration(1);
		return super.findMove(board, moveRanker, depth, evaluationThreshold);
	}

	@Override
	protected Move findMoveBySearch(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) throws SearchException, RuntimeException {
		try {
			for (currentDepth = 1; currentDepth <= depth; currentDepth++) {
				if (currentDepth == 1) {
					// no evaluation threshold on first search to ensure bestMove always gets set
					bestMove = searcher.findMove(board, moveRanker, currentDepth);
				} else {
					searcher.setIteration(searcher.getIteration() + 1);
					bestMove = searcher.findMoveWithoutResetting(board, moveRanker, currentDepth, evaluationThreshold);
				}
				bestMoveScore = searcher.getBestMoveScore();
			}
		} catch (SearchThresholdReachedException e) {
			// search threshold reached, so use the best move so far up to this point!
		}
		return bestMove;
	}
	
	@Override
	public void abortSearch() {
		super.abortSearch();
		searcher.abortSearch();
	}

	@Override
	public void addSearchProgressListener(SearchProgressListener searchProgressListener) {
		searcher.addSearchProgressListener(searchProgressListener);
	}

	@Override
	public void removeSearchProgressListener(SearchProgressListener searchProgressListener) {
		searcher.removeSearchProgressListener(searchProgressListener);
	}

	@Override
	public String toString() {
		return "Iterative " + searcher.toString();
	}

	@Override
	public void setOrderOfMovesIsImportant(boolean orderOfMovesIsImportant) {
		searcher.setOrderOfMovesIsImportant(orderOfMovesIsImportant);		
	}
}
