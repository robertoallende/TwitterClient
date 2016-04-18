package com.robertoallende.twitterclient.models;


import com.robertoallende.twitterclient.dao.TweetDao;
import com.robertoallende.twitterclient.entities.Tweet;

import java.util.Collection;

import de.greenrobot.dao.query.LazyList;

public class TweetModel {
    private static TweetModel instance;
    private TweetDao tweetDao;

    public synchronized static TweetModel getInstance() {
        if(instance == null) {
            instance = new TweetModel();
        }
        return instance;
    }

    private TweetModel() {
        tweetDao = DbHelper.getInstance().getDaoSession().getTweetDao();
    }

    public Tweet getLastTweet() {
        return tweetDao.queryBuilder().where(TweetDao.Properties.ServerId.isNotNull())
                .orderDesc(TweetDao.Properties.CreatedAt)
                .limit(1).unique();
    }

    public void insertOrReplace(Tweet tweet) {
        tweetDao.insertOrReplace(tweet);
    }

    public void insertOrReplaceAll(Collection<Tweet> tweets) {
        tweetDao.insertOrReplaceInTx(tweets);
    }

    public LazyList<Tweet> lazyLoadTweets() {
        return tweetDao.queryBuilder().orderDesc(TweetDao.Properties.IsLocal, TweetDao.Properties.CreatedAt).listLazy();

    }

    public Tweet getTweetByLocalId(long localId) {
        return tweetDao.queryBuilder().where(TweetDao.Properties.LocalId.eq(localId)).limit(1).unique();
    }

    public void deleteTweetById(long localId) {
        tweetDao.deleteByKey(localId);
    }
}
