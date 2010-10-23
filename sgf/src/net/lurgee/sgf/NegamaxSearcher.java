/*
 * @(#)NegamaxSearcher.java		2006/11/01
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.List;

/**
 * Class for generating and searching a game tree to find the best move using a negamax algorithm with alpha-beta
 * pruning.
 * @author mpatric
 */
public class NegamaxSearcher extends AbstractSinglePassSearcher {

	private static final int INFINITY = 100000;
	/**
	 * Constructor.
	 * @param gameContext A game context object.
	 * @param library A move library for selecting library moves.
	 * @param evaluator An evaluator, used to evaluate leaf node values.
	 * @param abCutoff Are we using alpha-beta cutoff?
	 * @param byeAllowed Should the search continue when there are board positions where a player cannot play?
	 */
	public NegamaxSearcher(GameContext gameContext, Library library, Evaluator evaluator, boolean abCutoff, boolean byeAllowed) {
		super(gameContext, library, evaluator, abCutoff, byeAllowed);
	}

	protected Move findMoveBySearch(AbstractBoard board, MoveRanker moveRanker, int depth, long evaluationThreshold) throws SearchException, RuntimeException {
		bestMove = null;
		depthReached = 0;
		notifyListenerOfIterationStart();
		int maxMovesLeft = board.countMaxMovesLeft();
		if (maxMovesLeft > 0) {
			int d = depth;
			if (d > maxMovesLeft) {
				d = maxMovesLeft;
			}
			search(board, board, moveRanker, d, d, -INFINITY, INFINITY, evaluationThreshold);
		}
		notifyListenerOfIterationEnd(false, bestMove, bestMoveScore);
		return bestMove;
	}

	/**
	 * Recursive method that implements the negamax algorithm.
	 * @param startBoard The board before any moves were made.
	 * @param board The current board.
	 * @param depth The maximum depth that this search is being done to.
	 * @param d Maximum depth minus the current depth (so when d is 0, maximum depth is reached).
	 * @param alpha Alpha value for alpha-beta cutoff.
	 * @param beta Beta value for alpha-beta cutoff.
	 * @param evaluationThreshold The number of evaluations at which the search is ended.
	 * @return The best score for the current state of the board, playing moves down to the allowed depth.
	 * @throws AbortException The abort flag was set to end the search prematurely.
	 * @throws SearchThresholdReachedException The search was ended as the evaluation threshold was reached.
	 * @throws RuntimeException Fatal error.
	 */
	protected int search(AbstractBoard startBoard, AbstractBoard board, MoveRanker moveRanker, int depth, int d, int alpha, int beta, long evaluationThreshold) throws SearchException, RuntimeException {
		if (aborted) {
			throw new AbortException(MSG_SEARCHER_ABORTED);
		}
		if (depth > 1 && evaluationThreshold != NO_EVALUATION_THRESHOLD) {
			if (evaluationCount >= evaluationThreshold) {
				notifyListenerOfIterationEnd(true, null, 0);
				throw new SearchThresholdReachedException(MSG_SEARCHER_THRESHOLD_REACHED + depth);
			}
		}
		int score = 0;
		Player player = ((AbstractBoard) board).getCurrentPlayer();
		if (d == 0) {
			// leaf node
			score = evaluator.score(startBoard, board, depth, depth);
			notifyListenerOfLeafEvaluation(score, player, depth, d);
		} else {
			if (!board.canMove()) {
				score = noMoves(startBoard, board, moveRanker, depth, d, alpha, beta, evaluationThreshold);
			} else {
				List<Move> moves = board.getValidMoves(moveRanker, depth - d + 1);
				int bestScore = alpha;
				int actualBestScore = -INFINITY;
				int b = beta;
				int bestCount = 0;
				int count = 0;
				for (Move moveToPlay : moves) {
					// copy board and make move
					AbstractBoard newBoard = (AbstractBoard) gameContext.checkOutBoard();
					newBoard.copy(board);
					newBoard.playMove(moveToPlay, null, true);
					notifyListenerOfBranch(moveToPlay, newBoard, player, depth, d);
					try {
						score = searchChildNode(startBoard, newBoard, moveRanker, depth, d, alpha, beta, b, (count == 0), evaluationThreshold);
						notifyListenerOfNodeEvaluation(moveToPlay, -score, player, depth, d);
						if (moveRanker != null) {
							notifyMoveRankerOfNodeEvaluation(moveToPlay, board, score, player, depth, d, moveRanker);
						}
					} finally {
						gameContext.checkInBoard(newBoard);
					}
					newBoard = null;
					if (score > actualBestScore) {
						actualBestScore = score;
					}
					if (score == bestScore) {
						bestCount++;
					} else if (score > bestScore) {
						bestScore = score;
						bestCount = 1;
						if (d == depth) {
							this.bestMove = moveToPlay;
							this.bestMoveScore = bestScore;
						}
					}
					if (abCutoff) {
						if (bestScore > alpha) {
							alpha = bestScore; // adjust search window
						}
						if (alpha >= beta) {
							// cutoff
							bestScore = alpha;
							break;
						}
					}
					b = alpha + 1; // set new null window
					count++;
				}
				score = actualBestScore;
			}
		}
		if (depth - d > depthReached) {
			depthReached = depth - d;
		}
		return score;
	}
	
