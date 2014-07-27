package com.smallcat.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.activity.ClubDetailActivity;
import com.smallcat.activity.ClubHomeActivity;
import com.smallcat.activity.MainActivity;
import com.smallcat.adapter.FindAdapter;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class ClubExpsFragment extends Fragment implements OnRefreshListener {
	
	private ListView lv;
	private Bundle bundle;
	private SimpleAdapter mAdapter;
	private PullToRefreshLayout mPullToRefreshLayout;
	private View mStatusView;
	
	public ClubExpsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = (View) inflater.inflate(R.layout.fragment_club_exps, container, false);
		lv = (ListView)rootView.findViewById(R.id.listView1);
		mStatusView = (View)rootView.findViewById(R.id.find_status);
		bundle = getArguments();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set the List Adapter to display the sample items
        String clubID = bundle.getString(MainActivity.EXTRA_CID);
        
        showProgress(true);
        WebAPI.get("club/GetTwitters?id=" + clubID + "&index=0", null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] header, byte[] data) {
				// TODO Auto-generated method stub
				List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
				JsonObj jo = new JsonObj(data);
				
				for (JsonObj item : jo.values()) {
					String date = item.getString("PublishedTime").replace('T', ' ');
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date d = null;
					try {
						d = sdf.parse(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Date now = new Date();
					long interval = (now.getTime() - d.getTime()) / (24 * 60 * 60);
					HashMap<String,Object> map = new HashMap<String, Object>();
		        	map.put("title", item.getString("UserRealName") + "参加了活动" + item.getString("Title"));
		        	map.put("content", item.getString("Comment"));
		        	map.put("date", String.valueOf(interval) + "分钟前");
		        	list.add(map);
				}
				
				mAdapter = new SimpleAdapter(getActivity(), list, R.layout.include_list_item_exp,
		        		new String[] {"title", "content", "date"}, new int[] {R.id.lbl_title, R.id.txt_content, R.id.lbl_date});
				
				showProgress(false);
				lv.setAdapter(mAdapter);
				
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(), ClubDetailActivity.class);
					    intent.putExtra(ClubHomeActivity.EXTRA_LOCATION, "1");
						startActivity(intent);
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				showProgress(false);
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
	
	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		//setListShown(false);
        /**
         * Simulate Refresh with 4 seconds sleep
         */
		String clubID = bundle.getString(MainActivity.EXTRA_CID);
        
        WebAPI.get("club/GetTwitters?id=" + clubID + "&index=0", null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] header, byte[] data) {
				// TODO Auto-generated method stub
				List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
				JsonObj jo = new JsonObj(data);
				
				for (JsonObj item : jo.values()) {
					String date = item.getString("PublishedTime").replace('T', ' ');
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date d = null;
					try {
						d = sdf.parse(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Date now = new Date();
					long interval = (now.getTime() - d.getTime()) / (24 * 60 * 60);
					HashMap<String,Object> map = new HashMap<String, Object>();
		        	map.put("title", item.getString("UserRealName") + "参加了活动" + item.getString("Title"));
		        	map.put("content", item.getString("Comment"));
		        	map.put("date", String.valueOf(interval) + "分钟前");
		        	list.add(map);
				}
				
				mAdapter = new SimpleAdapter(getActivity(), list, R.layout.include_list_item_exp,
		        		new String[] {"title", "content", "date"}, new int[] {R.id.lbl_title, R.id.txt_content, R.id.lbl_date});
				
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
		                
						lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(getActivity(), ClubDetailActivity.class);
							    intent.putExtra(ClubHomeActivity.EXTRA_LOCATION, "1");
								startActivity(intent);
							}
						});
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
}
