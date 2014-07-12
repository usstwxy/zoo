package com.smallcat.widget;

import com.example.smallcat.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageTextButton extends LinearLayout {

	private View mView;
	private TextView mText;
	
	public ImageTextButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ImageTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton);
	
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.control_imagetextbutton, this);
		
		mView = (View) findViewById(R.id.button);
		mText = (TextView) findViewById(R.id.text);
		
		Drawable nav_left = ta.getDrawable(R.styleable.ImageTextButton_itbIcon);
		nav_left.setBounds(0, 0, nav_left.getMinimumWidth(), nav_left.getMinimumHeight());
		mText.setCompoundDrawables(nav_left, null, null, null);
		mText.setText(ta.getString(R.styleable.ImageTextButton_itbText));
		
		ta.recycle();
	}
	
	public void setTextViewText(String text) {
		mText.setText(text);
	}

}