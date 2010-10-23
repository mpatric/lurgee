/*
 * @(#)Animatable.java		2007/06/10
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

/**
 * Interface animatable objects should implement.
 * @author mpatric
 */
public interface Animatable {

	/** Start animation, setup whatever is needed. */
	void startAnimating();
	
	/** End animation. */
	void endAnimating();
	
	/** Abort animation. */
	void abortAnimating();
	
	/**
	 * Run one cycle of animation.
	 * @return
	 * 	<ul>
	 * 		<li>true if there are still more animation cycles pending;</li>
	 * 		<li>false otherwise.</li>
	 * 	</ul>
	 */
	boolean animate();
}
