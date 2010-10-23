/*
 * @(#)GameContext.java		2007/10/27
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.Arrays;
import java.util.List;

/**
 * A class containing the context for the game. This is used by many of the classes in the framework. Typically an
 * implementation of a game will have one instance of this class (usually subclassed to provide additional functionality
 * such as game-specific methods for creating moves with the MoveFactory), which is injected into the
 * classes that require a game context.
 * @author mpatric
 */
public class GameContext {
	
	protected final List<Player> players;
	protected ObjectPool boardPool;
	protected final MoveFactory moveFactory;
	protected final boolean randomlyChooseFromEquallyRankedScores;
	
	public GameContext(Player[] players, ObjectPool boardPool, MoveFactory moveFactory, boolean randomlyChooseFromEquallyRankedScores) {
		this.players = Arrays.asList(players);
		this.boardPool = boardPool;
		this.moveFactory = moveFactory;
		this.randomlyChooseFromEquallyRankedScores = randomlyChooseFromEquallyRankedScores;
	}
	
	public void setBoardPool(ObjectPool boardPool) {
		this.boardPool = boardPool;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public MoveFactory getMoveFactory() {
		return moveFactory;
	}
	
	public AbstractBoard checkOutBoard() {
		return (AbstractBoard) boardPool.checkOut(this);
	}

	public boolean checkInBoard(AbstractBoard board) {
		return boardPool.checkIn(board);
	}
	
	public int checkInBoards(AbstractBoard... boards) {
		int checkIns = 0;
		for (AbstractBoard board : boards) {
			if (boardPool.checkIn(board)) {
				checkIns++;			
			}
		}
		return checkIns;
	}

	public MoveList createMoveList(int initialCapacity) {
		return new MoveList(initialCapacity, randomlyChooseFromEquallyRankedScores);
	}
}
