package com.smallcat.fragment;

import com.example.smallcat.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = (View) inflater.inflate(R.layout.fragment_detail, container, false);
		Bundle bundle = getArguments();
		TextView title = (TextView) rootView.findViewById(R.id.lbl_title);
		title.setText(bundle.getString("title"));
		return rootView;
	}
}
