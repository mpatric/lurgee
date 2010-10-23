/*
 * @(#)NineMensMorrisStatusWidget.java		2008/03/08
 *
 * Part of the ninemensmorris applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris.applet;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.IOException;

import net.lurgee.common.applet.Event;
import net.lurgee.common.awt.Widget;
import net.lurgee.common.awt.WidgetEvent;
import net.lurgee.common.awt.WidgetEventListener;

/**
 * Widget for status display. Shows an indication of the last moved played and a status message.
 * @author mpatric
 */
public class NineMensMorrisStatusWidget extends Widget implements WidgetEventListener {

	private static final long serialVersionUID = -8979056257932544745L;
	
	private final NineMensMorrisGame ninemensmorrisGame;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param ninemensmorrisGame A game container.
	 * @throws IOException 
	 */
	public NineMensMorrisStatusWidget(Widget parentWidget, int x, int y, int width, int height, NineMensMorrisGame ninemensmorrisGame) throws IOException {
		super(parentWidget, x, y, width, height);
		this.ninemensmorrisGame = ninemensmorrisGame;
		addWidgetEventListener(this);
	}
	
	public void processWidgetEvent(WidgetEvent event) {
		switch (event.getID()) {
			case Event.REFRESH_STATUS:
				repaint();
				break;
		}
	}

	@Override
	public void paint(Graphics g) {
		// status message
		FontMetrics fontMetrics = g.getFontMetrics();
		g.setFont(AppletConsts.DEFAULT_FONT);
		String statusMessage = ninemensmorrisGame.getStatusMessage();
		if (statusMessage != null) {
			g.setColor(AppletConsts.COLOUR_STATUS_TEXT);
			g.drawString(statusMessage, (getWidth() - fontMetrics.stringWidth(statusMessage)) / 2, getHeight() - 3 - (AppletConsts.DEFAULT_FONT.getSize() / 2));
		}
	}
	
	@Override
	public String toString() {
		return "NineMensMorrisStatusWidget:" + super.toString();
	}
}
