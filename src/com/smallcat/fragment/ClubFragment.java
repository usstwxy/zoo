package com.smallcat.fragment;

import com.example.smallcat.R;
import com.smallcat.adapter.ClubJoinedAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ClubFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_club, container,
				false);

		ListView lv = (ListView)rootView.findViewById(R.id.listView1);
		
		ClubJoinedAdapter adapter = new ClubJoinedAdapter(getActivity());
		
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "我是Item", Toast.LENGTH_SHORT).show();
			}
		
		});
		
		return rootView;
	}
}

