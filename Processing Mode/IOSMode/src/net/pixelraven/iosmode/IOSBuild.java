package net.pixelraven.iosmode;

import java.io.File;
import java.io.PrintWriter;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import processing.app.Base;
import processing.app.Library;
import processing.app.Sketch;
import processing.app.SketchCode;

/**
 * 
 * Class to handle the building of IOS files
 * 
 */

public class IOSBuild {
	Sketch sketch;
	String resultProgram;

	IOSMode mode;

	File binFolder;
	File outFile;

	String classPath, IOSLibs, javaLibs;

	boolean usesOpenGL;

	//build tracking, not that this actually does anything
	private int buildnumber;
	private static int buildstotal = 0;

	public IOSBuild(Sketch sketch, IOSMode mode) {
		this.sketch = sketch;
		this.mode = mode;

		buildnumber = buildstotal;
		buildstotal++;
	}

	/*
	 * 
	 * Get the sketch code and do shit to it.
	 * 
	 */
	public void build() throws Exception {
		//to hold all the code
		StringBuilder program = new StringBuilder();
		SketchCode[] parts = sketch.getCode();

		//concatenate code strings
		for (int i = 0; i < parts.length; i++) {
			program.append(parts[i].getProgram());
			program.append("\n");
		}

		//get final result
		resultProgram = program.toString();
		System.out.println("Your program: " + resultProgram);
		//create output folder
		binFolder = sketch.makeTempFolder();

		//create & write to output file
		outFile = new File(binFolder.getAbsolutePath() + File.separator + sketch.getName().toLowerCase() + ".test");
		outFile.createNewFile();

		PrintWriter writer = new PrintWriter(outFile);
		writer.print(resultProgram);
		writer.close();
	}

	/*
	 * Access to extracted library information
	 */
	public String getJavaLibraries() {
		return javaLibs;
	}

	public boolean hasJavaLibraries() {
		return javaLibs != null;
	}

	public String getIOSLibraries() {
		return IOSLibs;
	}

	public boolean hasIOSLibraries() {
		return IOSLibs != null;
	}

	/*
	 * The output code string, properly formatted and whatnot.
	 */
	public String getResultString() {
		return resultProgram;
	}

	/*
	 * The output file path
	 */
	public String getResultFile() {
		return outFile.getAbsolutePath();
	}

	/*
	 * This may eventually tie in to some global thing... or not
	 */
	public int getBuildNumber() {
		return buildnumber;
	}

	/*
	 * Java classes used to run the build.
	 */
	public String getClassPath() {
		return classPath; //computed during preprocessing
	}

	/*
	 * ...
	 */
	public String getName() {
		return sketch.getName();
	}

	/*
	 * I'm not sure what the java library path actually is, so, leaving this
	 * blank.
	 */
	public String getJavaLibraryPath() {
		return "";
	}
}