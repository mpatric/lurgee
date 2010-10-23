/*
 * @(#)AbstractBoard.java		2005/11/01
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.Iterator;
import java.util.List;

/**
 * Abstract class representing a board in the game. This class should be extended to represent the specifics of the
 * board for the game being implemented.
 * @author mpatric
 */
public abstract class AbstractBoard implements Poolable {

	protected GameContext gameContext = null;
	protected Player currentPlayer = null;
	protected boolean gameOver = false;
	private Move lastMovePlayed = null;
	
	public void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setCurrentPlayer(Player currentPlayer) {
		if (!gameContext.getPlayers().contains(currentPlayer)) {
			throw new IllegalArgumentException("Player not valid for this game");
		}
		this.currentPlayer = currentPlayer;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public Move getLastMovePlayed() {
		return lastMovePlayed;
	}
	
	protected void clear() {
		currentPlayer = null;
		lastMovePlayed = null;
		gameOver = false;
	}
	
	/**
	 * Count the maximum number of moves left in the game. Should be over-ridden by games where a definite
	 * number of maximum moves to the end of the game can be determined.
	 * @return A count of the maximum number of moves left in the game.
	 */
	public int countMaxMovesLeft() {
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Plays the move specified by the provided move for the current player, then set the board state accordingly.
	 * Calls {@link AbstractBoard#makeMove(Move, List, boolean) makeMove} method on {@link AbstractBoard}. 
	 * @param move The move to play.
	 * @param changes A container for adding the changes that this move resulted in. Should be empty on entry as it's not cleared here.
	 * Must also allow for null if the caller is not interested in the changes to the board.
	 * @param searching This is being called during a tree search for the best move. May be used to determine whether
	 * to set properties on the board only of interest after the actual move is played.
	 * @return The result of playing the move:
	 * 	<ul>
	 * 		<li>&lt;&gt;0 if the move was successful; the actual value is dependent on the game;</li>
	 * 		<li>0 if the move was invalid.</li>
	 * 	</ul>
	 */
	public int playMove(Move move, List<Position> changes, boolean searching) {
		int result = makeMove(move, changes, searching);
		if (result != 0) {
			lastMovePlayed = move;
		}
		nextPlayer();
		return result;
	}
	
	/**
	 * Change the current player to the next player.
	 */
	public void nextPlayer() {
		if (gameContext.getPlayers().size() == 0) {
			throw new IllegalStateException("No players defined");
		}
		if (currentPlayer == null) {
			// get the first player
			currentPlayer = (Player) gameContext.getPlayers().get(0);
		} else {
			Iterator<Player> iterator = gameContext.getPlayers().iterator();
			while (iterator.hasNext()) {
				Player player = iterator.next();
				if (player.equals(currentPlayer)) {
					if (iterator.hasNext()) {
						currentPlayer = iterator.next();
					} else {
						currentPlayer = gameContext.getPlayers().get(0);
					}
					break;
				}
			}
		}
	}
	
	public void copy(AbstractBoard board) {
		if (board instanceof AbstractBoard) {
			this.gameContext = ((AbstractBoard) board).gameContext;
			this.currentPlayer = ((AbstractBoard) board).currentPlayer;
			this.lastMovePlayed = ((AbstractBoard) board).lastMovePlayed;
			this.gameOver = ((AbstractBoard) board).gameOver;
		}
	}

	public void poolableInit(Object params) throws RuntimeException {
		if (params == null) {
			throw new ObjectPoolException("Game context missing");
		}
		setGameContext((GameContext) params);
	}

	public void poolableRecycle(Object params) throws RuntimeException {
		if (params == null) {
			throw new ObjectPoolException("Game context missing");
		}
		setGameContext((GameContext) params);
	}

	public void poolableDone() {
		clear();
	}
	
	/**
	 * Compare this board with another object. The objects are deemed to be equal if and only if the argument is not
	 * null and is a {@link AbstractBoard} of the same subclass as this one, and represents the same board (it has
	 * the same identifiers).
	 * @param obj The object to compare to.
	 * @return
	 * 	<ul>
	 * 		<li>true if the objects are equal;</li>
	 * 		<li>false otherwise.</li>
	 * 	</ul>
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		final AbstractBoard other = (AbstractBoard) obj;
		if (gameOver != other.gameOver) return false;
		if (currentPlayer == null) {
			if (other.currentPlayer != null) return false;
		} else if (!currentPlayer.equals(other.currentPlayer)) return false;
		if (gameContext == null) {
			if (other.gameContext != null) return false;
		} else if (!gameContext.equals(other.gameContext)) return false;
		return true;
	}
	
	/**
	 * Initialise the board to the state it is in at the start of a game.
	 */
	public abstract void initialise();

	/**
	 * Determine whether the current player has any valid moves.
	 * @return
	 * 	<ul>
	 * 		<li>true if the player can move;</li>
	 * 		<li>false otherwise.</li>
	 * 	</ul>
	 */
	public abstract boolean canMove();

	/**
	 * Get a list of all the valid moves that the current player has. Should use {@link #gameContext}.{@link GameContext#moveFactory moveFactory}
	 * to generate {@link Move} instances.
	 * @param moveRanker A ranker for ordering the moves, may be null for no ordering.
	 * @param depth The current search depth, needed for ranking moves.
	 * @return A list of the moves the player has; should be an empty list if the user has no moves.
	 */
	public abstract List<Move> getValidMoves(MoveRanker moveRanker, int depth);

	/**
	 * Count the number of moves that have been made so far in the game.
	 * @return A count of the number of moves that have been made so far in the game.
	 */
	public abstract int countMovesMade();

	/**
	 * Make the move specified by the provided move for the current player.
	 * @param move The move to make.
	 * @param changes A container for adding the positions on the board that were changed by this move. Should be empty on entry as it's not cleared here.
	 * Must also allow for null if the caller is not interested in the changes.
	 * @param searching This is being called during a tree search for the best move. May be used to determine whether
	 * to set properties on the board only of interest after the actual move is played.
	 * @return The result of making the move
	 * 	<ul>
	 * 		<li>&lt;&gt;0 if the move was successful; the actual value is dependent on the game;</li>
	 * 		<li>0 if the move was invalid.</li>
	 * 	</ul>
	 */
	public abstract int makeMove(Move move, List<Position> changes, boolean searching);
}
