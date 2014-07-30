package com.smallcat.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smallcat.R;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;
import com.smallcat.activity.PostGameActivity;
import com.smallcat.adapter.ImageLoader;


public class SubmissionFragment extends Fragment implements OnDateSetListener, OnTimeSetListener, OnClickListener{
	public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    public static final int cameraRequest = 1;
	public static final int galleryRequest = 2;
    
	private ImageView camera, selectedImage;
	private ImageView[] image = new ImageView[3];
	private Bitmap[] picture = new Bitmap[]{null, null, null};
	private View rootView;
	private Bundle bundle;
	private EditText mTitle;
	private EditText mContent;
	private View mSelectDate;
	private TextView mDate; 
	private TextView mTime;
	private TextView mTip;
	
	@SuppressLint("SimpleDateFormat") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = (View) inflater.inflate(R.layout.fragment_submission, container, false);
		mContent = (EditText)rootView.findViewById(R.id.edt_content);
		mContent.setText(getArguments().getString("comment"));
		mTip = (TextView)rootView.findViewById(R.id.text_submit_tip);
		
		bundle = getArguments();
		
		String flag = bundle.getString(PostGameActivity.EXTRA_MANAGE);
		if (flag != null && flag.equals("true")) {
			
			mTip.setText("快捷发布活动，不提供图片功能哦~");
			mContent.setHint("在此写下内容吧:)");
			mSelectDate = (View)rootView.findViewById(R.id.select_starttime);
			mSelectDate.setVisibility(View.VISIBLE);
			mDate = (TextView)rootView.findViewById(R.id.txt_startdate);
			mTime = (TextView)rootView.findViewById(R.id.txt_starttime);
			mTitle = (EditText)rootView.findViewById(R.id.edt_title);
			mTitle.setVisibility(View.VISIBLE);
			
			setDateListener();
			
			if (savedInstanceState != null) {
	            DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag(DATEPICKER_TAG);
	            if (dpd != null) {
	                dpd.setOnDateSetListener(this);
	            }

	            TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
	            if (tpd != null) {
	                tpd.setOnTimeSetListener(this);
	            }
	        }
		} else {
			camera = (ImageView) rootView.findViewById(R.id.camera);
			image[0] = (ImageView) rootView.findViewById(R.id.image1);
			image[1] = (ImageView) rootView.findViewById(R.id.image2);
			image[2] = (ImageView) rootView.findViewById(R.id.image3);
			
			camera.setOnClickListener(this);
			image[0].setOnClickListener(this);
			image[1].setOnClickListener(this);
			image[2].setOnClickListener(this);
			
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(bundle.getString("image1"),
					bundle.getString("image2"), bundle.getString("image3"));
		}
		
		
		return rootView;
	}
	
	public void setPicture(Bitmap bmp){
		selectedImage.setImageBitmap(bmp);
		switch (selectedImage.getId()){
		case R.id.image1:
			picture[0] = bmp;
			break;
		case R.id.image2:
			picture[1] = bmp;
			break;
		case R.id.image3:
			picture[2] = bmp;
			break;
		}
	}
	
	public void setPicture(){
		Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "smallcat_temp.jpg"));
        ContentResolver cr = getActivity().getContentResolver();
		try {
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
        	boolean ok = false;
        	for (int i=0;i<3 && !ok;++i){
        		if (picture[i] == null){
        			image[i].setImageBitmap(bitmap);
        			picture[i] = bitmap;
        			ok = true;
        		}
        	}
        	if (!ok){
        		image[2].setImageBitmap(bitmap);
        		picture[2] = bitmap;
        	}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] getPicture(int index){
		byte[] data = null;
		if (picture[index] != null){
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				picture[index].compress(Bitmap.CompressFormat.JPEG, 100, stream);
				data = stream.toByteArray();
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}
	
	public String getContent(){
		return mContent.getText().toString();
	}
	
	public String getTitle(){
		return mTitle.getText().toString();
	}
	
	public String getDate(){
		return mDate.getText() + " " + mTime.getText();
	}
	
	public String getPlace(){
		return "";
	}

	private void setDateListener() {
		Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
        
        
        mDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });
	}
	
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub
		mDate.setText(year + "-" + ++month + "-" + day);
	}
	
	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		mTime.setText(hourOfDay + ":" + minute);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){
		case R.id.camera:{
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Intent intent_camera = getActivity().getPackageManager()
		                .getLaunchIntentForPackage("com.android.camera");
				if (intent_camera != null) {
				    intent.setPackage("com.android.camera");
				}
				Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "smallcat_temp.jpg"));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, false);
				startActivityForResult(intent, cameraRequest);
			}
			break;
		case R.id.image1:
		case R.id.image2:
		case R.id.image3:{
				Intent intent = new Intent();
	            intent.setType("image/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(intent, galleryRequest);
	            selectedImage = (ImageView) arg0;
			}
			break;
		}
	}
	
	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap[]> {

		@Override
		protected Bitmap[] doInBackground(Object... params) {
			String image1 = (String) params[0];
			String image2 = (String) params[1];
			String image3 = (String) params[2];
			if (!image1.equals("")){
				SubmissionFragment.this.picture[0] = ImageLoader.loadImage(image1);
			}
			else{
				SubmissionFragment.this.picture[0] = null;
			}
			if (!image2.equals("")){
				SubmissionFragment.this.picture[1] = ImageLoader.loadImage(image2);
			}
			else{
				SubmissionFragment.this.picture[1] = null;
			}
			if (!image3.equals("")){
				SubmissionFragment.this.picture[2] = ImageLoader.loadImage(image3);
			}
			else{
				SubmissionFragment.this.picture[2] = null;
			}
			Bitmap[] bmp = new Bitmap[]{SubmissionFragment.this.picture[0],
					SubmissionFragment.this.picture[1], SubmissionFragment.this.picture[2]};
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap[] result) {
			SubmissionFragment.this.image[0].setImageBitmap(result[0]);
			SubmissionFragment.this.image[1].setImageBitmap(result[1]);
			SubmissionFragment.this.image[2].setImageBitmap(result[2]);
		}
	}
}
