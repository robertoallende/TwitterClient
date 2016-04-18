package com.robertoallende.twitterclient.controllers;

import com.robertoallende.twitterclient.Config;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/*
    Code taken from:
    https://github.com/yigit/android-priority-jobqueue/blob/v2/examples/twitter/TwitterClient/src/com/birbit/android/jobqueue/examples/twitter/controllers/TwitterController.java
*/

public class TwitterController {
    private static TwitterController instance;
    private Twitter twitter;
    // in a real app, this would be saved on login and come from shared preferences on startup
    private final long userId = 1443060589;
    public static final int PAGE_LENGTH = 20;


    public synchronized static TwitterController getInstance() {
        if(instance == null) {
            instance = new TwitterController();
        }
        return instance;
    }

    public TwitterController() {
        twitter = new TwitterFactory(new ConfigurationBuilder()
                .setOAuthAccessToken(Config.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(Config.ACCESS_TOKEN_SECRET)
                .setDebugEnabled(true)
                .setOAuthConsumerKey(Config.CONSUMER_KEY)
                .setOAuthConsumerKey(Config.CONSUMER_SECRET)
                .build()).getSingleton();
        AccessToken accessToken = new AccessToken(Config.ACCESS_TOKEN, Config.ACCESS_TOKEN_SECRET);
        twitter.setOAuthConsumer(Config.CONSUMER_KEY, Config.CONSUMER_SECRET);
        twitter.setOAuthAccessToken(accessToken);
    }

    public List<Status> loadTweets(Long sinceId) throws TwitterException {
        Paging paging = new Paging();
        paging.setCount(PAGE_LENGTH);
        if(sinceId != null) {
            paging.setSinceId(sinceId);
        }
        return twitter.getHomeTimeline(paging);
    }

    public Status postTweet(String status) throws TwitterException {
        return twitter.updateStatus(status);
    }

    public long getUserId() throws TwitterException {
        return userId;
    }
}