/*
 * @(#)AbstractGame.java		2007/04/21
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.lurgee.sgf.AbortException;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;

/**
 * Container for all game entities related to sgf and common elements of the game being implemented.
 * @author mpatric
 */
public abstract class AbstractGame {
	
	protected Input input;
	protected Output output;
	protected GameContext gameContext;
	private final List<AbstractCompetitor> competitors = new ArrayList<AbstractCompetitor>();
	protected AbstractBoard board = null;
	protected long startTime;
	
	public AbstractGame() {
		input = new Input();
		output = new Output();
	}
	
	/** Get the name of the game. Concrete subclasses must define this method. */
	protected abstract String getGameName();
	
	/** Called once before any games are played. Should setup everything required for the games to be played. */
	protected abstract void init();
	
	/** Called once after all the games have been played. Should cleanup after all the games have been played. */
	protected abstract void done();
	
	/** Print the current state of the game. Called after each move is made. */
	protected abstract void printCurrentState();
	
	/** Print the end state of the game. Called at the end of each game. */
	protected abstract void printEndGameState(AbstractCompetitor startCompetitor);
	
	/** Print the stats for all the games played. Called once after all the games have been played. */
	protected abstract void printStats(long startTime, long endTime);
	
	protected void addCompetitor(AbstractCompetitor competitor) {
		competitors.add(competitor);
	}
	
	protected AbstractCompetitor getCompetitor(Player player) {
		for (Iterator<AbstractCompetitor> iterator = competitors.iterator(); iterator.hasNext();) {
			AbstractCompetitor competitor = iterator.next();
			if (competitor.getPlayer() == player) return competitor;
		}
		throw new IllegalArgumentException("Invalid player for game");
	}

	protected void run() {
		output.println("Lurgee " + getGameName() + " " + net.lurgee.sgf.Version.VERSION);
		output.println();
		init();
		int gamesToPlay = 1;
		boolean switchStarter = false;
		gamesToPlay = (int) input.enterInteger("How many games (1)?", 1, 1, 100000);
		if (gamesToPlay > 1) {
			ArrayList<String> list = new ArrayList<String>();
			list.add("Yes (default)");
			list.add("No");
			switchStarter = (input.selectFromList("Switch the player that starts each game?", list, 1) == 1);
		}
		// games loop
		startTime = System.currentTimeMillis();
		AbstractCompetitor startCompetitor = competitors.get(0);
		for (int i = 0; i < gamesToPlay; i++) {
			playGame(startCompetitor);
			if (switchStarter) {
				startCompetitor = getNextCompetitor(startCompetitor);
			}
		}
		long endTime = System.currentTimeMillis();
		printStats(startTime, endTime);
		done();
	}

	private void playGame(AbstractCompetitor startCompetitor) {
		board.initialise();
		board.setCurrentPlayer(startCompetitor.getPlayer());
		for (AbstractCompetitor competitor : competitors) {
			competitor.setMovesPlayed(0);
		}
		while (!board.isGameOver()) {
			if (!board.canMove()) {
				board.nextPlayer();
			} else {
				printCurrentState();
				while (true) {
					Player player = board.getCurrentPlayer();
					Move move = null;
					AbstractCompetitor competitor = getCompetitor(player);
					try {
						move = competitor.determineMove(board);
					} catch (AbortException ibe) {
						System.out.println("MOVE ABORTED!");
						// do nothing
					}
					// make move
					int changes = board.playMove(move, null, false);
					if (changes > 0) {
						if (competitor instanceof ComputerCompetitor) {
							output.println(player + " (" + player.getSymbol() + ") plays " + move + " with score " + ((ComputerCompetitor) competitor).getLastMoveScore());
						} else {						
							output.println(player + " (" + player.getSymbol() + ") plays " + move);
						}
						competitor.setMovesPlayed(competitor.getMovesPlayed() + 1);
						break;
					}
					output.println("Invalid move" + move);
				}
			}
		}
		printEndGameState(startCompetitor);
	}

	protected AbstractCompetitor getNextCompetitor(AbstractCompetitor nextCompetitor) {
		if (competitors.size() == 0) {
			throw new IllegalStateException("No players defined");
		}
		int index = competitors.indexOf(nextCompetitor);
		if (index < 0) {
			throw new IllegalArgumentException("Invalid competitor for game");
		}
		if (index == competitors.size() - 1) {
			nextCompetitor = competitors.get(0);
		} else {
			nextCompetitor = competitors.get(index + 1);
		}
		return nextCompetitor;
	}
}