	protected int searchChildNode(AbstractBoard startBoard, AbstractBoard board, MoveRanker moveRanker, int depth, int d, int alpha, int beta, int b, boolean firstChild, long evaluationThreshold) {
		return -(search(startBoard, board, moveRanker, depth, d - 1, -beta, -alpha, evaluationThreshold));
	}

	private int noMoves(AbstractBoard startBoard, AbstractBoard board, MoveRanker moveRanker, int depth, int d, int alpha, int beta, long evaluationThreshold) {
		int score;
		Player player = ((AbstractBoard) board).getCurrentPlayer();
		if (byeAllowed) {
			// call search again for next depth - must copy board so as not to modify passed in board
			AbstractBoard newBoard = (AbstractBoard) gameContext.checkOutBoard();
			newBoard.copy(board);
			notifyListenerOfBranch(null, newBoard, player, depth, d);
			newBoard.nextPlayer();
			try {
				score = -(search(startBoard, newBoard, moveRanker, depth, d - 1, -beta, -alpha, evaluationThreshold));
			} finally {
				gameContext.checkInBoard(newBoard);
			}
			notifyListenerOfNodeEvaluation(null, -score, player, depth, d);
		} else {
			// end here as a bye is not allowed, so this is a leaf node
			score = evaluator.score(startBoard, board, depth - d, depth);
			notifyListenerOfLeafEvaluation(score, player, depth, d);
		}
		return score;
	}
	
	private void notifyListenerOfIterationStart() {
		if (searchProgressListeners != null) {
			for (int i = 0; i < searchProgressListeners.length; i++) {
				searchProgressListeners[i].onIterationStart(iteration);
			}
		}
	}
	
	private void notifyListenerOfIterationEnd(boolean thresholdReached, Move move, int score) {
		if (searchProgressListeners != null) {
			for (int i = 0; i < searchProgressListeners.length; i++) {
				searchProgressListeners[i].onIterationEnd(iteration , move, score, depthReached, thresholdReached);
			}
		}
	}
	
	private void notifyListenerOfBranch(Move move, AbstractBoard board, Player player, int depth, int d) {
		if (searchProgressListeners != null) {
			for (int i = 0; i < searchProgressListeners.length; i++) {
				searchProgressListeners[i].onBranch(move, board, player, depth - d + 1);
			}
		}
	}
	
	private void notifyListenerOfNodeEvaluation(Move move, int score, Player player, int depth, int d) {
		if (searchProgressListeners != null) {
			for (int i = 0; i < searchProgressListeners.length; i++) {
				searchProgressListeners[i].onNodeEvaluation(move, score, player, depth - d + 1);
			}
		}
	}
	
	private void notifyListenerOfLeafEvaluation(int score, Player player, int depth, int d) {
		evaluationCount++;
		if (searchProgressListeners != null) {
			for (int i = 0; i < searchProgressListeners.length; i++) {
				searchProgressListeners[i].onLeafEvaluation(score, player, depth - d);
			}
		}
	}
	
	private void notifyMoveRankerOfNodeEvaluation(Move move, AbstractBoard board, int score, Player player, int depth, int d, MoveRanker moveRanker) {
		if (!orderOfMovesIsImportant || d == 1) {
			moveRanker.onNodeEvaluation(move, board, score, player, depth - d + 1);
		}
	}
	
	@Override
	public String toString() {
		return "Negamax";
	}
}
