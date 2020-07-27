/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.ddlab.pathxplorer.Activator;

/**
 * This is a dialog class which displays about the features for this plugin.
 * 
 * @author <a href="mailto:debadatta.mishra@gmail.com"> Debadatta Mishra (PIKU)
 * @since 2013
 * 
 */
public class PluginLogger {

	/**
	 * Method to log the information.
	 *
	 * @param message the message
	 */
	public static void info(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);
	}

	/**
	 * Method to log the error.
	 *
	 * @param exception the exception
	 */
	public static void error(Throwable exception) {
		error("DebaUtilPlugin UnExpected Exception ...\n", exception); //$NON-NLS-1$
	}

	/**
	 * Method to log the error.
	 *
	 * @param message   the message
	 * @param exception the exception
	 */
	public static void error(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}

	/**
	 * Method to log in your custom way.
	 *
	 * @param severity  the severity
	 * @param code      the code
	 * @param message   the message
	 * @param exception the exception
	 */
	public static void log(int severity, int code, String message, Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}

	/**
	 * Method to create a status in your own way and to log the information.
	 *
	 * @param severity  the severity
	 * @param code      the code
	 * @param message   the message
	 * @param exception the exception
	 * @return the i status
	 */
	public static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(severity, Activator.getDefault().getBundle().getSymbolicName(), code, message, exception);
	}

	/**
	 * Method to log the created status.
	 *
	 * @param status the status
	 */
	public static void log(IStatus status) {
		Activator.getDefault().getLog().log(status);
	}
}
