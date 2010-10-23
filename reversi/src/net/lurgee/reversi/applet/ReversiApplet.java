/*
 * @(#)ReversiApplet.java		2005/11/28
 *
 * Part of the reversi applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.applet;

import java.io.IOException;

import net.lurgee.common.applet.AbstractGameApplet;

/**
 * Reversi applet.
 * @author mpatric
 */
public class ReversiApplet extends AbstractGameApplet {

	private static final long serialVersionUID = 4187900055356026668L;

	@Override
	public void init() {
		super.init();
		setBackground(AppletConsts.COLOUR_APPLET_BACKGROUND);
	}

	protected void createMainWindow() throws IOException {
		mainWindow = new ReversiMainWindow(this, 0, 0, AppletConsts.APPLET_WIDTH, AppletConsts.APPLET_HEIGHT);
	}
}
