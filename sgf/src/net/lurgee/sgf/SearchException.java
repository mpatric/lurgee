/*
 * @(#)SearchException.java		2007/11/11
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.lang.RuntimeException;

/**
 * General-purpose runtime exception for search.
 * @author mpatric
 */
public class SearchException extends RuntimeException {

	private static final long serialVersionUID = -2547074310012400682L;

	public SearchException(String message) {
		super(message);
	}
}
