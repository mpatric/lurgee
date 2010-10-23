/*
 * @(#)ReversiLibrary.java		2005/11/02
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import java.util.Random;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Library;
import net.lurgee.sgf.Move;

/**
 * Reversi opening move library. Used by a {@link net.lurgee.sgf.AbstractSinglePassSearcher searcher} to select the 2nd move of
 * the game only. Selection of the parallel, diagonal and perpendicular opening is done randomly, with better openings
 * selected more often.
 * @author mpatric
 */
public class ReversiLibrary implements Library {

	private static final Random random = new Random();
	
	public static final int NONE = -2;
	public static final int RANDOM = -1;
	public static final int PARALLEL = 0;
	public static final int DIAGONAL = 1;
	public static final int PERPENDICULAR = 2;

	private final GameContext gameContext;
	private final int openingToPlay;
	private int openingPlayed = -1;

	public ReversiLibrary(GameContext gameContext) {
		this.gameContext = gameContext;
		openingToPlay = RANDOM;
	}
	
	/**
	 * Constructor with forced opening option.
	 * @param openingToPlay The opening move to play.
	 */
	public ReversiLibrary(GameContext gameContext, int openingToPlay) {
		this.gameContext = gameContext;
		if (openingToPlay != RANDOM && openingToPlay != PARALLEL && openingToPlay != DIAGONAL && openingToPlay != PERPENDICULAR) {
			throw new IllegalArgumentException();
		}
		this.openingToPlay = openingToPlay;
	}
	
	public boolean shouldUseLibrary(AbstractBoard board, int depth) {
		if (board.countMovesMade() == 1) {
			return true;
		}
		return false;
	}

	public Move findMove(AbstractBoard board, int depth) {
		ReversiMove reversiMove = null;
		int r = (random.nextInt()) % 5; // -4 to 4 (9 options)
		ReversiMoveFactory moveFactory = (ReversiMoveFactory) gameContext.getMoveFactory();
		if (openingToPlay == PARALLEL || (openingToPlay == RANDOM && depth <= 3 && r == -4)) {
			openingPlayed = PARALLEL;
			// for RANDOM, picked 1 out of 9 times (11%) for level <= 3 OR 0 out of 9 times (0%) for level > 3
			if (((ReversiBoard) board).getSquare(5, 3) != 0) reversiMove = moveFactory.createMove(4, 3);
			else if (((ReversiBoard) board).getSquare(6, 4) != 0) reversiMove = moveFactory.createMove(6, 5);
			else if (((ReversiBoard) board).getSquare(3, 5) != 0) reversiMove = moveFactory.createMove(3, 4);
			else if (((ReversiBoard) board).getSquare(4, 6) != 0) reversiMove = moveFactory.createMove(5, 6);
			// other colour
			else if (((ReversiBoard) board).getSquare(4, 3) != 0) reversiMove = moveFactory.createMove(5, 3);
			else if (((ReversiBoard) board).getSquare(3, 4) != 0) reversiMove = moveFactory.createMove(3, 5);
			else if (((ReversiBoard) board).getSquare(6, 5) != 0) reversiMove = moveFactory.createMove(6, 4);
			else if (((ReversiBoard) board).getSquare(5, 6) != 0) reversiMove = moveFactory.createMove(4, 6);
		} else if (openingToPlay == DIAGONAL || (openingToPlay == RANDOM && r >= -3 && r <= -1)) {
			openingPlayed = DIAGONAL;
			// for RANDOM, picked 3 out of 9 times (33%)
			if (((ReversiBoard) board).getSquare(5, 3) != 0) reversiMove = moveFactory.createMove(6, 3);
			else if (((ReversiBoard) board).getSquare(6, 4) != 0) reversiMove = moveFactory.createMove(6, 3);
			else if (((ReversiBoard) board).getSquare(3, 5) != 0) reversiMove = moveFactory.createMove(3, 6);
			else if (((ReversiBoard) board).getSquare(4, 6) != 0) reversiMove = moveFactory.createMove(3, 6);
			// other colour
			else if (((ReversiBoard) board).getSquare(4, 3) != 0) reversiMove = moveFactory.createMove(3, 3);
			else if (((ReversiBoard) board).getSquare(3, 4) != 0) reversiMove = moveFactory.createMove(3, 3);
			else if (((ReversiBoard) board).getSquare(6, 5) != 0) reversiMove = moveFactory.createMove(6, 6);
			else if (((ReversiBoard) board).getSquare(5, 6) != 0) reversiMove = moveFactory.createMove(6, 6);
		} else if (openingToPlay == PERPENDICULAR || openingToPlay == RANDOM) {
			openingPlayed = PERPENDICULAR;
			// for RANDOM, picked 5 out of 9 times (56%) for level <= 3 OR 6 out of 9 times (67%) for level > 3
			if (((ReversiBoard) board).getSquare(5, 3) != 0) reversiMove = moveFactory.createMove(6, 5);
			else if (((ReversiBoard) board).getSquare(6, 4) != 0) reversiMove = moveFactory.createMove(4, 3);
			else if (((ReversiBoard) board).getSquare(3, 5) != 0) reversiMove = moveFactory.createMove(5, 6);
			else if (((ReversiBoard) board).getSquare(4, 6) != 0) reversiMove = moveFactory.createMove(3, 4);
			// other colour
			else if (((ReversiBoard) board).getSquare(4, 3) != 0) reversiMove = moveFactory.createMove(3, 5);
			else if (((ReversiBoard) board).getSquare(3, 4) != 0) reversiMove = moveFactory.createMove(5, 3);
			else if (((ReversiBoard) board).getSquare(6, 5) != 0) reversiMove = moveFactory.createMove(4, 6);
			else if (((ReversiBoard) board).getSquare(5, 6) != 0) reversiMove = moveFactory.createMove(6, 4);
		} 
		return reversiMove;
	}

	public int getOpeningPlayed() {
		return openingPlayed;
	}
}
