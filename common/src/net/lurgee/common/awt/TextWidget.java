/*
 * @(#)TextWidget.java		2006/02/05
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Text widget, which supports highlighting when the mouse is over it.
 * @author mpatric
 */
public abstract class TextWidget extends HoverOverWidget {

	private static final long serialVersionUID = -6164244426503966449L;
	private final String label;
	private final Color hiColour;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param foreColour The colour to display the text in when the mouse is not over it.
	 * @param hiColour The colour to display the text in when the mouse is over it.
	 * @param font Font to use when displaying the text.
	 * @param label The text to display.
	 */
	public TextWidget(Widget parentWidget, int x, int y, int width, int height, Color foreColour, Color hiColour, Font font, String label) {
		super(parentWidget, x, y, width, height);
		this.label = label;
		this.hiColour = hiColour;
		setForeground(foreColour);
		setFont(font);
	}

	public String getLabel() {
		return label;
	}

	@Override
	public void paint(Graphics g) {
		g.setFont(getFont());
		if (isMouseOver() && isEnabled()) {
			g.setColor(hiColour);
			g.drawString(label, 0, getFont().getSize());
		} else {
			g.setColor(getForeground());
			g.drawString(label, 0, getFont().getSize());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (parentWidget.isEnabled() && isEnabled()) {
			if (isMouseOver()) {
				selectAction(e);
			}
		} else {
			super.mouseClicked(e);
		}
	}

	/** Abstract method called when button is clicked. Concrete subclasses must define this method. */
	protected abstract void selectAction(MouseEvent e);
}
