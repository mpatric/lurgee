/*
 * @(#)NineMensMorrisBoardWidget.java		2008/03/08
 *
 * Part of the ninemensmorris applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris.applet;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;

import net.lurgee.common.applet.AbstractBoardWidget;
import net.lurgee.common.applet.AbstractGame;
import net.lurgee.common.applet.Event;
import net.lurgee.common.awt.Widget;
import net.lurgee.ninemensmorris.Colour;
import net.lurgee.ninemensmorris.NineMensMorrisBoard;
import net.lurgee.ninemensmorris.NineMensMorrisMove;
import net.lurgee.ninemensmorris.NineMensMorrisPlayer;
import net.lurgee.ninemensmorris.NineMensMorrisPosition;

/**
 * Abstract class for a widget representing the game board. Supports painting and interaction with the user. The abstract selectAction
 * method needs to be defined by a concrete subclass (anonymous or otherwise).
 * @author mpatric
 */
public abstract class NineMensMorrisBoardWidget extends AbstractBoardWidget {

	private static final long serialVersionUID = -2656920959120220604L;
	private static final int PIECES_IN_HAND_SPACING_FROM_EDGE = 9;

	private static final Integer WHITE_IMAGE = new Integer(1);
	private static final Integer BLACK_IMAGE = new Integer(2);
	private static final Integer HH = new Integer(3);
	private static final Integer VV = new Integer(4);
	private static final Integer TL = new Integer(5);
	private static final Integer TR = new Integer(6);
	private static final Integer BR = new Integer(7);
	private static final Integer BL = new Integer(8);
	private static final Integer TT = new Integer(9);
	private static final Integer RR = new Integer(10);
	private static final Integer BB = new Integer(11);
	private static final Integer LL = new Integer(12);
	private static final Integer MM = new Integer(13);
	private static final Integer SHADOW_IMAGE = new Integer(14);
	private static final Integer SELECT_SPOT_IMAGE = new Integer(15);
	
	private static final Integer[][] LINE_IMAGE_MAP = {
		{TL, HH, HH, TT, HH, HH, TR},
		{VV, TL, HH, MM, HH, TR, VV},
		{VV, VV, TL, BB, TR, VV, VV},
		{LL, MM, RR,  0, LL, MM, RR},
		{VV, VV, BL, TT, BR, VV, VV},
		{VV, BL, HH, MM, HH, BR, VV},
		{BL, HH, HH, BB, HH, HH, BR}
	};

	private static final int ANIMATION_SPEED = 6;
	private static final int CAPTURE_ANIMATION_LENGTH = 15;
	
	private static final int STATE_READY = 0;
	private static final int STATE_USER_DRAGGING = 1;
	private static final int STATE_USER_CAPTURING = 2;
	private static final int STATE_ANIMATING_SHIFT = 3;
	private static final int STATE_ANIMATING_CAPTURE = 4;
	
	private int mouseX = -1;
	private int mouseY = -1;
	private int dragOffsetX;
	private int dragOffsetY;
	
	protected int state = STATE_READY;
	protected int toPlayPosition1 = -1;
	protected int toPlayPosition2 = -1;
	protected int toPlayCapturePosition = -1;

	private int squareWidth;

	private int animationFrame;
	private double dx;
	private double dy;
	
	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param game A ninemensmorris game container.
	 * @throws IOException If any of the images cannot be loaded.
	 */
	public NineMensMorrisBoardWidget(Widget parentWidget, int x, int y, int width, int height, AbstractGame game) throws IOException {
		super(parentWidget, x, y, width, height, game);
		loadImages();
		squareWidth = getWidth() / NineMensMorrisBoard.X_DIMENSION;
	}

