package com.smallcat.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import com.example.smallcat.R;
import com.example.smallcat.R.id;
import com.example.smallcat.R.layout;
import com.example.smallcat.R.menu;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;
import com.smallcat.dialog.CheckDialogFragment;
import com.smallcat.dialog.CheckDialogFragment.CheckDialogListener;
import com.smallcat.fragment.ClubsFragment;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.os.Build;

public class ClubsActivity extends FragmentActivity implements CheckDialogListener {

	private Fragment fg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clubs);
		
		getActionBar().setTitle("搜索社团");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		handleIntent(getIntent());
		
		if (savedInstanceState == null) {
			fg = new ClubsFragment(); 
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fg).commit();
		}
	}

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clubs, menu);
		SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
		SearchManager searchManager =
		           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));
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
		} else if (id == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		try {
			String checkCode = ((CheckDialogFragment)dialog).getCheckCode();
			String clubID = ((CheckDialogFragment)dialog).getClubID();
			
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setMessage("验证中。。。。稍等哦，亲！");
			pd.setCancelable(false);
			pd.show();
			
			WebAPI.get("clubkey/check?userID=" + LoginActivity.USERID + "&clubID="
					+ clubID + "&superkey=" + checkCode, null, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] header, byte[] data) {
					// TODO Auto-generated method stub
					JsonObj jo = new JsonObj(data);
					boolean result = jo.getBool("result");
					if (result){
						Toast.makeText(getApplicationContext(), "恭喜你！加入该社团啦", Toast.LENGTH_SHORT).show();
						((ClubsFragment)fg).UpDate();
						pd.dismiss();
					}
					else {
						Toast.makeText(getApplicationContext(), "抱歉。。验证失败", Toast.LENGTH_SHORT).show();
						pd.dismiss();
					}
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			});
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		dialog.dismiss();
	}

}
