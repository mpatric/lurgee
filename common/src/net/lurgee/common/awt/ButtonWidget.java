/*
 * @(#)ButtonWidget.java		2006/03/05
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Abstract class for generic button widgets.
 * @author mpatric
 */
public abstract class ButtonWidget extends HoverOverWidget {

	private static final long serialVersionUID = -5687600223823930169L;
	private boolean depressed = false;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this button if there is one.
	 * @param x The x-position of the left side of this button relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this button relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this button.
	 * @param height The height in pixels of this button.
	 */
	public ButtonWidget(Widget parentWidget, int x, int y, int width, int height) {
		super(parentWidget, x, y, width, height);
	}
	
	public boolean isDepressed() {
		return depressed;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable() && !depressed) {
			depressed = true;
			repaint();
		} else {
			super.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (depressed) {
			depressed = false;
			repaint();
		} else {
			super.mouseReleased(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable() && isMouseOver()) {	
			selectAction(e);
		} else {
			super.mouseClicked(e);
		}
	}

	/** Abstract method to paint the button. Concrete subclasses must define this method. */
	@Override
	public abstract void paint(Graphics g);
	
	/** Abstract method called when icon is clicked. Concrete subclasses must define this method. */
	protected abstract void selectAction(MouseEvent e);
}
