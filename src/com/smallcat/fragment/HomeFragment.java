package com.smallcat.fragment;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.example.smallcat.R;
import com.smallcat.activity.ClubHomeActivity;
import com.smallcat.adapter.ClubJoinedAdapter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class HomeFragment extends Fragment implements OnRefreshListener {
	
	private PullToRefreshLayout mPullToRefreshLayout;
	private ClubJoinedAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		ListView lv = (ListView)rootView.findViewById(R.id.listView1);
		
		mAdapter = new ClubJoinedAdapter(getActivity());
		
		mAdapter.AddFooter();
		mAdapter.AddCategory("My Clubs", "2");
		mAdapter.AddClub("吉他社");
		mAdapter.AddClub("轮滑社");
		
		
		lv.setAdapter(mAdapter);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "arg2:" + arg2 + "; arg3:" + arg3, Toast.LENGTH_SHORT).show();
				if (arg2 >= 2) {
					Intent intent = new Intent(getActivity(), ClubHomeActivity.class);
					startActivity(intent);
				}
			}
		});
		
		return rootView;
	}
	
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) view;
        // As we're using a ListFragment we create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
        
        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .insertLayoutInto(viewGroup)
                // Here we mark just the ListView and it's Empty View as pullable
                .theseChildrenArePullable(R.id.listView1)
                .listener(this)
                .setup(mPullToRefreshLayout);
    }

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();

                if (getView() != null) {
                    // Show the list again
                    //setListShown(true);
                }
            }
        }.execute();
	}
}

