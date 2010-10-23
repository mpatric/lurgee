/*
 * @(#)Library.java		2006/01/28
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Interface representing a move library in the game. Should a move library be required for the game being implemented,
 * this interface should be implemented to provide library moves based on the state of the board.
 * @author mpatric
 */
public interface Library {

	/**
	 * Determine if the library should be used based on the state of the game board. The depth that would be used if
	 * this move was determined by a game tree search is also provided as this may be used as a determining factor on
	 * whether or not to use the library.
	 * @param board The current state of the board.
	 * @param depth The depth that would be used if this move was determined by a game tree search.
	 * @return
	 * 	<ul>
	 * 		<li>true if the library should be used;</li>
	 * 		<li>false otherwise.</li>
	 * 	</ul>
	 */
	boolean shouldUseLibrary(AbstractBoard board, int depth);

	/**
	 * Use the library to determine the next move. The depth that would be used if this move was determined by a game
	 * tree search is also provided as this may be used as a factor in the lookup (for example, it might be used to
	 * represent the difficulty level).
	 * @param board The current state of the board.
	 * @param depth The depth that would be used if this move was determined by a game tree search.
	 * @return The selected move.
	 */
	Move findMove(AbstractBoard board, int depth);
}
