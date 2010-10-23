/*
 * @(#)ReversiBoard.java		2005/11/02
 *
 * Part of the reversi common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi;

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
 * Reversi board. It is is a 8x8 square board; each square on the board may be empty, or may contain a black piece or a
 * white piece. The class has been optimised for performance - a table of adjacent squares is kept to help quickly
 * determine which squares represent valid moves for a particular colour.
 * <p/>
 * Squares on the board are identified by x and y co-ordinates, which are one-based (so each has the range 1 to 8).
 * @author mpatric
 */
public class ReversiBoard extends AbstractBoard implements ReversiDifferenceBoard {

	public static final int X_DIMENSION = 8;
	public static final int Y_DIMENSION = 8;
	
	private static final String MSG_PIECE_ALREADY_SET = "Piece already set";
	private static final int INITIAL_MOVE_LIST_CAPACITY = 10;

	/**
	 * A 1x8 array representing the state of the squares on the board. Each element in the array represents a row on
	 * the board, with each group of 2 bits in the element representing a square in that row, starting with the 2 least
	 * significant bits (first column). They are stored like this for performance reasons.
	 * <p/>
	 * The values are:
	 * <ul>
	 * 	<li>{@link Colour#NONE} - the square is empty;</li>
	 * 	<li>{@link Colour#BLACK} - the square contains a black piece;</li>
	 * 	<li>{@link Colour#WHITE} - the square contains a white piece;</li>
	 * 	<li>{@link Colour#ANY} - undefined.</li>
	 * </ul>
	 */
	protected final int squares[] = new int[Y_DIMENSION];
	private final int counts[] = {0, 0};
	private final int validCounts[] = {-1, -1};

	/**
	 * A 2x8 array representing the empty squares that are adjacent to black or white pieces. <i>Adjacent</i> means
	 * in a square next to that piece, vertically, horizontally or diagonally). The first array is for black pieces and
	 * the second for white. Each element in the array represents a row on the board, with each group of 8 bits
	 * representing a square in that row, starting with the 8 least significant bits (first column). Each set of bits
	 * represents the <u>directions</u> that the square is in with respect to the adjacent black or white piece using
	 * the direction constants defined in {@link Direction}.
	 */
	private final long adjacents[][] = {new long[Y_DIMENSION], new long[Y_DIMENSION]};
	private final int adjacentCounts[] = {-1, -1};
	
	/**
	 * @deprecated ReversiBoard instances should be obtained from a GameContext. 
	 */
	@Deprecated 
	public ReversiBoard() {
		super();
	}

	public void initialise() {
		clear();
		setPiece(4, 5, Colour.BLACK);
		setPiece(5, 4, Colour.BLACK);
		setPiece(4, 4, Colour.WHITE);
		setPiece(5, 5, Colour.WHITE);
		setCurrentPlayer(ReversiPlayer.getInstance(Colour.BLACK));
	}

	@Override
	protected void clear() {
		super.clear();
		for (int i = 0; i <= 7; i++) {
			squares[i] = 0;
			adjacents[0][i] = 0;
			adjacents[1][i] = 0;
		}
		counts[0] = 0;
		counts[1] = 0;
		validCounts[0] = -1;
		validCounts[1] = -1;
		adjacentCounts[0] = -1;
		adjacentCounts[1] = -1;
	}

	public int getSquares(int y) {
		return squares[y - 1];
	}

	public int getSquare(int x, int y) {
		if (x < 1 || x > X_DIMENSION) {
			throw new IndexOutOfBoundsException();
		}
		return (squares[y - 1] >> (2 * (x - 1))) & 3;
	}

	public int getCount(int colour) {
		if (colour < Colour.NONE || colour > Colour.ANY) {
			throw new IllegalArgumentException();
		}
		if (colour == Colour.ANY) {
			return (counts[Colour.BLACK - 1] + counts[Colour.WHITE - 1]);
		} else if (colour == Colour.NONE) {
			return (64 - counts[Colour.BLACK - 1] - counts[Colour.WHITE - 1]);
		} else {
			return counts[colour - 1];
		}
	}

