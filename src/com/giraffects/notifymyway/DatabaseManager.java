package com.giraffects.notifymyway;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
	private static final String TAG = "NotifyMyWay";
	private Context context;
	private DatabaseHelper database_helper;
	private SQLiteDatabase database;

	// Vibration Patterns Table
	private static final String VP_TABLE_NAME = "vibration_patterns";
	public static final String VP_KEY_NAME = "name";
	public static final String VP_KEY_PATTERN = "pattern";
	public static final String VP_KEY_ROWID = "_id";

	private static class DatabaseHelper extends SQLiteOpenHelper {
		// Upgrade when changing schema
		private static final int DATABASE_VERSION = 2;
		// Database name
		private static final String DATABASE_NAME = "nmw_data";

		private static final String VP_TABLE_CREATE = "CREATE TABLE "
				+ VP_TABLE_NAME + " (" + VP_KEY_ROWID
				+ " integer primary key autoincrement, " + VP_KEY_NAME
				+ " TEXT, " + VP_KEY_PATTERN + " TEXT" + ");";

		private static final String DATABASE_CREATE = VP_TABLE_CREATE; // TODO:

		private static Context context;

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			DatabaseHelper.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Run sql to create database
			Log.d(TAG, "Creating database");
			db.execSQL(DATABASE_CREATE);
			//Load and execute default SQL
			Log.d(TAG, "Running default sql");
			db.beginTransaction();
			DatabaseManager.createVibrationPattern("Quarter Second Buzz", "0,250", db);
			DatabaseManager.createVibrationPattern("Half Second Buzz", "0,500", db);
			DatabaseManager.createVibrationPattern("One Second Buzz", "0,1000", db);
			DatabaseManager.createVibrationPattern("Two Second Buzz", "0,2000", db);
			DatabaseManager.createVibrationPattern("Three Second Buzz", "0,3000", db);
			DatabaseManager.createVibrationPattern("Four Second Buzz", "0,4000", db);
			DatabaseManager.createVibrationPattern("Triple Buzz", "0,250,100,250,100,250", db);
			DatabaseManager.createVibrationPattern("Triple Buzz (shorter)", "0,150,50,150,50,150", db);
			DatabaseManager.createVibrationPattern("Triple Buzz (shortest)", "0,75,25,75,25,75", db);
			DatabaseManager.createVibrationPattern("5 Super Rapid Pulses", "0,50,50,50,50,50,50,50,50,50,50", db);
			DatabaseManager.createVibrationPattern("10 Super Rapid Pulses", "0,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50", db);
			DatabaseManager.createVibrationPattern("20 Super Rapid Pulses", "0,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50", db);
			DatabaseManager.createVibrationPattern("5 Rapid Pulses", "0,100,100,100,100,100,100,100,100,100,100", db);
			DatabaseManager.createVibrationPattern("10 Rapid Pulses", "0,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100", db);
			DatabaseManager.createVibrationPattern("20 Rapid Pulses", "0,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100", db);
			DatabaseManager.createVibrationPattern("Delayed One-Second-Buzz", "250,1000", db);
			DatabaseManager.createVibrationPattern("RapTap Tap Slap", "0,200,200,200,200,500,250,1000", db);
			db.setTransactionSuccessful();
			db.endTransaction();
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
		return database.query(VP_TABLE_NAME, new String[] { VP_KEY_ROWID,
				VP_KEY_NAME, VP_KEY_PATTERN }, null, null, null, null, null);
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

		database.query(true, VP_TABLE_NAME, new String[] { VP_KEY_ROWID,
				VP_KEY_NAME, VP_KEY_PATTERN }, VP_KEY_ROWID + "=" + rowId,
				null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public long createVibrationPattern(String name, String pattern) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(VP_KEY_NAME, name);
		initialValues.put(VP_KEY_PATTERN, pattern);

		return database.insert(VP_TABLE_NAME, null, initialValues);
	}
	
	public static long createVibrationPattern(String name, String pattern, SQLiteDatabase database) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(VP_KEY_NAME, name);
		initialValues.put(VP_KEY_PATTERN, pattern);

		return database.insert(VP_TABLE_NAME, null, initialValues);
	}

	/**
	 * Update the vibration pattern using the details provided. The VP to be
	 * updated is specified using the rowId, and it is altered to use the name
	 * and pattern values passed in
	 * 
	 * @param rowId
	 *            id of vp to update
	 * @param name
	 *            value to set vp name to
	 * @param pattern
	 *            value to set vp pattern to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateVibrationPattern(long rowId, String name,
			String pattern) {
		ContentValues args = new ContentValues();
		args.put(VP_KEY_NAME, name);
		args.put(VP_KEY_PATTERN, pattern);

		return database.update(VP_TABLE_NAME, args, VP_KEY_ROWID + "=" + rowId,
				null) > 0;
	}
}