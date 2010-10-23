/*
 * @(#)MoveList.java		2005/11/01
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Stores a list of moves, sorted in descending order by rank if provided, or by the rank determined with a
 * {@link MoveRanker} if provided.
 * @author mpatric
 */
public class MoveList {

	private static final Random random = new Random();

	protected final ArrayList<Move> moves;
	protected final ArrayList<Integer> ranks;

	private final boolean randomOrderForEqualScores;
	
	public MoveList(int initialCapacity, boolean randomOrderForEqualScores) {
		moves = new ArrayList<Move>(initialCapacity);
		ranks = new ArrayList<Integer>(initialCapacity);
		this.randomOrderForEqualScores = randomOrderForEqualScores;
	}

	public List<Move> getMoves() {
		return moves;
	}

	protected Move getMove(int index) {
		return moves.get(index);
	}
	
	public int size() {
		return moves.size();
	}
	
	public void add(Move move, AbstractBoard board, int depth, MoveRanker moveRanker) {
		if (moveRanker != null) {
			add(move, moveRanker.getRank(move, board, depth));
		} else {
			add(move);
		}
	}

	protected void add(Move move) {
		moves.add(move);
		ranks.add(0);
	}
	
	protected void add(Move move, int rank) {
		if (! randomOrderForEqualScores) {
			int i = 0;
			for (Integer storedRank : ranks) {
				if (rank >= storedRank) {
					// add it at this point
					moves.add(i, move);
					ranks.add(i, rank);
					return;
				}
				i++;
			}
		} else {
			int equalRanks = 0;
			int i = 0;
			for (Integer storedRank : ranks) {
				if (rank > storedRank) {
					if (equalRanks == 0) {
						// add it at this point
						moves.add(i, move);
						ranks.add(i, rank);
					} else {
						// add it at a random point amongst the equal ranked moves
						int position = i - equalRanks + selectRandom(equalRanks + 1);
						moves.add(position, move);
						ranks.add(position, rank);
					}
					return;
				} else if (rank == storedRank) {
					equalRanks++;
					if (i == ranks.size() - 1) {
						int position = i - equalRanks + selectRandom(equalRanks + 1) + 1;
						moves.add(position, move);
						ranks.add(position, rank);
						return;
					}
				}
				i++;
			}
		}
		// add it to the end
		moves.add(move);
		ranks.add(rank);
	}
	
	public void clear() {
		moves.clear();
	}
	
	protected int selectRandom(int numberOfChoices) {
		return random.nextInt(numberOfChoices);
	}
}
