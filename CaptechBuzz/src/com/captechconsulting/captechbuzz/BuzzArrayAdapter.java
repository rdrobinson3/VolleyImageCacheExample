package com.captechconsulting.captechbuzz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.captechconsulting.captechbuzz.model.images.ImageCacheManager;
import com.captechconsulting.captechbuzz.model.twitter.Tweet;
import com.captechconsulting.captechbuzz.model.twitter.TweetData;
import com.captechconsulting.captechbuzz.model.twitter.TwitterManager;

/**
 * 
 * @author Trey Robinson
 *
 */
public class BuzzArrayAdapter extends ArrayAdapter<String> implements Listener<TweetData>, ErrorListener {

	private final String TAG = getClass().getSimpleName();

	private static final SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy h:mm a", Locale.US);

	/**
	 *  The data that drives the adapter
	 */
	private final List<Tweet> mData;

	/**
	 * The last network response containing twitter metadata
	 */
	private TweetData mTweetData;

	private boolean isLoading;


	/**
	 * Flag telling us our last network call returned 0 results and we do not need to execute any new requests
	 */
	private boolean moreDataToLoad;

	/**
	 * @param context
	 * 			The context
	 * @param textViewResourceId
	 * 			Resource for the rows of the listview
	 * @param newData
	 * 			Initial dataset.
	 */
	public BuzzArrayAdapter(Context context, TweetData newData) {
		super(context, R.layout.tweet_list_item);
		mData = newData.getTweets();
		mTweetData = newData;

		moreDataToLoad = true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder viewHolder;

		//check to see if we need to load more data
		if(shouldLoadMoreData(mData, position) ) {
			loadMoreData();
		}

		if(v == null){
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.tweet_list_item, null);

			viewHolder = new ViewHolder();
			viewHolder.twitterUserImage = (NetworkImageView)v.findViewById(R.id.twitterUserImage);
			viewHolder.userNameTextView = (TextView)v.findViewById(R.id.usernameTextView);
			viewHolder.messageTextView = (TextView)v.findViewById(R.id.messageTextView);
			viewHolder.tweetTimeTextView = (TextView)v.findViewById(R.id.tweetTimeTextView);

			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		Tweet tweet = mData.get(position);
		if(tweet != null){
			viewHolder.twitterUserImage.setImageUrl(tweet.getUserImageUrl(), ImageCacheManager.getInstance().getImageLoader());
			viewHolder.userNameTextView.setText("@" + tweet.getUsername());
			viewHolder.messageTextView.setText(tweet.getMessage());
			viewHolder.tweetTimeTextView.setText(formatDisplayDate(tweet.getCreatedDate()));
			viewHolder.destinationUrl = tweet.getDestinationUrl();
		}

		return v;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	private String formatDisplayDate(Date date){
		if(date != null){
			return sdf.format(date);
		}

		return "";
	}
	/**
	 * Add the data to the current listview
	 * @param newData
	 * 			Data to be added to the listview
	 */
	public void add(List<Tweet> newData)
	{
		isLoading = false;
		if(!newData.isEmpty()){
			mData.addAll(newData);
			notifyDataSetChanged();
		}
	}

	/**
	 * a new request.
	 * @param data
	 * 			Current list of data
	 * @param position
	 * 			Current view position
	 * @return
	 */
	private boolean shouldLoadMoreData(List<Tweet> data, int position){
		// If showing the last set of data, request for the next set of data
		boolean scrollRangeReached = (position > (data.size() - TwitterManager.TWITTER_DEFAULT_PAGE_SIZE));
		return (scrollRangeReached && !isLoading && moreDataToLoad);
	}

	private void loadMoreData(){
		isLoading = true;
		Log.v(getClass().toString(), "Load more tweets");
		TwitterManager.getInstance().getDefaultHashtagTweets(this, this, null, mTweetData.getPage() + 1);
	}


	/**
	 * Viewholder for the listview row
	 * 
	 * @author Trey Robinson
	 *
	 */
	static class ViewHolder{
		NetworkImageView twitterUserImage;
		TextView userNameTextView;
		TextView messageTextView;
		TextView tweetTimeTextView;
		String destinationUrl;
	}

	@Override
	public void onResponse(TweetData response) {
		if(response != null){
			mData.addAll(response.getTweets());
			mTweetData = response;

			if(mTweetData.getTweets() != null && mTweetData.getTweets().size() > 0
					&& mTweetData.getNextPage() != null && !mTweetData.getNextPage().equals("")) {
				moreDataToLoad = true;
			}
			else {
				moreDataToLoad = false;
			}

			notifyDataSetChanged();
			Log.v(TAG, "New tweets retrieved");
		}

		isLoading = false;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.e(TAG, "Error retrieving additional tweets: " + error.getMessage());
		isLoading = false;
	}

}
