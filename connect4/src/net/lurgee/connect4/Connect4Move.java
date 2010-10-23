/*
 * @(#)Connect4Move.java		2007/03/07
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import net.lurgee.sgf.Move;

/**
 * Connect-four move. This is simply the column on the board being played (1-7).
 * @author mpatric
 */
public class Connect4Move implements Move {

	private final int x;
	
	/**
	 * @deprecated Connect-four moves should be obtained from a {@link Connect4MoveFactory} rather.
	 */
	@Deprecated
	public Connect4Move(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	@Override
	public String toString() {
		return Integer.toString(x);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		final Connect4Move other = (Connect4Move) obj;
		if (x != other.x) return false;
		return true;
	}
}