	private void loadImages() throws IOException {
		loadImage(BLACK_IMAGE, AppletConsts.PIECE_BLACK_IMAGE_FILENAME);
		loadImage(WHITE_IMAGE, AppletConsts.PIECE_WHITE_IMAGE_FILENAME);
		loadImage(HH, AppletConsts.BOARD_1H_IMAGE_FILENAME);
		loadImage(VV, AppletConsts.BOARD_1V_IMAGE_FILENAME);
		loadImage(TL, AppletConsts.BOARD_2TL_IMAGE_FILENAME);
		loadImage(TR, AppletConsts.BOARD_2TR_IMAGE_FILENAME);
		loadImage(BR, AppletConsts.BOARD_2BR_IMAGE_FILENAME);
		loadImage(BL, AppletConsts.BOARD_2BL_IMAGE_FILENAME);
		loadImage(TT, AppletConsts.BOARD_3T_IMAGE_FILENAME);
		loadImage(RR, AppletConsts.BOARD_3R_IMAGE_FILENAME);
		loadImage(BB, AppletConsts.BOARD_3B_IMAGE_FILENAME);
		loadImage(LL, AppletConsts.BOARD_3L_IMAGE_FILENAME);
		loadImage(MM, AppletConsts.BOARD_4_IMAGE_FILENAME);
		loadImage(SHADOW_IMAGE, AppletConsts.PIECE_SHADOW_IMAGE);
		loadImage(SELECT_SPOT_IMAGE, AppletConsts.PIECE_SELECT_SPOT_IMAGE);
	}
	
	private int screenToBoard(int screen) {
		return screen / squareWidth;
	}
	
