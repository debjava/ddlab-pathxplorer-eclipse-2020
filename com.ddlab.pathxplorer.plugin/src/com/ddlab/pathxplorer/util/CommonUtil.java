/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import static com.ddlab.pathxplorer.util.CommonConstants.*;

/**
 * The Class CommonUtil.
 * 
 * @author Debadatta Mishra (PIKU)
 */
public class CommonUtil {

	/**
	 * Gets the file folder path.
	 *
	 * @param selection the selection
	 * @return the file folder path
	 * @throws Exception the exception
	 */
	public static List<String> getFileFolderPath(IStructuredSelection selection) throws Exception {
		List<String> resourceList = new ArrayList<String>();

		Iterator<?> itr = ((IStructuredSelection) selection).iterator();
		try {
			while (itr.hasNext()) {
				Object selctionElement = itr.next();
				if (selctionElement instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) selctionElement;
					IResource resource = (IResource) adaptable.getAdapter(IResource.class);
					resourceList.add(resource.getName());
				}
			}
		} catch (Exception e) {
			PluginLogger.error(CP_INFO_ERR_MSG, e);
			MessageDialog.openError(new Shell(), ERROR, CP_INFO_ERR_MSG + RPT_ERR_TXT);
		}
		return resourceList;
	}

	/**
	 * Gets the directory paths.
	 *
	 * @param selection the selection
	 * @return the directory paths
	 * @throws Exception the exception
	 */
	public static List<String> getDirectoryPaths(IStructuredSelection selection) throws Exception {
		List<String> resourceList = new ArrayList<String>();

		Iterator<?> itr = ((IStructuredSelection) selection).iterator();
		while (itr.hasNext()) {
			Object selctionElement = itr.next();
			if (selctionElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selctionElement;
				IResource resource = (IResource) adaptable.getAdapter(IResource.class);
				String resourcePath = resource.getLocationURI().getPath();
				resourcePath = resourcePath.startsWith("/")
						? resourcePath.substring(resourcePath.indexOf("/") + 1, resourcePath.length())
						: resourcePath;
				resourceList.add(resourcePath);
			}
		}
		return resourceList;
	}

	/**
	 * Copy file to clip board.
	 *
	 * @param data the data
	 */
	public static void copyFileToClipBoard(String[] data) {
		try {
			if (data.length != 0) {
				Display display = Display.getCurrent();
				Clipboard clipboard = new Clipboard(display);
				clipboard.setContents(new Object[] { data }, new Transfer[] { FileTransfer.getInstance() });
				clipboard.dispose();
			}
		} catch (Exception e) {
			MessageDialog.openError(new Shell(), ERROR, CP_ERR_MSG + RPT_ERR_TXT);
		}
	}

	/**
	 * Copy text to clip board.
	 *
	 * @param data the data
	 */
	public static void copyTextToClipBoard(String[] data) {
		try {
			if (data.length != 0) {
				Display display = Display.getCurrent();
				Clipboard clipboard = new Clipboard(display);
				clipboard.setContents(data, new Transfer[] { TextTransfer.getInstance() });
				clipboard.dispose();
			}
		} catch (Exception e) {
			MessageDialog.openError(new Shell(), ERROR, CP_ERR_MSG + RPT_ERR_TXT);
		}
	}

}
