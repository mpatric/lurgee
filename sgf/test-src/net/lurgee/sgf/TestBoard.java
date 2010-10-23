/*
 * @(#)TestBoard.java		2005/11/16
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.List;

/**
 * Board used for unit tests. The 'board' is a contrived structure in which a state identifier specifies the state of
 * the board. The moves for each board state are shown in the tree below. The board state is shown in red. So, when the
 * board is in state A, moves are possible for player 1 to change it to B or C. When the board is in state B, moves are
 * possible for player 2 to change it to D or E, and so on. The scores at each leaf (board states H to T) are
 * determined by {@link TestEvaluator}, which implement the following two contrived example game trees. When {@link #infiniteGameTree}
 * is true, these trees are artificially extended by making moves possible from all leaves back to board state A. 
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
public class TestBoard extends AbstractBoard {

	private static final char START_POSITION_VALUE = 'A';
	
	public static boolean noMoveSituation = false;
	public static boolean infiniteGameTree = false;
	
	private char state = START_POSITION_VALUE;
	private boolean makeMoveCalled = false;
	private int movesPlayed = 0;

	public TestBoard() {
		super();
	}
	
	public TestBoard(TestBoard board) {
		this();
		copy(board);
	}
	
	public boolean isMakeMoveCalled() {
		return makeMoveCalled;
	}
	
	public char getState() {
		return state;
	}
	
	public void setState(char state) {
		this.state = state;
	}
	
	public void initialise() {
		state = START_POSITION_VALUE;
		movesPlayed = 0;
		setCurrentPlayer(TestPlayer.getInstance(1));
	}

	@Override
	public void copy(AbstractBoard board) {
		super.copy(board);
		if (board instanceof TestBoard) {
			TestBoard testBoard = (TestBoard) board;
			state = testBoard.state;
			movesPlayed = testBoard.movesPlayed;
		}
	}

	public boolean canMove() {
		return getValidMoves(null, 1).size() > 0;
	}

	public int makeMove(Move move, List<Position> changes, boolean searching) {
		state = ((TestMove) move).getPosition().getValue();
		makeMoveCalled = true;
		movesPlayed++;
		return 1;
	}

	@Override
	public List<Move> getValidMoves(MoveRanker moveRanker, int depth) {
		MoveList moves = gameContext.createMoveList(4);
		switch (state) {
			case 'A':
				if (!noMoveSituation) {
					moves.add(new TestMove('B'), this, depth, moveRanker);
					moves.add(new TestMove('C'), this, depth, moveRanker);
				} else {
					moves.add(new TestMove('R'), this, depth, moveRanker);
					moves.add(new TestMove('C'), this, depth, moveRanker);
				}
				break;
			case 'B':
				moves.add(new TestMove('D'), this, depth, moveRanker);
				moves.add(new TestMove('E'), this, depth, moveRanker);
				break;
			case 'C':
				moves.add(new TestMove('F'), this, depth, moveRanker);
				moves.add(new TestMove('G'), this, depth, moveRanker);
				break;
			case 'D':
				moves.add(new TestMove('H'), this, depth, moveRanker);
				moves.add(new TestMove('I'), this, depth, moveRanker);
				moves.add(new TestMove('J'), this, depth, moveRanker);
				break;
			case 'E':
				moves.add(new TestMove('K'), this, depth, moveRanker);
				moves.add(new TestMove('L'), this, depth, moveRanker);
				break;
			case 'F':
				moves.add(new TestMove('M'), this, depth, moveRanker);
				break;
			case 'G':
				moves.add(new TestMove('N'), this, depth, moveRanker);
				moves.add(new TestMove('O'), this, depth, moveRanker);
				moves.add(new TestMove('P'), this, depth, moveRanker);
				moves.add(new TestMove('Q'), this, depth, moveRanker);
				break;
			case 'R':
				if (((TestPlayer) getCurrentPlayer()).getNumber() == 1) {
					moves.add(new TestMove('S'), this, depth, moveRanker);
					moves.add(new TestMove('T'), this, depth, moveRanker);
				} else {
					// no moves for player 2 from this board state
				}
		}
		if (infiniteGameTree && moves.size() == 0) {
			moves.add(new TestMove('A'), this, depth, moveRanker);
		}
		return moves.getMoves();
	}

	public int countMaxMovesLeft() {
		if (infiniteGameTree) {
			return 100;
		}
		switch (state) {
			case 'A':
				return 3;
			case 'B':
				return 2;
			case 'C':
				return 2;
			case 'D':
				return 1;
			case 'E':
				return 1;
			case 'F':
				return 1;
			case 'G':
				return 1;
			case 'R':
				return 1;
		}
		return 0;
	}

	public int countMovesMade() {
		return movesPlayed;
	}
	
	public boolean isValidMove(Move move) {
		return true;
	}
	
	@Override
	public void poolableDone() {
		state = START_POSITION_VALUE;
		movesPlayed = 0;
	}
	
	@Override
	public String toString() {
		return "Board " + state;
	}
}
