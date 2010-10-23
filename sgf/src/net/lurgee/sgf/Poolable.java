/*
 * @(#)Poolable.java		2005/10/31
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Interface that classes that will be pooled with an {@link ObjectPool} must implement.
 * @author mpatric
 */
public interface Poolable {

	/**
	 * Initialiser method, called when the object is created, just before it is checked out of the pool.
	 * @param params Arbitrary object which may be used to pass parameters to the initialiser - may be null.
	 * @throws RuntimeException
	 */
	void poolableInit(Object params) throws RuntimeException;

	/**
	 * Recycle method, called when the object is reused, just before it is checked out of the pool. For example,
	 * when an object is first checked out, then checked in and out twice, the sequence of calls to the initialise,
	 * recycle and done methods is:<br/><p/>
	 * &nbsp;{@link #poolableInit(Object) poolableInit}<br/>
	 * &nbsp;{@link #poolableDone() poolableDone}<br/>
	 * &nbsp;{@link #poolableRecycle(Object) poolableRecycle}<br/>
	 * &nbsp;{@link #poolableDone() poolableDone}<br/>
	 * &nbsp;{@link #poolableRecycle(Object) poolableRecycle}<br/>
	 * &nbsp;{@link #poolableDone() poolableDone}<br/>
	 * @param params Arbitrary object which may be used to pass parameters to the recycler - may be null.
	 * @throws RuntimeException
	 */
	void poolableRecycle(Object params) throws RuntimeException;

	/**
	 * Cleanup method, called when the object is returned to the pool.
	 */
	void poolableDone();
}
