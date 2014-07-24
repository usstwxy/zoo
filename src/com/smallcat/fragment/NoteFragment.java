package com.smallcat.fragment;

import java.util.Calendar;

import com.example.smallcat.R;
import com.smallcat.activity.PublishNoticeActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteFragment extends Fragment{
	public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
	
	private View rootView;
	private Bundle bundle;
	private EditText mTitle;
	private EditText mContent;
	private View mSelectDate;
	private EditText mPlace;
	private TextView mTip;
	
	@SuppressLint("SimpleDateFormat") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = (View) inflater.inflate(R.layout.fragment_note, container, false);
		mContent = (EditText)rootView.findViewById(R.id.edt_content);
		mContent.setText(getArguments().getString("comment"));
		mTip = (TextView)rootView.findViewById(R.id.text_submit_tip);
		bundle = getArguments();
		String flag = bundle.getString(PublishNoticeActivity.EXTRA_MANAGE);
		if (flag != null && flag.equals("true")) {
			
			mTip.setText("快捷发布活动，不提供图片功能哦~");
			mContent.setHint("在此写下内容吧:)");
			mSelectDate = (View)rootView.findViewById(R.id.select_starttime);
			mSelectDate.setVisibility(View.VISIBLE);
			mTitle = (EditText)rootView.findViewById(R.id.edt_title);
			mTitle.setVisibility(View.VISIBLE);
			
/*			setDateListener();
			
			if (savedInstanceState != null) {
	            DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag(DATEPICKER_TAG);
	            if (dpd != null) {
	                dpd.setOnDateSetListener(this);
	            }

	            TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
	            if (tpd != null) {
	                tpd.setOnTimeSetListener(this);
	            }
	        }*/
		}	
		
		return rootView;
	}
	
	public String getContent(){
		return mContent.getText().toString();
	}
	
	public String getTitle(){
		return mTitle.getText().toString();
	}
	
	public String getDate(){
		return "";
	}
	
	public String getPlace(){
		return "";
	}

	/*private void setDateListener() {
		Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
        
        
        rootView.findViewById(R.id.txt_startdate).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        rootView.findViewById(R.id.txt_starttime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });
	}
	
	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
		
	}*/
}
