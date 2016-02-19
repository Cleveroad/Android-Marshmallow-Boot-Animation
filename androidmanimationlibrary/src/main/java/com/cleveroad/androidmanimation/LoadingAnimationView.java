package com.cleveroad.androidmanimation;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

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

	private void fromBuilder(@NonNull Builder builder) {
		if (builder.firstColor == null)
			builder.firstColor = getColor(builder.context, R.color.google_red);
		if (builder.secondColor == null)
			builder.secondColor = getColor(builder.context, R.color.google_green);
		if (builder.thirdColor == null)
			builder.thirdColor = getColor(builder.context, R.color.google_blue);
		if (builder.fourthColor == null)
			builder.fourthColor = getColor(builder.context, R.color.google_yellow);
		if (builder.backgroundColor == null)
			builder.backgroundColor = Color.BLACK;
		initValues(builder.firstColor, builder.secondColor, builder.thirdColor, builder.fourthColor,
				builder.backgroundColor, builder.speedCoefficient);
	}

	@SuppressWarnings("deprecation")
	private int getColor(@NonNull Context context, int colorId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return context.getColor(colorId);
		}
		return context.getResources().getColor(colorId);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context, AttributeSet attrs) {
		int googleBlue = getContext().getResources().getColor(R.color.google_blue);
		int googleYellow = getContext().getResources().getColor(R.color.google_yellow);
		int googleRed = getContext().getResources().getColor(R.color.google_red);
		int googleGreen = getContext().getResources().getColor(R.color.google_green);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingAnimationView);
		int firstColor;
		int secondColor;
		int thirdColor;
		int fourthColor;
		int bgColor;
		float speedCoefficient;
		try {
			firstColor = typedArray.getColor(R.styleable.LoadingAnimationView_la_firstColor, googleRed);
			secondColor = typedArray.getColor(R.styleable.LoadingAnimationView_la_secondColor, googleGreen);
			thirdColor = typedArray.getColor(R.styleable.LoadingAnimationView_la_thirdColor, googleBlue);
			fourthColor = typedArray.getColor(R.styleable.LoadingAnimationView_la_fourthColor, googleYellow);
			bgColor = typedArray.getColor(R.styleable.LoadingAnimationView_la_backgroundColor, Color.BLACK);
			speedCoefficient = typedArray.getFloat(R.styleable.LoadingAnimationView_la_speedCoefficient, Constants.DEFAULT_SPEED_COEFFICIENT);
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
		//float top = getPaddingTop();
		float right = getPaddingRight();
		//float bottom = getPaddingBottom();
		float maxWidth = 1f * (getWidth() - (left + right)) / LAYERS_COUNT;
		float spacing = maxWidth * 0.1f;
		float size = maxWidth - spacing;
		float halfSize = size / 2f;
		// float cy = (getHeight() - (top + bottom) - size) / 2f;

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

	/**
	 * Builder class for dialog with loading animation.
	 */
	public static final class Builder {

		private final Context context;
		private Integer firstColor, secondColor, thirdColor, fourthColor;
		private Integer backgroundColor;
		private float speedCoefficient;
		private DialogInterface.OnDismissListener onDismissListener;
		private DialogInterface.OnShowListener onShowListener;

		public Builder(@NonNull Context context) {
			this.context = context;
		}

		/**
		 * Set first color. Default value: red.
		 * @param firstColor first color
		 */
		public Builder setFirstColor(@ColorInt int firstColor) {
			this.firstColor = firstColor;
			return this;
		}

		/**
		 * Set second color. Default value: green.
		 * @param secondColor second color
		 */
		public Builder setSecondColor(@ColorInt int secondColor) {
			this.secondColor = secondColor;
			return this;
		}

		/**
		 * Set third color. Default value: blue.
		 * @param thirdColor third color
		 */
		public Builder setThirdColor(@ColorInt int thirdColor) {
			this.thirdColor = thirdColor;
			return this;
		}

		/**
		 * Set fourth color. Default value: yellow.
		 * @param fourthColor fourth color
		 */
		public Builder setFourthColor(@ColorInt int fourthColor) {
			this.fourthColor = fourthColor;
			return this;
		}

		/**
		 * Set background color. Default value: black.
		 * @param backgroundColor background color
		 */
		public Builder setBackgroundColor(@ColorInt int backgroundColor) {
			this.backgroundColor = backgroundColor;
			return this;
		}

		/**
		 * Set speed coefficient. Default value: 1.
		 * @param speedCoefficient speed coefficient in range <code>[0..Integer.MAX_VALUE]</code>
		 */
		public Builder setSpeedCoefficient(float speedCoefficient) {
			this.speedCoefficient = speedCoefficient;
			return this;
		}

		/**
		 * Set onDismissListener.
		 * @param onDismissListener instance of listener
		 */
		public Builder setOnDismissListener(@Nullable DialogInterface.OnDismissListener onDismissListener) {
			this.onDismissListener = onDismissListener;
			return this;
		}

		/**
		 * Set onShowListener.
		 * @param onShowListener instance of listener
		 */
		public Builder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
			this.onShowListener = onShowListener;
			return this;
		}

		/**
		 * Create new dialog with loading animation.
		 * @return created dialog.
		 */
		public Dialog build() {
			if (speedCoefficient < 0) {
				throw new IllegalArgumentException("Speed coefficient must be positive.");
			}
			if (speedCoefficient == 0) {
				speedCoefficient = Constants.DEFAULT_SPEED_COEFFICIENT;
			}
			Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.view_loading_animation);
			final LoadingAnimationView animation = (LoadingAnimationView) dialog.findViewById(R.id.animation);
			animation.fromBuilder(this);
			dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					animation.startAnimation();
					if (onShowListener != null) {
						onShowListener.onShow(dialog);
					}
				}
			});
			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					animation.stopAnimation();
					if (onDismissListener != null) {
						onDismissListener.onDismiss(dialog);
					}
				}
			});
			return dialog;
		}
	}
}
