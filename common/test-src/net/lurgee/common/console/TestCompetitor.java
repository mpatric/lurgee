/*
 * @(#)TestCompetitor.java		2007/05/25
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.TestMove;

/**
 * Test competitor for unit tests.
 * @author mpatric
 */
public class TestCompetitor extends AbstractCompetitor {

	public TestCompetitor(GameContext gameContext, Player player) {
		super(gameContext, player);
	}

	public Move determineMove(AbstractBoard board) {
		return new TestMove('A');
	}
}
