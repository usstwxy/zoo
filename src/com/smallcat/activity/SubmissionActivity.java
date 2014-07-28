package com.smallcat.activity;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;
import com.smallcat.fragment.SubmissionFragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
		byte[] picture1 = fragment.getPicture(0);
		byte[] picture2 = fragment.getPicture(1);
		byte[] picture3 = fragment.getPicture(2);
		if (id == R.id.action_submit) {
			final String text = fragment.getContent();
			if (!text.equals("")){
				bundle.putString("comment", text);
				RequestParams params = new RequestParams();
				
				if (picture1 != null){
					params.put("Image1", new ByteArrayInputStream(picture1), "picture1.jpg");
				}
				if (picture2 != null){
					params.put("Image2", new ByteArrayInputStream(picture2), "picture2.jpg");
				}
				if (picture3 != null){
					params.put("Image3", new ByteArrayInputStream(picture3), "picture3.jpg");
				}
				
				WebAPI.post("exps/uploadfiles", params, new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						
						JsonObj joo = new JsonObj(arg2);
						
						RequestParams params2 = new RequestParams();
						params2.add("UserID", LoginActivity.USERID);
						params2.add("ActivityID", bundle.getString("id"));
						params2.add("Comment", text);
						if (joo.getString("Image1") != null){
							params2.add("Image1", joo.getString("Image1"));
						}
						else{
							params2.add("Image1", "null");
						}
						if (joo.getString("Image2") != null){
							params2.add("Image2", joo.getString("Image2"));
						}
						else{
							params2.add("Image2", "null");
						}
						if (joo.getString("Image3") != null){
							params2.add("Image3", joo.getString("Image3"));
						}
						else{
							params2.add("Image3", "null");
						}
						
						WebAPI.post("exps/publishexp", params2, new AsyncHttpResponseHandler() {
							
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
								//Toast.makeText(NoteActivity.this, "网络问题，请重试", Toast.LENGTH_SHORT).show();
								Toast.makeText(SubmissionActivity.this, arg0 + " " + arg1.toString() + " " + String.valueOf(arg2) + " " + arg3.toString(), Toast.LENGTH_LONG).show();
							}
						});
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(SubmissionActivity.this, arg0 + " " + arg1.toString() + " " + String.valueOf(arg2) + " " + arg3.toString(), Toast.LENGTH_LONG).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 65536 + SubmissionFragment.cameraRequest){
			Uri uri = data.getData();
			
	        ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				fragment.setPicture(bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (requestCode == 65536 + SubmissionFragment.galleryRequest) {
			if (resultCode == RESULT_OK){
	            try {
	            	Uri uri = data.getData();
	            	ContentResolver cr = this.getContentResolver();
	            	BitmapFactory.Options option = new BitmapFactory.Options();
	            	option.inJustDecodeBounds = true;
	            	Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, option);
	            	int xration = 0, yration = 0;
	            	for (int i = 1024; i >= 64; i /= 2){
	            		xration = option.outWidth / i;
	            		yration = option.outHeight / i;
	            		if (xration >= 1 || yration >= 1){
	            			if (xration > yration){
	            				option.inSampleSize = xration;
	            			}
	            			else{
	            				option.inSampleSize = yration;
	            			}
	            			break;
	            		}
	            	}
	            	option.inJustDecodeBounds = false;
	            	bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, option);
	                fragment.setPicture(bitmap); 
	            } catch (FileNotFoundException e) {
	            	e.printStackTrace();
	            }
			}
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	@Override
	public void onBackPressed(){
		setResult(0, null);
		super.onBackPressed();
	}
}
