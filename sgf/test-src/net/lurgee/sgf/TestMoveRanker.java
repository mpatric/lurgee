/*
 * @(#)TestMoveRanker.java		2007/05/07
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 * 
 */

package net.lurgee.sgf;

import java.util.ArrayList;


/**
 * Move ranker used for unit tests.
 * @author mpatric
 */
public class TestMoveRanker implements MoveRanker {
	
	private final boolean backwards;
	private ArrayList<Integer> ranks = new ArrayList<Integer>();
	private ArrayList<Integer> depths = new ArrayList<Integer>();
	private ArrayList<Character> moves = new ArrayList<Character>();
	private int nodes = 0;
	
	public TestMoveRanker() {
		this(true);
	}
	
	public TestMoveRanker(boolean backwards) {
		this.backwards = backwards;
	}

	public int getRank(Move move, AbstractBoard board, int depth) {
		int multiplier;
		if (backwards) {
			multiplier = 1;
		} else {
			multiplier = -1;
		}
		int rank = 0;
		switch(((TestMove) move).getPosition().getValue()) {
			case 'B':
			case 'D':
			case 'F':
			case 'H':
			case 'K':
			case 'M':
			case 'P':
			case 'R':
			case 'S':
				rank = multiplier * 1;
				break;
			case 'C':
			case 'E':
			case 'G':
			case 'I':
			case 'L':
			case 'N':
			case 'Q':
			case 'T':
				rank = multiplier * 2;
				break;
			case 'J':
			case 'O':
				rank = multiplier * 3;
		}
		moves.add(((TestMove) move).getPosition().getValue());
		ranks.add(rank);
		depths.add(depth);
		return rank;
	}

	public int[] getRanks() {
		return ArrayUtils.integerArrayListToArray(ranks);
	}

	public int[] getDepths() {
		return ArrayUtils.integerArrayListToArray(depths);
	}
	
	public char[] getMoves() {
		return ArrayUtils.characterArrayListToArray(moves);
	}
	
	public void onNodeEvaluation(Move move, AbstractBoard board, int score, Player player, int depth) {
		nodes++;
	}

	public int getNodes() {
		return nodes;
	}

	public void reset() {
	}
}
