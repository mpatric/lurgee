/*
 * @(#)Connect4BoardWidget.java		2007/06/11
 *
 * Part of the connect4 applet that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.connect4.applet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;

import net.lurgee.common.applet.AbstractBoardWidget;
import net.lurgee.common.applet.AbstractGame;
import net.lurgee.common.awt.Widget;
import net.lurgee.connect4.Colour;
import net.lurgee.connect4.Connect4Board;
import net.lurgee.connect4.Connect4Move;
import net.lurgee.connect4.Connect4Player;
import net.lurgee.connect4.Connect4Position;

/**
 * Abstract class for a widget representing the game board. Supports painting and interaction with the user. The abstract selectAction
 * method needs to be defined by a concrete subclass (anonymous or otherwise).
 * @author mpatric
 */
public abstract class Connect4BoardWidget extends AbstractBoardWidget {

	private static final long serialVersionUID = -3460149902471338959L;
	private static final Integer HIYELLOW_IMAGE = new Integer(1);
	private static final Integer HIRED_IMAGE = new Integer(2);
	private static final Integer YELLOW_IMAGE = new Integer(3);
	private static final Integer RED_IMAGE = new Integer(4);
	private static final Integer FRONTPLANE_IMAGE = new Integer(5);
	private static final Integer BACKPLANE_IMAGE = new Integer(6);
	private static final Integer SKYTILE_IMAGE = new Integer(7);
	private static final Integer HILIGHT_IMAGE = new Integer(8);
	private static final float INITIAL_ANIMATION_SPEED = 0.6f;
	private static final float ANIMATION_ACCELERATION = 0.4f;
	
	protected int overX = -1;
	private int animationFrame = 0;
	private float animationSpeed;
	private boolean playedDropSound;

	/**
	 * Constructor.
	 * @param parentWidget The parent widget which contains this widget if there is one.
	 * @param x The x-position of the left side of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param y The y-position of the top of this widget relative to the parent if there is one or the applet if there isn't.
	 * @param width The width in pixels of this widget.
	 * @param height The height in pixels of this widget.
	 * @param game A connect4 game container.
	 * @throws IOException If any of the images cannot be loaded.
	 */
	public Connect4BoardWidget(Widget parentWidget, int x, int y, int width, int height, AbstractGame game) throws IOException {
		super(parentWidget, x, y, width, height, game);
		loadImages();
	}

	private void loadImages() throws IOException {
		loadImage(BACKPLANE_IMAGE, AppletConsts.BACKPLANE_IMAGE_FILENAME);
		loadImage(FRONTPLANE_IMAGE, AppletConsts.FRONTPLANE_IMAGE_FILENAME);
		loadImage(RED_IMAGE, AppletConsts.PIECE_RED_IMAGE_FILENAME);
		loadImage(YELLOW_IMAGE, AppletConsts.PIECE_YELLOW_IMAGE_FILENAME);
		loadImage(HIRED_IMAGE, AppletConsts.PIECE_RED_HI_IMAGE_FILENAME);
		loadImage(HIYELLOW_IMAGE, AppletConsts.PIECE_YELLOW_HI_IMAGE_FILENAME);
		loadImage(SKYTILE_IMAGE, AppletConsts.SKYTILE_IMAGE_FILENAME);
		loadImage(HILIGHT_IMAGE, AppletConsts.HILIGHT_IMAGE_FILENAME);
	}
	
