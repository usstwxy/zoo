package com.smallcat.dialog;

import com.example.smallcat.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class CheckDialogFragment extends DialogFragment {
	
	private String checkCode;
	private String clubID;
	
	public CheckDialogFragment(String clubID) {
		this.clubID = clubID;
	}
	
	public interface CheckDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	CheckDialogListener mListener;

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CheckDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CheckDialogListener");
        }
    }

	
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_check, null);
		builder.setView(view);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
		                // Check
					   EditText edtCheck = (EditText)view.findViewById(R.id.edt_check);
					   checkCode = edtCheck.getText().toString();
					   mListener.onDialogPositiveClick(CheckDialogFragment.this);

		           }
			   })
	           .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	        	   public void onClick(DialogInterface dialog, int id) {
	        		   mListener.onDialogPositiveClick(CheckDialogFragment.this);
	        		   CheckDialogFragment.this.getDialog().cancel();
	        	   }
	           });
		
		// Create the AlertDialog object and return it
		return builder.create();
	}


	public String getCheckCode() {
		return checkCode;
	}

	public String getClubID() {
		return clubID;
	}
	
}
