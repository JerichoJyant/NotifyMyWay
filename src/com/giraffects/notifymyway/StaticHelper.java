package com.giraffects.notifymyway;

import android.util.Log;

public class StaticHelper {
	public static String TAG = "NotifyMyWay";
	private final static boolean debug = true;

	public static void d(String debugMessage) {
		if (debug)
			Log.d(TAG, debugMessage);
	}
	public static void w(String debugMessage) {
		if (debug)
			Log.w(TAG, debugMessage);
	}
	public static void e(String debugMessage) {
		if (debug)
			Log.e(TAG, debugMessage);
	}
	public static void e(String debugMessage, Throwable error) {
		if (debug)
			Log.e(TAG, debugMessage, error);
	}
	public static void v(String debugMessage) {
		if (debug)
			Log.v(TAG, debugMessage);
	}
	public static void i(String debugMessage) {
		if (debug)
			Log.i(TAG, debugMessage);
	}
}
