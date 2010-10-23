/*
 * @(#)ReversiBoardWidget.java		2006/02/05
 *
 * Part of the reversi applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.reversi.applet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.lurgee.common.applet.AbstractBoardWidget;
import net.lurgee.common.applet.AbstractGame;
import net.lurgee.common.awt.Widget;
import net.lurgee.reversi.Colour;
import net.lurgee.reversi.ReversiBoard;
import net.lurgee.reversi.ReversiMove;
import net.lurgee.reversi.ReversiPlayer;
import net.lurgee.reversi.ReversiPosition;
import net.lurgee.sgf.Position;

/**
 * Abstract class for a widget representing the game board. Supports painting and interaction with the user. The abstract selectAction
 * method needs to be defined by a concrete subclass (anonymous or otherwise).
 * @author mpatric
 */
public abstract class ReversiBoardWidget extends AbstractBoardWidget {

	private static final long serialVersionUID = -7952394095101503624L;
	private static final Integer SQUARE_IMAGE = new Integer(1);
	private static final Integer HI_IMAGE = new Integer(2);
	private static final Integer SPOT_IMAGE = new Integer(3);
	private static final Integer BLACK_IMAGE = new Integer(4);
	private static final Integer WHITE_IMAGE = new Integer(5);
	private static final Integer HIBLACK_IMAGE = new Integer(6);
	private static final Integer HIWHITE_IMAGE = new Integer(7);
	private static final Integer LAST_PLAY_IMAGE = new Integer(8);
	private static final Integer[] ANIMATED_PIECE_IMAGES = new Integer[AppletConsts.NUM_ANIMATED_PIECES];
	private static final int ANIMATED_PIECES_START = 9;
	
	protected int overX = -1;
	protected int overY = -1;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param game A reversi game container.
	 * @throws IOException If any of the images cannot be loaded.
	 */
	public ReversiBoardWidget(Widget parentWidget, int x, int y, int width, int height, AbstractGame game) throws IOException {
		super(parentWidget, x, y, width, height, game);
		loadImages();
	}

	private void loadImages() throws IOException {
		loadImage(SQUARE_IMAGE, AppletConsts.SQUARE_NORMAL_IMAGE_FILENAME);
		loadImage(HI_IMAGE, AppletConsts.SQUARE_HI_IMAGE_FILENAME);
		loadImage(SPOT_IMAGE, AppletConsts.SPOT_IMAGE_FILENAME);
		loadImage(BLACK_IMAGE, AppletConsts.PIECE_BLACK_IMAGE_FILENAME);
		loadImage(WHITE_IMAGE, AppletConsts.PIECE_WHITE_IMAGE_FILENAME);
		loadImage(HIBLACK_IMAGE, AppletConsts.PIECE_BLACK_HI_IMAGE_FILENAME);
		loadImage(HIWHITE_IMAGE, AppletConsts.PIECE_WHITE_HI_IMAGE_FILENAME);
		loadImage(LAST_PLAY_IMAGE, AppletConsts.LAST_PLAY_INDICATOR);
		for (int j = 0; j < AppletConsts.NUM_ANIMATED_PIECES; j++) {
			String filename = AppletConsts.PIECE_FLIP_IMAGE_PREFIX + (j + 1) + AppletConsts.PIECE_FLIP_IMAGE_SUFFIX;
			ANIMATED_PIECE_IMAGES[j] = new Integer(ANIMATED_PIECES_START + j);
			loadImage(ANIMATED_PIECE_IMAGES[j], filename);
		}
	}

