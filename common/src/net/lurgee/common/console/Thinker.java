/*
 * @(#)Thinker.java		2006/01/28
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import net.lurgee.sgf.AbortException;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.AbstractSearcher;
import net.lurgee.sgf.AbstractSinglePassSearcher;
import net.lurgee.sgf.IterativeSearcher;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveRanker;

/**
 * Wraps a {@link AbstractSinglePassSearcher searcher} to provided threaded game searches.
 * @author mpatric
 */
public class Thinker implements Runnable {

	private final AbstractSearcher searcher;
	private final MoveRanker moveRanker;
	protected Move bestMove = null;
	protected int bestMoveScore = 0;
	protected AbstractBoard board = null;
	protected int depth = 1;
	private long evaluationThreshold = AbstractSearcher.NO_EVALUATION_THRESHOLD;

	public Thinker(AbstractSearcher searcher, MoveRanker moveRanker) {
		this.searcher = searcher;
		this.moveRanker = moveRanker;
	}

	public void setBoard(AbstractBoard board) {
		this.board = board;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public int getDepth() {
		return depth;
	}

	public Move getBestMove() {
		return bestMove;
	}
	
	public int getBestMoveScore() {
		return bestMoveScore;
	}
	
	public void setEvaluationThreshold(long evaluationThreshold) {
		this.evaluationThreshold = evaluationThreshold;
	}
	public void run() {
		
		try {
			if (searcher instanceof IterativeSearcher) {
				bestMove = searcher.findMove(board, moveRanker, depth, evaluationThreshold);
				bestMoveScore = searcher.getBestMoveScore();
			} else {
				bestMove = searcher.findMove(board, moveRanker, depth);
				bestMoveScore = searcher.getBestMoveScore();
			}
		} catch (AbortException ae) {
			bestMove = null;
			bestMoveScore = 0;
		}
	}

	public synchronized void abort() {
		searcher.abortSearch();
	}
}
