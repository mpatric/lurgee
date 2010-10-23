/*
 * @(#)NineMensMorrisPlayerTest.java		2008/01/21
 *
 * Part of the ninemensmorris common module that uses the strategy game framework.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.ninemensmorris;

import junit.framework.TestCase;

/**
 * Unit tests for {@link NineMensMorrisPlayer} class.
 * @author mpatric
 */
public class NineMensMorrisPlayerTest extends TestCase {

	private NineMensMorrisPlayer blackPlayer = NineMensMorrisPlayer.getInstance(Colour.BLACK);
	private NineMensMorrisPlayer whitePlayer = NineMensMorrisPlayer.getInstance(Colour.WHITE);
	
	public void testShouldGetValidPlayers() throws Exception {
		assertEquals(Colour.BLACK, blackPlayer.getColour());
		assertEquals(Colour.WHITE, whitePlayer.getColour());
	}
	
	public void testShouldGetNullForInvalidPlayers() throws Exception {
		assertNull(NineMensMorrisPlayer.getInstance(Colour.NONE));
		assertNull(NineMensMorrisPlayer.getInstance(Colour.ANY));
		assertNull(NineMensMorrisPlayer.getInstance(4));
	}
	
	public void testStaticInstancesShouldHaveUniqueNamesAndSymbols() throws Exception {
		assertFalse(blackPlayer.getName().equals(whitePlayer.getName()));
		assertFalse(blackPlayer.getSymbol() == whitePlayer.getSymbol());
	}
}
