package com.giraffects.notifymyway;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditVibrationPatternDialog extends Dialog implements
		OnClickListener {
	public final int TYPE_ADD = 1;
	public final int TYPE_EDIT = 2;
	private int type;
	private final String TAG = "NotifyMyWay";
	private Context context;

	public EditVibrationPatternDialog(Context context, int type) {
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
		Button cancel_button = (Button) findViewById(R.id.save_pattern_button);
		cancel_button.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.save_pattern_button) {
			if (type == TYPE_ADD) {
				dismiss();
			} else if (type == TYPE_EDIT) {
				DatabaseManager db_manager = new DatabaseManager(context)
						.open();
				// Very dense, but retrieves text values
				String vp_name = (String) ((TextView) findViewById(R.id.name))
						.getText();
				String vp_pattern = (String) ((TextView) findViewById(R.id.pattern))
						.getText();
				Log.d(TAG, "createVibrationPattern, name: " + vp_name
						+ ", pattern:" + vp_pattern);
				long result = db_manager.createVibrationPattern(vp_name,
						vp_pattern);
				if (result == -1) {
					Log.e(TAG,
							"Could not insert new row into database with values "
									+ vp_name + ", " + vp_pattern);
					CharSequence text = "Error inserting the new row into database";
					int duration = Toast.LENGTH_LONG;

					Toast toast = Toast.makeText(getApplicationContext(), text,
							duration);
					toast.show();
				} else {
					Log.d(TAG, "Created database row id: " + result);
				}
				db_manager.close();
				// onChangeVibrationPatterns();
			}
		} else if (v.getId() == R.id.cancel_pattern_button) {
			Log.d(TAG, "Dialog Canceled");
			dismiss();
		}
	}

}
