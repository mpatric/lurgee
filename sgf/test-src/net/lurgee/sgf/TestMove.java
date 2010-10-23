/*
 * @(#)TestMove.java		2005/11/16
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Move used for unit tests.
 * @author mpatric
 */
public class TestMove implements Move {

	private final TestPosition position;
	
	public TestMove(char positionValue) {
		position = new TestPosition(positionValue);
	}

	public TestPosition getPosition() {
		return position;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final TestMove other = (TestMove) obj;
		if (position == null) {
			if (other.position != null) return false;
		} else if (!position.equals(other.position)) return false;
		return true;
	}

	@Override
	public String toString() {
		return getPosition().toString();
	}
}
