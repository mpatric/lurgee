/*
 * @(#)Connect4Board.java		2007/03/07
 *
 * Part of the connect4 common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveList;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.Position;

/**
 * Connect-four board. It is is a 6x7 board; each square on the board may be empty, or may contain a red piece or a
 * yellow piece.
 * <p/>
 * Squares on the board are identified by x and y co-ordinates, which are one-based (so x in 1..7 and y in 1..6).
 * @author mpatric
 */
public class Connect4Board extends AbstractBoard {

	private static final int TOTAL_POSSIBLE_WINNING_GROUPS = 69;
	private static final int[][][] SQUARE_TO_WINNING_GROUP = {
		{{1, 25, 49}, {1, 2, 28, 52}, {1, 2, 3, 31, 55}, {1, 2, 3, 4, 34, 57, 58}, {2, 3, 4, 37, 60}, {3, 4, 40, 63}, {4, 43, 66}},
		{{5, 25, 26, 47}, {5, 6, 28, 29, 49, 50}, {5, 6, 7, 31, 32, 52, 53, 58}, {5, 6, 7, 8, 34, 35, 55, 56, 59, 60}, {6, 7, 8, 37, 38, 57, 62, 63}, {7, 8, 40, 41, 65, 66}, {8, 43, 44, 68}},
		{{9, 25, 26, 27, 46}, {9, 10, 28, 29, 30, 47, 48, 58}, {9, 10, 11, 31, 32, 33, 49, 50, 51, 59, 60}, {9, 10, 11, 12, 34, 35, 36, 52, 53, 54, 61, 62, 63}, {10, 11, 12, 37, 38, 39, 55, 56, 64, 65, 66}, {11, 12, 40, 41, 42, 57, 67, 68}, {12, 43, 44, 45, 69}},
		{{13, 25, 26, 27, 58}, {13, 14, 28, 29, 30, 46, 59, 60}, {13, 14, 15, 31, 32, 33, 47, 48, 61, 62, 63}, {13, 14, 15, 16, 34, 35, 36, 49, 50, 51, 64, 65, 66}, {14, 15, 16, 37, 38, 39, 52, 53, 54, 67, 68}, {15, 16, 40, 41, 42, 55, 56, 69}, {16, 43, 44, 45, 57}},
		{{17, 26, 27, 59}, {17, 18, 29, 30, 61, 62}, {17, 18, 19, 32, 33, 46, 64, 65}, {17, 18, 19, 20, 35, 36, 47, 48, 67, 68}, {18, 19, 20, 38, 39, 50, 51, 69}, {19, 20, 41, 42, 53, 54}, {20, 44, 45, 56}},
		{{21, 27, 61}, {21, 22, 30, 64}, {21, 22, 23, 33, 67}, {21, 22, 23, 24, 36, 46, 69}, {22, 23, 24, 39, 48}, {23, 24, 42, 51}, {24, 45, 54}}
	};
	private static Connect4Position[][] WINNING_GROUP_TO_SQUARE = new Connect4Position[TOTAL_POSSIBLE_WINNING_GROUPS][];
	
	private static final int BROKEN_GROUP = -1;
	private static final String MSG_PIECE_ALREADY_SET = "Piece already set";
	private static final int INITIAL_MOVE_LIST_CAPACITY = 7;
	
	public static final int X_DIMENSION = 7;
	public static final int Y_DIMENSION = 6;

	/**
	 * A 1x6 array representing the state of the squares on the board. Each element in the array represents a row on
	 * the board, with each group of 2 bits in the element representing a square in that row, starting with the 2 least
	 * significant bits (first column). They are stored like this for performance reasons.
	 * <p/>
	 * The values are:
	 * <ul>
	 * 	<li>{@link Colour#NONE} - the square is empty;</li>
	 * 	<li>{@link Colour#RED} - the square contains a red piece;</li>
	 * 	<li>{@link Colour#YELLOW} - the square contains a yellow piece;</li>
	 * 	<li>{@link Colour#ANY} - undefined.</li>
	 * </ul>
	 */
	private final int squares[] = new int[Y_DIMENSION];
	
	/** The index of the top piece in each column. */ 
	private final int tops[] = new int[X_DIMENSION];
	
	/** Count of the number of pieces on the board. */
	private int piecesPlayed = 0;
	
	/** A count of the number of pieces each player has in each possible winning group, set to {@link #BROKEN_GROUP} if the group is broken by an opponent piece. */
	private final int unbrokenGroupsOfFourStatus[][] = {new int[TOTAL_POSSIBLE_WINNING_GROUPS], new int[TOTAL_POSSIBLE_WINNING_GROUPS]};
	
	/** Stored so it does not need to be calculated every time {@link #getWinningGroup() getWinningGroup} is called. */
	protected transient Connect4Position[] winningGroup = null;

	protected Player winner = null;
	
