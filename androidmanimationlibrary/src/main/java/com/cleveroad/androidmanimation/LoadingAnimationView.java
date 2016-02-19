package com.cleveroad.androidmanimation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Android M Loading animation view.
 */
public class LoadingAnimationView extends View {

	public static final int STATE_STARTED = 1;
	public static final int STATE_PAUSED = 2;
	public static final int STATE_STOPPED = 0;

	private static final long UPDATE_INTERVAL = 16;
	private static final int LAYERS_COUNT = 4;

	private final Layer[] layers = new Layer[LAYERS_COUNT];
	private YellowRectangle yellowRectangle;
	private final RectF bounds = new RectF();

	private int state = STATE_STOPPED;
	private long startTime;
	private Timer timer;
	private int bgColor;

	public LoadingAnimationView(Context context) {
		this(context, null);
	}

	public LoadingAnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public LoadingAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public LoadingAnimationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	void fromBuilder(@NonNull AnimationDialogFragment.Builder builder) {
		initValues(
				builder.getFirstColor(getContext()),
				builder.getSecondColor(getContext()),
				builder.getThirdColor(getContext()),
				builder.getFourthColor(getContext()),
				builder.getBackgroundColor(),
				builder.getSpeedCoefficient()
		);
	}



	private void init(Context context, AttributeSet attrs) {
		int googleBlue = ColorUtil.getColor(context, R.color.google_blue);
		int googleYellow = ColorUtil.getColor(context, R.color.google_yellow);
		int googleRed = ColorUtil.getColor(context, R.color.google_red);
		int googleGreen = ColorUtil.getColor(context, R.color.google_green);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingAnimationView);
		int firstColor;
		int secondColor;
		int thirdColor;
		int fourthColor;
		int bgColor;
		float speedCoefficient;
		try {
			firstColor = typedArray.getColor(R.styleable.LoadingAnimationView_lav_firstColor, googleRed);
			secondColor = typedArray.getColor(R.styleable.LoadingAnimationView_lav_secondColor, googleGreen);
			thirdColor = typedArray.getColor(R.styleable.LoadingAnimationView_lav_thirdColor, googleBlue);
			fourthColor = typedArray.getColor(R.styleable.LoadingAnimationView_lav_fourthColor, googleYellow);
			bgColor = typedArray.getColor(R.styleable.LoadingAnimationView_lav_backgroundColor, Color.BLACK);
			speedCoefficient = typedArray.getFloat(R.styleable.LoadingAnimationView_lav_speedCoefficient, Constants.DEFAULT_SPEED_COEFFICIENT);
		} finally {
			typedArray.recycle();
		}

		initValues(firstColor, secondColor, thirdColor, fourthColor, bgColor, speedCoefficient);
	}

	private void initValues(int red, int green, int blue, int yellow, int bgColor, float speedCoefficient) {
		Paint bluePaint = new Paint();
		Paint yellowPaint = new Paint();
		Paint redPaint = new Paint();
		Paint greenPaint = new Paint();
		redPaint.setColor(red);
		redPaint.setAntiAlias(true);
		greenPaint.setColor(green);
		greenPaint.setAntiAlias(true);
		bluePaint.setColor(blue);
		bluePaint.setAntiAlias(true);
		yellowPaint.setColor(yellow);
		yellowPaint.setAntiAlias(true);
		Paint bgPaint = new Paint();
		bgPaint.setColor(bgColor);
		bgPaint.setAntiAlias(true);

		this.bgColor = bgColor;
		layers[0] = new FirstLayer(bluePaint, greenPaint, yellowPaint, bgPaint);
		layers[1] = new SecondLayer(redPaint, yellowPaint, bgPaint);
		layers[2] = new ThirdLayer(redPaint, greenPaint, bgPaint);
		layers[3] = new FourthLayer(redPaint, greenPaint, bluePaint, yellowPaint, bgPaint);
		yellowRectangle = new YellowRectangle(yellowPaint);
		Constants.SPEED_COEFFICIENT = speedCoefficient;
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(bgColor);
		long dt;
		if (state != STATE_STARTED) {
			dt = 0;
		} else {
			long endTime = System.currentTimeMillis();
			dt = endTime - startTime;
			startTime = endTime;
		}
		float left = getPaddingLeft();
		float right = getPaddingRight();
		float maxWidth = 1f * (getWidth() - (left + right)) / LAYERS_COUNT;
		float spacing = maxWidth * 0.1f;
		float size = maxWidth - spacing;
		float halfSize = size / 2f;

		for (int i = 0; i< LAYERS_COUNT; i++) {
			float l = left + i * (size + spacing);
			float t = getHeight() / 2f - halfSize;
			float r = l + size;
			float b = t + size;
			bounds.set(l, t, r, b);
			layers[i].update(bounds, dt);
			layers[i].draw(canvas);
			if (i == 1) {
				yellowRectangle.setFirstValues(bounds.centerX(), bounds.centerY());
			} else if (i == 2) {
				yellowRectangle.setSecondValues(bounds.centerX(), bounds.centerY());
			} else if (i == 3) {
				yellowRectangle.setThirdValues(bounds.centerX(), bounds.centerY());
				yellowRectangle.updateRadius(bounds.height());
			}
		}
		yellowRectangle.update(bounds, dt);
		yellowRectangle.draw(canvas);
	}

	/**
	 * Start animation.
	 */
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

	/**
	 * Pause animation.
	 */
	public void pauseAnimation() {
		if (state != STATE_STARTED) {
			return;
		}
		timer.cancel();
		timer.purge();
		state = STATE_PAUSED;
		invalidate();
	}

	/**
	 * Stop animation.
	 */
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
			layer.reset();
		}
		yellowRectangle.reset();
	}

	/**
	 * Get current state of animation.
	 * @return current state of animation.
	 * @see #STATE_STARTED
	 * @see #STATE_PAUSED
	 * @see #STATE_STOPPED
	 */
	public int getState() {
		return state;
	}
}
