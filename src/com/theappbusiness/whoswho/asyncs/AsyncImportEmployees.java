package com.theappbusiness.whoswho.asyncs;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.theappbusiness.whoswho.WhosWhoContract;

/**
 * 
 * @author lucas
 *
 */
public class AsyncImportEmployees extends AsyncTask<ContentValues, Void, Boolean> {

	public interface CallbackEmployees {
		public void onFinish(Boolean done);
	}

	private static final String TAG = "Import Employees";

	private final Context mContext;
	private final CallbackEmployees mCallback;

	public AsyncImportEmployees(final Context context, CallbackEmployees callback) {
		super();
		mContext = context;
		mCallback = callback;
	}

	@Override
	protected Boolean doInBackground(ContentValues... employees) {
		
		if (employees == null || employees.length == 0) {
			return false;
		}
		
		/**
		 * Also because can be a lot of employees, withYieldAllowed improve the performance.
		 */
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		
		/**
		 * Delete all. I dont know what info can be use to ID in each employee to update or delete.
		 * If some day the app receive a push notification, we can improve it. Passing the ID.
		 * 
		 * ATTENTION: This can cause: go to top at the list of employees. But I do not had the idea to work around.
		 * Solution can be delete just the employee that is not in the list of values, but I do not have a ID. =(
		 */
		operations.add(ContentProviderOperation.newDelete(WhosWhoContract.Biographies.CONTENT_URI).withYieldAllowed(true).build());
		
		/**
		 * Insert or Reinsert the employees.
		 */
		for (ContentValues employee : employees) {
			operations.add(ContentProviderOperation.newInsert(WhosWhoContract.Biographies.CONTENT_URI).withValues(employee).withYieldAllowed(true).build());
		}
				
		try {
			/**
			 * Apply batch of operations.
			 */
			mContext.getContentResolver().applyBatch(WhosWhoContract.AUTHORITY, operations);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return false;
		}		
	}

	@Override
	protected void onPostExecute(Boolean done) {
		mCallback.onFinish(done);
	}
}

