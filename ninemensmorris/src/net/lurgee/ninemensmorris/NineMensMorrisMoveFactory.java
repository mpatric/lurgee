/*
 * @(#)NineMensMorrisMoveFactory.java		2008/01/21
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import java.util.HashMap;

import net.lurgee.sgf.MoveFactory;

/**
 * Factory class that provides {@link NineMensMorrisPlacementMove} instances specified by the move number (1 - 24) or by a
 * string in the usual nine men's morris notation; for example, "c5" (placement), "c4d3" (shift), "c5 -d7" (placement with
 * capture), "c4d3 -d7" (shift with capture).
 * @author mpatric
 */
public class NineMensMorrisMoveFactory implements MoveFactory {

	private HashMap<Integer, NineMensMorrisMove> moveMap;
	
	public NineMensMorrisMoveFactory() {
		clear();
	}

	public NineMensMorrisMove createPlacementMove(int position) {
		return createMove(position, -1, -1);
	}
	
	public NineMensMorrisMove createPlacementMove(int position, int capturePosition) {
		return createMove(position, -1, capturePosition);
	}

	public NineMensMorrisMove createShiftMove(int fromPosition, int toPosition) {
		return createMove(fromPosition, toPosition, -1);
	}
	
	public NineMensMorrisMove createShiftMove(int fromPosition, int toPosition, int capturePosition) {
		return createMove(fromPosition, toPosition, capturePosition);
	}
	
	@SuppressWarnings("deprecation")
	public NineMensMorrisMove createMove(int position1, int position2, int capturePosition) {
		int key = (position1 + 1) + ((position2 + 1) << 5) + ((capturePosition + 1) << 10);
		NineMensMorrisMove move = (NineMensMorrisMove) moveMap.get(key);
		if (move == null) {
			move = new NineMensMorrisMove(position1, position2, capturePosition);
			moveMap.put(key, move);
		}
		return move;
	}

	public NineMensMorrisMove createMoveFromString(String moveAsString) {
		if (moveAsString.length() == 2) {
			int position = NineMensMorrisPosition.lookupNumberFromName(moveAsString.substring(0, 2));
			return createPlacementMove(position);
		} else if (moveAsString.length() == 4) {
			int fromPosition = NineMensMorrisPosition.lookupNumberFromName(moveAsString.substring(0, 2));
			int toPosition = NineMensMorrisPosition.lookupNumberFromName(moveAsString.substring(2, 4));
			return createShiftMove(fromPosition, toPosition);
		} else if (moveAsString.length() == 6) {
			int position = NineMensMorrisPosition.lookupNumberFromName(moveAsString.substring(0, 2));
			int capturePosition = NineMensMorrisPosition.lookupNumberFromName(moveAsString.substring(4, 6));
			return createPlacementMove(position, capturePosition);
		} else if (moveAsString.length() == 8) {
			int fromPosition = NineMensMorrisPosition.lookupNumberFromName(moveAsString.substring(0, 2));
			int toPosition = NineMensMorrisPosition.lookupNumberFromName(moveAsString.substring(2, 4));
			int capturePosition = NineMensMorrisPosition.lookupNumberFromName(moveAsString.substring(6, 8));
			return createShiftMove(fromPosition, toPosition, capturePosition);
		}
		throw new IllegalArgumentException("Cannot set nine men's morris move from provided string");
	}
	
	protected HashMap<Integer, NineMensMorrisMove> getMoveMap() {
		return moveMap;
	}
	
	public void clear() {
		moveMap = new HashMap<Integer, NineMensMorrisMove>();
	}
}