	@Override
	public void paint(Graphics g) {
		boolean busyAnimating = game.isBusy();
		int squareWidth = getWidth() / 8;
		ReversiBoard board = (ReversiBoard) game.getBoard();
		ReversiPlayer currentPlayer = ((ReversiPlayer) game.getBoard().getCurrentPlayer());
		for (int by = 1; by <= 8; by++) {
			for (int bx = 1; bx <= 8; bx++) {
				int gx = (bx - 1) * squareWidth;
				int gy = (by - 1) * squareWidth;
				if (g.hitClip(gx, gy, squareWidth, squareWidth)) {
					Image pieceImage = null;
					int squareColour = board.getSquare(bx, by);
					// draw square, overlaying highlights and the mouseover piece if necessary
					g.drawImage(getImage(SQUARE_IMAGE), gx, gy, this);
					if (squareColour == 0 && !busyAnimating && currentPlayer.equals(game.getSettings().getPlayer())) {
						if (board.isValidMove(bx, by)) {
							g.drawImage(getImage(HI_IMAGE), gx + 1, gy + 1, this);
							if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable() && bx == overX && by == overY) {
								if (currentPlayer.getColour() == Colour.BLACK) {
									pieceImage = getImage(HIBLACK_IMAGE);
								} else {
									pieceImage = getImage(HIWHITE_IMAGE);
								}
							}
						}
					}
					// select image for piece
					if (squareColour != 0) {
						if (busyAnimating) {
							Iterator<Position> flipIterator = game.getChanges().iterator();
							while (flipIterator.hasNext()) {
								ReversiPosition position = (ReversiPosition) flipIterator.next();
								if (position.getY() == by && position.getX() == bx) {
									if (position.getAnimationFrame() == 0) {
										// hasn't started flipping, so display opposite colour (as board now has new colour)
										if (squareColour == Colour.WHITE) {
											pieceImage = getImage(BLACK_IMAGE);
										} else if (squareColour == Colour.BLACK) {
											pieceImage = getImage(WHITE_IMAGE);
										}
									}
									else if (position.getAnimationFrame() < AppletConsts.NUM_ANIMATED_PIECES) {
										// piece is busy flipping
										int frame;
										if (squareColour == Colour.BLACK) {
											frame = position.getAnimationFrame();
										} else {
											frame = AppletConsts.NUM_ANIMATED_PIECES - 1 - position.getAnimationFrame();
										}
										pieceImage = getImage(ANIMATED_PIECE_IMAGES[frame]);
									}
									break;
								}
							}
						}
						if (pieceImage == null) {
							if (squareColour == Colour.BLACK) {
								pieceImage = getImage(BLACK_IMAGE);
							} else if (squareColour == Colour.WHITE) {
								pieceImage = getImage(WHITE_IMAGE);
							}
						}
					}
					if (pieceImage != null) {
						g.drawImage(pieceImage, gx + 1, gy + 1, this);
					}
				}
			}
		}
		// last move
		ReversiMove lastMove = (ReversiMove) game.getBoard().getLastMovePlayed();
		if (lastMove != null) {
			Image lastPlayImage = getImage(LAST_PLAY_IMAGE);
			int lastPlayImageWidth = lastPlayImage.getWidth(this);
			int lastPlayImageHeight = lastPlayImage.getHeight(this);
			int lx = ((lastMove.getPosition().getX() - 1) * squareWidth) + ((squareWidth - lastPlayImageWidth) / 2) ;
			int ly = ((lastMove.getPosition().getY() - 1) * squareWidth) + ((squareWidth - lastPlayImageHeight) / 2);
			if (g.hitClip(lx + 1, ly + 1, lastPlayImageWidth, lastPlayImageHeight)) {
				g.drawImage(lastPlayImage, lx + 1, ly + 1, this);
			}
		}
		// corner spots
		Image spotImage = getImage(SPOT_IMAGE);
		int r1 = (2 * squareWidth) - (spotImage.getWidth(this) / 2);
		int r2 = (6 * squareWidth) - (spotImage.getWidth(this) / 2);
		if (g.hitClip(r1, r1, squareWidth, squareWidth)) g.drawImage(spotImage, r1, r1, this);
		if (g.hitClip(r1, r2, squareWidth, squareWidth)) g.drawImage(spotImage, r1, r2, this);
		if (g.hitClip(r2, r1, squareWidth, squareWidth)) g.drawImage(spotImage, r2, r1, this);
		if (g.hitClip(r2, r2, squareWidth, squareWidth)) g.drawImage(spotImage, r2, r2, this);
	}
	
	private void repaintSquare(int bx, int by) {
		int squareWidth = getWidth() / 8;
		repaint((bx - 1) * squareWidth, (by - 1) * squareWidth, squareWidth, squareWidth);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			boolean busyAnimating = game.isBusy();
			if (overX >= 1 && overX <= ReversiBoard.X_DIMENSION && overY >= 1 && overY <= ReversiBoard.Y_DIMENSION) {
				if (!busyAnimating && overX != -1 && ((ReversiBoard) game.getBoard()).isValidMove(overX, overY)) {
					selectAction(e);
				}
			}
		} else {
			super.mouseClicked(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (overX != -1){
			if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
				repaintSquare(overX, overY);
			}
			overX = -1;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int squareWidth = getWidth() / 8;
		int bx = (e.getX() / squareWidth) + 1;
		int by = (e.getY() / squareWidth) + 1;
		if (bx != overX || by != overY) {
			if (bx >= 1 && bx <= 8 && by >= 1 && by <= 8) {
				if (overX != -1) {
					if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
						repaintSquare(overX, overY);
					}
				}
				overX = bx;
				overY = by;
				if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
					repaintSquare(overX, overY);
				}
			} else if (overX != -1) {
				if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
					repaintSquare(overX, overY);
				}
				overX = -1;
			}
		}
		super.mouseMoved(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	@Override
	public void startAnimating() {
		super.startAnimating();
		setAllAnimationFrames(0);
	}
	
	@Override
	public void abortAnimating() {
		super.abortAnimating();
		setAllAnimationFrames(AppletConsts.NUM_ANIMATED_PIECES + 1);
	}

	private void setAllAnimationFrames(int animationFrameValue) {
		List<Position> flips = game.getChanges();
		Iterator<Position> iterator = flips.iterator();
		while (iterator.hasNext()) {
			ReversiPosition position = (ReversiPosition) iterator.next();
			position.setAnimationFrame(animationFrameValue);
		}
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
		int count = 0;
		List<Position> flips = game.getChanges();
		Iterator<Position> iterator = flips.iterator();
		ReversiPosition position = null;
		int squareWidth = getWidth() / 8;
		int frame;
		int dirtyX1 = 9999;
		int dirtyY1 = 9999;
		int dirtyX2 = -1;
		int dirtyY2 = -1;
		while (iterator.hasNext()) {
			position = (ReversiPosition) iterator.next();
			frame = position.getAnimationFrame();
			if (frame <= AppletConsts.NUM_ANIMATED_PIECES) {
				int gx = (position.getX() - 1) * squareWidth;
				int gy = (position.getY() - 1) * squareWidth;
				if (gx < dirtyX1) dirtyX1 = gx;
				if (gy < dirtyY1) dirtyY1 = gy;
				if (gx + squareWidth > dirtyX2) dirtyX2 = gx + squareWidth;
				if (gy + squareWidth > dirtyY2) dirtyY2 = gy + squareWidth;
				position.setAnimationFrame(frame + 1);
				count++;
				if (frame == 3) {
					((ReversiMainWindow) parentWidget).playAudioClip(ReversiMainWindow.FLIP_AUDIO);
				}
				if (frame < 3) break;
			}
		}
		if (dirtyX2 >= 0) {
			repaint(dirtyX1, dirtyY1, dirtyX2 - dirtyX1, dirtyY2 - dirtyY1);
		}
		return (count > 0);
	}
	
	@Override
	public String toString() {
		return "ReversiBoardWidget:" + super.toString();
	}
	
	/** Abstract method called when move is selected. Concrete subclasses must define this method. */
	protected abstract void selectAction(MouseEvent e);
}
