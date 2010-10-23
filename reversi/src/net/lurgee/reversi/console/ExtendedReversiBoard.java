package net.lurgee.reversi.console;

import java.util.Arrays;
import java.util.List;

import net.lurgee.reversi.Direction;
import net.lurgee.reversi.ReversiBoard;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Position;

@SuppressWarnings("deprecation")
public class ExtendedReversiBoard extends ReversiBoard {
	
	private final int oldAdjacentCounts[] = {-1, -1};
	private final long oldAdjacents[][] = {new long[Y_DIMENSION], new long[Y_DIMENSION]};
	
	protected void clear() {
		super.clear();
		for (int i = 0; i <= 7; i++) {
			oldAdjacents[0][i] = 0;
			oldAdjacents[1][i] = 0;
		}
		oldAdjacentCounts[0] = -1;
		oldAdjacentCounts[1] = -1;
	}
	
	public int getNewAdjacentCount(int colour) {
		// see if we've already calculated it
		if (oldAdjacentCounts[colour - 1] >= 0) return oldAdjacentCounts[colour - 1];
		// calculate it
		int adjcount = 0;
		for (int y = 0; y <= 7; y++) {
			if (oldAdjacents[colour - 1][y] != 0) {
				for (int x = 0; x <= 7; x++) {
					if (((oldAdjacents[colour - 1][y] >> (8 * (x))) & 255) != 0) {
						adjcount++;
					}
				}
			}
		}
		oldAdjacentCounts[colour - 1] = adjcount;
		return adjcount;
	}

