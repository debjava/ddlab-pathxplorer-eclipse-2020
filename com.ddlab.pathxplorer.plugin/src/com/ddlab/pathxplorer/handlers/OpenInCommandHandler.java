/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.handlers;

import static com.ddlab.pathxplorer.util.CommonConstants.CMD_PROMPT_CMD;
import static com.ddlab.pathxplorer.util.CommonConstants.ERROR;
import static com.ddlab.pathxplorer.util.CommonConstants.GEN_ERR_MSG;
import static com.ddlab.pathxplorer.util.CommonConstants.PUSHD_CMD;
import static com.ddlab.pathxplorer.util.CommonConstants.ROOT_CMD;
import static com.ddlab.pathxplorer.util.CommonConstants.START_CMD;
import static com.ddlab.pathxplorer.util.CommonConstants.WIN_C_DRIVE;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ddlab.pathxplorer.threads.CommandThread;
import com.ddlab.pathxplorer.util.PluginLogger;

/**
 * The Class OpenInCommandHandler.
 * 
 * @author Debadatta Mishra (PIKU)
 */
public class OpenInCommandHandler extends AbstractHandler {

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

		Object trigger = event.getTrigger();
		if (trigger instanceof Event) {
			Widget widget = ((Event) trigger).widget;
			if (widget instanceof ToolItem) {
				openRootPrompt();
			} else {
				openCmdPromptForSelected(window);
			}
		}
		return null;
	}

	/**
	 * Open cmd prompt for selected.
	 *
	 * @param window the window
	 */
	public void openCmdPromptForSelected(IWorkbenchWindow window) {
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structuredSelection = (IStructuredSelection) service.getSelection();
		Iterator<?> itr = ((IStructuredSelection) structuredSelection).iterator();
		while (itr.hasNext()) {
			Object selctionElement = itr.next();
			if (selctionElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selctionElement;
				IResource resource = (IResource) adaptable.getAdapter(IResource.class);
				String resourcePath = resource.getLocationURI().getPath();

				resourcePath = resourcePath.startsWith("/")
						? resourcePath.substring(resourcePath.indexOf("/") + 1, resourcePath.length())
						: resourcePath;
				resourcePath = (selctionElement instanceof IFile)
						? resourcePath.substring(0, resourcePath.lastIndexOf("/"))
						: resourcePath;
				executeCmd(resourcePath);
			}
		}

	}

	/**
	 * Open root prompt.
	 */
	public void openRootPrompt() {
		Thread cmdThread = new CommandThread(ROOT_CMD, WIN_C_DRIVE);
		cmdThread.start();
	}

	/**
	 * Execute cmd.
	 *
	 * @param folderPath the folder path
	 */
	private void executeCmd(String folderPath) {
		try {
			String actualCmd = new StringBuilder("").append(CMD_PROMPT_CMD).append(" ").append(START_CMD).append(" ")
					.append(PUSHD_CMD).toString();
			Thread cmdThread = new CommandThread(actualCmd, folderPath);
			cmdThread.start();
		} catch (Exception e) {
			PluginLogger.error(e);
			MessageDialog.openError(new Shell(), ERROR, GEN_ERR_MSG);
		}
	}
}
