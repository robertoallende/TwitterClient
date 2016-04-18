package com.robertoallende.twitterclient.jobs;


import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.robertoallende.twitterclient.controllers.TwitterController;
import com.robertoallende.twitterclient.entities.Tweet;
import com.robertoallende.twitterclient.events.FetchedNewTweetsEvent;
import com.robertoallende.twitterclient.models.TweetModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

public class FetchTweetsJob extends Job {

    public FetchTweetsJob() {
        //use singleWith so that if the same job has already been added and is not yet running,
        //it will only run once.
        super(new Params(Priority.LOW).requireNetwork().groupBy("fetch-tweets").singleInstanceBy("fetch-tweets"));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        TweetModel tweetModel = TweetModel.getInstance();
        Tweet lastTweet = tweetModel.getLastTweet();
        List<Status> statusList = TwitterController.getInstance().loadTweets(lastTweet == null ? null : lastTweet.getServerId());
        if(statusList.size() > 0) {
            List<Tweet> tweets = new ArrayList<Tweet>(statusList.size());
            for(Status status : statusList) {
                Tweet tweet = new Tweet(status);
                tweets.add(tweet);
            }
            tweetModel.insertOrReplaceAll(tweets);
            EventBus.getDefault().post(new FetchedNewTweetsEvent());
        }
    }

    @Override
    protected void onCancel(@CancelReason int cancelReason) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                     int maxRunCount) {
        if(throwable instanceof TwitterException) {
            //if it is a 4xx error, stop
            TwitterException twitterException = (TwitterException) throwable;
            int errorCode = twitterException.getErrorCode();
            return errorCode < 400 || errorCode > 499 ? RetryConstraint.RETRY : RetryConstraint.CANCEL;
        }
        return RetryConstraint.RETRY;
    }
}
