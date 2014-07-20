package com.smallcat.fragment;

import org.apache.http.Header;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.adapter.FindAdapter;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ClubActivitysFragment extends Fragment implements OnRefreshListener {
	
	private ListView lv;
	private PullToRefreshLayout mPullToRefreshLayout;
	private FindAdapter mAdapter;
	private View mFindStatusView;
	public ClubActivitysFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = (View) inflater.inflate(R.layout.fragment_club_activitys, container, false);
		lv = (ListView) rootView.findViewById(R.id.listView1);
		mFindStatusView = rootView.findViewById(R.id.find_status);
		
		showProgress(true);
		WebAPI.get("activity/0", null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				mAdapter = new FindAdapter(getActivity());
				JsonObj jo = new JsonObj(arg2);
				Integer count = jo.count();
				for (JsonObj item : jo.values()) {
					mAdapter.AddActivity(item.getString("Title"), item.getString("Num"), item.getString("ClubName"), "0", item.getString("StartTime"));
				}
				showProgress(false);
				lv.setAdapter(mAdapter);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
			}
		});
		
		return rootView;
	}
	
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) view;
        Log.i("Club:", "ViewCreated");
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
		//setListShown(false);
        /**
         * Simulate Refresh with 4 seconds sleep
         */
		Log.i("Club:", "Refresh");
		
		
		
        WebAPI.get("activity/0", null, new AsyncHttpResponseHandler() {		
        	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (mAdapter == null)
					mAdapter = new FindAdapter(getActivity());
				Toast.makeText(getActivity(), "123", Toast.LENGTH_SHORT).show();
				JsonObj jo = new JsonObj(arg2);
				Integer count = jo.count();
				for (JsonObj item : jo.values()) {
					mAdapter.AddActivity(item.getString("Title"), item.getString("Num"), item.getString("ClubName"), "0", item.getString("StartTime"));
				}
				
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
 
						lv.setAdapter(mAdapter);
		                
		                // Notify PullToRefreshLayout that the refresh has finished
		                mPullToRefreshLayout.setRefreshComplete();

		                if (getView() != null) {
		                    // Show the list again
		                    //setListShown(true);
		                }
		            }
		        }.execute();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mFindStatusView.setVisibility(View.VISIBLE);
			mFindStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mFindStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mFindStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}
}
