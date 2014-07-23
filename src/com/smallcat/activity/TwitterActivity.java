package com.smallcat.activity;

import com.example.smallcat.R;
import com.smallcat.fragment.TwitterFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class TwitterActivity extends FragmentActivity {
	
	private TwitterFragment twitterFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
		

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("我的随笔管理");
		
		if (savedInstanceState == null) {
			twitterFragment = new TwitterFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, twitterFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twitter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
