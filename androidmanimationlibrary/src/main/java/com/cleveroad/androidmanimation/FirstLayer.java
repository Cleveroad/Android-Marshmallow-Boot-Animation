package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Александр on 15.02.2016.
 */
public class FirstLayer extends Layer {


	private final DrawableObject[] objects;

	public FirstLayer(Paint bluePaint, Paint greenPaint, Paint yellowPaint, Paint bgPaint) {
		objects = new DrawableObject[4];
		objects[0] = new YellowCircle(yellowPaint, bgPaint);
		objects[1] = new BlueCircle(bluePaint);
		objects[2] = new BlueArc(bluePaint);
		objects[3] = new GreenCircles(greenPaint);
	}

	@Override
	protected void update(@NonNull RectF bounds, long dt, float ddt) {
		for (DrawableObject object : objects) {
			object.update(bounds, dt);
		}
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		for (DrawableObject object : objects) {
			object.draw(canvas);
		}
	}

	private static final class GreenCircles extends DrawableShape {

		private static final float FRACTION_START = Constants.FIRST_FRAME_FRACTION;
		private static final float FRACTION_END = 50 * Constants.FRAME_SPEED;

		private static final float SIZE_FRACTION = 0.75f;
		private static final float START_ANGLE = 135;
		private static final float ENG_ANGLE = 245;
		private static final int CIRCLES_COUNT = 14;
		private static final int VISIBLE_CIRCLES = 6;
		private static final float ANGLE_STEP = 360f / CIRCLES_COUNT;
		private final float[][] circlePositions;
		private final float[] sizes;
		private final float[] coefficients = new float[4];
		private boolean draw;

		public GreenCircles(Paint paint) {
			super(paint);
			circlePositions = new float[CIRCLES_COUNT][2];
			sizes = new float[CIRCLES_COUNT];
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			draw = DrawableUtils.between(ddt, FRACTION_START, FRACTION_END);
			if (!draw) {
				return;
			}
			int i = 0;
			float t = DrawableUtils.normalize(ddt, FRACTION_START, FRACTION_END);
			float angle = START_ANGLE + (ENG_ANGLE - START_ANGLE) * t;
			for (float step = 0; step < 360 + ANGLE_STEP / 2; step += ANGLE_STEP) {
				float cX = getBounds().centerX();
				float cY = getBounds().centerY();
				float pX = cX;
				float pY = getBounds().top;
				float x = DrawableUtils.rotateX(pX, pY, cX, cY, step + angle);
				float y = DrawableUtils.rotateY(pX, pY, cX, cY, step + angle);
				circlePositions[i][0] = x;
				circlePositions[i][1] = y;
				i++;
				if (i >= CIRCLES_COUNT)
					break;
			}
			float step = 1f / (VISIBLE_CIRCLES + 1);
			float d = 1f * VISIBLE_CIRCLES / (CIRCLES_COUNT + VISIBLE_CIRCLES);
			for (i = 0; i < CIRCLES_COUNT; i++) {
				int index = i;
				if (index >= CIRCLES_COUNT) {
					index %= CIRCLES_COUNT;
				}
				getCoefficients(0, i, step, t, d, coefficients);
				float k = DrawableUtils.trapeze(t, 0, coefficients[0], 1, coefficients[1], 1, coefficients[2], 0, coefficients[3]);
				sizes[index] = k;
			}
		}

		private void getCoefficients(float startT, int i, float step, float t, float d, float[] array) {
			float aT = startT + step * i * d;
			float bT = aT + 0.25f * d;
			float cT = bT + 0.5f * d;
			float dT = cT + 0.25f * d;
			array[0] = aT;
			array[1] = bT;
			array[2] = cT;
			array[3] = dT;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			for (int i = 0; i < CIRCLES_COUNT; i++) {
				if (sizes[i] > 0) {
					canvas.drawCircle(circlePositions[i][0], circlePositions[i][1], getBounds().width() * 0.07f * sizes[i], getPaint());
				}
			}
		}
	}

