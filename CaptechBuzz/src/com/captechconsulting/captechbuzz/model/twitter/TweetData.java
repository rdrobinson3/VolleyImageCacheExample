package com.captechconsulting.captechbuzz.model.twitter;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TweetData {
	@SerializedName("completed_in")
	private double completedIn;
	
	@SerializedName("max_id")
	private long maxId;
	
	@SerializedName("max_id_str")
	private String maxIdStr;
	
	@SerializedName("next_page")
	private String nextPage;
	
	@SerializedName("page")
	private int page;
	
	@SerializedName("query")
	private String query;
	
	@SerializedName("refresh_url")
	private String refresh_url;
	
	@SerializedName("results")
	private List<Tweet> tweets = new ArrayList<Tweet>();
	
	public TweetData(double completedIn, long maxId, String maxIdStr,
			String nextPage, int page, String query, String refresh_url,
			List<Tweet> tweets) {
		super();
		this.completedIn = completedIn;
		this.maxId = maxId;
		this.maxIdStr = maxIdStr;
		this.nextPage = nextPage;
		this.page = page;
		this.query = query;
		this.refresh_url = refresh_url;
		this.tweets = tweets;
	}

	public double getCompletedIn() {
		return completedIn;
	}

	public long getMaxId() {
		return maxId;
	}

	public String getMaxIdStr() {
		return maxIdStr;
	}

	public String getNextPage() {
		return nextPage;
	}

	public int getPage() {
		return page;
	}

	public String getQuery() {
		return query;
	}

	public String getRefresh_url() {
		return refresh_url;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

    
    
}
