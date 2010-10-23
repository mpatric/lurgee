/*
 * @(#)ScrollbarWidget.java		2006/03/04
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Simple vertical scrollbar widget.
 * @author mpatric
 */
public class ScrollbarWidget extends Widget {

	private static final long serialVersionUID = 5549896329481685239L;
	private static final int MIN_FREE_SCROLL_PIXELS = 6;

	protected final Color hiColour;
	protected final Color hiBackColour;
	
	private ScrollerThumbWidget scrollerThumbWidget;
	private float scale;
	protected int min = 0;
	protected int max = 0;
	protected int value = 0;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param min Minimum value represented by the scrollbar.
	 * @param max Maximum value represented by the scrollbar.
	 * @param foreColour Foreground colour.
	 * @param backColour Background colour.
	 * @param hiColour Highlighted foreground colour.
	 * @param hiBackColour Highlighted background colour.
	 */
	public ScrollbarWidget(Widget parentWidget, int x, int y, int width, int height, int min, int max, Color foreColour, Color backColour, Color hiColour, Color hiBackColour) {
		super(parentWidget, x, y, width, height);
		this.min = min;
		this.max = max;
		this.value = min;
		this.hiColour = hiColour;
		this.hiBackColour = hiBackColour;
		setForeground(foreColour);
		setBackground(backColour);
		addWidgets();
	}

	private void addWidgets() {
		ArrowButtonWidget upArrowButtonWidget = new ArrowButtonWidget(this, 0, 0, getWidth(), getWidth(), ArrowButtonWidget.ARROW_UP);
		add(upArrowButtonWidget);
		ArrowButtonWidget downArrowButtonWidget = new ArrowButtonWidget(this, 0, getHeight() - getWidth(), getWidth(), getWidth(), ArrowButtonWidget.ARROW_DOWN);
		add(downArrowButtonWidget);
		// set dummy values for the bounds of the scroller
		scrollerThumbWidget = new ScrollerThumbWidget(this, 0, 0, getWidth(), getHeight());
		add(scrollerThumbWidget);
		adjustScrollerSize();
	}

	protected void adjustScrollerSize() {
		if (min >= max) {
			if (isVisible()) {
				setVisible(false);
				setEnabled(false);
			}
		} else {
			int oldSize = scrollerThumbWidget.getHeight();
			int newSize = getHeight() - (2 * (getWidth() - 1)) - (MIN_FREE_SCROLL_PIXELS * (max - min + 1));
			if (newSize < getWidth()) newSize = getWidth();
			if (newSize != oldSize) {
				scrollerThumbWidget.setBounds(scrollerThumbWidget.getX(), scrollerThumbWidget.getY(), scrollerThumbWidget.getWidth(), newSize);
				adjustScrollerPosition();
				scrollerThumbWidget.repaint();
			}
			if (!isVisible()) {
				setVisible(true);
				setEnabled(true);
			}
		}
	}

	protected void adjustScrollerPosition() {
		int newPos;
		if (min == max) {
			newPos = 0;
		} else {
			scale = getHeight() - (2 * (getWidth() - 1)) - scrollerThumbWidget.getHeight();
			scale /= max - min;
			newPos = (int)(getWidth() - 1 + ((value - min) * scale));
		}
		if (newPos != scrollerThumbWidget.getY()) {
			scrollerThumbWidget.setBounds(scrollerThumbWidget.getX(), newPos, scrollerThumbWidget.getWidth(), scrollerThumbWidget.getHeight());
			parentWidget.repaint();
		}
	}

	protected void moveScroller(int delta) {
		int newY = scrollerThumbWidget.getY() + delta;
		if (newY < (getWidth() - 1)) {
			newY = getWidth() - 1;
		} else if (newY > getHeight() - scrollerThumbWidget.getHeight() - getWidth() + 1) {
			newY = getHeight() - scrollerThumbWidget.getHeight() - getWidth() + 1;
		}
		int newValue = (int)(min + ((newY - scrollerThumbWidget.getWidth() + 1) / scale) + 0.5);
		if (value < min) {
			value = min;
		} else if (value > max) {
			value = max;
		}
		value = newValue;
		scrollerThumbWidget.setBounds(scrollerThumbWidget.getX(), newY, scrollerThumbWidget.getWidth(), scrollerThumbWidget.getHeight());
		parentWidget.repaint();
	}

	public int getValue() {
		return value;
	}

	public void setRange(int min, int max) {
		this.min = min;
		this.max = max;
		adjustScrollerSize();
	}

