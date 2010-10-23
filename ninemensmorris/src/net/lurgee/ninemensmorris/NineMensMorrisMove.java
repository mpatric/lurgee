/*
 * @(#)NineMensMorrisMove.java		2008/01/21
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import net.lurgee.sgf.Move;

/**
 * Abstract nine men's morris move. Base class for all move types.
 * @author mpatric
 */
public class NineMensMorrisMove implements Move {
	
	private static final int UNDEFINED = -1;

	protected final int position1;
	protected final int position2;
	protected final int capturePosition;
	
	/**
	 * @deprecated Nine men's morris moves should be obtained from a {@link NineMensMorrisMoveFactory} rather.
	 */
	@Deprecated
	protected NineMensMorrisMove(int position1, int position2, int capturePosition) {
		this.position1 = position1;
		this.position2 = position2;
		this.capturePosition = capturePosition;
	}
	
	/**
	 * @deprecated Nine men's morris moves should be obtained from a {@link NineMensMorrisMoveFactory} rather.
	 */
	public NineMensMorrisMove(NineMensMorrisMove move) {
		this.position1 = move.position1;
		this.position2 = move.position2;
		this.capturePosition = move.capturePosition;
	}

	public int getPosition() {
		return position1;
	}
	
	public int getFromPosition() {
		return position1;
	}
	
	public int getToPosition() {
		return position2;
	}
	
	public int getCapturePosition() {
		return capturePosition;
	}
	
	public boolean isPlacement() {
		return (position2 == UNDEFINED);
	}
	
	public boolean hasCapture() {
		return (capturePosition != UNDEFINED);
	}
	
	@Override
	public String toString() {
		if (position2 == UNDEFINED) {
			if (capturePosition == UNDEFINED) {
				return NineMensMorrisPosition.lookupPositionFromNumber(position1);
			} else {
				return NineMensMorrisPosition.lookupPositionFromNumber(position1) + " -" + NineMensMorrisPosition.lookupPositionFromNumber(capturePosition);
			}
		} else {
			if (capturePosition == UNDEFINED) {
				return NineMensMorrisPosition.lookupPositionFromNumber(position1) + NineMensMorrisPosition.lookupPositionFromNumber(position2);
			} else {
				return NineMensMorrisPosition.lookupPositionFromNumber(position1) + NineMensMorrisPosition.lookupPositionFromNumber(position2) + " -" + NineMensMorrisPosition.lookupPositionFromNumber(capturePosition);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capturePosition;
		result = prime * result + position1;
		result = prime * result + position2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final NineMensMorrisMove other = (NineMensMorrisMove) obj;
		if (capturePosition != other.capturePosition) return false;
		if (position1 != other.position1) return false;
		if (position2 != other.position2) return false;
		return true;
	}
}
