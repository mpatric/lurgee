/*
 * @(#)HoverOverWidget.java		2007/06/09
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.event.MouseEvent;

/**
 * Abstract class representing a special type of widget that tracks when a mouse is over and repaints whenever a mouse enters or leaves it.
 * @author mpatric
 */
public abstract class HoverOverWidget extends Widget {

	private static final long serialVersionUID = 1590398905036089590L;
	private boolean mouseOver = false;
	
	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 */
	public HoverOverWidget(Widget parentWidget, int x, int y, int width, int height) {
		super(parentWidget, x, y, width, height);
	}
	
	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (!mouseOver) {
			mouseOver = true;
			repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (mouseOver) {
			mouseOver = false;
			repaint();
		}
	}
}
