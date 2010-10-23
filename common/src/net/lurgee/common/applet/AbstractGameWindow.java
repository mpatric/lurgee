/*
 * @(#)AbstractGameWindow.java		2007/06/07
 *
 * Part of the common applet classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.applet;

import java.applet.Applet;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.lurgee.common.awt.IconWidget;
import net.lurgee.common.awt.MainWindow;
import net.lurgee.common.awt.StatefulIconWidget;
import net.lurgee.common.awt.Widget;
import net.lurgee.common.awt.WidgetEvent;
import net.lurgee.common.awt.WidgetEventListener;
import net.lurgee.sgf.Move;
import net.lurgee.sgf.Player;

/**
 * Abstract main window class, which contains common functionality for main windows for all games.
 * @author mpatric
 */
public abstract class AbstractGameWindow extends MainWindow implements WidgetEventListener {

	private static final long serialVersionUID = -7441458989575697868L;
	private static final String WIN_MESSAGE = " wins";
	private static final String DRAW_MESSAGE = "It's a draw";
	
	protected static final Integer HELP_ICON = new Integer(-1);
	protected static final Integer SOUND_ICON = new Integer(-2);
	protected static final Integer UNDO_ICON = new Integer(-3);
	protected static final Integer NEW_ICON = new Integer(-4);
	protected static final Integer COLOUR_ICON = new Integer(-5);
	protected static final Integer LEVEL_ICON = new Integer(-6);
	
	protected static final Integer START_PLAY_AUDIO = new Integer(-1);
	protected static final Integer END_PLAY_AUDIO = new Integer(-2);
	protected static final Integer ALERT_AUDIO = new Integer(-3);
	protected static final Integer ACTION_AUDIO = new Integer(-4);
	protected static final Integer END_GAME_AUDIO = new Integer(-5);

	protected final AbstractGame game;
	private final Map<Integer, IconWidget> icons = new HashMap<Integer, IconWidget>();

	protected Thinker thinker;
	protected Animator animator;
	protected boolean undoing = false;
	private boolean closeHelp = false;
	private boolean gameOver = false;

	public AbstractGameWindow(Applet applet, AbstractGame game, int x, int y, int width, int height) {
		super(applet, null, x, y, width, height);
		this.game = game;
		addWidgetEventListener(this);
		game.setup();
	}
	
	protected void setThinker(Thinker thinker) {
		this.thinker = thinker;
		game.getSearcher().addSearchProgressListener(thinker);
	}
	
	protected void setAnimator(Animator animator) {
		this.animator = animator;
	}
	
	public boolean isUndoing() {
		return undoing;
	}
	
	@Override
	public void playAudioClip(Integer key) {
		if (game.getSettings().isSoundOn()) {
			super.playAudioClip(key);
		}
	}
	
	protected void addIcon(Integer key, IconWidget icon) {
		if (icons.containsKey(key)) {
			throw new IllegalArgumentException("Icon already registered with that key");
		}
		icon.setId(key.intValue());
		icons.put(key, icon);
		add(icon);
	}
	
	protected IconWidget getIcon(Integer key) {
		return icons.get(key);
	}

	protected void selectIcon(int iconId, int state) {
		if (iconId == HELP_ICON.intValue()) {
			closeHelp = true;
			postWidgetEvent(Event.OPEN_HELP);
		} else if (iconId == SOUND_ICON.intValue()) {
			game.getSettings().setSoundOn(state != 0);
		} else if (iconId == LEVEL_ICON.intValue()) {
			game.getSettings().setLevelIndex(state + 1);
		} else if (iconId == COLOUR_ICON.intValue()) {
			thinker.abort();
			animator.abort();
			game.getSettings().setPlayerIndex(state + 1);
			updateIconStates();
		} else if (iconId == NEW_ICON.intValue()) {
			if (!game.getBoard().isGameOver()) {
				playAudioClip(ALERT_AUDIO);
				postWidgetEvent(Event.NEW_GAME);
			} else {
				playAudioClip(ACTION_AUDIO);
				startGame();
			}
		} else if (iconId == UNDO_ICON.intValue()) {
			undoing = true;
			thinker.abort();
			animator.abort();
			playAudioClip(ACTION_AUDIO);
			undo();
			undoing = false;
			if (gameOver) {
				gameOver = false;
			}
			postWidgetEvent(Event.REFRESH_STATUS);
			postWidgetEvent(Event.REFRESH_BOARD);
			updateIconStates();
		}
	}

	protected void undo() {
		game.undo();
	}
	
