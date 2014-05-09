package com.theappbusiness.whoswho.asyncs;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.theappbusiness.whoswho.helper.ParseWebsiteHelper;

/**
 * 
 * @author lucas
 *
 */
public class AsyncParseWebsite extends AsyncTask<String, Void, ArrayList<ContentValues>> {

	public interface CallbackParse {
		public void onFinish(ArrayList<ContentValues> values);
	}

	private final CallbackParse mCallback;

	public AsyncParseWebsite(CallbackParse callback) {
		super();
		mCallback = callback;
	}

	@Override
	protected ArrayList<ContentValues> doInBackground(String... args) {
		
		HttpResponse response = null;
        HttpGet httpGet = null;
        HttpClient mHttpClient = null;
        String html = null;

        try {
            if(mHttpClient == null){
                mHttpClient = new DefaultHttpClient();
            }
            httpGet = new HttpGet("http://www.theappbusiness.com/our-team/");
            response = mHttpClient.execute(httpGet);
            html = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } 		
		
		
		ParseWebsiteHelper bio = new ParseWebsiteHelper();
		bio.setHtml(html);
		ArrayList<ContentValues> values =  bio.parseBio();
		return values;
	}

	@Override
	protected void onPostExecute(ArrayList<ContentValues> values) {
		mCallback.onFinish(values);
	}
}