	/** Static initialiser - populates {@link #WINNING_GROUP_TO_SQUARE} table. */
	static {
		for (int group = 1; group <= TOTAL_POSSIBLE_WINNING_GROUPS; group++) {
			ArrayList<Connect4Position> positionsForGroup = new ArrayList<Connect4Position>();
			for (int y = 1; y <= Y_DIMENSION; y++) {
				for (int x = 1; x <= X_DIMENSION; x++) {
					int groupsForSquare[] = SQUARE_TO_WINNING_GROUP[y - 1][x - 1];
					for (int groupForSquare : groupsForSquare) {
						if (groupForSquare == group) {
							positionsForGroup.add(new Connect4Position(x, y));
						}
					}
				}
			}
			WINNING_GROUP_TO_SQUARE[group - 1] = new Connect4Position[positionsForGroup.size()];
			WINNING_GROUP_TO_SQUARE[group - 1] = positionsForGroup.toArray(WINNING_GROUP_TO_SQUARE[group - 1]);
		}
	}

	/**
	 * @deprecated Connect4Board instances should be obtained from a GameContext. 
	 */
	@Deprecated
	public Connect4Board() {
		super();
	}

	public void initialise() {
		clear();
		setCurrentPlayer(Connect4Player.getInstance(Colour.RED));
	}

	@Override
	protected void clear() {
		super.clear();
		for (int i = 0; i < Y_DIMENSION; i++) {
			squares[i] = Colour.NONE;
		}
		for (int i = 0; i < X_DIMENSION; i++) {
			tops[i] = 0;
		}
		piecesPlayed = 0;
		winner = null;
		winningGroup = null;
		for (int i = 0; i < TOTAL_POSSIBLE_WINNING_GROUPS; i++) {
			unbrokenGroupsOfFourStatus[0][i] = 0;
			unbrokenGroupsOfFourStatus[1][i] = 0;
		}
	}
	
	public Player getWinner() {
		return winner;
	}

	/**
	 * Get the squares for one line of the board.
	 * @param y Y-coordinate of the square (1-6).
	 * @return The bit field representing a row on the board.
	 */
	public int getSquares(int y) {
		return squares[y - 1];
	}

	/**
	 * Get the state of a square at the specified position on the board.
	 * @param x X-coordinate of the square (1-7).
	 * @param y Y-coordinate of the square (1-6).
	 * @return
	 * <ul>
	 * 	<li>{@link Colour#NONE} if the square is empty;</li>
	 * 	<li>{@link Colour#RED} if the square contains a red piece;</li>
	 * 	<li>{@link Colour#YELLOW} if the square contains a yellow piece.</li>
	 * </ul>
	 */
	public int getSquare(int x, int y) {
		if (x < 1 || x > X_DIMENSION) {
			throw new IndexOutOfBoundsException();
		}
		return (squares[y - 1] >> (2 * (x - 1))) & 3;
	}
	
	public int getTop(int x) {
		return tops[x - 1];
	}

	protected void setPiece(int x, int y, int colour, boolean updateWinningGroup) {
		if (((squares[y - 1] >> (2 * (x - 1))) & 3) != Colour.NONE) {
			throw new IllegalStateException(MSG_PIECE_ALREADY_SET);
		}
		//squares[y - 1] &= ~(3 << (2 * (x - 1))); // unset the bits
		squares[y - 1] |= (colour << (2 * (x - 1))); // set the bits for the specified colour
		piecesPlayed++;
		updateTops(x, y);
		updateUnbrokenGroupCompletions(x, y, colour);
	}

	private void updateTops(int x, int y) {
		tops[x - 1] = y;
	}
	
	private void updateUnbrokenGroupCompletions(int x, int y, int colour) {
		int[] groups = SQUARE_TO_WINNING_GROUP[y - 1][x - 1];
		for (int group : groups) {
			int playerCountForGroup = unbrokenGroupsOfFourStatus[colour - 1][group - 1];
			if (playerCountForGroup != BROKEN_GROUP) {
				unbrokenGroupsOfFourStatus[colour - 1][group - 1]++;
			}
			int opponentCountForGroup = unbrokenGroupsOfFourStatus[2 - colour][group - 1];
			if (opponentCountForGroup != BROKEN_GROUP) {
				// this group is no longer an unbroken one for the opponent
				unbrokenGroupsOfFourStatus[2 - colour][group - 1] = BROKEN_GROUP;
			}
		}
	}

