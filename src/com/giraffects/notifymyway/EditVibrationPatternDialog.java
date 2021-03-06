package com.giraffects.notifymyway;

import android.app.Dialog;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditVibrationPatternDialog extends Dialog implements
		OnClickListener {
	public static final int TYPE_ADD = 1;
	public static final int TYPE_EDIT = 2;
	private int type;
	private SelectVibrationPattern context; // Effectively a context, however
	// additional methods needed
	private long id;

	public EditVibrationPatternDialog(SelectVibrationPattern context, int type) {
		// For adding
		super(context);
		this.type = type;
		this.context = context;

		setContentView(R.layout.edit_pattern_dialog);
		if (type == TYPE_EDIT) {
			setTitle("Edit Pattern");
		} else if (type == TYPE_ADD) {
			setTitle("Add Pattern");
		}
		Button save_button = (Button) findViewById(R.id.save_pattern_button);
		save_button.setOnClickListener(this);
		Button cancel_button = (Button) findViewById(R.id.cancel_pattern_button);
		cancel_button.setOnClickListener(this);
	}

	public EditVibrationPatternDialog(SelectVibrationPattern context, int type,
			long id) {
		// Editing
		super(context);
		this.type = type;
		this.context = context;
		this.id = id;

		setContentView(R.layout.edit_pattern_dialog);
		setTitle("Edit Pattern");
		TextView vp_name = (TextView) findViewById(R.id.name);
		TextView vp_pattern = (TextView) findViewById(R.id.pattern);

		DatabaseManager db_manager = new DatabaseManager(context).open();
		Cursor vp = db_manager.fetchVibrationPattern(id);
		vp_name.setText(vp.getString(vp
				.getColumnIndexOrThrow(DatabaseManager.VP_KEY_NAME)));
		vp_pattern.setText(vp.getString(vp
				.getColumnIndexOrThrow(DatabaseManager.VP_KEY_PATTERN)));
		db_manager.close();
		Button save_button = (Button) findViewById(R.id.save_pattern_button);
		save_button.setOnClickListener(this);
		Button cancel_button = (Button) findViewById(R.id.cancel_pattern_button);
		cancel_button.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.save_pattern_button) {

			// Very dense, but retrieves text values
			String vp_name = (String) ((TextView) findViewById(R.id.name))
					.getText().toString();
			String vp_pattern = ((TextView) findViewById(R.id.pattern))
					.getText().toString();
			if (vp_name == "") {
				// Name is blank
				// Notify user and cancel operation
				Toast toast = Toast
						.makeText(
								context,
								"The name of the pattern can not be blank, please try again.",
								Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			if (vp_pattern == "") {
				// Pattern is blank
				// Notify user and cancel operation
				Toast toast = Toast.makeText(context,
						"The the pattern can not be blank, please try again.",
						Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			DatabaseManager db_manager = new DatabaseManager(context).open();
			if (type == TYPE_ADD) {

				StaticHelper.d("createVibrationPattern, name: " + vp_name
						+ ", pattern:" + vp_pattern);
				long result = db_manager.createVibrationPattern(vp_name,
						vp_pattern);
				if (result == -1) {
					StaticHelper
							.e("Could not insert new row into database with values "
									+ vp_name
									+ ", "
									+ vp_pattern
									+ ". Contact developer");
					CharSequence text = "Error inserting the new row into database";
					int duration = Toast.LENGTH_LONG;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else {
					StaticHelper.d("Created database row id: " + result);
				}
				context.onChangeVibrationPatterns();
				db_manager.close();
				dismiss();
			} else if (type == TYPE_EDIT) {
				if (db_manager.updateVibrationPattern(id, vp_name, vp_pattern)) {
					Toast toast = Toast.makeText(context,
							"Edited vibration pattern", Toast.LENGTH_SHORT);
					toast.show();
					context.onChangeVibrationPatterns();
					db_manager.close();
				} else {
					StaticHelper.e("Could not edit vibration pattern");
					Toast toast = Toast
							.makeText(
									context,
									"Failed to edit vibration pattern, contact developer",
									Toast.LENGTH_SHORT);
					toast.show();
				}
				dismiss();
				// onChangeVibrationPatterns();
			}
		} else if (v.getId() == R.id.cancel_pattern_button) {
			StaticHelper.d("Dialog Canceled");
			dismiss();
		}
	}

}
