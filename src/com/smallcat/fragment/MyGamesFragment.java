package com.smallcat.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.activity.LoginActivity;
import com.smallcat.adapter.FindAdapter;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;

public class MyGamesFragment extends Fragment {
	
	private ListView listView;
	private FindAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = (View) inflater.inflate(R.layout.fragment_mygames, container, false);
		
		listView = (ListView) rootView.findViewById(R.id.listView1);
		
		WebAPI.get("exps/getFExps?id=" + LoginActivity.USERID, null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				mAdapter = new FindAdapter(getActivity());
				JsonObj jo = new JsonObj(arg2);
				Integer count = jo.count();
				//mAdapter.AddCategory("社团类别1", count.toString());
				for (JsonObj item : jo.values()) {
					String dateText = item.getString("StartTime").replace('T', ' ');
					String url = item.getString("Poster");
					if (url != null && !url.equals("")){
						url = "http://114.215.207.88" + url;
					}
					mAdapter.AddGame(url, item.getString("Title"), item.getString("Num"),item.getString("ClubName"),
							item.getString("CNum"), dateText, item.getString("Place"), item.getString("ID"));
				}
				listView.setAdapter(mAdapter);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});

		return rootView;
	}
}
