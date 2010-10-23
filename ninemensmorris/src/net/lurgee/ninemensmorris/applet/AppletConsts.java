/*
 * @(#)AppletConsts.java		2008/03/08
 *
 * Part of the ninemensmorris applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris.applet;

import java.awt.Color;
import java.awt.Font;

import net.lurgee.ninemensmorris.Colour;
import net.lurgee.ninemensmorris.NineMensMorrisPlayer;
import net.lurgee.sgf.Player;

/**
 * Constants specific to the ninemensmorris applet.
 * @author mpatric
 */
public class AppletConsts {

	/* Board and piece images */
	public static final String RES_BOARD_PATH = "images/board/";
	public static final String BOARD_1H_IMAGE_FILENAME = RES_BOARD_PATH + "1h.png";
	public static final String BOARD_1V_IMAGE_FILENAME = RES_BOARD_PATH + "1v.png";
	public static final String BOARD_2TL_IMAGE_FILENAME = RES_BOARD_PATH + "2tl.png";
	public static final String BOARD_2TR_IMAGE_FILENAME = RES_BOARD_PATH + "2tr.png";
	public static final String BOARD_2BR_IMAGE_FILENAME = RES_BOARD_PATH + "2br.png";
	public static final String BOARD_2BL_IMAGE_FILENAME = RES_BOARD_PATH + "2bl.png";
	public static final String BOARD_3T_IMAGE_FILENAME = RES_BOARD_PATH + "3t.png";
	public static final String BOARD_3R_IMAGE_FILENAME = RES_BOARD_PATH + "3r.png";
	public static final String BOARD_3B_IMAGE_FILENAME = RES_BOARD_PATH + "3b.png";
	public static final String BOARD_3L_IMAGE_FILENAME = RES_BOARD_PATH + "3l.png";
	public static final String BOARD_4_IMAGE_FILENAME = RES_BOARD_PATH + "4.png";
	public static final String BOARD_SURFACE_IMAGE = RES_BOARD_PATH + "surface.png";
		
	/* Piece images */
	public static final String RES_PIECES_PATH = "images/pieces/";
	public static final String PIECE_WHITE_IMAGE_FILENAME = RES_PIECES_PATH + "white.png";
	public static final String PIECE_BLACK_IMAGE_FILENAME = RES_PIECES_PATH + "black.png";
	public static final String PIECE_SHADOW_IMAGE = RES_PIECES_PATH + "shadow.png";
	public static final String PIECE_SELECT_SPOT_IMAGE = RES_PIECES_PATH + "selectspot.png";

	/* Icon images */
	public static final String RES_ICONS_PATH = "images/icons/";
	public static final String ICON_SELECT = RES_ICONS_PATH + "select.png";
	public static final String ICON_COLOUR_WHITE_FILENAME = RES_ICONS_PATH + "colourw.png";
	public static final String ICON_COLOUR_BLACK_FILENAME = RES_ICONS_PATH + "colourb.png";
	public static final String ICON_LEVEL_PREFIX = RES_ICONS_PATH + "level";
	public static final String ICON_LEVEL_SUFFIX = ".png";
	public static final String ICON_UNDO_FILENAME = RES_ICONS_PATH + "undo.png";
	public static final String ICON_UNDO_DISABLED_FILENAME = RES_ICONS_PATH + "undod.png";
	public static final String ICON_SOUND_ON_FILENAME = RES_ICONS_PATH + "soundon.png";
	public static final String ICON_SOUND_OFF_FILENAME = RES_ICONS_PATH + "soundoff.png";
	public static final String ICON_HELP_FILENAME = RES_ICONS_PATH + "help.png";
	public static final String ICON_NEW_FILENAME = RES_ICONS_PATH + "new.png";
	public static final String ICON_NEW_DISABLED_FILENAME = RES_ICONS_PATH + "newd.png";

	/* Sounds */
	public static final String RES_SOUND_PATH = "sounds/";
	public static final String SOUND_ALERT = RES_SOUND_PATH + "alert.au";
	public static final String SOUND_ACTION = RES_SOUND_PATH + "action.au";
	public static final String SOUND_ENDGAME = RES_SOUND_PATH + "endgame.au";

	/* Colours */
	public static final Color COLOUR_APPLET_BACKGROUND = new Color(152, 152, 152);
	public static final Color COLOUR_STATUS_TEXT = Color.BLACK;
	public static final Color COLOUR_WINDOW_BACKGROUND = new Color(216, 216, 216);
	public static final Color COLOUR_WINDOW_FOREGROUND = Color.BLACK;
	public static final Color COLOUR_WINDOW_HI_FOREGROUND = new Color(166, 86, 36);
	public static final Color COLOUR_SCROLLBAR_HI = new Color(162, 153, 144);
	
	/* Levels */
	public static final int[] SEARCH_LEVELS = {3, 4, 6, 8, 10};
	public static final int[] SEARCH_THRESHOLDS = {250, 1000, 5000, 50000, 100000};
	
	/* Search stuff */
	public static final int KILLER_MOVES_PER_LEVEL = 5;
	
	/* Players */
	public static final Player[] PLAYERS = {NineMensMorrisPlayer.getInstance(Colour.WHITE), NineMensMorrisPlayer.getInstance(Colour.BLACK)};

	/* Fonts */
	public static final Font DEFAULT_FONT = new Font("SansSerif", Font.BOLD, 12);
	public static final Font TEXT_WINDOW_FONT = new Font("SansSerif", Font.PLAIN, 12);
	public static final Font SCORED_MOVE_FONT = new Font("SansSerif", Font.PLAIN, 10);

	/* Metrics */
	public static int APPLET_WIDTH = 315;
	public static int APPLET_HEIGHT = 405;
	public static final int ICON_SPACING = 0;
	public static final int ICON_WIDTH = 45;
	public static final int ICON_HEIGHT = 45;
	public static final int SELECT_ITEM_SPACING = 8;
	public static final int TEXT_WINDOW_SPACING = 8;
	public static final int TEXT_WINDOW_INSET = 16;

	/* Strings */
	public static final String THINKING_MESSAGE = "Thinking";
	public static final String[] PROGRESS_STRINGS = {"  ", ". ", "..", ". "};
	public static final String LABEL_CONFIRM_NEW = "Start a new game?";
	public static final String[] LABEL_OPTIONS_CONFIRM_NEW = {"Yes", "No"};
	public static final String HELP_TEXT =
		"Lurgee Nine Men's Morris " + net.lurgee.sgf.Version.VERSION + "\n" +
		"Michael Patricios 2008\n" +
		"\n" +
		"Part of the Strategy Game Framework project at lurgee.net.\n" +
		"\n" +
		"LICENSE\n" +
		"This is free software released under a MIT License - see lurgee.net for more details.\n" +
		"\n" +
		"Click in the applet, outside of this window, to return to the game!";
}
