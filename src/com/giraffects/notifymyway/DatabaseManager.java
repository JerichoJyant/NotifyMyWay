package com.giraffects.notifymyway;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
	private static final String TAG = "NMW DBManager";
	private Context context;
	private DatabaseHelper database_helper;
	private SQLiteDatabase database;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		// Upgrade when changing schema
		private static final int DATABASE_VERSION = 1;
		
		// Database name
		private static final String DATABASE_NAME = "nmw_data";
		
		// Vibration Patterns Table
		private static final String VP_TABLE_NAME = "vibration_patterns";
		public static final String VP_KEY_NAME = "name";
		public static final String VP_KEY_PATTERN = "body";
		public static final String VP_KEY_ROWID = "_id";
		private static final String VP_TABLE_CREATE = "CREATE TABLE "
				+ VP_TABLE_NAME + " (" + VP_KEY_ROWID
				+ " integer primary key autoincrement" + VP_KEY_NAME
				+ " TEXT, " + VP_KEY_PATTERN + " TEXT" + ");";

		private static final String DATABASE_CREATE = VP_TABLE_CREATE; // TODO:
																		// Add
																		// more
																		// tables

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Run sql to create database
			db.execSQL(DATABASE_CREATE);
			// TODO: Run SQL to pre-populate
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + VP_TABLE_NAME);
			onCreate(db);
		}
	}

	DatabaseManager(Context context) {
		this.context = context;
	}

	/**
	 * Open the database. If it cannot be opened, try to create a new instance
	 * of the database. If it cannot be created, throw an exception to signal
	 * the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public DatabaseManager open() throws SQLException {
		database_helper = new DatabaseHelper(context);
		database = database_helper.getWritableDatabase();
		return this;
	}

	public void close() {
		database_helper.close();
	}

	/**
	 * Return a Cursor over the list of all vibration patterns in the vibration
	 * patterns database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllVibrationPatterns() {
		return database.query(DatabaseHelper.VP_TABLE_NAME, new String[] {
				DatabaseHelper.VP_KEY_ROWID, DatabaseHelper.VP_KEY_NAME,
				DatabaseHelper.VP_KEY_PATTERN }, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchVibrationPattern(long rowId) throws SQLException {

		Cursor mCursor =

		database.query(true, DatabaseHelper.VP_TABLE_NAME, new String[] {
				DatabaseHelper.VP_KEY_ROWID, DatabaseHelper.VP_KEY_NAME,
				DatabaseHelper.VP_KEY_PATTERN }, DatabaseHelper.VP_KEY_ROWID
				+ "=" + rowId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
}