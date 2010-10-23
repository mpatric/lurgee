/*
 * @(#)CustomEventListener.java		2007/06/09
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

/**
 * Interface for custom widget event listener.
 * @author mpatric
 */
public interface WidgetEventListener {

	/** Called when a widget event is received. */
	void processWidgetEvent(WidgetEvent event);
}
