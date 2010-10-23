/*
 * @(#)NineMensMorrisGame.java		2007/03/08
 *
 * Part of the ninemensmorris console application that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 * 
 */

package net.lurgee.ninemensmorris.console;

import java.util.ArrayList;

import net.lurgee.common.console.AbstractCompetitor;
import net.lurgee.common.console.AbstractGame;
import net.lurgee.common.console.ComputerCompetitor;
import net.lurgee.common.console.GameStats;
import net.lurgee.ninemensmorris.Colour;
import net.lurgee.ninemensmorris.NineMensMorrisBoard;
import net.lurgee.ninemensmorris.NineMensMorrisEvaluator;
import net.lurgee.ninemensmorris.NineMensMorrisMoveFactory;
import net.lurgee.ninemensmorris.NineMensMorrisMoveRanker;
import net.lurgee.ninemensmorris.NineMensMorrisPlayer;
import net.lurgee.sgf.AbstractSearcher;
import net.lurgee.sgf.Evaluator;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Library;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Main class for the ninemensmorris console application.
 * @author mpatric
 */
public class NineMensMorrisGame extends AbstractGame {

	private static final NineMensMorrisPlayer WHITE_PLAYER = NineMensMorrisPlayer.getInstance(Colour.WHITE);
	private static final NineMensMorrisPlayer BLACK_PLAYER = NineMensMorrisPlayer.getInstance(Colour.BLACK);
	private static final String NA = "n/a";
	
	/** Game stats for the games played. */
	private GameStats gameStats = new GameStats();
	
	protected String getGameName() {
		return "Nine Men's Morris";
	}

	protected void init() {
		Player[] nineMensMorrisPlayers = new Player[] {WHITE_PLAYER, BLACK_PLAYER};
		ObjectPool nineMensMorrisBoardPool = new ObjectPool(NineMensMorrisBoard.class);
		NineMensMorrisMoveFactory nineMensMorrisMoveFactory = new NineMensMorrisMoveFactory();
		gameContext = new GameContext(nineMensMorrisPlayers, nineMensMorrisBoardPool, nineMensMorrisMoveFactory, true);
		board = gameContext.checkOutBoard();
		addCompetitor(configureCompetitor("WHITE", Colour.WHITE));
		addCompetitor(configureCompetitor("BLACK", Colour.BLACK));
	}
	
	protected void done() {
		gameContext.checkInBoard(board);
	}
	
	protected void printCurrentState() {
		output.println((System.currentTimeMillis() - startTime) + "ms");
		output.println(board.toString());
	}
	
