/*
 * @(#)AbortException.java		2005/10/31
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Runtime exception to indicate a search was aborted.
 * @author mpatric
 */
public class AbortException extends SearchException {

	static final long serialVersionUID = 3926921096004031686L;

	public AbortException(String message) {
		super(message);
	}
}
