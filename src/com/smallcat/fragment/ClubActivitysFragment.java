package com.smallcat.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.activity.MainActivity;
import com.smallcat.adapter.FindAdapter;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
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
	private Bundle bundle;
	private PullToRefreshLayout mPullToRefreshLayout;
	private FindAdapter mAdapter;
	private View mStatusView;
	
	public ClubActivitysFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = (View) inflater.inflate(R.layout.fragment_club_activitys, container, false);
		lv = (ListView) rootView.findViewById(R.id.listView1);
		bundle = getArguments();
		mStatusView = rootView.findViewById(R.id.find_status);
		
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
	
        String clubID = bundle.getString(MainActivity.EXTRA_CID);
        
        showProgress(true);
		WebAPI.get("club/getActivity?id=" + clubID + "&index=0", null, new AsyncHttpResponseHandler() {
			
			@SuppressLint("SimpleDateFormat") @Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				mAdapter = new FindAdapter(getActivity());
				
				JsonObj jo = new JsonObj(arg2);
				String clubName = bundle.getString(MainActivity.EXTRA_CTITLE);
				for (JsonObj item : jo.values()) {
					String dateText = item.getString("StartTime").replace('T', ' ');
					String url = item.getString("Poster");
					if (url != null && !url.equals("")){
						url = "http://114.215.207.88" + url;
					}
					mAdapter.AddActivity(url, item.getString("Title"), item.getString("Num"), clubName, item.getString("CNum"), dateText, item.getString("ID"));
				}
				showProgress(false);
				lv.setAdapter(mAdapter);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				showProgress(false);
				Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
			}
		});
	
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		//setListShown(false);
        /**
         * Simulate Refresh with 4 seconds sleep
         */
		String clubID = bundle.getString(MainActivity.EXTRA_CID);

		WebAPI.get("club/getActivity?id=" + clubID + "&index=0", null, new AsyncHttpResponseHandler() {		
        	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				mAdapter = new FindAdapter(getActivity());
				
				String clubName = bundle.getString(MainActivity.EXTRA_CTITLE);
				JsonObj jo = new JsonObj(arg2);
				for (JsonObj item : jo.values()) {
					String dateText = item.getString("StartTime").replace('T', ' ');
					String url = item.getString("Poster");
					if (url != null && !url.equals("")){
						url = "http://114.215.207.88" + url;
					}
					mAdapter.AddActivity(url, item.getString("Title"), item.getString("Num"), clubName, item.getString("CNum"), dateText, item.getString("ID"));
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

			mStatusView.setVisibility(View.VISIBLE);
			mStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}
}
