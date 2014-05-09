package com.theappbusiness.whoswho;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.theappbusiness.whoswho.asyncs.AsyncImportEmployees;
import com.theappbusiness.whoswho.asyncs.AsyncImportEmployees.CallbackEmployees;
import com.theappbusiness.whoswho.asyncs.AsyncParseWebsite;
import com.theappbusiness.whoswho.asyncs.AsyncParseWebsite.CallbackParse;
import com.theappbusiness.whoswho.fragments.MainFragment;

/**
 * 
 * @author lucas
 *
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_main);
		
		setSupportProgressBarIndeterminateVisibility(false);
		
		/*
		 * Prevent duplicate calls on rotation
		 */
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).commit();
			syncEmployees();
		}
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		/*
		 * Sync employees on reload
		 */
		case R.id.action_reload:				
			syncEmployees();
			break;
		}		
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method make the Async Call for parsing website and insert employees on data base.
	 */
	protected void syncEmployees() {
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if ( ! (netInfo != null && netInfo.isConnectedOrConnecting())) {
	    	return;
	    }
		
		setSupportProgressBarIndeterminateVisibility(true);
		
		CallbackParse callback = new CallbackParse() {
			
			@Override
			public void onFinish(ArrayList<ContentValues> values) {
				
				setSupportProgressBarIndeterminateVisibility(false);
				
				if (values == null) {
					return;
				}
				
				setSupportProgressBarIndeterminateVisibility(true);
				
				CallbackEmployees callback = new CallbackEmployees() {
					
					@Override
					public void onFinish(Boolean done) {
						if (done) {
							setSupportProgressBarIndeterminateVisibility(false);
						}
					}
				};
				AsyncImportEmployees sync = new AsyncImportEmployees(MainActivity.this, callback);
				sync.execute(values.toArray(new ContentValues[values.size()]));			
			}
		};
		
		AsyncParseWebsite asyncParse = new AsyncParseWebsite(callback);
		asyncParse.execute();
	}
}
