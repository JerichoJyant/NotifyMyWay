package com.giraffects.notifymyway;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

public class DatabaseManager {
	private Context context;
	private DatabaseHelper database_helper;
	private SQLiteDatabase database;

	// Vibration Patterns Table
	private static final String VP_TABLE_NAME = "vibration_patterns";
	public static final String VP_KEY_NAME = "name";
	public static final String VP_KEY_PATTERN = "pattern";
	public static final String VP_KEY_ROWID = "_id";

	// Assign Contact Table
	private static final String AC_TABLE_NAME = "contact_assignments";
	public static final String AC_KEY_CONTACT = "contact";
	public static final String AC_KEY_ROWID = "_id";
	public static final String AC_KEY_VP_ROWID = "vibration_pattern_id";

	private static class DatabaseHelper extends SQLiteOpenHelper {
		// Upgrade when changing schema
		// Database Version 1: Pre release
		// Database Version 2: Release 1.0
		// Database Version 3: 1.5.0 Alpha (subject to change to Release 1.5.0)
		private static final int VERSION_PRERELEASE = 1;
		private static final int VERSION_1 = 2;
		private static final int VERSION_1POINT5 = 3;
		private static final int DATABASE_VERSION = VERSION_1POINT5;
		// Database name
		private static final String DATABASE_NAME = "nmw_data";

		private static final String VP_TABLE_CREATE = "CREATE TABLE "
				+ VP_TABLE_NAME + " (" + VP_KEY_ROWID
				+ " integer primary key autoincrement, " + VP_KEY_NAME
				+ " TEXT, " + VP_KEY_PATTERN + " TEXT);";

		private static final String AC_TABLE_CREATE = "CREATE TABLE "
				+ AC_TABLE_NAME + " (" + AC_KEY_ROWID
				+ " integer primary key autoincrement, " + AC_KEY_CONTACT
				+ " INTEGER, " + AC_KEY_VP_ROWID + " INTEGER);";

		private static final String DATABASE_CREATE = VP_TABLE_CREATE + " "
				+ AC_TABLE_CREATE; // TODO:

		@SuppressWarnings("unused")
		private static Context context;

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			DatabaseHelper.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Run sql to create database
			StaticHelper.d("Creating database");
			db.execSQL(DATABASE_CREATE);
			// Load and execute default SQL
			StaticHelper.d("Running default sql");
			insertVersion1SQL(db);
			insertVersion1Point5SQL(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (oldVersion == VERSION_PRERELEASE) {
				// Should not happen
				StaticHelper.w("Upgrading database from version " + oldVersion
						+ " to " + newVersion
						+ ", which will destroy all old data");
				db.execSQL("DROP TABLE IF EXISTS " + VP_TABLE_NAME);
				onCreate(db);
			}
			if (oldVersion == VERSION_1) {
				// Upgrade from 1.0.0 to 1.5.0
				// Insert the SQL for 1.5.0
				StaticHelper.i("Upgrading database from version " + oldVersion
						+ " to " + newVersion
						+ ", which will keep all old data");
				insertVersion1Point5SQL(db);
				db.execSQL(AC_TABLE_CREATE); // Create Assign Contacts table
			}
		}
	}

