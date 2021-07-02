package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class tweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    int position;

    TextView tvUserName;
    TextView tvUserHandle;
    TextView tvTweetContent;
    ImageView ivProfile;
    ImageView ivTweetMedia;

    TextView tvLikeCount;
    TextView tvRetweetCount;

    TwitterClient client;

    Button ibLike;
    Button ibRetweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserHandle = findViewById(R.id.tvUserHandle);
        tvTweetContent = findViewById(R.id.tvTweetContent);
        ivProfile = findViewById(R.id.ivProfile);
        ivTweetMedia = findViewById(R.id.ivTweetMedia);
        ibLike = findViewById(R.id.ibLike);
        ibRetweet = findViewById(R.id.btnRetweetDetail);

        tvLikeCount = findViewById(R.id.tvLikes);
        tvRetweetCount = findViewById(R.id.tvRetweets);

        client = TwitterApp.getRestClient(this);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        Log.d("MovieDetailsActivity", String.format("Showing details for %s", tweet.getId()));

        tvUserName.setText(tweet.user.name);
        tvUserHandle.setText("@" + tweet.user.screenName);
        tvTweetContent.setText(tweet.body);
        tvLikeCount.setText(tweet.favorite_count + " likes");
        tvRetweetCount.setText(tweet.retweet_count + " retweets");

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfile);
        Glide.with(this).load(tweet.mediaUrl).into(ivTweetMedia);

        if(!tweet.favorited){
            ibLike.setBackground(getDrawable(R.drawable.ic_vector_heart_stroke));
        } else {
            ibLike.setBackground(getDrawable(R.drawable.ic_vector_heart));
        }

        if(!tweet.retweeted){
            ibRetweet.setBackground(getDrawable(R.drawable.ic_vector_retweet_stroke));
        } else {
            ibRetweet.setBackground(getDrawable(R.drawable.ic_vector_retweet));
        }

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tweet.favorited){
                    tweet.like(client);
                    tweet.favorited = true;
                    tweet.favorite_count += 1;
                    tvLikeCount.setText(tweet.favorite_count + " likes");
                }
                else {
                    tweet.unlike(client);
                    tweet.favorited = false;
                    tweet.favorite_count -= 1;
                    tvLikeCount.setText(tweet.favorite_count + " likes");
                }
                changeLikeButtons();
                return;
            }
        });


        ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tweet.retweeted){
                    tweet.retweet(client);
                    tweet.retweeted = true;
                    tweet.retweet_count += 1;
                    tvRetweetCount.setText(tweet.retweet_count + " retweets");
                }
                else {
                    tweet.unretweet(client);;
                    tweet.favorited = false;
                    tweet.favorite_count -= 1;
                    tvRetweetCount.setText(tweet.retweet_count + " retweets");
                }
                changeRetweetButtons();
                return;
            }
        });
        position = getIntent().getIntExtra("position", -1);
        Log.d("Position in detail: ", String.valueOf(position));
    }

    public void changeLikeButtons(){
        if (tweet.favorited){
            ibLike.setBackground(getDrawable(R.drawable.ic_vector_heart));
        } else {
            ibLike.setBackground(getDrawable(R.drawable.ic_vector_heart_stroke));
        }
    }

    public void changeRetweetButtons(){
        if (tweet.retweeted){
            ibRetweet.setBackground(getDrawable(R.drawable.ic_vector_retweet));
        } else {
            ibRetweet.setBackground(getDrawable(R.drawable.ic_vector_retweet_stroke));
        }
    }

    public void goToTimeline(){
        Intent i = new Intent();
        i.putExtra("position", position);
        i.putExtra("modifiedTweet", Parcels.wrap(tweet));
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToTimeline();
        super.onBackPressed();
    }
}