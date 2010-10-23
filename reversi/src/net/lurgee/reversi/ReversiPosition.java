/*
 * @(#)ReversiPosition.java		2007/10/05
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import net.lurgee.sgf.Position;

/**
 * Reversi position. This is simply a position on the board described by its x and y coordinates.
 * @author mpatric
 */
public class ReversiPosition implements Position {

	private final int value;
	private transient int animationFrame = 0;

	public ReversiPosition(int x, int y) {
		value = (x - 1) + ((y - 1) << 3);
	}

	public ReversiPosition(ReversiPosition position) {
		this.value = position.value;
		this.animationFrame = position.animationFrame;
	}

	public int getX() {
		return (value & 7) + 1;
	}
	
	public int getY() {
		return (value >> 3) + 1;
	}	

	public int getAnimationFrame() {
		return animationFrame;
	}

	public void setAnimationFrame(int animationFrame) {
		this.animationFrame = animationFrame;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ReversiPosition other = (ReversiPosition) obj;
		if (value != other.value) return false;
		return true;
	}
	
	@Override
	public String toString() {
		return (char) (getX() + 96) + "" + getY();
	}
}
