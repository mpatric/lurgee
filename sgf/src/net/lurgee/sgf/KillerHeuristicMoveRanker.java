/*
 * @(#)KillerHeuristicMoveRanker.java		2007/11/07
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of a move ranker that stores a specified number of move ranks based on previous evaluations.
 * These are then used for subsequent rankings. It can encapsulate another move ranker, which is used for ranking
 * if the move being ranked is not one of the 'killer' moves. The encapsulated move ranker should never return
 * ranks higher than {@link #KILLER_MOVE_SCORE_BASE} or this move ranker won't operate as expected.
 * @author mpatric
 */
public class KillerHeuristicMoveRanker implements MoveRanker {

	public static final int KILLER_MOVE_SCORE_BASE = 1000000;
	
	private final MoveRanker moveRanker;
	private final Map<Integer, KillerMoveList> killerMoveMap = new HashMap<Integer, KillerMoveList>();
	private final int killerMovesPerLevel;

	public KillerHeuristicMoveRanker(MoveRanker moveRanker, int killerMovesPerLevel) {
		this.moveRanker = moveRanker;
		this.killerMovesPerLevel = killerMovesPerLevel;
	}

	public int getRank(Move move, AbstractBoard board, int depth) {
		KillerMoveList killerMoveList = killerMoveMap.get(depth);
		if (killerMoveList != null) {
			Integer score = killerMoveList.getScore(move);
			if (score != null) {
				return KILLER_MOVE_SCORE_BASE + score;
			}
		}
		return moveRanker.getRank(move, board, depth);
	}
	
	public void onNodeEvaluation(Move move, AbstractBoard board, int score, Player player, int depth) {
		addIfKillerMove(move, score, depth);
		moveRanker.onNodeEvaluation(move, board, score, player, depth);
	}
	
	public void reset() {
		killerMoveMap.clear();
	}
	
	protected int count(int depth) {
		KillerMoveList killerMoveList = killerMoveMap.get(depth);
		if (killerMoveList != null) {
			return killerMoveList.count;
		} else {
			return 0;
		}
	}

	protected void addIfKillerMove(Move move, int score, int depth) {
		KillerMoveList killerMoveList = killerMoveMap.get(depth);
		if (killerMoveList == null) {
			killerMoveList = new KillerMoveList(killerMovesPerLevel);
			killerMoveMap.put(depth, killerMoveList);
		}
		killerMoveList.addIfKillerMove(move, score);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true; 
		for (int depth : killerMoveMap.keySet()) {
			KillerMoveList killerMoveList = killerMoveMap.get(depth);
			if (killerMoveList != null) {
				if (first) {
					first = false;
				} else {
					sb.append('\n');
				}
				sb.append(depth).append(": ").append(killerMoveList.toString());
			} else {
				break;
			}
		}
		return sb.toString();
	}
	
	private class KillerMoveList {
		private int count; 
		private Move[] killerMoves;
		private int[] scores;
		
		private KillerMoveList(int maxMoves) {
			killerMoves = new Move[maxMoves];
			scores = new int[maxMoves];
			for (int i = 0; i < maxMoves; i++) {
				killerMoves[i] = null;
			}
			count = 0;
		}
		
		private Integer getScore(Move move) {
			for (int i = 0; i < killerMoves.length; i++) {
				if (killerMoves[i] == null) {
					return null;
				} else if (killerMoves[i].equals(move)) {
					return scores[i];
				}
			}
			return null;
		}
		
		private boolean addIfKillerMove(Move move, int score) {
			boolean added = false;
			if (score > getWorstScore()) {
				for (int i = 0; i < killerMoves.length; i++) {
					if (killerMoves[i] == null) {
						break;
					} else if (killerMoves[i].equals(move)) {
						if (score <= scores[i]) {
							return false;
						} else {
							for (int j = i; j < killerMoves.length - 1; j++) {
								killerMoves[j] = killerMoves[j + 1];
								scores[j] = scores[j + 1];
							}
							killerMoves[killerMoves.length - 1] = null;
							scores[killerMoves.length - 1] = Integer.MIN_VALUE;
							count--;
							break;
						}
					}
				}
				for (int i = 0; i < killerMoves.length; i++) {
					if (killerMoves[i] == null) {
						killerMoves[i] = move;
						scores[i] = score;
						count++;
						added = true;
						break;
					} else if (score >= scores[i]) {
						for (int j = killerMoves.length - 1; j > i; j--) {
							killerMoves[j] = killerMoves[j - 1];
							scores[j] = scores[j - 1];
						}
						killerMoves[i] = move;
						scores[i] = score;
						if (count < killerMovesPerLevel) {
							count++;
						}
						added = true;
						break;
					}
				}
			}
			return added;
		}

		private int getWorstScore() {
			if (killerMoves[killerMoves.length - 1] == null) {
				return Integer.MIN_VALUE;
			} else {
				return scores[scores.length - 1];
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < killerMoves.length; i++) {
				if (killerMoves[i] != null) {
					sb.append(killerMoves[i].toString()).append('(').append(scores[i]).append(") ");
				}
			}
			return sb.toString();
		}
	}
}
