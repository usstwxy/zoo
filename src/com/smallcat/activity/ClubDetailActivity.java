package com.smallcat.activity;

import com.example.smallcat.R;
import com.smallcat.fragment.*;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ClubDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_club_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		//根据ClubHome主页面的location显示对应详情 
	    String message = getIntent().getStringExtra(ClubHomeActivity.EXTRA_LOCATION);
		
		if (savedInstanceState == null) {
			if (message.equals("1"))
				getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new ClubExpDetailFragment()).commit();
			else
				getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new GameDetailFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.club_detail, menu);
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
		}else if (id == android.R.id.home) {
			this.finish();
		}
		
		return super.onOptionsItemSelected(item);
	}

}