	@Override
	public void paint(Graphics g) {
		boolean busyAnimating = game.isBusy();
		Connect4Board board = (Connect4Board) game.getBoard();
		Connect4Player currentPlayer = ((Connect4Player) game.getBoard().getCurrentPlayer());
		int lastX = -1;
		int lastY = -1;
		Connect4Move lastMove = ((Connect4Move) game.getBoard().getLastMovePlayed());
		if (lastMove != null) {
			lastX = lastMove.getX();
			lastY = ((Connect4Board) game.getBoard()).getTop(lastX);
		}
		// cursor piece
		if (!busyAnimating && currentPlayer.equals(game.getSettings().getPlayer()) && isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			if (overX >= 1 && overX <= Connect4Board.X_DIMENSION) { 
				if (((Connect4Board) game.getBoard()).isValidMove(overX)) {
					Image pieceImage;
					if (currentPlayer.getColour() == Colour.RED) {
						pieceImage = getImage(HIRED_IMAGE);
					} else {
						pieceImage = getImage(HIYELLOW_IMAGE);
					}
					int gx = (overX - 1) * AppletConsts.BLOCK_SIZE;
					g.drawImage(pieceImage, gx + 1, 0, this);
				}
			}
		}
		// sky and backplane
		Image skytileImage = getImage(SKYTILE_IMAGE);
		Image backplaneImage = getImage(BACKPLANE_IMAGE);
		for (int by = 1; by <= Connect4Board.Y_DIMENSION; by++) {
			for (int bx = 1; bx <= Connect4Board.X_DIMENSION; bx++) {
				int gx = (bx - 1) * AppletConsts.BLOCK_SIZE;
				int gy = getHeight() - (by * AppletConsts.BLOCK_SIZE);
				if (g.hitClip(gx, gy, AppletConsts.BLOCK_SIZE, AppletConsts.BLOCK_SIZE)) {
					g.drawImage(skytileImage, gx, gy, this);
					g.drawImage(backplaneImage, gx, gy, this);
				}
			}
		}
		Image redImage = getImage(RED_IMAGE);
		Image yellowImage = getImage(YELLOW_IMAGE);
		// animated piece
		if (busyAnimating) {
			int gx = (lastX - 1) * AppletConsts.BLOCK_SIZE;
			int pieceColour = ((Connect4Player) game.getPreviousBoard().getCurrentPlayer()).getColour(); 
			if (pieceColour == Colour.RED) {
				g.drawImage(redImage, gx, animationFrame, this);						
			} else if (pieceColour == Colour.YELLOW) {
				g.drawImage(yellowImage, gx, animationFrame, this);
			}
		}
		// pieces
		for (int by = 1; by <= Connect4Board.Y_DIMENSION; by++) {
			for (int bx = 1; bx <= Connect4Board.X_DIMENSION; bx++) {
				int gx = (bx - 1) * AppletConsts.BLOCK_SIZE;
				int gy = getHeight() - (by * AppletConsts.BLOCK_SIZE);
				if (g.hitClip(gx, gy, AppletConsts.BLOCK_SIZE, AppletConsts.BLOCK_SIZE)) {
					if (!busyAnimating || by != lastY || bx != lastX) {
						int pieceColour = board.getSquare(bx, by);
						if (pieceColour == Colour.RED) {
							g.drawImage(redImage, gx, gy, this);
						} else if (pieceColour == Colour.YELLOW) {
							g.drawImage(yellowImage, gx, gy, this);
						}
					}
				}
			}
		}
		// winning pieces hilights
		if (!game.isBusy() && game.getBoard().isGameOver() && game.getWinner() != null) {
			Connect4Position[] winningPieces = ((Connect4Board) game.getBoard()).getWinningGroup();
			Image hilightImage = getImage(HILIGHT_IMAGE);
			for (int i = 0; i < winningPieces.length; i++) {
				int gx = (winningPieces[i].getX() - 1) * AppletConsts.BLOCK_SIZE;
				int gy = getHeight() - (winningPieces[i].getY() * AppletConsts.BLOCK_SIZE);
				if (g.hitClip(gx, gy, AppletConsts.BLOCK_SIZE, AppletConsts.BLOCK_SIZE)) {
					g.drawImage(hilightImage, gx, gy, this);
				}
			}
		}
		// frontplane
		Image frontPlaneImage = getImage(FRONTPLANE_IMAGE);
		for (int by = 1; by <= Connect4Board.Y_DIMENSION; by++) {
			for (int bx = 1; bx <= Connect4Board.X_DIMENSION; bx++) {
				int gx = (bx - 1) * AppletConsts.BLOCK_SIZE;
				int gy = getHeight() - (by * AppletConsts.BLOCK_SIZE);
				if (g.hitClip(gx, gy, AppletConsts.BLOCK_SIZE, AppletConsts.BLOCK_SIZE)) {
					g.drawImage(frontPlaneImage, gx, gy, this);
				}
			}
		}
	}
	
	private void repaintCursorBlock(int x) {
		int gx = (x - 1) * AppletConsts.BLOCK_SIZE;
		repaint(gx, 0, AppletConsts.BLOCK_SIZE, AppletConsts.BLOCK_SIZE);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
			boolean busyAnimating = game.isBusy();
			if (!busyAnimating && overX != -1 && ((Connect4Board) game.getBoard()).isValidMove(overX)) {
				selectAction(e);
			}
		}
		else super.mouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (overX != -1){
			if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
				repaintCursorBlock(overX);
			}
			overX = -1;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int bx = (e.getX() / AppletConsts.BLOCK_SIZE) + 1;
		int by = (e.getY() / AppletConsts.BLOCK_SIZE) + 1;
		if (bx != overX) {
			if (bx >= 1 && bx <= 8 && by >= 1 && by <= 8) {
				if (overX != -1) {
					if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
						repaintCursorBlock(overX);
					}
				}
				overX = bx;
				if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
					repaintCursorBlock(overX);
				}
			} else if (overX != -1) {
				if (isEnabled() && parentWidget.isEnabled() && parentWidget.isFocusable()) {
					repaintCursorBlock(overX);
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
	
	public void startAnimating() {
		super.startAnimating();
		animationFrame = 0;
		animationSpeed = INITIAL_ANIMATION_SPEED;
		playedDropSound = false;
	}
	
	public void abortAnimating() {
		super.abortAnimating();
		animationFrame = 1000;
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
		animationFrame += animationSpeed;
		int x = ((Connect4Move) game.getBoard().getLastMovePlayed()).getX();
		int gx = (x - 1) * AppletConsts.BLOCK_SIZE;
		int gy = animationFrame;
		int endy = ((Connect4Board) game.getBoard()).getTop(x);
		int gendy = (Connect4Board.Y_DIMENSION - endy) * AppletConsts.BLOCK_SIZE;
		repaint(gx, (int)(gy - animationSpeed), AppletConsts.BLOCK_SIZE, (int)(AppletConsts.BLOCK_SIZE + animationSpeed));
		animationSpeed += ANIMATION_ACCELERATION;
		if (!playedDropSound) {
			// play drop sound just before piece reaches end or sound seems too delayed		
			if (animationFrame + (8 * animationSpeed) - AppletConsts.BLOCK_SIZE >= gendy) {
				playedDropSound = true;
				((Connect4MainWindow) parentWidget).playAudioClip(Connect4MainWindow.DROP_AUDIO);
			}
		}
		return (animationFrame + animationSpeed - AppletConsts.BLOCK_SIZE < gendy);
	}
	
	@Override
	public String toString() {
		return "Connect4BoardWidget:" + super.toString();
	}
	
	/** Abstract method called when move is selected. Concrete subclasses must define this method. */
	protected abstract void selectAction(MouseEvent e);
}
