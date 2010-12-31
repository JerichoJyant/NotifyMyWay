package com.giraffects.notifymyway;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		Button back_button = (Button) findViewById(R.id.help_back_button);
		back_button.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		// Assume back button
		finish();
	}

}
