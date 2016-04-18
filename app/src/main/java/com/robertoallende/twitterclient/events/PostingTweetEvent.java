package com.robertoallende.twitterclient.events;

import com.robertoallende.twitterclient.entities.Tweet;

public class PostingTweetEvent {
    private Tweet tweeet;

    public PostingTweetEvent(Tweet tweeet) {
        this.tweeet = tweeet;
    }

    public Tweet getTweeet() {
        return tweeet;
    }
}