	/**
	 * Counts how many empty squares there are on the board adjacent to the specified colour. Internally, the count is
	 * stored if it has been calculated and the stored value is returned for subsequent calls. The stored value needs
	 * to be recalculated when the board state changes.
	 * @param colour {@link Colour#BLACK} or {@link Colour#WHITE}.
	 * @return The count.
	 */
	public int getAdjacentCount(int colour) {
		// see if we've already calculated it
		if (adjacentCounts[colour - 1] >= 0) return adjacentCounts[colour - 1];
		// calculate it
		int adjcount = 0;
		for (int y = 0; y <= 7; y++) {
			if (adjacents[colour - 1][y] != 0) {
				for (int x = 0; x <= 7; x++) {
					if (((adjacents[colour - 1][y] >> (8 * (x))) & 255) != 0) {
						adjcount++;
					}
				}
			}
		}
		adjacentCounts[colour - 1] = adjcount;
		return adjcount;
	}
	
	/**
	 * Compare this board and another board and generate a delta of the two, which is a board containing only the
	 * squares that are empty on one of the boards and not empty on the other - all other squares are set to empty on
	 * the delta board.
	 * @param reversiBoard The board to compare to.
	 * @param differenceBoard Object for storing the difference board.
	 * @return A differential board, which can be used to examine the differences between this board and the supplied board.
	 */
	public ReversiDifferenceBoard compare(ReversiBoard reversiBoard, ReversiBoard differenceBoard) {
		differenceBoard.counts[0] = 0;
		differenceBoard.counts[1] = 0;
		differenceBoard.setCurrentPlayer(getCurrentPlayer());
		for (int y = 0; y <= 7; y++) {
			differenceBoard.squares[y] = (squares[y] ^ reversiBoard.squares[y]) & squares[y];
			if (differenceBoard.squares[y] != 0) {
				for (int x = 0; x <= 7; x++) {
					int colr = (differenceBoard.squares[y] >> (2 * x)) & 3;
					if (colr != 0) {
						differenceBoard.counts[colr - 1]++;
					}
				}
			}
		}
		return differenceBoard;
	}

	protected void setPiece(int x, int y, int colour) {
		if (((squares[y - 1] >> (2 * (x - 1))) & 3) != Colour.NONE) {
			throw new IllegalStateException(MSG_PIECE_ALREADY_SET);
		}
		//squares[y - 1] &= ~(3 << (2 * (x - 1))); // unset the bits
		squares[y - 1] |= (colour << (2 * (x - 1))); // set the bits for the specified colour
		counts[colour - 1]++;
		setAdjacents(x, y, colour);
	}

	private int flipPiece(int x, int y) {
		int colour = ((squares[y - 1] >> (2 * (x - 1))) & 3);
		if (colour < Colour.BLACK || colour > Colour.WHITE) {
			return Colour.NONE;
		}
		counts[colour - 1]--;
		colour = 3 - colour;
		// set piece
		squares[y - 1] &= ~(3 << (2 * (x - 1))); // unset the bits
		squares[y - 1] |= (colour << (2 * (x - 1))); // set the bits for the specified colour
		counts[colour - 1]++;
		// set adjacents for other colour as this piece has flipped!
		updateAdjacents(x, y, colour);
		return colour;
	}

