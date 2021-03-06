package com.smallcat.activity;

import com.example.smallcat.R;
import com.smallcat.fragment.MyExpsFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MyExpsActivity extends FragmentActivity {
	
	private MyExpsFragment twitterFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exps);
		

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("我的随笔管理");
		
		if (savedInstanceState == null) {
			twitterFragment = new MyExpsFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, twitterFragment).commit();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1){
			twitterFragment.update(data.getExtras());
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
