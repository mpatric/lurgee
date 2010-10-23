/*
 * @(#)Debug.java		2007/05/07
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Generic debug messaging.
 * @author mpatric
 */
public class Debug {

	public static boolean DEBUG = false;
	
	public static void output(String message) {
		if (DEBUG) System.out.println(message);
	}
	
	public static void output(int indent, String message) {
		output(indent(indent) + message);
	}
	
	private static String indent(int depth) { 
		int d = depth - 1;
		if (d < 1) return "";
		if (d > 8) d = 8;
		return "\t\t\t\t\t\t\t\t".substring(0, d);
	}
}
