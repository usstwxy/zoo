package com.smallcat.activity;

import org.apache.http.Header;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;
import com.smallcat.fragment.SubmissionFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class PostGameActivity extends FragmentActivity {
	public static final String EXTRA_MANAGE = "clubmanage";
	private SubmissionFragment fragment;
	private Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postgame);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		bundle = getIntent().getExtras();
		bundle.putString(EXTRA_MANAGE, "true");
		
		getActionBar().setTitle("发布活动");
		
		if (savedInstanceState == null) {
			fragment = new SubmissionFragment();
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.publish_sample, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_submit) {
			String title = fragment.getTitle();
			String content = fragment.getContent();
			String startTime = fragment.getDate();
			if (!content.equals("")){
				RequestParams params = new RequestParams();
				params.add("UserID", LoginActivity.USERID);
				params.add("ClubID", bundle.getString(MainActivity.EXTRA_CID));
				params.add("Title", title);
				params.add("Content", content);
				params.add("StartTime", startTime);
				WebAPI.post("activity/publish", params, new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						JsonObj jo = new JsonObj(arg2);
						finish();
						Toast.makeText(PostGameActivity.this, jo.getString("result"), Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(PostGameActivity.this, "网络问题，请重试", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
		else if (id == android.R.id.home){
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

}
