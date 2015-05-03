package net.pixelraven.iosmode;

import processing.app.Editor;
import processing.app.Preferences;
import processing.app.Sketch;
import processing.app.syntax.JEditTextArea;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * A class to handle smart-indentation and things. Should really use a proper
 * Python parser, will have to find and/or make one.
 *
 * So, the Editor class uses a homebrewed text editing panel based on the
 * primordial goo that was to become JEdit (see jedit.org). That class is
 * hardcoded to have a PdeKeyListener managing things; to override stuff, I have
 * to override it.
 * 
 * I didn't write that FYI ^
 *
 */
public class IOSKeyListener extends processing.mode.java.PdeKeyListener {
	IOSEditor Ieditor;
	JEditTextArea Itextarea;

	//ctrl-alt on windows & linux, cmd-alt on os x
	private static int CTRL_ALT = ActionEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

  private static final String TAB = "  ";

	public IOSKeyListener(Editor editor, JEditTextArea textarea) {
		super(editor, textarea);

		Ieditor = (IOSEditor) editor;
		Itextarea = textarea;
	}

	/*
	 * Handles special stuff for Java brace indenting & outdenting, etc.
	 */
	@Override
	public boolean keyPressed(KeyEvent event) {
		char c = event.getKeyChar();
		int code = event.getKeyCode();

		Sketch sketch = Ieditor.getSketch();

		// things that change the content of the text area
		if ((code == KeyEvent.VK_BACK_SPACE) || (code == KeyEvent.VK_TAB) || (code == KeyEvent.VK_ENTER) || ((c >= 32) && (c < 128))) {
			sketch.setModified(true);
		}

		// ctrl-alt-[arrow] switches sketch tab
		if ((event.getModifiers() & CTRL_ALT) == CTRL_ALT) {
			if (code == KeyEvent.VK_LEFT) {
				sketch.handlePrevCode();
				return true;
			} else if (code == KeyEvent.VK_RIGHT) {
				sketch.handleNextCode();
				return true;
			}
		}

		// handle specific keypresses
		switch (c) {

		case 9: //tab; overriding with spaces
			Itextarea.setSelectedText(TAB);
			break;

		case 10: //return
		case 13: //also return
			String text = Itextarea.getText();	//text
			int cursor = Itextarea.getCaretPosition();	//location of element to be placed; may be out of bounds

			Itextarea.setSelectedText(getIndent(cursor, text));
			break;
		}

		return false;
	}

	private static Pattern findIndent = Pattern.compile("^((?: |\\t)*)");
	private static Pattern incIndent = Pattern.compile(":( |\\t)*(#.*)?$");

	String getIndent(int cursor, String text) {
		if (cursor <= 1) return "\n";

		int lineStart, lineEnd;
		int i;
		for (i = cursor - 1; i >= 0 && text.charAt(i) != '\n'; i--)
			;
		lineStart = i + 1;
		for (i = cursor - 1; i < text.length() && text.charAt(i) != '\n'; i++)
			;
		lineEnd = i;

		if (lineEnd <= lineStart) return "\n";

		String line = text.substring(lineStart, lineEnd);

		String indent;
		Matcher f = findIndent.matcher(line);

		if (f.find()) {
			indent = '\n' + f.group();

			if (incIndent.matcher(line).find()) {
				indent += TAB;
			}
		} else {
			indent = "\n";
		}

		return indent;
	}
}