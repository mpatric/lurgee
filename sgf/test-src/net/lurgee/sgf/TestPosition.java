/*
 * @(#)TestMove.java		2005/11/16
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Immutable position used for unit tests.
 * @author mpatric
 */
public class TestPosition implements Position {

	private final char value;
	
	public TestPosition(char state) {
		this.value = state;
	}

	public TestPosition(TestPosition position) {
		this(position.getValue());
	}

	public char getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final TestPosition other = (TestPosition) obj;
		if (value != other.value) return false;
		return true;
	}

	@Override
	public String toString() {
		return "" + getValue();
	}
}
