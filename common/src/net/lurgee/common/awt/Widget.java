/*
 * @(#)Widget.java		2006/02/05
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for general-purpose GUI component for use in applets. Extended from the awt {@link Container} class
 * and includes mouse event support and is a listener for custom events.
 * @author mpatric
 */
public abstract class Widget extends Container implements MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = -4704219817313528948L;
	protected final Widget parentWidget;
	protected final Map<Integer, Image> images = new HashMap<Integer, Image>();

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 */
	public Widget(Widget parentWidget, int x, int y, int width, int height) {
		super();
		this.parentWidget = parentWidget;
		setBounds(x, y, width, height);
		setLayout(null);
		setEnabled(true);
		setFocusable(true);
		setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public Widget getParentWidget() {
		return parentWidget;
	}

	public void mousePressed(MouseEvent e) {
		if (parentWidget != null) {
			parentWidget.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (parentWidget != null) {
			parentWidget.mouseReleased(e);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (parentWidget != null) {
			parentWidget.mouseClicked(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		if (parentWidget != null) {
			parentWidget.mouseMoved(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (parentWidget != null) {
			parentWidget.mouseDragged(e);
		}
	}
	
	private MainWindow findMainWindow() {
		Widget widget = this;
		while (widget != null) {
			if (widget instanceof MainWindow) {
				return (MainWindow) widget;
			}
			widget = widget.getParentWidget();
		}
		throw new NoMainWindowException("A listener can only be added to a widget contained in a MainWindow");
	}
	
	public void addWidgetEventListener(WidgetEventListener widgetEventListener) {
		findMainWindow().addListener(widgetEventListener);
	}
	
	public void removeWidgetEventListener(WidgetEventListener widgetEventListener) {
		findMainWindow().removeListener(widgetEventListener);
	}
	
	public void postWidgetEvent(int id) {
		postWidgetEvent(id, null);
	}
	
	public void postWidgetEvent(int id, Object data) {
		postWidgetEvent(findMainWindow(), id, data);
	}
	
	public void postWidgetEvent(Widget source, int id) {
		postWidgetEvent(source, id, null);
	}
	
	public void postWidgetEvent(Widget source, int id, Object data) {
		findMainWindow().postWidgetEventToSystemQueue(new WidgetEvent(source, id, data));
	}
	
	public boolean loadImageIfFound(Integer key, String filename) {
		if (filename != null) {
			try {
				loadImage(key, filename);
				return true;
			} catch (IOException e) {
				// do nothing
			}
		}
		return false;
	}

	public void loadImage(Integer key, String filename) throws IOException {
		if (images.containsKey(key)) {
			throw new IllegalArgumentException("Image already registered with that key");
		}
		InputStream is = getImageAsInputStream(filename);
		if (is == null) {
			throw new IllegalArgumentException("Image not found " + filename);
		}
		BufferedInputStream bis = new BufferedInputStream(is);
		byte[] buffer = null;
		int bufferSize = is.available();
		buffer = new byte[bufferSize];
		bis.read(buffer, 0, bufferSize);
		Image image = Toolkit.getDefaultToolkit().createImage(buffer);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(image, 0);
		try {
			mediaTracker.waitForAll();
		} catch (InterruptedException ie) {
			// do nothing
		}
		images.put(key, image);
	}
	
	public void unloadImage(Integer key) {
		images.remove(key);
	}
	
	public Image getImage(Integer key) {
		return images.get(key);
	}
	
	protected InputStream getImageAsInputStream(String filename) {
		return getClass().getResourceAsStream("/" + filename);
	}
}
