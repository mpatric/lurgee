/*
 * @(#)NineMensMorrisPosition.java		2008/01/21
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import java.util.HashMap;
import java.util.Map;

import net.lurgee.sgf.Position;

/**
 * Nine men's morris position. This is simply a position on the board described by its position number (1 - 24).
 * @author mpatric
 */
public class NineMensMorrisPosition implements Position {

	protected static final NineMensMorrisPosition[] POSITIONS = new NineMensMorrisPosition[NineMensMorrisBoard.NUMBER_OF_POSITIONS];
	private static final String[] POSITION_NAMES = {
		"d6", "f4", "d2", "b4", "d7", "g4", "d1", "a4",
		"d5", "e4", "d3", "c4", "a7", "g7", "g1", "a1",
		"b6", "f6", "f2", "b2", "c5", "e5", "e3", "c3"
	};
	private static final Map<String, Integer> NAME_MAP = new HashMap<String, Integer>();
	private static final int[] POSITIONS_BY_INDEX = {
		12, 4, 13, 16, 0, 17, 20, 8, 21, 7, 3, 11, 9, 1, 5, 23, 10, 22, 19, 2, 18, 15, 6, 14
	};
	
	private static final int[][] POSITIONS_BY_COORDINATE = {
		{12, -1, -1,  4, -1, -1, 13},		
		{-1, 16, -1,  0, -1, 17, -1},
		{-1, -1, 20,  8, 21, -1, -1},
		{ 7,  3, 11, -1,  9,  1,  5},
		{-1, -1, 23, 10, 22, -1, -1},
		{-1, 19, -1,  2, -1, 18, -1},
		{15, -1, -1,  6, -1, -1, 14}
	};
	private static final int[][] COORDINATES_BY_POSITION = new int[NineMensMorrisBoard.NUMBER_OF_POSITIONS][];
	
	private final int position;
	
	public NineMensMorrisPosition(int position) {
		if (position < 0 || position >= NineMensMorrisBoard.NUMBER_OF_POSITIONS) {
			throw new IllegalArgumentException("Invalid board position");
		}
		this.position = position;
	}
	
	public NineMensMorrisPosition(NineMensMorrisPosition position) {
		this.position = position.position;
	}

	public int getNumber() {
		return position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final NineMensMorrisPosition other = (NineMensMorrisPosition) obj;
		if (position != other.position) return false;
		return true;
	}

	@Override
	public String toString() {
		return lookupPositionFromNumber(position);
	}
	
	static {
		for (int i = 0; i < NineMensMorrisBoard.NUMBER_OF_POSITIONS; i++) {
			POSITIONS[i] = new NineMensMorrisPosition(i);
		}
		for (int i = 0; i < POSITION_NAMES.length; i++) {
			NAME_MAP.put(POSITION_NAMES[i], i);
		}
		for (int i = 0; i < POSITIONS_BY_COORDINATE.length; i++) {
			for (int j = 0; j < POSITIONS_BY_COORDINATE[i].length; j++) {
				if (POSITIONS_BY_COORDINATE[i][j] >= 0) {
					COORDINATES_BY_POSITION[POSITIONS_BY_COORDINATE[j][i]] = new int[] {i, j};
				}
			}
		}
	}
	
	public static int lookupNumberFromName(String name) {
		Integer position = NAME_MAP.get(name);
		if (position != null) {
			return position;
		}
		return 0;
	}

	public static String lookupPositionFromNumber(int position) {
		return POSITION_NAMES[position];
	}

	public static int lookupPositionFromIndex(int index) {
		return POSITIONS_BY_INDEX[index];
	}

	public static int lookupPositionFromCoordinates(int x, int y) {
		int position;
		if (x < 0 || x > NineMensMorrisBoard.X_DIMENSION -1 || y < 0 || y > NineMensMorrisBoard.Y_DIMENSION - 1) {
			position = -1;
		} else {
			position = POSITIONS_BY_COORDINATE[y][x];
		}
		if (position < 0) {
			throw new IllegalArgumentException("Invalid co-ordinate");
		}
		return position;
	}
	
	public static int[] lookupCoordinatesFromPosition(int position) {
		return COORDINATES_BY_POSITION[position];
	}
}
