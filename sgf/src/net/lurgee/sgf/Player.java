/*
 * @(#)Player.java		2005/11/01
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Interface that a class representing a player in the game must implement.
 * @author mpatric
 */
public interface Player {
	
	/**
	 * Return a character symbol representing the player.
	 * @return A character symbol representing the player.
	 */
	char getSymbol();
		
	/**
	 * Get a name representing the player.
	 * @return A name representing the player.
	 */
	String getName();
}
