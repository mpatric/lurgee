/*
 * @(#)NineMensMorrisGame.java		2008/03/08
 *
 * Part of the ninemensmorris applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris.applet;

import net.lurgee.common.applet.AbstractGame;
import net.lurgee.common.applet.Settings;
import net.lurgee.ninemensmorris.Colour;
import net.lurgee.ninemensmorris.NineMensMorrisBoard;
import net.lurgee.ninemensmorris.NineMensMorrisEvaluator;
import net.lurgee.ninemensmorris.NineMensMorrisMoveFactory;
import net.lurgee.ninemensmorris.NineMensMorrisMoveRanker;
import net.lurgee.ninemensmorris.NineMensMorrisPlayer;
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
public class NineMensMorrisGame extends AbstractGame {
	
	public NineMensMorrisGame(GameContext gameContext, Settings settings) {
		super(gameContext, settings);
	}
	
	public void initialise() {
		getBoard().setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
	}
	
	public String getWinner() {
		Player winner = ((NineMensMorrisBoard) getBoard()).getWinner();
		if (winner == null) {
			return null;
		}
		return winner.toString();
	}
	
	protected void setupSearcherAndMoveRanker() {
		Evaluator evaluator = new NineMensMorrisEvaluator();
		NegascoutSearcher negascoutSearcher = new NegascoutSearcher(gameContext, null, evaluator, false);
		IterativeSearcher iterativeSearcher = new IterativeSearcher(negascoutSearcher, null);
		setSearcher(iterativeSearcher);
		NineMensMorrisMoveRanker ninemensmorrisMoveRanker = new NineMensMorrisMoveRanker();
		KillerHeuristicMoveRanker killerHeuristicMoveRanker = new KillerHeuristicMoveRanker(ninemensmorrisMoveRanker, AppletConsts.KILLER_MOVES_PER_LEVEL);
		setMoveRanker(killerHeuristicMoveRanker);
	}
	
	@Override
	protected void postDetermineMove() {
		((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).clear();
	}
}
