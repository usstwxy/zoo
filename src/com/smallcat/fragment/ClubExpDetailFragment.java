package com.smallcat.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.smallcat.R;
import com.smallcat.activity.ClubDetailActivity;
import com.smallcat.activity.ClubHomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class ClubExpDetailFragment extends Fragment {
	
	ListView lv;
	
	public ClubExpDetailFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = (View) inflater.inflate(R.layout.fragment_club_exp_detail, container, false);
		lv = (ListView)rootView.findViewById(R.id.list_twitter_comments);
		
		return rootView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set the List Adapter to display the sample items
        List<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
        for (int i = 0; i < 10; i++) {
        	HashMap<String,Object> map = new HashMap<String, Object>();
        	map.put("title", "lhc");
        	map.put("content", "Nice Article :)");
        	data.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.include_list_item_exp_comment,
        		new String[] {"title", "content"}, new int[] {R.id.lbl_title, R.id.txt_content});
        lv.setAdapter(adapter);
        
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ClubDetailActivity.class);
			    intent.putExtra(ClubHomeActivity.EXTRA_LOCATION, "1");
				startActivity(intent);
			}
		});
    }
}
