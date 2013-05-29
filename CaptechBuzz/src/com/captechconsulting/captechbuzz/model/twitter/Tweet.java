package com.captechconsulting.captechbuzz.model.twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.annotations.SerializedName;

public class Tweet {

	private final static String TWEET_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(TWEET_DATE_FORMAT, Locale.US);
	static {
		sdf.setLenient(true);
	}

	@SerializedName("from_user")
	private final String username;


	@SerializedName("text")
	private final String message;

	@SerializedName("profile_image_url")
	private final String userImageUrl;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String tweetId;

	private Date createdDate;

	public Tweet(String username, String message, String userImageUrl){
		this.username = username;
		this.message = message;
		this.userImageUrl = userImageUrl;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getDestinationUrl() {
		return "https://twitter.com/" + username + "/status/" + tweetId;
	}

	public Date getCreatedDate(){
		if(createdDate == null){
			try {
				createdDate = sdf.parse(createdAt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return createdDate;

	}
}
