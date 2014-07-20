package com.smallcat.fragment;

import com.example.smallcat.R;
import com.smallcat.activity.DetailActivity;
import com.smallcat.activity.MainActivity;
import com.smallcat.activity.ManagerActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class MeFragment extends Fragment implements OnClickListener{
	
	private LinearLayout btn_activities;
	private PendingIntent pendingIntent;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder notificationBuilder;
	private int notificationID = 100;
	
	public MeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = (View) inflater.inflate(R.layout.fragment_me, container, false);
		
		btn_activities = (LinearLayout) rootView.findViewById(R.id.btn_activities);
		btn_activities.setOnClickListener(this);
		
		initNotification();
		return rootView;
	}
	
	private void initNotification(){
		notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
		pendingIntent= PendingIntent.getActivity(getActivity(), 1, new Intent(), Notification.FLAG_AUTO_CANCEL);
		notificationBuilder = new NotificationCompat.Builder(getActivity());
		notificationBuilder.setContentTitle("测试标题")
						.setContentText("测试内容")
						.setContentIntent(pendingIntent)
						.setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
						.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
						.setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
						.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消  
						.setOngoing(false)//True，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
						//.setDefaults(Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
						//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
						.setSmallIcon(R.drawable.ic_launcher);
	}
	
	private void clickActivities(){
		//notificationManager.notify(notificationID, notificationBuilder.build());
		//Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getActivity(), ManagerActivity.class);
		getActivity().startActivityForResult(intent, 0);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){
		case R.id.btn_activities:
			clickActivities();
			break;
		}
	}
}
