package com.smallcat.activity;

import com.example.smallcat.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class PostActivity extends Activity implements OnTouchListener{
	
	public static Bitmap post = null;
	private ImageView image = null;
	
	Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    DisplayMetrics dm = new DisplayMetrics(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		
		image = (ImageView) findViewById(R.id.post);
		
		getWindowManager().getDefaultDisplay().getMetrics(dm);  
		
		if (post != null){
			image.setImageBitmap(post);
			image.setOnTouchListener(this);
			matrix.set(image.getImageMatrix());
			float cx = (dm.widthPixels - post.getWidth())* 0.5f;
			float cy = (dm.heightPixels - post.getHeight()) * 0.5f;
			matrix.postTranslate(cx, cy);
			if (post.getWidth() > dm.widthPixels){
				float scale = (float) dm.widthPixels / (float) post.getWidth();
				matrix.postScale(scale, scale, dm.widthPixels * 0.5f, dm.heightPixels * 0.5f);
			}
			image.setImageMatrix(matrix);
		}
	}

	@Override  
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            matrix.set(view.getImageMatrix());
            savedMatrix.set(matrix);
            start.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            oldDist = spacing(event);
            if (oldDist > 10f) {
                savedMatrix.set(matrix);
                midPoint(mid, event);
                mode = ZOOM;
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
            } else if (mode == ZOOM) {
                float newDist = spacing(event);
                if (newDist > 10f) {
                    matrix.set(savedMatrix);
                    float scale = newDist / oldDist;
                    matrix.postScale(scale, scale, mid.x, mid.y);
                }
            }
            break;
        }
        view.setImageMatrix(matrix);
        return true;
    }
      
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }
      
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
