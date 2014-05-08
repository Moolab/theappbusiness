package com.theappbusiness.whoswho;

import java.util.ArrayList;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.theappbusiness.whoswho.asyncs.AsyncParseWebsite;
import com.theappbusiness.whoswho.asyncs.AsyncParseWebsite.CallbackParse;
import com.theappbusiness.whoswho.asyncs.AsyncSyncEmployees;
import com.theappbusiness.whoswho.asyncs.AsyncSyncEmployees.CallbackEmployees;
import com.theappbusiness.whoswho.fragments.MainFragment;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_main);
		
		setSupportProgressBarIndeterminateVisibility(false);
		
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
		case R.id.action_reload:		
			syncEmployees();
			break;
		}		
		
		return super.onOptionsItemSelected(item);
	}

	protected void syncEmployees() {
		setSupportProgressBarIndeterminateVisibility(true);
		
		CallbackParse callback = new CallbackParse() {
			
			@Override
			public void onFinish(ArrayList<ContentValues> values) {
				if (values == null) {
					return;
				}
				
				CallbackEmployees callback = new CallbackEmployees() {
					
					@Override
					public void onFinish(Boolean done) {
						if (done) {
							setSupportProgressBarIndeterminateVisibility(false);
						}
					}
				};
				AsyncSyncEmployees sync = new AsyncSyncEmployees(MainActivity.this, callback);
				sync.execute(values.toArray(new ContentValues[values.size()]));			
			}
		};
		
		AsyncParseWebsite asyncParse = new AsyncParseWebsite(callback);
		asyncParse.execute();
	}
}
