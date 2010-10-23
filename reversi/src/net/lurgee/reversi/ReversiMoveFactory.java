/*
 * @(#)ReversiMoveFactory.java		2007/10/27
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import net.lurgee.sgf.MoveFactory;

/**
 * Factory class that provides {@link ReversiMove} instances specified by a set of x and y co-ordinates
 * or by a string in the usual reversi notation (for example, "c5").
 * @author mpatric
 */
public class ReversiMoveFactory implements MoveFactory {

	public ReversiMove moves[][] = {
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null }
	};
	
	@SuppressWarnings("deprecation")
	public ReversiMoveFactory() {
		for (int x = 1; x <= moves.length; x++) {
			for (int y = 1; y <= moves[x - 1].length; y++) {
				moves[x - 1][y - 1] = new ReversiMove(x, y);
			}
		}
	}
		
	public ReversiMove createMove(int x, int y) {
		return moves[x - 1][y - 1];
	}

	public ReversiMove createMoveFromString(String moveAsString) {
		if (moveAsString.length() != 2) {
			throw new IllegalArgumentException("Cannot set reversi move from provided string");
		}
		int x = moveAsString.charAt(0) - 96;
		int y = moveAsString.charAt(1) - 48;
		if (x < 1 || x > ReversiBoard.X_DIMENSION || y < 1 || y > ReversiBoard.Y_DIMENSION) {
			throw new IllegalArgumentException("Invalid reversi move");
		}
		return createMove(x, y);
	}
}