	private static final class BlueArc extends DrawableShape {

		private static final float FRACTION_START = 77 * Constants.FRAME_SPEED;
		private static final float FRACTION_END = 132 * Constants.FRAME_SPEED;


		private static final float SIZE_FRACTION = 0.55f;
		private static final float START_ANGLE = 135f;
		private static final float END_ANGLE = 1260f;
		private static final float START_SWEEP_ANGLE = 0f;
		private static final float END_SWEEP_ANGLE = 180f;
		private float startAngle = START_ANGLE;
		private float sweepAngle;
		private boolean draw;
		private Interpolator accelerate = new AccelerateDecelerateInterpolator();
		private Interpolator decelerate = new AccelerateDecelerateInterpolator();

		public BlueArc(Paint paint) {
			super(new Paint(paint));
			getPaint().setStyle(Paint.Style.STROKE);
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			getPaint().setStrokeWidth(bounds.width() * 0.08f);
			draw = DrawableUtils.between(ddt, FRACTION_START, FRACTION_END);
			if (!draw) {
				startAngle = START_ANGLE;
				return;
			}
			float time = DrawableUtils.normalize(ddt, FRACTION_START, FRACTION_END);
			startAngle = START_ANGLE + accelerate.getInterpolation(time) * END_ANGLE;
			sweepAngle = DrawableUtils.trapeze(decelerate.getInterpolation(time), START_SWEEP_ANGLE, 0, END_SWEEP_ANGLE, 0.25f, END_SWEEP_ANGLE, 0.75f, START_SWEEP_ANGLE, 1);
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (draw) {
				canvas.drawArc(getBounds(), startAngle, sweepAngle, false, getPaint());
			}
		}
	}

	private static final class YellowCircle extends DrawableShape {

		private static final float BLACK_REDUCE_FRACTION_START = 50 * Constants.FRAME_SPEED;
		private static final float BLACK_REDUCE_FRACTION_END = 64 * Constants.FRAME_SPEED;

		private static final float YELLOW_REDUCE_FRACTION_START = 64 * Constants.FRAME_SPEED;
		private static final float YELLOW_REDUCE_FRACTION_END = 80 * Constants.FRAME_SPEED;

		private static final float YELLOW_INVISIBLE_FRACTION_START = 141 * Constants.FRAME_SPEED;

		private static final float SIZE_FRACTION = 0.75f;
		private static final float ZERO_SIZE = 0;
		private static final float LARGE_SIZE = 1;
		private static final float BLACK_INVISIBLE_SIZE = BlueCircle.SIZE_FRACTION / BlueCircle.LARGE_SIZE;
		private static final float SMALL_SIZE = 0.3f;

		private float yellowSize;
		private float blackSize;
		private Paint bgPaint;


		public YellowCircle(Paint paint, Paint bgPaint) {
			super(paint);
			this.bgPaint = bgPaint;
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			yellowSize = computeYellowSizeFraction(ddt) * getBounds().width();
			blackSize = computeBlackSizeFraction(ddt) * getBounds().width();
		}

