package net.lurgee.ninemensmorris;

import java.util.List;

import junit.framework.TestCase;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.MoveRanker;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;

public class NineMensMorrisBoardTest extends TestCase {

	private static final int A1 = NineMensMorrisPosition.lookupNumberFromName("a1");
	private static final int D1 = NineMensMorrisPosition.lookupNumberFromName("d1");
	private static final int G1 = NineMensMorrisPosition.lookupNumberFromName("g1");
	private static final int B2 = NineMensMorrisPosition.lookupNumberFromName("b2");
	private static final int D2 = NineMensMorrisPosition.lookupNumberFromName("d2");
	private static final int F2 = NineMensMorrisPosition.lookupNumberFromName("f2");
	private static final int C3 = NineMensMorrisPosition.lookupNumberFromName("c3");
	private static final int D3 = NineMensMorrisPosition.lookupNumberFromName("d3");
	private static final int E3 = NineMensMorrisPosition.lookupNumberFromName("e3");
	private static final int A4 = NineMensMorrisPosition.lookupNumberFromName("a4");
	private static final int B4 = NineMensMorrisPosition.lookupNumberFromName("b4");
	private static final int C4 = NineMensMorrisPosition.lookupNumberFromName("c4");
	private static final int E4 = NineMensMorrisPosition.lookupNumberFromName("e4");
	private static final int F4 = NineMensMorrisPosition.lookupNumberFromName("f4");
	private static final int G4 = NineMensMorrisPosition.lookupNumberFromName("g4");
	private static final int C5 = NineMensMorrisPosition.lookupNumberFromName("c5");
	private static final int D5 = NineMensMorrisPosition.lookupNumberFromName("d5");
	private static final int E5 = NineMensMorrisPosition.lookupNumberFromName("e5");
	private static final int B6 = NineMensMorrisPosition.lookupNumberFromName("b6");
	private static final int D6 = NineMensMorrisPosition.lookupNumberFromName("d6");
	private static final int F6 = NineMensMorrisPosition.lookupNumberFromName("f6");
	private static final int A7 = NineMensMorrisPosition.lookupNumberFromName("a7");
	private static final int D7 = NineMensMorrisPosition.lookupNumberFromName("d7");
	private static final int G7 = NineMensMorrisPosition.lookupNumberFromName("g7");

	private static final String TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR =
		"  a  b  c  d  e  f  g\n" +
		"7 X--------O--------O\n" +
		"  |        |        |\n" +
		"6 |  +-----X-----O  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  +--X--O  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 +--X--+     X--+--O\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  X--+--X  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  +-----X-----+  |\n" +
		"  |        |        |\n" +
		"1 O--------O--------O";
	
	private static final String TEST_BOARD_WITH_BLACK_HAVING_ALL_PIECES_IN_MILLS =
		"  a  b  c  d  e  f  g\n" +
		"7 X--------+--------O\n" +
		"  |        |        |\n" +
		"6 |  +-----X-----+  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  +--X--+  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 +--X--+     X--+--O\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  X--+--X  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  +-----X-----+  |\n" +
		"  |        |        |\n" +
		"1 O--------O--------O";
	
	private static final String TEST_BOARD_WITH_WHITE_BLOCKED =
		"  a  b  c  d  e  f  g\n" +
		"7 X--------X--------X\n" +
		"  |        |        |\n" +
		"6 |  O-----X-----O  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  O--X--O  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 O--X--O     +--+--O\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  +--+--+  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  O-----+-----+  |\n" +
		"  |        |        |\n" +
		"1 X--------O--------+";

	private static final String TEST_BOARD_WITH_ONLY_3_WHITE_PIECES =
		"  a  b  c  d  e  f  g\n" +
		"7 X--------X--------O\n" +
		"  |        |        |\n" +
		"6 |  O-----O-----O  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  +--+--+  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 X--O--+     +--+--+\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  +--+--+  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  +-----+-----+  |\n" +
		"  |        |        |\n" +
		"1 +--------+--------+";
	
	private static final String TEST_BOARD_WITH_ONLY_3_WHITE_PIECES_AND_BLACK_HAVING_ALL_PIECES_IN_MILLS =
		"  a  b  c  d  e  f  g\n" +
		"7 X--------X--------+\n" +
		"  |        |        |\n" +
		"6 |  O-----O-----O  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  +--+--+  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 X--O--+     +--+--+\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  +--+--+  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  O-----+-----+  |\n" +
		"  |        |        |\n" +
		"1 +--------+--------+";
	
	private static final String TEST_BOARD_WITH_WHITE_HAVING_2_MILLS_WITH_SHARED_PIECE =
		"  a  b  c  d  e  f  g\n" +
		"7 X--------X--------X\n" +
		"  |        |        |\n" +
		"6 |  X-----+-----X  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  +--+--+  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 X--+--+     +--+--O\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  +--+--+  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  +-----+-----+  |\n" +
		"  |        |        |\n" +
		"1 X--------O--------O";
	
