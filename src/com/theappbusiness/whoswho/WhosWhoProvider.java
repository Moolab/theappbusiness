/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.theappbusiness.whoswho;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.theappbusiness.whoswho.helper.DatabaseHelper;

/**
 * Provides access to a database of WhosWho.
 */
public class WhosWhoProvider extends ContentProvider {

	public static final String TAG = "WhosWhoProvider";

	public static final String QUERY_PARAMETER_LIMIT = "limit";

	private static HashMap<String, String> sProjectionMapBiographies;

	private static final int URI_MATCHES_BIOGRAPHIES = 1;
	private static final int URI_MATCHES_ID_BIOGRAPHIES = 2;	

	/**
	 * A UriMatcher instance
	 */
	private static final UriMatcher sUriMatcher;

	// Handle to a new DatabaseHelper.
	private DatabaseHelper mOpenHelper;

	/**
	 * A block that instantiates and sets static objects
	 */
	static {

		/*
		 * Creates and initializes the URI matcher
		 */
		// Create a new instance
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		String fatura = WhosWhoContract.Biographies.TABLE_NAME;
		sUriMatcher.addURI(WhosWhoContract.AUTHORITY, fatura, URI_MATCHES_BIOGRAPHIES);
		sUriMatcher.addURI(WhosWhoContract.AUTHORITY, fatura + "/#", URI_MATCHES_ID_BIOGRAPHIES);

		/*
		 * Creates and initializes a projection map that returns all columns
		 */
		sProjectionMapBiographies = new HashMap<String, String>();
		sProjectionMapBiographies.put(WhosWhoContract.Biographies._ID, WhosWhoContract.Biographies._ID);
		sProjectionMapBiographies.put(WhosWhoContract.Biographies.COLUMN_NAME_BIO, WhosWhoContract.Biographies.COLUMN_NAME_BIO);
		sProjectionMapBiographies.put(WhosWhoContract.Biographies.COLUMN_NAME_NUM, WhosWhoContract.Biographies.COLUMN_NAME_NUM);
		sProjectionMapBiographies.put(WhosWhoContract.Biographies.COLUMN_NAME_NAME, WhosWhoContract.Biographies.COLUMN_NAME_NAME);
		sProjectionMapBiographies.put(WhosWhoContract.Biographies.COLUMN_NAME_PHOTO, WhosWhoContract.Biographies.COLUMN_NAME_PHOTO);
		sProjectionMapBiographies.put(WhosWhoContract.Biographies.COLUMN_NAME_TITLE, WhosWhoContract.Biographies.COLUMN_NAME_TITLE);
	}