	/**
	 * Update {@link #adjacents} for a newly placed piece. The specified square (which is now not empty) is cleared in
	 * {@link #adjacents} and each empty square around this square is updated to reflect it is adjacent to this newly
	 * placed piece.
	 * @param x X-coordinate of the square (1-8).
	 * @param y Y-coordinate of the square (1-8).
	 * @param colour {@link Colour#BLACK} or {@link Colour#WHITE}.
	 */
	protected void setAdjacents(int x, int y, int colour) {
		// set adjacents for a newly placed piece
		adjacents[0][y - 1] &= ~((long) (255) << (8 * (x - 1))); // unset the bits for (x,y) for black
		adjacents[1][y - 1] &= ~((long) (255) << (8 * (x - 1))); // unset the bits for (x,y) for white
		// look at squares around (x,y) and set those that are empty
		if (x > 1 && x < X_DIMENSION) {
			if (((squares[y - 1] >> (2 * (x))) & 3) == 0) {
				adjacents[colour - 1][y - 1] |= ((long) (Direction.LEFT) << (8 * (x)));
			}
			if (((squares[y - 1] >> (2 * (x - 2))) & 3) == 0) {
				adjacents[colour - 1][y - 1] |= ((long) Direction.RIGHT << (8 * (x - 2)));
			}
		}
		if (y > 1 && y < Y_DIMENSION) {
			if (((squares[y] >> (2 * (x - 1))) & 3) == 0) {
				adjacents[colour - 1][y] |= ((long) Direction.UP << (8 * (x - 1)));
			}
			if (((squares[y - 2] >> (2 * (x - 1))) & 3) == 0) {
				adjacents[colour - 1][y - 2] |= ((long) Direction.DOWN << (8 * (x - 1)));
			}
		}
		if (x > 1 && x < X_DIMENSION && y > 1 && y < X_DIMENSION) {
			if (((squares[y] >> (2 * (x))) & 3) == 0) {
				adjacents[colour - 1][y] |= ((long) Direction.UP_LEFT << (8 * (x)));
			}		
			if (((squares[y] >> (2 * (x - 2))) & 3) == 0) {
				adjacents[colour - 1][y] |= ((long) Direction.UP_RIGHT << (8 * (x - 2)));
			}		
			if (((squares[y - 2] >> (2 * (x - 2))) & 3) == 0) {
				adjacents[colour - 1][y - 2] |= ((long) Direction.DOWN_RIGHT << (8 * (x - 2)));
			}
			if (((squares[y - 2] >> (2 * (x))) & 3) == 0) {
				adjacents[colour - 1][y - 2] |= ((long) Direction.DOWN_LEFT << (8 * (x)));
			}
		}
	}

	/**
	 * Update {@link #adjacents} for a flipped piece. Each empty square around this square is cleared as an adjacent
	 * to the old colour and set to an adjacent to the new colour.
	 * @param x X-coordinate of the square (1-8).
	 * @param y Y-coordinate of the square (1-8).
	 * @param colour The new colour of the square ({@link Colour#BLACK} or {@link Colour#WHITE}).
	 */
	protected void updateAdjacents(int x, int y, int colour) {
		// unset bits around (x,y) for square with old colour
		if (x > 1 && x < X_DIMENSION) {
			adjacents[2 - colour][y - 1] &= ~((long) (Direction.LEFT) << (8 * (x)));
			adjacents[2 - colour][y - 1] &= ~((long) Direction.RIGHT << (8 * (x - 2)));
		}
		if (y > 1 && y < Y_DIMENSION) {
			adjacents[2 - colour][y] &= ~((long) Direction.UP << (8 * (x - 1)));
			adjacents[2 - colour][y - 2] &= ~((long) Direction.DOWN << (8 * (x - 1)));
		}
		if (x > 1 && x < X_DIMENSION && y > 1 && y < Y_DIMENSION) {
			adjacents[2 - colour][y] &= ~((long) Direction.UP_LEFT << (8 * (x)));
			adjacents[2 - colour][y] &= ~((long) Direction.UP_RIGHT << (8 * (x - 2)));
			adjacents[2 - colour][y - 2] &= ~((long) Direction.DOWN_RIGHT << (8 * (x - 2)));
			adjacents[2 - colour][y - 2] &= ~((long) Direction.DOWN_LEFT << (8 * (x)));
		}
		// look at squares around (x,y) and set those that are empty
		if (x > 1 && x < X_DIMENSION) {
			if (((squares[y - 1] >> (2 * (x))) & 3) == 0) {
				adjacents[colour - 1][y - 1] |= ((long) (Direction.LEFT) << (8 * (x)));
			}
			if (((squares[y - 1] >> (2 * (x - 2))) & 3) == 0) {
				adjacents[colour - 1][y - 1] |= ((long) Direction.RIGHT << (8 * (x - 2)));
			}
		}
		if (y > 1 && y < Y_DIMENSION) {
			if (((squares[y] >> (2 * (x - 1))) & 3) == 0) {
				adjacents[colour - 1][y] |= ((long) Direction.UP << (8 * (x - 1)));
			}
			if (((squares[y - 2] >> (2 * (x - 1))) & 3) == 0) {
				adjacents[colour - 1][y - 2] |= ((long) Direction.DOWN << (8 * (x - 1)));
			}
		}
		if (x > 1 && x < X_DIMENSION && y > 1 && y < X_DIMENSION) {
			if (((squares[y] >> (2 * (x))) & 3) == 0) {
				adjacents[colour - 1][y] |= ((long) Direction.UP_LEFT << (8 * (x)));
			}		
			if (((squares[y] >> (2 * (x - 2))) & 3) == 0) {
				adjacents[colour - 1][y] |= ((long) Direction.UP_RIGHT << (8 * (x - 2)));
			}		
			if (((squares[y - 2] >> (2 * (x - 2))) & 3) == 0) {
				adjacents[colour - 1][y - 2] |= ((long) Direction.DOWN_RIGHT << (8 * (x - 2)));
			}
			if (((squares[y - 2] >> (2 * (x))) & 3) == 0) {
				adjacents[colour - 1][y - 2] |= ((long) Direction.DOWN_LEFT << (8 * (x)));
			}
		}
	}

