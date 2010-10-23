/*
 * @(#)ReversiStatusWidget.java		2006/02/05
 *
 * Part of the reversi applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.applet;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import net.lurgee.common.applet.Event;
import net.lurgee.common.awt.Widget;
import net.lurgee.common.awt.WidgetEvent;
import net.lurgee.common.awt.WidgetEventListener;
import net.lurgee.reversi.Colour;
import net.lurgee.reversi.ReversiBoard;
import net.lurgee.reversi.ReversiPlayer;

/**
 * Widget for status display. Shows the score, an indication of which player it is to play, and a status message.
 * @author mpatric
 */
public class ReversiStatusWidget extends Widget implements WidgetEventListener {

	private static final long serialVersionUID = -8979056257932544745L;
	private static final Integer BLACK_IMAGE = new Integer(1);
	private static final Integer WHITE_IMAGE = new Integer(2);
	private static final Integer SELECTOR_IMAGE = new Integer(3);
	
	private final ReversiGame reversiGame;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param reversiGame A game container.
	 * @throws IOException 
	 */
	public ReversiStatusWidget(Widget parentWidget, int x, int y, int width, int height, ReversiGame reversiGame) throws IOException {
		super(parentWidget, x, y, width, height);
		this.reversiGame = reversiGame;
		addWidgetEventListener(this);
		loadImages();
	}

	private void loadImages() throws IOException {
		loadImage(BLACK_IMAGE, AppletConsts.PIECE_BLACK_IMAGE_FILENAME);
		loadImage(WHITE_IMAGE, AppletConsts.PIECE_WHITE_IMAGE_FILENAME);
		loadImage(SELECTOR_IMAGE, AppletConsts.SELECTOR_IMAGE_FILENAME);
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
		// pieces
		Image blackImage = getImage(BLACK_IMAGE);
		Image whiteImage = getImage(WHITE_IMAGE);
		if (g.hitClip(0, 0, blackImage.getWidth(this), blackImage.getHeight(this))) {
			g.drawImage(blackImage, 0, 0, this);
		}
		if (g.hitClip(199, 0, whiteImage.getWidth(this), whiteImage.getHeight(this))) {
			g.drawImage(whiteImage, 199, 0, this);
		}
		FontMetrics fontMetrics = g.getFontMetrics();
		// selector
		if (!reversiGame.getBoard().isGameOver() && !reversiGame.isBusy()) {
			Image selectorImage = getImage(SELECTOR_IMAGE);
			ReversiPlayer currentPlayer;
			if (reversiGame.isBusy()) {
				currentPlayer = ((ReversiPlayer) reversiGame.getPreviousBoard().getCurrentPlayer());
			} else {
				currentPlayer = ((ReversiPlayer) reversiGame.getBoard().getCurrentPlayer());
			}
			if (currentPlayer.getColour() == Colour.BLACK) {
				if (g.hitClip(10, 10, selectorImage.getWidth(this), selectorImage.getHeight(this)))
					g.drawImage(selectorImage, 10, 10, this);
			} else if (currentPlayer.getColour() == Colour.WHITE) {
				if (g.hitClip(208, 10, selectorImage.getWidth(this), selectorImage.getHeight(this)))
					g.drawImage(selectorImage, 208, 10, this);
			}
		}
		// status message
		g.setFont(AppletConsts.DEFAULT_FONT);
		String statusMessage = reversiGame.getStatusMessage();
		if (statusMessage != null) {
			g.setColor(AppletConsts.COLOUR_STATUS_TEXT);
			g.drawString(statusMessage, (getWidth() - fontMetrics.stringWidth(statusMessage)) / 2, getHeight() - 3 - (AppletConsts.DEFAULT_FONT.getSize() / 2));
		}
		// scores
		String blackScore;
		String whiteScore;
		if (reversiGame.isBusy()) {
			blackScore = Integer.toString(((ReversiBoard) reversiGame.getPreviousBoard()).getCount(Colour.BLACK));
			whiteScore = Integer.toString(((ReversiBoard) reversiGame.getPreviousBoard()).getCount(Colour.WHITE));
		} else {
			blackScore = Integer.toString(((ReversiBoard) reversiGame.getBoard()).getCount(Colour.BLACK));
			whiteScore = Integer.toString(((ReversiBoard) reversiGame.getBoard()).getCount(Colour.WHITE));
		}
		
		g.setColor(Color.BLACK);
		g.drawString(blackScore, 32, getHeight() - 3 - (AppletConsts.DEFAULT_FONT.getSize() / 2));
		g.setColor(Color.WHITE);
		g.drawString(whiteScore, getWidth() - 32 - fontMetrics.stringWidth(whiteScore) + 1, getHeight() - 3 - (AppletConsts.DEFAULT_FONT.getSize() / 2));
	}
	
	@Override
	public String toString() {
		return "ReversiStatusWidget:" + super.toString();
	}
}
