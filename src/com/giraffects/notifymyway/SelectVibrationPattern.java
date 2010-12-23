package com.giraffects.notifymyway;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SelectVibrationPattern extends Activity {
	static final int DIALOG_EDIT_VP = 1;
	static final int DIALOG_ADD_VP = 2;
	private static final String TAG = "NotifyMyWay";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_vibration_pattern);

		ListView vibration_patterns_list = (ListView) findViewById(R.id.vibration_patterns_list);
		DatabaseManager db_manager = new DatabaseManager(this).open();
		SimpleCursorAdapter cursor_adapter = new SimpleCursorAdapter(
				this,
				R.layout.vp_list_item, // Use a template
				// that displays a
				// text view
				db_manager.fetchAllVibrationPatterns(), // Give the cursor to
				// the list adapter
				new String[] { DatabaseManager.VP_KEY_NAME },
				new int[] { android.R.id.text1 }); // The "text1" view defined
		// in
		// the XML template
		vibration_patterns_list.setAdapter(cursor_adapter);

		Button add_pattern_button = (Button) findViewById(R.id.add_vibration_pattern_button);
		// Register the onClick listener with the implementation above
		add_pattern_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_ADD_VP);
			}
		});

	}

	public Dialog onCreateDialog(int id) {
		Log.d(TAG, "onCreateDialog called, id: "+id);
		switch (id) {
		case (DIALOG_EDIT_VP): {
			Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.edit_pattern_dialog);
			dialog.setTitle("Edit Pattern");
			Button save_button = (Button) dialog
					.findViewById(R.id.save_pattern_button);
			save_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO: Edit table row
					removeDialog(DIALOG_EDIT_VP);
				}
			});
			Button cancel_button = (Button) dialog
					.findViewById(R.id.save_pattern_button);
			cancel_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.d(TAG, "Dialog Canceled");
					removeDialog(DIALOG_EDIT_VP);
				}
			});
			return dialog;
		}
		case (DIALOG_ADD_VP): {
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.edit_pattern_dialog);
			dialog.setTitle("Add Pattern");
			Button save_button = (Button) dialog
					.findViewById(R.id.save_pattern_button);
			save_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					DatabaseManager db_manager = new DatabaseManager(getApplicationContext()).open();
					// Very dense, but retrieves text values
					String vp_name = (String) ((TextView) dialog
							.findViewById(R.id.name)).getText();
					String vp_pattern = (String) ((TextView) dialog
							.findViewById(R.id.pattern)).getText();
					Log.d(TAG, "createVibrationPattern, name: " + vp_name + ", pattern:"+ vp_pattern);
					long result = db_manager.createVibrationPattern(vp_name, vp_pattern); // TODO:
																			// Get
																			// from
																			// dialog
					if (result==-1) {
						Log.e(TAG, "Could not insert new row into database with values " + vp_name + ", " + vp_pattern);
						CharSequence text = "Error inserting the new row into database";
						int duration = Toast.LENGTH_LONG;

						Toast toast = Toast.makeText(getApplicationContext(), text, duration);
						toast.show();
					}
					db_manager.close();
					onChangeVibrationPatterns();
					removeDialog(DIALOG_ADD_VP);
				}
			});
			Button cancel_button = (Button) dialog
					.findViewById(R.id.save_pattern_button);
			cancel_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.d(TAG, "Dialog Canceled");
					removeDialog(DIALOG_ADD_VP);
				}
			});
			return dialog;
		}
		default:
			return null;
		}

	}

	private void onChangeVibrationPatterns() {
		ListView vibration_patterns_list = (ListView) findViewById(R.id.vibration_patterns_list);
		( (CursorAdapter) vibration_patterns_list.getAdapter()).notifyDataSetChanged();
	}
	// TODO: notifyDataSetChanged() when adding/editing patterns
}
