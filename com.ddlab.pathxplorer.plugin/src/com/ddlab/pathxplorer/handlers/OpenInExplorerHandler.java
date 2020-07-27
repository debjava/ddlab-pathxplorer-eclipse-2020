/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.handlers;

import static com.ddlab.pathxplorer.util.CommonConstants.ERROR;
import static com.ddlab.pathxplorer.util.CommonConstants.RPT_ERR_TXT;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ddlab.pathxplorer.threads.FolderOpenThread;
import com.ddlab.pathxplorer.util.CommonUtil;
import com.ddlab.pathxplorer.util.PluginLogger;

/**
 * The Class OpenInExplorerHandler.
 * 
 * @author Debadatta Mishra (PIKU)
 */
public class OpenInExplorerHandler extends AbstractHandler {

	/**
	 * Execute.
	 *
	 * @param event the event
	 * @return the object
	 * @throws ExecutionException the execution exception
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structuredSelection = (IStructuredSelection) service.getSelection();

		try {
			List<String> pathsList = CommonUtil.getDirectoryPaths(structuredSelection);
			for (String path : pathsList) {
				Thread exOpenThread = new FolderOpenThread(path);
				exOpenThread.start();
			}
		} catch (NullPointerException npe) {
			PluginLogger.error(npe);
			MessageDialog.openError(new Shell(), ERROR, npe.getMessage() + " " + RPT_ERR_TXT);
		} catch (Exception e) {
			PluginLogger.error(e);
			MessageDialog.openError(new Shell(), ERROR, e.getMessage() + " " + RPT_ERR_TXT);
		}
		return null;
	}

}
