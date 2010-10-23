/*
 * @(#)OptionWindow.java		2006/02/20
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

/**
 * Window to present the user with a list of text options that they can select from. The window is always automatically
 * centered within its parent widget.
 * @author mpatric
 */
public abstract class OptionWindow extends Window implements WidgetEventListener {

	private static final long serialVersionUID = -1781243973154849569L;
	private final String label;
	private final Color hiColour;
	private final int spacing;
	
	private int labelX = 0;
	private int labelY = 0;
	protected int selection = 0;
	protected TextWidget textWidgets[] = null;
	private boolean firstPaint = true;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this window if there is one.
	 * @param label The text to display.
	 * @param options The options to present.
	 * @param foreColour Foreground colour, for text and the window border.
	 * @param backColour Background colour for the window.
	 * @param hiColour Highlighted text colour.
	 * @param font Font to use when displaying the label and for the options.
	 * @param spacing Spacing (padding) between text and edges of the window, between the text and the options, and
	 * 	between each of the options.
	 */
	public OptionWindow(Widget parentWidget, String label, String[] options, Color foreColour, Color backColour, Color hiColour, Font font, int spacing) {
		// Initially, set bounds to nothing, after the first paint this is adjusted to fit the text and to centre the window.
		super(parentWidget, 0, 0, 1, 1);
		this.label = label;
		this.hiColour = hiColour;
		this.spacing = spacing;
		addWidgets(options);
		setForeground(foreColour);
		setBackground(backColour);
		setFont(font);
		addWidgetEventListener(this);
	}

	private void addWidgets(String[] options) {
		/*
		 * The bounds of each text widget are not specified properly here as after the
		 * first paint of this component, they will be moved and sized appropriately.
		 */
		textWidgets = new TextWidget[options.length];
		for (int i = 0; i < options.length; i++) {
			textWidgets[i] = new TextWidget(this, 0, 0, 1, 1, getForeground(), hiColour, getFont(), options[i]) {
				private static final long serialVersionUID = -8022736721581070260L;
				protected void selectAction(MouseEvent e) {
					selection = -1;
					for (int j = 0; j < textWidgets.length; j++) {
						if (this == textWidgets[j]) selection = j;
					}
					close();
					((OptionWindow) parentWidget).selectAction(e);
				}
			};
			add(textWidgets[i]);
		}
	}

	private void adjustWindow(FontMetrics fontMetrics) {
		// work out widow width
		int optionsWidth = spacing;
		for (int i = 0; i < textWidgets.length; i++) {
			optionsWidth += fontMetrics.stringWidth(textWidgets[i].getLabel()) + spacing;
		}
		int labelWidth = fontMetrics.stringWidth(label) + (2 * spacing);
		int newWidth = optionsWidth;
		if (labelWidth > newWidth) {
			newWidth = labelWidth;
		}
		// work out window height
		int newHeight = (3 * spacing) + (2 * getFont().getSize());
		// work out x and y co-ordinates to center window
		int newX = (parentWidget.getWidth() - newWidth) / 2;
		int newY = (parentWidget.getHeight() - newHeight) / 2;
		setBounds(newX, newY, newWidth, newHeight);
		// set co-ordinates for text label
		labelX = ((newWidth - labelWidth) / 2) + spacing;
		labelY = spacing + getFont().getSize();
		// set bounds of text options
		newX = ((newWidth - optionsWidth) / 2) + spacing;
		newY = labelY + spacing;
		for (int i = 0; i < textWidgets.length; i++) {
			newWidth = fontMetrics.stringWidth(textWidgets[i].getLabel());
			textWidgets[i].setBounds(newX, newY, newWidth, getFont().getSize());
			newX += newWidth + spacing;
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setFont(getFont());
		if (firstPaint) {
			FontMetrics fontMetrics = g.getFontMetrics();
			adjustWindow(fontMetrics);
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
		// label
		g.drawString(label, labelX, labelY);
		super.paint(g);
	}
	
	@Override
	public String toString() {
		return "OptionWindow:" + super.toString();
	}

	/** Abstract method called when an option is selected. Concrete subclasses must define this method. */
	protected abstract void selectAction(MouseEvent e);
}
