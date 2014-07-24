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
import com.smallcat.activity.ClubHomeActivity;
import com.smallcat.adapter.ClubJoinedAdapter;
import com.smallcat.adapter.FindAdapter;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
	private ListView lv;
	private View mStatusView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		lv = (ListView)rootView.findViewById(R.id.listView1);
		mStatusView = (View)rootView.findViewById(R.id.home_status);
		/*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 >= 2) {
					Intent intent = new Intent(getActivity(), ClubHomeActivity.class);
					startActivity(intent);
				}
			}
		});*/
		
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
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new ClubJoinedAdapter(getActivity());
		
		mAdapter.AddFooter();
		mAdapter.AddCategory("My Clubs", "");
		
		showProgress(true);
		WebAPI.get("club/GetMyClubs?id=2", null, new AsyncHttpResponseHandler() {
			
			@SuppressLint("SimpleDateFormat") @Override
			public void onSuccess(int arg0, Header[] header, byte[] data) {
				// TODO Auto-generated method stub
				mAdapter = new ClubJoinedAdapter(getActivity());
				JsonObj jo = new JsonObj(data);
				Integer count = jo.count();
				mAdapter.AddFooter();
				mAdapter.AddCategory("My Clubs", count.toString());
				for (JsonObj item : jo.values()) {
					mAdapter.AddClub(item.getString("ClubID"), item.getString("ClubName"), item.getString("Twitters"), item.getString("ClubRole"));
				}
				mAdapter.AddCategory("友情提示：", "点击搜索按钮,加入更多社团吧");
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
		
		WebAPI.get("club/GetMyClubs?id=2", null, new AsyncHttpResponseHandler() {		
        	
			@Override
			public void onSuccess(int arg0, Header[] header, byte[] data) {
				// TODO Auto-generated method stub
				mAdapter = new ClubJoinedAdapter(getActivity());
				JsonObj jo = new JsonObj(data);
				Integer count = jo.count();
				mAdapter.AddFooter();
				mAdapter.AddCategory("My Clubs", count.toString());
				for (JsonObj item : jo.values()) {
					mAdapter.AddClub(item.getString("ClubID"), item.getString("ClubName"), item.getString("Twitters"), item.getString("ClubRole"));
				}
				mAdapter.AddCategory("友情提示：", "点击搜索按钮,加入更多社团吧");
				new AsyncTask<Void, Void, Void>() {

		            @Override
		            protected Void doInBackground(Void... params) {
		                try {
		                    Thread.sleep(1000);
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

