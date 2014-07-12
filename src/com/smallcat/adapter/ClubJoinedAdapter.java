package com.smallcat.adapter;

import com.example.smallcat.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClubJoinedAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context mContext;
	
	public ClubJoinedAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ClubJoinedHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.include_list_item_club, null);
			holder = new ClubJoinedHolder();
			holder.clubTitle = (TextView)convertView.findViewById(R.id.lbl_title);
			holder.comments = (TextView)convertView.findViewById(R.id.txt_comments);
			holder.btn_member = (View)convertView.findViewById(R.id.btnone);
			holder.btn_more = (View)convertView.findViewById(R.id.btntwo);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ClubJoinedHolder)convertView.getTag();
		}
		
		holder.clubTitle.setText("吉他社");
		holder.comments.setText("lhc参加了'大家一起来拨弦'\nlhc参加了'大家一起来拨弦'\nwxy参加了'大家一起来拨弦'\nwmj参加了'大家一起来拨弦'\nothers参加了'大家一起来拨弦'");

		holder.btn_member.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	Toast.makeText(mContext, "社团成员", Toast.LENGTH_SHORT).show();
            }
        });
		
		/*holder.btn_more.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	Toast.makeText(mContext, "更多", Toast.LENGTH_SHORT).show();
            }
        });*/
		
		return convertView;
	}
	
	private final class ClubJoinedHolder {
		public TextView clubTitle;
		public TextView comments;
		public View btn_member;
		public View btn_more;
	}
}
