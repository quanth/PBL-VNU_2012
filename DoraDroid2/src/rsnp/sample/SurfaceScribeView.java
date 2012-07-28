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

/**
 * 關ｽ譖ｸ縺阪ン繝･繝ｼ繧ｯ繝ｩ繧ｹ
 *
 */
public class SurfaceScribeView extends SurfaceView implements ImageProvidor {

	/** 繧ｿ繝�メ縺ｮ霆瑚ｷ｡繧呈�邏阪☆繧九Μ繧ｹ繝�*/
	private ArrayList<ArrayList<Point>> strokes = new ArrayList<ArrayList<Point>>();
	/** 閭梧勹濶ｲ */
	private int backColor = Color.GRAY;

	/** 謠冗判謗剃ｻ也畑繝ｭ繝�け */
	private ReentrantLock lock = new ReentrantLock();
	/** 繧ｪ繝輔せ繧ｯ繝ｪ繝ｼ繝ｳ逕ｨ繝薙ャ繝医�繝�� */
	private Bitmap osb;
	/** 繧ｪ繝輔せ繧ｯ繝ｪ繝ｼ繝ｳ逕ｨ繧ｭ繝｣繝ｳ繝舌せ */
	private Canvas osc;
	/** SurfaceView逕ｨ繧ｳ繝ｼ繝ｫ繝舌ャ繧ｯ */
	private Callback surfcb = new Callback() {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			System.out.println("surfaceChanged");
			osb = null;
			osc = null;
			doDraw();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			doDraw();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	};

	/**
	 * 謖�ｮ壹＠縺溘ヱ繝ｩ繝｡繧ｿ縺ｧScribeView繧ｪ繝悶ず繧ｧ繧ｯ繝医ｒ讒狗ｯ峨☆繧�
	 *
	 * @param context
	 *            繧｢繝励Μ繧ｱ繝ｼ繧ｷ繝ｧ繝ｳ繧ｳ繝ｳ繝�く繧ｹ繝�
	 */
	public SurfaceScribeView(Context context) {
		super(context);

		setFocusable(true);
		initStrokes();

		SurfaceHolder holder = getHolder();
		holder.addCallback(surfcb);
	}

	/**
	 * 謠冗判繧ｹ繝医Ο繝ｼ繧ｯ繧貞�譛溷喧縺吶ｋ
	 */
	private void initStrokes() {

		strokes.clear();
		strokes.add(new ArrayList<Point>());
	}

	/**
	 * 謠冗判繧ｹ繝医Ο繝ｼ繧ｯ繧偵け繝ｪ繧｢縺吶ｋ
	 */
	public void clearStrokes() {

		initStrokes();
		doDraw();
	}

	/**
	 * 閭梧勹濶ｲ繧定ｨｭ螳壹☆繧�
	 *
	 * @param color
	 *            閭梧勹濶ｲ
	 */
	public void setBackColor(int color) {

		backColor = color;
		doDraw();
	}

	/**
	 * 謠冗判縺吶ｋ
	 */
	private void doDraw() {

		Canvas canvas = getHolder().lockCanvas();
		if (canvas != null) {
			if (osc == null) {
				// 繧ｪ繝輔せ繧ｯ繝ｪ繝ｼ繝ｳ逕滓�
				int width = getWidth();
				int height = getHeight();
				osb = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_8888);
				osc = new Canvas(osb);
			}

			// 謠冗判縺吶ｋ繝薙ャ繝医�繝��縺ｮ遶ｶ蜷亥宛蠕｡
			lock.lock();
			try {
				// 繧ｪ繝輔せ繧ｯ繝ｪ繝ｼ繝ｳ縺ｫ謠冗判
				doRender(osc);
			} finally {
				lock.unlock();
			}

			// 逕ｻ髱｢縺ｸ謠冗判
			canvas.drawBitmap(osb, 0, 0, null);
			getHolder().unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * 繝代Λ繝｡繧ｿ縺ｧ謖�ｮ壹＆繧後◆繧ｭ繝｣繝ｳ繝舌せ縺ｫ謠冗判縺吶ｋ縲�
	 *
	 * @param canvas
	 *            謠冗判縺吶ｋ繧ｭ繝｣繝ｳ繝舌せ
	 */
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
	 * (髱�Javadoc)
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
	 * 逕ｻ蜒上ョ繝ｼ繧ｿ繧呈欠螳壹＆繧後◆蠖｢蠑上�繝舌う繝磯�蛻励〒蜿門ｾ励☆繧���NG/JPEG縺ｮ縺ｿ��
	 *
	 * @param type
	 *            逕ｻ蜒上ヵ繧ｩ繝ｼ繝槭ャ繝�
	 * @return 逕ｻ蜒上�繝舌う繝磯�蛻�
	 */
	@Override
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
