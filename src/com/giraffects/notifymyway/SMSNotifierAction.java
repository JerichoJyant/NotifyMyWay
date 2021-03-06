package com.giraffects.notifymyway;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsMessage;

class SMSNotifierAction {
	Context context;
	SmsMessage sms_message;
	String phoneNumber;

	SMSNotifierAction(Context context, SmsMessage sms_message) {
		this.context = context;
		this.sms_message = sms_message;
		if(sms_message!=null) {
			this.phoneNumber = sms_message.getOriginatingAddress();
		} else {
			this.phoneNumber = "5555555555"; //Not valid in USA
		}
	}

	void notify_user() {
		try {
			StaticHelper.d("Receiving SMS");
			StaticHelper.i("SMS from: " + phoneNumber);
			// ---get the SMS message passed in---
			// Bundle bundle = intent.getExtras();
			// Todo: Correctly handle message.

			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(context);

			CharSequence tickerText = "Text Message Received";
			long when = System.currentTimeMillis();
			StaticHelper.d("Creating Notification");
			Notification notif = new Notification(0, tickerText, when);
			if (sp.getBoolean("vibration_on_preference", true)) {
				DatabaseManager db = new DatabaseManager(context).open();
				String str_vibration_pattern;
				try {
					str_vibration_pattern = db
							.getVibrationPatternFromNumber(phoneNumber);
					
					StaticHelper.i("Vibration pattern found for " + phoneNumber
							+ ": " + phoneNumber);
				} catch (NumberNotKnownException nnk) {
					StaticHelper
							.i("Number not known thrown for " + phoneNumber);
					str_vibration_pattern = sp
							.getString(
									NotificationPreferences.VIBRATION_PATTERN_PREFERENCE,
									"0,500");
				} finally {
					db.close();
				}

				notif.vibrate = VPStringToArray(str_vibration_pattern);
			}
			/*
			 * Taken out due to issues with LED remaining, will be back in
			 * future // notif.ledARGB = 0xff00ffff; notif.ledARGB =
			 * sp.getInt("led_color", 0); notif.ledOnMS =
			 * Integer.decode(sp.getString( "flash_on_duration_preference",
			 * "500")); notif.ledOffMS = Integer.decode(sp.getString(
			 * "flash_off_duration_preference", "500")); if
			 * (sp.getBoolean("flash_led_preference", true)) { notif.flags |=
			 * Notification.FLAG_SHOW_LIGHTS; }
			 */
			StaticHelper.d("Notifying");
			try {
				nm.notify(1, notif);
			} catch (Exception e) {
				StaticHelper.e("Error while notifying", e);
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
			StaticHelper.e("Generic Failure", e);
		}
	}
	long[] VPStringToArray(String str_vibration_pattern) {
		String[] str_vibrate_array = str_vibration_pattern.split(",");
		long[] long_vibrate_array = new long[str_vibrate_array.length];
		for (int i = 0; i < long_vibrate_array.length; i++) {
			long_vibrate_array[i] = Long
					.parseLong(str_vibrate_array[i]);
		}
		return long_vibrate_array;
	}

}
