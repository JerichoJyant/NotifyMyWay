package com.giraffects.notifymyway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import com.giraffects.notifymyway.ColorPickerDialog.OnColorChangedListener;

public class NotificationPreferences extends PreferenceActivity implements
		OnColorChangedListener, OnPreferenceClickListener {
	private static final String COLOR_PREFERENCE_KEY = "led_color";
	private static final String TEST_PREFERENCE_KEY = "do_notification_test";
	private static final String VIBRATION_PATTERN_PREFERENCE_KEY = "select_vibration_pattern";
	static final String VIBRATION_PATTERN_PREFERENCE = "vibration_pattern";
	// Constant for startActivityForResult
	private static final int ACTIVITY_CHOOSE_VP = 1001;

	class TestNotificationListener implements OnPreferenceClickListener {
		Context context;

		TestNotificationListener(Context context) {
			this.context = context;
		}

		public boolean onPreferenceClick(Preference preference) {
			SMSNotifierAction action = new SMSNotifierAction(context, null);
			action.notify_user();
			return true;
		}
	}

	class VibrationPatternListener implements OnPreferenceClickListener {
		Activity activity;

		VibrationPatternListener(Context context) {
			this.activity = (Activity) context;
		}

		public boolean onPreferenceClick(Preference preference) {
			activity.startActivityForResult(new Intent(activity,
					SelectVibrationPattern.class), ACTIVITY_CHOOSE_VP);
			return true;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// See which child activity is calling us back.
		switch (requestCode) {
		case ACTIVITY_CHOOSE_VP:
			// This is the standard resultCode that is sent back if the
			// activity crashed or didn't doesn't supply an explicit result.
			if (resultCode == RESULT_CANCELED) {
				// No pattern chosen (back button)
			} else {
				String pattern = (String) data.getExtras().get(
						DatabaseManager.VP_KEY_PATTERN);
				StaticHelper.d("Pattern returned from SelectVibrationPattern: "
						+ pattern);
				PreferenceManager.getDefaultSharedPreferences(this).edit()
						.putString(VIBRATION_PATTERN_PREFERENCE, pattern)
						.commit();
				Toast toast = Toast.makeText(this,
						"Vibration Pattern changed, now testing it for you",
						Toast.LENGTH_SHORT);
				SMSNotifierAction action = new SMSNotifierAction(this, null);
				action.notify_user();
				toast.show();
			}
		default:
			break;
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		/*
		 * Taken out due to LED issues Preference colorPref = (Preference)
		 * findPreference(COLOR_PREFERENCE_KEY);
		 * colorPref.setOnPreferenceClickListener(this);
		 */
		Preference testPref = (Preference) findPreference(TEST_PREFERENCE_KEY);
		testPref
				.setOnPreferenceClickListener(new TestNotificationListener(this));

		Preference vibrationPatternPref = (Preference) findPreference(VIBRATION_PATTERN_PREFERENCE_KEY);
		vibrationPatternPref
				.setOnPreferenceClickListener(new VibrationPatternListener(this));
	}

	public void colorChanged(int color) {
		PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(
				COLOR_PREFERENCE_KEY, color).commit();
	}

	public boolean onPreferenceClick(Preference preference) {
		StaticHelper.d("Opening Color Picker");
		try {
			new ColorPickerDialog(this, this, PreferenceManager
					.getDefaultSharedPreferences(this).getInt(
							COLOR_PREFERENCE_KEY, 0)).show();
		} catch (Exception e) {
			StaticHelper.e("Error while opening color dialog", e);
		}
		return true;
	}
}
