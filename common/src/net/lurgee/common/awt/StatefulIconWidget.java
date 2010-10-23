/*
 * @(#)StatefulIconWidget.java		2006/02/11
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Graphical stateful icon widget, which supports different images when enabled and disabled and different backgrounds when
 * the mouse is not over it and when it is. The state (and associated normal image) of the icon changes each time it is
 * clicked.
 * @author mpatric
 */
public abstract class StatefulIconWidget extends IconWidget {

	private static final long serialVersionUID = 1855001589542907872L;
	private static final int ICON_IMAGES_START = 10;

	private int state = -1;
	protected int maxState = 0;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this icon if there is one.
	 * @param x The x-position of the left side of this icon relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this icon relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this icon.
	 * @param height The height in pixels of this icon.
	 * @param initialState Index of the icon for the initial state.
	 * @param iconFilenames Filenames of icon images for each state.
	 * @param backFilename Filename of background image. May be null, then no background image is used.
	 * @param hiFilename Filename of highlighted background image. May be null, then the normal background image is used.
	 * @param disabledFilename Filename of disabled icon background image. May be null, then the normal icon image is used.
	 * @throws IOException If the first icon image cannot be loaded. 
	 */
	public StatefulIconWidget(Widget parentWidget, int x, int y, int width, int height, int initialState, String[] iconFilenames, String backFilename, String hiFilename, String disabledFilename) throws IOException {
		super(parentWidget, x, y, width, height, null, backFilename, hiFilename, disabledFilename);
		loadImages(iconFilenames);
		setState(initialState);
	}

	private void loadImages(String[] iconFilenames) throws IOException {
		loadImage(new Integer(ICON_IMAGES_START), iconFilenames[0]);
		for (int i = 1; i < iconFilenames.length; i++) {
			if (loadImageIfFound(new Integer(ICON_IMAGES_START + i), iconFilenames[i])) {
				maxState++;
			}
		}
	}

	protected void setState(int state) {
		if (this.state != state) {
			if (state <= maxState) {
				this.state = state;
				setCurrentIconIndex(new Integer(ICON_IMAGES_START + state));
			}
		}
	}
	
	protected int getState() {
		return state;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (parentWidget.isEnabled() && isEnabled()) {
			if (isMouseOver()) {
				int newState = state + 1;
				if (newState > maxState) {
					newState = 0;
				}
				setState(newState);
				repaint();
				selectAction(e);
			}
		} else {
			super.mouseClicked(e);
		}
	}
}
