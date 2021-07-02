package com.codepath.apps.restclienttemplate.models;

import android.content.Context;
import android.util.Log;

import androidx.versionedparcelable.ParcelField;

import com.codepath.apps.restclienttemplate.TimelineActivity;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String mediaUrl;
    public String id;
    public int favorite_count;
    public int retweet_count;
    public boolean favorited;
    public boolean retweeted;
    public boolean liked;
    public String TAG = "TWEET";

    //empty constructor needed for Parceler library
    public Tweet() { }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // check if tweet is an extended version (New Twitter API allows for > 140 characters
        if (jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }

        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getString("id_str");

        tweet.favorite_count = jsonObject.getInt("favorite_count");
        tweet.favorited = jsonObject.getBoolean("favorited");

        tweet.retweet_count = jsonObject.getInt("retweet_count");
        tweet.retweeted = jsonObject.getBoolean("retweeted");

        if (jsonObject.getJSONObject("entities").has("media")) {
            tweet.mediaUrl = jsonObject.getJSONObject("entities").getJSONArray("media")
                    .getJSONObject(0).getString("media_url_https");
        }
        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getId() {
        return id;
    }

    public void like(TwitterClient client) {
        client.likeTweet(getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("TWEET", "Tweet successfully liked");
                try {
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    Log.i(TAG, "Number of likes: " + tweet.favorite_count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Tweet not liked" + String.valueOf(getId()), throwable);
            }
        });
    }

    public void unlike(TwitterClient client) {
        client.unlikeTweet(getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Tweet successfully unliked");
                try {
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    Log.i(TAG, "Number of likes: " + tweet.favorite_count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Tweet not unliked" + String.valueOf(getId()), throwable);
            }
        });

    }

    public void retweet(TwitterClient client) {
        client.retweet(getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("TWEET", "Tweet successfully retweeted");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Tweet not retweeted: " + String.valueOf(getId()), throwable);
            }
        });
    }

    public void unretweet(TwitterClient client) {
        client.unretweet(getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("TWEET", "Tweet successfully unretweeted");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Tweet not unretweeted: " + String.valueOf(getId()), throwable);
            }
        });
    }





}
