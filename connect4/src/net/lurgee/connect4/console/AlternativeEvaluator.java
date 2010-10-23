/*
 * @(#)AlternativeEvaluator.java		2007/03/08
 *
 * Part of the connect4 console application that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4.console;

import net.lurgee.connect4.Connect4Board;
import net.lurgee.connect4.Connect4Evaluator;
import net.lurgee.connect4.Connect4Move;
import net.lurgee.connect4.Connect4Player;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Evaluator;

/**
 * Another connect-four evaluator. Used by a {@link net.lurgee.sgf.AbstractSinglePassSearcher searcher} to score board states. This
 * evaluator is based on the standard evaluator {@link Connect4Evaluator}, but is included here to be tweaked for
 * various evaluation strategies to be tried against the standard evaluator (by playing them against each other) in an
 * effort to improve it.
 * @author mpatric
 */
public class AlternativeEvaluator implements Evaluator {
	
	private static final int BASE_GROUP_OF_FOUR_SCORE = 5000;

	/**
	 * Determine the score of the given board for the specified player. Depth is an important consideration as a win after more moves is
	 * NOT preferable to a loss after fewer moves, so the depth is subtracted from the score.
	 * @param startBoard The board before any moves were made.
	 * @param board The board to score.
	 * @param currentDepth The depth of the leaf in the tree where this scoring is being done.
	 * @param searchDepth The depth being used for searches - will be the same as currentDepth unless an early evaluation is done for a 'no-bye' situation.
	 * @return A score for the board for the specified player.
	 */
	public int score(AbstractBoard startBoard, AbstractBoard board, int currentDepth, int searchDepth) {
		int colour = ((Connect4Player) board.getCurrentPlayer()).getColour();
		boolean startingPlayer = isEven(board.countMovesMade());
		return scoreBoard((Connect4Board) board, currentDepth, colour, startingPlayer) - scoreBoard((Connect4Board) board, currentDepth, 3 - colour, !startingPlayer);
	}

	private int scoreBoard(Connect4Board board, int currentDepth, int colour, boolean startingPlayer) {
		int score = 0;
		Connect4Move lastMove = (Connect4Move) board.getLastMovePlayed();
		// groups of four 
		if (board.countUnbrokenGroupsOfFour(colour, 4) > 0) {
			// own score is BASE_GROUP_OF_FOUR_SCORE minus 10 times the number of moves needed for win (so earlier wins are better)
			// minus the height on the board of the winning piece (to encourage blocking)
			score += BASE_GROUP_OF_FOUR_SCORE - (10 * currentDepth);
			if (lastMove != null) {
				score -= ((Connect4Board) board).getTop((lastMove).getX());
			}
		} else if (board.countUnbrokenGroupsOfFour(3 - colour, 4) == 0) {
			score += 2 * board.countUnbrokenGroupsOfFour(colour, 1);
			score += 2 << board.countUnbrokenGroupsOfFour(colour, 2);
			int threes = board.countUnbrokenGroupsOfFour(colour, 3);
			if (threes > 0) {
				score += 4 << threes;
			}
		}
		return score;
	}

	private boolean isEven(int i) {
		return i % 2 == 0;
	}

	@Override
	public String toString() {
		return "Alternative";
	}
}
