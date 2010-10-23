/*
 * @(#)NoMainWindowException.java		2007/06/29
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

/**
 * Exception to indicate an operation that requires a widget to be contained in a MainWindow was attempted on a widget that isn't.
 * @author mpatric
 */
public class NoMainWindowException extends RuntimeException {

	private static final long serialVersionUID = 8040311800174429867L;

	public NoMainWindowException(String message) {
		super(message);
	}
}
