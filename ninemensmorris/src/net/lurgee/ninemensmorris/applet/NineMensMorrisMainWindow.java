/*
 * @(#)NineMensMorrisMainWindow.java		2008/03/08
 *
 * Part of the ninemensmorris applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris.applet;

import java.applet.Applet;
import java.awt.Graphics;
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
import net.lurgee.ninemensmorris.Colour;
import net.lurgee.ninemensmorris.NineMensMorrisBoard;
import net.lurgee.ninemensmorris.NineMensMorrisMove;
import net.lurgee.ninemensmorris.NineMensMorrisMoveFactory;
import net.lurgee.ninemensmorris.NineMensMorrisPlayer;
import net.lurgee.sgf.GameContext;
import net.lurgee.sgf.ObjectPool;
import net.lurgee.sgf.Player;

/**
 * Main window for the game. Supports painting and interaction with the user.
 * @author mpatric
 */
public class NineMensMorrisMainWindow extends AbstractGameWindow {

	private static final long serialVersionUID = -879575833944121063L;
	private static final int ANIMATION_MILLIS_PER_TICK = 15;
	private static final Player[] PLAYERS = new Player[] {NineMensMorrisPlayer.getInstance(Colour.WHITE), NineMensMorrisPlayer.getInstance(Colour.BLACK)};
	private static final Integer SURFACE_IMAGE = new Integer(1);

	private NineMensMorrisBoardWidget boardWidget = null;

	public NineMensMorrisMainWindow(Applet applet, int x, int y, int width, int height) throws IOException {
		super(applet, new NineMensMorrisGame(
				new GameContext(PLAYERS, new ObjectPool(NineMensMorrisBoard.class), new NineMensMorrisMoveFactory(), true),
				new Settings(AppletConsts.SEARCH_LEVELS, AppletConsts.SEARCH_THRESHOLDS, AppletConsts.PLAYERS, 0, 3)), x, y, width, height);
		loadImages();
		loadAudioClips();
		// new game option window
		OptionWindow newGameOptionWindow = new OptionWindow(this, AppletConsts.LABEL_CONFIRM_NEW, AppletConsts.LABEL_OPTIONS_CONFIRM_NEW, AppletConsts.COLOUR_WINDOW_FOREGROUND, AppletConsts.COLOUR_WINDOW_BACKGROUND, AppletConsts.COLOUR_WINDOW_HI_FOREGROUND, AppletConsts.DEFAULT_FONT, AppletConsts.SELECT_ITEM_SPACING) {
			private static final long serialVersionUID = 0L;
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
		loadImage(SURFACE_IMAGE, AppletConsts.BOARD_SURFACE_IMAGE);
	}

	private void loadAudioClips() {
		loadAudioClip(ALERT_AUDIO, AppletConsts.SOUND_ALERT);
		loadAudioClip(ACTION_AUDIO, AppletConsts.SOUND_ACTION);
		loadAudioClip(END_GAME_AUDIO, AppletConsts.SOUND_ENDGAME);
	}

	private void addIcons() throws IOException {
		int iy = AppletConsts.ICON_SPACING;
		int ix = AppletConsts.ICON_SPACING;
		// colour icon
		String[] colourIcons = {
				AppletConsts.ICON_COLOUR_WHITE_FILENAME,
				AppletConsts.ICON_COLOUR_BLACK_FILENAME
		};
		int humanColour = ((NineMensMorrisPlayer)game.getSettings().getPlayer()).getColour();
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
		boardWidget = new NineMensMorrisBoardWidget(this, 0, AppletConsts.ICON_HEIGHT, getWidth(), 8 * AppletConsts.ICON_HEIGHT, (NineMensMorrisGame) game) {
			private static final long serialVersionUID = -4550738007612907183L;
			protected void selectAction(MouseEvent e) {
				setEnabled(false);
				NineMensMorrisMove moveToPlay = ((NineMensMorrisMoveFactory) game.getGameContext().getMoveFactory()).createMove(toPlayPosition1, toPlayPosition2, toPlayCapturePosition);
				playMove(moveToPlay);
			}
		};
		add(boardWidget);
		// status widget
		NineMensMorrisStatusWidget statusWidget = new NineMensMorrisStatusWidget(this, 0, getHeight() - AppletConsts.ICON_HEIGHT, getWidth(), AppletConsts.ICON_HEIGHT, (NineMensMorrisGame) game);
		add(statusWidget);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(getImage(SURFACE_IMAGE), 0, 0, this);
		super.paint(g);
	}

	@Override
	protected void undo() {
		if (!boardWidget.undo()) {
			super.undo();
		}
	}
	
	protected void setupAnimator() {
		setAnimator(new Animator(boardWidget, ANIMATION_MILLIS_PER_TICK));
	}

	protected void setupThinker() {
		setThinker(new Thinker(this, game, AppletConsts.THINKING_MESSAGE, AppletConsts.PROGRESS_STRINGS));
	}
	
	@Override
	public String toString() {
		return "NineMensMorrisMainWindow:" + super.toString();
	}
}
