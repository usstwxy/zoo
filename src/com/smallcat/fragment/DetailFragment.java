package com.smallcat.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.smallcat.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	
	private ListView comments;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = (View) inflater.inflate(R.layout.fragment_detail, container, false);
		Bundle bundle = getArguments();
		
		comments = (ListView) rootView.findViewById(R.id.listView1);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i=0;i<10;++i){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("user_name", "伍凯" + i);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.fragment_detail_comment_item, new String[]{"user_name"}, new int[]{R.id.user_name});
		comments.setAdapter(adapter);
		
		return rootView;
	}
}
