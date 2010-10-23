/*
 * @(#)Connect4Applet.java		2007/06/11
 *
 * Part of the connect4 applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4.applet;

import java.io.IOException;

import net.lurgee.common.applet.AbstractGameApplet;

/**
 * Connect-four applet.
 * @author mpatric
 */
public class Connect4Applet extends AbstractGameApplet {

	private static final long serialVersionUID = -2045566903592756353L;

	@Override
	public void init() {
		super.init();
		setBackground(AppletConsts.COLOUR_APPLET_BACKGROUND);
	}

	protected void createMainWindow() throws IOException {
		mainWindow = new Connect4MainWindow(this, 0, 0, AppletConsts.APPLET_WIDTH, AppletConsts.APPLET_HEIGHT);
	}
}
