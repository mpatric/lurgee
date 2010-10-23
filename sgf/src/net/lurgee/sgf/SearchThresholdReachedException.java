/*
 * @(#)SearchThresholdReachedException.java		2007/11/11
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Runtime exception to indicate a search ended due to the threshold being reached.
 * @author mpatric
 */
public class SearchThresholdReachedException extends SearchException {

	private static final long serialVersionUID = -6088715705150993857L;

	public SearchThresholdReachedException(String message) {
		super(message);
	}
}
