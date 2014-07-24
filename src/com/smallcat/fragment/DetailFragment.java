package com.smallcat.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.Header;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.activity.PostActivity;
import com.smallcat.adapter.ImageLoader;
import com.smallcat.adapter.FindAdapter.TwitterActivity;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment implements OnClickListener{
	
	private Bundle bundle;
	private View rootView;
	private ListView comments;
	private ImageView post, arrow;
	private TextView date, source, attend, cnt;
	private LinearLayout activity, info, expand;
	private Bitmap bmp;
	
	@SuppressLint("SimpleDateFormat") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = (View) inflater.inflate(R.layout.fragment_detail, container, false);
		bundle = getArguments();
		
		comments = (ListView) rootView.findViewById(R.id.listView1);
		
		post = (ImageView) rootView.findViewById(R.id.image);
		date = (TextView) rootView.findViewById(R.id.date);
		source = (TextView) rootView.findViewById(R.id.source);
		attend = (TextView) rootView.findViewById(R.id.attend);
		cnt = (TextView) rootView.findViewById(R.id.label_cnt);
		
		post.setOnClickListener(this);
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = sdf.parse(bundle.getString("date"));
			Date now = new Date();
			long interval = (d.getTime() - now.getTime()) / (24 * 60 * 60 * 1000);
			date.setText("距离活动开始还有" + String.valueOf(interval) + "天");
		}catch (ParseException e){
			
		}
		
		source.setText("由" + bundle.getString("source") + "举办");
		attend.setText("报名人数已达" + bundle.getString("attend") + "人");
		cnt.setText("评论共有" + bundle.getString("comment") + "条");
		
		activity = (LinearLayout) rootView.findViewById(R.id.activity);
		info = (LinearLayout) rootView.findViewById(R.id.info);
		expand = (LinearLayout) rootView.findViewById(R.id.btn_expand);
		arrow = (ImageView) rootView.findViewById(R.id.arrow);
		
		expand.setOnClickListener(new ExpandClickListener() {});
		
		String url = bundle.getString("url");
		
		if (url != null && !url.equals("")){
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url);
		}
		
		
		WebAPI.get("activity/getComments?id=" + bundle.getString("id") + "&index=0", null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JsonObj jo = new JsonObj(arg2);
				
				cnt.setText("评论共有" + String.valueOf(jo.count()) + "条");
				
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
				
				for (JsonObj item : jo.values()){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("userName", item.getString("UserName") + " " + item.getString("PublishedTime"));
					map.put("comment", item.getString("Comment"));
					list.add(map);
				}
				
				SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
						R.layout.fragment_detail_comment_item,
						new String[]{"userName", "comment"},
						new int[]{R.id.user_name, R.id.comment_text});
				comments.setAdapter(adapter);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});

		return rootView;
	}
	
	public void update(){
		int i = bundle.getInt("attend") + 1;
		attend.setText("报名人数已达" + String.valueOf(i) + "人");
	}
	
	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
		
		@Override
		protected Bitmap doInBackground(Object... params) {
			String url = (String) params[0];
			return ImageLoader.loadImage(url);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			DetailFragment.this.bmp = result;
			DetailFragment.this.post.setImageBitmap(result);
		}
	}
	
	class ExpandClickListener implements OnClickListener{
		
		private int state = 0;
		private RotateAnimation animation1, animation2;
		private TranslateAnimation animation;
		
		public ExpandClickListener(){
			AnimationSet set = new AnimationSet(true);
			animation1 = new RotateAnimation(0f, 180f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animation1.setDuration(300);
			animation1.setFillAfter(true);
			
			animation2 = new RotateAnimation(180f, 0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animation2.setDuration(300);
			animation2.setFillAfter(true);
			
			animation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE, -500f);
			animation.setDuration(300);
			animation.setFillAfter(true);
			
			set.addAnimation(animation);
			set.addAnimation(animation1);
			set.addAnimation(animation2);
			animation1.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					activity.setVisibility(View.GONE);
					info.setVisibility(View.GONE);
				}
			});
			animation2.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					activity.setVisibility(View.VISIBLE);
					info.setVisibility(View.VISIBLE);
				}
			});
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (state == 0){
				arrow.startAnimation(animation1);
				state = 1;
			}
			else{
				arrow.startAnimation(animation2);
				state = 0;
			}
		}
		
	}
	
	private void showBigPicture(){
		if (bmp != null){
			PostActivity.post = bmp;
			Intent intent = new Intent(getActivity(), PostActivity.class);
			getActivity().startActivity(intent);
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
}
