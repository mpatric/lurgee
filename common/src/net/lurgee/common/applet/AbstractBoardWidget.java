/*
 * @(#)AbstractBoardWidget.java		2007/11/03
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import java.awt.AWTEvent;
import java.io.IOException;

import net.lurgee.common.awt.Widget;
import net.lurgee.common.awt.WidgetEvent;
import net.lurgee.common.awt.WidgetEventListener;

/**
 * Abstract class for a widget representing a game board.
 * @author mpatric
 */
public abstract class AbstractBoardWidget extends Widget implements WidgetEventListener, Animatable {
	
	private static final long serialVersionUID = -813016588537351236L;
	protected final AbstractGame game;
	private boolean abortedAnimation = false;
	
	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param game A game container.
	 * @throws IOException If any errors in loading resources (for example, images or sounds).
	 */
	public AbstractBoardWidget(Widget parentWidget, int x, int y, int width, int height, AbstractGame game) throws IOException {
		super(parentWidget, x, y, width, height);
		this.game = game;
		addWidgetEventListener(this);
	}
	
	@Override
	protected void processEvent(AWTEvent e) {
		super.processEvent(e);
	}
	
	public void processWidgetEvent(WidgetEvent event) {
		switch (event.getID()) {
			case Event.REFRESH_BOARD:
				repaint();
				break;
			case Event.ENABLE_BOARD:
				setEnabled(true);
				break;
		}
	}
	
	public void startAnimating() {
		game.setBusy(true);
		abortedAnimation = false;
	}
	
	public void endAnimating() {
		game.setBusy(false);
		repaint();
		if (abortedAnimation) {
			postWidgetEvent(Event.DONE_PLAYING, Boolean.TRUE);
		} else {
			postWidgetEvent(Event.DONE_PLAYING, Boolean.FALSE);
		}
	}
	
	public void abortAnimating() {
		abortedAnimation = true;
	}
}
