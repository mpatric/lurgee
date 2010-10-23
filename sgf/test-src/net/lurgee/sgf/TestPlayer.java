/*
 * @(#)TestPlayer.java		2005/11/16
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Player used for unit tests.
 * @author mpatric
 */
public class TestPlayer implements Player {

	private static final TestPlayer PLAYER_ONE = new TestPlayer(1, '1', "One");
	private static final TestPlayer PLAYER_TWO = new TestPlayer(2, '2', "Two");

	private int number = 0;
	private final char symbol;
	private final String name;

	public TestPlayer(int number, char symbol, String name) {
		this.number = number;
		this.symbol = symbol;
		this.name = name;
	}

	public int getNumber() {
		return number;
	}
	
	public char getSymbol() {
		return symbol;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object object) {
		if (object != null) {
			if (object instanceof TestPlayer) {
				if (((TestPlayer) object).getNumber() == number) return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return Integer.toString(getNumber());
	}
	
	public static TestPlayer getInstance(int number) {
		if (number == 1) return PLAYER_ONE;
		else if (number == 2) return PLAYER_TWO;
		else return null;
	}
}
