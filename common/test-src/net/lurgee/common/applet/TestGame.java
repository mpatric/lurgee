/*
 * @(#)TestGame.java		2007/06/12
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import net.lurgee.sgf.GameContext;

/**
 * Concrete subclass of {@link AbstractGame} for unit tests.
 * @author mpatric
 */
public class TestGame extends AbstractGame {

	public TestGame(GameContext gameContext, Settings settings) {
		super(gameContext, settings);
	}

	@Override
	public String getWinner() {
		return null;
	}

	@Override
	protected void initialise() {
	}

	@Override
	protected void setupSearcherAndMoveRanker() {
	}
}
