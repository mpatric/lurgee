/*
 * @(#)TextWindow.java		2006/02/20
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Window to display text, with a vertical scrollbar if needed. The window is always automatically centred within its
 * parent widget.
 * @author mpatric
 */
public class TextWindow extends Window implements WidgetEventListener {

	private static final long serialVersionUID = 7401082263253894280L;
	private static final int SCROLLBAR_WIDTH = 10;
	private static final double LINE_SPACING_FACTOR = 1.2;

	private final String text;
	private final Color hiColour;
	private final Color hiBackColour;
	private final int spacing;

	private ScrollbarWidget scrollbarWidget = null;
	private ArrayList<String> lines = null;
	private boolean firstPaint = true;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this window if there is one.
	 * @param width The width in pixels of this window.
	 * @param height The height in pixels of this window.
	 * @param text The text to display.
	 * @param foreColour Foreground colour, for text and the window border.
	 * @param backColour Background colour for the window.
	 * @param hiColour Highlighted colour for the scrollbar.
	 * @param font Font to use when displaying the text in the window.
	 * @param spacing Spacing (padding) between text and edges of the window.
	 */
	public TextWindow(Widget parentWidget, int width, int height, String text, Color foreColour, Color backColour, Color hiColour, Color hiBackColour, Font font, int spacing) {
		super(parentWidget, (parentWidget.getWidth() - width) / 2, (parentWidget.getHeight() - height) / 2, width, height);
		this.text = text;
		this.hiColour = hiColour;
		this.hiBackColour = hiBackColour;
		this.spacing = spacing;
		setForeground(foreColour);
		setBackground(backColour);
		setFont(font);
		addWidgets();
		addWidgetEventListener(this);
	}

	private void addWidgets() {
		scrollbarWidget = new ScrollbarWidget(this, getWidth() - SCROLLBAR_WIDTH, 0, SCROLLBAR_WIDTH, getHeight(), 1, 1, getForeground(), hiBackColour, hiColour, getBackground());
		add(scrollbarWidget);
	}

	private void splitText(FontMetrics fontMetrics) {
		int targetWidth = getWidth() - (2 * spacing) - SCROLLBAR_WIDTH;
		lines = new ArrayList<String>();	
		String splitByCRs[] = text.split("\n");
		for (int i = 0; i < splitByCRs.length; i++) {
			String splitBySpaces[] = splitByCRs[i].split(" ");
			if (splitBySpaces.length == 0) {
				lines.add("");
			} else {		
				StringBuffer segment = new StringBuffer();
				for (int j = 0; j < splitBySpaces.length; j++) {
					if (fontMetrics.stringWidth(segment.toString() + splitBySpaces[j]) < targetWidth) {				
						segment.append(splitBySpaces[j]);
						segment.append(' ');
					} else if (segment.length() == 0) {
						lines.add(splitBySpaces[j]);
					} else {
						lines.add(segment.toString());
						segment = new StringBuffer(splitBySpaces[j] + " ");
					}
				}
				if (segment.length() > 0) {
					lines.add(segment.toString());
				}
			}
		}
		int linesPerPage = (int)((getHeight() - (2 * spacing)) / (LINE_SPACING_FACTOR * fontMetrics.getHeight()));
		scrollbarWidget.setRange(1, lines.size() - linesPerPage + 1);
	}

	@Override
	public void paint(Graphics g) {
		g.setFont(getFont());
		FontMetrics fontMetrics = g.getFontMetrics();
		if (firstPaint) {
			splitText(fontMetrics);
			firstPaint = false;
		}
		// background
		g.setColor(getBackground());
		g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
		// border
		g.setColor(getForeground());
		g.fillRect(0, 0, 1, getHeight());
		g.fillRect(0, 0, getWidth(), 1);
		g.fillRect(getWidth() - 1, 0, 1, getHeight());
		g.fillRect(0, getHeight() - 1, getWidth(), 1);
		// lines
		int line = scrollbarWidget.getValue();
		int y = spacing + getFont().getSize();
		while (y < getHeight() - spacing) {
			if (line > lines.size()) {
				break;
			}
			g.drawString(lines.get(line - 1), spacing, y);
			line++;
			y += LINE_SPACING_FACTOR * fontMetrics.getHeight();
		}
		super.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	protected void selectAction(MouseEvent e) {
	}

	public void processWidgetEvent(WidgetEvent event) {
	}
	
	@Override
	public String toString() {
		return "TextWindow:" + super.toString();
	}
}
