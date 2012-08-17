/*
 * $Id: SurfaceScribeView.java 212 2010-04-26 07:48:33Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited. FUJITSU CONFIDENTIAL.
 */
package rsnp.sample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class SurfaceScribeView extends SurfaceView implements ImageProvidor {

	private ArrayList<ArrayList<Point>> strokes = new ArrayList<ArrayList<Point>>();
	private int backColor = Color.GRAY;

	private ReentrantLock lock = new ReentrantLock();
	private Bitmap osb;
	private Canvas osc;
	private Callback surfcb = new Callback() {
		
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			System.out.println("surfaceChanged");
			osb = null;
			osc = null;
			doDraw();
		}
		
		public void surfaceCreated(SurfaceHolder holder) {
			doDraw();
		}

		
		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	};

	/**
	 * @param context
	 */
	public SurfaceScribeView(Context context) {
		super(context);

		setFocusable(true);
		initStrokes();

		SurfaceHolder holder = getHolder();
		holder.addCallback(surfcb);
	}

	private void initStrokes() {

		strokes.clear();
		strokes.add(new ArrayList<Point>());
	}

	public void clearStrokes() {

		initStrokes();
		doDraw();
	}

	public void setBackColor(int color) {

		backColor = color;
		doDraw();
	}

	private void doDraw() {

		Canvas canvas = getHolder().lockCanvas();
		if (canvas != null) {
			if (osc == null) {
				int width = getWidth();
				int height = getHeight();
				osb = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_8888);
				osc = new Canvas(osb);
			}

			lock.lock();
			try {
				doRender(osc);
			} finally {
				lock.unlock();
			}

			canvas.drawBitmap(osb, 0, 0, null);
			getHolder().unlockCanvasAndPost(canvas);
		}
	}

	private void doRender(Canvas canvas) {

		canvas.drawColor(backColor);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(3);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);

		for (ArrayList<Point> stroke : strokes) {
			Point p = null;
			for (Point c : stroke) {
				if (p != null) {
					canvas.drawLine(p.x, p.y, c.x, c.y, paint);
				}
				p = c;
			}
		}
	}

	/*
	 *
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	public boolean onTouchEvent(MotionEvent event) {

		int x = (int) event.getX();
		int y = (int) event.getY();

		ArrayList<Point> stroke = strokes.get(strokes.size() - 1);
		stroke.add(new Point(x, y));
		if (event.getAction() == MotionEvent.ACTION_UP) {
			strokes.add(new ArrayList<Point>());
		}
		doDraw();
		return true;
	}

	/**
	 * @param type
	 *      
	 * @return image in byte array 
	 */
	
	public byte[] getImage(String type) {

		CompressFormat format = CompressFormat.JPEG;
		if (type.equals("PNG")) {
			format = CompressFormat.PNG;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		lock.lock();
		try {
			osb.compress(format, 100, out);
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			lock.unlock();
		}

		byte[] bytes = out.toByteArray();
		return bytes;
	}
	
	public void takeImage() {
		
	}

	public int getSemaphore(){
		return 0;
	}
	public void setSemaphore(int semaphore){
		
	}
}
