package net.lurgee.ninemensmorris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NineMensMorrisBoardLinks {
	
	private static final int[][] LINK_MAP = new int[NineMensMorrisBoard.NUMBER_OF_POSITIONS][];
	private static final int[][] LINKS = {
		{0, 4}, {0, 17}, {0, 8}, {0, 16}, {1, 17}, {1, 5}, {1, 18}, {1, 9},
		{2, 10}, {2, 18}, {2, 6}, {2, 19}, {3, 16}, {3, 11}, {3, 19}, {3, 7},
		{4, 13}, {4, 12}, {5, 13}, {5, 14}, {6, 14}, {6, 15}, {7, 12}, {7, 15},
		{8, 21}, {8, 20}, {9, 21}, {9, 22}, {10, 22}, {10, 23}, {11, 20}, {11, 23}
	};
	
	protected static final int[][] POSITIONS_TO_LINES = new int[NineMensMorrisBoard.NUMBER_OF_POSITIONS][];
	protected static final int[][] LINES_TO_POSITIONS = {
		{4, 0, 8}, {5, 1, 9}, {6, 2, 10}, {7, 3, 11},
		{16, 0, 17}, {17, 1, 18}, {18, 2, 19}, {19, 3, 16},
		{12, 4, 13}, {13, 5, 14}, {14, 6, 15}, {15, 7, 12},
		{20, 8, 21}, {21, 9, 22}, {22, 10, 23}, {23, 11, 20}
	};

	private static final int[][] ADJACENT_POSITIONS_TO_LINES = new int[LINES_TO_POSITIONS.length][];
	
	static {
		Map<Integer,Set<Integer>> linkMap = new HashMap<Integer,Set<Integer>>();
		for (int i = 0; i < LINKS.length; i++) {
			addToLinkMap(linkMap, LINKS[i][0], LINKS[i][1]);
			addToLinkMap(linkMap, LINKS[i][1], LINKS[i][0]);
		}
		for (int i = 0; i < NineMensMorrisBoard.NUMBER_OF_POSITIONS; i++) {
			Set<Integer> linkSet = linkMap.get(i);
			LINK_MAP[i] = new int[linkSet.size()];
			int j = 0;
			for (Integer link : linkSet) {
				LINK_MAP[i][j++] = link;
			}
		}
		for (int position = 0; position < NineMensMorrisBoard.NUMBER_OF_POSITIONS; position++) {
			addLinesToPosition(position);
		}
		Map<Integer,Set<Integer>> adjacentMap = new HashMap<Integer,Set<Integer>>();
		for (int line = 0; line < ADJACENT_POSITIONS_TO_LINES.length; line++) {
			int[] positionsInThisLine = LINES_TO_POSITIONS[line];
			for (int i = 0; i < positionsInThisLine.length; i++) {
				int[] linksTo = linksTo(positionsInThisLine[i]);
				for (int positionLinkedTo : linksTo) {
					if (!arrayContains(positionsInThisLine, positionLinkedTo)) {
						addToAdjacentMap(adjacentMap, line, positionLinkedTo);
					}
				}
			}
		}
		for (int line = 0; line < ADJACENT_POSITIONS_TO_LINES.length; line++) {
			Set<Integer> adjacentSet = adjacentMap.get(line);
			ADJACENT_POSITIONS_TO_LINES[line] = new int[adjacentSet.size()];
			int j = 0;
			for (Integer adjacentPosition : adjacentSet) {
				ADJACENT_POSITIONS_TO_LINES[line][j++] = adjacentPosition;
			}
		}
	}

	private static void addToLinkMap(Map<Integer,Set<Integer>> linkMap, int position1, int position2) {
		Set<Integer> set = linkMap.get(position1);
		if (set == null) {
			set = new HashSet<Integer>();
			set.add(position2);
			linkMap.put(position1, set);
		} else {
			set.add(position2);
		}
	}

	private static void addToAdjacentMap(Map<Integer, Set<Integer>> adjacentMap, int line, int adjacentPosition) {
		Set<Integer> set = adjacentMap.get(line);
		if (set == null) {
			set = new HashSet<Integer>();
			set.add(adjacentPosition);
			adjacentMap.put(line, set);
		} else {
			set.add(adjacentPosition);
		}
	}

	private static boolean arrayContains(int[] intArray, int i) {
		for (int intInArray : intArray) {
			if (intInArray == i) {
				return true;
			}
		}
		return false;
	}
	
	private static void addLinesToPosition(int position) {
		List<Integer> linesForPosition = new ArrayList<Integer>();
		for (int j = 0; j < LINES_TO_POSITIONS.length; j++) {
			for (int k = 0; k < LINES_TO_POSITIONS[j].length; k++) {
				if (LINES_TO_POSITIONS[j][k] == position) {
					linesForPosition.add(j);
				}
			}
		}
		POSITIONS_TO_LINES[position] = new int[linesForPosition.size()];
		int index = 0;
		for (Integer line : linesForPosition) {
			POSITIONS_TO_LINES[position][index++] = line;
		}
	}
	
	public static int[] linksTo(int position) {
		return LINK_MAP[position];
	}
	
	protected static int[] getLinesForPosition(int position) {
		return POSITIONS_TO_LINES[position];
	}

	public static int[] getPositionsForLine(int line) {
		return LINES_TO_POSITIONS[line];
	}
	
	public static int[] getAdjacentPositionsToLine(int line) {
		return ADJACENT_POSITIONS_TO_LINES[line];
	}

	protected static boolean isInLine(int position, int line) {
		for (int positionLine : POSITIONS_TO_LINES[position]) {
			if (positionLine == line) {
				return true;
			}
		}
		return false;
	}

	public static boolean areLinked(int fromPosition, int toPosition) {
		for (int linkPosition : LINK_MAP[fromPosition]) {
			if (linkPosition == toPosition) {
				return true;
			}
		}
		return false;
	}
}
