/*
 * @(#)Connect4Position.java		2007/06/19
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import net.lurgee.sgf.Position;

/**
 * Position on a connect4 board, which is simply an x and y value. Used by {@link Connect4Board} to provide a list of board positions.
 * @author mpatric
 */
public class Connect4Position implements Position {

	private final int x;
	private final int y;
	
	public Connect4Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Connect4Position(Connect4Position position) {
		this(position.x, position.y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Connect4Position other = (Connect4Position) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}
	
	public String toString() {
		return x + "," + y;
	}
}
