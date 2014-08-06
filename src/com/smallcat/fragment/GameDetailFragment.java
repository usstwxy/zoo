package com.smallcat.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.activity.PosterActivity;
import com.smallcat.adapter.ImageLoader;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GameDetailFragment extends Fragment implements OnClickListener{
	
	private Bundle bundle;
	private View rootView;
	private ImageView poster;
	private Bitmap bmp;
	
	private TextView title, date, place, source, info;
	
	@SuppressLint("SimpleDateFormat") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = (View) inflater.inflate(R.layout.fragment_game_detail, container, false);
		bundle = getArguments();
		
		poster = (ImageView) rootView.findViewById(R.id.image);
		title = (TextView) rootView.findViewById(R.id.title);
		date = (TextView) rootView.findViewById(R.id.date);
		place = (TextView) rootView.findViewById(R.id.place);
		source = (TextView) rootView.findViewById(R.id.source);
		info = (TextView) rootView.findViewById(R.id.info);
		
		title.setText(bundle.getString("title"));
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date deadline = new Date();
			deadline = sdf.parse(bundle.getString("date"));
			date.setText(FormatTime(new Date(), deadline));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		place.setText(bundle.getString("place"));
		
		source.setText(bundle.getString("source"));
		
		String url = bundle.getString("url");
		
		if (url != null && !url.equals("")){
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url);
			poster.setOnClickListener(this);
		}
		
		WebAPI.get("activity/getbriefinfo?id=" + bundle.getString("id"), null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] header, byte[] data) {
				// TODO Auto-generated method stub
				JsonObj jo = new JsonObj(data);
				info.setText(jo.getString("Intro"));
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return rootView;
	}
	
	public void update(){
		
	}
	
	private void showBigPicture(){
		if (bmp != null){
			PosterActivity.post = bmp;
			Intent intent = new Intent(getActivity(), PosterActivity.class);
			getActivity().startActivity(intent);
		}
	}
	
	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
		
		@Override
		protected Bitmap doInBackground(Object... params) {
			String url = (String) params[0];
			return ImageLoader.loadImage(url);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null) return;
			GameDetailFragment.this.bmp = result;
			if (GameDetailFragment.this.poster != null){
				GameDetailFragment.this.poster.setImageBitmap(result);
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){
		case R.id.image:
			showBigPicture();
			break;
		}
	}
	
	private String FormatTime(Date startDate, Date endDate) {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		
		long interval = 0;
		
		if (startCalendar.after(endCalendar))
			return "已开始";
		
		startCalendar.add(Calendar.YEAR, 1);  
		if (startCalendar.before(endCalendar)) {
			endCalendar.add(Calendar.YEAR, 1);  
			while (startCalendar.before(endCalendar)) {
				interval++;
				startCalendar.add(Calendar.YEAR, 1);  
			}
			interval--;
			return "还有" + interval + "年";
		} 
		endCalendar.add(Calendar.YEAR, 1);
		
		startCalendar.add(Calendar.MONTH, 1);  
		if (startCalendar.before(endCalendar)) {
			endCalendar.add(Calendar.MONTH, 1);  
			while (startCalendar.before(endCalendar)) {
				interval++;
				startCalendar.add(Calendar.MONTH, 1);
			}
			interval--;
			return "还有" + interval + "月";
		} 
		endCalendar.add(Calendar.MONTH, 1);  
		
		startCalendar.add(Calendar.DAY_OF_MONTH, 1);  
		if (startCalendar.before(endCalendar)) {
			endCalendar.add(Calendar.DAY_OF_MONTH, 1);  
			while (startCalendar.before(endCalendar)) {
				interval++;
				startCalendar.add(Calendar.DAY_OF_MONTH, 1); 
			}
			interval--;
			return "还有" + interval + "天";
		} 
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);  
		
		startCalendar.add(Calendar.HOUR, 1);  
		if (startCalendar.before(endCalendar)) {
			endCalendar.add(Calendar.HOUR, 1);  
			while (startCalendar.before(endCalendar)) {
				interval++;
				startCalendar.add(Calendar.HOUR, 1);  
			}
			interval--;
			return "还有" + interval + "小时";
		} 
		endCalendar.add(Calendar.HOUR, 1);
		
		startCalendar.add(Calendar.MINUTE, 1);  
		if (startCalendar.before(endCalendar)) {
			endCalendar.add(Calendar.MINUTE, 1);  
			while (startCalendar.before(endCalendar)) {
				interval++;
				startCalendar.add(Calendar.MINUTE, 1);  
			}
			interval--;
			return "还有" + interval + "分钟";
		}
		
		return "已开始";
	}
}
