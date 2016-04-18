package com.robertoallende.twitterclient.events;

public class DeletedTweetEvent {
    private long id;
    public DeletedTweetEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