	private int boardToScreen(int board) {
		return board * squareWidth;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (mouseX != -1 && isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			if (state == STATE_READY) {
				repaintSquare(screenToBoard(mouseX), screenToBoard(mouseY));
			} else if (state == STATE_USER_DRAGGING) {					
				changeState(STATE_READY);
				repaint(mouseX - dragOffsetX, mouseY - dragOffsetY, squareWidth, squareWidth);
				repaintSquare(screenToBoard(mouseX - dragOffsetX + squareWidth/2), screenToBoard(mouseY - dragOffsetY + squareWidth/2)); 
				repaintSquare(toPlayPosition1);
			}
			mouseX = -1;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		int oldBoardX;
		int oldBoardY;
		int boardX;
		int boardY;
		if (state == STATE_USER_DRAGGING) {
			oldBoardX = screenToBoard(mouseX - dragOffsetX + squareWidth/2);
			oldBoardY = screenToBoard(mouseY - dragOffsetY + squareWidth/2);
			boardX = screenToBoard(e.getX() - dragOffsetX + squareWidth/2);
			boardY = screenToBoard(e.getY() - dragOffsetY + squareWidth/2);
		} else {
			oldBoardX = screenToBoard(mouseX);
			oldBoardY = screenToBoard(mouseY);
			boardX = screenToBoard(e.getX());
			boardY = screenToBoard(e.getY());
		}
		if ((mouseX == -1 || boardX != oldBoardX || boardY != oldBoardY) && isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			if (mouseX != -1) {
				repaintSquare(oldBoardX, oldBoardY);
			}
			mouseX = e.getX();
			mouseY = e.getY();
			repaintSquare(boardX, boardY);
		} else {
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			if (state == STATE_USER_DRAGGING) {
				// dragged piece - old position
				repaint(mouseX - dragOffsetX, mouseY - dragOffsetY, squareWidth, squareWidth);
				// dragged piece - new position
				repaint(e.getX() - dragOffsetX, e.getY() - dragOffsetY, squareWidth, squareWidth);
				// destination square - old position
				repaintSquare(screenToBoard(mouseX - dragOffsetX + squareWidth/2), screenToBoard(mouseY - dragOffsetY + squareWidth/2)); 
				// destination square - new position
				repaintSquare(screenToBoard(e.getX() - dragOffsetX + squareWidth/2), screenToBoard(e.getY() - dragOffsetY + squareWidth/2));
			}		
			mouseMoved(e);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (mouseX != -1 && isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			if (state == STATE_READY) {
				NineMensMorrisBoard board = ((NineMensMorrisBoard) game.getBoard());
				if (!board.isGameOver()) {
					NineMensMorrisPlayer currentPlayer = ((NineMensMorrisPlayer) game.getBoard().getCurrentPlayer());
					int colour = currentPlayer.getColour();
					if (board.countPiecesInHand(colour) == 0) {
						try {
							int position = NineMensMorrisPosition.lookupPositionFromCoordinates(screenToBoard(mouseX), screenToBoard(mouseY));
							if (board.getColour(position) == colour) {
								// start dragging piece
								toPlayPosition1 = position;
								dragOffsetX = mouseX - boardToScreen(screenToBoard(mouseX));
								dragOffsetY = mouseY - boardToScreen(screenToBoard(mouseY));
								changeState(STATE_USER_DRAGGING);
								repaintSquare(toPlayPosition1);
							}
						} catch (IllegalArgumentException iae) {
							// do nothing
						}
					}
				}
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (state == STATE_USER_DRAGGING) {
			NineMensMorrisBoard board = ((NineMensMorrisBoard) game.getBoard());
			if (!board.isGameOver()) {
				try {
					int position = NineMensMorrisPosition.lookupPositionFromCoordinates(screenToBoard(mouseX - dragOffsetX + squareWidth/2), screenToBoard(mouseY - dragOffsetY + squareWidth/2));
					if (board.isValidShift(toPlayPosition1, position)) {
						toPlayPosition2 = position;
						if (board.shiftWouldCreateMill(toPlayPosition1, toPlayPosition2)) {
							changeState(STATE_USER_CAPTURING);
						} else {					
							selectAction(e);
						}
						repaintSquare(toPlayPosition2);
					} else {
						// invalid move
						changeState(STATE_READY);
					}
				} catch (IllegalArgumentException iae) {
					// do nothing
					changeState(STATE_READY);
				} catch (ArrayIndexOutOfBoundsException aiobe) {
					// do nothing
					changeState(STATE_READY);
				}
				repaint(mouseX - dragOffsetX, mouseY - dragOffsetY, squareWidth, squareWidth);
				repaint(boardToScreen(screenToBoard(mouseX)), boardToScreen(screenToBoard(mouseY)), squareWidth, squareWidth);
				repaintSquare(toPlayPosition1);
			}
		}
	}

	private void changeState(int state) {
		if (this.state != state) {
			if (this.state == STATE_USER_CAPTURING) {
				game.setStatusMessage("");
				postWidgetEvent(Event.REFRESH_STATUS);
			}
			this.state = state;
			if (state == STATE_USER_CAPTURING) {
				game.setStatusMessage("Select piece to capture");
				postWidgetEvent(Event.REFRESH_STATUS);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (mouseX != -1 && isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			NineMensMorrisBoard board = ((NineMensMorrisBoard) game.getBoard());
			if (!board.isGameOver()) {
				NineMensMorrisPlayer currentPlayer = ((NineMensMorrisPlayer) game.getBoard().getCurrentPlayer());
				try {
					int position = NineMensMorrisPosition.lookupPositionFromCoordinates(screenToBoard(mouseX), screenToBoard(mouseY));
					if (state == STATE_READY && board.countPiecesInHand(currentPlayer.getColour()) > 0 && board.isValidPlacement(position)) {
						toPlayPosition1 = position;
						if (board.placementWouldCreateMill(position)) {
							changeState(STATE_USER_CAPTURING);
							repaintSquare(toPlayPosition1);
						} else {
							selectAction(e);
						}
					} else if (state == STATE_USER_CAPTURING && board.isValidCapture(position)) {
						toPlayCapturePosition = position;
						selectAction(e);
						changeState(STATE_ANIMATING_CAPTURE);
						animationFrame = 0;
					}
				} catch (IllegalArgumentException iae) {
					// do nothing
				}
			}
		} else {
			super.mouseClicked(e);
		}
	}

	private void reset() {
		toPlayPosition1 = -1;
		toPlayPosition2 = -1;
		toPlayCapturePosition = -1;
		changeState(STATE_READY);
	}

	@Override
	public void paint(Graphics g) {
		boolean busyAnimating = game.isBusy();
		NineMensMorrisPlayer currentPlayer = ((NineMensMorrisPlayer) game.getBoard().getCurrentPlayer());
		paintLines(g);
		paintStaticPieces(g, busyAnimating);
		paintInHandPieces(g);
		if (!busyAnimating && currentPlayer.equals(game.getSettings().getPlayer()) && !game.getBoard().isGameOver()) {
			paintSelectorSpot(g);
		}
		if (!busyAnimating && currentPlayer.equals(game.getSettings().getPlayer())) {
			paintDynamicUserPieces(g);
		}
		if (busyAnimating) {
			paintAnimatedPieces(g);
		}
	}
	
	private void paintLines(Graphics g) {
		for (int y = 0; y < NineMensMorrisBoard.X_DIMENSION; y++) {
			for (int x = 0; x < NineMensMorrisBoard.X_DIMENSION; x++) {
				int screenX = boardToScreen(x);
				int screenY = boardToScreen(y);
				if (g.hitClip(screenX, screenY, squareWidth, squareWidth)) {
					g.drawImage(getImage(LINE_IMAGE_MAP[y][x]), screenX, screenY, this);
				}
			}
		}
	}
	
	private void paintStaticPieces(Graphics g, boolean busyAnimating) {
		NineMensMorrisBoard board = ((NineMensMorrisBoard) game.getBoard());
		for (int position = 0; position < NineMensMorrisBoard.NUMBER_OF_POSITIONS; position++) {
			int[] coordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(position);
			int screenX = boardToScreen(coordinates[0]);
			int screenY = boardToScreen(coordinates[1]);
			if (g.hitClip(screenX, screenY, squareWidth, squareWidth)) {
				int colour = board.getColour(position);
				if (busyAnimating) {
					// Don't paint piece if we're animating and:
					// It's a shift animation and it's the end position for the shift
					NineMensMorrisMove lastMove = (NineMensMorrisMove) game.getBoard().getLastMovePlayed();
					if (lastMove == null  || state != STATE_ANIMATING_SHIFT || position != lastMove.getToPosition()) {
						paintPiece(g, screenX, screenY, colour, true, 0);
					}
				} else {
					// Don't paint piece if not animating and:
					// the user is dragging and this piece is at the start position for the drag
					// or the user is capturing and this piece is at the start position for a shift move
					if (state == STATE_READY || position != toPlayPosition1) {
						paintPiece(g, screenX, screenY, colour, true, 0);
					}
				}
			}
		}
	}

	private void paintInHandPieces(Graphics g) {
		NineMensMorrisBoard board = ((NineMensMorrisBoard) game.getBoard());
		int x = PIECES_IN_HAND_SPACING_FROM_EDGE;
		int y = boardToScreen(7);
		for (int i = 0; i < board.countPiecesInHand(Colour.WHITE); i++) {
			paintPiece(g, x, y, Colour.WHITE, true, 0);
			x += 1;
		}
		x = getWidth() - squareWidth - PIECES_IN_HAND_SPACING_FROM_EDGE;
		for (int i = 0; i < board.countPiecesInHand(Colour.BLACK); i++) {
			paintPiece(g, x, y, Colour.BLACK, true, 0);
			x += 1;
		}
	}
	
	private void paintSelectorSpot(Graphics g) {
		NineMensMorrisBoard board = ((NineMensMorrisBoard) game.getBoard());
		NineMensMorrisPlayer currentPlayer = (NineMensMorrisPlayer) board.getCurrentPlayer();
		if (mouseX >= 0) {
			try {
				if (state == STATE_READY) {
					int overX = screenToBoard(mouseX);
					int overY = screenToBoard(mouseY);
					int mouseoverPosition = NineMensMorrisPosition.lookupPositionFromCoordinates(overX, overY);
					int colour = currentPlayer.getColour();
					if (board.countPiecesInHand(colour) > 0) {
						if (board.isValidPlacement(mouseoverPosition)) {
							g.drawImage(getImage(SELECT_SPOT_IMAGE), boardToScreen(overX), boardToScreen(overY), this);
						}
					} else {
						if (board.getColour(mouseoverPosition) == colour) {
							g.drawImage(getImage(SELECT_SPOT_IMAGE), boardToScreen(overX), boardToScreen(overY), this);
						}
					}
				} else if (state == STATE_USER_DRAGGING) {
					int overX = screenToBoard(mouseX - dragOffsetX + squareWidth/2);
					int overY = screenToBoard(mouseY - dragOffsetY + squareWidth/2);
					int dragPosition = NineMensMorrisPosition.lookupPositionFromCoordinates(overX, overY);
					if (board.isValidShift(toPlayPosition1, dragPosition)) {
						g.drawImage(getImage(SELECT_SPOT_IMAGE), boardToScreen(overX), boardToScreen(overY), this);
					}
				} else if (state == STATE_USER_CAPTURING) {
					int overX = screenToBoard(mouseX);
					int overY = screenToBoard(mouseY);
					int mouseoverPosition = NineMensMorrisPosition.lookupPositionFromCoordinates(overX, overY);
					if (board.isValidCapture(mouseoverPosition)) {
						g.drawImage(getImage(SELECT_SPOT_IMAGE), boardToScreen(overX), boardToScreen(overY), this);
					}
				}
			} catch (IllegalArgumentException iae) {
				// mouse not in valid position square, so don't do anything
			}
		}
	}
	
	private void paintDynamicUserPieces(Graphics g) {
		NineMensMorrisPlayer currentPlayer = (NineMensMorrisPlayer) game.getBoard().getCurrentPlayer();
		if (state == STATE_USER_DRAGGING) {
			// dragging piece
			int screenX = mouseX - dragOffsetX;
			int screenY = mouseY - dragOffsetY;
			paintPiece(g, screenX, screenY, currentPlayer.getColour(), true, 3);
		} else if (state == STATE_USER_CAPTURING) {
			int[] coordinates;
			if (toPlayPosition2 != -1) {
				coordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(toPlayPosition2);
			} else {
				coordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(toPlayPosition1);
			}
			int screenX = boardToScreen(coordinates[0]);
			int screenY = boardToScreen(coordinates[1]);
			paintPiece(g, screenX, screenY, currentPlayer.getColour(), true, 0);
		}
	}
	
	private void paintAnimatedPieces(Graphics g) {
		NineMensMorrisMove lastMove = (NineMensMorrisMove) game.getBoard().getLastMovePlayed();
		int colour = ((NineMensMorrisPlayer) game.getPreviousBoard().getCurrentPlayer()).getColour();
		if (state == STATE_ANIMATING_SHIFT) {
			if (!lastMove.isPlacement()) {
				int[] fromCoordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(lastMove.getFromPosition());
				int screenX = (int) (boardToScreen(fromCoordinates[0]) + animationFrame * dx);
				int screenY = (int) (boardToScreen(fromCoordinates[1]) + animationFrame * dy);
				paintPiece(g, screenX, screenY, colour, true, 3);
			}
			if (lastMove.hasCapture()) {
				int[] captureCoordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(lastMove.getCapturePosition());
				int screenX = boardToScreen(captureCoordinates[0]);
				int screenY = boardToScreen(captureCoordinates[1]);
				paintPiece(g, screenX, screenY, 3 - colour, true, 0);
			}
		} else if (state == STATE_ANIMATING_CAPTURE) {
			if ((animationFrame / ANIMATION_SPEED) % 2 == 0) {
				int[] captureCoordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(lastMove.getCapturePosition());
				int screenX = boardToScreen(captureCoordinates[0]);
				int screenY = boardToScreen(captureCoordinates[1]);
				paintPiece(g, screenX, screenY, 3 - colour, true, 0);
			}
		}
	}

	private void paintPiece(Graphics g, int screenX, int screenY, int colour, boolean withShadow, int shadowOffset) {
		if (colour == Colour.WHITE) {
			if (withShadow) {
				g.drawImage(getImage(SHADOW_IMAGE), screenX, screenY, this);
			}
			g.drawImage(getImage(WHITE_IMAGE), screenX, screenY - shadowOffset, this);
		} else if (colour == Colour.BLACK) {
			if (withShadow) {
				g.drawImage(getImage(SHADOW_IMAGE), screenX, screenY, this);
			}
			g.drawImage(getImage(BLACK_IMAGE), screenX, screenY - shadowOffset, this);
		}
	}

	private void repaintSquare(int x, int y) {
		repaint(boardToScreen(x), boardToScreen(y), squareWidth, squareWidth);
	}
	
	private void repaintSquare(int position) {
		try {
			int[] coordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(position);
			repaintSquare(coordinates[0], coordinates[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
	}
	
	public boolean undo() {
		if (state == STATE_USER_CAPTURING) {
			reset();
			return true;
		} else {
			return false;
		}
	}
		
	public void startAnimating() {
		super.startAnimating();
		NineMensMorrisMove lastMove = (NineMensMorrisMove) game.getBoard().getLastMovePlayed();
		int colour = ((NineMensMorrisPlayer) game.getPreviousBoard().getCurrentPlayer()).getColour();
		if (((NineMensMorrisPlayer) game.getSettings().getPlayer()).getColour() != colour && !lastMove.isPlacement()) {
			changeState(STATE_ANIMATING_SHIFT);
			int[] fromCoordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(lastMove.getFromPosition());
			int[] toCoordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(lastMove.getToPosition());
			int deltaX = toCoordinates[0] - fromCoordinates[0];
			int deltaY = toCoordinates[1] - fromCoordinates[1];
			double absDeltaX = Math.abs(deltaX);
			double absDeltaY = Math.abs(deltaY);
			if (absDeltaX > absDeltaY) {
				if (deltaX >= 0) {
					dx = 1.0;					
				} else {
					dx = -1.0;
				}
				if (deltaY >= 0) {
					dy = absDeltaY / absDeltaX;
				} else {
					dy = -(absDeltaY / absDeltaX);
				}
			} else {
				if (deltaY >= 0) {
					dy = 1.0;					
				} else {
					dy = -1.0;
				}
				if (deltaX >= 0) {
					dx = absDeltaX / absDeltaY;
				} else {
					dx = -(absDeltaX / absDeltaY);
				}
			}
			animationFrame = 0;
		} else if (lastMove.hasCapture()) {
			changeState(STATE_ANIMATING_CAPTURE);
			animationFrame = 0;
		}
	}
	
	@Override
	public void endAnimating() {
		if (!((NineMensMorrisMainWindow) parentWidget).isUndoing()) {
			reset();
		}
		super.endAnimating();
	}
	
	public void abortAnimating() {
		super.abortAnimating();
	}
	
	/**
	 * Run one cycle of animation. Updates the frames counts stored in the flips lists in {@link #game} appropriately, where:
	 * 	<ul>
	 * 		<li><code>frame = 0</code> - piece has not started flipping, so original colour piece should be shown;</li>
	 * 		<li><code>frame = 1 to NUM_ANIMATED_PIECES</code> - piece is on corresponding animation frame;</li>
	 * 		<li><code>frame &gt; NUM_ANIMATED_PIECES</code> - piece has finished flipping, so new colour piece should be shown.</li>
	 *	</ul>
	 * @return
	 * 	<ul>
	 * 		<li>true if there are still more animation cycles pending;</li>
	 * 		<li>false otherwise.</li>
	 * 	</ul>
	 */
	public boolean animate() {
		NineMensMorrisMove lastMove = (NineMensMorrisMove) game.getBoard().getLastMovePlayed();
		if (state == STATE_ANIMATING_SHIFT) {
			int[] fromCoordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(lastMove.getFromPosition());
			int[] toCoordinates = NineMensMorrisPosition.lookupCoordinatesFromPosition(lastMove.getToPosition());
			int oldX = (int) (boardToScreen(fromCoordinates[0]) + animationFrame * dx);
			int oldY = (int) (boardToScreen(fromCoordinates[1]) + animationFrame * dy);
			animationFrame += ANIMATION_SPEED;
			int x = (int) (boardToScreen(fromCoordinates[0]) + animationFrame * dx);
			int y = (int) (boardToScreen(fromCoordinates[1]) + animationFrame * dy);
			boolean moreToCome = true;
			if ((toCoordinates[0] > fromCoordinates[0] && x > boardToScreen(toCoordinates[0]))
					|| (toCoordinates[0] < fromCoordinates[0] && x < boardToScreen(toCoordinates[0]))
					|| (toCoordinates[1] > fromCoordinates[1] && y > boardToScreen(toCoordinates[1]))
					|| (toCoordinates[1] < fromCoordinates[1] && y < boardToScreen(toCoordinates[1]))) {
				if (lastMove.hasCapture()) {
					changeState(STATE_ANIMATING_CAPTURE);
					animationFrame = 0;
				} else {
					moreToCome = false;
				}
			}
			repaint(oldX, oldY, squareWidth, squareWidth);
			repaint(x, y, squareWidth, squareWidth);
			return moreToCome;
		} else if (state == STATE_ANIMATING_CAPTURE) {
			int oldState = (animationFrame / ANIMATION_SPEED) % 2;
			animationFrame++;
			int newState = (animationFrame / ANIMATION_SPEED) % 2;
			if (oldState != newState) {
				repaintSquare(lastMove.getCapturePosition());
			}
			if (animationFrame < ANIMATION_SPEED * CAPTURE_ANIMATION_LENGTH) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "NineMensMorrisBoardWidget:" + super.toString();
	}
	
	/** Abstract method called when move is selected. Concrete subclasses must define this method. */
	protected abstract void selectAction(MouseEvent e);
}