	private boolean isValidMove(int x, int y, int colour) {
		if (x < 1 || x > X_DIMENSION || y < 1 || y > Y_DIMENSION) {
			throw new IndexOutOfBoundsException();
		}
		if (colour != Colour.BLACK && colour != Colour.WHITE) {
			throw new IndexOutOfBoundsException();
		}
		if (gameOver) {
			return false;
		}
		long l = adjacents[2 - colour][y - 1] >> (8 * (x - 1));
		if ((l & 255) == 0) {
			return false; // not adjacent to opposite colour
		}
		if ((l & Direction.RIGHT) != 0 && traverseLine(x, y, colour, 1, 0, true, null) > 0) {
			return true;
		}
		if ((l & Direction.DOWN) != 0 && traverseLine(x, y, colour, 0, 1, true, null) > 0) {
			return true;
		}
		if ((l & Direction.LEFT) != 0 && traverseLine(x, y, colour, -1, 0, true, null) > 0) {
			return true;
		}
		if ((l & Direction.UP) != 0 && traverseLine(x, y, colour, 0, -1, true, null) > 0) {
			return true;	
		}
		if ((l & Direction.DOWN_RIGHT) != 0 && traverseLine(x, y, colour, 1, 1, true, null) > 0) {
			return true;
		}
		if ((l & Direction.DOWN_LEFT) != 0 && traverseLine(x, y, colour, -1, 1, true, null) > 0) {
			return true;	
		}
		if ((l & Direction.UP_LEFT) != 0 && traverseLine(x, y, colour, -1, -1, true, null) > 0) {
			return true;	
		}
		if ((l & Direction.UP_RIGHT) != 0 && traverseLine(x, y, colour, 1, -1, true, null) > 0) {
			return true;
		}
		return false;
	}
	
	public boolean isValidMove(int x, int y) {
		return isValidMove(x, y, ((ReversiPlayer) getCurrentPlayer()).getColour());
	}

