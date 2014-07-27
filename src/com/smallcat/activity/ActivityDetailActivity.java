package com.smallcat.activity;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.http.Header;

import cn.jpush.android.api.JPushInterface;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;
import com.smallcat.fragment.ActivityDetailFragment;
import com.smallcat.service.AlarmReceiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActivityDetailActivity extends FragmentActivity {
	
	private Bundle bundle;
	
	private ActivityDetailFragment fragment;
	
	private boolean modified = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		bundle = getIntent().getExtras();
		
		getActionBar().setTitle(bundle.getString("title"));
		
		if (savedInstanceState == null) {
			fragment = new ActivityDetailFragment();
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			WebAPI.get("activity/SignUp?userID=" + LoginActivity.USERID + "&aID=" + bundle.getString("id"), null, new AsyncHttpResponseHandler() {
				
				@SuppressLint("SimpleDateFormat") @Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					JsonObj jo = new JsonObj(arg2);
					boolean result = jo.getBool("result");
					if (result){
						fragment.update();
						modified = true;
						try {
							AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
							Intent intent = new Intent(ActivityDetailActivity.this, AlarmReceiver.class);
							bundle.putString("comment", String.valueOf(Integer.valueOf(bundle.getString("comment") + 1)));
							intent.putExtras(bundle);
							int requestCode = 0;
							PendingIntent pendIntent = PendingIntent.getBroadcast(ActivityDetailActivity.this,
									requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = sdf.parse(bundle.getString("date"));
							Date now = new Date();
							long interval = date.getTime() - now.getTime() + SystemClock.elapsedRealtime();
							interval = 5000;
							alarmMgr.set(AlarmManager.ELAPSED_REALTIME, interval, pendIntent);
							
							Toast.makeText(ActivityDetailActivity.this, "报名成功", Toast.LENGTH_SHORT).show();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						String msg = jo.getString("msg");
						Toast.makeText(ActivityDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					// TODO Auto-generated method stub
				}
			});
			return true;
		}
		else if (id == android.R.id.home){
			if (modified){
				setResult(1, null);
			}
			else{
				setResult(0, null);
			}
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		if (modified){
			setResult(1, null);
		}
		else{
			setResult(0, null);
		}
		super.onBackPressed();
	}
}