	/**
	 * 
	 * Initializes the provider by creating a new DatabaseHelper. onCreate() is
	 * called automatically when Android creates the provider in response to a
	 * resolver request from a client.
	 */
	@Override
	public boolean onCreate() {

		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	/**
	 * This method is called when a client calls
	 * {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)}
	 * . Queries the database and returns a cursor containing the results.
	 * 
	 * @return A cursor containing the results of the query. The cursor exists
	 *         but is empty if the query returns no results or an exception
	 *         occurs.
	 * @throws IllegalArgumentException
	 *             if the incoming URI pattern is invalid.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		Cursor c = null;

		switch (sUriMatcher.match(uri)) {
		case URI_MATCHES_BIOGRAPHIES:
		case URI_MATCHES_ID_BIOGRAPHIES:
			c = queryBiographies(uri, projection, selection, selectionArgs, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return c;
	}
	
	/**
	 * Query for biographies.
	 * 
	 * @param uri
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	private Cursor queryBiographies(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		
		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(WhosWhoContract.Biographies.TABLE_NAME);

		/**
		 * Choose the projection and adjust the "where" clause based on URI
		 * pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case URI_MATCHES_BIOGRAPHIES:
			qb.setProjectionMap(sProjectionMapBiographies);
			break;

			/*
			 * If the incoming URI is for a single note identified by its ID,
			 * chooses the note ID projection, and appends "_ID = <noteID>" to the
			 * where clause, so that it selects that single note
			 */
		case URI_MATCHES_ID_BIOGRAPHIES:
			qb.setProjectionMap(sProjectionMapBiographies);
			qb.appendWhere(WhosWhoContract.Biographies._ID + "=" + uri.getPathSegments().get(WhosWhoContract.Biographies.ID_PATH_POSITION));
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		String orderBy;
		// If no sort order is specified, uses the default
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = WhosWhoContract.Biographies.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		/*
		 * Performs the query. If no problems occur trying to read the database,
		 * then a Cursor object is returned; otherwise, the cursor variable
		 * contains null. If no records were selected, then the Cursor object is
		 * empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = qb.query(db, // The database to query
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				orderBy // The sort order
				);

		// Tells the Cursor what URI to watch, so it knows when its source data
		// changes.
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#getType(Uri)}. Returns the MIME
	 * data type of the URI given as a parameter.
	 * 
	 * @param uri
	 *            The URI whose MIME type is desired.
	 * @return The MIME type of the URI.
	 * @throws IllegalArgumentException
	 *             if the incoming URI pattern is invalid.
	 */
	@Override
	public String getType(Uri uri) {

		String type = null;

		switch (sUriMatcher.match(uri)) {
		case URI_MATCHES_BIOGRAPHIES:
		case URI_MATCHES_ID_BIOGRAPHIES:
			type = getTypeBiographies(uri);			
			break;
		}

		return type;
	}

	private String getTypeBiographies(Uri uri) {

		/**
		 * Chooses the MIME type based on the incoming URI pattern
		 */
		switch (sUriMatcher.match(uri)) {

		// If the pattern is for notes or live folders, returns the general
		// content type.
		case URI_MATCHES_BIOGRAPHIES:
			// case URI_MATCHES_LIVE_FOLDER_INGREDIENTS:
			return WhosWhoContract.Biographies.CONTENT_TYPE;

			// If the pattern is for note IDs, returns the note ID content type.
		case URI_MATCHES_ID_BIOGRAPHIES:
			return WhosWhoContract.Biographies.CONTENT_ITEM_TYPE;

			// If the URI pattern doesn't match any permitted patterns, throws
			// an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
	
	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#insert(Uri, ContentValues)}.
	 * Inserts a new row into the database. This method sets up default values
	 * for any columns that are not included in the incoming map. If rows were
	 * inserted, then listeners are notified of the change.
	 * 
	 * @return The row ID of the inserted row.
	 * @throws SQLException
	 *             if the insertion fails.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {

		Uri noteUri = null;

		switch (sUriMatcher.match(uri)) {
		case URI_MATCHES_BIOGRAPHIES:
		case URI_MATCHES_ID_BIOGRAPHIES:
			noteUri = insertBiographies(uri, initialValues);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return noteUri;
	}

	private Uri insertBiographies(Uri uri, ContentValues initialValues) {

		ContentValues values;

		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		long rowId = db.insert(WhosWhoContract.Biographies.TABLE_NAME, WhosWhoContract.Biographies._ID, values);

		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(WhosWhoContract.Biographies.CONTENT_ID_URI_BASE, rowId);

			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#delete(Uri, String, String[])}.
	 * Deletes records from the database. If the incoming URI matches the note
	 * ID URI pattern, this method deletes the one record specified by the ID in
	 * the URI. Otherwise, it deletes a a set of records. The record or records
	 * must also match the input selection criteria specified by where and
	 * whereArgs.
	 * 
	 * If rows were deleted, then listeners are notified of the change.
	 * 
	 * @return If a "where" clause is used, the number of rows affected is
	 *         returned, otherwise 0 is returned. To delete all rows and get a
	 *         row count, use "1" as the where clause.
	 * @throws IllegalArgumentException
	 *             if the incoming URI pattern is invalid.
	 */
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String finalWhere;

		int count;

		switch (sUriMatcher.match(uri)) {
		case URI_MATCHES_BIOGRAPHIES:
			count = db.delete(WhosWhoContract.Biographies.TABLE_NAME, where, whereArgs);
			break;
		case URI_MATCHES_ID_BIOGRAPHIES:
			finalWhere = WhosWhoContract.Biographies._ID + " = " + uri.getPathSegments().get(WhosWhoContract.Biographies.ID_PATH_POSITION);
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}
			count = db.delete(WhosWhoContract.Biographies.TABLE_NAME, finalWhere, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#update(Uri,ContentValues,String,String[])}
	 * Updates records in the database. The column names specified by the keys
	 * in the values map are updated with new data specified by the values in
	 * the map. If the incoming URI matches the note ID URI pattern, then the
	 * method updates the one record specified by the ID in the URI; otherwise,
	 * it updates a set of records. The record or records must match the input
	 * selection criteria specified by where and whereArgs. If rows were
	 * updated, then listeners are notified of the change.
	 * 
	 * @param uri
	 *            The URI pattern to match and update.
	 * @param values
	 *            A map of column names (keys) and new values (values).
	 * @param where
	 *            An SQL "WHERE" clause that selects records based on their
	 *            column values. If this is null, then all records that match
	 *            the URI pattern are selected.
	 * @param whereArgs
	 *            An array of selection criteria. If the "where" param contains
	 *            value placeholders ("?"), then each placeholder is replaced by
	 *            the corresponding element in the array.
	 * @return The number of rows updated.
	 * @throws IllegalArgumentException
	 *             if the incoming URI pattern is invalid.
	 */
	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		String finalWhere;

		switch (sUriMatcher.match(uri)) {
		case URI_MATCHES_BIOGRAPHIES:
			count = db.update(WhosWhoContract.Biographies.TABLE_NAME, values, where, whereArgs);
			break;
		case URI_MATCHES_ID_BIOGRAPHIES:
			finalWhere = WhosWhoContract.Biographies._ID + " = " + uri.getPathSegments().get(WhosWhoContract.Biographies.ID_PATH_POSITION);
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}
			count = db.update(WhosWhoContract.Biographies.TABLE_NAME, values, finalWhere, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	/**
	 * A test package can call this to get a handle to the database underlying
	 * NotePadProvider, so it can insert test data into the database. The test
	 * case class is responsible for instantiating the provider in a test
	 * context; {@link android.test.ProviderTestCase2} does this during the call
	 * to setUp()
	 * 
	 * @return a handle to the database helper object for the provider's data.
	 */
	DatabaseHelper getOpenHelperForTest() {
		return mOpenHelper;
	}
}
