/*
 * @(#)Input.java		2005/11/19
 *
 * Part of the common console classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

/**
 * Common console input functionality.
 * @author mpatric
 */
public class Input {
	
	private static final String PROMPT = "> ";

	protected Output output;
	
	public Input() {
		output = new Output();
	}
	
	/**
	 * Allow the user to select an option from a list.
	 * @param prompt The prompt to display.
	 * @param options A list of Strings, which are the items the user can select from.
	 * @param defaultOption The index of the default option if the user doesn't enter anything (0 to <code>options.size()</code> - 1).
	 * @return Index of the selected option plus one (1 to <code>options.size()</code>).
	 */
	public int selectFromList(String prompt, List<String> options, int defaultOption) {
		if (prompt != null) {
			output.println(prompt);
		}
		Iterator<String> iterator = options.iterator();
		int i = 1;
		while (iterator.hasNext()) {
			String option = iterator.next();
			output.println(i + " - " + option);
			i++;
		}
		while (true) {
			output.print(PROMPT);
			InputStreamReader inReader = new InputStreamReader(System.in);
			BufferedReader stdin = new BufferedReader(inReader);
			try {
				String line = stdin.readLine();
				int selection = 0;
				if (line == null || line.length() == 0) selection = defaultOption;
				else selection = Integer.parseInt(line);
				if (selection >= 1 && selection <= options.size()) {
					return selection;
				}
			} catch (IOException ioe) {
				// do nothing
			} catch (NumberFormatException nfe) {
				// do nothing
			}
			output.println("Invalid option");
		}
	}
	
	/**
	 * Allow the user to input an integer.
	 * @param prompt The prompt to display.
	 * @param defaultNumber The default number if the user doesn't enter anything.
	 * @param minNumber The minimum number.
	 * @param maxNumber The maximum number.
	 * @return The entered number, or the default if the user doesn't enter anything.
	 */
	public long enterInteger(String prompt, long defaultNumber, long minNumber, long maxNumber) {
		if (prompt != null) {
			output.println(prompt);
		}
		while (true) {
			output.print(PROMPT);
			InputStreamReader inReader = new InputStreamReader(System.in);
			BufferedReader stdin = new BufferedReader(inReader);
			try {
				String line = stdin.readLine();
				long number = 0;
				if (line == null || line.length() == 0) {
					number = defaultNumber;
				} else {
					number = Integer.parseInt(line);
				}
				if (number >= minNumber && number <= maxNumber) {
					return number;
				}
			} catch (IOException ioe) {
				// do nothing
			} catch (NumberFormatException nfe) {
				// do nothing
			}
			output.println("Invalid amount");
		}
	}
	
	/**
	 * Allow the user to input a string.
	 * @param prompt The prompt to display.
	 * @param defaultString The default string if the user doesn't enter anything.
	 * @return The entered string, or the default if the user doesn't enter anything.
	 */
	public String enterString(String prompt, String defaultString) {
		if (prompt != null) {
			output.println(prompt);
		}
		while (true) {
			output.print(PROMPT);
			InputStreamReader inReader = new InputStreamReader(System.in);
			BufferedReader stdin = new BufferedReader(inReader);
			try {
				String line = stdin.readLine();
				if (line == null || line.length() == 0) return defaultString;
				return line;
			} catch (IOException ioe) {
				// do nothing
			}
			output.println("Invalid entry");
		}
	}
}
