/*
 * @(#)Reversi.java		2005/11/19
 *
 * Part of the reversi console application that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.console;

import java.util.ArrayList;

import net.lurgee.common.console.AbstractCompetitor;
import net.lurgee.common.console.AbstractGame;
import net.lurgee.common.console.ComputerCompetitor;
import net.lurgee.reversi.Colour;
import net.lurgee.reversi.ReversiBoard;
import net.lurgee.reversi.ReversiEvaluator;
import net.lurgee.reversi.ReversiLibrary;
import net.lurgee.reversi.ReversiMoveFactory;
import net.lurgee.reversi.ReversiMoveRanker;
import net.lurgee.reversi.ReversiPlayer;
import net.lurgee.sgf.AbstractSearcher;
import net.lurgee.sgf.Evaluator;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Library;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Main class for the reversi console application.
 * @author mpatric
 */
public class ReversiGame extends AbstractGame {
	
	private static final ReversiPlayer BLACK_PLAYER = ReversiPlayer.getInstance(Colour.BLACK);
	private static final ReversiPlayer WHITE_PLAYER = ReversiPlayer.getInstance(Colour.WHITE);
	private static final String NA = "N/A";
	
	private ReversiGameStats gameStats = new ReversiGameStats();
	
	protected String getGameName() {
		return "Reversi";
	}

	protected void init() {
		Player[] reversiPlayers = new Player[] {BLACK_PLAYER, WHITE_PLAYER};
		ObjectPool reversiBoardPool = new ObjectPool(ExtendedReversiBoard.class);
		ReversiMoveFactory reversiMoveFactory = new ReversiMoveFactory();
		gameContext = new GameContext(reversiPlayers, reversiBoardPool, reversiMoveFactory, true);
		board = gameContext.checkOutBoard();
		addCompetitor(configureCompetitor("BLACK", Colour.BLACK));
		addCompetitor(configureCompetitor("WHITE", Colour.WHITE));
	}
	
	protected void done() {
		gameContext.checkInBoard(board);
	}

	protected void printCurrentState() {
		output.print((System.currentTimeMillis() - startTime) + "ms"); 
		output.print("; Score: ");
		output.print(BLACK_PLAYER.getName() + " (" + BLACK_PLAYER.getSymbol() + "): " + ((ReversiBoard) board).getCount(Colour.BLACK));
		output.print(", " + WHITE_PLAYER.getName() + " (" + WHITE_PLAYER.getSymbol() + "): " + ((ReversiBoard) board).getCount(Colour.WHITE));
		output.println();
		output.println(board.toString());
	}

	protected void printEndGameState(AbstractCompetitor startCompetitor) {
		printCurrentState();
		int blackScore = ((ReversiBoard) board).getCount(Colour.BLACK);
		int whiteScore = ((ReversiBoard) board).getCount(Colour.WHITE);
		printResults(blackScore, whiteScore);
		AbstractCompetitor blackCompetitor = getCompetitor(BLACK_PLAYER);
		AbstractCompetitor whiteCompetitor = getCompetitor(WHITE_PLAYER);
		int startColour = ((ReversiPlayer) startCompetitor.getPlayer()).getColour();
		int opening = blackCompetitor instanceof ComputerCompetitor ? ((ReversiLibrary)((ComputerCompetitor) blackCompetitor).getLibrary()).getOpeningPlayed() : ReversiLibrary.NONE;
		gameStats.addStat(((ReversiPlayer) blackCompetitor.getPlayer()), startColour == Colour.BLACK, blackScore > whiteScore, blackCompetitor.getMovesPlayed(), blackScore, blackScore + whiteScore < 64, opening);
		opening = whiteCompetitor instanceof ComputerCompetitor ? ((ReversiLibrary)((ComputerCompetitor) whiteCompetitor).getLibrary()).getOpeningPlayed() : ReversiLibrary.NONE;
		gameStats.addStat(((ReversiPlayer) whiteCompetitor.getPlayer()), startColour == Colour.WHITE, whiteScore > blackScore, whiteCompetitor.getMovesPlayed(), whiteScore, blackScore + whiteScore < 64, opening);
	}

