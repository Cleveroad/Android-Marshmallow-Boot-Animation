package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.Arrays;

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

		private static final float SIZE_FRACTION = 0.75f;
		private static final float SPACING_FRACTION = (1 - SIZE_FRACTION) / 2f;
		private static final float START_ANGLE = 135;
		private static final float ENG_ANGLE = 245;
		private static final int CIRCLES_COUNT = 14;
		private static final int VISIBLE_CIRCLES = 6;
		private static final float ANGLE_STEP = 360f / CIRCLES_COUNT;
		private static final float ROTATION_STEP = (ENG_ANGLE - START_ANGLE) / (Constants.Circle1.SIZE_ANIMATION_DURATION + Constants.Circle1.IDLE_1_DURATION);
		private final float[][] circlePositions;
		private final float[] sizes;
		private float angle;
		private final float[] coefficients = new float[4];

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
		protected float getSpacingFraction() {
			return SPACING_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			float curStep = Constants.Circle1.SIZE_ANIMATION_FRACTION + Constants.Circle1.IDLE_1_FRACTION;
			if (ddt > curStep) {
				angle = START_ANGLE;
				Arrays.fill(sizes, 0);
				return;
			}
			int i = 0;
			angle += ROTATION_STEP * dt;
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
			float t = DrawableUtils.normalize(ddt, 0, curStep);
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
					canvas.drawCircle(circlePositions[i][0], circlePositions[i][1], getBounds().width() * 0.08f * sizes[i], getPaint());
				}
			}
		}
	}

	private static final class BlueArc extends DrawableShape {

		private static final float SIZE_FRACTION = 0.55f;
		private static final float SPACING_FRACTION = (1 - SIZE_FRACTION) / 2f;
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
		protected float getSpacingFraction() {
			return SPACING_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			getPaint().setStrokeWidth(bounds.width() * 0.1f);
			float curStep = Constants.Circle1.SIZE_ANIMATION_FRACTION * 3 + Constants.Circle1.IDLE_1_FRACTION;
			if (ddt <= curStep) {
				draw = false;
				startAngle = START_ANGLE;
				return;
			}
			draw = true;
			curStep += Constants.Circle1.IDLE_2_FRACTION;
			if (ddt <= curStep) {
				float t = 1 - (curStep - ddt) / Constants.Circle1.IDLE_2_FRACTION;
				startAngle = START_ANGLE + accelerate.getInterpolation(t) * END_ANGLE;
				sweepAngle = DrawableUtils.trapeze(decelerate.getInterpolation(t), START_SWEEP_ANGLE, 0, END_SWEEP_ANGLE, 0.25f, END_SWEEP_ANGLE, 0.75f, START_SWEEP_ANGLE, 1);
				return;
			}
			draw = false;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (draw) {
				canvas.drawArc(getBounds(), startAngle, sweepAngle, false, getPaint());
			}
		}
	}

	private static final class YellowCircle extends DrawableShape {

		private static final float SIZE_FRACTION = 0.75f;
		private static final float SPACING_FRACTION = (1 - SIZE_FRACTION) / 2f;
		private static final float ZERO_WIDTH = 0;
		private static final float START_WIDTH = 1;
		private static final float END_WIDTH = 0.3f;

		private float width;
		private float blackWidth;
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
		protected float getSpacingFraction() {
			return SPACING_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getWidthFraction(ddt, START_WIDTH, END_WIDTH) * getBounds().width();
			blackWidth = getWidthFraction(ddt, START_WIDTH, -START_WIDTH / 2) * getBounds().width();
		}

		private float getWidthFraction(float ddt, float start, float end) {
			float startPoint = Constants.Circle1.SIZE_ANIMATION_FRACTION * 2 + Constants.Circle1.IDLE_1_FRACTION;
			if (ddt < startPoint) {
				return ZERO_WIDTH;
			}
			float curStep = startPoint + Constants.Circle1.SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return reduce(start, end, ddt, curStep, Constants.Circle1.SIZE_ANIMATION_FRACTION);
			}
			curStep += Constants.Circle1.IDLE_2_FRACTION + Constants.Circle1.SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return end;
			}
			return end;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
			if (blackWidth > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), blackWidth / 2f, bgPaint);
			}
		}
	}

	private static final class BlueCircle extends DrawableShape {

		private static final float SIZE_FRACTION = 0.6f;
		private static final float SPACING_FRACTION = (1 - SIZE_FRACTION) / 2f;
		private static final float DEFAULT_SIZE = 0.8f;
		private static final float ZERO_SIZE = 0f;
		private static final float LARGE_SIZE = 1f;

		private float width;

		public BlueCircle(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected float getSpacingFraction() {
			return SPACING_FRACTION;
		}

		@Override
		public void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getWidthFraction(ddt) * getBounds().width();
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}

		private float getWidthFraction(float ddt) {
			// reduce to 80%
			float curStep = Constants.Circle1.SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) { // 300 ms
				return reduce(LARGE_SIZE, DEFAULT_SIZE, ddt, curStep, Constants.Circle1.SIZE_ANIMATION_FRACTION);
			}

			// idle with size of 80%
			curStep += Constants.Circle1.IDLE_1_FRACTION;
			if (ddt <= curStep) { // 1800 ms
				return DEFAULT_SIZE;
			}

			// enlarge to 100%
			curStep += Constants.Circle1.SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) { // 2100 ms
				return enlarge(DEFAULT_SIZE, LARGE_SIZE, ddt, curStep, Constants.Circle1.SIZE_ANIMATION_FRACTION);
			}

			// reduce to 0%
			curStep += Constants.Circle1.SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) { // 2400 ms
				return reduce(DEFAULT_SIZE, ZERO_SIZE, ddt, curStep, Constants.Circle1.SIZE_ANIMATION_FRACTION);
			}

			// idle with size of 0%
			curStep += Constants.Circle1.IDLE_2_FRACTION;
			if (ddt <= curStep) { // 4200 ms
				return ZERO_SIZE;
			}

			// enlarge to 100%
			curStep += Constants.Circle1.SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep + (Constants.TOTAL_DURATION - curStep)) { // 4530 ms
				return enlarge(ZERO_SIZE, DEFAULT_SIZE, ddt, curStep, Constants.Circle1.SIZE_ANIMATION_FRACTION);
			}

			// default value
			return DEFAULT_SIZE;
		}
	}

}
