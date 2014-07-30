package com.smallcat.adapter;

import java.util.ArrayList;

import com.example.smallcat.R;
import com.smallcat.activity.ClubHomeActivity;
import com.smallcat.activity.ClubsActivity;

import com.smallcat.activity.GameDetailActivity;
import com.smallcat.activity.MainActivity;
import com.smallcat.activity.PostGameActivity;
import com.smallcat.adapter.FindAdapter.Game;
import com.smallcat.adapter.FindAdapter.Row;
import com.smallcat.adapter.FindAdapter.Exp;
import com.smallcat.adapter.FindAdapter.ViewHolder;
import com.smallcat.adapter.FindAdapter.Game.ImageLoadTask;

import com.smallcat.dialog.CheckCodeDialogFragment;
import com.smallcat.dialog.CheckDialogFragment;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class HomeAdapter extends BaseAdapter{

	private ArrayList<Row> rows = new ArrayList<Row>();
	private Context mContext;
	
	public HomeAdapter(Context context) {
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
	
	public void AddCategory(String name, String count){
		rows.add(new Category(name, count){});
	}
	
	public void AddClub(String url, String clubID, String clubName, String twitters, String role){
		rows.add(new Club(url, clubID, clubName, twitters, role){});
	}
	
	public void AddHeader(){
		rows.add(new Header(){});
	}
	
	public void AddClubRow(String url, String clubID, String clubName, String members, String isMember){
		rows.add(new ClubRow(url, clubID, clubName, members, isMember){});
	}
	
	abstract class ViewHolder{ 
		
		public int layoutID;
	}
	
	class HeaderViewHolder extends ViewHolder{
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
		public View contentView;
		public ImageView logo;
	}
	
	class ClubRowViewHolder extends ViewHolder{
		public TextView clubName;
		public TextView members;
		public TextView txtJoin;
		public View btn_join;
		public ImageView logo;
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
			super(R.layout.include_header_home);
		}

		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(mContext).inflate(layoutID, null);
			HeaderViewHolder holder = new HeaderViewHolder();
			holder.layoutID = layoutID;
			holder.btn_exps = (View)view.findViewById(R.id.btn_exps);
			holder.btn_search = (View)view.findViewById(R.id.btn_search);
			view.setTag(holder);

			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			HeaderViewHolder holder = (HeaderViewHolder)view.getTag();
			
			holder.btn_search.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	Intent intent = new Intent(mContext, ClubsActivity.class);
					mContext.startActivity(intent);
	            }
	        });
		}

		@Override
		public void setListen(View view) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Club extends Row{
		
		public String clubName, twitters, clubID, role, url;
		public Bitmap bmp;
		public PopupWindow mPopUpWindow;
		
		public Club(String url, String clubID, String clubName, String twitters, String role){
			super(R.layout.include_list_item_club);
			this.url = url;
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
			holder.contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_club_manage, null, true);
			holder.logo = (ImageView)view.findViewById(R.id.image);
			holder.layoutID = layoutID;
			view.setTag(holder);
			
			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			ClubViewHolder holder = (ClubViewHolder)view.getTag();
			
			holder.clubName.setText(this.clubName);
			holder.twitters.setText(this.twitters);
			if (bmp != null){
				holder.logo.setImageBitmap(bmp);
			}
			else if (url != null && !url.equals("")){
				ImageLoadTask imageLoadTask = new ImageLoadTask();
				imageLoadTask.execute(url);
			}
			else{
				holder.logo.setImageResource(R.drawable.placeholder_small);
			}
			
			mPopUpWindow = new PopupWindow(holder.contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
	    	mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());// 有了这句才可以点击返回（撤销）按钮dismiss()popwindow  
	    	mPopUpWindow.setOutsideTouchable(true);
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
			
			holder.contentView.findViewById(R.id.btn_publish).setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {

	            	mPopUpWindow.dismiss();
	            	Intent intent = new Intent(mContext, PostGameActivity.class);
	            	
					Bundle bundle = new Bundle();
					bundle.putString(MainActivity.EXTRA_CTITLE, clubName);
					bundle.putString(MainActivity.EXTRA_CID, clubID);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
	            }
	        });
			
			holder.contentView.findViewById(R.id.btn_open).setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	            	mPopUpWindow.dismiss();
	            	FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
	            	FragmentTransaction ft = fm.beginTransaction();
	            	
	            	Fragment prev = fm.findFragmentByTag("checkcodedialog");
	            	if (prev != null)
	            		ft.remove(prev);
	            	ft.addToBackStack(null);

	                // Create and show the dialog.
	            	DialogFragment newFragment = new CheckCodeDialogFragment(clubID);
	                newFragment.show(ft, "checkcodedialog");
	            }
	        });
		}
		
		@Override
		public void setListen(final View view) {
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
		
		class ImageLoadTask extends AsyncTask<Object, Void, Void> {
			
			@Override
			protected Void doInBackground(Object... params) {
				String url = (String) params[0];
				Club.this.bmp = ImageLoader.loadImage(url);
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
			View view = LayoutInflater.from(mContext).inflate(layoutID, null);
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
	
	class ClubRow extends Row{
		
		public String clubName, members, clubID, url, isMember;
		public Bitmap bmp;
		public PopupWindow mPopUpWindow;
		
		public ClubRow(String url, String clubID, String clubName, String members, String isMember){
			super(R.layout.include_list_item_club_row);
			this.url = url;
			this.clubName = clubName;
			this.members = members;
			this.clubID = clubID;
			this.isMember = isMember;
		}

		@Override
		public View set() {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(mContext).inflate(layoutID, null);
			ClubRowViewHolder holder = new ClubRowViewHolder();
			holder.clubName = (TextView)view.findViewById(R.id.lbl_name);
			holder.members = (TextView)view.findViewById(R.id.lbl_members);
			holder.btn_join = (View)view.findViewById(R.id.btn_join);
			holder.logo = (ImageView)view.findViewById(R.id.image);
			holder.txtJoin = (TextView)holder.btn_join.findViewById(R.id.txt_join);
			holder.layoutID = layoutID;
			view.setTag(holder);
			
			return view;
		}

		@Override
		public void set(View view) {
			// TODO Auto-generated method stub
			ClubRowViewHolder holder = (ClubRowViewHolder)view.getTag();
			
			holder.clubName.setText(this.clubName);
			holder.members.setText(this.members + "名成员");
			if (bmp != null){
				holder.logo.setImageBitmap(bmp);
			}
			else if (url != null && !url.equals("")){
				ImageLoadTask imageLoadTask = new ImageLoadTask();
				imageLoadTask.execute(url);
			}
			else{
				holder.logo.setImageResource(R.drawable.placeholder_small);
			}
			
			if (isMember.equals("false")) {
				holder.txtJoin.setText("申请加入");
				holder.txtJoin.setBackgroundResource(R.color.umano_orange);
				holder.btn_join.setClickable(true);

				holder.btn_join.setOnClickListener(new OnClickListener() {
		            
		            @Override
		            public void onClick(View v) {
		            	FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
		            	FragmentTransaction ft = fm.beginTransaction();
		            	
		            	Fragment prev = fm.findFragmentByTag("checkdialog");
		            	if (prev != null)
		            		ft.remove(prev);
		            	ft.addToBackStack(null);

		                // Create and show the dialog.
		            	DialogFragment newFragment = new CheckDialogFragment(clubID);
		                newFragment.show(ft, "checkdialog");

		            }
		        });
			} else {
				holder.txtJoin.setText("已加入");
				holder.txtJoin.setBackgroundResource(R.color.umano_gray);
				holder.btn_join.setClickable(false);
			}
		}
		
		@Override
		public void setListen(final View view) {
			// TODO Auto-generated method stub
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(mContext, "社团主页", Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		class ImageLoadTask extends AsyncTask<Object, Void, Void> {
			
			@Override
			protected Void doInBackground(Object... params) {
				String url = (String) params[0];
				ClubRow.this.bmp = ImageLoader.loadImage(url);
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
