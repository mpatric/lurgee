/*
 * @(#)Connect4Game.java		2007/03/08
 *
 * Part of the connect4 console application that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 * 
 */

package net.lurgee.connect4.console;

import java.util.ArrayList;

import net.lurgee.common.console.AbstractCompetitor;
import net.lurgee.common.console.AbstractGame;
import net.lurgee.common.console.ComputerCompetitor;
import net.lurgee.common.console.GameStats;
import net.lurgee.connect4.Colour;
import net.lurgee.connect4.Connect4Board;
import net.lurgee.connect4.Connect4Evaluator;
import net.lurgee.connect4.Connect4Library;
import net.lurgee.connect4.Connect4MoveFactory;
import net.lurgee.connect4.Connect4MoveRanker;
import net.lurgee.connect4.Connect4Player;
import net.lurgee.sgf.AbstractSearcher;
import net.lurgee.sgf.Evaluator;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Library;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Main class for the connect4 console application.
 * @author mpatric
 */
public class Connect4Game extends AbstractGame {

	private static final Connect4Player RED_PLAYER = Connect4Player.getInstance(Colour.RED);
	private static final Connect4Player YELLOW_PLAYER = Connect4Player.getInstance(Colour.YELLOW);
	private static final String NA = "n/a";
	
	/** Game stats for the games played. */
	private GameStats gameStats = new GameStats();
	
	protected String getGameName() {
		return "Connect4";
	}

	protected void init() {
		Player[] connect4Players = new Player[] {RED_PLAYER, YELLOW_PLAYER};
		ObjectPool connect4BoardPool = new ObjectPool(Connect4Board.class);
		Connect4MoveFactory connect4MoveFactory = new Connect4MoveFactory();
		gameContext = new GameContext(connect4Players, connect4BoardPool, connect4MoveFactory, true);
		board = gameContext.checkOutBoard();
		addCompetitor(configureCompetitor("RED", Colour.RED));
		addCompetitor(configureCompetitor("YELLOW", Colour.YELLOW));
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
		Player winner = ((Connect4Board) board).getWinner();
		printResults(winner);
		AbstractCompetitor redCompetitor = getCompetitor(Connect4Player.getInstance(Colour.RED));
		AbstractCompetitor yellowCompetitor = getCompetitor(Connect4Player.getInstance(Colour.YELLOW));
		int startColour = ((Connect4Player) startCompetitor.getPlayer()).getColour();
		gameStats.addStat(((Connect4Player) redCompetitor.getPlayer()), startColour == Colour.RED, winner == null ? false : ((Connect4Player) winner).getColour() == Colour.RED, redCompetitor.getMovesPlayed());
		gameStats.addStat(((Connect4Player) yellowCompetitor.getPlayer()), startColour == Colour.YELLOW, winner == null ? false : ((Connect4Player) winner).getColour() == Colour.YELLOW, yellowCompetitor.getMovesPlayed());
	}

	/**
	 * Print the results for a single game and update the game stats. 
	 * @param winner The player that won the game.
	 */
	private void printResults(Player winner) {
		if (winner == Connect4Player.getInstance(Colour.RED)) {
			printResult(getCompetitor(Connect4Player.getInstance(Colour.RED)));
		} else if (winner == Connect4Player.getInstance(Colour.YELLOW)) {
			printResult(getCompetitor(Connect4Player.getInstance(Colour.YELLOW)));
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
		AbstractCompetitor redCompetitor = getCompetitor(Connect4Player.getInstance(Colour.RED));
		AbstractCompetitor yellowCompetitor = getCompetitor(Connect4Player.getInstance(Colour.YELLOW));
		Connect4Player redPlayer = (Connect4Player) redCompetitor.getPlayer();
		Connect4Player yellowPlayer = (Connect4Player) yellowCompetitor.getPlayer();
		int totalGames = gameStats.getGameCount();
		output.println();
		printStat("STATS", Connect4Player.getInstance(Colour.RED).getName() + " (" + Connect4Player.getInstance(Colour.RED).getSymbol() + ")", Connect4Player.getInstance(Colour.YELLOW).getName() + " (" + Connect4Player.getInstance(Colour.YELLOW).getSymbol() + ")");		
		printStat("Total wins", valueAndPercentage(gameStats.countWins(redPlayer), totalGames), 
				valueAndPercentage(gameStats.countWins(yellowPlayer), totalGames));
		printStat("Wins when starting", valueAndPercentage(gameStats.countStartWins(redPlayer), totalGames), 
				valueAndPercentage(gameStats.countStartWins(yellowPlayer), totalGames));
		printStat("Moves considered", gameStats.getMovesConsidered(redPlayer), gameStats.getMovesConsidered(yellowPlayer));
		printStat("Moves discarded", gameStats.getMovesConsideredInIncompleteIterations(redPlayer), gameStats.getMovesConsideredInIncompleteIterations((yellowPlayer)));
		printStat("Moves played", Integer.toString(gameStats.countMovesPlayed(redPlayer)), Integer.toString(gameStats.countMovesPlayed(yellowPlayer))); 
		printStat("Evaluations done", gameStats.getEvaluationsDone(redPlayer), gameStats.getEvaluationsDone(yellowPlayer));
		printStat("Evaluations discarded", gameStats.getEvaluationsDoneInIncompleteIterations(redPlayer), gameStats.getEvaluationsDoneInIncompleteIterations(yellowPlayer));
		printStat("Search depths", gameStats.getSearchDepths(redPlayer), gameStats.getSearchDepths(yellowPlayer));
		printStat("Searcher", redCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) redCompetitor).getSearcher().toString() : NA,
				yellowCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) yellowCompetitor).getSearcher().toString() : NA);
		printStat("Evaluator", redCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) redCompetitor).getEvaluator().toString() : NA,
				yellowCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) yellowCompetitor).getEvaluator().toString() : NA);
		printStat("Tree depth", redCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) redCompetitor).getThinker().getDepth() + "" : NA,
				yellowCompetitor instanceof ComputerCompetitor ? ((ComputerCompetitor) yellowCompetitor).getThinker().getDepth() + "" : NA);
		printStat("Games played", totalGames + "", "");
		printStat("Time taken", (endTime - startTime) + " ms", "");
	}

	/**
	 * Print a single stat from the game as a formatted string.
	 * @param message The message to print in front of the stat.
	 * @param redValue The value of the stat for the red competitor.
	 * @param yellowValue The value of the stat for the yellow competitor.
	 */
	private void printStat(String message, String redValue, String yellowValue) {
		output.printAndPad(message, 24);
		output.printAndPad(redValue, 21);
		output.printAndPad(yellowValue, 21);
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
	 * @param colour Colour of player ({@link Colour#RED} or {@link Colour#YELLOW}).
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
		Connect4Player player = Connect4Player.getInstance(colour);
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
			else evaluator = new Connect4Evaluator();
			Library library = new Connect4Library((GameContext) gameContext);
			MoveRanker moveRanker = new Connect4MoveRanker();
			competitor = new ComputerCompetitor(gameContext, moveRanker, evaluator, library, player, searchProgressListener, useNegamax, useKillerHeuristic, useIterativeDeepening, false);
			((ComputerCompetitor) competitor).setEvaluationThreshold(evaluationThreshold);
			((ComputerCompetitor) competitor).setTreeDepth(searchDepth);
		} else {
			competitor = new Connect4HumanCompetitor(gameContext, player);
		}
		return competitor;
	}

	/** Main method - program entry point for connect4 console version. */
	public static void main(String[] args) {
		Connect4Game connect4 = new Connect4Game();
		connect4.run();
	}
}