	private void printResults(int blackScore, int whiteScore) {
		boolean early = (blackScore + whiteScore < 64);
		if (blackScore > whiteScore) {
			printResult(getCompetitor(BLACK_PLAYER), early);
		} else if (blackScore < whiteScore) {
			printResult(getCompetitor(WHITE_PLAYER), early);
		} else {
			output.print("Draw");
		}
	}
	
	private void printResult(AbstractCompetitor winner, boolean early) {
		String winnerText = winner.getPlayer() + " (" + winner.getPlayer().getSymbol() + ")"; 
		output.print(winnerText + " wins");
		if (early) output.println(" early");
		else output.println();
	}

	protected void printStats(long startTime, long endTime) {
		AbstractCompetitor blackCompetitor = getCompetitor(BLACK_PLAYER);
		AbstractCompetitor whiteCompetitor = getCompetitor(WHITE_PLAYER);
		ReversiPlayer blackPlayer = ((ReversiPlayer) blackCompetitor.getPlayer());
		ReversiPlayer whitePlayer = ((ReversiPlayer) whiteCompetitor.getPlayer());
		int totalGames = gameStats.getGameCount();
		output.println();
		printStat("STATS", BLACK_PLAYER.getName() + " (" + BLACK_PLAYER.getSymbol() + ")", WHITE_PLAYER.getName() + " (" + WHITE_PLAYER.getSymbol() + ")");		
		printStat("Total wins", valueAndPercentage(gameStats.countWins(blackPlayer), totalGames), 
				valueAndPercentage(gameStats.countWins(whitePlayer), totalGames));
		printStat("Wins when starting", valueAndPercentage(gameStats.countStartWins(blackPlayer), totalGames), 
				valueAndPercentage(gameStats.countStartWins(whitePlayer), totalGames));
		printStat("Early wins", Integer.toString(gameStats.countEarlyWins(blackPlayer)),
				Integer.toString(gameStats.countEarlyWins(whitePlayer)));
		printStat("Average score", Float.toString(gameStats.calculateAverageScore(blackPlayer)),
				Float.toString(gameStats.calculateAverageScore(whitePlayer)));
		printStat("Parallel openings", Integer.toString(gameStats.countOpenings(blackPlayer, ReversiLibrary.PARALLEL)),
				Integer.toString(gameStats.countOpenings(whitePlayer, ReversiLibrary.PARALLEL)));
		printStat("Diagonal openings", Integer.toString(gameStats.countOpenings(blackPlayer, ReversiLibrary.DIAGONAL)),
				Integer.toString(gameStats.countOpenings(whitePlayer, ReversiLibrary.DIAGONAL)));
		printStat("Perpendicular openings", Integer.toString(gameStats.countOpenings(blackPlayer, ReversiLibrary.PERPENDICULAR)),
				Integer.toString(gameStats.countOpenings(whitePlayer, ReversiLibrary.PERPENDICULAR)));
		printStat("Moves considered", gameStats.getMovesConsidered(blackPlayer), gameStats.getMovesConsidered(whitePlayer));  
		printStat("Moves discarded", gameStats.getMovesConsideredInIncompleteIterations(blackPlayer), gameStats.getMovesConsideredInIncompleteIterations(whitePlayer));
		printStat("Moves played", Integer.toString(gameStats.countMovesPlayed(blackPlayer)), Integer.toString(gameStats.countMovesPlayed(whitePlayer)));
		printStat("Evaluations done", gameStats.getEvaluationsDone(blackPlayer), gameStats.getEvaluationsDone(whitePlayer));
		printStat("Evaluations discarded", gameStats.getEvaluationsDoneInIncompleteIterations(blackPlayer), gameStats.getEvaluationsDoneInIncompleteIterations(whitePlayer));
		printStat("Search depths", gameStats.getSearchDepths(blackPlayer), gameStats.getSearchDepths(whitePlayer)); 
		printStat("Searcher", blackCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) blackCompetitor).getSearcher().toString() : NA,
				whiteCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) whiteCompetitor).getSearcher().toString() : NA);
		printStat("Evaluator", blackCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) blackCompetitor).getEvaluator().toString() : NA,
				whiteCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) whiteCompetitor).getEvaluator().toString() : NA);
		printStat("Tree depth", blackCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) blackCompetitor).getThinker().getDepth() + "" : NA,
				whiteCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) whiteCompetitor).getThinker().getDepth() + "" : NA);
		printStat("Games played", totalGames + "", "");
		printStat("Time taken", (endTime - startTime) + " ms", "");
		printStat("Scores", gameStats.listScores(blackPlayer, whitePlayer), "");
	}

	private void printStat(String message, String blackValue, String whiteValue) {
		output.printAndPad(message, 24);
		output.printAndPad(blackValue, 21);
		output.printAndPad(whiteValue, 21);
		output.println();
	}
	
	private String valueAndPercentage(int value, int total) {
		return value + " (" + ((100 * value) / total) + "%)";
	}
	
	private AbstractCompetitor configureCompetitor(String playerName, int colour) {
		AbstractCompetitor competitor = null;
		ArrayList<String> list = new ArrayList<String>();
		list.add("Computer (default)");
		list.add("Human");
		int competitorType = input.selectFromList(playerName, list, 1);
		int searchDepth = 0;
		if (competitorType == 1) {	
			searchDepth = (int) input.enterInteger("Search depth (5)", 5, 1, 16);
		}
		ReversiPlayer player = ReversiPlayer.getInstance(colour);
		SearchProgressListener searchProgressListener = gameStats.registerPlayer(player, searchDepth);
		if (competitorType == 1) {
			list.clear();
			list.add("Negascout (default)");
			list.add("Negamax");
			boolean useNegamax = (input.selectFromList("Searcher to use", list, 1) != 1);
			list.clear();
			list.add("Yes (default)");
			list.add("No");
			boolean useKillerHeuristic = (input.selectFromList("Use killer move heuristic", list, 1) == 1);
			list.clear();
			list.add("Yes (default)");
			list.add("No");
			boolean useIterativeDeepening = (input.selectFromList("Iterative deeping on search", list, 1) == 1);
			long evaluationThreshold = 0;
			if (useIterativeDeepening) {
				evaluationThreshold = input.enterInteger("Iterative deepening evaluation threshold [" + AbstractSearcher.NO_EVALUATION_THRESHOLD + " = none] (" + AbstractSearcher.NO_EVALUATION_THRESHOLD + ")", AbstractSearcher.NO_EVALUATION_THRESHOLD, 0, 10000000);
			}
			list.clear();
			list.add("Standard (default)");
			list.add("Alternative");
			boolean useAlternativeEvaluator = (input.selectFromList("Evaluator to use", list, 1) != 1);
			list.clear();
			list.add("Weighted random (default)");
			list.add("Parallel");
			list.add("Diagonal");
			list.add("Perpendicular");
			int useOpening = input.selectFromList("Library opening to use", list, 1) - 2;
			// create competitor
			Evaluator evaluator;
			if (useAlternativeEvaluator) {
				evaluator = new AlternativeEvaluator();
			} else {
				evaluator = new ReversiEvaluator();
			}
			Library library = new ReversiLibrary((GameContext) gameContext, useOpening);
			MoveRanker moveRanker = new ReversiMoveRanker();
			competitor = new ComputerCompetitor(gameContext, moveRanker, evaluator, library, player, searchProgressListener, useNegamax, useKillerHeuristic, useIterativeDeepening, true);
			((ComputerCompetitor) competitor).setEvaluationThreshold(evaluationThreshold);
			((ComputerCompetitor) competitor).setTreeDepth(searchDepth);
		} else {
			competitor = new ReversiHumanCompetitor(gameContext, player);
		}
		return competitor;
	}

	public static void main(String[] args) {
		ReversiGame reversi = new ReversiGame();
		reversi.run();
	}
}
