/*
 * @(#)ReversiMainWindow.java		2006/02/04
 *
 * Part of the reversi applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.applet;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;

import net.lurgee.common.applet.AbstractGameWindow;
import net.lurgee.common.applet.Animator;
import net.lurgee.common.applet.Event;
import net.lurgee.common.applet.Settings;
import net.lurgee.common.applet.Thinker;
import net.lurgee.common.awt.OptionWindow;
import net.lurgee.common.awt.TextWindow;
import net.lurgee.common.awt.WidgetEvent;
import net.lurgee.reversi.Colour;
import net.lurgee.reversi.ReversiBoard;
import net.lurgee.reversi.ReversiMove;
import net.lurgee.reversi.ReversiMoveFactory;
import net.lurgee.reversi.ReversiPlayer;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;

/**
 * Main window for the game. Supports painting and interaction with the user.
 * @author mpatric
 */
public class ReversiMainWindow extends AbstractGameWindow {

	private static final long serialVersionUID = -4638836298799197315L;
	private static final int ANIMATION_MILLIS_PER_TICK = 50;
	private static final Integer WOOD_IMAGE = new Integer(1);
	private static final Player[] PLAYERS = new Player[] {ReversiPlayer.getInstance(Colour.BLACK), ReversiPlayer.getInstance(Colour.WHITE)};
	protected static final Integer FLIP_AUDIO = new Integer(1);

	private ReversiBoardWidget boardWidget = null;

	public ReversiMainWindow(Applet applet, int x, int y, int width, int height) throws IOException {
		super(applet, new ReversiGame(
				new GameContext(PLAYERS, new ObjectPool(ReversiBoard.class), new ReversiMoveFactory(), true),
				new Settings(AppletConsts.SEARCH_LEVELS, AppletConsts.SEARCH_THRESHOLDS, AppletConsts.PLAYERS, 0, 0)), x, y, width, height);
		loadImages();
		loadAudioClips();
		// new game option window
		OptionWindow newGameOptionWindow = new OptionWindow(this, AppletConsts.LABEL_CONFIRM_NEW, AppletConsts.LABEL_OPTIONS_CONFIRM_NEW, AppletConsts.COLOUR_WINDOW_FOREGROUND, AppletConsts.COLOUR_WINDOW_BACKGROUND, AppletConsts.COLOUR_WINDOW_HI_FOREGROUND, AppletConsts.DEFAULT_FONT, AppletConsts.SELECT_ITEM_SPACING) {
			private static final long serialVersionUID = 558060748211269739L;
			public void processWidgetEvent(WidgetEvent event) {
				switch (event.getID()) {
					case Event.NEW_GAME:
						open();
						break;
				}
			}
			protected void selectAction(MouseEvent e) {
				if (selection == 0) {
					playAudioClip(ACTION_AUDIO);
					startGame();
				}
			}
		};
		newGameOptionWindow.close();
		add(newGameOptionWindow);
		// help text window
		TextWindow helpTextWindow = new TextWindow(this, getWidth() - (2 * AppletConsts.TEXT_WINDOW_INSET), getHeight() - (2 * AppletConsts.TEXT_WINDOW_INSET), AppletConsts.HELP_TEXT, AppletConsts.COLOUR_WINDOW_FOREGROUND, AppletConsts.COLOUR_WINDOW_BACKGROUND, AppletConsts.COLOUR_WINDOW_HI_FOREGROUND, AppletConsts.COLOUR_SCROLLBAR_HI, AppletConsts.TEXT_WINDOW_FONT, AppletConsts.TEXT_WINDOW_SPACING) {
			private static final long serialVersionUID = -4660922912811065972L;
			@Override
			public void processWidgetEvent(WidgetEvent event) {
				switch (event.getID()) {
					case Event.OPEN_HELP:
						open();
						break;
					case Event.CLOSE_HELP:
						close();
						break;
				}
			}
		};
		helpTextWindow.close();
		add(helpTextWindow);
		addIcons();
		addWidgets();
		startGame();
	}

	private void loadImages() throws IOException {
		loadImage(WOOD_IMAGE, AppletConsts.WOOD_IMAGE_FILENAME);
	}

	private void loadAudioClips() {
		loadAudioClip(START_PLAY_AUDIO, AppletConsts.SOUND_START_PLAY);
		loadAudioClip(FLIP_AUDIO, AppletConsts.SOUND_FLIP);
		loadAudioClip(ALERT_AUDIO, AppletConsts.SOUND_ALERT);
		loadAudioClip(ACTION_AUDIO, AppletConsts.SOUND_ACTION);
		loadAudioClip(END_GAME_AUDIO, AppletConsts.SOUND_ENDGAME);
	}