	protected void printEndGameState(AbstractCompetitor startCompetitor) {
		printCurrentState();
		Player winner = ((NineMensMorrisBoard) board).getWinner();
		printResults(winner);
		AbstractCompetitor whiteCompetitor = getCompetitor(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		AbstractCompetitor blackCompetitor = getCompetitor(NineMensMorrisPlayer.getInstance(Colour.BLACK));
		int startColour = ((NineMensMorrisPlayer) startCompetitor.getPlayer()).getColour();
		gameStats.addStat(((NineMensMorrisPlayer) whiteCompetitor.getPlayer()), startColour == Colour.WHITE, winner == null ? false : ((NineMensMorrisPlayer) winner).getColour() == Colour.WHITE, whiteCompetitor.getMovesPlayed());
		gameStats.addStat(((NineMensMorrisPlayer) blackCompetitor.getPlayer()), startColour == Colour.BLACK, winner == null ? false : ((NineMensMorrisPlayer) winner).getColour() == Colour.BLACK, blackCompetitor.getMovesPlayed());
	}

	/**
	 * Print the results for a single game and update the game stats. 
	 * @param winner The player that won the game.
	 */
	private void printResults(Player winner) {
		if (winner == NineMensMorrisPlayer.getInstance(Colour.WHITE)) {
			printResult(getCompetitor(NineMensMorrisPlayer.getInstance(Colour.WHITE)));
		} else if (winner == NineMensMorrisPlayer.getInstance(Colour.BLACK)) {
			printResult(getCompetitor(NineMensMorrisPlayer.getInstance(Colour.BLACK)));
		} else {
			output.println("Stalemate");
		}
	}
	
	/**
	 * Print the result of the game as a formatted string.
	 * @param winner The competitor that won the game.
	 */
	private void printResult(AbstractCompetitor winner) {
		String winnerText = winner.getPlayer() + " (" + winner.getPlayer().getSymbol() + ")"; 
		output.println(winnerText + " wins");
	}
	
	/**
	 * Print stats for the games.
	 * @param startTime The time in milliseconds when the series of games started.
	 * @param endTime The time in milliseconds when the series of games ended.
	 */
	protected void printStats(long startTime, long endTime) {
		AbstractCompetitor whiteCompetitor = getCompetitor(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		AbstractCompetitor blackCompetitor = getCompetitor(NineMensMorrisPlayer.getInstance(Colour.BLACK));
		NineMensMorrisPlayer whitePlayer = (NineMensMorrisPlayer) whiteCompetitor.getPlayer();
		NineMensMorrisPlayer blackPlayer = (NineMensMorrisPlayer) blackCompetitor.getPlayer();
		int totalGames = gameStats.getGameCount();
		output.println();
		printStat("STATS", NineMensMorrisPlayer.getInstance(Colour.WHITE).getName() + " (" + NineMensMorrisPlayer.getInstance(Colour.WHITE).getSymbol() + ")", NineMensMorrisPlayer.getInstance(Colour.BLACK).getName() + " (" + NineMensMorrisPlayer.getInstance(Colour.BLACK).getSymbol() + ")");		
		printStat("Total wins", valueAndPercentage(gameStats.countWins(whitePlayer), totalGames), 
				valueAndPercentage(gameStats.countWins(blackPlayer), totalGames));
		printStat("Wins when starting", valueAndPercentage(gameStats.countStartWins(whitePlayer), totalGames), 
				valueAndPercentage(gameStats.countStartWins(blackPlayer), totalGames));
		printStat("Moves considered", gameStats.getMovesConsidered(whitePlayer), gameStats.getMovesConsidered(blackPlayer));
		printStat("Moves discarded", gameStats.getMovesConsideredInIncompleteIterations(whitePlayer), gameStats.getMovesConsideredInIncompleteIterations((blackPlayer)));
		printStat("Moves played", Integer.toString(gameStats.countMovesPlayed(whitePlayer)), Integer.toString(gameStats.countMovesPlayed(blackPlayer))); 
		printStat("Evaluations done", gameStats.getEvaluationsDone(whitePlayer), gameStats.getEvaluationsDone(blackPlayer));
		printStat("Evaluations discarded", gameStats.getEvaluationsDoneInIncompleteIterations(whitePlayer), gameStats.getEvaluationsDoneInIncompleteIterations(blackPlayer));
		printStat("Search depths", gameStats.getSearchDepths(whitePlayer), gameStats.getSearchDepths(blackPlayer));
		printStat("Searcher", whiteCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) whiteCompetitor).getSearcher().toString() : NA,
				blackCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) blackCompetitor).getSearcher().toString() : NA);
		printStat("Evaluator", whiteCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) whiteCompetitor).getEvaluator().toString() : NA,
				blackCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) blackCompetitor).getEvaluator().toString() : NA);
		printStat("Tree depth", whiteCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) whiteCompetitor).getThinker().getDepth() + "" : NA,
				blackCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) blackCompetitor).getThinker().getDepth() + "" : NA);
		printStat("Games played", totalGames + "", "");
		printStat("Time taken", (endTime - startTime) + " ms", "");
	}

	/**
	 * Print a single stat from the game as a formatted string.
	 * @param message The message to print in front of the stat.
	 * @param whiteValue The value of the stat for the red competitor.
	 * @param blackValue The value of the stat for the yellow competitor.
	 */
	private void printStat(String message, String whiteValue, String blackValue) {
		output.printAndPad(message, 24);
		output.printAndPad(whiteValue, 21);
		output.printAndPad(blackValue, 21);
		output.println();
	}
	
	/**
	 * Create a formatted string containing a value and the percentage it represents of a given total.
	 * @param value The value.
	 * @param total The total which will be used to determine the percentage, i.e. 100 * (value / total).
	 * @return The formatted string.
	 */
	private String valueAndPercentage(int value, int total) {
		return value + " (" + ((100 * value) / total) + "%)";
	}
	
	/**
	 * Configure a competitor based on input from stdin.
	 * @param playerName Player name to display.
	 * @param colour Colour of player ({@link Colour#WHITE} or {@link Colour#BLACK}).
	 * @return A newly created competitor instance.
	 */
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
		NineMensMorrisPlayer player = NineMensMorrisPlayer.getInstance(colour);
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
			// create competitor
			Evaluator evaluator;
			if (useAlternativeEvaluator) evaluator = new AlternativeEvaluator();
			else evaluator = new NineMensMorrisEvaluator();
			Library library = null;
			MoveRanker moveRanker = new NineMensMorrisMoveRanker();
			competitor = new ComputerCompetitor(gameContext, moveRanker, evaluator, library, player, searchProgressListener, useNegamax, useKillerHeuristic, useIterativeDeepening, false);
			((ComputerCompetitor) competitor).setEvaluationThreshold(evaluationThreshold);
			((ComputerCompetitor) competitor).setTreeDepth(searchDepth);
		} else {
			competitor = new NineMensMorrisHumanCompetitor(gameContext, player);
		}
		return competitor;
	}

	/** Main method - program entry point for ninemensmorris console version. */
	public static void main(String[] args) {
		NineMensMorrisGame nineMensMorris = new NineMensMorrisGame();
		nineMensMorris.run();
	}
}