	public boolean isValidMove(int x) {
		if (gameOver || getTop(x) >= Y_DIMENSION) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("  1 2 3 4 5 6 7\n");
		for (int y = Y_DIMENSION; y >= 1; y--) {
			buffer.append(y).append(" ");
			for (int x = 1; x <= X_DIMENSION; x++) {
				int squareColour = getSquare(x, y);
				if (squareColour != Colour.NONE) {
					buffer.append(Connect4Player.getInstance(squareColour).getSymbol()).append(' ');
				} else {
					buffer.append("- ");
				}
			}
			if (y > 1) buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public void fromString(String boardAsString) {
		clear();
		int x = 1;
		int y = 1;
		String[] lines = boardAsString.split("\n");
		for (int i = lines.length - 1; i >= 0; i--) {
			for (int j = 2; j < lines[i].length(); j += 2) {
				String piece = lines[i].substring(j, j + 2);
				if (Connect4Player.getInstance(Colour.RED).getSymbol() == piece.charAt(0)) {
					setPiece(x, y, Colour.RED, true);
				} else if (Connect4Player.getInstance(Colour.YELLOW).getSymbol() == piece.charAt(0)) {
					setPiece(x, y, Colour.YELLOW, true);
				}
				x++;
				if (x > X_DIMENSION) {
					x = 1;
					y++;
				}
			}
		}
	}

	@Override
	public void copy(AbstractBoard board) {
		if (! (board instanceof Connect4Board)) {
			throw new IllegalArgumentException("Cannot copy " + board.getClass().getName() + " to " + getClass().getName());
		}
		super.copy(board);
		Connect4Board connect4Board = (Connect4Board) board;
		for (int i = 0; i < Y_DIMENSION; i++) {
			squares[i] = connect4Board.squares[i];
		}
		for (int i = 0; i < X_DIMENSION; i++) {
			tops[i] = connect4Board.tops[i];
		}
		piecesPlayed = connect4Board.piecesPlayed;
		winner = connect4Board.winner;
		winningGroup = connect4Board.winningGroup;
		for (int i = 0; i < TOTAL_POSSIBLE_WINNING_GROUPS; i++) {
			unbrokenGroupsOfFourStatus[0][i] = connect4Board.unbrokenGroupsOfFourStatus[0][i];
			unbrokenGroupsOfFourStatus[1][i] = connect4Board.unbrokenGroupsOfFourStatus[1][i];
		}
	}

	public boolean canMove() {
		if (gameOver) {
			return false;
		}
		for (int x = 1; x <= X_DIMENSION; x++) {
			if (isValidMove(x)) {
				return true;
			}
		}
		return false;
	}

	public int makeMove(Move move, List<Position> changes, boolean searching) {
		int x = ((Connect4Move) move).getX();
		int colour = ((Connect4Player) getCurrentPlayer()).getColour();
		for (int y = 1; y <= Y_DIMENSION; y++) {
			if (getSquare(x, y) == Colour.NONE) {
				setPiece(x, y, colour, !searching);
				if (changes != null) {
					changes.add(new Connect4Position(x, y));
				}
				if (countMaxMovesLeft() == 0) {
					gameOver = true;
				}
				if (countUnbrokenGroupsOfFour(colour, 4) > 0) {
					gameOver = true;
					winner = getCurrentPlayer();
				}
				return 1;
			}
		}
		return 0;
	}

	@Override
	public List<Move> getValidMoves(MoveRanker moveRanker, int depth) {
		if (gameOver) {
			return Collections.emptyList();
		}
		MoveList moves = gameContext.createMoveList(INITIAL_MOVE_LIST_CAPACITY);
		for (int x = 1; x <= X_DIMENSION; x++) {
			if (isValidMove(x)) {
				Connect4Move connect4Move = ((Connect4MoveFactory) gameContext.getMoveFactory()).createMove(x); 
				moves.add(connect4Move, this, depth, moveRanker);
			}
		}
		return moves.getMoves();
	}

	public int countMaxMovesLeft() {
		return (X_DIMENSION * Y_DIMENSION) - piecesPlayed;
	}

	public int countMovesMade() {
		return piecesPlayed;
	}
	
	public Connect4Position[] getWinningGroup() {
		if (winner != null) {
			if (winningGroup != null) {
				return winningGroup;
			}
			int colour = ((Connect4Player) winner).getColour();
			for (int group = 1; group <= TOTAL_POSSIBLE_WINNING_GROUPS; group++) {
				if (unbrokenGroupsOfFourStatus[colour - 1][group - 1] == 4) {
					return WINNING_GROUP_TO_SQUARE[group - 1];
				}
			}
		}
		return null;
	}

	public int countUnbrokenGroupsOfFour(int colour, int filledSquares) {
		int total = 0;
		for (int i = 0; i < TOTAL_POSSIBLE_WINNING_GROUPS; i++) {
			if (unbrokenGroupsOfFourStatus[colour - 1][i] == filledSquares) {
				total++;
			}
		}
		return total;
	}
	
	@Override
	public void poolableRecycle(Object params) throws RuntimeException {
		super.poolableRecycle(params);
		winner = null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		final Connect4Board other = (Connect4Board) obj;
		if (winner == null) {
			if (other.winner != null) return false;
		} else if (!winner.equals(other.winner)) return false;
		if (piecesPlayed != other.piecesPlayed) return false;
		if (!Arrays.equals(tops, other.tops)) return false;
		if (!Arrays.equals(unbrokenGroupsOfFourStatus[0], other.unbrokenGroupsOfFourStatus[0])) return false;
		if (!Arrays.equals(unbrokenGroupsOfFourStatus[1], other.unbrokenGroupsOfFourStatus[1])) return false;
		if (!Arrays.equals(squares, other.squares)) return false;
		return true;
	}
}
