package com.example.countdowntimer;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CountdownTimer extends Activity {

	public static TextView tv;
	public static SeekBar sb;
	public static Context mContext;
	public static Integer timeLeft = 0;
	public static Button btnStart;
	public static Button btnStop;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);
        
        mContext = this;
        tv = (TextView) this.findViewById(R.id.textView1);
        btnStart = (Button) this.findViewById(R.id.buttonStart);
        btnStop = (Button) this.findViewById(R.id.buttonStop);
        sb = (SeekBar) this.findViewById(R.id.seekBar1);
        sb.setBackgroundDrawable(drawScale());
        setListeners();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_countdown_timer, menu);
        return true;
    }
*/    
    public BitmapDrawable drawScale(){
    	Paint paint;
    	Path path;
    	Canvas canvas;
    	Bitmap bitmap;
    	
    	paint = new Paint();
    	paint.setStrokeWidth(0);
    	paint.setStyle(Paint.Style.STROKE);
    	bitmap = Bitmap.createBitmap(241, 30, Bitmap.Config.ARGB_8888);
    	path = new Path();
    	canvas = new Canvas(bitmap);
    	
    	for(int i = 0; i < 17; i++){
    		path.reset();
    		if(i == 5 || i == 10 || i == 15){
    			paint.setColor(Color.WHITE);
    		}else{
    			paint.setColor(Color.GRAY);
    		}
    		
    		path.moveTo(i * 16, 5);
    		path.quadTo(i * 16, 5, i * 16, 15);
    		canvas.drawPath(path, paint);
    	}
    	BitmapDrawable bd = new BitmapDrawable(bitmap);
    	
    	return bd;
    }
    
    public void setListeners(){
    	sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
    		@Override
    		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
    			timeLeft = progress * 60;
    			if(fromUser){
    				showTime(progress * 60);
    			}
    			if(fromUser && (progress > 0)){
    				btnStart.setEnabled(true);
    			}else{
    				btnStart.setEnabled(false);
    			}
    			if(progress == 0){
    				btnStop.setEnabled(false);
    			}
    		}
    		
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
		});
    	btnStart.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			Intent intent = new Intent(mContext, TimerService.class);
    			intent.putExtra("conuter", timeLeft);
    			startService(intent);
    			btnStart.setEnabled(false);
    			btnStop.setEnabled(true);
    			sb.setEnabled(false);
    		}
    	});
    	
    	btnStop.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			Intent i = new Intent(mContext, TimerService.class);
    			mContext.stopService(i);
    			btnStop.setEnabled(false);
    			btnStart.setEnabled(true);
    			sb.setEnabled(true);
    		}
    	});
    	
    	((Button)findViewById(R.id.buttonSettings)).setOnClickListener(
    			new OnClickListener(){
    				public void onClick(View v){
    					Intent intent = new Intent(CountdownTimer.this, Preferences.class);
    					startActivity(intent);
    				}
    			});
    }
    
    public static void showTime(int timeSeconds){
    	SimpleDateFormat form = new SimpleDateFormat("mm:ss");
    	tv.setText(form.format(timeSeconds * 1000));
    }
    
    public static void countdown(int counter){
    	showTime(counter);
    	timeLeft = counter;
    	if(counter % 60 == 0){
    		sb.setProgress(counter/60);
    	}else{
    		sb.setProgress(counter / 60 + 1);
    	}
    	if(counter != 0){
    		btnStop.setEnabled(true);
    		btnStart.setEnabled(false);
    		sb.setEnabled(false);
    	}else{
    		btnStop.setEnabled(false);
    		btnStart.setEnabled(false);
    		sb.setEnabled(true);
    	}
    }
}
