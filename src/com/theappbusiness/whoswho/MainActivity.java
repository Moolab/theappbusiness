package com.theappbusiness.whoswho;

import java.util.ArrayList;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.theappbusiness.whoswho.asyncs.AsyncSyncEmplyees;
import com.theappbusiness.whoswho.asyncs.AsyncSyncEmplyees.Callback;
import com.theappbusiness.whoswho.fragments.MainFragment;
import com.theappbusiness.whoswho.provider.AccessBioProvider;
import com.theappbusiness.whoswho.provider.AccessBioProviderTest;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).commit();
		}
		
		syncEmployees();
	}

	protected void syncEmployees() {
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
					Toast.makeText(MainActivity.this, getString(R.string.sync_finish), Toast.LENGTH_SHORT).show();
				}
			}
		};
		AsyncSyncEmplyees sync = new AsyncSyncEmplyees(this, callback);
		sync.execute(values.toArray(new ContentValues[values.size()]));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
