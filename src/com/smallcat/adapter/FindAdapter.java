package com.smallcat.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smallcat.R;
import com.smallcat.activity.GameDetailActivity;
import com.smallcat.activity.SubmissionActivity;

public class FindAdapter extends BaseAdapter{
	
	private ArrayList<Row> rows = new ArrayList<Row>();
	private Game selected = null;
	private Exp selectedTwitter = null;
	private View selectedView = null;
	private Context context;
	private boolean isBusy = false;
	
	public FindAdapter(Context context){
		this.context = context;
	}
	
	public void updateActivity(){
		int i = Integer.valueOf(selected.comment) + 1;
		selected.comment = String.valueOf(i);
		GameViewHolder holder = (GameViewHolder) selectedView.getTag();
		holder.comment.setText(selected.comment);
	}
	
	public void updateTwitterActivity(String comment){
		selectedTwitter.comment = comment;
	}
	
	public void setFlagBusy(boolean isBusy){
		this.isBusy = isBusy;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rows.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return rows.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		Row row = (Row) getItem(position);
		if (view == null){
			view =  row.set();
		}else{
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null || holder.layoutID != row.layoutID){
				view = row.set();
			}
		}
		row.set(view);
		row.setListen(view);
		return view;
	}
	
	public void AddHeader(){
		rows.add(new Header());
	}
	
	public void AddCategory(String name, String count){
		rows.add(new Category(name, count));
	}
	
	public void AddGame(String url, String title, String attend, String source,
			String comment, String date, String place, String id){
		rows.add(new Game(url, title, attend, source, comment, date, place, id));
	}
	
	public void AddExp(String url, String title, String id, String comment, String image1Url, String image2Url, String image3Url){
		rows.add(new Exp(url, title, id, comment, image1Url, image2Url, image3Url));
	}
	
	abstract class ViewHolder{
		
		public int layoutID;
	}
	
	class HeaderViewHolder extends ViewHolder{
		
	}
	
	class CategoryViewHolder extends ViewHolder{
		
		public TextView name, count;
	}
	
	class GameViewHolder extends ViewHolder{

		public TextView title, attend, source, comment, date, place;
		public ImageView post;
	}
	
	class ExpViewHolder extends ViewHolder{
		
		public TextView title;
		public ImageView post;
	}
	
	abstract class Row{

		public int layoutID;
		
		public Row(int layoutID){
			this.layoutID = layoutID;
		}
		
		public abstract View set();
		
		public abstract void set(View view);
		
		public abstract void setListen(View view);
	}
	
	class Header extends Row{
		public Header(){
			super(R.layout.include_header_find);
		}

		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(context).inflate(layoutID, null);
			View btn1 = view.findViewById(R.id.btn_popular);
			View btn2 = view.findViewById(R.id.btn_search);
			btn1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "popular", Toast.LENGTH_SHORT).show();
				}
			});
			btn2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "search", Toast.LENGTH_SHORT).show();
				}
			});
			HeaderViewHolder holder = new HeaderViewHolder();
			holder.layoutID = layoutID;
			view.setTag(holder);
			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setListen(View view) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Category extends Row{
		
		public String name, count;
		
		public Category(String name, String count){
			super(R.layout.include_item_category);
			this.name = name;
			this.count = count;
		}
		
		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(context).inflate(layoutID, null);
			CategoryViewHolder holder = new CategoryViewHolder();
			holder.name = (TextView) view.findViewById(R.id.cname);
			holder.count = (TextView) view.findViewById(R.id.cnumber);
			holder.layoutID = layoutID;
			view.setTag(holder);
			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			CategoryViewHolder holder = (CategoryViewHolder)view.getTag();
			holder.name.setText(name);
			holder.count.setText(count);
		}

		@Override
		public void setListen(View view) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public class Game extends Row{
		
		public String title, attend, source, comment, date, place, id, url;
		public Bitmap bmp;
		
		public Game(String url, String title, String attend, String source,
				String comment, String date, String place, String id){
			super(R.layout.include_list_item_game);
			this.url = url;
			this.title = title;
			this.attend = attend;
			this.source = source;
			this.comment = comment;
			this.date = date;
			this.place = place;
			this.id = id;
		}
		
		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(context).inflate(layoutID, null);
			GameViewHolder holder = new GameViewHolder();
			holder.title = (TextView) view.findViewById(R.id.lbl_title);
			holder.attend = (TextView) view.findViewById(R.id.lbl_likes);
			holder.source = (TextView) view.findViewById(R.id.lbl_source_name);
			holder.comment = (TextView) view.findViewById(R.id.lbl_comments);
			holder.date = (TextView) view.findViewById(R.id.lbl_date);
			holder.place = (TextView) view.findViewById(R.id.lbl_place);
			holder.post = (ImageView) view.findViewById(R.id.image);
			holder.layoutID = layoutID;
			view.setTag(holder);
			return view;
		}

		@SuppressLint("SimpleDateFormat") @Override
		public void set(View view) {
			// TODO Auto-generated method stub
			GameViewHolder holder = (GameViewHolder)view.getTag();
			holder.title.setText(title);
			holder.attend.setText(attend);
			holder.source.setText(source);
			holder.comment.setText(comment);
			holder.date.setText("时间：" + date);
			holder.place.setText("地点：" + place);
			if (bmp != null){
				holder.post.setImageBitmap(bmp);
			}
			else if (url != null && !url.equals("")){
				ImageLoadTask imageLoadTask = new ImageLoadTask();
				imageLoadTask.execute(url);
			}
			else{
				holder.post.setImageResource(R.drawable.placeholder_small);
			}
		}

		@Override
		public void setListen(final View view) {
			// TODO Auto-generated method stub
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					selected = Game.this;
					selectedView = view;
					Intent intent = new Intent(context, com.smallcat.test.GameDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", title);
					bundle.putString("attend", attend);
					bundle.putString("source", source);
					bundle.putString("comment", comment);
					bundle.putString("date", date);
					bundle.putString("id", id);
					bundle.putString("place", place);
					bundle.putString("url", url);
					intent.putExtras(bundle);
					((FragmentActivity)context).startActivityForResult(intent, 0);
				}
			});
		}
		
		class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
			@Override
			protected Bitmap doInBackground(Object... params) {
				String url = (String) params[0];
				return ImageLoader.loadImage(url);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				Game.this.bmp = result;
				notifyDataSetChanged();
			}
		}
	}
	
	public class Exp extends Row{
		
		public String title, url, id, comment, image1Url, image2Url, image3Url;
		public Bitmap bmp;

		public Exp(String url, String title, String id, String comment, String image1Url, String image2Url, String image3Url) {
			super(R.layout.include_list_item_myexp);
			this.url = url;
			this.title = title;
			this.id = id;
			this.comment = comment;
			this.image1Url = image1Url;
			this.image2Url = image2Url;
			this.image3Url = image3Url;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(context).inflate(layoutID, null);
			ExpViewHolder holder = new ExpViewHolder();
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.post = (ImageView) view.findViewById(R.id.image);
			holder.layoutID = layoutID;
			view.setTag(holder);
			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			ExpViewHolder holder = (ExpViewHolder)view.getTag();
			holder.title.setText(title);
			if (bmp != null){
				holder.post.setImageBitmap(bmp);
			}
			else if (url != null && !url.equals("")){
				ImageLoadTask imageLoadTask = new ImageLoadTask();
				imageLoadTask.execute(url);
			}
			else{
				holder.post.setImageResource(R.drawable.placeholder_small);
			}
		}

		@Override
		public void setListen(View view) {
			// TODO Auto-generated method stub
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					selectedTwitter = Exp.this;
					Intent intent = new Intent(context, SubmissionActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", title);
					bundle.putString("id", id);
					bundle.putString("comment", comment);
					bundle.putString("image1", image1Url);
					bundle.putString("image2", image2Url);
					bundle.putString("image3", image3Url);
					intent.putExtras(bundle);
					((FragmentActivity)context).startActivityForResult(intent, 0);
				}
			});
		}
		
		class ImageLoadTask extends AsyncTask<Object, Void, Void> {
			
			@Override
			protected Void doInBackground(Object... params) {
				String url = (String) params[0];
				Exp.this.bmp = ImageLoader.loadImage(url);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (result == null) {
					return;
				}
				notifyDataSetChanged();
			}
		}
		
	}
}