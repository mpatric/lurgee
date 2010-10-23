/*
 * @(#)ObjectPoolException.java		2005/11/02
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

import java.lang.RuntimeException;

/**
 * Runtime exception for object pool errors.
 * @author mpatric
 */
public class ObjectPoolException extends RuntimeException {

	static final long serialVersionUID = -8701676077635260292L;

	public ObjectPoolException(String message) {
		super(message);
	}
}
