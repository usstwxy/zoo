package com.smallcat.adapter;

import java.util.ArrayList;

import com.example.smallcat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ClubJoinedAdapter extends BaseAdapter{

	private ArrayList<Row> rows = new ArrayList<Row>();
	private Context mContext;
	
	public ClubJoinedAdapter(Context context) {
		mContext = context;
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
			view = row.set();
		}else{
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null || holder.layoutID != row.layoutID){
				view = row.set();
			}else{
				row.set(view);
			}
		}
		return view;
	}
	
/*	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ClubJoinedHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.include_list_item_club, null);
			holder = new ClubJoinedHolder();
			holder.clubTitle = (TextView)convertView.findViewById(R.id.lbl_title);
			holder.btn_more = (View)convertView.findViewById(R.id.btn_more);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ClubJoinedHolder)convertView.getTag();
		}
		
		holder.clubTitle.setText("吉他社");
		
		holder.btn_more.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	Toast.makeText(mContext, "社团成员", Toast.LENGTH_SHORT).show();
            }
        });
		
		return convertView;
	}*/
	
	public void AddCategory(String name, String count){
		rows.add(new Category(name, count){});
	}
	
	public void AddClub(String title){
		rows.add(new Club(title){});
	}
	
	public void AddFooter(){
		rows.add(new Footer(){});
	}
	
	abstract class ViewHolder{ 
		
		public int layoutID;
	}
	
	class FooterViewHolder extends ViewHolder{
		public View btn_exps;
		public View btn_search;
	}
	
	class CategoryViewHolder extends ViewHolder{
		public TextView name, count;
	}
	
	class ClubViewHolder extends ViewHolder{
		public TextView clubTitle;
		public View btn_more;
	}
	
	abstract class Row{

		public int layoutID;
		
		public Row(int layoutID){
			this.layoutID = layoutID;
		}
		
		public abstract View set();
		
		public abstract void set(View view);
	}
	
	class Footer extends Row{
		public Footer(){
			super(R.layout.include_footer_club);
		}

		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(mContext).inflate(layoutID, null);
			FooterViewHolder holder = new FooterViewHolder();
			holder.layoutID = layoutID;
			holder.btn_exps = (View)view.findViewById(R.id.btn_exps);
			holder.btn_search = (View)view.findViewById(R.id.btn_search);
			view.setTag(holder);
			
			holder.btn_exps.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	Toast.makeText(mContext, "显示我的活动", Toast.LENGTH_SHORT).show();
	            }
	        });
			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Club extends Row{
		
		public String title;
		
		public Club(String title){
			super(R.layout.include_list_item_club);
			this.title = title;
		}

		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(mContext).inflate(layoutID, null);
			ClubViewHolder holder = new ClubViewHolder();
			holder.clubTitle = (TextView)view.findViewById(R.id.date);
			holder.btn_more = (ImageButton)view.findViewById(R.id.btn_more);
			holder.layoutID = layoutID;
			view.setTag(holder);
			
			holder.clubTitle.setText(this.title);
			
			holder.btn_more.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	Toast.makeText(mContext, "社团成员", Toast.LENGTH_SHORT).show();
	            }
	        });
			
			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			ClubViewHolder holder = (ClubViewHolder)view.getTag();
			
			holder.clubTitle.setText("吉他社");
			
			holder.btn_more.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	Toast.makeText(mContext, "社团成员", Toast.LENGTH_SHORT).show();
	            }
	        });
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
			View view = LayoutInflater.from(mContext).inflate(layoutID, null);
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
	}
	
}
