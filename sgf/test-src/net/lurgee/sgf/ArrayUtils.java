/*
 * @(#)ArrayUtils.java		2007/11/08
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.util.Iterator;
import java.util.List;

/**
 * Utility class used in unit tests to convert lists into arrays.
 * @author mpatric
 */
public class ArrayUtils {

	public static int[] integerArrayListToArray(List<Integer> integers) {
		int[] intArray = new int[integers.size()];
		Iterator<Integer> iterator = integers.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Integer value = iterator.next();
			intArray[i++] = value.intValue();
		}
		return intArray;
	}
	
	public static char[] characterArrayListToArray(List<Character> chars) {
		char[] charArray = new char[chars.size()];
		Iterator<Character> iterator = chars.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Character state = iterator.next();
			charArray[i++] = state;
		}
		return charArray;
	}
}
