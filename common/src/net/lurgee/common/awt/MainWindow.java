/*
 * @(#)MainWindow.java		2007/06/29
 *
 * Part of the common AWT classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for a main window for use in applets. An applet should generally have one main window at it's
 * root, with all controls and subwindows as child widgets on the main window.
 * @author mpatric
 */
public abstract class MainWindow extends Window {

	private static final long serialVersionUID = -2308296244990594976L;
	private final Applet applet;
	private final List<WidgetEventListener> listeners = new ArrayList<WidgetEventListener>();
	private final Map<Integer, AudioClip> audioClips = new HashMap<Integer, AudioClip>();

	/**
	 * Constructor.
	 * @param applet The applet.
	 * @param parentWidget The parent widget which contains this window if there is one.
	 * @param x The x-position of the left side of this window relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this window relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this window.
	 * @param height The height in pixels of this window.
	 */
	public MainWindow(Applet applet, Widget parentWidget, int x, int y, int width, int height) {
		super(parentWidget, x, y, width, height);
		this.applet = applet;
	}
	
	public Applet getApplet() {
		return applet;
	}
	
	protected void addListener(WidgetEventListener listener) {
		synchronized(this) {
			listeners.add(listener);
		}
	}
	
	protected void removeListener(WidgetEventListener listener) {
		synchronized(this) {
			listeners.remove(listener);
		}
	}
	
	protected void postWidgetEventToSystemQueue(WidgetEvent event) {
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(event);
	}
	
	private void dispatchWidgetEvent(WidgetEvent event) {
		Iterator<WidgetEventListener> iterator = listeners.iterator();
		while (iterator.hasNext()) {
			WidgetEventListener listener = iterator.next();
			listener.processWidgetEvent(event);
		}
	}
	
	@Override
	protected void processEvent(AWTEvent e) {
		if (e instanceof WidgetEvent) {
			dispatchWidgetEvent((WidgetEvent) e);
		} else {
			super.processEvent(e);
		}
	}

	protected void loadAudioClip(Integer key, String filename) {
		if (audioClips.containsKey(key)) {
			throw new IllegalArgumentException("Audioclip already registered with that key");
		}
		Applet applet = getApplet();
		AudioClip audioClip = applet.getAudioClip(applet.getCodeBase(), filename);
		audioClips.put(key, audioClip);
	}

	public void playAudioClip(Integer key) {
		AudioClip audioClip = audioClips.get(key);
		if (audioClip != null) {
			audioClip.play();
		}
	}
}
