package com.smallcat.adapter;

import java.util.ArrayList;

import com.example.smallcat.PublishSampleActivity;
import com.example.smallcat.R;
import com.smallcat.activity.ClubHomeActivity;
import com.smallcat.activity.DetailActivity;
import com.smallcat.activity.MainActivity;
import com.smallcat.adapter.FindAdapter.Activity;
import com.smallcat.adapter.FindAdapter.Row;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ClubJoinedAdapter extends BaseAdapter{

	private ArrayList<Row> rows = new ArrayList<Row>();
	private Context mContext;
	private View contentView;
	private PopupWindow mPopUpWindow;
	
	public ClubJoinedAdapter(Context context) {
		mContext = context;
		contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_club_manage, null, true);
		mPopUpWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
    	mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());// 有了这句才可以点击返回（撤销）按钮dismiss()popwindow  
    	mPopUpWindow.setOutsideTouchable(true);
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
		row.setListen(view, row);
		return view;
	}
	
	public void AddCategory(String name, String count){
		rows.add(new Category(name, count){});
	}
	
	public void AddClub(String clubID, String clubName, String twitters, String role){
		rows.add(new Club(clubID, clubName, twitters, role){});
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
		public TextView clubName;
		public TextView twitters;
		public View btn_more;
		public TextView btn_publish;
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

		@Override
		public void setListen(View view, Row row) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Club extends Row{
		
		public String clubName;
		public String twitters;
		public String clubID;
		public String role;
		
		public Club(String clubID, String clubName, String twitters, String role){
			super(R.layout.include_list_item_club);
			this.clubName = clubName;
			this.twitters = twitters;
			this.clubID = clubID;
			this.role = role;
		}

		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(mContext).inflate(layoutID, null);
			ClubViewHolder holder = new ClubViewHolder();
			holder.clubName = (TextView)view.findViewById(R.id.lbl_title);
			holder.twitters = (TextView)view.findViewById(R.id.txt_twitters);
			holder.btn_more = (ImageButton)view.findViewById(R.id.btn_more);
			holder.btn_publish = (TextView)contentView.findViewById(R.id.btn_publish);
			holder.layoutID = layoutID;
			view.setTag(holder);
			
			holder.clubName.setText(this.clubName);
			holder.twitters.setText(this.twitters);
			
			holder.btn_more.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	try {  
	                    if (mPopUpWindow.isShowing()) {  
	  
	                    	mPopUpWindow.dismiss();  
	                    }  
	                    mPopUpWindow.showAsDropDown(v, 8, -8);
	  
	                } catch (Exception e) {  
	                	Toast.makeText(mContext, "社团成员", Toast.LENGTH_SHORT).show();
	                }  
	            	
	            }
	        });
			
			holder.btn_publish.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	mPopUpWindow.dismiss();  
	            	Intent intent = new Intent(mContext, PublishSampleActivity.class);
	            	
					Bundle bundle = new Bundle();
					bundle.putString(MainActivity.EXTRA_CTITLE, clubName);
					bundle.putString(MainActivity.EXTRA_CID, clubID);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
	            }
	        });
			
			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			ClubViewHolder holder = (ClubViewHolder)view.getTag();
			
			holder.clubName.setText(this.clubName);
			holder.twitters.setText(this.twitters);
			
			holder.btn_more.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	try {  
	                    if (mPopUpWindow.isShowing()) {  
	  
	                    	mPopUpWindow.dismiss();  
	                    }  
	                    mPopUpWindow.showAsDropDown(v, 8, -8);
	  
	                } catch (Exception e) {  
	                	Toast.makeText(mContext, "社团成员", Toast.LENGTH_SHORT).show();
	                }  
	            }
	        });
			
			holder.btn_publish.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	mPopUpWindow.dismiss();  
	            	Intent intent = new Intent(mContext, PublishSampleActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(MainActivity.EXTRA_CTITLE, clubName);
					bundle.putString(MainActivity.EXTRA_CID, clubID);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
	            }
	        });
		}
		
		@Override
		public void setListen(final View view, final Row row) {
			// TODO Auto-generated method stub
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext, ClubHomeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(MainActivity.EXTRA_CTITLE, clubName);
					bundle.putString(MainActivity.EXTRA_CID, clubID);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
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

		@Override
		public void setListen(View view, Row row) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
