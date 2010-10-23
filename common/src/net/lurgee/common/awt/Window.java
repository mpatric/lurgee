/*
 * @(#)Window.java		2006/02/01
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;


/**
 * Abstract class for a generic modal window for use in applets.
 * @author mpatric
 */
public abstract class Window extends Widget {

	private static final long serialVersionUID = 7268404590528521991L;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this window if there is one.
	 * @param x The x-position of the left side of this window relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this window relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this window.
	 * @param height The height in pixels of this window.
	 */
	public Window(Widget parentWidget, int x, int y, int width, int height) {
		super(parentWidget, x, y, width, height);
	}
	
	public void open() {
		if (parentWidget != null) {
			parentWidget.setEnabled(false);
			parentWidget.setFocusable(false);
		}
		if (!isVisible()) {
			setVisible(true);
		}
	}

	public void close() {
		if (parentWidget != null) {
			parentWidget.setEnabled(true);
			parentWidget.setFocusable(true);
		}
		if (isVisible()) {
			setVisible(false);
		}
	}
}
