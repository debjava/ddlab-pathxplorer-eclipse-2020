/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.pathxplorer.util;

import java.io.File;

/**
 * The Interface PluginConstants.
 *
 * @author <a href="mailto:debadatta.mishra@gmail.com"> Debadatta Mishra (PIKU)
 * @since 2013
 */
public interface PluginConstants {

	/** The img save path. */
	final String IMG_SAVE_PATH = "IMG_SAVE_PATH";

	/** The zip file path. */
	final String ZIP_FILE_PATH = "ZIP_FILE_PATH";

	/** The send 2 path. */
	final String SEND_2_PATH = "SEND_2_PATH";

	/** The hide status bar. */
	final String HIDE_STATUS_BAR = "STATUS_FIELD_VALUE";

	/** The default desktop path. */
	final String DEFAULT_DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop";
}
