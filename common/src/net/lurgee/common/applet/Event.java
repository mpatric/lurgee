/*
 * @(#)Event.java		2007/06/10
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import java.awt.AWTEvent;

/**
 * Event type constants.
 * @author mpatric
 */
public class Event {
	
	public static final String[] EVENT_DESCRIPTIONS = {"REFRESH_BOARD", "ENABLE_BOARD", "REFRESH_STATUS", "PLAY_MOVE", "DONE_PLAYING", "OPEN_HELP", "CLOSE_HELP", "OPEN_NEW_GAME_OPTION"};

	public static final int PLAY_MOVE = AWTEvent.RESERVED_ID_MAX + 1;
	public static final int DONE_PLAYING = AWTEvent.RESERVED_ID_MAX + 2;
	public static final int REFRESH_BOARD = AWTEvent.RESERVED_ID_MAX + 3;
	public static final int ENABLE_BOARD = AWTEvent.RESERVED_ID_MAX + 4;
	public static final int REFRESH_STATUS = AWTEvent.RESERVED_ID_MAX + 5;
	public static final int OPEN_HELP = AWTEvent.RESERVED_ID_MAX + 6;
	public static final int CLOSE_HELP = AWTEvent.RESERVED_ID_MAX + 7;
	public static final int NEW_GAME = AWTEvent.RESERVED_ID_MAX + 8;
}
