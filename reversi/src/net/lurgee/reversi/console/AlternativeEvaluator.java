/*
 * @(#)AlternativeEvaluator.java		2005/11/03
 *
 * Part of the reversi console application that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.console;

import net.lurgee.reversi.Colour;
import net.lurgee.reversi.ReversiBoard;
import net.lurgee.reversi.ReversiEvaluator;
import net.lurgee.reversi.ReversiPlayer;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Evaluator;

/**
 * Another reversi evaluator. Used by a {@link net.lurgee.sgf.AbstractSinglePassSearcher searcher} to score board states. This
 * evaluator is based on the standard evaluator {@link ReversiEvaluator}, but is included here to be tweaked for
 * various evaluation strategies to be tried against the standard evaluator (by playing them against each other) in an
 * effort to improve it.
 * @author mpatric
 */
public class AlternativeEvaluator implements Evaluator {
	
	private static final int WIPEOUT_SCORE = 1000;

	// LEVEL 1
//	private static final int PIECE_COUNT_WEIGHT[] = {5, 5, 5, 5, 5};
//	private static final int POTENTIAL_MOBILITY_WEIGHT[] = {0, 0, 0, 0, 0};
//	private static final int MOBILITY_WEIGHT[] = {0, 0, 0, 0, 0};
//	private static final int CORNER_WEIGHT[] = {35, 35, 35, 35, 0};
//	private static final int EDGE_WEIGHT[] = {0, 0, 0, 0, 0};
//	private static final int XSQUARE_WEIGHT[] = {0, 0, 0, 0, 0};
	// LEVEL 2
	private static final int PIECE_COUNT_WEIGHT[] = {1, 1, 2, 4, 1};
	private static final int POTENTIAL_MOBILITY_WEIGHT[] = {7, 4, 3, 2, 0};
	private static final int MOBILITY_WEIGHT[] = {0, 0, 0, 0, 0};
	private static final int CORNER_WEIGHT[] = {35, 35, 35, 35, 0};
	private static final int EDGE_WEIGHT[] = {0, 0, 0, 0, 0};
	private static final int XSQUARE_WEIGHT[] = {0, 0, 0, 0, 0};
	// LEVEL 3+
//	private static final int PIECE_COUNT_WEIGHT[] = {0, 0, 0, 4, 1};
//	private static final int POTENTIAL_MOBILITY_WEIGHT[] = {7, 4, 3, 2, 0};
//	private static final int MOBILITY_WEIGHT[] = {7, 6, 5, 4, 0};
//	private static final int CORNER_WEIGHT[] = {35, 35, 35, 35, 0};
//	private static final int EDGE_WEIGHT[] = {0, 2, 3, 4, 0};
//	private static final int XSQUARE_WEIGHT[] = {-8, -8, -8, -8, 0};
	
	public AlternativeEvaluator() {
	}