	@Override
	public void paint(Graphics g) {
		// background
		g.setColor(getBackground());
		g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
		// border
		g.setColor(getForeground());
		g.fillRect(0, 0, 1, getHeight());
		g.fillRect(0, 0, getWidth(), 1);
		g.fillRect(getWidth() - 1, 0, 1, getHeight());
		g.fillRect(0, getHeight() - 1, getWidth(), 1);
		// child components
		super.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			if (e.getY() < scrollerThumbWidget.getY()) {		
				moveScroller(e.getY() - scrollerThumbWidget.getY() - (MIN_FREE_SCROLL_PIXELS / 2));
			} else {
				moveScroller(e.getY() - scrollerThumbWidget.getY() - scrollerThumbWidget.getHeight() + (MIN_FREE_SCROLL_PIXELS / 2));
			}
			adjustScrollerPosition();
		} else {
			super.mouseClicked(e);
		}
	}
	
	/**
	 * Inner class for representing the arrow buttons on the scrollbar.
	 */
	class ArrowButtonWidget extends ButtonWidget {
	
		private static final long serialVersionUID = 1591060607823802253L;
		private static final int ARROW_UP = 1;
		private static final int ARROW_DOWN = 2;
		private final int direction;
	
		/**
		 * Constructor.
		 * @param parentWidget The parent widget which contains this widget if there is one.
		 * @param x The x-position of the left side of this button relative to the parent if there is one or the applet if there isn't.
		 * @param y The y-position of the top of this button relative to the parent if there is one or the applet if there isn't.
		 * @param width The width in pixels of this button.
		 * @param height The height in pixels of this button.
		 * @param direction Direction of the arrow.
		 */
		public ArrowButtonWidget(Widget parentWidget, int x, int y, int width, int height, int direction) {
			super(parentWidget, x, y, width, height);
			this.direction = direction;
		}
	
		public void paint(Graphics g) {
			// background
			g.setColor(hiBackColour);
			g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
			// border
			g.setColor(getForeground());
			g.fillRect(0, 0, 1, getHeight());
			g.fillRect(0, 0, getWidth(), 1);
			g.fillRect(getWidth() - 1, 0, 1, getHeight());
			g.fillRect(0, getHeight() - 1, getWidth(), 1);
			// arrow
			if (isDepressed()) {
				g.setColor(hiColour);
			} else {
				g.setColor(getForeground());
			}
			if (direction == ARROW_UP) {
				int i = getWidth() - 4;
				for (int j = 0; j <= getWidth() - 3; j++) {
					g.fillRect((getWidth() - i) / 2, getHeight() - j - 3, i, 1);
					if (j % 2 != 0) {
						i -= 2;
					}
				}
			} else if (direction == ARROW_DOWN) {
				int i = getWidth() - 4;
				for (int j = 0; j <= getWidth() - 3; j++) {
					g.fillRect((getWidth() - i) / 2, j + 2, i, 1);
					if (j % 2 != 0) {
						i -= 2;
					}
				}
			}
		}
		
		protected void selectAction(MouseEvent e) {
			switch (direction) {
				case ARROW_UP:
					if (value > min) {
						value--;
						adjustScrollerPosition();
					}
					break;
				case ARROW_DOWN:
					if (value < max) {
						value++;
						adjustScrollerPosition();
					}
					break;
			}
		}
	}

	/**
	 * Inner class for representing the scroller on the scrollbar.
	 */
	class ScrollerThumbWidget extends ButtonWidget {
	
		private static final long serialVersionUID = 2229596003570410574L;

		private int clickX = -1;
		private int clickY = -1;

		/**
		 * Constructor.
		 * @param parentWidget The parent widget which contains this widget if there is one.
		 * @param x The x-position of the left side of this button relative to the parent if there is one or the applet if there isn't.
		 * @param y The y-position of the top of this button relative to the parent if there is one or the applet if there isn't.
		 * @param width The width in pixels of this button.
		 * @param height The height in pixels of this button.
		 */
		public ScrollerThumbWidget(Widget parentWidget, int x, int y, int width, int height) {
			super(parentWidget, x, y, width, height);
		}
	
		public void paint(Graphics g) {
			// background
			g.setColor(hiBackColour);
			g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
			// border
			g.setColor(getForeground());
			g.fillRect(0, 0, 1, getHeight());
			g.fillRect(0, 0, getWidth(), 1);
			g.fillRect(getWidth() - 1, 0, 1, getHeight());
			g.fillRect(0, getHeight() - 1, getWidth(), 1);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {	
				if (isMouseOver()) {
					clickX = e.getX();
					clickY = e.getY();
				}
			}
			else {
				super.mousePressed(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {	
				if (clickX >= 0) {
					clickX = -1;
					adjustScrollerPosition();
				}
			}
			else {
				super.mousePressed(e);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {	
				moveScroller(e.getY() - clickY);
			}
			else {
				super.mouseClicked(e);
			}
		}
	
		protected void selectAction(MouseEvent e) {
		}
	}
}
