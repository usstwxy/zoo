package com.smallcat.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smallcat.R;
import com.smallcat.activity.DetailActivity;
import com.smallcat.activity.MainActivity;

public class FindAdapter extends BaseAdapter{
	
	private ArrayList<Row> rows = new ArrayList<Row>();
	private Activity selected = null;
	private View selectedView = null;
	private Context context;
	
	public FindAdapter(Context context){
		this.context = context;
	}
	
	public void updateActivity(){
		int i = Integer.valueOf(selected.attend) + 1;
		selected.attend = String.valueOf(i);
		ActivityViewHolder holder = (ActivityViewHolder) selectedView.getTag();
		holder.attend.setText(selected.attend);
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
			}else{
				row.set(view);
			}
		}
		row.setListen(view, row);
		return view;
	}
	
	public void AddHeader(){
		rows.add(new Header(){});
	}
	
	public void AddCategory(String name, String count){
		rows.add(new Category(name, count){});
	}
	
	public void AddActivity(String title, String attend, String source, String comment, String date, String id){
		rows.add(new Activity(title, attend, source, comment, date, id){});
	}
	
	abstract class ViewHolder{
		
		public int layoutID;
	}
	
	class HeaderViewHolder extends ViewHolder{
		
	}
	
	class CategoryViewHolder extends ViewHolder{
		
		public TextView name, count;
	}
	
	class ActivityViewHolder extends ViewHolder{

		public TextView title, attend, source, comment, date;
	}
	
	abstract class Row{

		public int layoutID;
		
		public Row(int layoutID){
			this.layoutID = layoutID;
		}
		
		public abstract View set();
		
		public abstract void set(View view);
		
		public abstract void setListen(View view, Row row);
	}
	
	class Header extends Row{
		public Header(){
			super(R.layout.find_header);
		}

		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(context).inflate(layoutID, null);
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
		public void setListen(View view, Row row) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Category extends Row{
		
		public String name, count;
		
		public Category(String name, String count){
			super(R.layout.find_category);
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
			holder.name.setText(name);
			holder.count.setText(count);
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
		public void setListen(View view, Row row) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public class Activity extends Row{
		
		public String title, attend, source, comment, date, id;
		
		public Activity(String title, String attend, String source, String comment, String date, String id){
			super(R.layout.find_activity);
			this.title = title;
			this.attend = attend;
			this.source = source;
			this.comment = comment;
			this.date = date;
			this.id = id;
		}
		
		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(context).inflate(layoutID, null);
			ActivityViewHolder holder = new ActivityViewHolder();
			holder.title = (TextView) view.findViewById(R.id.date);
			holder.attend = (TextView) view.findViewById(R.id.lbl_likes);
			holder.source = (TextView) view.findViewById(R.id.lbl_source_name);
			holder.comment = (TextView) view.findViewById(R.id.lbl_comments);
			holder.date = (TextView) view.findViewById(R.id.lbl_data);
			holder.title.setText(title);
			holder.source.setText(source);
			holder.attend.setText(attend);
			holder.comment.setText(comment);
			holder.date.setText(date);
			holder.layoutID = layoutID;
			view.setTag(holder);
			return view;
		}

		@SuppressLint("SimpleDateFormat") @Override
		public void set(View view) {
			// TODO Auto-generated method stub
			ActivityViewHolder holder = (ActivityViewHolder)view.getTag();
			
			
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = sdf.parse(date);
				Date now = new Date();
				long interval = (d.getTime() - now.getTime()) / (24 * 60 * 60 * 1000);
				holder.title.setText(title);
				holder.attend.setText(attend);
				holder.source.setText(source);
				holder.comment.setText(comment);
				holder.date.setText("还有" + String.valueOf(interval) + "天");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void setListen(final View view, final Row row) {
			// TODO Auto-generated method stub
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					selected = (Activity) row;
					selectedView = view;
					Intent intent = new Intent(context, DetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", title);
					bundle.putString("attend", attend);
					bundle.putString("source", source);
					bundle.putString("comment", comment);
					bundle.putString("date", date);
					bundle.putString("id", id);
					intent.putExtras(bundle);
					((FragmentActivity)context).startActivityForResult(intent, 0);
				}
			});
		}
	}
	
}