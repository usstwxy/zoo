package com.smallcat.fragment;

import com.example.smallcat.R;
import com.small.service.AlarmReceiver;
import com.smallcat.activity.ManagerActivity;
import com.smallcat.activity.TwitterActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class MeFragment extends Fragment implements OnClickListener{
	
	private LinearLayout btn_activities, btn_twitter;
	
	
	public MeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = (View) inflater.inflate(R.layout.fragment_me, container, false);
		
		btn_activities = (LinearLayout) rootView.findViewById(R.id.btn_activities);
		btn_twitter = (LinearLayout) rootView.findViewById(R.id.btn_twitter);
		btn_activities.setOnClickListener(this);
		btn_twitter.setOnClickListener(this);
		
		return rootView;
	}
	
	
	private void clickActivities(){
		Intent intent = new Intent(getActivity(), ManagerActivity.class);
		getActivity().startActivityForResult(intent, 0);
	}
	
	private void clickTwitter(){
		Intent intent = new Intent(getActivity(), TwitterActivity.class);
		getActivity().startActivityForResult(intent, 0);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){
		case R.id.btn_activities:
			clickActivities();
			break;
		case R.id.btn_twitter:
			clickTwitter();
			break;
		}
	}
}
