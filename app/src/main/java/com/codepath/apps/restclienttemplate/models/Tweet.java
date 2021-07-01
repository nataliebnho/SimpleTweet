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
    public boolean liked;

    // add:   "retweet_count": 161,
    //  "favorite_count": 296,
    //  "favorited": false,
    //  "retweeted": false,

    //empty constructor needed for Parceler library
    //also need to do the same for User class since Tweet uses User
    public Tweet() { }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        // Note: "text" and "created_at" are from the twitter JSON website
        // https://developer.twitter.com/en/docs/twitter-api/v1/tweets/timelines/api-reference/get-statuses-home_timeline
        tweet.body = jsonObject.getString("full_text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getString("id_str");
        tweet.favorite_count = jsonObject.getInt("favorite_count");
        tweet.favorited = jsonObject.getBoolean("favorited");

        tweet.retweet_count = jsonObject.getInt("retweet_count");

        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        if (jsonObject.getJSONObject("entities").has("media")) {
            tweet.mediaUrl = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
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

    // create new methods here "public void Like ", changing boolean value
    public void like(TwitterClient client) {
        client.likeTweet(getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("TWEET", "Tweet successfully liked");
                try {
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    //tweet.favorited = true;
                    //tweet.favorite_count += 1;
                    Log.i("TWEETDETAILSACTIVITY", "Number of likes: " + tweet.favorite_count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("TWEET", "Tweet not liked" + String.valueOf(getId()), throwable);
            }
        });

    }

    public void unlike(TwitterClient client) {
        client.unlikeTweet(getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("TWEET", "Tweet successfully unliked");
                try {
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    //tweet.favorited = false;
                    //tweet.favorite_count -= 1;
                    Log.i("TWEETDETAILSACTIVITY", "Number of likes: " + tweet.favorite_count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("TWEET", "Tweet not unliked" + String.valueOf(getId()), throwable);
            }
        });

    }



}
