package com.giraffects.notifymyway;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class SelectVibrationPattern extends Activity implements
		OnItemClickListener, OnItemLongClickListener {
	static final int DIALOG_EDIT_VP = 1;
	static final int DIALOG_ADD_VP = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_vibration_pattern);
		setTitle("Select or Manage Vibration Patterns");
		ListView vp_list = (ListView) findViewById(R.id.vibration_patterns_list);
		vp_list.setOnItemLongClickListener(this);
		vp_list.setOnItemClickListener(this);
		DatabaseManager db_manager = new DatabaseManager(this).open();
		SimpleCursorAdapter cursor_adapter = new SimpleCursorAdapter(
				this,
				R.layout.vp_list_item, // Use a template
				// that displays a
				// text view
				db_manager.fetchAllVibrationPatterns(), // Give the cursor to
				// the list adapter
				new String[] { DatabaseManager.VP_KEY_NAME, DatabaseManager.VP_KEY_PATTERN },
				new int[] { R.id.vp_list_item_name, R.id.vp_list_item_pattern }); 
		// in
		// the XML template
		vp_list.setAdapter(cursor_adapter);
		db_manager.close();

		Button add_pattern_button = (Button) findViewById(R.id.add_vibration_pattern_button);
		// Register the onClick listener with the implementation above
		add_pattern_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_ADD_VP);
			}
		});
		Button cancel_button = (Button) findViewById(R.id.cancel_vibration_pattern_button);
		// Register the onClick listener with the implementation above
		cancel_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

	}

	public Dialog onCreateDialog(int id) {
		StaticHelper.d("onCreateDialog called, id: " + id);
		switch (id) {
		case (DIALOG_EDIT_VP): {
			//

		}
		case (DIALOG_ADD_VP): {
			return new EditVibrationPatternDialog(this,
					EditVibrationPatternDialog.TYPE_ADD);
		}
		default:
			return null;
		}

	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		StaticHelper.d("Item in list long clicked, id: " + id);
		Dialog edit_dialog = new EditVibrationPatternDialog(this,
				EditVibrationPatternDialog.TYPE_EDIT, id);
		edit_dialog.setOwnerActivity(this);
		edit_dialog.show();
		return true;

	}

	void onChangeVibrationPatterns() {
		DatabaseManager db_manager = new DatabaseManager(this).open();
		ListView vibration_patterns_list = (ListView) findViewById(R.id.vibration_patterns_list);
		((SimpleCursorAdapter)vibration_patterns_list.getAdapter()).changeCursor(db_manager.fetchAllVibrationPatterns());
		db_manager.close();
		//((CursorAdapter) vibration_patterns_list.getAdapter()).notifyDataSetChanged();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		StaticHelper.d("Item in list clicked, id: " + id);
		DatabaseManager db_manager = new DatabaseManager(this).open();
		Cursor vp = db_manager.fetchVibrationPattern(id);
		String vp_pattern = vp.getString(vp
				.getColumnIndexOrThrow(DatabaseManager.VP_KEY_PATTERN));
		db_manager.close();
		Bundle result = new Bundle();
        result.putString(DatabaseManager.VP_KEY_PATTERN, vp_pattern);
        setResult(RESULT_OK, new Intent().putExtras(result));
        finish();

	}

	// TODO: notifyDataSetChanged() when adding/editing patterns
}
