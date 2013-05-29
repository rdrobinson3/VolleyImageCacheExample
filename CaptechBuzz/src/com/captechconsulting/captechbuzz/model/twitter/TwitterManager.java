package com.captechconsulting.captechbuzz.model.twitter;

import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.captechconsulting.captechbuzz.model.GsonRequest;
import com.captechconsulting.captechbuzz.model.RequestManager;

public class TwitterManager {
	
	private final String TAG = getClass().getSimpleName();
	private static TwitterManager mInstance;
	
	private static String TWITTER_BASE = "http://search.twitter.com/search.json?q=";
	private static String TWITTER_DEFAULT_SEARCH_TERM = "CapTech"; 
	private static String TWITTER_INCLUDE_ENTITIES = "&include_entities=true";
	private static String TWITTER_MAX_ID = "&max_id=";
	private static String TWITTER_COUNT = "&rpp=";
	private static int TWITTER_DEFAULT_PAGE_SIZE = 15;
	
	public static TwitterManager getInstance(){
		if(mInstance == null)
			mInstance = new TwitterManager();
		
		return mInstance;
	}

	public <T> void getDefaultHashtagTweets(Listener<TweetData> listener, ErrorListener errorListener, String maxId){
		getTweetForHashtag(TWITTER_DEFAULT_SEARCH_TERM, listener, errorListener, maxId, TWITTER_DEFAULT_PAGE_SIZE);
	}
	
	public void getTweetForHashtag(String hashtag, Listener<TweetData> listener, ErrorListener errorListener, String maxId, int pageSize){
		
		StringBuilder sb = new StringBuilder().append(TWITTER_BASE)
								.append(hashtag).append(TWITTER_INCLUDE_ENTITIES)
								.append(TWITTER_COUNT).append(pageSize);
		
		if(maxId != null)
			sb.append(TWITTER_MAX_ID).append(maxId);
		
		GsonRequest<TweetData> request = new GsonRequest<TweetData>(Method.GET
				, sb.toString()
				, TweetData.class
				, listener
				, errorListener);
		
		Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}
	
}
