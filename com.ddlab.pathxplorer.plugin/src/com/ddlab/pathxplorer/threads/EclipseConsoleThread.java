/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.threads;

import java.io.File;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;

import com.ddlab.pathxplorer.util.PluginLogger;

/**
 * This class is used to create a new thread to open the command prompt from the
 * eclipse console.
 * 
 * @author <a href="mailto:debadatta.mishra@gmail.com"> Debadatta Mishra (PIKU)
 * @since 2013
 * 
 */
@SuppressWarnings("restriction")
@Deprecated
public class EclipseConsoleThread extends Thread {

	/** Folder path of the executing command. */
	private String folderPath;

	/**
	 * Default constructor.
	 *
	 * @param folderPath the folder path
	 */
	public EclipseConsoleThread(String folderPath) {
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
			PluginLogger.info("Folder Path :::" + folderPath);
			// Obtain the Windows OS Directory
			String osDir = System.getenv("WINDIR");
			// Form the complete command to execute
			String exeCommand = osDir + File.separator + "System32" + File.separator + "cmd.exe";
			PluginLogger.info("ExeCommand :::" + exeCommand);// C:/Windows/
			// System32
			// /cmd.exe
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType programType = manager
					.getLaunchConfigurationType(IExternalToolConstants.ID_PROGRAM_LAUNCH_CONFIGURATION_TYPE);
			ILaunchConfiguration cfg = programType.newInstance(null, "Command in Eclipse");
			ILaunchConfigurationWorkingCopy wc = cfg.getWorkingCopy();
			wc.setAttribute(IExternalToolConstants.ATTR_LOCATION, exeCommand);
			wc.setAttribute(IExternalToolConstants.ATTR_WORKING_DIRECTORY, folderPath);
			cfg = wc.doSave();
			// Launch the command using Eclipse Launcher API
			cfg.launch(ILaunchManager.RUN_MODE, null, false, true);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the exception
			PluginLogger.error("UnExpected Excepton while executing command in Eclipse console ...\n", e);
			MessageDialog.openError(new Shell(), "Error", "Unexpected Error.\nReport to debadatta.mishra@gmail.com");
		}
	}
}
