/*
 * @(#)NineMensMorrisBoard.java		2008/01/26
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveList;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.Position;

public class NineMensMorrisBoard extends AbstractBoard {

	public static final int X_DIMENSION = 7;
	public static final int Y_DIMENSION = 7;
	
	public static final int NUMBER_OF_POSITIONS = 24;
	public static final int NUMBER_OF_LINES = 16;
	
	private static final int INITIAL_MOVE_LIST_CAPACITY = 10;
	private static final int MAX_SHIFTS_WITHOUT_A_CAPTURE = 20;
	
	private static final String BOARD_TEMPLATE =
	    "  a  b  c  d  e  f  g\n" +
		"7 +--------+--------+\n" +
		"  |        |        |\n" +
		"6 |  +-----+-----+  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  +--+--+  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 +--+--+     +--+--+\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  +--+--+  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  +-----+-----+  |\n" +
		"  |        |        |\n" +
		"1 +--------+--------+";
	
	public static final int LINE_MILL = 1;
	public static final int LINE_OPPONENT_MILL = 2;
	public static final int LINE_HOLE = 3;
	public static final int LINE_OPPONENT_HOLE = 4;
	
	private final int[] squares = new int[NUMBER_OF_POSITIONS];
	private final boolean[] holes = new boolean[NUMBER_OF_POSITIONS];
	private final int[] millLines = new int[NUMBER_OF_LINES];
	private final int[] holeLines = new int[NUMBER_OF_LINES];
	private final int[] squareCount = {0, 0};
	private final int[] inHandCount = {9, 9};
	private final int[] movesPlayedCount = {0, 0};
	private final int[] mobility = {0, 0};
	private int[][] validCaptures = {null, null};
	private int shiftsSinceCapture = 0;
	protected Player winner = null;
	
	/**
	 * @deprecated NineMensMorrisBoard instances should be obtained from a GameContext. 
	 */
	@Deprecated
	public NineMensMorrisBoard() {
		super();
	}
	
	@Override
	public void initialise() {
		clear();
		setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
	}

	protected void clear() {
		super.clear();
		for (int i = 0; i < squares.length; i++) {
			squares[i] = Colour.NONE;
		}
		for (int i = 0; i < holes.length; i++) {
			holes[i] = false;
		}
		for (int i = 0; i < millLines.length; i++) {
			millLines[i] = Colour.NONE;
		}
		for (int i = 0; i < holeLines.length; i++) {
			holeLines[i] = Colour.NONE;
		}
		squareCount[0] = 0;
		squareCount[1] = 0;
		movesPlayedCount[0] = 0;
		movesPlayedCount[1] = 0;
		inHandCount[0] = 9;
		inHandCount[1] = 9;
		mobility[0] = 0;
		mobility[1] = 0;
		validCaptures[0] = null;
		validCaptures[1] = null;
		shiftsSinceCapture = 0;
		winner = null;
	}
	
	public int getColour(int position) {
		return squares[position];
	}
	
	public int countPieces(int colour) {
		return squareCount[colour - 1];
	}
	
	public int countPiecesInHand(int colour) {
		return inHandCount[colour - 1];
	}
	
	public void setInHandCount(int colour, int count) {
		this.inHandCount[colour - 1] = count;
	}
	
	public int getMobility(int colour) {
		return mobility[colour - 1];
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public boolean isHole(int colour, int position) {
		if (holes[position]) {
			for (int line : NineMensMorrisBoardLinks.getLinesForPosition(position)) {
				if (holeLines[line] == colour) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getHolePosition(int line) {
		for (int position : NineMensMorrisBoardLinks.getPositionsForLine(line)) {
			if (squares[position] == Colour.NONE) {
				return position;
			}
		}
		return -1;
	}
	
	public boolean isMiddleHole(int line) {
		int[] positionsForLine = NineMensMorrisBoardLinks.getPositionsForLine(line);
		return (positionsForLine[1] == Colour.NONE);
	}

	public int getLineState(int line, int colour) {
		if (millLines[line] == colour) {
			return LINE_MILL;
		} else if (millLines[line] == 3 - colour) {
			return LINE_OPPONENT_MILL;
		} else if (holeLines[line] == colour) {
			return LINE_HOLE;
		} else if (holeLines[line] == 3 - colour) {
			return LINE_OPPONENT_HOLE;
		} else {		
			return 0;
		}
	}
	
	@Override
	public int countMovesMade() {
		return movesPlayedCount[((NineMensMorrisPlayer) currentPlayer).getColour() - 1];
	}
	
	@Override
	public int makeMove(Move move, List<Position> changes, boolean searching) {
		final NineMensMorrisMove nineMensMorrisMove = (NineMensMorrisMove) move;
		int colour = ((NineMensMorrisPlayer) currentPlayer).getColour();
		if (nineMensMorrisMove.isPlacement()) {
			placePiece(nineMensMorrisMove.getPosition(), colour);
			if (nineMensMorrisMove.hasCapture()) {
				capturePiece(nineMensMorrisMove.getCapturePosition());
			}
		} else {
			shiftPiece(nineMensMorrisMove.getFromPosition(), nineMensMorrisMove.getToPosition());
			if (nineMensMorrisMove.hasCapture()) {
				capturePiece(nineMensMorrisMove.getCapturePosition());
			}
		}
		if (!canMove(3 - colour)) {
			gameOver = true;
			winner = currentPlayer;
		} else if (shiftsSinceCapture >= MAX_SHIFTS_WITHOUT_A_CAPTURE) {
			gameOver = true;
			winner = null;
		}
		return 1;
	}	
	
	protected void placePiece(int position, int colour) {
		squares[position] = colour;
		squareCount[colour - 1]++;
		inHandCount[colour - 1]--;
		movesPlayedCount[colour - 1]++;
		validCaptures[0] = null;
		validCaptures[1] = null;
		updateLines(colour, position);
		updateMobilityAfterFillingSquare(position, colour);
	}
	
	protected void capturePiece(int position) {
		int colour = squares[position];
		squares[position] = Colour.NONE;
		squareCount[colour - 1]--;
		validCaptures[0] = null;
		validCaptures[1] = null;
		shiftsSinceCapture = 0;
		updateLines(colour, position);
		updateMobilityAfterVacatingSquare(position, -1, colour);
	}

	protected void shiftPiece(int initialPosition, int finalPosition) {
		int colour = squares[initialPosition];
		squares[finalPosition] = squares[initialPosition];
		squares[initialPosition] = Colour.NONE;
		movesPlayedCount[colour - 1]++;
		validCaptures[0] = null;
		validCaptures[1] = null;
		shiftsSinceCapture++;
		updateLines(colour, initialPosition);
		updateLines(colour, finalPosition);
		updateMobilityAfterVacatingSquare(initialPosition, finalPosition, colour);
		updateMobilityAfterFillingSquare(finalPosition, colour);
	}
	
	public boolean isValidMove(Move move) {
		final NineMensMorrisMove nineMensMorrisMove = (NineMensMorrisMove) move;
		int colour = ((NineMensMorrisPlayer) currentPlayer).getColour();
		if (nineMensMorrisMove.isPlacement()) {
			if (!isValidPlacement(nineMensMorrisMove.getPosition())) {
				return false;
			} else if (placementWouldCreateMill(nineMensMorrisMove.getPosition(), colour)) {
				if (!nineMensMorrisMove.hasCapture()) {
					return false;
				} else if (!isValidCapture(nineMensMorrisMove.getCapturePosition())) {
					return false;
				}
			} else if (nineMensMorrisMove.hasCapture()) {
				return false;
			}
		} else {
			if (!isValidShift(nineMensMorrisMove.getFromPosition(), nineMensMorrisMove.getToPosition())) {
				return false;
			} else if (shiftWouldCreateMill(nineMensMorrisMove.getFromPosition(), nineMensMorrisMove.getToPosition())) {
				if (!nineMensMorrisMove.hasCapture()) {
					return false;
				} else if (!isValidCapture(nineMensMorrisMove.getCapturePosition())) {
					return false;
				}
			} else if (nineMensMorrisMove.hasCapture()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canMove() {
		int colour = ((NineMensMorrisPlayer) currentPlayer).getColour();
		return canMove(colour);
	}

	private boolean canMove(int colour) {
		if (gameOver) {
			return false;
		}
		if (inHandCount[colour - 1] > 0) {
			return true;
		} else if (squareCount[colour - 1] < 3) {
			return false;
		} else if (squareCount[colour - 1] == 3) {
			return true;
		} else {
			for (int position = 0; position < squares.length; position++) {
				if (squares[position] == colour) {
					for (int linksToPosition : NineMensMorrisBoardLinks.linksTo(position)) {
						if (squares[linksToPosition] == Colour.NONE) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public List<Move> getValidMoves(MoveRanker moveRanker, int depth) {
		int colour = ((NineMensMorrisPlayer) currentPlayer).getColour();
		if (inHandCount[colour - 1] > 0) {
			return getValidPlacements(moveRanker, depth);
		} else {
			return getValidShifts(moveRanker, depth);
		}
	}
	
	private List<Move> getValidPlacements(MoveRanker moveRanker, int depth) {
		MoveList moves = gameContext.createMoveList(INITIAL_MOVE_LIST_CAPACITY);
		for (int position = 0; position < squares.length; position++) {
			if (squares[position] == Colour.NONE) {
				int colour = ((NineMensMorrisPlayer) currentPlayer).getColour();
				if (!placementWouldCreateMill(position, colour)) {
					moves.add(((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createPlacementMove(position), this, depth, moveRanker);
				} else {
					int[] capturePositions = getValidCaptures(colour);
					for (int capturePosition : capturePositions) {
						moves.add(((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createPlacementMove(position, capturePosition), this, depth, moveRanker);
					}
				}
			}
		}
		return moves.getMoves();
	}
	
	private List<Move> getValidShifts(MoveRanker moveRanker, int depth) {
		int colour = ((NineMensMorrisPlayer) currentPlayer).getColour();
		if (squareCount[colour - 1] > 3) {
			return getValidSlidingShifts(moveRanker, depth, colour);
		} else {
			return getValidFlyingShifts(moveRanker, depth, colour);
		}
	}
	
	private List<Move> getValidSlidingShifts(MoveRanker moveRanker, int depth, int colour) {
		MoveList moves = gameContext.createMoveList(INITIAL_MOVE_LIST_CAPACITY);
		for (int position = 0; position < squares.length; position++) {
			if (squares[position] == colour) {
				for (int linksToPosition : NineMensMorrisBoardLinks.linksTo(position)) {
					if (squares[linksToPosition] == Colour.NONE) {
						if (!shiftWouldCreateMill(position, linksToPosition)) {
							moves.add(((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createShiftMove(position, linksToPosition), this, depth, moveRanker);
						} else {
							int[] capturePositions = getValidCaptures(colour);
							for (int capturePosition : capturePositions) {
								moves.add(((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createShiftMove(position, linksToPosition, capturePosition), this, depth, moveRanker);
							}
						}
					}
				}
			}
		}
		return moves.getMoves();
	}

	private List<Move> getValidFlyingShifts(MoveRanker moveRanker, int depth, int colour) {
		MoveList moves = gameContext.createMoveList(INITIAL_MOVE_LIST_CAPACITY);
		for (int toPosition = 0; toPosition < squares.length; toPosition++) {
			if (squares[toPosition] == Colour.NONE) {
				for (int fromPosition = 0; fromPosition < squares.length; fromPosition++) {
					if (squares[fromPosition] == colour) {
						if (!shiftWouldCreateMill(fromPosition, toPosition)) {
							moves.add(((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createShiftMove(fromPosition, toPosition), this, depth, moveRanker);
						} else {
							int[] capturePositions = getValidCaptures(colour);
							for (int capturePosition : capturePositions) {
								moves.add(((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createShiftMove(fromPosition, toPosition, capturePosition), this, depth, moveRanker);
							}
						}
					}
				}
			}
		}
		return moves.getMoves();
	}
	
	private int[] getValidCaptures(int colour) {
		if (validCaptures[colour - 1] == null) {		              
			updateValidCaptures(colour);
		}
		return validCaptures[colour - 1];
	}

	private void updateValidCaptures(int colour) {
		int[] captures = new int[9];
		int captureIndex = 0;
		for (int position = 0; position < squares.length; position++) {
			if (squares[position] == 3 - colour && !isInMill(position)) {
				captures[captureIndex++] = position;
			}
		}
		if (captureIndex == 0) {
			for (int position = 0; position < squares.length; position++) {
				if (squares[position] == 3 - colour) {
					captures[captureIndex++] = position;
				}
			}
		}
		int captureArray[] = new int[captureIndex];
		for (int i = 0; i < captureIndex; i++) {
			captureArray[i] = captures[i];
		}
		validCaptures[colour - 1] = captureArray;
	}
	
	public boolean isValidPlacement(int position) {
		if (inHandCount[((NineMensMorrisPlayer) currentPlayer).getColour() - 1] < 1) {
			return false;
		} else if (squares[position] != Colour.NONE) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isValidShiftFrom(int fromPosition) {
		int colour = ((NineMensMorrisPlayer) currentPlayer).getColour();
		if (inHandCount[colour - 1] > 0) {
			return false;
		} else if (squares[fromPosition] != colour) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isValidShift(int fromPosition, int toPosition) {
		if (!isValidShiftFrom(fromPosition)) {
			return false;
		} else if (squares[toPosition] != Colour.NONE) {
			return false;
		} else if (squareCount[squares[fromPosition] - 1] > 3 && !NineMensMorrisBoardLinks.areLinked(fromPosition, toPosition)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isValidCapture(int position) {
		int colour = 3 - ((NineMensMorrisPlayer) currentPlayer).getColour();
		if (squares[position] != colour) {
			return false;
		} else if (isInMill(position)) {
			for (int checkPosition = 0; checkPosition < squares.length; checkPosition++) {
				if (squares[checkPosition] == colour) {
					if (!isInMill(checkPosition)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void updateLines(int colour, int position) {
		for (int line : NineMensMorrisBoardLinks.getLinesForPosition(position)) {
			int emptyCount = 0;
			int myCount = 0;
			int opponentCount = 0;
			int lastEmptyPosition = 0; 
			for (int positionInLine : NineMensMorrisBoardLinks.getPositionsForLine(line)) {
				if (squares[positionInLine] == Colour.NONE) {
					emptyCount++;
					lastEmptyPosition = positionInLine;
				} else if (squares[positionInLine] == colour) {
					myCount++;
				} else {
					opponentCount++;
				}
			}
			if (emptyCount == 1 && myCount == 2) {
				millLines[line] = Colour.NONE;
				holeLines[line] = colour;
				holes[lastEmptyPosition] = true;
			} else if (emptyCount == 1 && opponentCount == 2) {
				millLines[line] = Colour.NONE;
				holeLines[line] = 3 - colour;
				holes[lastEmptyPosition] = true;
			} else if (myCount == 3) {
				millLines[line] = colour;
				holeLines[line] = Colour.NONE;
				holes[position] = false;
			} else if (opponentCount == 3) {
				millLines[line] = 3 - colour;
				holeLines[line] = Colour.NONE;
				holes[position] = false;
			} else {
				millLines[line] = Colour.NONE;
				holeLines[line] = Colour.NONE;
				if (squares[position] != Colour.NONE) {
					holes[position] = false;
				}
			}
		}
	}
	
	private void updateMobilityAfterFillingSquare(int position, int colour) {
		for (Integer linkPosition : NineMensMorrisBoardLinks.linksTo(position)) {
			int linkColour = squares[linkPosition];
			if (linkColour == Colour.NONE) {
				mobility[colour - 1]++;
			} else if (linkColour == colour) {
				mobility[colour - 1]--;
			} else {
				mobility[2 - colour]--;
			}
		}
	}

	private void updateMobilityAfterVacatingSquare(int position, int ignorePosition, int colour) {
		for (Integer linkPosition : NineMensMorrisBoardLinks.linksTo(position)) {
			int linkColour = squares[linkPosition];
			if (linkColour == Colour.NONE || linkPosition.equals(ignorePosition)) {
				mobility[colour - 1]--;
			} else {
				mobility[linkColour - 1]++;
			}
		}
	}

	private boolean isInMill(int position) {
		for (int line : NineMensMorrisBoardLinks.getLinesForPosition(position)) {
			if (millLines[line] == squares[position]) {
				return true;
			}
		}
		return false;
	}

	public boolean placementWouldCreateMill(int position) {
		int colour = ((NineMensMorrisPlayer) getCurrentPlayer()).getColour();
		return wouldCreateMill(position, -1, colour);
	}

	private boolean placementWouldCreateMill(int position, int colour) {
		return wouldCreateMill(position, -1, colour);
	}

	public boolean shiftWouldCreateMill(int fromPosition, int toPosition) {
		int colour = squares[fromPosition];
		return wouldCreateMill(toPosition, fromPosition, colour);
	}
	
	private boolean wouldCreateMill(int filledPosition, int vacatedPosition, int colour) {
		for (int line : NineMensMorrisBoardLinks.getLinesForPosition(filledPosition)) {
			int count = 0;
			for (int positionInLine : NineMensMorrisBoardLinks.getPositionsForLine(line)) {
				if (positionInLine == vacatedPosition || (positionInLine != filledPosition && squares[positionInLine] != colour)) {
					break;
				} else {
					count++;
				}
			}
			if (count == 3) {
				return true;
			}
		}
		return false;
	}
	
	public int countMills(int colour) {
		int count = 0;
		for (int i = 0; i < NineMensMorrisBoardLinks.LINES_TO_POSITIONS.length; i++) {
			if (millLines[i] == colour) {
				count++;
			}
		}
		return count;
	}

	public int countHoles(int colour) {
		int count = 0;
		for (int position = 0; position < holes.length; position++) {
			if (holes[position]) {
				for (int line : NineMensMorrisBoardLinks.getLinesForPosition(position)) {
					if (holeLines[line] == colour) {
						count++;
						break;
					}
				}
			}
		}
		return count;
	}

	public int countAdjacents(int colour) {
		Set<Integer> adjacentPositions = new HashSet<Integer>();
		for (int line = 0; line < holeLines.length; line++) {
			if (holeLines[line] == colour) {
				for (int positionInLine : NineMensMorrisBoardLinks.getPositionsForLine(line)) {
					if (holes[positionInLine]) {
						for (Integer adjacentPosition : NineMensMorrisBoardLinks.linksTo(positionInLine)) {
							if (squares[adjacentPosition] == colour) {
								if (!NineMensMorrisBoardLinks.isInLine(adjacentPosition, line)) {
									adjacentPositions.add(adjacentPosition);
								}
							}
						}
					}
				}
			}
		}
		return adjacentPositions.size();
	}
	
	public int getDegreesOfFreedomForPosition(int position) {
		int mobility = 0;
		int[] linksTo = NineMensMorrisBoardLinks.linksTo(position);
		for (int link : linksTo) {
			if (squares[link] == Colour.NONE) {
				mobility++;
			}
		}
		return mobility;
	}

	public void fromString(String boardAsString) {
		clear();
		int index = 0;
		for (int i = 0; i < boardAsString.length(); i++) {
			char ch = boardAsString.charAt(i);
			if (NineMensMorrisPlayer.getInstance(Colour.WHITE).getSymbol() == ch) {
				placePiece(NineMensMorrisPosition.lookupPositionFromIndex(index++), Colour.WHITE);
			} else if (NineMensMorrisPlayer.getInstance(Colour.BLACK).getSymbol() == ch) {
				placePiece(NineMensMorrisPosition.lookupPositionFromIndex(index++), Colour.BLACK);
			} else if ('+' == ch) {
				index++;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		char whiteChar = NineMensMorrisPlayer.getInstance(Colour.WHITE).getSymbol();
		char blackChar = NineMensMorrisPlayer.getInstance(Colour.BLACK).getSymbol();
		int fromIndex = 0;
		int squareIndex = 0;
		while (true) {
			int indexOfPlus = BOARD_TEMPLATE.indexOf('+', fromIndex);
			if (indexOfPlus >= 0) {
				sb.append(BOARD_TEMPLATE.substring(fromIndex, indexOfPlus));
				if (Colour.WHITE == squares[NineMensMorrisPosition.lookupPositionFromIndex(squareIndex)]) {
					sb.append(whiteChar);
				} else if (Colour.BLACK == squares[NineMensMorrisPosition.lookupPositionFromIndex(squareIndex)]) {
					sb.append(blackChar);
				} else {
					sb.append('+');
				}
				squareIndex++;
				fromIndex = indexOfPlus + 1;
			} else {
				sb.append(BOARD_TEMPLATE.substring(fromIndex));
				break;
			}
		}
		return sb.toString();
	}

	@Override
	public void copy(AbstractBoard board) {
		if (! (board instanceof NineMensMorrisBoard)) {
			throw new IllegalArgumentException("Cannot copy " + board.getClass().getName() + " to " + getClass().getName());
		}
		super.copy(board);
		final NineMensMorrisBoard nineMensMorrisBoard = (NineMensMorrisBoard) board;
		for (int i = 0; i < squares.length; i++) {
			squares[i] = nineMensMorrisBoard.squares[i];
		}
		for (int i = 0; i < holes.length; i++) {
			holes[i] = nineMensMorrisBoard.holes[i];
		}
		for (int i = 0; i < millLines.length; i++) {
			millLines[i] = nineMensMorrisBoard.millLines[i];
		}
		for (int i = 0; i < holeLines.length; i++) {
			holeLines[i] = nineMensMorrisBoard.holeLines[i];
		}
		squareCount[0] = nineMensMorrisBoard.squareCount[0];
		squareCount[1] = nineMensMorrisBoard.squareCount[1];
		inHandCount[0] = nineMensMorrisBoard.inHandCount[0];
		inHandCount[1] = nineMensMorrisBoard.inHandCount[1];
		movesPlayedCount[0] = nineMensMorrisBoard.movesPlayedCount[0];
		movesPlayedCount[1] = nineMensMorrisBoard.movesPlayedCount[1];
		shiftsSinceCapture = nineMensMorrisBoard.shiftsSinceCapture;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(holeLines);
		result = prime * result + Arrays.hashCode(holes);
		result = prime * result + Arrays.hashCode(inHandCount);
		result = prime * result + Arrays.hashCode(millLines);
		result = prime * result + Arrays.hashCode(mobility);
		result = prime * result + Arrays.hashCode(movesPlayedCount);
		result = prime * result + shiftsSinceCapture;
		result = prime * result + Arrays.hashCode(squareCount);
		result = prime * result + Arrays.hashCode(squares);
		result = prime * result + Arrays.hashCode(validCaptures);
		result = prime * result + ((winner == null) ? 0 : winner.hashCode());
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
		final NineMensMorrisBoard other = (NineMensMorrisBoard) obj;
		if (!Arrays.equals(holeLines, other.holeLines))
			return false;
		if (!Arrays.equals(holes, other.holes))
			return false;
		if (!Arrays.equals(inHandCount, other.inHandCount))
			return false;
		if (!Arrays.equals(millLines, other.millLines))
			return false;
		if (!Arrays.equals(mobility, other.mobility))
			return false;
		if (!Arrays.equals(movesPlayedCount, other.movesPlayedCount))
			return false;
		if (shiftsSinceCapture != other.shiftsSinceCapture)
			return false;
		if (!Arrays.equals(squareCount, other.squareCount))
			return false;
		if (!Arrays.equals(squares, other.squares))
			return false;
		if (!Arrays.equals(validCaptures, other.validCaptures))
			return false;
		if (winner == null) {
			if (other.winner != null)
				return false;
		} else if (!winner.equals(other.winner))
			return false;
		return true;
	}
}
