/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.threads;

import static com.ddlab.pathxplorer.util.CommonConstants.*;
import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.ddlab.pathxplorer.util.PluginLogger;

/**
 * This class is used to open the selected folder or file in the Windows
 * Explorer in a separate thread.
 * 
 * @author <a href="mailto:debadatta.mishra@gmail.com"> Debadatta Mishra (PIKU)
 * @since 2013
 * 
 */
public class FolderOpenThread extends Thread {

	/** Folder path to open in Windows Explorer. */
	private String folderPath;

	/**
	 * Default Constructor.
	 *
	 * @param folderPath the folder path
	 */
	public FolderOpenThread(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * Method to open the Windows Explorer by executing the explorer command for the
	 * selected path.
	 *
	 * @param folderPath the folder path
	 */
	public void openWindowsExplorer(String folderPath) {
		try {
			if (folderPath == null)
				throw new NullPointerException(FILE_NULL_ERR_MSG);
			Runtime.getRuntime().exec(EXPLR_CMD + new File(folderPath).getAbsolutePath());
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			PluginLogger.error("UnExpected Excepton while executing folder opener ...\n", npe);
			MessageDialog.openError(new Shell(), ERROR, EXPLR_ERR_MSG);
		} catch (Exception e) {
			PluginLogger.error("UnExpected Excepton while executing folder opener ...\n", e);
			MessageDialog.openError(new Shell(), ERROR, EXPLR_SHOW_ERR_MSG);
		}
	}

	/**
	 * Run.
	 */
	@Override
	public void run() {
		openWindowsExplorer(folderPath);
	}

}
