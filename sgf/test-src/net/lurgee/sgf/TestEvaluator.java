/*
 * @(#)TestEvaluator.java		2005/11/16
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Evaluator used for unit tests. Returns scores according to the trees below, which are the same contrived examples
 * implemented by {@link TestBoard}, which determines the valid moves for each board state. The numbers shown at the ends
 * of each branch are the leaves, which are the scores as determined by this evaluator. So, the score when evaluating
 * the board in state H for player 1 is -1, board in state I for player 2 is -4 and so on.
 * <p/>
 * Test board 1:<br/>
 * <img src="testtree1.png"/>
 * <p/>
 * Test board 2, with byes not allowed:<br/>
 * <img src="testtree2.png"/>
 * <p/>
 * Test board 2, with byes allowed:<br/>
 * <img src="testtree3.png"/>
 * 
 * @author mpatric
 */
public class TestEvaluator implements Evaluator {

	public int score(AbstractBoard startBoard, AbstractBoard board, int currentDepth, int searchDepth) {
		Player player = board.getCurrentPlayer();
		int score = calculateScore(board, player);
		Debug.output(currentDepth + 1, "EVALUATION score = " + score + " player = " + player);
		return score;
	}

	private int calculateScore(AbstractBoard board, Player player) {
		int positionValue = ((TestBoard) board).getState();
		int multiplier = 0;
		if (TestPlayer.getInstance(1).equals(player)) {
			multiplier = -1;
		} else if (TestPlayer.getInstance(2).equals(player)) {
			multiplier = 1;
		}
		switch (positionValue) {
			// For iterative-deepening - nodes at level less than 3
			case 'B':
				return multiplier * 1;
			case 'C':
				return multiplier * 7;
			case 'D':
				return multiplier * 2;
			case 'E':
				return multiplier * 1;
			case 'F':
				return multiplier * 6;
			case 'G':
				return multiplier * -3;
			// Normal 'leaf' nodes
			case 'H':
				return multiplier * 7;
			case 'I':
				return multiplier * 6;
			case 'J':
				return multiplier * 3;
			case 'K':
				return multiplier * -2;
			case 'L':
				return multiplier * 0;
			case 'M':
				return multiplier * 8;
			case 'N':
				return multiplier * 6;
			case 'O':
				return multiplier * -4;
			case 'P':
				return multiplier * 8;
			case 'Q':
				return multiplier * 9;
			// For byes - alternative nodes
			case 'R':
				return multiplier * 5;
			case 'S':
				return multiplier * 6;
			case 'T':
				return multiplier * 7;
		}
		return 0;
	}
}
