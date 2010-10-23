/*
 * @(#)PoolFullException.java		2005/10/31
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Runtime exception to indicate a pool is full.
 * @author mpatric
 */
public class ObjectPoolFullException extends ObjectPoolException {

	static final long serialVersionUID = -6727883683594336582L;

	public ObjectPoolFullException(String message) {
		super(message);
	}
}
