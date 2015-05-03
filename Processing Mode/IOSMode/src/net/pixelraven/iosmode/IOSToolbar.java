package net.pixelraven.iosmode;

import java.awt.Image;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import processing.app.Base;
import processing.app.Editor;
import processing.app.EditorToolbar;

/**
 * 
 * The row of icons at the top of the PDE. Handles images, titles, and clicking
 * events.
 * Thanks kazimuth for the class layout!
 * 
 */

@SuppressWarnings("serial")
public class IOSToolbar extends EditorToolbar {

	static protected final int EMULATE = 0;
	static protected final int STOP = 1;

	static protected final int NEW = 2;
	static protected final int OPEN = 3;
	static protected final int SAVE = 4;
	static protected final int BUILD = 5;
	
	static public String getTitle(int index, boolean shift) {
		switch (index) {
		case EMULATE:
			return "Emulate";
		case STOP:
			return "Stop";
		case NEW:
			return "New";
		case OPEN:
			return "Open";
		case SAVE:
			return "Save";
		case BUILD:
			return "Build";
		}
	return null;
	}

	public IOSToolbar(Editor editor, Base base) {
		super(editor, base);
	}

	@Override
	public void handlePressed(MouseEvent e, int sel) {
		IOSEditor Ieditor = (IOSEditor) editor;

		switch (sel) {
		case EMULATE:
			Ieditor.handleRun();
			break;

		case STOP:
			Ieditor.handleStop();
			break;

		case OPEN:
			JPopupMenu popup = editor.getMode().getToolbarMenu().getPopupMenu(); //the 'open' dropdown
			popup.show(this, e.getX(), e.getY());
			break;

		case NEW:
			base.handleNew();
			break;

		case SAVE:
			Ieditor.handleSave(false);
			break;
		case BUILD:
			Ieditor.handleBuild();
			break;
		}
	}

	@Override
	public void init() { // open up the processing icons
		Image[][] images = loadImages();
		for (int i = 0; i < 6; i++) {
			addButton(getTitle(i, false), getTitle(i, true), images[i], i == NEW);
		}
	}
}