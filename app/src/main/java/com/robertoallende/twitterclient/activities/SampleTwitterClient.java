package com.robertoallende.twitterclient.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;
import com.robertoallende.twitterclient.R;
import com.robertoallende.twitterclient.TwitterApplication;
import com.robertoallende.twitterclient.adapters.LazyListAdapter;
import com.robertoallende.twitterclient.entities.Tweet;
import com.robertoallende.twitterclient.events.DeletedTweetEvent;
import com.robertoallende.twitterclient.events.FetchedNewTweetsEvent;
import com.robertoallende.twitterclient.events.PostedTweetEvent;
import com.robertoallende.twitterclient.events.PostingTweetEvent;
import com.robertoallende.twitterclient.jobs.FetchTweetsJob;
import com.robertoallende.twitterclient.jobs.PostTweetJob;
import com.robertoallende.twitterclient.models.TweetModel;
import com.robertoallende.twitterclient.tasks.SimpleBackgroundTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.greenrobot.dao.query.LazyList;

public class SampleTwitterClient extends BaseActivity {
    private TweetAdapter tweetAdapter;
    private boolean dataDirty = true;
    JobManager jobManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDirty = true;
        setContentView(R.layout.main);
        jobManager = TwitterApplication.getInstance().getJobManager();
        ListView listView = (ListView) findViewById(R.id.tweet_list);
        tweetAdapter = new TweetAdapter(getLayoutInflater());
        findViewById(R.id.send_tweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText) findViewById(R.id.edit_status);
                if(text.getText().toString().trim().length() > 0) {
                    sendTweet(text.getText().toString());
                    text.setText("");
                }
            }
        });
        listView.setAdapter(tweetAdapter);
        EventBus.getDefault().register(this);
    }

    private void sendTweet(final String text) {
        jobManager.addJobInBackground(new PostTweetJob(text));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t){
            //this may crash if registration did not go through. just be safe
        }
    }

    @Subscribe
    public void onEventMainThread(FetchedNewTweetsEvent ignored) {
        onUpdateEvent();
    }

    @Subscribe
    public void onEventMainThread(PostingTweetEvent ignored) {
        //we could just add this to top or replace element instead of refreshing whole list
        onUpdateEvent();
    }

    @Subscribe
    public void onEventMainThread(PostedTweetEvent ignored) {
        //we could just add this to top or replace element instead of refreshing whole list
        onUpdateEvent();
    }

    @Subscribe
    public void onEventMainThread(DeletedTweetEvent ignored) {
        //we could just add this to top or replace element instead of refreshing whole list
        Toast.makeText(this, "cannot send the tweet", Toast.LENGTH_SHORT).show();
        onUpdateEvent();
    }

    private void onUpdateEvent() {
        if(isVisible()) {
            refreshList();
        } else {
            dataDirty = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jobManager.addJobInBackground(new FetchTweetsJob());
        if(dataDirty) {
            refreshList();
            dataDirty = false;
        }
    }

    private void refreshList() {
        new SimpleBackgroundTask<LazyList<Tweet>>(this) {
            @Override
            protected LazyList<Tweet> onRun() {
                return TweetModel.getInstance().lazyLoadTweets();
            }

            @Override
            protected void onSuccess(LazyList<Tweet> result) {
                tweetAdapter.replaceLazyList(result);
            }
        }.execute();
    }

    private static class TweetAdapter extends LazyListAdapter<Tweet> {
        private final LayoutInflater layoutInflater;
        public TweetAdapter(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view == null) {
                view = layoutInflater.inflate(R.layout.list_tweet, viewGroup, false);
                holder = new ViewHolder(view);
            } else {
                holder = ViewHolder.getFromView(view);
            }
            holder.render(getItem(i));
            return view;
        }

        private static class ViewHolder {
            TextView statusTextView;
            public ViewHolder(View view) {
                statusTextView = (TextView) view.findViewById(R.id.status);
                view.setTag(this);
            }

            public static ViewHolder getFromView(View view) {
                Object tag = view.getTag();
                if(tag instanceof ViewHolder) {
                    return (ViewHolder) tag;
                } else {
                    return new ViewHolder(view);
                }
            }

            public void render(Tweet tweet) {
                statusTextView.setText(tweet.getText());
                if(tweet.getServerId() == null) {
                    statusTextView.setTextColor(Color.GRAY);
                } else {
                    statusTextView.setTextColor(Color.BLACK);
                }
            }
        }
    }
}