	public void updateIconStates() {
		// see if undo button should be enabled or disabled
		IconWidget undoIcon = getIcon(UNDO_ICON);
		if (game.canUndo()) {
			if (!undoIcon.isEnabled()) {
				undoIcon.setEnabled(true);
				undoIcon.repaint();
			}
		} else {
			if (undoIcon.isEnabled()) {
				undoIcon.setEnabled(false);
				undoIcon.repaint();
			}
		}
		// see if new button should be enabled or disabled
		IconWidget newIcon = getIcon(NEW_ICON);
		if (game.getBoard().countMovesMade() == 0) {
			if (newIcon.isEnabled()) {
				newIcon.setEnabled(false);
				newIcon.repaint();
			}
		} else {
			if (!newIcon.isEnabled()) {
				newIcon.setEnabled(true);
				newIcon.repaint();
			}
		}
	}
	
	public void processWidgetEvent(WidgetEvent event) {
		switch(event.getID()) {
			case Event.PLAY_MOVE:
				if (event.getData() != null) {
					playMove((Move) event.getData());
				} else {
					playMove(null);
				}
				break;
			case Event.DONE_PLAYING:
				Boolean abortedMove = (Boolean) event.getData();
				donePlaying(abortedMove.booleanValue());
				break;
		}
	}
	
	protected void startGame() {
		if (thinker == null) {
			setupThinker();
		}
		if (animator == null) {
			setupAnimator();
		}
		animator.abort();
		thinker.abort();
		gameOver = false;
		game.setup();
		postWidgetEvent(Event.REFRESH_BOARD);
		postWidgetEvent(Event.REFRESH_STATUS);
		updateIconStates();
	}

	protected void playMove(Move move) {
		if (move != null) {
			game.abortSearches();
			game.playMove(move);
			playAudioClip(START_PLAY_AUDIO);
			animator.start();
			postWidgetEvent(Event.REFRESH_BOARD);			
			postWidgetEvent(Event.REFRESH_STATUS);			
		}
		updateIconStates();	
	}

	private void donePlaying(boolean abortedMove) {
		if (! gameOver) {
			if (! abortedMove) {
				playAudioClip(END_PLAY_AUDIO);
			}
			if (game.getBoard().isGameOver()) {
				gameOver = true;
				String winner = game.getWinner();
				if (winner == null) {
					game.setStatusMessage(DRAW_MESSAGE);
				} else {
					game.setStatusMessage(winner + WIN_MESSAGE);
				}
				playAudioClip(END_GAME_AUDIO);
				postWidgetEvent(Event.REFRESH_STATUS);
			} else {
				postWidgetEvent(Event.REFRESH_STATUS);
				Player currentPlayer = game.getBoard().getCurrentPlayer();
				if (currentPlayer.equals(game.getSettings().getPlayer())) {
					postWidgetEvent(Event.ENABLE_BOARD);
					postWidgetEvent(Event.REFRESH_BOARD);
				} else {
					thinker.start();
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (closeHelp) {
			postWidgetEvent(Event.CLOSE_HELP);
			// request a repaint in case the position of the mouse causes a change in the main window (e.g. it's over an icon)
			repaint();
			closeHelp = false;
		} else {
			super.mouseReleased(e);
		}
	}

	/** Setup a thinker. */
	protected abstract void setupThinker();
	
	/** Setup an animator. */
	protected abstract void setupAnimator();
	
	/**
	 * Inner class for representing icons in the main window.
	 */
	protected class MainWindowIconWidget extends IconWidget {
	
		private static final long serialVersionUID = -7888775366785079761L;
		
		public MainWindowIconWidget(Widget parentWidget, int x, int y, int width, int height, String iconFilename, String backFilename, String hiFilename, String disabledFilename) throws IOException {
			super(parentWidget, x, y, width, height, iconFilename, backFilename, hiFilename, disabledFilename);
		}
		
		protected void selectAction(MouseEvent e) {
			selectIcon(getId(), 0);
		}
	}

	/**
	 * Inner class for representing stateful icons in the main window.
	 */
	protected class MainWindowStatefulIconWidget extends StatefulIconWidget {
	
		private static final long serialVersionUID = 4868521749524404125L;

		public MainWindowStatefulIconWidget(Widget parentWidget, int x, int y, int width, int height, int initialState, String[] iconFilenames, String backFilename, String hiFilename, String disabledFilename) throws IOException {
			super(parentWidget, x, y, width, height, initialState, iconFilenames, backFilename, hiFilename, disabledFilename);
		}
		
		protected void selectAction(MouseEvent e) {
			selectIcon(getId(), getState());
		}
	}
}
