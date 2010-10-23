/*
 * @(#)Output.java		2007/05/25
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;


/**
 * Common console output functionality.
 * @author mpatric
 */
public class Output {
	
	private static final String PAD_CHARS = "                                ";

	public void println() {
		System.out.println();
	}
	
	public void println(String message) {
		System.out.println(message);
	}

	public void print(String message) {
		System.out.print(message);
	}

	public void printAndPad(String s, int length) {
		int padChars = length - s.length();
		if (padChars > PAD_CHARS.length() - 1) padChars = PAD_CHARS.length() - 1;
		if (padChars > 0) {
			print(s + PAD_CHARS.substring(0, padChars));
		} else {
			print(s);
		}
	}
}
