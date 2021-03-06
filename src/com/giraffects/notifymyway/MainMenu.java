package com.giraffects.notifymyway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// TODO: Put the following in an Application subclass
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		// Capture our button from layout
		Button launchPreferences = (Button) findViewById(R.id.edit_preferences);
		// Register the onClick listener with the implementation above
		launchPreferences.setOnClickListener(this);

		Button help_button = (Button) findViewById(R.id.main_help_button);
		help_button.setOnClickListener(this);

	}

	public void onClick(View v) {
		// TODO: discriminate between events
		if (v.getId() == R.id.edit_preferences) {
			startActivity(new Intent(this, NotificationPreferences.class));
		} else if (v.getId() == R.id.main_help_button) {
			startActivity(new Intent(this, HelpActivity.class));
		}
	}

}