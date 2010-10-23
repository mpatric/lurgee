/*
 * @(#)NineMensMorrisHumanCompetitor.java		2008/02/02
 *
 * Part of the ninemensmorris console application that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris.console;

import net.lurgee.common.console.HumanCompetitor;
import net.lurgee.ninemensmorris.NineMensMorrisBoard;
import net.lurgee.ninemensmorris.NineMensMorrisMove;
import net.lurgee.ninemensmorris.NineMensMorrisMoveFactory;
import net.lurgee.ninemensmorris.NineMensMorrisPlayer;
import net.lurgee.ninemensmorris.NineMensMorrisPosition;
import net.lurgee.sgf.AbstractBoard;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;

/**
 * Represents a human competitor in the game of nine men's morris. Moves are determined through console input.
 * @author mpatric
 */
public class NineMensMorrisHumanCompetitor extends HumanCompetitor {

	public NineMensMorrisHumanCompetitor(GameContext gameContext, Player player) {
		super(gameContext, player);
	}

	@Override
	public Move determineMove(AbstractBoard board) {
		NineMensMorrisBoard nineMensMorrisBoard = (NineMensMorrisBoard) board;
		NineMensMorrisMove move = null;
		if (nineMensMorrisBoard.countPiecesInHand(((NineMensMorrisPlayer) getPlayer()).getColour()) > 0) {
			int position = determinePlacementPosition(nineMensMorrisBoard);
			if (nineMensMorrisBoard.placementWouldCreateMill(position)) {
				int capturePosition = determineCapturePosition(nineMensMorrisBoard);
				move = ((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createPlacementMove(position, capturePosition);
			} else {
				move = ((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createPlacementMove(position);
			}
		} else {
			while (true) {
				int fromPosition = determineFromPosition(nineMensMorrisBoard);
				int toPosition = determineShift(nineMensMorrisBoard, fromPosition);
				if (toPosition != 0 && nineMensMorrisBoard.isValidShift(fromPosition, toPosition)) {
					if (nineMensMorrisBoard.shiftWouldCreateMill(fromPosition, toPosition)) {
						int capturePosition = determineCapturePosition(nineMensMorrisBoard);
						move = ((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createShiftMove(fromPosition, toPosition, capturePosition);
					} else {
						move = ((NineMensMorrisMoveFactory) gameContext.getMoveFactory()).createShiftMove(fromPosition, toPosition);
					}
					break;
				}
				output.println("Invalid move");
			}
		}
		return move;
	}

	private int determinePlacementPosition(NineMensMorrisBoard board) {
		boolean start = true;
		while (true) {
			String line = input.enterString(start ? "Enter position to place piece" : null, null);
			if (line != null) {
				int position = NineMensMorrisPosition.lookupNumberFromName(line.trim());
				if (position >= 0 && board.isValidPlacement(position)) {
					return position;
				}
			}
			output.println("Invalid position");
			start = false;
		}
	}
	
	private int determineFromPosition(NineMensMorrisBoard board) {
		boolean start = true;
		while (true) {
			String line = input.enterString(start ? "Enter position of piece to move" : null, null);
			if (line != null) {
				int position = NineMensMorrisPosition.lookupNumberFromName(line.trim());
				if (position >= 0 && board.isValidShiftFrom(position)) {
					return position;
				}
			}
			output.println("Invalid position");
			start = false;
		}
	}
	
	private int determineShift(NineMensMorrisBoard board, int fromPosition) {
		String line = input.enterString("Enter position to shift piece to", null);
		if (line != null) {
			int position = NineMensMorrisPosition.lookupNumberFromName(line.trim());
			return position;
		} else {
			return 0;
		}
	}
	
	private int determineCapturePosition(NineMensMorrisBoard board) {
		boolean start = true;
		while (true) {
			String line = input.enterString(start ? "Enter position to capture opponent piece" : null, null);
			if (line != null) {
				int position = NineMensMorrisPosition.lookupNumberFromName(line.trim());
				if (position != 0 && board.isValidCapture(position)) {
					return position;
				}
			}
			output.println("Invalid position");
			start = false;
		}
	}
}
