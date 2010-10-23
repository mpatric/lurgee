/*
 * @(#)ReversiDifferenceBoard.java		2007/03/20
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 * 
 */

package net.lurgee.reversi;

/**
 * Interface with only the methods available when for comparing two boards.
 * @author mpatric
 */
public interface ReversiDifferenceBoard {
	
	/**
	 * Get the squares for one line of the board.
	 * @param y Y-coordinate of the square (1-8).
	 * @return The bit field representing a row on the board.
	 */
	int getSquares(int y);
	
	/**
	 * Get the state of a square at the specified position on the board.
	 * @param x X-coordinate of the square (1-8).
	 * @param y Y-coordinate of the square (1-8).
	 * @return
	 * <ul>
	 * 	<li>{@link Colour#NONE} if the square is empty;</li>
	 * 	<li>{@link Colour#BLACK} if the square contains a black piece;</li>
	 * 	<li>{@link Colour#WHITE} if the square contains a white piece.</li>
	 * </ul>
	 */
	int getSquare(int x, int y);
	
	/**
	 * Count how many empty squares there are on the board, how many non-empty squares there are on the board, or how many
	 * squares with a piece of a specific colour there are on the board.
	 * @param colour {@link Colour#NONE} to count empty squares, {@link Colour#ANY} to
	 * count non-empty squares, or {@link Colour#BLACK} or {@link Colour#WHITE} to count
	 * squares with a piece of a specific colour.
	 * @return The count.
	 */
	int getCount(int colour);
}