	/**
	 * Determine the score of the given board for the specified player. Evaluation is based on the following strategies: stage of the game
	 * (weights for each strategy based on the stage of the game), piece differential, potential mobility, mobility, corners, edges and
	 * x-squares.
	 * @param startBoard The board before any moves were made.
	 * @param board The board to score.
	 * @param currentDepth The depth of the leaf in the tree where this scoring is being done.
	 * @param searchDepth The depth being used for searches - will be the same as currentDepth unless an early evaluation is done for a 'no-bye' situation.
	 * @return A score for the board for the specified player.
	 */
	public int score(AbstractBoard startBoard, AbstractBoard board, int currentDepth, int searchDepth) {
		int sc = 0;
		int myScore = 0;
		int yourScore = 0;
		ReversiPlayer player = (ReversiPlayer) ((AbstractBoard) board).getCurrentPlayer();
		int colour = ((ReversiPlayer) player).getColour();
		ReversiBoard originalBoard = (ReversiBoard) startBoard;
		ReversiBoard currentBoard = (ReversiBoard) board;
		// *** check for a wipeout
		if (((ReversiBoard) board).getCount(colour) == 0) {
			// note: we stop evaluating here, there's no point going on if it's a wipeout
			sc = -WIPEOUT_SCORE;
		} else if (((ReversiBoard) board).getCount(3 - colour) == 0) {
			// note: we stop evaluating here, there's no point going on if it's a wipeout
			sc = WIPEOUT_SCORE;
		} else {
			int weightBand = determineWeightBand(board, currentDepth);		
			// *** piece change differential
			if (PIECE_COUNT_WEIGHT[weightBand] != 0) {
				myScore = currentBoard.getCount(colour) - originalBoard.getCount(colour);
				yourScore = currentBoard.getCount(3 - colour) - originalBoard.getCount(3 - colour);
				sc += PIECE_COUNT_WEIGHT[weightBand] * (myScore - yourScore);
			}
			// *** corner differential (only consider changed pieces)
			if (CORNER_WEIGHT[weightBand] != 0) {
				myScore = 0;
				yourScore = 0;
				for (int y = 1; y <= 8; y += 7) {
					if (currentBoard.getSquares(y) != 0) {
						for (int x = 1; x <= 8; x += 7) {
							if (originalBoard.getSquare(x, y) == Colour.NONE) {
								if (currentBoard.getSquare(x, y) == colour) {
									myScore++;
								} else if (currentBoard.getSquare(x, y) == 3 - colour) {
									yourScore++;
								}
							}
						}
					}
				}
				sc += CORNER_WEIGHT[weightBand] * (myScore - yourScore);
			}
			// *** edge differential, excluding corners and c-squares (only consider changed pieces)
			if (EDGE_WEIGHT[weightBand] != 0) {
				myScore = 0;
				yourScore = 0;
				for (int y = 1; y <= 8; y += 7) {
					if (currentBoard.getSquares(y) != 0) {
						for (int x = 2; x <= 7; x++) {
							int edgeColour = currentBoard.getSquare(x, y);
							if (originalBoard.getSquare(x, y) != edgeColour) {							
								if (edgeColour == colour) {
									myScore++;
								} else if (edgeColour == 3 - colour) {
									yourScore++;
								}
							}
						}
					}
				}
				for (int y = 2; y <= 7; y++) {
					if (currentBoard.getSquares(y) != 0) {
						for (int x = 1; x <= 8; x += 7) {
							int edgeColour = currentBoard.getSquare(x, y);
							if (originalBoard.getSquare(x, y) != edgeColour) {							
								if (edgeColour == colour) {
									myScore++;
								} else if (edgeColour == 3 - colour) {
									yourScore++;
								}
							}
						}
					}
				}
				sc += EDGE_WEIGHT[weightBand] * (myScore - yourScore);
			}
			// *** x-square differential (only consider new pieces, not flipped ones; only consider x-squares next to open corners)
			if (XSQUARE_WEIGHT[weightBand] != 0) {
				myScore = 0;
				yourScore = 0;
				for (int y = 2; y <= 7; y += 5) {
					if (currentBoard.getSquares(y) != 0) {
						for (int x = 2; x <= 7; x += 5) {
							int xsColour = currentBoard.getSquare(x, y);
							if (xsColour != Colour.NONE && originalBoard.getSquare(x, y) == Colour.NONE) {
								// determine nearest corner
								int cornerx = x, cornery = y;
								if (cornerx == 2) {
									cornerx = 1;
								} else if (cornerx == 7) {
									cornerx = 8;
								}
								if (cornery == 2) {
									cornery = 1;
								} else if (cornery == 7) {
									cornery = 8;
								}
								// only consider this x-square if the nearest corner is open
								if (currentBoard.getSquare(cornerx, cornery) == Colour.NONE) {
									if (xsColour == colour) {
										myScore++;
									} else {
										yourScore++;
									}
								}
							}
						}
					}
				}
				sc += XSQUARE_WEIGHT[weightBand] * (myScore - yourScore);
			}
			// *** potential mobility differential (consider whole board)
			if (POTENTIAL_MOBILITY_WEIGHT[weightBand] != 0) {
				// myCount is diffence in adjacent squares to opponent's colour
				myScore = ((ReversiBoard) board).getAdjacentCount(3 - colour) - ((ReversiBoard) startBoard).getAdjacentCount(3 - colour);
				// oppCount is diffence in adjacent squares to my colour
				yourScore = ((ReversiBoard) board).getAdjacentCount(colour) - ((ReversiBoard) startBoard).getAdjacentCount(colour);
				sc += POTENTIAL_MOBILITY_WEIGHT[weightBand] * (myScore - yourScore);
			}
			// *** mobility differential (consider whole board)
			if (MOBILITY_WEIGHT[weightBand] != 0) {
				myScore = ((ReversiBoard) board).countValidMoves(player) - ((ReversiBoard) startBoard).countValidMoves(player);
				yourScore = ((ReversiBoard) board).countValidMoves(ReversiPlayer.getInstance(3 - colour)) - ((ReversiBoard) startBoard).countValidMoves(ReversiPlayer.getInstance(3 - colour));
				sc += MOBILITY_WEIGHT[weightBand] * (myScore - yourScore);
			}
		}
		return sc;
	}

	/**
	 * Return the weight 'band' for the selection of weights in scoring. The bands are as follows:
	 * <ul>
	 * 		<li>0 - Up to 16 pieces have been placed on the board</li>
	 * 		<li>1 - 17 to 32 pieces have been placed on the board</li>
	 * 		<li>2 - 33 to 48 pieces have been placed on the board</li>
	 * 		<li>3 - 49 to (64 - depth) pieces have been placed on the board</li>
	 * 		<li>4 - (64 - depth) or more pieces have been placed on the board</li>
	 * </ul>
	 * @param board The board to score.
	 * @param depth The depth of the leaf in the tree where this scoring is being done.
	 * @return The selected weight band (0 to 4).
	 */
	private int determineWeightBand(AbstractBoard board, int depth) {
		int pieceCount = ((ReversiBoard) board).getCount(Colour.ANY);
		if (pieceCount <= 16) {
			return 0;
		} else if (pieceCount <= 32) {
			return 1;
		} else if (pieceCount <= 48) {
			return 2;
		} else if (pieceCount < 64 - depth) {
			return 3;
		} else {
			return 4;
		}
	}
	
	@Override
	public String toString() {
		return "Alternative";
	}
}
