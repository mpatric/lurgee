/*
 * @(#)CustomEvent.java		2007/06/09
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.AWTEvent;

/**
 * Class for custom events.
 * @author mpatric
 */
public class WidgetEvent extends AWTEvent {

	private static final long serialVersionUID = 8244056824654681976L;

	private final Object data;
	
	public WidgetEvent(Object source, int id, Object data) {
		super(source, id);
		this.data = data;
	}
	
	public WidgetEvent(Object source, int id) {
		this(source, id, null);
	}

	public Object getData() {
		return data;
	}
}
