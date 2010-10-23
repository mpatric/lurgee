/*
 * @(#)ReversiPlayer.java		2005/11/02
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

import net.lurgee.sgf.Player;

/**
 * Reversi player. A player is identified by the colour that they play (black or white). This class includes instances
 * {@link #PLAYER_BLACK} and {@link #PLAYER_WHITE}.
 * @author mpatric
 */
public class ReversiPlayer implements Player {

	private static final ReversiPlayer PLAYER_BLACK = new ReversiPlayer(Colour.BLACK, 'O', "Black");
	private static final ReversiPlayer PLAYER_WHITE = new ReversiPlayer(Colour.WHITE, 'X', "White");
	private static final String MSG_INVALID_COLOUR = "Invalid colour";

	private final int colour;
	private final char symbol;
	private final String name;

	/**
	 * Constructor.
	 * @param colour Colour for the player ({@link Colour#BLACK} or {@link Colour#WHITE}).
	 */
	public ReversiPlayer(int colour, char symbol, String name) {
		this.name = name;
		if (colour != Colour.BLACK && colour != Colour.WHITE) throw new IllegalArgumentException(MSG_INVALID_COLOUR);
		this.symbol = symbol;
		this.colour = colour;
	}

	public int getColour() {
		return colour;
	}
	
	public char getSymbol() {
		return symbol;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ReversiPlayer other = (ReversiPlayer) obj;
		if (colour != other.colour) return false;
		if (symbol != other.symbol) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}

	/**
	 * Gets one of the instances, {@link #PLAYER_BLACK} or {@link #PLAYER_WHITE}.
	 * @param colour Colour for the player to get ({@link Colour#BLACK} or {@link Colour#WHITE}).
	 */
	public static ReversiPlayer getInstance(int colour) {
		if (colour == Colour.BLACK) return PLAYER_BLACK;
		else if (colour == Colour.WHITE) return PLAYER_WHITE;
		else return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
