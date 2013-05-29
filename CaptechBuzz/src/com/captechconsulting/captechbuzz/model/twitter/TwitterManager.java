package com.captechconsulting.captechbuzz.model.twitter;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.captechconsulting.captechbuzz.model.GsonRequest;
import com.captechconsulting.captechbuzz.model.RequestManager;

public class TwitterManager {

	private final String TAG = getClass().getSimpleName();
	private static TwitterManager mInstance;

	private static String TWITTER_BASE = "http://search.twitter.com/search.json";
	private static String TWITTER_DEFAULT_SEARCH_TERM = "CapTech";
	private static String TWITTER_QUERY = "q";
	private static String TWITTER_INCLUDE_ENTITIES = "include_entities";
	private static String TWITTER_MAX_ID = "max_id";
	private static String TWITTER_RESULTS_PER_PAGE = "rpp";
	private static String TWITTER_PAGE_NUM = "page";
	public static int TWITTER_DEFAULT_PAGE_SIZE = 10;

	public static TwitterManager getInstance(){
		if(mInstance == null) {
			mInstance = new TwitterManager();
		}

		return mInstance;
	}

	public <T> void getDefaultHashtagTweets(Listener<TweetData> listener, ErrorListener errorListener, String maxId, int pageNum){
		getTweetForHashtag(listener, errorListener, maxId, TWITTER_DEFAULT_SEARCH_TERM, TWITTER_DEFAULT_PAGE_SIZE, pageNum);
	}

	public void getTweetForHashtag(Listener<TweetData> listener, ErrorListener errorListener, String maxId, String hashtag, int pageSize, int pageNum){

		Uri.Builder uriBuilder = Uri.parse(TWITTER_BASE).buildUpon()
				.appendQueryParameter(TWITTER_QUERY, hashtag)
				.appendQueryParameter(TWITTER_INCLUDE_ENTITIES, "true")
				.appendQueryParameter(TWITTER_RESULTS_PER_PAGE, "" + pageSize)
				.appendQueryParameter(TWITTER_PAGE_NUM, "" + pageNum);

		if(maxId != null) {
			uriBuilder.appendQueryParameter(TWITTER_MAX_ID, maxId);
		}

		String uri = uriBuilder.build().toString();
		Log.i(TAG, "getTweetForHashtag: uri = " + uri);

		GsonRequest<TweetData> request = new GsonRequest<TweetData>(Method.GET
				, uri
				, TweetData.class
				, listener
				, errorListener);

		Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}

}
