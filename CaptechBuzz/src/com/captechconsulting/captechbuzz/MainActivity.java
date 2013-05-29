package com.captechconsulting.captechbuzz;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.captechconsulting.captechbuzz.BuzzArrayAdapter.ViewHolder;
import com.captechconsulting.captechbuzz.model.twitter.TweetData;
import com.captechconsulting.captechbuzz.model.twitter.TwitterManager;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	private final String TAG = getClass().getSimpleName(); 
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView)findViewById(R.id.buzzListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(mListView.getAdapter() == null)
			TwitterManager.getInstance().getDefaultHashtagTweets(createMyReqSuccessListener(), createMyReqErrorListener(), null);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ViewHolder holder = (ViewHolder)view.getTag();
				Log.v(TAG, "Go to url " + holder.destinationUrl);
				if(holder.destinationUrl != null){
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.destinationUrl));
					startActivity(intent);
				}
				
			}
		});
		
	}
	
	 private Listener<TweetData> createMyReqSuccessListener() {
	        return new Listener<TweetData>() {
	            @Override
	            public void onResponse(TweetData response) {
	            	Log.v(TAG, "Tweet data loaded");
	            	mListView.setAdapter(new BuzzArrayAdapter(MainActivity.this, response));
	            }
	        };
	 }
	 
	 private Response.ErrorListener createMyReqErrorListener() {
	        return new Response.ErrorListener() {
	            @Override
	            public void onErrorResponse(VolleyError error) {
	            	Log.e(TAG, "Tweet data failed to load");
	            }
	        };
	    }

}
