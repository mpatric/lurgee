/*
 * @(#)Thinker.java		2007/06/09
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import net.lurgee.common.awt.MainWindow;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Runnable class that performs computer searches for best move in a thread.
 * @author mpatric
 */
public class Thinker implements Runnable, SearchProgressListener, Animatable {
	
	protected static final int MILLIS_PER_TICK = 50;
	protected static final int ANIMATION_MILLIS_PER_TICK = 25;
	protected static final int ANIMATION_SPEED = 5;
	
	private int lastProgressState = 0;
	private final String[] progressStrings;
	private final String thinkingMessage;
	private Animator animator = new Animator(this, ANIMATION_MILLIS_PER_TICK);
	private int animationWait;
	private Thread thread = null;
	private boolean aborting;
	protected final AbstractGame game;
	protected final MainWindow mainWindow;
	private int depthReached;
	private int evaluations;
	private int usefulEvaluations;
	
	public Thinker(MainWindow mainWindow, AbstractGame game, String thinkingMessage, String[] progressStrings) {
		this.mainWindow = mainWindow;
		this.game = game;
		this.thinkingMessage = thinkingMessage;
		this.progressStrings = progressStrings;
	}
	
	private void updateSearchingMessage() {
		lastProgressState++;
		if (lastProgressState >= progressStrings.length) {
			lastProgressState = 0;
		}
		game.setStatusMessage(thinkingMessage + progressStrings[lastProgressState]);
	}

	public void start() {
		synchronized (this) {
			if (thread == null) {
				aborting = false;
				animator.start();
				thread = new Thread(this);
				thread.start();
			}
		}
	}

	public void end() {
		animator.abort();
		// wait for thinker thread to end		
		while (thread != null) {
			try {
				synchronized (this) {
					wait(MILLIS_PER_TICK);
				}
			} catch (InterruptedException e) {
				// do nothing
			}
		}
	}

	public void abort() {
		synchronized (this) {
			aborting = true;
			game.abortSearches();
			animator.abort();
			end();
		}
	}

	public void run() {
		Move move = null;
		depthReached = 0;
		evaluations = 0;
		usefulEvaluations = 0;
		int bestMoveScore = 0;
		move = game.determineMove();
		bestMoveScore = game.getSearcher().getBestMoveScore();
		if (move != null) {
			System.out.print("Best move " + move + " (score " + bestMoveScore + "), ");
			System.out.println("search depth " + depthReached + ", did " + usefulEvaluations + " useful evaluations (of " + evaluations + ")");
		}
		synchronized (this) {
			if (game.lastSearchWasAborted()) {
				move = null;
			}
			mainWindow.postWidgetEvent(Event.PLAY_MOVE, move);
		}
		thread = null;
		synchronized (this) {
			// only call end() if thinker wasn't aborted (abort calls it otherwise)
			if (!aborting) {
				end();
			}
		}
	}
	
	public void onIterationStart(int iteration) {
	}
	
	public void onIterationEnd(int iteration, Move move, int score, int depth, boolean thresholdReached) {
		if (! thresholdReached) {
			usefulEvaluations = evaluations;
			depthReached = depth;
		}
	}
	
	public void onBranch(Move move, AbstractBoard board, Player player, int depth) {
	}
	
	public void onNodeEvaluation(Move move, int score, Player player, int depth) {
	}

	public void onLeafEvaluation(int score, Player player, int depth) {
		evaluations++;
	}

	public void abortAnimating() {
	}

	public boolean animate() {
		animationWait++;
		if (animationWait > ANIMATION_SPEED) {
			animationWait = 0;
			updateSearchingMessage();
			mainWindow.postWidgetEvent(Event.REFRESH_STATUS);
		}
		return true;
	}

	public void endAnimating() {
		if (!game.getBoard().isGameOver()) {
			game.setStatusMessage("");
			mainWindow.postWidgetEvent(Event.REFRESH_STATUS);
		}
	}

	public void startAnimating() {
		lastProgressState = progressStrings.length - 1;
		animationWait = ANIMATION_SPEED;
	}
}