		private float computeBlackSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, BLACK_REDUCE_FRACTION_START, BLACK_REDUCE_FRACTION_END)) {
				return reduce(LARGE_SIZE * 0.9f, BLACK_INVISIBLE_SIZE, ddt, BLACK_REDUCE_FRACTION_END, BLACK_REDUCE_FRACTION_END - BLACK_REDUCE_FRACTION_START);
			}
			return ZERO_SIZE;
		}

		private float computeYellowSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, BLACK_REDUCE_FRACTION_START, BLACK_REDUCE_FRACTION_END)) {
				return reduce(LARGE_SIZE, BLACK_INVISIBLE_SIZE * 1.4f, ddt, BLACK_REDUCE_FRACTION_END, BLACK_REDUCE_FRACTION_END - BLACK_REDUCE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, BLACK_REDUCE_FRACTION_END, YELLOW_REDUCE_FRACTION_START)) {
				return BLACK_INVISIBLE_SIZE * 1.4f;
			}
			if (DrawableUtils.between(ddt, YELLOW_REDUCE_FRACTION_START, YELLOW_REDUCE_FRACTION_END)) {
				return reduce(BLACK_INVISIBLE_SIZE * 1.4f, SMALL_SIZE, ddt, YELLOW_REDUCE_FRACTION_END, YELLOW_REDUCE_FRACTION_END - YELLOW_REDUCE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, YELLOW_REDUCE_FRACTION_END, YELLOW_INVISIBLE_FRACTION_START)) {
				return SMALL_SIZE;
			}
			return ZERO_SIZE;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (yellowSize > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), yellowSize / 2f, getPaint());
			}
			if (blackSize > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), blackSize / 2f, bgPaint);
			}
		}
	}

	private static final class BlueCircle extends DrawableShape {

		private static final float SMALL_REDUCE_FRACTION_START = Constants.FIRST_FRAME_FRACTION;
		private static final float SMALL_REDUCE_FRACTION_END = 12 * Constants.FRAME_SPEED;

		private static final float SMALL_ENLARGE_FRACTION_START = 44 * Constants.FRAME_SPEED;
		private static final float SMALL_ENLARGE_FRACTION_END = 56 * Constants.FRAME_SPEED;

		private static final float LARGE_REDUCE_FRACTION_START = 58 * Constants.FRAME_SPEED;
		private static final float LARGE_REDUCE_FRACTION_END = 80 * Constants.FRAME_SPEED;

		private static final float LARGE_ENLARGE_FRACTION_START = 133 * Constants.FRAME_SPEED;
		private static final float LARGE_ENLARGE_FRACTION_END = Constants.LAST_FRAME_FRACTION;


		private static final float SIZE_FRACTION = 0.6f;
		private static final float DEFAULT_SIZE = 0.8f;
		private static final float ZERO_SIZE = 0f;
		private static final float LARGE_SIZE = 1f;

		private float diameter;

		public BlueCircle(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		public void update(@NonNull RectF bounds, long dt, float ddt) {
			diameter = computeSizeFraction(ddt) * getBounds().width();
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (diameter > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), diameter / 2f, getPaint());
			}
		}

		private float computeSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, SMALL_REDUCE_FRACTION_START, SMALL_REDUCE_FRACTION_END)) {
				return reduce(LARGE_SIZE, DEFAULT_SIZE, ddt, SMALL_REDUCE_FRACTION_END, SMALL_REDUCE_FRACTION_END - SMALL_REDUCE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, SMALL_REDUCE_FRACTION_END, SMALL_ENLARGE_FRACTION_START)) {
				return DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, SMALL_ENLARGE_FRACTION_START, SMALL_ENLARGE_FRACTION_END)) {
				return enlarge(DEFAULT_SIZE, LARGE_SIZE, ddt, SMALL_ENLARGE_FRACTION_END, SMALL_ENLARGE_FRACTION_END - SMALL_ENLARGE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, SMALL_ENLARGE_FRACTION_END, LARGE_REDUCE_FRACTION_START)) {
				return LARGE_SIZE;
			}
			if (DrawableUtils.between(ddt, LARGE_REDUCE_FRACTION_START, LARGE_REDUCE_FRACTION_END)) {
				return reduce(LARGE_SIZE, ZERO_SIZE, ddt, LARGE_REDUCE_FRACTION_END, LARGE_REDUCE_FRACTION_END - LARGE_REDUCE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, LARGE_REDUCE_FRACTION_END, LARGE_ENLARGE_FRACTION_START)) {
				return ZERO_SIZE;
			}
			if (DrawableUtils.between(ddt, LARGE_ENLARGE_FRACTION_START, LARGE_ENLARGE_FRACTION_END)) {
				return enlarge(ZERO_SIZE, LARGE_SIZE, ddt, LARGE_ENLARGE_FRACTION_END, LARGE_ENLARGE_FRACTION_END - LARGE_ENLARGE_FRACTION_START);
			}

			// default value
			return DEFAULT_SIZE;
		}
	}
}