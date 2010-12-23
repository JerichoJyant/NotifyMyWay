package com.giraffects.notifymyway;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

class SMSNotifierAction {
	private static final String TAG = "NotifyMyWay";
	Context context;

	SMSNotifierAction(Context context) {
		this.context = context;
	}

	void notify_user() {
		try {
			Log.d(TAG, "Receiving SMS");

			// ---get the SMS message passed in---
			// Bundle bundle = intent.getExtras();
			// Todo: Correctly handle message.
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(context);

			CharSequence tickerText = "Text Message Received";
			long when = System.currentTimeMillis();
			Log.d(TAG, "Creating Notification");
			Notification notif = new Notification(0, tickerText, when);
			if (sp.getBoolean("vibration_on_preference", true)) {
				// Choose which vibration pattern string to load
				String str_vibration_pattern = "";

				str_vibration_pattern = sp.getString(
						NotificationPreferences.VIBRATION_PATTERN_PREFERENCE,
						"0,500");

				// Create array
				String[] str_vibrate_array = str_vibration_pattern.split(",");
				long[] long_vibrate_array = new long[str_vibrate_array.length];
				for (int i = 0; i < long_vibrate_array.length; i++) {
					long_vibrate_array[i] = Long
							.parseLong(str_vibrate_array[i]);
				}

				notif.vibrate = long_vibrate_array;
			}
			// notif.ledARGB = 0xff00ffff;
			notif.ledARGB = sp.getInt("led_color", 0);
			notif.ledOnMS = Integer.decode(sp.getString(
					"flash_on_duration_preference", "500"));
			notif.ledOffMS = Integer.decode(sp.getString(
					"flash_off_duration_preference", "500"));
			if (sp.getBoolean("flash_led_preference", true)) {
				notif.flags |= Notification.FLAG_SHOW_LIGHTS;
			}
			Log.d(TAG, "Notifying");
			try {
				nm.notify(1, notif);
			} catch (Exception e) {
				Log.e(TAG, "Error while notifying", e);
			}
			/*
			 * SmsMessage[] msgs = null; String str = ""; if (bundle != null) {
			 * // ---retrieve the SMS message received--- Object[] pdus =
			 * (Object[]) bundle.get("pdus"); msgs = new
			 * SmsMessage[pdus.length]; for (int i = 0; i < msgs.length; i++) {
			 * msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]); str +=
			 * "SMS from " + msgs[i].getOriginatingAddress(); str += " :"; str
			 * += msgs[i].getMessageBody().toString(); str += "\n"; } //
			 * ---display the new SMS message--- Toast.makeText(context, str,
			 * Toast.LENGTH_SHORT).show(); }
			 */
		} catch (Exception e) {
			Log.e(TAG, "Generic Failure", e);
		}
	}
}
