package com.smallcat.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import com.example.smallcat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smallcat.activity.GameDetailActivity;
import com.smallcat.activity.LoginActivity;
import com.smallcat.data.JsonObj;
import com.smallcat.data.WebAPI;
import com.smallcat.service.AlarmReceiver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class CheckCodeDialogFragment extends DialogFragment {
	
	private String clubID;
	
	public CheckCodeDialogFragment(String clubID) {
		this.clubID = clubID;
	}
	
	public interface CheckCodeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	CheckCodeDialogListener mListener;

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CheckCodeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CheckDialogListener");
        }
    }

	
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		// Use the Builder class for convenient dialog construction
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setCancelable(false)
				.setTitle("验证码:")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Check
						mListener
								.onDialogPositiveClick(CheckCodeDialogFragment.this);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener
								.onDialogPositiveClick(CheckCodeDialogFragment.this);
					}
				});
		
		WebAPI.get("clubkey/opencheck?clubID=" + clubID, null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JsonObj jo = new JsonObj(arg2);
				boolean result = jo.getBool("result");
				if (result){
					getDialog().setTitle("验证码:" + jo.getString("superKey"));
				}
				else{
					getDialog().setTitle("验证码: 显示错误");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public String getClubID() {
		return clubID;
	}


}
