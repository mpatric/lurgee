/*
 * @(#)AppletConsts.java		2006/02/06
 *
 * Part of the reversi applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.applet;

import java.awt.Color;
import java.awt.Font;

import net.lurgee.reversi.Colour;
import net.lurgee.reversi.ReversiPlayer;
import net.lurgee.sgf.Player;
import net.lurgee.sgf.Version;

/**
 * Constants specific to the reversi applet.
 * @author mpatric
 */
public class AppletConsts {

	/* Board images */
	public static final String RES_BOARD_PATH = "images/board/";
	public static final String SQUARE_NORMAL_IMAGE_FILENAME = RES_BOARD_PATH + "square.png";
	public static final String SQUARE_HI_IMAGE_FILENAME = RES_BOARD_PATH + "squarehi.png";
	public static final String SELECTOR_IMAGE_FILENAME = RES_BOARD_PATH + "selector.png";
	public static final String WOOD_IMAGE_FILENAME = RES_BOARD_PATH + "wood.png";
	public static final String SPOT_IMAGE_FILENAME = RES_BOARD_PATH + "spot.png";
	public static final String LAST_PLAY_INDICATOR = RES_BOARD_PATH + "selector.png";

	/* Piece images */
	public static final String RES_PIECES_PATH = "images/pieces/";
	public static final String PIECE_BLACK_IMAGE_FILENAME = RES_PIECES_PATH + "pieceb.png";
	public static final String PIECE_WHITE_IMAGE_FILENAME = RES_PIECES_PATH + "piecew.png";
	public static final String PIECE_BLACK_HI_IMAGE_FILENAME = RES_PIECES_PATH + "piecebhi.png";
	public static final String PIECE_WHITE_HI_IMAGE_FILENAME = RES_PIECES_PATH + "piecewhi.png";
	public static final String PIECE_FLIP_IMAGE_PREFIX = RES_PIECES_PATH + "piece";
	public static final String PIECE_FLIP_IMAGE_SUFFIX = ".png";
	public static int NUM_ANIMATED_PIECES = 7;

	/* Icon images */
	public static final String RES_ICONS_PATH = "images/icons/";
	public static final String ICON_SELECT = RES_ICONS_PATH + "select.png";
	public static final String ICON_COLOUR_BLACK_FILENAME = RES_ICONS_PATH + "colourb.png";
	public static final String ICON_COLOUR_WHITE_FILENAME = RES_ICONS_PATH + "colourw.png";
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
	public static final String SOUND_START_PLAY = RES_SOUND_PATH + "play.au";
	public static final String SOUND_FLIP = RES_SOUND_PATH + "flip.au";
	public static final String SOUND_ALERT = RES_SOUND_PATH + "alert.au";
	public static final String SOUND_ACTION = RES_SOUND_PATH + "action.au";
	public static final String SOUND_ENDGAME = RES_SOUND_PATH + "endgame.au";

	/* Colours */
	public static final Color COLOUR_APPLET_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color COLOUR_BOARD_BORDER = new Color(39, 39, 39);
	public static final Color COLOUR_STATUS_TEXT = Color.BLACK;
	public static final Color COLOUR_WINDOW_BACKGROUND = new Color(216, 204, 192);
	public static final Color COLOUR_WINDOW_FOREGROUND = Color.BLACK;
	public static final Color COLOUR_WINDOW_HI_FOREGROUND = new Color(36, 86, 166);
	public static final Color COLOUR_SCROLLBAR_HI = new Color(162, 153, 144);
	public static final Color COLOUR_SCORED_MOVE_TEXT = new Color(160, 160, 160);
	public static final Color COLOUR_BEST_SCORED_MOVE_TEXT = new Color(224, 224, 224);
	
	/* Levels */
	public static final int[] SEARCH_LEVELS = {5, 5, 7, 8, 10, 16};
	public static final int[] SEARCH_THRESHOLDS = {200, 500, 2000, 5000, 25000, 50000};
//	public static final int[] SEARCH_LEVELS = {4, 5, 7, 8, 10, 16};
//	public static final int[] SEARCH_THRESHOLDS = {250, 500, 2500, 5000, 10000, 50000};
	
	/* Search stuff */
	public static final int KILLER_MOVES_PER_LEVEL = 4;
	
	/* Players */
	public static final Player[] PLAYERS = {ReversiPlayer.getInstance(Colour.BLACK), ReversiPlayer.getInstance(Colour.WHITE)};

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
	public static final int BOARD_BORDER_SIZE = 8;
	public static final int BOARD_SIZE = 225;
	public static final int SELECT_ITEM_SPACING = 8;
	public static final int TEXT_WINDOW_SPACING = 8;
	public static final int TEXT_WINDOW_INSET = 16;

	/* Strings */
	public static final String THINKING_MESSAGE = "Thinking";
	public static final String[] PROGRESS_STRINGS = {"  ", ". ", "..", ". "};
	public static final String LABEL_CONFIRM_NEW = "Start a new game?";
	public static final String[] LABEL_OPTIONS_CONFIRM_NEW = {"Yes", "No"};
	public static final String HELP_TEXT =
		"Lurgee Reversi " + Version.VERSION + "\n" +
		"Michael Patricios 2005-2008\n" +
		"\n" +
		"Part of the Strategy Game Framework project at lurgee.net.\n" +
		"\n" +
		"HOW TO PLAY\n" +
		"Reversi (also known as Othello) is the classic board game where the aim is to have more " +
		"pieces on the board than your opponent when the game ends. Your opponent's pieces may be " +
		"flipped to yours by trapping them between two of your pieces horizontally, vertically or " +
		"diagonally. Every move must result in at least one of your opponent's pieces being flipped. " +
		"If a player has no valid moves, the other player plays again. The game ends when the " +
		"board is full or when neither player can make a valid move.\n" +
		"\n" +
		"Black always plays first. Select the colour you would like to play using the colour icon and " +
		"select the computer's skill level (1 being the easiest and 6 the most difficult) using the " +
		"level icon.\n" +
		"\n" +
		"LICENSE\n" +
		"This is free software released under a MIT License - see lurgee.net for more details.\n" +
		"\n" +
		"Click in the applet, outside of this window, to return to the game!";
}
