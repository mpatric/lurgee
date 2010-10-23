/*
 * @(#)PlayerStats.java		2007/11/22
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import java.util.ArrayList;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Debug;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.SearchProgressListener;

/**
 * Collects and provides statistics for a single user for games played.
 * 
 * @author mpatric
 */
public class PlayerStats implements SearchProgressListener {

	private final int maxDepth;

	protected ArrayList<ResultStats> results = new ArrayList<ResultStats>();
	private int movesConsidered = 0;
	private int evaluationsDone = 0;
	private int movesConsideredInIncompleteIterations = 0;
	private int evaluationsDoneInIncompleteIterations = 0;
	private int movesConsideredInCurrentIteration;
	private int evaluationsDoneInCurrentIteration;
	private ArrayList<Integer> completeSearchDepths = new ArrayList<Integer>();
	private Node gameTree;
	private Node currentNode;

	public PlayerStats(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public void addResult(ResultStats resultStats) {
		results.add(resultStats);
	}

	public int getGameCount() {
		return results.size();
	}

	public int getMovesConsidered() {
		return movesConsidered;
	}

	public int getEvaluationsDone() {
		return evaluationsDone;
	}

	public int getMovesConsideredInIncompleteIterations() {
		return movesConsideredInIncompleteIterations;
	}

	public int getEvaluationsDoneInIncompleteIterations() {
		return evaluationsDoneInIncompleteIterations;
	}

	public String getCompleteSearchDepths() {
		StringBuilder sb = new StringBuilder();
		if (completeSearchDepths.size() > 0) {
			for (int i = getMinCompleteSearchDepth(); i <= getMaxCompleteSearchDepth(); i++) {
				int total = 0;
				for (Integer searchDepth : completeSearchDepths) {
					if (searchDepth.equals(i)) {
						total++;
					}
				}
				if (total > 0) {
					sb.append(i).append(":").append(total).append(" ");
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	private int getMinCompleteSearchDepth() {
		int minSearchDepth = 100;
		for (Integer searchDepth : completeSearchDepths) {
			if (searchDepth < minSearchDepth) {
				minSearchDepth = searchDepth;
			}
		}
		return minSearchDepth == 100 ? 0 : minSearchDepth;
	}

	private int getMaxCompleteSearchDepth() {
		int maxSearchDepth = 0;
		for (Integer searchDepth : completeSearchDepths) {
			if (searchDepth > maxSearchDepth) {
				maxSearchDepth = searchDepth;
			}
		}
		return maxSearchDepth;
	}

	public int countWins() {
		int wins = 0;
		for (ResultStats resultStats : results) {
			if (resultStats.isWinner()) {
				wins++;
			}
		}
		return wins;
	}

	public int countStartWins() {
		int startWins = 0;
		for (ResultStats resultStats : results) {
			if (resultStats.isWinner() && resultStats.isStarter()) {
				startWins++;
			}
		}
		return startWins;
	}

	public int countMovesPlayed() {
		int movesPlayed = 0;
		for (ResultStats resultStats : results) {
			movesPlayed += resultStats.getMovesPlayed();
		}
		return movesPlayed;
	}

	public void onIterationStart(int iteration) {
		Debug.output(0, "ITERATION " + iteration + " START");
		movesConsideredInCurrentIteration = 0;
		evaluationsDoneInCurrentIteration = 0;
		gameTree = new Node(null, null, Integer.MIN_VALUE);
		currentNode = gameTree;
	}

	public void onIterationEnd(int iteration, Move move, int score, int depth, boolean thresholdReached) {
		if (thresholdReached) {
			Debug.output(0, "ITERATION " + iteration + " THRESHOLD REACHED");
			movesConsidered -= movesConsideredInCurrentIteration;
			evaluationsDone -= evaluationsDoneInCurrentIteration;
			movesConsideredInIncompleteIterations += movesConsideredInCurrentIteration;
			evaluationsDoneInIncompleteIterations += evaluationsDoneInCurrentIteration;
			completeSearchDepths.add(depth - 1);
		} else {
			if (iteration == maxDepth) {
				completeSearchDepths.add(depth);
				currentNode.move = move;
				currentNode.score = score;
			}
			Debug.output(0, "ITERATION " + iteration + " END move = " + move + " score = " + score);
			Node node = gameTree;
			int nodeScore = -score;
			// walk the tree
			StringBuilder sb = new StringBuilder();
			// find start branch
			for (Node childNode : node.getChildNodes()) {
				if (childNode.move == move) {
					node = childNode;
					break;
				}
			}
			while (node != null) {
				sb.append(node.getMove()).append("  ");
				Node nextNode = null;
				for (Node childNode : node.getChildNodes()) {
					if (childNode.getScore() == -nodeScore) {
						nextNode = childNode;
						nodeScore = -nodeScore;
						break;
					}
				}
				node = nextNode;
			}
			System.out.println("Best move score from: " + sb.toString());
		}
		
	}

	public void onBranch(Move move, AbstractBoard board, Player player,
			int depth) {
		Debug.output(depth + 1, "BRANCH move = " + move + " player = " + player
				+ " depth = " + depth);
		Node newNode = new Node(currentNode, move, Integer.MIN_VALUE);
		currentNode.addChildNode(newNode);
		currentNode = newNode;
		if (move != null) {
			movesConsidered++;
			movesConsideredInCurrentIteration++;
		}
	}

	public void onNodeEvaluation(Move move, int score, Player player, int depth) {
		Debug.output(depth + 1, "NODE move = " + move + " score = " + score
				+ " player = " + player + " depth = " + depth);
		if (currentNode.move != move) {
			throw new IllegalStateException(
					"Move for node should match current node!");
		}
		currentNode.setScore(score);
		currentNode = currentNode.getParent();
	}

	public void onLeafEvaluation(int score, Player player, int depth) {
		Debug.output(depth + 2, "LEAF score = " + score + " player = " + player
				+ " depth = " + depth);
		evaluationsDone++;
		evaluationsDoneInCurrentIteration++;
	}

	class Node {
		Move move;
		int score;
		ArrayList<Node> childNodes = new ArrayList<Node>();
		final Node parent;

		public Node(Node parent, Move move, int score) {
			this.parent = parent;
			this.move = move;
			this.score = score;
		}

		public Move getMove() {
			return move;
		}

		public void setMove(Move move) {
			this.move = move;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public Node getParent() {
			return parent;
		}

		public ArrayList<Node> getChildNodes() {
			return childNodes;
		}

		public void addChildNode(Node node) {
			childNodes.add(node);
		}
	}
}
