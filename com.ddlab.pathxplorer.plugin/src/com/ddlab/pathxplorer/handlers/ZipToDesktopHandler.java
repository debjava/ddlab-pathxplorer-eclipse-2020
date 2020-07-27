/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.handlers;

import static com.ddlab.pathxplorer.util.CommonConstants.ERROR;
import static com.ddlab.pathxplorer.util.CommonConstants.GEN_ERR_MSG;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ddlab.pathxplorer.Activator;
import com.ddlab.pathxplorer.threads.ZipFilesJobRunner;
import com.ddlab.pathxplorer.util.PluginConstants;
import com.ddlab.pathxplorer.util.PluginLogger;

/**
 * The Class ZipToDesktopHandler.
 * 
 * @author Debadatta Mishra (PIKU)
 */
public class ZipToDesktopHandler extends AbstractHandler {

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
		String send2PathVal = Activator.getDefault().getPreferenceStore().getString(PluginConstants.SEND_2_PATH);
		send2PathVal = (send2PathVal == null) ? PluginConstants.DEFAULT_DESKTOP_PATH : send2PathVal;
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structuredSelection = (IStructuredSelection) service.getSelection();
		if (structuredSelection.size() == 1) {
			IStructuredSelection iSelection = (IStructuredSelection) structuredSelection;
			Object selctionElement = iSelection.getFirstElement();
			if (selctionElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selctionElement;
				IResource resource = (IResource) adaptable.getAdapter(IResource.class);
				if (resource.getType() == IResource.FOLDER || resource.getType() == IResource.PROJECT) {
					String folderName = resource.getName();
					performZip(structuredSelection, folderName);
				} else {
					performZip(structuredSelection, null);
				}
			}
		} else {
			performZip(structuredSelection, null);
		}
		return null;
	}

	/**
	 * Perform zip.
	 *
	 * @param structuredSelection the structured selection
	 * @param folderName          the folder name
	 */
	private void performZip(IStructuredSelection structuredSelection, String folderName) {
		try {
			IStructuredSelection iSelection = (IStructuredSelection) structuredSelection;
			Thread th1 = (folderName == null) ? new Thread(new ZipFilesJobRunner(iSelection))
					: new Thread(new ZipFilesJobRunner(iSelection, folderName));
			th1.start();
		} catch (Exception e) {
			PluginLogger.error(e);
			MessageDialog.openError(new Shell(), ERROR, GEN_ERR_MSG);
		}
	}
}
