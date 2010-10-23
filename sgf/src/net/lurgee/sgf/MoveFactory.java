/*
 * @(#)AbstractMoveFactory.java		2005/10/27
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Interface for a factory that provides {@link Move} instances.
 * @author mpatric
 */
public interface MoveFactory {

	/**
	 * Create a move from the representation of the move in the provided string.
	 * @param moveAsString A string representation of the move, which is game-dependent.
	 * @return A move instance.
	 */
	Move createMoveFromString(String moveAsString);
}
