package com.theappbusiness.whoswho.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.theappbusiness.whoswho.asyncs.AsyncSyncEmplyees;
import com.theappbusiness.whoswho.asyncs.AsyncSyncEmplyees.Callback;
import com.theappbusiness.whoswho.provider.AccessBioProvider;
import com.theappbusiness.whoswho.provider.AccessBioProviderTest;

public class EmployeesReceiver extends BroadcastReceiver {
	
	private static final String TAG = "EmployeesReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
				
		/*
		 * Improvement to receive push.
		 */
		
		/*
		 * Sync Employees 
		 */
		AccessBioProvider bio = new AccessBioProvider();
		bio.setHtml(AccessBioProviderTest.HTML);
		ArrayList<ContentValues> values =  bio.parseBio();
		
		if (values == null) {
			return;
		}
		Callback callback = new Callback() {
			
			@Override
			public void onFinish(Boolean done) {
				if (done) {
					
				}
			}
		};
		AsyncSyncEmplyees sync = new AsyncSyncEmplyees(context, callback);
		sync.execute(values.toArray(new ContentValues[values.size()]));		
	}
}