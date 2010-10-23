/*
 * @(#)NineMensMorrisMoveRanker.java		2007/01/26
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.Player;

/**
 * Nine men's morris move ranker.
 * Rank is based on mobility of the destination position, plus some extra if the destination is a opponent hole,
 * and even more if it's a hole for the player. 
 * @author mpatric
 */
public class NineMensMorrisMoveRanker implements MoveRanker {
	
	private static final int HOLE_RANK = 10;
	private static final int OPPONENT_HOLE_RANK = 5;

	public int getRank(Move move, AbstractBoard board, int depth) {
		final NineMensMorrisMove nineMensMorrisMove = (NineMensMorrisMove) move;
		int rank = 0;
		int colour = ((NineMensMorrisPlayer) board.getCurrentPlayer()).getColour();
		if (nineMensMorrisMove.isPlacement()) {
			int position = nineMensMorrisMove.getPosition();
			rank += ((NineMensMorrisBoard) board).getDegreesOfFreedomForPosition(position);
			if (((NineMensMorrisBoard) board).isHole(colour, nineMensMorrisMove.getPosition())) {
				rank += HOLE_RANK;
			} else if (((NineMensMorrisBoard) board).isHole(3 - colour, nineMensMorrisMove.getPosition())) {
				rank += OPPONENT_HOLE_RANK;
			}
		} else {
			int position = nineMensMorrisMove.getToPosition();
			rank += ((NineMensMorrisBoard) board).getDegreesOfFreedomForPosition(position);
			if (((NineMensMorrisBoard) board).isHole(colour, nineMensMorrisMove.getPosition())) {
				rank += HOLE_RANK;
			} else if (((NineMensMorrisBoard) board).isHole(3 - colour, nineMensMorrisMove.getPosition())) {
				rank += OPPONENT_HOLE_RANK;
			}
		}
		return rank;
	}

	public void onNodeEvaluation(Move move, AbstractBoard board, int score, Player player, int depth) {
	}
	
	public void reset() {
	}
}
