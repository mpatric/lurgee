/*
 * @(#)ReversiMove.java		2005/11/02
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import net.lurgee.sgf.Move;

/**
 * Reversi move. This is simply a position on the board described by its x and y coordinates.
 * @author mpatric
 */
public class ReversiMove implements Move {
	
	private final ReversiPosition position;
	
	/**
	 * @deprecated Reversi moves should be obtained from a {@link ReversiMoveFactory} rather.
	 */
	@Deprecated
	public ReversiMove(int x, int y) {
		position = new ReversiPosition(x, y);
	}

	public ReversiPosition getPosition() {
		return position;
	}
	
	@Override
	public String toString() {
		return position.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		final ReversiMove other = (ReversiMove) obj;
		if (position == null) {
			if (other.position != null) return false;
		} else if (!position.equals(other.position)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}
}
