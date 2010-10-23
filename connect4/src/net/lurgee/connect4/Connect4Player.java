/*
 * @(#)Connect4Player.java		2007/03/07
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import net.lurgee.sgf.Player;

/**
 * Connect-four player. A player is identified by the colour that they play (red or yellow). This class includes instances
 * {@link #PLAYER_RED} and {@link #PLAYER_YELLOW}.
 * @author mpatric
 */
public class Connect4Player implements Player {

	private static final Connect4Player PLAYER_RED = new Connect4Player(Colour.RED, 'O', "Red");
	private static final Connect4Player PLAYER_YELLOW = new Connect4Player(Colour.YELLOW, 'X', "Yellow");
	private static final String MSG_INVALID_COLOUR = "Invalid colour";
	
	protected final int colour;
	private final char symbol;
	private final String name;

	public Connect4Player(int colour, char symbol, String name) {
		if (colour != Colour.RED && colour != Colour.YELLOW) throw new IllegalArgumentException(MSG_INVALID_COLOUR);
		this.colour = colour;
		this.symbol = symbol;
		this.name = name;
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
		final Connect4Player other = (Connect4Player) obj;
		if (colour != other.colour) return false;
		if (symbol != other.symbol) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}

	/**
	 * Gets one of the instances, {@link #PLAYER_RED} or {@link #PLAYER_YELLOW}.
	 * @param colour Colour for the player to get ({@link Colour#RED} or {@link Colour#YELLOW}).
	 */
	public static Connect4Player getInstance(int colour) {
		if (colour == Colour.RED) return PLAYER_RED;
		else if (colour == Colour.YELLOW) return PLAYER_YELLOW;
		else return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
