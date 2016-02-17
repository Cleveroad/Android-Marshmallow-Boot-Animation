package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Created by Александр on 16.02.2016.
 */
public class FourthLayer extends Layer {

	private final DrawableObject[] objects;

	public FourthLayer(Paint redPaint, Paint greenPaint, Paint bluePaint, Paint yellowPaint, Paint bgPaint) {
		objects = new DrawableObject[1];
		objects[0] = new YellowCircle(yellowPaint);
		//objects[1] = new GreenCircle(greenPaint);
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

	private static final class GreenCircle extends DrawableShape {
		private static final float CIRCLE_WIDTH_ENLARGING_ANIMATION_DURATION = 500f / Constants.TOTAL_DURATION; // 500 ms / TOTAL_DURATION
		private static final float IDLE_1_DURATION = 250f / Constants.TOTAL_DURATION; // 250 ms / TOTAL_DURATION

		private static final float SIZE_FRACTION = 0.7f;
		private static final float SPACING_FRACTION = (1f - SIZE_FRACTION) / 2f;

		private static final float START_CIRCLE_WIDTH = 0f;
		private static final float END_CIRCLE_WIDTH = 1f;

		private float width;

		public GreenCircle(Paint paint) {
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
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getWidthFraction(ddt, START_CIRCLE_WIDTH, END_CIRCLE_WIDTH) * getBounds().width();
		}

		private float getWidthFraction(float ddt, float start, float end) {
			float curStep = CIRCLE_WIDTH_ENLARGING_ANIMATION_DURATION;
			if (ddt <= curStep) {
				return enlarge(start, end, ddt, curStep, CIRCLE_WIDTH_ENLARGING_ANIMATION_DURATION);
			}

			curStep += IDLE_1_DURATION;
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
		}
	}

	private static final class YellowCircle extends DrawableShape {
		private static final float IDLE_1_DURATION = 17 * Constants.FRAME_SPEED;
		private static final float STROKE_WIDTH_REDUCING_1_ANIMATION_DURATION = 9 * Constants.FRAME_SPEED;
		private static final float IDLE_STROKE_DURATION = 24 * Constants.FRAME_SPEED;
		private static final float STROKE_WIDTH_REDUCING_2_ANIMATION_DURATION = 9 * Constants.FRAME_SPEED;
		private static final float ARC_ANGLE_CHANGING_ANIMATION_DURATION = 32 * Constants.FRAME_SPEED;
		private static final float STROKE_WIDTH_ENLARGING_1_ANIMATION_DURATION = 133 * Constants.FRAME_SPEED;
		private static final float STROKE_WIDTH_REDUCING_3_ANIMATION_DURATION = 138 * Constants.FRAME_SPEED;
		private static final float STROKE_WIDTH_ENLARGING_2_ANIMATION_DURATION = 144 * Constants.FRAME_SPEED;

		private static final float SIZE_FRACTION = 0.8f;
		private static final float SPACING_FRACTION = (1f - SIZE_FRACTION) / 2f;

		private static final float START_STROKE_WIDTH = 0.4f;
		private static final float END_STROKE_WIDTH_REDUCING_1 = 0.1f;
		private static final float END_STROKE_WIDTH_REDUCING_2 = 0f;

		private static final float START_ANGLE_BEGIN = 90f;
		private static final float END_ANGLE_BEGIN = -110f;

		private static final float ZERO_ANGLE = 0f;
		private static final float MAX_ANGLE = 360f;

		private float strokeWidth;
		private float arcAngleBegin = START_ANGLE_BEGIN;
		private float arcAngle = END_ANGLE_BEGIN;

		public YellowCircle(Paint paint) {
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
			strokeWidth = bounds.width() * getStrokeWidth(ddt);
			arcAngle = getArcAngle(ddt, MAX_ANGLE, ZERO_ANGLE);
			arcAngleBegin = getArcAngle(ddt, END_ANGLE_BEGIN, START_ANGLE_BEGIN);
			getPaint().setStrokeWidth(strokeWidth);
		}

		private float getStrokeWidth(float ddt) {
			float curStep = IDLE_1_DURATION;
			if (ddt < curStep) {
				return START_STROKE_WIDTH;
			}
			curStep += STROKE_WIDTH_REDUCING_1_ANIMATION_DURATION;
			if (ddt <= curStep) {
				return reduce(START_STROKE_WIDTH, END_STROKE_WIDTH_REDUCING_1, ddt, curStep, STROKE_WIDTH_REDUCING_1_ANIMATION_DURATION);
			}
			curStep += IDLE_STROKE_DURATION;
			if (ddt <= curStep) {
				return END_STROKE_WIDTH_REDUCING_1;
			}
			curStep += STROKE_WIDTH_REDUCING_2_ANIMATION_DURATION;
			if (ddt <= curStep) {
				return reduce(END_STROKE_WIDTH_REDUCING_1, END_STROKE_WIDTH_REDUCING_2, ddt, curStep, STROKE_WIDTH_REDUCING_2_ANIMATION_DURATION);
			}
			return END_STROKE_WIDTH_REDUCING_2;
		}

		private float getArcAngle(float ddt, float start, float end) {
			float curStep = IDLE_1_DURATION + STROKE_WIDTH_REDUCING_1_ANIMATION_DURATION;
			if (ddt <= curStep) {
				return start;
			}
			curStep += ARC_ANGLE_CHANGING_ANIMATION_DURATION;
			if (ddt <= curStep) {
				return reduce(start, end, ddt, curStep, ARC_ANGLE_CHANGING_ANIMATION_DURATION);
			}
			return end;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (strokeWidth > 0) {
				canvas.drawArc(getBounds(), arcAngleBegin, -arcAngle, false, getPaint());
			}
		}
	}
}
