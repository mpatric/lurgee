/*
 * @(#)AbstractGameApplet.java		2007/05/20
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.lurgee.common.awt.MainWindow;

/**
 * Abstract applet class, which contains common functionality for all games.
 * @author mpatric
 */
public abstract class AbstractGameApplet extends Applet {

	private static final long serialVersionUID = 6597293771141647963L;
	private BufferedImage bufferedImage = null;
	private Graphics bufferedGraphics = null;
	protected MainWindow mainWindow = null;

	@Override
	public void init() {
		bufferedImage = (BufferedImage)createImage(getWidth(), getHeight());
		bufferedGraphics = bufferedImage.createGraphics();
		setLayout(null);
		try {
			createMainWindow();
		} catch (IOException e) {
			throw new RuntimeException("Error loading resources required by applet", e);
		}
		add(mainWindow);
	}

	/** Over-ridden to not clear the screen on repaints to support the double-buffering strategy. */
	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		Shape clip = g.getClip();
		bufferedGraphics.setClip(clip);
		super.paint(bufferedGraphics);
		g.drawImage(bufferedImage, 0, 0, this);
	}
	
	/** Setup the main window for the applet. Concrete subclasses must implement this method to create an appropriate main window for the game. */
	protected abstract void createMainWindow() throws IOException;
}