	private static final String TEST_BOARD_WITH_3_PIECES_OF_EACH_COLOUR =
		"  a  b  c  d  e  f  g\n" +
		"7 X--------O--------X\n" +
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
		"1 O--------O--------X";

	private static final String TEST_BOARD_TO_RECREATE_BUG_WHERE_MOVE_INTO_MILL_DOES_NOT_REALISE_IT_SHOULD_CAPTURE_A_PIECE = 
		"  a  b  c  d  e  f  g" + 
		"7 +--------X--------+" + 
		"  |        |        |" + 
		"6 |  +-----O-----+  |" + 
		"  |  |     |     |  |" + 
		"5 |  |  X--+--+  |  |" + 
		"  |  |  |     |  |  |" + 
		"4 X--X--+     O--O--O" + 
		"  |  |  |     |  |  |" + 
		"3 |  |  X--X--X  |  |" + 
		"  |  |     |     |  |" + 
		"2 |  +-----O-----+  |" + 
		"  |        |        |" + 
		"1 +--------+--------+";
	
	private static final String TEST_BOARD_TO_RECREATE_BUG_WHERE_MOVE_RESULTS_IN_OPPONENT_HAVING_NO_MOVES_DOES_NOT_SET_GAMEOVER = 
 		"  a  b  c  d  e  f  g\n" +
		"7 O--------X--------X\n" +
		"  |        |        |\n" +
		"6 |  +-----O-----X  |\n" +
		"  |  |     |     |  |\n" +
		"5 |  |  +--+--O  |  |\n" +
		"  |  |  |     |  |  |\n" +
		"4 O--X--+     O--X--X\n" +
		"  |  |  |     |  |  |\n" +
		"3 |  |  +--+--+  |  |\n" +
		"  |  |     |     |  |\n" +
		"2 |  +-----O-----O  |\n" +
		"  |        |        |\n" +
		"1 +--------O--------O";
	
	private GameContext gameContext;
	private NineMensMorrisBoard board;
	private MoveRanker moveRanker = new NineMensMorrisMoveRanker();
	private NineMensMorrisMoveFactory moveFactory;
	
	@Override
	protected void setUp() throws Exception {
		Player[] players = {NineMensMorrisPlayer.getInstance(Colour.WHITE), NineMensMorrisPlayer.getInstance(Colour.BLACK)};
		ObjectPool boardPool = new ObjectPool(NineMensMorrisBoard.class);
		moveFactory = new NineMensMorrisMoveFactory();
		gameContext = new GameContext(players, boardPool, moveFactory, false);
		board = (NineMensMorrisBoard) gameContext.checkOutBoard();
		board.initialise();
	}
	
	@Override
	protected void tearDown() throws Exception {
		gameContext.checkInBoard(board);
		board = null;
	}
	
