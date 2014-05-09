package com.theappbusiness.whoswho.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.theappbusiness.whoswho.WhosWhoContract;

/**
 * @author lucas
 * 
 * This class helps open, create, and upgrade the database file. Set to package
 * visibility for testing purposes.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * Database creation SQL statement
	 */
	private static final String DATABASE_CREATE = "create table " + WhosWhoContract.Biographies.TABLE_NAME + " (" 
	 + WhosWhoContract.Biographies._ID + " integer primary key autoincrement, " 
	 + WhosWhoContract.Biographies.COLUMN_NAME_BIO + " text not null, "
	 + WhosWhoContract.Biographies.COLUMN_NAME_NAME + " text not null, "
	 + WhosWhoContract.Biographies.COLUMN_NAME_TITLE + " text not null, "
	 + WhosWhoContract.Biographies.COLUMN_NAME_PHOTO + " text not null, "
	 + WhosWhoContract.Biographies.COLUMN_NAME_NUM + " integer not null"
	 + ");";
	
	private static final String DATABASE_NAME = "theappbusiness";
	private static final int DATABASE_VERSION = 1;

	private static final String TAG = "DatabaseHelper";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
	}
}