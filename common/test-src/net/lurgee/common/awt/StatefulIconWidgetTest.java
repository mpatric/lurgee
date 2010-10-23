/*
 * @(#)StatefulIconWidgetTest.java		2007/06/09
 *
 * Part of the common awt classes.
 * Copyright (c) Michael Patricios, lurgee.net.
 *
 */

package net.lurgee.common.awt;

import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Unit tests for {@link net.lurgee.common.awt.StatefulIconWidget}.
 * @author mpatric
 */
public class StatefulIconWidgetTest extends TestCase {
	
	private static final String[] ICON_FILENAMES = {"one.png", "two.png", "three.png"};

	private Widget parentWidget;
	private StubStatefulIconWidget icon;
	private MouseEvent mouseEvent;
	
	@Override
	protected void setUp() throws Exception {
		parentWidget = new Widget(null, 0, 0, 100, 200) {
			private static final long serialVersionUID = 1L;
		};
		icon = new StubStatefulIconWidget(parentWidget, 0, 0, 10, 20, 0, ICON_FILENAMES, "back.png", "hi.png", "disabled.png");
		mouseEvent = new MouseEvent(icon, 0, 0, 0, 0, 0, 0, false);
	}
	
	public void testWithEachClickShouldCycleThroughIconsBackToFirst() throws Exception {
		parentWidget.setEnabled(true);
		icon.setEnabled(true);
		assertEquals(0, icon.getState());
		icon.mouseClicked(mouseEvent);
		assertEquals(1, icon.getState());
		icon.mouseClicked(mouseEvent);
		assertEquals(2, icon.getState());
		icon.mouseClicked(mouseEvent);
		assertEquals(0, icon.getState());
	}
	
	class StubStatefulIconWidget extends StatefulIconWidget {

		private static final long serialVersionUID = 1L;

		public StubStatefulIconWidget(Widget parentWidget, int x, int y, int width, int height, int initialState, String[] iconFilenames, String backFilename, String hiFilename, String disabledFilename) throws IOException {
			super(parentWidget, x, y, width, height, initialState, iconFilenames, backFilename, hiFilename, disabledFilename);
			setMouseOver(true);
		}
		
		protected InputStream getImageAsInputStream(String filename) {
			String s = "2038490283402834";
			return new ByteArrayInputStream(s.getBytes());
		}

		protected void selectAction(MouseEvent e) {
		}
	}
}
