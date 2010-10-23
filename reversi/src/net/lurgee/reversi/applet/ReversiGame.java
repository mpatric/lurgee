/*
 * @(#)ReversiGame.java		2006/02/06
 *
 * Part of the reversi applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.applet;

import net.lurgee.common.applet.AbstractGame;
import net.lurgee.common.applet.Settings;
import net.lurgee.reversi.Colour;
import net.lurgee.reversi.ReversiBoard;
import net.lurgee.reversi.ReversiEvaluator;
import net.lurgee.reversi.ReversiLibrary;
import net.lurgee.reversi.ReversiMoveRanker;
import net.lurgee.reversi.ReversiPlayer;
import net.lurgee.sgf.Evaluator;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.IterativeSearcher;
import net.lurgee.sgf.KillerHeuristicMoveRanker;
import net.lurgee.sgf.NegascoutSearcher;

/**
 * Container for all game-related entities related to sgf and the reversi implementation classes.
 * @author mpatric
 */
public class ReversiGame extends AbstractGame {

	public ReversiGame(GameContext gameContext, Settings settings) {
		super(gameContext, settings);
	}
	
	@Override
	public void initialise() {
		getBoard().setCurrentPlayer(ReversiPlayer.getInstance(Colour.BLACK));
	}
	
	@Override
	public String getWinner() {
		int blackScore = ((ReversiBoard) getBoard()).getCount(Colour.BLACK);
		int whiteScore = ((ReversiBoard) getBoard()).getCount(Colour.WHITE);
		if (blackScore > whiteScore) {
			return ReversiPlayer.getInstance(Colour.BLACK).toString();
		} else if (whiteScore > blackScore) {
			return ReversiPlayer.getInstance(Colour.WHITE).toString();
		}
		return null;
	}
	
	@Override
	protected void setupSearcherAndMoveRanker() {
		Evaluator evaluator = new ReversiEvaluator();
		ReversiLibrary library = new ReversiLibrary((GameContext) gameContext);
		NegascoutSearcher negascoutSearcher = new NegascoutSearcher(gameContext, library, evaluator, true);
		IterativeSearcher iterativeSearcher = new IterativeSearcher(negascoutSearcher, library);
		setSearcher(iterativeSearcher);
		ReversiMoveRanker reversiMoveRanker = new ReversiMoveRanker();
		KillerHeuristicMoveRanker killerHeuristicMoveRanker = new KillerHeuristicMoveRanker(reversiMoveRanker, AppletConsts.KILLER_MOVES_PER_LEVEL);
		setMoveRanker(killerHeuristicMoveRanker);
	}
}
