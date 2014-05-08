package com.theappbusiness.whoswho.asyncs;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.theappbusiness.whoswho.WhosWhoContract;

public class AsyncSyncEmplyees extends AsyncTask<ContentValues, Void, Boolean> {

	public interface Callback {
		public void onFinish(Boolean done);
	}

	private final Context mContext;
	private final Callback mCallback;

	public AsyncSyncEmplyees(final Context context, Callback callback) {
		super();
		mContext = context;
		mCallback = callback;
	}

	@Override
	protected Boolean doInBackground(ContentValues... employees) {

		if (employees[0] == null) {
			return false;
		}
		
		mContext.getContentResolver().delete(WhosWhoContract.Biographies.CONTENT_URI, null, null);
		for (ContentValues employee : employees) {
			mContext.getContentResolver().insert(WhosWhoContract.Biographies.CONTENT_URI, employee);
		}
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean done) {
		mCallback.onFinish(done);
	}
}

