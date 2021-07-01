package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.like.LikeButton;

import org.parceler.Parcels;

public class tweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;

    TextView tvUserName;
    TextView tvUserHandle;
    TextView tvTweetContent;
    ImageView ivProfile;
    ImageView ivTweetMedia;

    TextView tvLikeCount;
    TextView tvRetweetCount;

    LikeButton likeButton;

    TwitterClient client;

    Button ibLike;
    int favorite_count;
    boolean internal_favorite;

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

        tvLikeCount = findViewById(R.id.tvLikes);
        tvRetweetCount = findViewById(R.id.tvRetweets);

        client = TwitterApp.getRestClient(this);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        Log.d("MovieDetailsActivity", String.format("Showing details for %s", tweet.getId()));

        tvUserName.setText(tweet.user.name);
        tvUserHandle.setText("@" + tweet.user.screenName);
        tvTweetContent.setText(tweet.body);
        tvLikeCount.setText(tweet.favorite_count + " likes");
        tvRetweetCount.setText(tweet.retweet_count + " retweets");

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfile);
        Glide.with(this).load(tweet.mediaUrl).into(ivTweetMedia);

        if(tweet.favorited == false){
            ibLike.setBackground(getDrawable(R.drawable.ic_vector_heart_stroke));
        } else {
            ibLike.setBackground(getDrawable(R.drawable.ic_vector_heart));
        }

        // -----------------------------------------------

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tweet.favorited == false){
                    //call to unlike
                    tweet.like(client);
//                  //update favorite status
                    tweet.favorited = true;
                    //update count of likes
                    //tweet.favorite_count += 1;
                    tvLikeCount.setText((tweet.favorite_count + 1) + " likes");
                    //change Buttons
                    ibLike.setBackground(getDrawable(R.drawable.ic_vector_heart));
                }
                else {
                    // Call to unlike
                    tweet.unlike(client);
                    // Update favorite status
                    tweet.favorited = false;
                    // updates count of likes
                    //tweet.favorite_count -= 1;
                    tvLikeCount.setText((tweet.favorite_count -1) + " likes");
                    // changebuttons
                    ibLike.setBackground(getDrawable(R.drawable.ic_vector_heart_stroke));
                }
            }
        });

        // -----------------------------------------------

    }

    public void changeButtons(){
        // if like is ture

        // else
    }
}