/*
 * @(#)StatefulIconWidgetTest.java		2007/06/09
 *
 * Part of the common awt classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Unit tests for {@link net.lurgee.common.awt.Widget}.
 * @author mpatric
 */
public class WidgetTest extends TestCase {

	private static final Integer KEY = new Integer(1);

	public void testShouldAddLoadedImagesToImageMap() throws Exception {
		StubWidget widget = new StubWidget(null, 0, 0, 100, 200);
		widget.loadImage(KEY, "image1.png");
		assertNotNull(widget.getImage(KEY));
	}
	
	public void testShouldRemoveLoadedImageFromImageMap() throws Exception {
		StubWidget widget = new StubWidget(null, 0, 0, 100, 200);
		widget.loadImage(KEY, "image1.png");
		assertNotNull(widget.getImage(KEY));
		widget.unloadImage(KEY);
		assertNull(widget.getImage(KEY));
	}
	
	public void testShouldIgnoreGetAndLoadForNonExistentImage() throws Exception {
		StubWidget widget = new StubWidget(null, 0, 0, 100, 200);
		assertNull(widget.getImage(KEY));
		widget.unloadImage(KEY);
	}
	
	class StubWidget extends Widget {
		private static final long serialVersionUID = 1L;
		
		public StubWidget(Widget parentWidget, int x, int y, int width, int height) {
			super(parentWidget, x, y, width, height);
		}

		@Override
		protected InputStream getImageAsInputStream(String filename) {
			String s = "2038490283402834";
			return new ByteArrayInputStream(s.getBytes());
		}
	}
}
