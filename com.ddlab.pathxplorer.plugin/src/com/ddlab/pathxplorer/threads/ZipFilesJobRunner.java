/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.threads;

import static com.ddlab.pathxplorer.util.CommonConstants.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import com.ddlab.pathxplorer.Activator;
import com.ddlab.pathxplorer.util.PluginConstants;
import com.ddlab.pathxplorer.util.PluginLogger;

/**
 * This class is used to zip the file/s and send to Desktop or the path
 * configured in Eclipse Preference. This class performs the functionality in a
 * separate thread.
 * 
 * @author <a href="mailto:debadatta.mishra@gmail.com"> Debadatta Mishra (PIKU)
 * @since 2013
 * 
 */
public class ZipFilesJobRunner implements Runnable {

	/** Increment counter for the file naming convention. */
	private static int counter = 0;

	/** Selection for the selected file/s. */
	private IStructuredSelection iSelection = null;

	/** The folder name. */
	private String folderName = null;

	/**
	 * Default constructor.
	 *
	 * @param iSelection the i selection
	 */
	public ZipFilesJobRunner(IStructuredSelection iSelection) {
		this.iSelection = iSelection;
	}

	/**
	 * Instantiates a new zip files job runner.
	 *
	 * @param iSelection the i selection
	 * @param folderName the folder name
	 */
	public ZipFilesJobRunner(IStructuredSelection iSelection, String folderName) {
		this.iSelection = iSelection;
		this.folderName = folderName;
	}

	/**
	 * Run.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		/*
		 * Execting the functionality in the Eclipse progress thread
		 */
		runWorkspaceJob(iSelection);
	}

	/**
	 * Method used to zip file/s in the Eclipse UI Job thread to provide a progress
	 * indicator.
	 *
	 * @param iSelection the i selection
	 */
	public void runWorkspaceJob(final IStructuredSelection iSelection) {
		final int totalFiles = iSelection.size();
		counter++;
		String send2PathVal = Activator.getDefault().getPreferenceStore().getString(PluginConstants.ZIP_FILE_PATH);
		send2PathVal = (send2PathVal == null || send2PathVal.equals("")) ? PluginConstants.DEFAULT_DESKTOP_PATH
				: send2PathVal;
		String zipDirPath = send2PathVal;
		File zipFile = new File(zipDirPath);
		zipDirPath = (!zipFile.exists()) ? PluginConstants.DEFAULT_DESKTOP_PATH : zipDirPath;
		String zipFilePath = (folderName == null) ? zipDirPath + File.separator + ZIP_FILE_NAME + counter + DOT_ZIP
				: zipDirPath + File.separator + folderName + DOT_ZIP;

		PluginLogger.info("Zip File Path ::: " + zipFilePath);
		final byte[] buffer = new byte[1024];
		try {
			FileOutputStream fos = new FileOutputStream(zipFilePath);
			final ZipOutputStream zos = new ZipOutputStream(fos);

			Job job = new Job(ZIP_IN_PROGRESS) {
				protected IStatus run(IProgressMonitor monitor) {
					try {
						int eachP = (100 / totalFiles);
						int counter = 0;
						monitor.beginTask("", 100);

						Iterator<?> itr = iSelection.iterator();
						while (itr.hasNext()) {
							Object selctionElement = itr.next();
							if (selctionElement instanceof IAdaptable) {
								IAdaptable adaptable = (IAdaptable) selctionElement;
								IResource resource = (IResource) adaptable.getAdapter(IResource.class);
								String resourcePath = resource.getLocationURI().getPath();
								String fileName = resource.getName();
								monitor.subTask(ZIP_FILE_MSG + fileName);

								resourcePath = resourcePath.startsWith("/")
										? resourcePath.substring(resourcePath.indexOf("/") + 1, resourcePath.length())
										: resourcePath;
								File fileDir = new File(resourcePath);

								if (fileDir.isDirectory()) {
									zipSubDirectory(fileName + "/", fileDir, zos);
								} else {
									ZipEntry ze = new ZipEntry(fileName);
									zos.putNextEntry(ze);
									FileInputStream in = new FileInputStream(resourcePath);
									int len;
									while ((len = in.read(buffer)) > 0) {
										zos.write(buffer, 0, len);
									}
									in.close();
								}
								if (monitor.isCanceled())
									break;

								counter = counter + eachP;
								monitor.worked(counter);
							}
						}
						zos.closeEntry();
						zos.close();
						monitor.worked(100);
					} catch (Exception e) {
						e.printStackTrace();
						PluginLogger.error("UnExpected Excepton while zipping the files and sending to Desktop ...\n",
								e);
					}
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		} catch (Exception e) {
			PluginLogger.error("UnExpected Excepton while zipping the files and sending to Desktop ...\n", e);
			MessageDialog.openError(new Shell(), ERROR, GEN_ERR_MSG);
		}
	}

	/**
	 * Zip sub directory.
	 *
	 * @param basePath the base path
	 * @param dir      the dir
	 * @param zout     the zout
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void zipSubDirectory(String basePath, File dir, ZipOutputStream zout) throws IOException {
		byte[] buffer = new byte[4096];
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				String path = basePath + file.getName() + "/";
				zout.putNextEntry(new ZipEntry(path));
				zipSubDirectory(path, file, zout);
				zout.closeEntry();
			} else {
				FileInputStream fin = new FileInputStream(file);
				zout.putNextEntry(new ZipEntry(basePath + file.getName()));
				int length;
				while ((length = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, length);
				}
				zout.closeEntry();
				fin.close();
			}
		}
	}
}
