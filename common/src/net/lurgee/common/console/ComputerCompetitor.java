/*
 * @(#)ComputerCompetitor.java		2005/11/21
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.AbstractSearcher;
import net.lurgee.sgf.AbstractSinglePassSearcher;
import net.lurgee.sgf.Evaluator;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.IterativeSearcher;
import net.lurgee.sgf.KillerHeuristicMoveRanker;
import net.lurgee.sgf.Library;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.NegamaxSearcher;
import net.lurgee.sgf.NegascoutSearcher;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Represents a computer competitor in the game. Moves are determined with a {@link Thinker}. 
 * @author mpatric
 */
public class ComputerCompetitor extends AbstractCompetitor {
	
	private Thinker thinker = null;
	private Evaluator evaluator = null;
	private AbstractSearcher searcher = null;
	private Library library;
	private int lastMoveScore = 0;
	
	public ComputerCompetitor(GameContext gameContext, MoveRanker moveRanker, Evaluator evaluator, Library library, Player player, SearchProgressListener searchProgressListener, boolean useNegamax, boolean useKillerHeuristic, boolean useIterativeDeepening, boolean byeAllowed) {
		super(gameContext, player);
		this.evaluator = evaluator;
		this.library = library;
		AbstractSinglePassSearcher singlePassSearcher;
		if (useNegamax) {
			singlePassSearcher = new NegamaxSearcher(gameContext, library, evaluator, true, byeAllowed);
		} else {
			singlePassSearcher = new NegascoutSearcher(gameContext, library, evaluator, byeAllowed);  
		}
		singlePassSearcher.addSearchProgressListener(searchProgressListener);
		MoveRanker moveRankerToUse;
		if (useKillerHeuristic) {
			moveRankerToUse = new KillerHeuristicMoveRanker(moveRanker, 5);
		} else {
			moveRankerToUse = moveRanker;
		}
		if (useIterativeDeepening) {
			searcher = new IterativeSearcher(singlePassSearcher, null);
		} else {
			searcher = singlePassSearcher;
		}
		thinker = new Thinker(searcher, moveRankerToUse);
	}
	
	public Thinker getThinker() {
		return thinker;
	}
	
	public Evaluator getEvaluator() {
		return evaluator;
	}
	
	public AbstractSearcher getSearcher() {
		return searcher;
	}
	
	public Library getLibrary() {
		return library;		
	}

	public void setTreeDepth(int treeDepth) {
		thinker.setDepth(treeDepth);
	}
	
	public void setEvaluationThreshold(long evaluationThreshold) {
		thinker.setEvaluationThreshold(evaluationThreshold);
	}
	
	public int getLastMoveScore() {
		return lastMoveScore;
	}

	public Move determineMove(AbstractBoard board) {
		while (true) {
			Thread thinkerThread = new Thread(thinker);
			thinker.setBoard(board);
			thinkerThread.start();
			do {
				try {
					thinkerThread.join(50);
				} catch (InterruptedException ie) {
					// do nothing
				}
				//output.print(".");
			} while (thinkerThread.isAlive());
			//output.println();
			lastMoveScore = thinker.getBestMoveScore();
			if (thinker.getBestMove() != null) {
				return thinker.getBestMove();
			} else {
				throw(new NullPointerException("Move is null! This should never happen"));
			}
		}
	}
}
