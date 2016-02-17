package com.cleveroad.androidmanimation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Александр on 15.02.2016.
 */
public class LoadingAnimation extends View {

	public static final int STATE_STARTED = 1;
	public static final int STATE_PAUSED = 2;
	public static final int STATE_STOPPED = 0;
	private static final long UPDATE_INTERVAL = 16;
	private static final int LAYERS_COUNT = 4;

	private final Layer[] layers = new Layer[LAYERS_COUNT];
	private final RectF bounds = new RectF();

	private int state = STATE_STOPPED;
	private long startTime;
	private Timer timer;

	public LoadingAnimation(Context context) {
		this(context, null);
	}

	public LoadingAnimation(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadingAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public LoadingAnimation(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		Paint bluePaint = new Paint();
		Paint yellowPaint = new Paint();
		Paint redPaint = new Paint();
		Paint greenPaint = new Paint();
		bluePaint.setColor(context.getResources().getColor(R.color.google_blue));
		bluePaint.setAntiAlias(true);
		yellowPaint.setColor(context.getResources().getColor(R.color.google_yellow));
		yellowPaint.setAntiAlias(true);
		redPaint.setColor(context.getResources().getColor(R.color.google_red));
		redPaint.setAntiAlias(true);
		greenPaint.setColor(context.getResources().getColor(R.color.google_green));
		greenPaint.setAntiAlias(true);
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.BLACK);
		bgPaint.setAntiAlias(true);
		layers[0] = new FirstLayer(bluePaint, greenPaint, yellowPaint, bgPaint);
		layers[1] = new SecondLayer(redPaint, yellowPaint, bgPaint);
		layers[2] = new ThirdLayer(redPaint, greenPaint, yellowPaint);
		layers[3] = new FourthLayer(redPaint, greenPaint, bluePaint, yellowPaint);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		long dt;
		if (state != STATE_STARTED) {
			dt = 0;
		} else {
			long endTime = System.currentTimeMillis();
			dt = endTime - startTime;
			startTime = endTime;
		}
		float left = getPaddingLeft();
		float top = getPaddingTop();
		float right = getPaddingRight();
		float bottom = getPaddingBottom();
		float maxWidth = 1f * (getWidth() - (left + right)) / LAYERS_COUNT;
		float spacing = maxWidth * 0.1f;
		float size = maxWidth - spacing;
		float halfSize = size / 2f;
		float cy = (getHeight() - (top + bottom) - size) / 2f;

		for (int i = 0; i< LAYERS_COUNT; i++) {
			// TODO: 15.02.2016 remove check
			if (layers[i] == null)
				continue;
			float l = left + i * (size + spacing);
			float t = getHeight() / 2f - halfSize;
			float r = l + size;
			float b = t + size;
			bounds.set(l, t, r, b);
			layers[i].update(bounds, dt);
			layers[i].draw(canvas);
		}
	}

	public void startAnimation() {
		if (state == STATE_STARTED) {
			return;
		}
		state = STATE_STARTED;
		startTime = System.currentTimeMillis();
		timer = new Timer("Android M Animation Timer");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				postInvalidate();
			}
		}, UPDATE_INTERVAL, UPDATE_INTERVAL);
	}

	public void pauseAnimation() {
		if (state != STATE_STARTED) {
			return;
		}
		timer.cancel();
		timer.purge();
		state = STATE_PAUSED;
		invalidate();
	}

	public void stopAnimation() {
		if (state == STATE_STOPPED) {
			return;
		}
		timer.cancel();
		timer.purge();
		state = STATE_STOPPED;
		startTime = 0;
		resetAll();
		invalidate();
	}

	private void resetAll() {
		for (Layer layer : layers) {
			// TODO: 15.02.2016 remove check
			if (layer == null)
				continue;
			layer.reset();
		}
	}

	public int getState() {
		return state;
	}
}
