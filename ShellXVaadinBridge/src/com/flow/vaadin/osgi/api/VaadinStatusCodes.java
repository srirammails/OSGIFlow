package com.flow.vaadin.osgi.api;


/**
 * A class that contains all status codes of that bundle and helper methods to
 * create the status objects.
 */
public class VaadinStatusCodes {

	public static final int OK = 0;

	/**
	 * Indicates that a problem occurred setting the http service. See exception
	 * in status.
	 */
	public static final int SETTING_HTTP_SERVICE = 1000;

	/**
	 * Indicates that a problem occurred starting the http service tracker.
	 */
	public static final int HTTP_SERVICE_TRACKER = 1001;

	/**
	 * Represents the OK status.
	 */
	public static final int OK_STATUS = 0;

	/**
	 * Creates a status that indicates that the given alias is already used.
	 * 
	 * @param alias
	 * @return
	 */
	public static Integer createOK() {
		return 0;
	}

	/**
	 * Creates a status that indicates that a problem occurred setting the http
	 * service.
	 * 
	 * @param e
	 *            the thrown exception or <code>null</code>
	 * @return
	 */
	public static Integer createSettingHttpService(Throwable e) {
		return 8;
	}

	/**
	 * Creates a status that a problem with the http service tracker occured.
	 * 
	 * @param e
	 *            the thrown exception or <code>null</code>
	 * @return
	 */
	public static Integer createHttpServiceTracker(Throwable e) {
		return 8;
	}
}
