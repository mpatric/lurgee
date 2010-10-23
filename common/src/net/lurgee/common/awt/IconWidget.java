/*
 * @(#)IconWidget.java		2006/02/11
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

/**
 * Graphical icon widget, which supports different images when enabled and disabled and different backgrounds when the mouse
 * is not over it and when it is.
 * @author mpatric
 */
public abstract class IconWidget extends ButtonWidget {
	
	private static final long serialVersionUID = -8622826492116521911L;
	private static final Integer ICON_IMAGE = new Integer(1);
	private static final Integer BACK_IMAGE = new Integer(2);
	private static final Integer HI_IMAGE = new Integer(3);
	private static final Integer DISABLED_IMAGE = new Integer(4);
	
	private Integer currentIconIndex;
	private int id = 0;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this icon if there is one.
	 * @param x The x-position of the left side of this icon relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this icon relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this icon.
	 * @param height The height in pixels of this icon.
	 * @param iconFilename Filename of icon image.
	 * @param backFilename Filename of background image. May be null, then no background image is used.
	 * @param hiFilename Filename of highlighted background image. May be null, then the normal background image is used.
	 * @param disabledFilename Filename of disabled icon background image. May be null, then the normal icon image is used.
	 * @throws IOException If the main icon image cannot be loaded. 
	 */
	public IconWidget(Widget parentWidget, int x, int y, int width, int height, String iconFilename, String backFilename, String hiFilename, String disabledFilename) throws IOException {
		super(parentWidget, x, y, width, height);
		loadImages(backFilename, hiFilename, disabledFilename);
		if (iconFilename != null) {
			loadImage(ICON_IMAGE, iconFilename);
		}
		currentIconIndex = ICON_IMAGE;
	}
	
	private void loadImages(String backFilename, String hiFilename, String disabledFilename) {
		loadImageIfFound(BACK_IMAGE, backFilename);
		loadImageIfFound(HI_IMAGE, hiFilename);
		loadImageIfFound(DISABLED_IMAGE, disabledFilename);
	}
	
	protected void setCurrentIconIndex(Integer currentIconIndex) {
		this.currentIconIndex = currentIconIndex;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void paint(Graphics g) {
		int ix;
		int iy;
		if (isMouseOver() && isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			Image hiImage = getImage(HI_IMAGE);
			if (hiImage != null) {
				ix = (getWidth() - hiImage.getWidth(this) + 1) / 2;
				iy = (getWidth() - hiImage.getHeight(this) + 1) / 2;
				g.drawImage(hiImage, ix, iy, this);
			}
		} else {
			Image backImage = getImage(BACK_IMAGE);
			if (backImage != null) {
				ix = (getWidth() - backImage.getWidth(this) + 1) / 2;
				iy = (getWidth() - backImage.getHeight(this) + 1) / 2;
				g.drawImage(backImage, ix, iy, this);
			}
		}
		Image disabledImage = getImage(DISABLED_IMAGE);
		if (isEnabled() || disabledImage == null) {
			Image iconImage = getImage(currentIconIndex);			
			ix = (getWidth() - iconImage.getWidth(this) + 1) / 2;
			iy = (getWidth() - iconImage.getHeight(this) + 1) / 2;
			g.drawImage(iconImage, ix, iy, this);
		} else {
			ix = (getWidth() - disabledImage.getWidth(this) + 1) / 2;
			iy = (getWidth() - disabledImage.getHeight(this) + 1) / 2;
			g.drawImage(disabledImage, ix, iy, this);
		}
	}
}
