package com.smallcat.activity;

import org.apache.http.Header;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;
import com.smallcat.fragment.SubmissionFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SubmissionActivity extends FragmentActivity {
	private Bundle bundle;
	private SubmissionFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		bundle = getIntent().getExtras();
		
		getActionBar().setTitle(bundle.getString("title"));
		
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
		getMenuInflater().inflate(R.menu.note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_submit) {
			String text = fragment.getContent();
			if (!text.equals("")){
				bundle.putString("comment", text);
				RequestParams params = new RequestParams();
				params.add("UserID", LoginActivity.USERID);
				params.add("ActivityID", bundle.getString("id"));
				params.add("Comment", text);
				WebAPI.post("exps/publishexp", params, new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						JsonObj jo = new JsonObj(arg2);
						if (jo.getBool("result")){
							Intent intent = new Intent();
							intent.putExtras(bundle);
							setResult(1, intent);
							finish();
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(SubmissionActivity.this, "网络问题，请重试", Toast.LENGTH_SHORT).show();
					}
				});
			}
			return true;
		}
		else if (id == android.R.id.home){
			setResult(0, null);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		setResult(0, null);
		super.onBackPressed();
	}
}
