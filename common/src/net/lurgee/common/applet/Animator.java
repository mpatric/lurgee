/*
 * @(#)Animator.java		2007/06/10
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

/**
 * Runnable class that animates an object that implements the {@link Animatable} interface in a thread.
 * @author mpatric
 */
public class Animator implements Runnable {

	private final Animatable animatable;
	private Thread thread = null;
	private final int millisPerTick;
	private boolean aborting;
	
	public Animator(Animatable animatable, int millisPerTick) {
		this.animatable = animatable;
		this.millisPerTick = millisPerTick;
	}
	
	public void start() {
		synchronized (this) {
			if (thread == null) {
				aborting = false;
				animatable.startAnimating();
				// create thread
				thread = new Thread(this);
				thread.start();
			}
		}
	}

	protected void end() {
		// wait for animator thread to end
		while (thread != null) {
			try {
				synchronized (this) {
					wait(millisPerTick);
				}
			} catch (InterruptedException e) {
				// do nothing
			}
		}
		animatable.endAnimating();
	}
	
	public void abort() {
		synchronized (this) {
			aborting = true;
			animatable.abortAnimating();
			end();
		}
	}
	
	public void run() {
		Thread currentThread = Thread.currentThread();
		try {
			while (currentThread == thread) {
				long startTime = System.currentTimeMillis();
				if (aborting || !animatable.animate()) {
					currentThread = null;
				}
				long timeTaken = System.currentTimeMillis() - startTime;
				if (timeTaken < millisPerTick) {
					synchronized (this) {
						wait(millisPerTick - timeTaken);
					}
				} else {
					Thread.yield();
				}
			}
		} catch (InterruptedException e) {
			// do nothing
		}
		thread = null;
		synchronized (this) {
			// only call end() if animation wasn't aborted (abort calls it otherwise)
			if (!aborting) {
				end();
			}
		}
	}

	public boolean isAnimating() {
		synchronized (this) {
			return thread != null;			
		}
	}
}
