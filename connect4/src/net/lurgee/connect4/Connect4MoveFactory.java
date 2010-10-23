/*
 * @(#)Connect4MoveFactory.java		2007/10/07
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import net.lurgee.sgf.MoveFactory;

/**
 * Factory class that provides {@link Connect4Move} instances specified by an x co-ordinate
 * or by a string representing the column (for example, "5").
 * @author mpatric
 */
public class Connect4MoveFactory implements MoveFactory {

	public Connect4Move moves[] = { null, null, null, null, null, null, null };
	
	@SuppressWarnings("deprecation")
	public Connect4MoveFactory() {
		for (int x = 1; x <= moves.length; x++) {
			moves[x - 1] = new Connect4Move(x);
		}
	}
		
	public Connect4Move createMove(int x) {
		return moves[x - 1];
	}

	public Connect4Move createMoveFromString(String moveAsString) {
		if (moveAsString.length() != 1) {
			throw new IllegalArgumentException("Cannot set connect4 move from provided string");
		}
		int x = moveAsString.charAt(0) - 48;
		if (x < 1 || x > Connect4Board.X_DIMENSION) {
			throw new IllegalArgumentException("Invalid connect4 move");
		}
		return createMove(x);
	}
}
