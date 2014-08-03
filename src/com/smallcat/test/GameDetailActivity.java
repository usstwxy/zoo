package com.smallcat.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.http.Header;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.activity.LoginActivity;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;
import com.smallcat.test.GameDetailFragment;
import com.smallcat.service.AlarmReceiver;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GameDetailActivity extends FragmentActivity implements PanelSlideListener{
	
	private Bundle bundle;
	private GameDetailFragment fragment;
	private boolean modified = false;
	private ListView comments;
	private TextView cnt;
	private ImageView arrow;
	private SlidingUpPanelLayout sliding_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		bundle = getIntent().getExtras();
		
		//getActionBar().setTitle(bundle.getString("title"));
		
		getActionBar().setTitle("活动详情");
		
		if (savedInstanceState == null) {
			fragment = new GameDetailFragment();
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
		
		sliding_layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		comments = (ListView) findViewById(R.id.comments);
		cnt = (TextView) findViewById(R.id.label_cnt);
		cnt.setText("目前共有" + bundle.getString("comment") + "个小伙伴响应啦！");
		arrow = (ImageView) findViewById(R.id.arrow);
		
		sliding_layout.setPanelSlideListener(this);
		
		WebAPI.get("activity/getComments?id=" + bundle.getString("id") + "&index=0", null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] header, byte[] data) {
				// TODO Auto-generated method stub
				JsonObj jo = new JsonObj(data);
				
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
				
				for (JsonObj item : jo.values()){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("userName", item.getString("UserName") + " " + item.getString("PublishedTime").replace("T", " "));
					map.put("comment", item.getString("Comment"));
					list.add(map);
				}
				
				SimpleAdapter adapter = new SimpleAdapter(GameDetailActivity.this, list,
						R.layout.include_list_item_comment,
						new String[]{"userName", "comment"},
						new int[]{R.id.user_name, R.id.comment_text});
				comments.setAdapter(adapter);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
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
							Intent intent = new Intent(GameDetailActivity.this, AlarmReceiver.class);
							bundle.putString("comment", String.valueOf(Integer.valueOf(bundle.getString("comment") + 1)));
							intent.putExtras(bundle);
							int requestCode = 0;
							PendingIntent pendIntent = PendingIntent.getBroadcast(GameDetailActivity.this,
									requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = sdf.parse(bundle.getString("date"));
							Date now = new Date();
							long interval = date.getTime() - now.getTime() + SystemClock.elapsedRealtime();
							interval = 5000;
							alarmMgr.set(AlarmManager.ELAPSED_REALTIME, interval, pendIntent);
							
							Toast.makeText(GameDetailActivity.this, "报名成功", Toast.LENGTH_SHORT).show();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						String msg = jo.getString("msg");
						Toast.makeText(GameDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
	
	private int expanding = 1;

	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		// TODO Auto-generated method stub
		Matrix matrix = new Matrix();
		matrix.postRotate(expanding * 180 * slideOffset, arrow.getWidth() / 2.0f, arrow.getHeight() / 2.0f);
		arrow.setImageMatrix(matrix);
	}

	@Override
	public void onPanelCollapsed(View panel) {
		// TODO Auto-generated method stub
		expanding *= -1;
	}

	@Override
	public void onPanelExpanded(View panel) {
		// TODO Auto-generated method stub
		expanding *= -1;
	}

	@Override
	public void onPanelAnchored(View panel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPanelHidden(View panel) {
		// TODO Auto-generated method stub
		
	}
}
