package net.pixelraven.iosmode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import processing.app.Base;
import processing.app.Editor;
import processing.app.EditorState;
import processing.app.Library;
import processing.app.Mode;
import processing.app.Sketch;
import processing.app.SketchCode;
import processing.app.SketchException;
import processing.app.Toolkit;
import processing.mode.java.JavaMode;

/**
 *
 * The main class for the IOS mode.
 * Thanks kazimuth for the class layout!
 *
 */
public class IOSMode extends JavaMode {
    public IOSMode(Base base, File folder) {
        super(base, folder);
    }

    /**
     *
     * Create the editor with the IOSEditor class
     *
     */
    public Editor createEditor(Base base, String path, EditorState state) {
		return new IOSEditor(base, path, state, this);
	}
    
    /**
     * 
     * Return the 'pretty' name for this mode.
     * 
     */
    public String getTitle() {
        return "IOS Mode";
    }

    /*
     * 
	 * Something I don't quite understand properly
	 * 
	 */
	public static String getModeFolder() {
		return Base.getSketchbookModesFolder() + File.separator + "IOSMode" + File.separator;
	}

	/**
	 * 
	 * Returns the default extension for this editor setup. NOTE: no '.' at the
	 * beginning, that causes problems!
	 * 
	 */
	public String getDefaultExtension() {
		return "pde";
	}

	/**
	 * 
	 * Returns a String[] array of proper extensions. This happens to only be .pde
	 * 
	 */
	public String[] getExtensions() {
		return new String[] {"pde"};
	}
	
	/**
	 * 
	 * Get array of file/directory names that needn't be copied during "Save
	 * As".
	 * 
	 */
	public String[] getIgnorable() {
		return new String[] {};
	}
	
    /**
     * Retrieve the ClassLoader for JavaMode. This is used by Compiler to load
     * ECJ classes. Thanks to Ben Fry.
     *
     * @return the class loader from java mode
     */
    @Override
    public ClassLoader getClassLoader() {
        for (Mode m : base.getModeList()) {
            if (m.getClass() == JavaMode.class) {
                JavaMode jMode = (JavaMode) m;
                return jMode.getClassLoader();
            }
        }
        return null;  // aww shiiiit
    }
}
