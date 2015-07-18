package net.pixelraven.iosmode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import processing.app.Base;
import processing.app.Editor;
import processing.app.EditorState;
import processing.app.EditorToolbar;
import processing.app.Formatter;
import processing.app.Mode;
import processing.app.Preferences;
import processing.app.Toolkit;
import processing.mode.java.JavaEditor;
import processing.mode.java.JavaToolbar;

/**
 * 
 * The main editor class. 
 * 
 */
@SuppressWarnings("serial")
public class IOSEditor extends Editor {

	IOSMode IMode;
	//handles syntax highlighting/indents
	IOSKeyListener listener;

	private IOSRunner runner;

	protected IOSEditor(final Base base, String path, EditorState state, final Mode mode) {
		super(base, path, state, mode);

		runner = new IOSRunner(this);

		listener = new IOSKeyListener(this, textarea); //black magic
		IMode = (IOSMode) mode; //convenience
	}

	public String getCommentPrefix() {
		return "#";
	}

	@Override
	public void internalCloseRunner() {
		try {
			runner.internalClose();
		} catch (Exception e) {
			statusError(e);
		}
	}

	/**
	 * Build menus.
	 */
	@Override
	//Build the 'File' menu
	public JMenu buildFileMenu() {
		//Okay, this is kinda weird
		String appTitle = IOSToolbar.getTitle(IOSToolbar.BUILD, false);  //get export string

		JMenuItem exportApplication = Toolkit.newJMenuItem(appTitle, 'E'); //set it up

		exportApplication.addActionListener(new ActionListener() { //yadda yadda
			public void actionPerformed(ActionEvent e) {
				handleBuild();
			}
		});
		return buildFileMenu(new JMenuItem[] { exportApplication }); //and then call the SUPERCLASS method
	}

	@Override
	//Build the 'Help' menu
	public JMenu buildHelpMenu() { 
		//TODO implement
		JMenu menu = new JMenu("Help");
		JMenuItem item = new JMenuItem("Help is for the weak");
		item.setEnabled(false);
		menu.add(item);
		return menu;
	}

	@Override
	//Build the 'Sketch' menu
	public JMenu buildSketchMenu() {
		JMenuItem runItem = Toolkit.newJMenuItem(IOSToolbar.getTitle(IOSToolbar.EMULATE, false), 'R');
		runItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleRun();
			}
		});

		JMenuItem buildItem = Toolkit.newJMenuItem(IOSToolbar.getTitle(IOSToolbar.BUILD, false), 'B');
		runItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBuild();
			}
		});

		JMenuItem stopItem = new JMenuItem(IOSToolbar.getTitle(IOSToolbar.STOP, false));
		stopItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleStop();
			}
		});

		return buildSketchMenu(new JMenuItem[] {runItem, buildItem, stopItem});
	}

	@Override
	public Formatter createFormatter() {
		return new IOSFormat();
	}

	@Override
	public EditorToolbar createToolbar() {
		return new IOSToolbar(this, base);
	}

	/**
	 * Handlers
	 */
	public void handleBuild() {
		Base.showMessage("Sorry", "You can't do that yet."); //TODO implement
	}

	//Note that I'm doing the build here instead of in IOSMode
	public void handleRun() {
		toolbar.activate(IOSToolbar.EMULATE);
		new Thread(new Runnable() {
			public void run() {
				//create build
				IOSBuild build = new IOSBuild(sketch, IMode);
				try {
					//run build
					build.build();	
					runner.launch(build, false);				
				} 
				//Bad news
				catch (Exception e) {
					statusError("FUUUUUUUUUUUUU..... Something went wrong!");
					System.err.println("Crashed on launch of emulator! Error message: \n" + e.getMessage());
				}
			}
		}).start();
	}

	public void handlePresent() {
		Base.showMessage("My life story", "Something went wrong");
	}
	
	public void handleStop() {
		toolbar.activate(IOSToolbar.STOP);
		//internalCloseRunner();
		toolbar.deactivate(IOSToolbar.STOP);
		toolbar.deactivate(IOSToolbar.EMULATE);
		//toFront();
	}

	public void handleSave() {
		toolbar.activate(IOSToolbar.SAVE);
		super.handleSave(true);
		toolbar.deactivate(IOSToolbar.SAVE);
	}

	public boolean handleSaveAs() {
		toolbar.activate(IOSToolbar.SAVE);
		boolean result = super.handleSaveAs();
		toolbar.deactivate(IOSToolbar.SAVE);
		return result;
	}

	@Override
	public void handleImportLibrary(String arg0) {
		Base.showMessage("Sorry", "You can't do that yet."); //TODO implement
	}

	@Override
	public void statusError(String what) { //sketch died for some reason
		super.statusError(what);
		toolbar.deactivate(IOSToolbar.EMULATE);
	}
	
	@Override
	public void deactivateRun(){
		toolbar.deactivate(IOSToolbar.EMULATE);
	}
}