	private void addIcons() throws IOException {
		int iy = AppletConsts.ICON_SPACING;
		int ix = AppletConsts.ICON_SPACING;
		// colour icon
		String[] colourIcons = {
				AppletConsts.ICON_COLOUR_BLACK_FILENAME,
				AppletConsts.ICON_COLOUR_WHITE_FILENAME
		};
		int humanColour = ((ReversiPlayer)game.getSettings().getPlayer()).getColour();
		addIcon(COLOUR_ICON, new MainWindowStatefulIconWidget(this, ix, iy, AppletConsts.ICON_WIDTH, AppletConsts.ICON_HEIGHT, humanColour - 1, colourIcons, null, AppletConsts.ICON_SELECT, null));
		// level icon
		String[] levelIcons = new String[AppletConsts.SEARCH_LEVELS.length];
		for (int j = 1; j <= levelIcons.length; j++) {
			levelIcons[j - 1] = AppletConsts.ICON_LEVEL_PREFIX + j + AppletConsts.ICON_LEVEL_SUFFIX;
		}
		ix += AppletConsts.ICON_WIDTH + AppletConsts.ICON_SPACING + 1;
		addIcon(LEVEL_ICON, new MainWindowStatefulIconWidget(this, ix, iy, AppletConsts.ICON_WIDTH, AppletConsts.ICON_HEIGHT, game.getSettings().getLevelIndex() - 1, levelIcons, null, AppletConsts.ICON_SELECT, null));	
		// new icon
		ix = getWidth() - AppletConsts.ICON_SPACING - AppletConsts.ICON_WIDTH - 1;
		addIcon(NEW_ICON, new MainWindowIconWidget(this, ix, iy, AppletConsts.ICON_WIDTH, AppletConsts.ICON_HEIGHT, AppletConsts.ICON_NEW_FILENAME, null, AppletConsts.ICON_SELECT, AppletConsts.ICON_NEW_DISABLED_FILENAME));
		// help icon
		ix -= AppletConsts.ICON_SPACING + AppletConsts.ICON_WIDTH + 1;
		addIcon(HELP_ICON, new MainWindowIconWidget(this, ix, iy, AppletConsts.ICON_WIDTH, AppletConsts.ICON_HEIGHT, AppletConsts.ICON_HELP_FILENAME, null, AppletConsts.ICON_SELECT, null));
		// sound icon
		String[] soundIcons = {
				AppletConsts.ICON_SOUND_OFF_FILENAME,
				AppletConsts.ICON_SOUND_ON_FILENAME
		};
		ix -= AppletConsts.ICON_SPACING + AppletConsts.ICON_WIDTH + 1;
		int sound = 0;
		if (game.getSettings().isSoundOn()) sound = 1;
		addIcon(SOUND_ICON, new MainWindowStatefulIconWidget(this, ix, iy, AppletConsts.ICON_WIDTH, AppletConsts.ICON_HEIGHT, sound, soundIcons, null, AppletConsts.ICON_SELECT, null));
		// undo icon
		ix -= AppletConsts.ICON_SPACING + AppletConsts.ICON_WIDTH + 1;
		addIcon(UNDO_ICON, new MainWindowIconWidget(this, ix, iy, AppletConsts.ICON_WIDTH, AppletConsts.ICON_HEIGHT, AppletConsts.ICON_UNDO_FILENAME, null, AppletConsts.ICON_SELECT, AppletConsts.ICON_UNDO_DISABLED_FILENAME));
	}

	private void addWidgets() throws IOException {
		// board widget
		boardWidget = new ReversiBoardWidget(this, AppletConsts.BOARD_BORDER_SIZE, (2 * AppletConsts.ICON_SPACING) + AppletConsts.ICON_HEIGHT + AppletConsts.BOARD_BORDER_SIZE + 1, AppletConsts.BOARD_SIZE, AppletConsts.BOARD_SIZE, (ReversiGame) game) {
			private static final long serialVersionUID = -4550738007612907183L;
			protected void selectAction(MouseEvent e) {
				setEnabled(false);
				ReversiMove moveToPlay = ((ReversiMoveFactory) game.getGameContext().getMoveFactory()).createMove(overX, overY);
				playMove(moveToPlay);
			}
		};
		add(boardWidget);
		// status widget
		ReversiStatusWidget statusWidget = new ReversiStatusWidget(this, 7, 284, 226, 27, (ReversiGame) game);
		add(statusWidget);
	}

	@Override
	public void paint(Graphics g) {
		// wood areas
		Image woodImage = getImage(WOOD_IMAGE);
		if (g.hitClip(0, 0, woodImage.getWidth(this), woodImage.getHeight(this))) g.drawImage(woodImage, 0, 0, this);
		int gy = (2 * AppletConsts.ICON_SPACING) + AppletConsts.ICON_HEIGHT + (2 * AppletConsts.BOARD_BORDER_SIZE) + AppletConsts.BOARD_SIZE + 1;
		if (g.hitClip(0, gy, woodImage.getWidth(this), woodImage.getHeight(this))) g.drawImage(woodImage, 0, gy, this);
		// board boarder
		int iy = AppletConsts.ICON_HEIGHT + (2 * AppletConsts.ICON_SPACING) + 1;
		if (g.hitClip(0, iy, getWidth(), AppletConsts.BOARD_SIZE)) {
			g.setColor(AppletConsts.COLOUR_BOARD_BORDER);
			g.fillRect(0, iy, getWidth(), AppletConsts.BOARD_BORDER_SIZE);
			g.fillRect(0, iy + AppletConsts.BOARD_SIZE + AppletConsts.BOARD_BORDER_SIZE, getWidth(), AppletConsts.BOARD_BORDER_SIZE);
			g.fillRect(0, iy + AppletConsts.BOARD_BORDER_SIZE, AppletConsts.BOARD_BORDER_SIZE, AppletConsts.BOARD_SIZE);
			g.fillRect(AppletConsts.BOARD_SIZE + AppletConsts.BOARD_BORDER_SIZE, iy + AppletConsts.BOARD_BORDER_SIZE, AppletConsts.BOARD_BORDER_SIZE - 1, AppletConsts.BOARD_SIZE);
		}
		super.paint(g);
	}
	
	protected void setupAnimator() {
		setAnimator(new Animator(boardWidget, ANIMATION_MILLIS_PER_TICK));
	}

	protected void setupThinker() {
		setThinker(new Thinker(this, game, AppletConsts.THINKING_MESSAGE, AppletConsts.PROGRESS_STRINGS));
	}
	
	@Override
	public String toString() {
		return "ReversiMainWindow:" + super.toString();
	}
}