	/**
	 * Traverse a line in the direction specified by dx and dy, flipping the pieces if fakeIt is false. If the flipped
	 * parameter is not null, {@link ReversiMove} objects representing the pieces that were flipped are added to it.
	 * @param x X-coordinate of starting square (1-8).
	 * @param y Y-coordinate of starting square (1-8).
	 * @param colour The colour of the piece being placed ({@link Colour#BLACK} or {@link Colour#WHITE}).
	 * @param dx Delta for x traversal (-1, 0 or 1).
	 * @param dy Delta for y traversal (-1, 0 or 1).
	 * @param fakeIt If true, the pieces are not actually flipped.
	 * @param flipped A container for adding the pieces that were flipped by this move. May be null.
	 * @return The number of pieces flipped (or that would be flipped).
	 */
	private int traverseLine(int x, int y, int colour, int dx, int dy, boolean fakeIt, List<Position> flipped) {
		int colr = (squares[y - 1] >> (2 * (x - 1))) & 3;
		if (colr != Colour.NONE) {
			return 0;
		}
		int ox = x + dx;
		int oy = y + dy;
		int state = 0; // 0 = started, 1 = on opponent colour, 2 = on own colour
		while (ox >= 1 && ox <= X_DIMENSION && oy >= 1 && oy <= Y_DIMENSION) {
			colr = (squares[oy - 1] >> (2 * (ox - 1))) & 3;
			if (colr == Colour.NONE) {
				break;
			} else if (colr != colour) {
				if (state == 0) {
					state = 1;
				}
			} else {
				if (state == 1) {
					state = 2;
				}
				break;
			}
			ox += dx;
			oy += dy;
		}
		if (state != 2) {
			return 0;
		}
		ox = x + dx;
		oy = y + dy;
		int flips = 0;
		while (ox >= 1 && ox <= X_DIMENSION && oy >= 1 && oy <= Y_DIMENSION) {
			colr = (squares[oy - 1] >> (2 * (ox - 1))) & 3;
			if (colr != colour) {
				flips++;
				if (flipped != null) {
					flipped.add(new ReversiPosition(ox, oy));
				}
				if (!fakeIt) {
					flipPiece(ox, oy);
				}
			} else {
				break;
			}
			ox += dx;
			oy += dy;
		}
		return flips;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("  a b c d e f g h\n");
		ReversiPlayer player = (ReversiPlayer) getCurrentPlayer();
		for (int y = 1; y <= Y_DIMENSION; y++) {
			buffer.append(y).append(" ");
			for (int x = 1; x <= X_DIMENSION; x++) {
				int squareColour = getSquare(x, y);
				if (squareColour != Colour.NONE) {
					buffer.append(ReversiPlayer.getInstance(squareColour).getSymbol());
				} else if (player != null && isValidMove(x, y, player.getColour())) {
					buffer.append('+');
				} else {
					buffer.append("-");
				}
				buffer.append(' ');
			}
			if (y < Y_DIMENSION) {
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}
	
	public void fromString(String boardAsString) {
		clear();
		int x = 1;
		int y = 1;
		String[] lines = boardAsString.split("\n");
		for (int i = 1; i < lines.length; i++) {
			for (int j = 2; j < lines[i].length(); j += 2) {
				String piece = lines[i].substring(j, j + 2);
				if (ReversiPlayer.getInstance(Colour.BLACK).getSymbol() == piece.charAt(0)) {
					setPiece(x, y, Colour.BLACK);
				} else if (ReversiPlayer.getInstance(Colour.WHITE).getSymbol() == piece.charAt(0)) { 
					setPiece(x, y, Colour.WHITE);
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
		if (! (board instanceof ReversiBoard)) {
			throw new IllegalArgumentException("Cannot copy " + board.getClass().getName() + " to " + getClass().getName());
		}
		super.copy(board);
		ReversiBoard reversiBoard = (ReversiBoard) board;
		for (int i = 0; i <= 7; i++) {
			squares[i] = reversiBoard.squares[i];
			adjacents[0][i] = reversiBoard.adjacents[0][i];
			adjacents[1][i] = reversiBoard.adjacents[1][i];
		}
		counts[0] = reversiBoard.counts[0];
		counts[1] = reversiBoard.counts[1];
		validCounts[0] = reversiBoard.validCounts[0];
		validCounts[1] = reversiBoard.validCounts[1];
		adjacentCounts[0] = reversiBoard.adjacentCounts[0];
		adjacentCounts[1] = reversiBoard.adjacentCounts[1];
	}

	public boolean canMove() {
		int colour = ((ReversiPlayer) getCurrentPlayer()).getColour();
		return canMove(colour);
	}

	private boolean canMove(int colour) {
		if (gameOver) {
			return false;
		}
		if (validCounts[colour - 1] == 0) {
			return false;
		}
		for (int y = 0; y <= 7; y++) {
			if (adjacents[2 - colour][y] != 0) {
				for (int x = 0; x <= 7; x++) {
					if (((adjacents[2 - colour][y] >> (8 * (x))) & 255) != 0) {
						if (isValidMove(x + 1, y + 1, colour)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int makeMove(Move move, List<Position> changes, boolean searching) {
		int x = ((ReversiMove) move).getPosition().getX();
		int y = ((ReversiMove) move).getPosition().getY();
		int colour = ((ReversiPlayer) getCurrentPlayer()).getColour();
		int flips = 0;
		long l = adjacents[2 - colour][y - 1] >> (8 * (x - 1));
		if ((l & Direction.RIGHT) != 0) {
			flips += traverseLine(x, y, colour, 1, 0, false, changes);
		}
		if ((l & Direction.DOWN) != 0) {
			flips += traverseLine(x, y, colour, 0, 1, false, changes);
		}
		if ((l & Direction.LEFT) != 0) {
			flips += traverseLine(x, y, colour, -1, 0, false, changes);
		}
		if ((l & Direction.UP) != 0) {
			flips += traverseLine(x, y, colour, 0, -1, false, changes);
		}
		if ((l & Direction.DOWN_RIGHT) != 0) {
			flips += traverseLine(x, y, colour, 1, 1, false, changes);
		}
		if ((l & Direction.DOWN_LEFT) != 0) {
			flips += traverseLine(x, y, colour, -1, 1, false, changes);	
		}
		if ((l & Direction.UP_LEFT) != 0) {
			flips += traverseLine(x, y, colour, -1, -1, false, changes);
		}
		if ((l & Direction.UP_RIGHT) != 0) {
			flips += traverseLine(x, y, colour, 1, -1, false, changes);
		}
		if (flips == 0) {
			return -1;
		}
		setPiece(x, y, colour);
		validCounts[0] = -1; // validcounts for both colours is not valid any more
		validCounts[1] = -1; // validcounts for both colours is not valid any more
		adjacentCounts[0] = -1; // adjacentcounts for both colours is not valid any more
		adjacentCounts[1] = -1; // adjacentcounts for both colours is not valid any more
		if (!canMove(Colour.BLACK) && !canMove(Colour.WHITE)) {
			gameOver = true;
		}
		return flips;
	}

	@Override
	public List<Move> getValidMoves(MoveRanker moveRanker, int depth) {
		int colour = ((ReversiPlayer) getCurrentPlayer()).getColour();
		if (gameOver || validCounts[colour - 1] == 0) {
			return Collections.emptyList();
		} else {
			MoveList moves = gameContext.createMoveList(INITIAL_MOVE_LIST_CAPACITY);
			for (int y = 0; y <= 7; y++) {
				if (adjacents[2 - colour][y] != 0) {
					for (int x = 0; x <= 7; x++) {
						if (((adjacents[2 - colour][y] >> (8 * (x))) & 255) != 0) {
							if (isValidMove(x + 1, y + 1, colour)) {
								//ReversiMove reversiMove = new ReversiMove(x + 1, y + 1);
								ReversiMove reversiMove = ((ReversiMoveFactory) gameContext.getMoveFactory()).createMove(x + 1, y + 1);
								moves.add(reversiMove, this, depth, moveRanker);
								if (validCounts[colour - 1] >= 0 && moves.size() >= validCounts[colour - 1]) {
									y = 7;
									x = 7; // break out of both loops as there cannot be any further moves
								}
							}
						}
					}
				}
			}
			validCounts[colour - 1] = moves.size();
			return moves.getMoves();
		}
	}

	public int countValidMoves() {
		return countValidMoves(getCurrentPlayer());
	}
	
	public int countValidMoves(Player player) {
		// see if we've already calculated it
		int colour = ((ReversiPlayer) player).getColour();
		if (validCounts[colour - 1] >= 0) return validCounts[colour - 1];
		// calculate it
		int movecount = 0;
		for (int y = 0; y <= 7; y++) {
			if (adjacents[2 - colour][y] != 0) {
				for (int x = 0; x <= 7; x++) {
					if (((adjacents[2 - colour][y] >> (8 * (x))) & 255) != 0) {
						if (isValidMove(x + 1, y + 1, colour)) {
							movecount++;
						}
					}
				}
			}
		}
		validCounts[colour - 1] = movecount;
		return movecount;
	}

	public int countMaxMovesLeft() {
		return getCount(Colour.NONE);
	}

	public int countMovesMade() {
		return getCount(Colour.ANY) - 4;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		final ReversiBoard other = (ReversiBoard) obj;
		if (!Arrays.equals(counts, other.counts)) return false;
		if (!Arrays.equals(adjacentCounts, other.adjacentCounts)) return false;
		if (!Arrays.equals(validCounts, other.validCounts)) return false;
		if (!Arrays.equals(squares, other.squares)) return false;
		if (!Arrays.equals(adjacents[0], other.adjacents[0])) return false;
		if (!Arrays.equals(adjacents[1], other.adjacents[1])) return false;
		return true;
	}
}
