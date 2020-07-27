/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.handlers;

import static com.ddlab.pathxplorer.util.CommonConstants.ERROR;
import static com.ddlab.pathxplorer.util.CommonConstants.GEN_ERR_MSG;

import java.util.Iterator;

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

import com.ddlab.pathxplorer.util.CommonUtil;
import com.ddlab.pathxplorer.util.PluginLogger;

/**
 * The Class WindowsStyleCopyHandler.
 * 
 * @author Debadatta Mishra (PIKU)
 */
public class WindowsStyleCopyHandler extends AbstractHandler {

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

		StringBuilder pathBuilder = new StringBuilder();
		try {
			Iterator<?> itr = ((IStructuredSelection) structuredSelection).iterator();
			while (itr.hasNext()) {
				Object selctionElement = itr.next();
				if (selctionElement instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) selctionElement;
					IResource resource = (IResource) adaptable.getAdapter(IResource.class);
					String resourcePath = resource.getLocation().toOSString();
					pathBuilder.append(resourcePath).append("\n");
				}
			}
			if (pathBuilder.toString().endsWith("\n"))
				pathBuilder = pathBuilder.delete(pathBuilder.lastIndexOf("\n"), pathBuilder.lastIndexOf("\n"));
			PluginLogger.info("All Path :::" + pathBuilder.toString());

			String filePathsStr = pathBuilder.toString().endsWith("\n")
					? pathBuilder.substring(0, pathBuilder.lastIndexOf("\n"))
					: pathBuilder.toString();
			String[] data = new String[] { filePathsStr };
			CommonUtil.copyTextToClipBoard(data);
		} catch (Exception e) {
			PluginLogger.error(e);
			MessageDialog.openError(new Shell(), ERROR, GEN_ERR_MSG);
		}
		return null;
	}

}
