/*
 * @(#)Connect4StatusWidget.java		2007/06/11
 *
 * Part of the connect4 applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4.applet;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.IOException;

import net.lurgee.common.applet.Event;
import net.lurgee.common.awt.Widget;
import net.lurgee.common.awt.WidgetEvent;
import net.lurgee.common.awt.WidgetEventListener;
import net.lurgee.connect4.Connect4Move;

/**
 * Widget for status display. Shows an indication of the last moved played and a status message.
 * @author mpatric
 */
public class Connect4StatusWidget extends Widget implements WidgetEventListener {

	private static final long serialVersionUID = -8979056257932544745L;
	
	private final Connect4Game connect4Game;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param connect4Game A game container.
	 * @throws IOException 
	 */
	public Connect4StatusWidget(Widget parentWidget, int x, int y, int width, int height, Connect4Game connect4Game) throws IOException {
		super(parentWidget, x, y, width, height);
		this.connect4Game = connect4Game;
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
		String statusMessage = connect4Game.getStatusMessage();
		if (statusMessage != null) {
			g.setColor(AppletConsts.COLOUR_STATUS_TEXT);
			g.drawString(statusMessage, (getWidth() - fontMetrics.stringWidth(statusMessage)) / 2, getHeight()  + 1 - ((3 * AppletConsts.DEFAULT_FONT.getSize()) / 2));
		}
		// last played piece
		Connect4Move lastMove = (Connect4Move) connect4Game.getBoard().getLastMovePlayed();
		if (lastMove != null) {
			int lx = (lastMove.getX() - 1) * AppletConsts.BLOCK_SIZE;
			if (g.hitClip(lx + 6, 0, AppletConsts.BLOCK_SIZE - 8, AppletConsts.LAST_MOVE_BLOCK_HEIGHT)) {
				g.setColor(AppletConsts.COLOUR_STATUS_TEXT);
				g.fillRect(lx + 6, 0, AppletConsts.BLOCK_SIZE - 8, AppletConsts.LAST_MOVE_BLOCK_HEIGHT);
			}
		}
	}
	
	@Override
	public String toString() {
		return "Connect4StatusWidget:" + super.toString();
	}
}
