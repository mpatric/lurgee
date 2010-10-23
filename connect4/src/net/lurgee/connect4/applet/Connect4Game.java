/*
 * @(#)Connect4Game.java		2007/06/11
 *
 * Part of the connect4 applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4.applet;

import net.lurgee.common.applet.AbstractGame;
import net.lurgee.common.applet.Settings;
import net.lurgee.connect4.Colour;
import net.lurgee.connect4.Connect4Board;
import net.lurgee.connect4.Connect4Evaluator;
import net.lurgee.connect4.Connect4Library;
import net.lurgee.connect4.Connect4MoveRanker;
import net.lurgee.connect4.Connect4Player;
import net.lurgee.sgf.Evaluator;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.IterativeSearcher;
import net.lurgee.sgf.KillerHeuristicMoveRanker;
import net.lurgee.sgf.NegascoutSearcher;
import net.lurgee.sgf.Player;

/**
 * Container for all game-related entities related to sgf and the connect-four implementation classes.
 * @author mpatric
 */
public class Connect4Game extends AbstractGame {
	
	public Connect4Game(GameContext gameContext, Settings settings) {
		super(gameContext, settings);
	}
	
	public void initialise() {
		getBoard().setCurrentPlayer(Connect4Player.getInstance(Colour.RED));
	}
	
	public String getWinner() {
		Player winner = ((Connect4Board) getBoard()).getWinner();
		if (winner == null) {
			return null;
		}
		return winner.toString();
	}
	
	protected void setupSearcherAndMoveRanker() {
		Evaluator evaluator = new Connect4Evaluator();
		Connect4Library library = new Connect4Library((GameContext) gameContext);
		NegascoutSearcher negascoutSearcher = new NegascoutSearcher(gameContext, library, evaluator, false);
		IterativeSearcher iterativeSearcher = new IterativeSearcher(negascoutSearcher, library);
		setSearcher(iterativeSearcher);
		Connect4MoveRanker connect4MoveRanker = new Connect4MoveRanker();
		KillerHeuristicMoveRanker killerHeuristicMoveRanker = new KillerHeuristicMoveRanker(connect4MoveRanker, AppletConsts.KILLER_MOVES_PER_LEVEL);
		setMoveRanker(killerHeuristicMoveRanker);
	}
}
