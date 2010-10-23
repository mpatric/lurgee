/*
 * @(#)NineMensMorrisApplet.java		2008/03/08
 *
 * Part of the ninemensmorris applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris.applet;

import java.io.IOException;

import net.lurgee.common.applet.AbstractGameApplet;

/**
 * Nine Men's Morris applet.
 * @author mpatric
 */
public class NineMensMorrisApplet extends AbstractGameApplet {

	private static final long serialVersionUID = -6769030413841024854L;

	@Override
	public void init() {
		super.init();
		setBackground(AppletConsts.COLOUR_APPLET_BACKGROUND);
	}

	protected void createMainWindow() throws IOException {
		mainWindow = new NineMensMorrisMainWindow(this, 0, 0, AppletConsts.APPLET_WIDTH, AppletConsts.APPLET_HEIGHT);
	}
}
