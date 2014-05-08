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

import android.net.Uri;
import android.provider.BaseColumns;

public final class WhosWhoContract {

	public static final String AUTHORITY = "com.theappbusiness.provider.WhosWho";

	// This class cannot be instantiated
	private WhosWhoContract() {
	}

	public static final class Biographies implements BaseColumns {

		// This class cannot be instantiated
		private Biographies() {
		}

		/**
		 * The table name offered by this provider
		 */
		public static final String TABLE_NAME = "biographies";

		/*
		 * URI definitions
		 */

		/**
		 * The scheme part for this provider's URI
		 */
		private static final String SCHEME = "content://";

		/**
		 * Path parts for the URIs
		 */

		/**
		 * Path part for the Ingredients URI
		 */
		private static final String PATH = "/" + TABLE_NAME;

		/**
		 * Path part for the Ingredients ID URI
		 */
		private static final String PATH_ID = "/" + TABLE_NAME + "/";

		/**
		 * 0-relative position of a note ID segment in the path part of a note
		 * ID URI
		 */
		public static final int ID_PATH_POSITION = 1;

		/**
		 * Path part for the Live Folder URI
		 */
		private static final String PATH_LIVE_FOLDER = "/live_folders/" + TABLE_NAME;

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);

		/**
		 * The content URI base for a single account. Callers must append a
		 * numeric account id to this Uri to retrieve a note
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ID);

		/**
		 * The content URI match pattern for a single account, specified by its
		 * ID. Use this to match incoming URIs or to construct an Intent.
		 */
		public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_ID + "/#");

		/**
		 * The content Uri pattern for a notes listing for live folders
		 */
		public static final Uri LIVE_FOLDER_URI = Uri.parse(SCHEME + AUTHORITY + PATH_LIVE_FOLDER);

		/*
		 * MIME type definitions
		 */

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of
		 * accounts.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.theappbusiness." + TABLE_NAME;

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.theappbusiness." + TABLE_NAME;

		public static final String COLUMN_NAME_NUM = "num";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_BIO = "biography";
		public static final String COLUMN_NAME_PHOTO = "photo";

		public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_NUM + " ASC";
	}
}