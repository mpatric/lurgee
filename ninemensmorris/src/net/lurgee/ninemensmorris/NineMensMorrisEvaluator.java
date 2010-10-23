/*
 * @(#)NineMensMorrisEvaluator.java		2008/02/02
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Evaluator;

/**
 * Nine men's morris evaluator. Used by a {@link net.lurgee.sgf.AbstractSinglePassSearcher searcher} to score board states.
 * @author mpatric
 */
public class NineMensMorrisEvaluator implements Evaluator {
	
	private static final int WINNING_SCORE = 100000;
	private static final int DRAW_SCORE = 50000;

	/**
	 * Determine the score of the given board for the specified player. Depth is an important consideration as a win after more moves is
	 * NOT preferable to a loss after fewer moves, so the depth is subtracted from the score if it's a win or loss situation.
	 * @param startBoard The board before any moves were made.
	 * @param board The board to score.
	 * @param currentDepth The depth of the leaf in the tree where this scoring is being done.
	 * @param searchDepth The depth being used for searches - will be the same as currentDepth unless an early evaluation is done for a 'no-bye' situation.
	 * @return A score for the board for the specified player.
	 */
	public int score(AbstractBoard startBoard, AbstractBoard board, int currentDepth, int searchDepth) {
		final NineMensMorrisBoard nineMensMorrisBoard = (NineMensMorrisBoard) board;
		int colour = ((NineMensMorrisPlayer) nineMensMorrisBoard.getCurrentPlayer()).getColour();
		int score = 0;
		if (nineMensMorrisBoard.isGameOver()) {
			// --- Game won, lost or drawn ---
			NineMensMorrisPlayer winner = ((NineMensMorrisPlayer) nineMensMorrisBoard.getWinner());
			if (winner == null) {
				if (currentDepth % 2 == 0) {
					score = -DRAW_SCORE + nineMensMorrisBoard.countMovesMade();
				} else {
					score = DRAW_SCORE - nineMensMorrisBoard.countMovesMade();
				}
			} else if (winner.getColour() == colour) {
				score = WINNING_SCORE - nineMensMorrisBoard.countMovesMade();
			} else {
				score = -WINNING_SCORE + nineMensMorrisBoard.countMovesMade();
			}
		} else {
			// --- Piece differential ---
			int myPieceCount = nineMensMorrisBoard.countPieces(colour) + nineMensMorrisBoard.countPiecesInHand(colour);
			int opponentPieceCount = nineMensMorrisBoard.countPieces(3 - colour) + nineMensMorrisBoard.countPiecesInHand(3 - colour);
			int pieceDiffScore = myPieceCount - opponentPieceCount;
			score += pieceDiffScore * 256;
			// --- Mobility score ---
			int mobilityScore = nineMensMorrisBoard.getMobility(colour) - nineMensMorrisBoard.getMobility(3 - colour);
			score += mobilityScore * 64;
			// --- Lines ---
			int linesScore = 0;
			for (int line = 0; line < NineMensMorrisBoard.NUMBER_OF_LINES; line++) {
				int myCountInLine = 0;
				int opponentCountInLine = 0;
				int emptyPosition = -1;
				int[] positionsForLine = NineMensMorrisBoardLinks.getPositionsForLine(line);
				for (int position : positionsForLine) {
					int squareColour = nineMensMorrisBoard.getColour(position);
					if (squareColour == colour) {
						myCountInLine++;
					} else if (squareColour == 3 - colour) {
						opponentCountInLine++;
					} else {
						emptyPosition = position;
					}
				}
				if (myCountInLine > 0 && opponentCountInLine == 0) {
					linesScore += scoreOneColourInRow(emptyPosition, myCountInLine, nineMensMorrisBoard, colour, line); 
				} else if (opponentCountInLine > 0 && myCountInLine == 0) {
					linesScore -= scoreOneColourInRow(emptyPosition, opponentCountInLine, nineMensMorrisBoard, 3 - colour, line);
				}
			}
			score += linesScore;
		}
		return score;
	}

	private int scoreOneColourInRow(int emptyPosition, int count, NineMensMorrisBoard board, int colour, int line) {
		int scoreForOneColourInRow = 0;
		switch (count) {
			case 3:
				// mill
				scoreForOneColourInRow += 32;
				// add line mobility
				int waysToOpenLine = 0;
				int[] adjacentPositionsToLine = NineMensMorrisBoardLinks.getAdjacentPositionsToLine(line);
				for (int adjacentPositionToLine : adjacentPositionsToLine) {
					if (board.getColour(adjacentPositionToLine) == Colour.NONE) {
						waysToOpenLine++;
					}
				}			
				scoreForOneColourInRow += waysToOpenLine * 8;
				break;
			case 2:
				// two in row filled, one empty
				scoreForOneColourInRow += 64;
				int[] positionsForLine = NineMensMorrisBoardLinks.getPositionsForLine(line);
				int[] linksToEmptyPosition = NineMensMorrisBoardLinks.linksTo(emptyPosition);
				for (int linkToEmptyPosition : linksToEmptyPosition) {
					if (linkToEmptyPosition != positionsForLine[0] && linkToEmptyPosition != positionsForLine[1] &&
							linkToEmptyPosition != positionsForLine[2] && board.getColour(linkToEmptyPosition) == colour) {
						if (((NineMensMorrisPlayer)board.getCurrentPlayer()).getColour() == colour) {
							// can make mill immediately!
							scoreForOneColourInRow += 96;
						} else {
							// can make mill next go
							scoreForOneColourInRow += 64;
						}
					}
				}
				break;
			default:
				// one in row filled, two empty
				scoreForOneColourInRow += 64;
		}
		// Bonus based on line valency
		if (line <= 3) {
			scoreForOneColourInRow += 24; // valency 6 * 4
		} else if (line <= 7) {
			scoreForOneColourInRow += 16; // valency 4 * 4
		} else {
			scoreForOneColourInRow += 12; // valency 3 * 4
		}
		return scoreForOneColourInRow;
	}

	@Override
	public String toString() {
		return "Standard";
	}
}