	private static void insertVersion1SQL(SQLiteDatabase db) {
		StaticHelper.i("Inserting Version 1 SQL");
		db.beginTransaction();
		DatabaseManager.createVibrationPattern("Quarter Second Buzz", "0,250",
				db);
		DatabaseManager.createVibrationPattern("Half Second Buzz", "0,500", db);
		DatabaseManager.createVibrationPattern("One Second Buzz", "0,1000", db);
		DatabaseManager.createVibrationPattern("Two Second Buzz", "0,2000", db);
		DatabaseManager.createVibrationPattern("Three Second Buzz", "0,3000",
				db);
		DatabaseManager
				.createVibrationPattern("Four Second Buzz", "0,4000", db);
		DatabaseManager.createVibrationPattern("Triple Buzz",
				"0,250,100,250,100,250", db);
		DatabaseManager.createVibrationPattern("Triple Buzz (shorter)",
				"0,150,50,150,50,150", db);
		DatabaseManager.createVibrationPattern("Triple Buzz (shortest)",
				"0,75,25,75,25,75", db);
		DatabaseManager.createVibrationPattern("5 Super Rapid Pulses",
				"0,50,50,50,50,50,50,50,50,50,50", db);
		DatabaseManager
				.createVibrationPattern(
						"10 Super Rapid Pulses",
						"0,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50",
						db);
		DatabaseManager
				.createVibrationPattern(
						"20 Super Rapid Pulses",
						"0,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50",
						db);
		DatabaseManager.createVibrationPattern("5 Rapid Pulses",
				"0,100,100,100,100,100,100,100,100,100,100", db);
		DatabaseManager
				.createVibrationPattern(
						"10 Rapid Pulses",
						"0,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100",
						db);
		DatabaseManager
				.createVibrationPattern(
						"20 Rapid Pulses",
						"0,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100",
						db);
		DatabaseManager.createVibrationPattern("Delayed One-Second-Buzz",
				"250,1000", db);
		DatabaseManager.createVibrationPattern("RapTap Tap Slap",
				"0,200,200,200,200,500,250,1000", db);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	private static void insertVersion1Point5SQL(SQLiteDatabase db) {
		StaticHelper.i("Inserting Version 1Point5 SQL");
		db.beginTransaction();
		DatabaseManager.createVibrationPattern("Royal Entrance",
				"0,400,100,250,100,250,200,500,100,250,500,250,100,250", db); // Tyler
		// Burnham

		DatabaseManager
				.createVibrationPattern(
						"Fibonacci",
						"0,1,100,2,100,3,100,5,100,8,100,13,100,21,100,34,100,55,100,89,100,144,100,233,100,337,100,610",
						db); // Tyler Burnham
		// TODO: More patterns
		db.setTransactionSuccessful();
		db.endTransaction();
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
	 * @return Cursor over all vibration patterns
	 */
	public Cursor fetchAllVibrationPatterns() {
		return database.query(VP_TABLE_NAME, new String[] { VP_KEY_ROWID,
				VP_KEY_NAME, VP_KEY_PATTERN }, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the vp that matches the given rowId
	 * 
	 * @param rowId
	 *            id of vp to retrieve
	 * @return Cursor positioned to matching vp, if found
	 * @throws SQLException
	 *             if vp could not be found/retrieved
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

	public Cursor fetchAssignedContactByContactRowId(long contactRowId)
			throws SQLException {
		Cursor mCursor =

		database.query(true, AC_TABLE_NAME, new String[] { AC_KEY_ROWID,
				AC_KEY_CONTACT, AC_KEY_VP_ROWID }, AC_KEY_CONTACT + "="
				+ contactRowId, null, null, null, null, null);

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

	public static long createVibrationPattern(String name, String pattern,
			SQLiteDatabase database) {
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

	public String getVibrationPatternFromNumber(String phoneNumber)
			throws NumberNotKnownException {
		long id;
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri
				.encode(phoneNumber));
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] {
				PhoneLookup.DISPLAY_NAME, PhoneLookup._ID }, null, null, null);
		if (cursor.moveToFirst()) {
			id = cursor.getLong(cursor.getColumnIndex(PhoneLookup._ID));
		} else {
			// Number not in contacts
			throw new NumberNotKnownException();
		}
		DatabaseManager db = new DatabaseManager(context).open();
		try {
			Cursor assignedContact = db.fetchAssignedContactByContactRowId(id);
			long vp_rowid = assignedContact.getLong(assignedContact
					.getColumnIndex(DatabaseManager.AC_KEY_VP_ROWID));
			Cursor vp_cur = db.fetchVibrationPattern(vp_rowid);
			String vp = vp_cur.getString(vp_cur
					.getColumnIndex(DatabaseManager.VP_KEY_ROWID));
		} catch (SQLException e) {
			// Contact not assigned
			throw new NumberNotKnownException();
		} finally {
			db.close();
		}
		return vp;
	}
}