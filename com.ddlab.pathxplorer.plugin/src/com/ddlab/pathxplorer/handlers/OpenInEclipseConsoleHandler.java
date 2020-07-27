/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ddlab.pathxplorer.threads.EclipseConsoleThread;

/**
 * The Class OpenInEclipseConsoleHandler.
 *
 * @author Debadatta Mishra
 * @deprecated
 */
public class OpenInEclipseConsoleHandler extends AbstractHandler {

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

		Object selctionElement = ((IStructuredSelection) structuredSelection).getFirstElement();
		if (selctionElement instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) selctionElement;
			IResource resource = (IResource) adaptable.getAdapter(IResource.class);
			System.out.println("Location :::" + resource.getLocation().toString());
			String resourcePath = resource.getLocation().toString();
			System.out.println("Resource Path ::: " + resourcePath);
			resourcePath = (selctionElement instanceof IFile) ? resourcePath.substring(0, resourcePath.lastIndexOf("/"))
					: resourcePath;
			executeCmd(resourcePath);
		}

		return null;
	}

	/**
	 * Execute cmd.
	 *
	 * @param folderPath the folder path
	 */
	private void executeCmd(String folderPath) {
		try {
			@SuppressWarnings("unused")
			IViewPart consoleView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("org.eclipse.ui.console.ConsoleView");
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(new Shell(), "Error",
					"Unexpected Error while opening Eclipse Console.\nReport to debadatta.mishra@gmail.com");
		}
		Thread consoleTh = new EclipseConsoleThread(folderPath);
		consoleTh.start();
	}
}
