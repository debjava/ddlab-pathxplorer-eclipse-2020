/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.threads;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.ddlab.pathxplorer.util.PluginLogger;
import static com.ddlab.pathxplorer.util.CommonConstants.*;

/**
 * This class is used to execute a command in a separate thread.
 * 
 * @author <a href="mailto:debadatta.mishra@gmail.com"> Debadatta Mishra (PIKU)
 * @since 2013
 * 
 */
public class CommandThread extends Thread {

	/** String for the complete command. */
	private String command;

	/** Directory path where the command gets executed. */
	private String folderPath;

	/**
	 * Default constructor.
	 *
	 * @param command    the command
	 * @param folderPath the folder path
	 */
	public CommandThread(String command, String folderPath) {
		this.command = command;
		this.folderPath = folderPath;
	}

	/**
	 * Run.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			/*
			 * Execution of the command using Java Runtime and Process
			 */
			@SuppressWarnings("unused")
			Process process = Runtime.getRuntime().exec(command, null, new File(folderPath));
		} catch (Exception e) {
			PluginLogger.error("UnExpected Excepton while executing command ...\n", e);
			MessageDialog.openError(new Shell(), ERROR, GEN_ERR_MSG);
		}
	}
}