	public void testShouldSetBoardFromStringRepresentation() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		assertEquals(Colour.WHITE, board.getColour(A7));
		assertEquals(Colour.BLACK, board.getColour(D7));
		assertEquals(Colour.BLACK, board.getColour(G7));		
		assertEquals(Colour.NONE, board.getColour(B6));
		assertEquals(Colour.WHITE, board.getColour(D6));
		assertEquals(Colour.BLACK, board.getColour(F6));
		assertEquals(Colour.NONE, board.getColour(C5));
		assertEquals(Colour.WHITE, board.getColour(D5));
		assertEquals(Colour.BLACK, board.getColour(E5));
		assertEquals(Colour.NONE, board.getColour(A4));
		assertEquals(Colour.WHITE, board.getColour(B4));
		assertEquals(Colour.NONE, board.getColour(C4));
		assertEquals(Colour.WHITE, board.getColour(E4));
		assertEquals(Colour.NONE, board.getColour(F4));
		assertEquals(Colour.BLACK, board.getColour(G4));
		assertEquals(Colour.WHITE, board.getColour(C3));
		assertEquals(Colour.NONE, board.getColour(D3));
		assertEquals(Colour.WHITE, board.getColour(E3));
		assertEquals(Colour.NONE, board.getColour(B2));
		assertEquals(Colour.WHITE, board.getColour(D2));
		assertEquals(Colour.NONE, board.getColour(F2));
		assertEquals(Colour.BLACK, board.getColour(A1));
		assertEquals(Colour.BLACK, board.getColour(D1));
		assertEquals(Colour.BLACK, board.getColour(G1));
	}
	
	public void testGetSameStringRepresentationFromBoardInitialisedWithString() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		String boardAsString = board.toString();
		assertEquals(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR, boardAsString);
	}

	public void testShouldCountMovesPlayedForEachPlacementAndShiftMove() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		assertEquals(8, board.countMovesMade());
		board.placePiece(B6, Colour.WHITE);
		assertEquals(9, board.countMovesMade());
		board.shiftPiece(E4, F4);
		assertEquals(10, board.countMovesMade());
		board.capturePiece(E5);
		assertEquals(10, board.countMovesMade());
	}
	
	public void testShouldDecreaseNumberOfPiecesInHandWhenPiecesArePlaced() throws Exception {
		assertEquals(9, board.countPiecesInHand(Colour.WHITE));
		assertEquals(9, board.countPiecesInHand(Colour.BLACK));
		board.placePiece(A7, Colour.WHITE);
		assertEquals(8, board.countPiecesInHand(Colour.WHITE));
		assertEquals(9, board.countPiecesInHand(Colour.BLACK));
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		assertEquals(1, board.countPiecesInHand(Colour.WHITE));
		assertEquals(1, board.countPiecesInHand(Colour.BLACK));
	}
	
	public void testShouldGetValidPlacementMovesForEmptyBoard() throws Exception {
		List<Move> validMoves = board.getValidMoves(moveRanker, 5);
		assertEquals(24, validMoves.size());
		assertPlacementMovesAreValid(validMoves);
		assertCapturesAreValid(validMoves);
	}
	
	public void testShouldGetValidPlacementMovesForTestBoard() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		List<Move> validMoves = board.getValidMoves(moveRanker, 5);
		assertEquals(10, validMoves.size());
		assertPlacementMovesAreValid(validMoves);
		assertCapturesAreValid(validMoves);
		int[] expectedMovesThatDontCompleteAMill = {B6, C5, A4, C4, F4, B2, F2};
		int[] expectedMovesThatCompleteAMill = {D3};
		int[] expectedCaptures = {D7, F6, E5};
		assertPlacementMovesAreAsExpected(validMoves, expectedMovesThatDontCompleteAMill, expectedMovesThatCompleteAMill, expectedCaptures);
	}

	public void testShouldGetValidPlacementMovesForTestBoardWhereOpponentHasAllPiecesInMills() throws Exception {
		board.fromString(TEST_BOARD_WITH_BLACK_HAVING_ALL_PIECES_IN_MILLS);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		List<Move> validMoves = board.getValidMoves(moveRanker, 5);
		assertEquals(23, validMoves.size());
		assertPlacementMovesAreValid(validMoves);
		assertCapturesAreValid(validMoves);
		int[] expectedMovesThatDontCompleteAMill = {B6, F6, C5, A4, C4, F4, B2, F2};
		int[] expectedMovesThatCompleteAMill = {D7, E5, D3};
		int[] expectedCaptures = {G7, G4, A1, D1, G1};
		assertPlacementMovesAreAsExpected(validMoves, expectedMovesThatDontCompleteAMill, expectedMovesThatCompleteAMill, expectedCaptures);
	}
	
	public void testShouldGetValidShiftMovesForTestBoard() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		List<Move> validMoves = board.getValidMoves(moveRanker, 5);
		assertEquals(16, validMoves.size());
		assertShiftFromsAreValid(validMoves);
		assertShiftMovesAreValid(validMoves);
		assertCapturesAreValid(validMoves);
		int[][] expectedMoves = {
			{A7, A4}, {D6, B6}, {D5, C5}, {B4, B6}, {B4, A4}, {B4, C4}, {B4, B2}, {E4, F4},
			{C3, C4}, {C3, D3}, {E3, D3}, {D2, B2}, {D2, F2}
		};
		int[][] expectedMovesThatCompleteAMill = {{D2, D3}};
		int[] expectedCaptures = {D7, F6, E5};
		assertShiftMovesAreAsExpected(validMoves, expectedMoves, expectedMovesThatCompleteAMill, expectedCaptures);
	}

	public void testShouldGetValidShiftMovesForTestBoardWhereOpponentHasAllPiecesInMills() throws Exception {
		board.fromString(TEST_BOARD_WITH_BLACK_HAVING_ALL_PIECES_IN_MILLS);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		List<Move> validMoves = board.getValidMoves(moveRanker, 5);
		assertEquals(31, validMoves.size());
		assertShiftFromsAreValid(validMoves);
		assertShiftMovesAreValid(validMoves);
		assertCapturesAreValid(validMoves);
		int[][] expectedMoves = {
			{A7, A4}, {D6, B6}, {D6, D7}, {D6, F6}, {D5, C5}, {B4, B6}, {B4, A4}, {B4, C4},
			{B4, B2}, {E4, E5}, {E4, F4}, {C3, C4}, {C3, D3}, {E3, D3}, {D2, B2}, {D2, F2}
		};
		int[][] expectedMovesThatCompleteAMill = {{A7, D7}, {D5, E5}, {D2, D3}};
		int[] expectedCaptures = {G7, G4, A1, D1, G1};
		assertShiftMovesAreAsExpected(validMoves, expectedMoves, expectedMovesThatCompleteAMill, expectedCaptures);
	}
	
	public void testShouldGetValidFlyingShiftMovesForTestBoard() throws Exception {
		board.fromString(TEST_BOARD_WITH_ONLY_3_WHITE_PIECES);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		List<Move> validMoves = board.getValidMoves(moveRanker, 5);
		assertEquals(49, validMoves.size());
		assertShiftFromsAreValid(validMoves);
		assertShiftMovesAreValid(validMoves);
		assertCapturesAreValid(validMoves);
		int[][] expectedMoves = {
			{A7, C5}, {A7, D5}, {A7, E5}, {A7, C4}, {A7, E4}, {A7, F4}, {A7, G4}, {A7, C3},
			{A7, D3}, {A7, E3}, {A7, B2}, {A7, D2}, {A7, F2}, {A7, A1}, {A7, D1}, {A7, G1},
			{D7, C5}, {D7, D5}, {D7, E5}, {D7, C4}, {D7, E4}, {D7, F4}, {D7, G4}, {D7, C3},
			{D7, D3}, {D7, E3}, {D7, B2}, {D7, D2}, {D7, F2}, {D7, D1}, {D7, G1}, {A4, C5},
			{A4, D5}, {A4, E5}, {A4, C4}, {A4, E4}, {A4, F4}, {A4, G4}, {A4, C3}, {A4, D3},
			{A4, E3}, {A4, B2}, {A4, D2}, {A4, F2}, {A4, A1}, {A4, D1}, {A4, G1}
		};
		int[][] expectedMovesThatCompleteAMill = {{D7, A1}};
		int[] expectedCaptures = {G7, B4};
		assertShiftMovesAreAsExpected(validMoves, expectedMoves, expectedMovesThatCompleteAMill, expectedCaptures);
	}
	
	public void testShouldGetValidFlyingShiftMovesForTestBoardWhereOpponentHasAllPiecesInMills() throws Exception {
		board.fromString(TEST_BOARD_WITH_ONLY_3_WHITE_PIECES_AND_BLACK_HAVING_ALL_PIECES_IN_MILLS);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		List<Move> validMoves = board.getValidMoves(moveRanker, 5);
		assertEquals(56, validMoves.size());
		assertShiftFromsAreValid(validMoves);
		assertShiftMovesAreValid(validMoves);
		assertCapturesAreValid(validMoves);
		int[][] expectedMoves = {
			{A7, G7}, {A7, C5}, {A7, D5}, {A7, E5}, {A7, C4}, {A7, E4}, {A7, F4}, {A7, G4},
			{A7, C3}, {A7, D3}, {A7, E3}, {A7, D2}, {A7, F2}, {A7, A1}, {A7, D1}, {A7, G1},
			{D7, G7}, {D7, C5}, {D7, D5}, {D7, E5}, {D7, C4}, {D7, E4}, {D7, F4}, {D7, G4},
			{D7, C3}, {D7, D3}, {D7, E3}, {D7, D2}, {D7, F2}, {D7, D1}, {D7, G1}, {A4, C5},
			{A4, D5}, {A4, E5}, {A4, C4}, {A4, E4}, {A4, F4}, {A4, G4}, {A4, C3}, {A4, D3},
			{A4, E3}, {A4, D2}, {A4, F2}, {A4, A1}, {A4, D1}, {A4, G1}
		};
		int[][] expectedMovesThatCompleteAMill = {{D7, A1}, {A4, G7}};
		int[] expectedCaptures = {B6, D6, F6, B4, B2};
		assertShiftMovesAreAsExpected(validMoves, expectedMoves, expectedMovesThatCompleteAMill, expectedCaptures);
	}
	
	public void testShouldIndicateUserCanMoveWhenPlacementMovesAreAvailable() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		assertTrue(board.canMove());
	}
	
	public void testShouldIndicateUserCanMoveWhenShiftMovesAreAvailable() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		assertTrue(board.canMove());
	}
	
	public void testShouldIndicateUserCannotMoveWhenAllShiftMovesAreBlocked() throws Exception {
		board.fromString(TEST_BOARD_WITH_WHITE_BLOCKED);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		assertFalse(board.canMove());
	}
	
	public void testShouldIndicateUserCanMoveWhenFlyingShiftMovesAreAvailable() throws Exception {
		board.fromString(TEST_BOARD_WITH_ONLY_3_WHITE_PIECES);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		assertTrue(board.canMove());
	}
	
	public void testShouldIndicateUserCanMoveWhenFlyingShiftMovesAreAvailableEvenThoughNoSlidingShiftMovesAre() throws Exception {
		board.fromString(TEST_BOARD_WITH_ONLY_3_WHITE_PIECES);
		board.placePiece(A1, Colour.BLACK);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		assertTrue(board.canMove());
	}
	
	public void testShouldIndicateUserCannotMoveWhenGameOver() throws Exception {
		board.fromString(TEST_BOARD_WITH_ONLY_3_WHITE_PIECES);
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		board.placePiece(D2, Colour.BLACK);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.BLACK));
		NineMensMorrisMove move = moveFactory.createShiftMove(D2, B2, D7);
		assertTrue(board.isValidMove(move));
		board.makeMove(move, null, false); // this move wins the game
		assertFalse(board.canMove());
		assertEquals(NineMensMorrisPlayer.getInstance(Colour.BLACK), board.getWinner());
	}
	
	public void testShouldIndicateValidPlacementMovesAreValid() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		assertTrue(board.isValidMove(moveFactory.createPlacementMove(A4)));
		assertTrue(board.isValidMove(moveFactory.createPlacementMove(D3, D7)));
	}

	public void testShouldIndicateInvalidPlacementMovesAreInvalid() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		assertFalse("square already filled with own piece should be invalid", board.isValidMove(moveFactory.createPlacementMove(A7)));
		assertFalse("square already filled with opponent piece should be invalid", board.isValidMove(moveFactory.createPlacementMove(D7)));
		assertFalse("when not making a mill, capture specified should be invalid", board.isValidMove(moveFactory.createPlacementMove(A4, D7)));
		assertFalse("when making a mill, capture not specified should be invalid", board.isValidMove(moveFactory.createPlacementMove(D3)));
		assertFalse("when making a mill, capture of own piece should be invalid", board.isValidMove(moveFactory.createPlacementMove(D3, A7)));
		assertFalse("when making a mill, capture of opponent piece in mill should be invalid", board.isValidMove(moveFactory.createPlacementMove(D3, G7)));
	}
	
	public void testShouldIndicateValidShiftMovesAreValid() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		assertTrue(board.isValidMove(moveFactory.createShiftMove(A7, A4)));
		assertTrue(board.isValidMove(moveFactory.createShiftMove(D2, D3, D7)));
	}
	
	public void testShouldIndicateInvalidShiftMovesAreInvalid() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		assertFalse("playing from an empty square should be invalid", board.isValidMove(moveFactory.createShiftMove(C5, C4)));
		assertFalse("playing to a square already filled with own piece should be invalid", board.isValidMove(moveFactory.createShiftMove(D5, D6)));
		assertFalse("playing to a square already filled with opponent piece should be invalid", board.isValidMove(moveFactory.createShiftMove(D5, E5)));
		assertFalse("playing to the same square should be invalid", board.isValidMove(moveFactory.createShiftMove(D5, D5)));
		assertFalse("playing a flying move when more than 3 pieces on board should be invalid", board.isValidMove(moveFactory.createShiftMove(D5, D3)));
		assertFalse("when not making a mill, capture specified should be invalid", board.isValidMove(moveFactory.createShiftMove(A7, A4, E5)));
		assertFalse("when making a mill, capture not specified should be invalid", board.isValidMove(moveFactory.createShiftMove(D2, D3)));
		assertFalse("when making a mill, capture of own piece should be invalid", board.isValidMove(moveFactory.createShiftMove(D2, D3, A7)));
		assertFalse("when making a mill, capture of opponent piece in mill should be invalid", board.isValidMove(moveFactory.createShiftMove(D2, D3, G7)));
	}
	
	public void testShouldIndicateValidFlyingShiftMovesAreValid() throws Exception {
		board.fromString(TEST_BOARD_WITH_ONLY_3_WHITE_PIECES);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		assertTrue(board.isValidMove(moveFactory.createShiftMove(A7, A1)));
		assertTrue(board.isValidMove(moveFactory.createShiftMove(A4, A1)));
		assertTrue(board.isValidMove(moveFactory.createShiftMove(D7, A1, B4)));
	}
	
	public void testShouldIndicateInvalidFlyingShiftMovesAreInvalid() throws Exception {
		board.fromString(TEST_BOARD_WITH_ONLY_3_WHITE_PIECES);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.setInHandCount(Colour.WHITE, 0);
		assertFalse("playing from an empty square should be invalid", board.isValidMove(moveFactory.createShiftMove(A1, G1)));
		assertFalse("playing to a square already filled with own piece should be invalid", board.isValidMove(moveFactory.createShiftMove(D7, A7)));
		assertFalse("playing to a square already filled with opponent piece should be invalid", board.isValidMove(moveFactory.createShiftMove(D7, G7)));
		assertFalse("when not making a mill, capture specified should be invalid", board.isValidMove(moveFactory.createShiftMove(A7, A1, B4)));
		assertFalse("when making a mill, capture not specified should be invalid", board.isValidMove(moveFactory.createShiftMove(D7, A1)));
		assertFalse("when making a mill, capture of own piece should be invalid", board.isValidMove(moveFactory.createShiftMove(D7, A1, A7)));
		assertFalse("when making a mill, capture of opponent piece in mill should be invalid", board.isValidMove(moveFactory.createShiftMove(D7, A1, D6)));
	}
	
	public void testShouldPlacePiece() throws Exception {
		board.placePiece(B6, Colour.WHITE);
		assertEquals(Colour.WHITE, board.getColour(B6));
	}
	
	public void testShouldCapturePiece() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.placePiece(B6, Colour.WHITE);
		assertEquals(9, board.countPieces(Colour.WHITE));
		board.capturePiece(B6);
		assertEquals(8, board.countPieces(Colour.WHITE));
	}
	
	public void testShouldShiftPiece() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.placePiece(F2, Colour.WHITE);
		assertEquals(Colour.NONE, board.getColour(F4));
		board.shiftPiece(F2, F4);
		assertEquals(Colour.NONE, board.getColour(F2));
		assertEquals(Colour.WHITE, board.getColour(F4));
	}
	
	public void testShouldFlyingShiftPiece() throws Exception {
		board.fromString(TEST_BOARD_WITH_ONLY_3_WHITE_PIECES);
		board.setInHandCount(Colour.WHITE, 0);
		assertEquals(Colour.WHITE, board.getColour(D7));
		assertEquals(Colour.NONE, board.getColour(A1));
		board.shiftPiece(D7, A1);
		assertEquals(Colour.NONE, board.getColour(D7));
		assertEquals(Colour.WHITE, board.getColour(A1));
	}
	
	public void testShouldIndicateHolesCorrectly() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		board.capturePiece(G4);
		assertTrue(board.isHole(Colour.WHITE, D3));
		assertTrue(board.isHole(Colour.BLACK, G4));
		assertFalse(board.isHole(Colour.WHITE, A7));
		assertFalse(board.isHole(Colour.WHITE, D7));
		assertFalse(board.isHole(Colour.WHITE, B6));
		assertFalse(board.isHole(Colour.WHITE, G4));
		board.placePiece(G4, Colour.WHITE);
		assertFalse(board.isHole(Colour.WHITE, G4));
		assertFalse(board.isHole(Colour.BLACK, G4));
	}
	
	public void testShouldCountMillsAndHolesAndPiecesAdjacentToHolesCorrectlyWithNoSharedPiecesOrHoles() throws Exception {
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		board.placePiece(A7, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		board.placePiece(G7, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 1, 0);
		board.placePiece(D6, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 1, 1);
		board.placePiece(D7, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 1, 1, 0);
	}
	
	public void testShouldCountMillsAndHolesAndPiecesAdjacentToHolesCorrectlyWithSeveralPiecesAdjacentToTheSameHole() throws Exception {
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		board.placePiece(D7, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		board.placePiece(B6, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		board.placePiece(D5, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 1, 1);
		board.placePiece(F6, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 1, 4);
		// and fill the hole
		board.placePiece(D6, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 2, 0, 0);
	}
	
	public void testShouldCountMillsAndHolesAndPiecesAdjacentToHolesCorrectlyWithOnePieceAdjacentToTwoHoles() throws Exception {
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		board.placePiece(A7, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		board.placePiece(C5, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		board.placePiece(G7, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 1, 0);
		board.placePiece(E5, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 2, 0);
		board.placePiece(D6, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 2, 1);
		// and fill the holes
		board.placePiece(D7, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 1, 1, 3);
		board.placePiece(D5, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 3, 0, 0);
	}
	
	public void testShouldCountMillsAndHolesAndPiecesAdjacentToHolesCorrectlyWithPiecesOfBothColoursSharingAHole() throws Exception {
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.BLACK, 0, 0, 0);
		board.placePiece(D7, Colour.BLACK);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.BLACK, 0, 0, 0);
		board.placePiece(B6, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.BLACK, 0, 0, 0);
		board.placePiece(D5, Colour.BLACK);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 0, 0);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.BLACK, 0, 1, 0);
		board.placePiece(F6, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 0, 1, 0);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.BLACK, 0, 1, 0);
		// and fill the hole
		board.placePiece(D6, Colour.WHITE);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.WHITE, 1, 0, 0);
		assertMillsAndHolesAndAdjacentsAreAsExpected(Colour.BLACK, 0, 0, 0);
	}

	public void testShouldReturnCorrectCountForNumberOfMillsAndPiecesInMillsWhenPlacingPieces() throws Exception {
		board.fromString(TEST_BOARD_WITH_WHITE_HAVING_2_MILLS_WITH_SHARED_PIECE);
		assertEquals(2, board.countMills(Colour.WHITE));
		board.placePiece(D5, Colour.WHITE);
		assertEquals(2, board.countMills(Colour.WHITE));
		board.placePiece(D6, Colour.WHITE);
		assertEquals(4, board.countMills(Colour.WHITE));
	}
	
	public void testShouldReturnCorrectCountForNumberOfMillsAndPiecesInMillsWhenShiftingPieces() throws Exception {
		board.fromString(TEST_BOARD_WITH_WHITE_HAVING_2_MILLS_WITH_SHARED_PIECE);
		board.setInHandCount(Colour.WHITE, 0);
		board.shiftPiece(D7, D6);
		assertEquals(2, board.countMills(Colour.WHITE));
		board.shiftPiece(D6, D5);
		assertEquals(1, board.countMills(Colour.WHITE));
		board.shiftPiece(A4, B4);
		assertEquals(0, board.countMills(Colour.WHITE));
	}
	
	public void testShouldReturnCorrectCountForNumberOfMillsAndPiecesInMillsWhenCapturingPieces() throws Exception {
		board.fromString(TEST_BOARD_WITH_WHITE_HAVING_2_MILLS_WITH_SHARED_PIECE);
		board.placePiece(D6, Colour.WHITE);
		assertEquals(3, board.countMills(Colour.WHITE));
		board.capturePiece(A7);
		assertEquals(1, board.countMills(Colour.WHITE));
		board.capturePiece(D6);
		assertEquals(0, board.countMills(Colour.WHITE));
	}
	
	public void testShouldReturnCorrectMobilityScoreWhenPlacingPieces() throws Exception {
		assertEquals(0, board.getMobility(Colour.WHITE));
		board.placePiece(A7, Colour.WHITE);
		assertEquals(2, board.getMobility(Colour.WHITE));
		board.placePiece(D6, Colour.WHITE);
		assertEquals(6, board.getMobility(Colour.WHITE));
		board.placePiece(D7, Colour.WHITE);
		assertEquals(5, board.getMobility(Colour.WHITE));
		board.placePiece(A1, Colour.WHITE);
		assertEquals(7, board.getMobility(Colour.WHITE));
		board.placePiece(D1, Colour.BLACK);
		assertEquals(6, board.getMobility(Colour.WHITE));
		assertEquals(2, board.getMobility(Colour.BLACK));
	}
	
	public void testShouldReturnCorrectMobilityScoreWhenShiftingPieces() throws Exception {
		board.placePiece(A7, Colour.WHITE);
		assertEquals(2, board.getMobility(Colour.WHITE));
		board.shiftPiece(A7, D7);
		assertEquals(3, board.getMobility(Colour.WHITE));
		board.shiftPiece(D7, D6);
		assertEquals(4, board.getMobility(Colour.WHITE));
		board.placePiece(A7, Colour.BLACK);
		assertEquals(4, board.getMobility(Colour.WHITE));
		board.shiftPiece(A7, D7);
		assertEquals(3, board.getMobility(Colour.WHITE));
		assertEquals(2, board.getMobility(Colour.BLACK));
		board.shiftPiece(D6, D5);
		assertEquals(3, board.getMobility(Colour.WHITE));
		assertEquals(3, board.getMobility(Colour.BLACK));
	}
	
	public void testShouldReturnCorrectMobilityScoreWhenCapturingPieces() throws Exception {
		board.placePiece(A7, Colour.WHITE);
		board.placePiece(D6, Colour.WHITE);
		board.placePiece(D7, Colour.WHITE);
		board.placePiece(A1, Colour.WHITE);
		board.placePiece(A4, Colour.BLACK);
		assertEquals(5, board.getMobility(Colour.WHITE));
		board.capturePiece(A7);
		assertEquals(6, board.getMobility(Colour.WHITE));
		board.capturePiece(A4);
		assertEquals(7, board.getMobility(Colour.WHITE));
		board.capturePiece(D7);
		assertEquals(6, board.getMobility(Colour.WHITE));
	}
	
	public void testGameShouldBeDrawnIf20MovesAreMadeWithoutAPieceBeingCaptured() throws Exception {
		board.fromString(TEST_BOARD_WITH_3_PIECES_OF_EACH_COLOUR);
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		// place a piece so it's not 3-on-3
		board.placePiece(13, Colour.BLACK);
		// place and capture a piece to reset capture counter
		board.placePiece(B6, Colour.WHITE);
		board.capturePiece(B6);
		// play 20 moves
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		for (int i = 1; i <= 5; i++) {
			assertFalse(board.isGameOver());
			board.playMove(moveFactory.createMoveFromString("g1g4"), null, false);
			assertFalse(board.isGameOver());
			board.playMove(moveFactory.createMoveFromString("d7d6"), null, false);
			assertFalse(board.isGameOver());
			board.playMove(moveFactory.createMoveFromString("g4g1"), null, false);
			assertFalse(board.isGameOver());
			board.playMove(moveFactory.createMoveFromString("d6d7"), null, false);
		}
		assertTrue(board.isGameOver());
	}
	
	public void testRecreateBugWhereMoveIntoMillDoesNotRealiseItShouldCaptureAPiece() throws Exception {
		board.fromString(TEST_BOARD_TO_RECREATE_BUG_WHERE_MOVE_INTO_MILL_DOES_NOT_REALISE_IT_SHOULD_CAPTURE_A_PIECE);
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		board.playMove(moveFactory.createMoveFromString("c5d5"), null, false);
		board.playMove(moveFactory.createMoveFromString("f4f6"), null, false);
		board.playMove(moveFactory.createMoveFromString("c3c4 -g4"), null, false);
		board.playMove(moveFactory.createMoveFromString("d6b6"), null, false);
		assertFalse(board.isValidMove(moveFactory.createMoveFromString("c4c3")));
		List<Move> validMoves = board.getValidMoves(moveRanker, 5);
		assertFalse(validMoves.contains(moveFactory.createMoveFromString("c4c3")));
	}
	
	public void testRecreateBugWhereMoveResultsInOpponentHavingNoMovesDoesNoSetGameOver() throws Exception {
		board.fromString(TEST_BOARD_TO_RECREATE_BUG_WHERE_MOVE_RESULTS_IN_OPPONENT_HAVING_NO_MOVES_DOES_NOT_SET_GAMEOVER);
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.BLACK));
		board.playMove(moveFactory.createMoveFromString("a4a1 -b4"), null, false);
		assertTrue(board.isGameOver());
	}
	
	public void testRecreateBugWhereCanMoveReturnsTrueWhenUserIsBlocked() throws Exception {
		board.fromString(TEST_BOARD_TO_RECREATE_BUG_WHERE_MOVE_RESULTS_IN_OPPONENT_HAVING_NO_MOVES_DOES_NOT_SET_GAMEOVER);
		board.setInHandCount(Colour.WHITE, 0);
		board.setInHandCount(Colour.BLACK, 0);
		board.capturePiece(3);
		board.capturePiece(7);
		board.setCurrentPlayer(NineMensMorrisPlayer.getInstance(Colour.WHITE));
		assertFalse(board.canMove());
	}
	
	public void testShouldCalculateDegreesOfFreedomForPositions() throws Exception {
		board.fromString(TEST_BOARD_WITH_8_PIECES_OF_EACH_COLOUR);
		assertEquals(0, board.getDegreesOfFreedomForPosition(G7));
		assertEquals(1, board.getDegreesOfFreedomForPosition(A7));
		assertEquals(1, board.getDegreesOfFreedomForPosition(D6));
		assertEquals(1, board.getDegreesOfFreedomForPosition(F2));
		assertEquals(2, board.getDegreesOfFreedomForPosition(C3));
		assertEquals(3, board.getDegreesOfFreedomForPosition(D2));
		assertEquals(4, board.getDegreesOfFreedomForPosition(B4));
	}
	
	private void assertPlacementMovesAreValid(List<Move> moves) {
		for (Move move : moves) {
			final NineMensMorrisMove nineMensMorrisMove = (NineMensMorrisMove) move;
			assertTrue(nineMensMorrisMove.isPlacement());
			assertTrue(board.isValidPlacement(nineMensMorrisMove.getPosition()));
		}
	}
	
	private void assertShiftFromsAreValid(List<Move> moves) {
		for (Move move : moves) {
			final NineMensMorrisMove nineMensMorrisMove = (NineMensMorrisMove) move;
			assertFalse(nineMensMorrisMove.isPlacement());
			assertTrue(board.isValidShiftFrom(nineMensMorrisMove.getFromPosition()));
		}
	}
	private void assertShiftMovesAreValid(List<Move> moves) {
		for (Move move : moves) {
			final NineMensMorrisMove nineMensMorrisMove = (NineMensMorrisMove) move;
			assertFalse(nineMensMorrisMove.isPlacement());
			assertTrue(board.isValidShift(nineMensMorrisMove.getFromPosition(), nineMensMorrisMove.getToPosition()));
		}
	}
	
	private void assertCapturesAreValid(List<Move> moves) {
		for (Move move : moves) {
			final NineMensMorrisMove nineMensMorrisMove = (NineMensMorrisMove) move;
			if (nineMensMorrisMove.hasCapture()) {
				assertTrue(board.isValidCapture(nineMensMorrisMove.getCapturePosition()));
			}
		}
	}
	
	private void assertPlacementMovesAreAsExpected(List<Move> validMoves, int[] expectedMovesThatDontCompleteAMill, int[] expectedMovesThatCompleteAMill, int[] expectedCaptures) {
		for (int i = 0; i < expectedMovesThatDontCompleteAMill.length; i++) {
			NineMensMorrisMove move = moveFactory.createPlacementMove(expectedMovesThatDontCompleteAMill[i]);
			assertTrue("Expected move " + expectedMovesThatDontCompleteAMill[i], validMoves.contains(move));
		}
		for (int i = 0; i < expectedMovesThatCompleteAMill.length; i++) {
			for (int j = 0; j < expectedCaptures.length; j++) {
				NineMensMorrisMove move = moveFactory.createPlacementMove(expectedMovesThatCompleteAMill[i], expectedCaptures[j]);
				assertTrue("Expected move " + expectedMovesThatCompleteAMill[i] + " -" + expectedCaptures[j], validMoves.contains(move));
			}
		}
	}
	
	private void assertShiftMovesAreAsExpected(List<Move> validMoves, int[][] expectedMoves, int[][] expectedMovesThatCompleteAMill, int[] expectedCaptures) {
		for (int i = 0; i < expectedMoves.length; i++) {
			NineMensMorrisMove move = moveFactory.createShiftMove(expectedMoves[i][0], expectedMoves[i][1]);
			assertTrue("Expected move from " + expectedMoves[i][0] + " to " + expectedMoves[i][1], validMoves.contains(move));
		}
		for (int i = 0; i < expectedMovesThatCompleteAMill.length; i++) {
			for (int j = 0; j < expectedCaptures.length; j++) {
				NineMensMorrisMove move = moveFactory.createShiftMove(expectedMovesThatCompleteAMill[i][0], expectedMovesThatCompleteAMill[i][1], expectedCaptures[j]);
				assertTrue("Expected move from " + expectedMovesThatCompleteAMill[i][0] + " to " + expectedMovesThatCompleteAMill[i][1] + " -" + expectedCaptures[j], validMoves.contains(move));
			}
		}
	}
	
	private void assertMillsAndHolesAndAdjacentsAreAsExpected(int colour, int mills, int holes, int adjacents) {
		assertEquals("Wrong number of mills", mills, board.countMills(colour));
		assertEquals("Wrong number of holes", holes, board.countHoles(colour));
		assertEquals("Wrong number of adjacents", adjacents, board.countAdjacents(colour));
	}
}
