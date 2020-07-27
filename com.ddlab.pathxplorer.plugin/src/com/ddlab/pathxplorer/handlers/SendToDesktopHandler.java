/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ddlab.pathxplorer.Activator;
import com.ddlab.pathxplorer.threads.FileCopyJobRunner;
import com.ddlab.pathxplorer.util.PluginConstants;
import com.ddlab.pathxplorer.util.PluginLogger;
import static com.ddlab.pathxplorer.util.CommonConstants.*;

/**
 * The Class SendToDesktopHandler.
 * 
 * @author Debadatta Mishra (PIKU)
 */
public class SendToDesktopHandler extends AbstractHandler {

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

		String send2PathVal = Activator.getDefault().getPreferenceStore().getString(PluginConstants.SEND_2_PATH);
		send2PathVal = (send2PathVal == null) ? PluginConstants.DEFAULT_DESKTOP_PATH : send2PathVal;
		try {
			IStructuredSelection iSelection = (IStructuredSelection) structuredSelection;
			Thread th1 = new Thread(new FileCopyJobRunner(iSelection));
			th1.start();
		} catch (Exception e) {
			e.printStackTrace();
			PluginLogger.error(e);
			MessageDialog.openError(new Shell(), ERROR, GEN_ERR_MSG);
		}

		return null;
	}
}
