/*
 * @(#)AbstractGame.java		2007/06/09
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import java.util.ArrayList;
import java.util.List;

import net.lurgee.sgf.AbortException;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.AbstractSearcher;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.Position;

/**
 * Container for all game entities related to sgf and common elements of the game being implemented.
 * @author mpatric
 */
public abstract class AbstractGame {

	private final Settings settings;
	protected final GameContext gameContext;
	private String statusMessage = "";
	private AbstractSearcher searcher = null;
	private MoveRanker moveRanker = null;
	private AbstractBoard board = null;
	private ArrayList<AbstractBoard> boardHistory = new ArrayList<AbstractBoard>();
	private boolean busy = false;
	private ArrayList<Position> changes = new ArrayList<Position>();
	
	public AbstractGame(GameContext gameContext, Settings settings) {
		this.gameContext = gameContext;
		this.settings = settings;
		setupSearcherAndMoveRanker();
	}
	
	public GameContext getGameContext() {
		return gameContext;
	}

	public Settings getSettings() {
		return settings;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	public AbstractSearcher getSearcher() {
		return searcher;
	}

	public void setSearcher(AbstractSearcher searcher) {
		this.searcher = searcher;
	}
	
	public MoveRanker getMoveRanker() {
		return moveRanker;
	}

	public void setMoveRanker(MoveRanker moveRanker) {
		this.moveRanker = moveRanker;
	}
	
	public void abortSearches() {
		searcher.abortSearch();
	}

	public boolean lastSearchWasAborted() {
		return searcher.isAborted();
	}
	
	public AbstractBoard getBoard() {
		return board;
	}
	
	public void setBoard(AbstractBoard board) {
		this.board = board;
	}
	
	public List<AbstractBoard> getBoardHistory() {
		return boardHistory;
	}
	
	public List<Position> getChanges() {
		return changes;
	}
	
	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	public void setup() {
		if (getBoard() == null) {
			board = gameContext.checkOutBoard();
		}
		board.initialise();
		changes.clear();
		// must check the boards from the board history back into the pool
		int boardHistorySize;
		while ((boardHistorySize = boardHistory.size()) > 0) {
			AbstractBoard board = boardHistory.get(boardHistorySize - 1);
			gameContext.checkInBoard(board);
			boardHistory.remove(board);
		}
		setStatusMessage("");
		initialise();
	}
	
	public Move determineMove() {
		Move move;
		try {
			move = (Move) getSearcher().findMove(getBoard(), moveRanker, settings.getSearchLevel(), settings.getSearchThresholds()); 
		} catch (AbortException ae) {
			move = null;
		} finally {
			postDetermineMove();
		}
		return move;
	}
	
	protected void postDetermineMove() {
	}

	public void playMove(Move move) {
		AbstractBoard oldBoard = gameContext.checkOutBoard();
		oldBoard.copy(getBoard());
		boardHistory.add(oldBoard);
		changes.clear();
		board.playMove(move, getChanges(), false);
		if (!board.isGameOver() && !getBoard().canMove()) {
			// if next player can't play and game is not over, swap players again
			getBoard().nextPlayer();
		}
	}
	
	public AbstractBoard getPreviousBoard() {
		if (boardHistory.size() == 0) {
			return null;
		} else {
			return boardHistory.get(boardHistory.size() - 1);
		}
	}
	
	public boolean canUndo() {
		int i = 1;
		while (boardHistory.size() >= i) {
			AbstractBoard oldBoard = boardHistory.get(boardHistory.size() - i);
			Player currentPlayer = oldBoard.getCurrentPlayer();
			if (currentPlayer.equals(settings.getPlayer())) {
				return true;
			}
			i++;
		}
		return false;
	}
	
	/**
	 * Perform an undo operation to go back to the last board state for the <b>human</b> player. Once this operation is
	 * complete, the board state will have changed as well as the current player if an undo operation was initiated when
	 * it was the computer's turn to play. If there are not enough boards in the board history to perform an undo,
	 * nothing happens.
	 */
	public void undo() {
		int i = 1;
		boolean wasGameOver = board.isGameOver();
		while (boardHistory.size() >= i) {
			AbstractBoard oldBoard = boardHistory.get(boardHistory.size() - i);
			Player currentPlayer = oldBoard.getCurrentPlayer();
			if (currentPlayer.equals(settings.getPlayer())) {
				board.copy(oldBoard);
				// remove this, and later boards, from the board history
				for (int j = 1; j <= i; j++) {
					AbstractBoard unusedBoard = boardHistory.get(boardHistory.size() - 1);
					gameContext.checkInBoard(unusedBoard);
					boardHistory.remove(unusedBoard);
				}
				if (wasGameOver) {
					setStatusMessage("");
				}
				return;
			}
			i++;
		}
	}
	
	/** Setup the searcher and move ranker for the game. */
	protected abstract void setupSearcherAndMoveRanker();
	
	/** Game initialiser, called before each game is started. */
	protected abstract void initialise();
	
	/** Get a string representation of the winner of the game. */
	public abstract String getWinner();
}
