package com.smallcat.activity;

import com.example.smallcat.R;
import com.example.smallcat.R.id;
import com.example.smallcat.R.layout;
import com.example.smallcat.R.menu;
import com.smallcat.fragment.ManagerFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ManagerActivity extends FragmentActivity {
	
	private ManagerFragment managerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("我的活动管理");
		
		if (savedInstanceState == null) {
			managerFragment = new ManagerFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, managerFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
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
