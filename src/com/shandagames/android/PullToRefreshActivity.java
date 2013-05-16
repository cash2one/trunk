package com.shandagames.android;

import java.util.Arrays;
import java.util.LinkedList;
import com.shandagames.android.util.DateHelper;
import com.shandagames.android.widget.PullToRefreshListView;
import com.shandagames.android.widget.PullToRefreshListView.OnRefreshListener;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class PullToRefreshActivity extends Activity {    
    private LinkedList<String> mListItems;
    private PullToRefreshListView mListView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = new PullToRefreshListView(this);
        setContentView(mListView);

        final String date = DateHelper.formatString(System.currentTimeMillis());
        
        // Set a listener to be invoked when the list should be refreshed.
        mListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
            	mListView.setLastUpdated(date);
                new GetDataTask().execute();
            }
        });

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListItems);

        mListView.setAdapter(adapter);
        
        mListView.setLastUpdated(date);
        mListView.autoStartRefresh();
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
            mListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private String[] mStrings = {
            /*"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
            "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",*/
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};
}