	protected void setAdjacents(int x, int y, int colour) {
		super.setAdjacents(x, y, colour);
		// set adjacents for a newly placed piece
		oldAdjacents[0][y - 1] &= ~((long) (255) << (8 * (x - 1))); // unset the bits for (x,y) for black
		oldAdjacents[1][y - 1] &= ~((long) (255) << (8 * (x - 1))); // unset the bits for (x,y) for white
		// look at squares around (x,y) and set those that are empty
		if (x < X_DIMENSION && (((squares[y - 1] >> (2 * (x))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 1] |= ((long) Direction.LEFT << (8 * (x)));
		}
		if (x < X_DIMENSION && y < Y_DIMENSION && (((squares[y] >> (2 * (x))) & 3) == 0)) {
			oldAdjacents[colour - 1][y] |= ((long) Direction.UP_LEFT << (8 * (x)));
		}
		if (y < Y_DIMENSION && (((squares[y] >> (2 * (x - 1))) & 3) == 0)) {
			oldAdjacents[colour - 1][y] |= ((long) Direction.UP << (8 * (x - 1)));
		}
		if (x > 1 && y < Y_DIMENSION && (((squares[y] >> (2 * (x - 2))) & 3) == 0)) {
			oldAdjacents[colour - 1][y] |= ((long) Direction.UP_RIGHT << (8 * (x - 2)));
		}
		if (x > 1 && (((squares[y - 1] >> (2 * (x - 2))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 1] |= ((long) Direction.RIGHT << (8 * (x - 2)));
		}
		if (x > 1 && y > 1 && (((squares[y - 2] >> (2 * (x - 2))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 2] |= ((long) Direction.DOWN_RIGHT << (8 * (x - 2)));
		}
		if (y > 1 && (((squares[y - 2] >> (2 * (x - 1))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 2] |= ((long) Direction.DOWN << (8 * (x - 1)));
		}
		if (x < X_DIMENSION && y > 1 && (((squares[y - 2] >> (2 * (x))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 2] |= ((long) Direction.DOWN_LEFT << (8 * (x)));
		}
	}
	
	protected void updateAdjacents(int x, int y, int colour) {
		super.updateAdjacents(x, y, colour);
		// unset bits around (x,y) for square with old colour
		if (x < X_DIMENSION) {
			oldAdjacents[2 - colour][y - 1] &= ~((long) (Direction.LEFT) << (8 * (x)));
		}
		if (x < X_DIMENSION && y < Y_DIMENSION) {
			oldAdjacents[2 - colour][y] &= ~((long) Direction.UP_LEFT << (8 * (x)));
		}
		if (y < Y_DIMENSION) {
			oldAdjacents[2 - colour][y] &= ~((long) Direction.UP << (8 * (x - 1)));
		}
		if (x > 1 && y < Y_DIMENSION) {
			oldAdjacents[2 - colour][y] &= ~((long) Direction.UP_RIGHT << (8 * (x - 2)));
		}
		if (x > 1) {
			oldAdjacents[2 - colour][y - 1] &= ~((long) Direction.RIGHT << (8 * (x - 2)));
		}
		if (x > 1 && y > 1) {
			oldAdjacents[2 - colour][y - 2] &= ~((long) Direction.DOWN_RIGHT << (8 * (x - 2)));
		}
		if (y > 1) {
			oldAdjacents[2 - colour][y - 2] &= ~((long) Direction.DOWN << (8 * (x - 1)));
		}
		if (x < X_DIMENSION && y > 1) {
			oldAdjacents[2 - colour][y - 2] &= ~((long) Direction.DOWN_LEFT << (8 * (x)));
		}
		// look at squares around (x,y) and set those that are empty
		if (x < X_DIMENSION && (((squares[y - 1] >> (2 * (x))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 1] |= ((long) (Direction.LEFT) << (8 * (x)));
		}
		if (x < X_DIMENSION && y < Y_DIMENSION && (((squares[y] >> (2 * (x))) & 3) == 0)) {
			oldAdjacents[colour - 1][y] |= ((long) Direction.UP_LEFT << (8 * (x)));
		}
		if (y < Y_DIMENSION && (((squares[y] >> (2 * (x - 1))) & 3) == 0)) {
			oldAdjacents[colour - 1][y] |= ((long) Direction.UP << (8 * (x - 1)));
		}
		if (x > 1 && y < Y_DIMENSION && (((squares[y] >> (2 * (x - 2))) & 3) == 0)) {
			oldAdjacents[colour - 1][y] |= ((long) Direction.UP_RIGHT << (8 * (x - 2)));
		}
		if (x > 1 && (((squares[y - 1] >> (2 * (x - 2))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 1] |= ((long) Direction.RIGHT << (8 * (x - 2)));
		}
		if (x > 1 && y > 1 && (((squares[y - 2] >> (2 * (x - 2))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 2] |= ((long) Direction.DOWN_RIGHT << (8 * (x - 2)));
		}
		if (y > 1 && (((squares[y - 2] >> (2 * (x - 1))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 2] |= ((long) Direction.DOWN << (8 * (x - 1)));
		}
		if (x < X_DIMENSION && y > 1 && (((squares[y - 2] >> (2 * (x))) & 3) == 0)) {
			oldAdjacents[colour - 1][y - 2] |= ((long) Direction.DOWN_LEFT << (8 * (x)));
		}
	}
	
	public void copy(AbstractBoard board) {
		if (! (board instanceof ExtendedReversiBoard)) {
			throw new IllegalArgumentException("Cannot copy " + board.getClass().getName() + " to " + getClass().getName());
		}
		super.copy(board);
		ExtendedReversiBoard reversiBoard = (ExtendedReversiBoard) board;
		for (int i = 0; i <= 7; i++) {
			oldAdjacents[0][i] = reversiBoard.oldAdjacents[0][i];
			oldAdjacents[1][i] = reversiBoard.oldAdjacents[1][i];
		}
		oldAdjacentCounts[0] = reversiBoard.oldAdjacentCounts[0];
		oldAdjacentCounts[1] = reversiBoard.oldAdjacentCounts[1];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(oldAdjacentCounts);
		result = prime * result + Arrays.hashCode(oldAdjacents);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ExtendedReversiBoard other = (ExtendedReversiBoard) obj;
		if (!Arrays.equals(oldAdjacentCounts, other.oldAdjacentCounts))
			return false;
		if (!Arrays.equals(oldAdjacents, other.oldAdjacents))
			return false;
		return true;
	}
	
	public int makeMove(Move move, List<Position> changes, boolean searching) {
		int result = super.makeMove(move, changes, searching);
		oldAdjacentCounts[0] = -1; // adjacentcounts for both colours is not valid any more
		oldAdjacentCounts[1] = -1; // adjacentcounts for both colours is not valid any more
		return result;
	}
}
