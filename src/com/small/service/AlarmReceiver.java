package com.small.service;

import com.example.smallcat.R;
import com.smallcat.activity.DetailActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

	private Bundle bundle;
	private Context context;
	private Intent intent;
	private PendingIntent pendingIntent;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder notificationBuilder;
	private int notificationID = 100;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context = context;
		bundle = intent.getExtras();
		initNotification();
		notificationBuilder.setTicker("活动马上开始啦")
						.setContentTitle(bundle.getString("title"))
						.setContentText(bundle.getString("source"));
		notificationManager.notify(notificationID, notificationBuilder.build());
	}
	
	private void initNotification(){
		intent = new Intent(context, DetailActivity.class);
		intent.putExtras(bundle);
		pendingIntent= PendingIntent.getActivity(context, 1, intent, Notification.FLAG_AUTO_CANCEL);
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationBuilder = new NotificationCompat.Builder(context);
		notificationBuilder.setContentTitle("测试标题")
						.setContentText("测试内容")
						.setContentIntent(pendingIntent)
						.setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
						.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
						.setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
						.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消  
						.setOngoing(false)//True，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
						.setDefaults(Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
						//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
						.setSmallIcon(R.drawable.ic_launcher);
	}
	

}
