/*
 * @(#)Widget.java		2005/10/31
 *
 * Part of the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.sgf;

/**
 * Poolable class used for unit tests.
 * @author mpatric
 */
public class TestPoolable implements Poolable {

	private int initCount = 0;
	private int recycleCount = 0;
	private int doneCount = 0;
	private Object lastParams = null;
	
	public int getInitCount() {
		return initCount;
	}

	public int getRecycleCount() {
		return recycleCount;
	}

	public int getDoneCount() {
		return doneCount;
	}
	
	public Object getLastParams() {
		return lastParams;
	}

	public void poolableInit(Object params) {
		initCount++;
		lastParams = params;
	}

	public void poolableRecycle(Object params) {
		recycleCount++;
		lastParams = params;
	}

	public void poolableDone() {
		doneCount++;
	}
}
