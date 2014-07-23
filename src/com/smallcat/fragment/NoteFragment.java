package com.smallcat.fragment;

import com.example.smallcat.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NoteFragment extends Fragment {
	private View rootView;
	private EditText editText;
	
	@SuppressLint("SimpleDateFormat") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = (View) inflater.inflate(R.layout.fragment_note, container, false);
		editText = (EditText) rootView.findViewById(R.id.text_submit);
		return rootView;
	}
	
	public String getText(){
		return editText.getText().toString();
	}
	
}
