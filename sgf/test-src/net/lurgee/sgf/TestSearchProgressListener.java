/*
 * @(#)TestGameListener.java		2005/11/18
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.ArrayList;
import java.util.List;

/**
 * Search progress listener used for unit tests.
 * @author mpatric
 */
public class TestSearchProgressListener implements SearchProgressListener {

	private final List<Character> branches = new ArrayList<Character>();
	private final List<Integer> nodeScores = new ArrayList<Integer>();
	private final List<Integer> leafScores = new ArrayList<Integer>();
	private final List<Integer> leafDepths = new ArrayList<Integer>();
	private final List<Integer> iterations = new ArrayList<Integer>();
	private int lastIteration = 0;
	private int lastIterationEndDepth = 0;	
	private int lastCompleteIteration = 0;
	private int lastCompleteIterationEndDepth = 0;
	
	public char[] getBranches() {
		return ArrayUtils.characterArrayListToArray(branches);
	}

	public int[] getNodeScores() {
		return ArrayUtils.integerArrayListToArray(nodeScores);
	}

	public int[] getLeafScores() {
		return ArrayUtils.integerArrayListToArray(leafScores);
	}
	
	public int[] getLeafDepths() {
		return ArrayUtils.integerArrayListToArray(leafDepths);
	}
	
	public int[] getIterations() {
		return ArrayUtils.integerArrayListToArray(iterations);
	}
	
	public int getLastIteration() {
		return lastIteration;
	}

	public int getLastIterationEndDepth() {
		return lastIterationEndDepth;
	}
	
	public int getLastCompleteIteration() {
		return lastCompleteIteration;
	}
	
	public int getLastCompleteIterationEndDepth() {
		return lastCompleteIterationEndDepth;
	}
	
	public void onIterationStart(int iteration) {
		iterations.add(iteration);
		Debug.output(0, "ITERATION " + iteration + " START");
	}
	
	public void onIterationEnd(int iteration, Move move, int score, int depth, boolean thresholdReached) {
		lastIteration = iteration;
		lastIterationEndDepth = depth;
		if (thresholdReached) {
			Debug.output(0, "ITERATION " + iteration + " THRESHOLD REACHED");
		} else {
			lastCompleteIteration = iteration;
			lastCompleteIterationEndDepth = depth;
			Debug.output(0, "ITERATION " + iteration + " END move = " + move + " score = " + score);
		}
	}

	public void onBranch(Move move, AbstractBoard board, Player player, int depth) {
		branches.add(((TestBoard) board).getState());		
		Debug.output(depth + 1, "BRANCH move = " + move + " player = " + player + " depth = " + depth);
	}

	public void onNodeEvaluation(Move move, int score, Player player, int depth) {
		nodeScores.add(score);
		Debug.output(depth + 1, "NODE move = " + move + " score = " + score + " player = " + player + " depth = " + depth);
	}

	public void onLeafEvaluation(int score, Player player, int depth) {
		leafScores.add(score);
		leafDepths.add(depth);
		Debug.output(depth + 2, "LEAF score = " + score + " player = " + player + " depth = " + depth);
	}
}
