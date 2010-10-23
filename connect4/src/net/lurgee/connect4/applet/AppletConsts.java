/*
 * @(#)AppletConsts.java		2007/06/11
 *
 * Part of the connect4 applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4.applet;

import java.awt.Color;
import java.awt.Font;

import net.lurgee.connect4.Colour;
import net.lurgee.connect4.Connect4Player;
import net.lurgee.sgf.Player;

/**
 * Constants specific to the connect4 applet.
 * @author mpatric
 */
public class AppletConsts {

	/* Board and piece images */
	public static final String RES_BOARD_PATH = "images/board/";
	public static final String BACKPLANE_IMAGE_FILENAME = RES_BOARD_PATH + "backplane.png";
	public static final String FRONTPLANE_IMAGE_FILENAME = RES_BOARD_PATH + "frontplane.png";
	public static final String PIECE_RED_IMAGE_FILENAME = RES_BOARD_PATH + "red.png";
	public static final String PIECE_YELLOW_IMAGE_FILENAME = RES_BOARD_PATH + "yellow.png";
	public static final String PIECE_RED_HI_IMAGE_FILENAME = RES_BOARD_PATH + "redhi.png";
	public static final String PIECE_YELLOW_HI_IMAGE_FILENAME = RES_BOARD_PATH + "yellowhi.png";
	public static final String HILIGHT_IMAGE_FILENAME = RES_BOARD_PATH + "hilight.png";
	
	/* Decoration images */
	public static final String RES_DECO_PATH = "images/deco/";
	public static final String WOOD_IMAGE_FILENAME = RES_DECO_PATH + "wood.png";
	public static final String SKYTOP_IMAGE_FILENAME = RES_DECO_PATH + "skytop.png";
	public static final String SKYTILE_IMAGE_FILENAME = RES_DECO_PATH + "skytile.png";

	/* Icon images */
	public static final String RES_ICONS_PATH = "images/icons/";
	public static final String ICON_SELECT = RES_ICONS_PATH + "select.png";
	public static final String ICON_COLOUR_RED_FILENAME = RES_ICONS_PATH + "colourr.png";
	public static final String ICON_COLOUR_YELLOW_FILENAME = RES_ICONS_PATH + "coloury.png";
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
	public static final String SOUND_DROP = RES_SOUND_PATH + "drop.au";

	/* Colours */
	public static final Color COLOUR_APPLET_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color COLOUR_BOARD_BORDER = new Color(36, 55, 90);
	public static final Color COLOUR_STATUS_TEXT = Color.BLACK;
	public static final Color COLOUR_WINDOW_BACKGROUND = new Color(216, 216, 216);
	public static final Color COLOUR_WINDOW_FOREGROUND = Color.BLACK;
	public static final Color COLOUR_WINDOW_HI_FOREGROUND = new Color(166, 86, 36);
	public static final Color COLOUR_SCROLLBAR_HI = new Color(162, 153, 144);
	public static final Color COLOUR_SCORED_MOVE_TEXT = new Color(96, 96, 96);
	public static final Color COLOUR_BEST_SCORED_MOVE_TEXT = new Color(32, 32, 32);
	
	/* Levels */
	public static final int[] SEARCH_LEVELS = {4, 10, 12, 16};
	public static final int[] SEARCH_THRESHOLDS = {250, 5000, 50000, 100000};
	
	/* Search stuff */
	public static final int KILLER_MOVES_PER_LEVEL = 4;
	
	/* Players */
	public static final Player[] PLAYERS = {Connect4Player.getInstance(Colour.RED), Connect4Player.getInstance(Colour.YELLOW)};

	/* Fonts */
	public static final Font DEFAULT_FONT = new Font("SansSerif", Font.BOLD, 12);
	public static final Font TEXT_WINDOW_FONT = new Font("SansSerif", Font.PLAIN, 12);
	public static final Font SCORED_MOVE_FONT = new Font("SansSerif", Font.PLAIN, 10);

	/* Metrics */
	public static int APPLET_WIDTH = 240;
	public static int APPLET_HEIGHT = 320;
	public static final int ICON_SPACING = 4;
	public static final int ICON_WIDTH = 28;
	public static final int ICON_HEIGHT = 28;
	public static final int BOARD_EDGE_SIZE = 2;
	public static final int BOARD_STAND_SIZE = 4;
	public static final int BOARD_WIDTH = 238;
	public static final int BOARD_HEIGHT = 204;
	public static final int BLOCK_SIZE = 34;
	public static final int SELECT_ITEM_SPACING = 8;
	public static final int TEXT_WINDOW_SPACING = 8;
	public static final int TEXT_WINDOW_INSET = 16;
	public static final int LAST_MOVE_BLOCK_HEIGHT = 4;

	/* Strings */
	public static final String THINKING_MESSAGE = "Thinking";
	public static final String[] PROGRESS_STRINGS = {"  ", ". ", "..", ". "};
	public static final String LABEL_CONFIRM_NEW = "Start a new game?";
	public static final String[] LABEL_OPTIONS_CONFIRM_NEW = {"Yes", "No"};
	public static final String HELP_TEXT =
		"Lurgee Connect-Four " + net.lurgee.sgf.Version.VERSION + "\n" +
		"Michael Patricios 2007-2008\n" +
		"\n" +
		"Part of the Strategy Game Framework project at lurgee.net.\n" +
		"\n" +
		"HOW TO PLAY\n" +
		"Connect-Four (also known as Four-in-a-Row) is a board game where each player takes turns " +
		"to drop one of their discs into a vertical grid with the aim to achieve four discs in a row " +
		"horizontally, vertically or diagonally. The game is won by the first player to achieve four " +
		"in a row or is drawn if neither player manages this.\n" +
		"\n" +
		"Red always plays first. Select the colour you would like to play using the colour icon and " +
		"select the computer's skill level (smiley face being the easiest and surprised face the most " +
		"difficult) using the level icon.\n" +
		"\n" +
		"LICENSE\n" +
		"This is free software released under a MIT License - see lurgee.net for more details.\n" +
		"\n" +
		"Click in the applet, outside of this window, to return to the game!";
}
