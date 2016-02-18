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
		objects = new DrawableObject[6];
		int i = 0;
		objects[i++] = new GreenCircle(greenPaint);
		objects[i++] = new RedCircle(redPaint);
		objects[i++] = new BlueCircle(bluePaint);
		objects[i++] = new YellowArc(yellowPaint);
		objects[i++] = new BlackCircle(bgPaint);
		objects[i++] = new YellowCircle(yellowPaint);
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

	private static final class RedCircle extends DrawableShape {
		// TIMING
		private static final float IDLE_1_FRACTION_START = 0f;
		private static final float IDLE_1_FRACTION_END = 138 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_START = 138 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_END = 143 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_START = 143 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_END = 146 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_85 = 0.85f;
		private static final float SIZE_FRACTION_0 = 0f;

		private float width;

		public RedCircle(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return 1f;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getBounds().width() * getWidthFraction(ddt);
		}

		private float getWidthFraction(float ddt) {
			if (DrawableUtils.between(ddt, IDLE_1_FRACTION_START, IDLE_1_FRACTION_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_START, FRACTION_ENLARGING_END)) {
				return enlarge(SIZE_FRACTION_0, SIZE_FRACTION_85, ddt, FRACTION_ENLARGING_END,
						FRACTION_ENLARGING_END - FRACTION_ENLARGING_START);
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_START, FRACTION_REDUCING_END)) {
				return reduce(SIZE_FRACTION_85, SIZE_FRACTION_0, ddt, FRACTION_REDUCING_END,
						FRACTION_REDUCING_END - FRACTION_REDUCING_START);
			}
			return SIZE_FRACTION_0;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class BlueCircle extends DrawableShape {
		// TIMING
		private static final float IDLE_1_FRACTION_START = 0f;
		private static final float IDLE_1_FRACTION_END = 137 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_START = 137 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_END = 143 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_START = 143 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_END = 146 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_85 = 0.85f;
		private static final float SIZE_FRACTION_0 = 0f;

		private float width;

		public BlueCircle(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return 1.0f;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getBounds().width() * getWidthFraction(ddt);
		}

		private float getWidthFraction(float ddt) {
			if (DrawableUtils.between(ddt, IDLE_1_FRACTION_START, IDLE_1_FRACTION_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_START, FRACTION_ENLARGING_END)) {
				return enlarge(SIZE_FRACTION_0, SIZE_FRACTION_85, ddt, FRACTION_ENLARGING_END,
						FRACTION_ENLARGING_END - FRACTION_ENLARGING_START);
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_START, FRACTION_REDUCING_END)) {
				return reduce(SIZE_FRACTION_85, SIZE_FRACTION_0, ddt, FRACTION_REDUCING_END,
						FRACTION_REDUCING_END * FRACTION_REDUCING_START);
			}
			return SIZE_FRACTION_0;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class YellowCircle extends DrawableShape {
		// TIMING
		private static final float IDLE_FRACTION_1_START = 0f;
		private static final float IDLE_FRACTION_1_END = 131 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_1_START = 131 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_1_END = 137 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_35 = 0.25f;
		private static final float SIZE_FRACTION_0 = 0f;

		private float width;

		public YellowCircle(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return 1.0f;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getBounds().width() * getWidthFraction(ddt);
		}

		private float getWidthFraction(float ddt) {
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END)) {
				return reduce(SIZE_FRACTION_35, SIZE_FRACTION_0, ddt, FRACTION_REDUCING_1_END,
						FRACTION_REDUCING_1_END - FRACTION_REDUCING_1_START);
			}
			return SIZE_FRACTION_0;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class BlackCircle extends DrawableShape {
		// TIMING
		private static final float IDLE_FRACTION_1_START = 0f;
		private static final float IDLE_FRACTION_1_END = 12 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_2_START = 12 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_2_END = 37 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_1_START = 37 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_1_END = 51 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_3_START = 51 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_3_END = 79 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_1_START = 79 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_1_END = 86 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_4_START = 86 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_4_END = 116 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_2_START = 116 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_2_END = 130 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_40 = 0.4f;
		private static final float SIZE_FRACTION_30 = 0.3f;
		private static final float SIZE_FRACTION_0 = 0f;


		private float width;

		public BlackCircle(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return 1.0f;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getBounds().width() * getWidthFraction(ddt);
		}

		private float getWidthFraction(float ddt) {
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_40;
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_2_START, IDLE_FRACTION_2_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END)) {
				return enlarge(SIZE_FRACTION_0, SIZE_FRACTION_30, ddt, FRACTION_ENLARGING_1_END,
						FRACTION_ENLARGING_1_END - FRACTION_ENLARGING_1_START);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_3_START, IDLE_FRACTION_3_END)) {
				return SIZE_FRACTION_30;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END)) {
				return reduce(SIZE_FRACTION_30, SIZE_FRACTION_0, ddt, FRACTION_REDUCING_1_END,
						FRACTION_ENLARGING_1_END - FRACTION_ENLARGING_1_START);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_4_START, IDLE_FRACTION_4_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END)) {
				return enlarge(SIZE_FRACTION_0, SIZE_FRACTION_40, ddt, FRACTION_ENLARGING_2_END,
						FRACTION_ENLARGING_2_END - FRACTION_ENLARGING_2_START);
			}
			return SIZE_FRACTION_40;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class GreenCircle extends DrawableShape {
		// TIMING
		private static final float FRACTION_ENLARGING_1_START = 0f;
		private static final float FRACTION_ENLARGING_1_END = 20 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_1_START = 20 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_1_END = 92 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_1_START = 92 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_1_END = 103 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_2_START = 103 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_2_END = 129 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_2_START = 129 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_2_END = 139 * Constants.FRAME_SPEED;

		// DIMENSIONS FACTORS
		private static final float SIZE_FRACTION_85 = 0.85f;
		private static final float SIZE_FRACTION_50 = 0.5f;
		private static final float SIZE_FRACTION_25 = 0.25f;
		private static final float SIZE_FRACTION_0 = 0f;

		private float width;

		public GreenCircle(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return 1.0f;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getWidthFraction(ddt) * getBounds().width();
		}

		private float getWidthFraction(float ddt) {
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END)) {
				return enlarge(SIZE_FRACTION_0, SIZE_FRACTION_50, ddt, FRACTION_ENLARGING_1_END,
						FRACTION_ENLARGING_1_END - FRACTION_ENLARGING_1_START);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_50;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END)) {
				return reduce(SIZE_FRACTION_50, SIZE_FRACTION_25, ddt, FRACTION_REDUCING_1_END,
						FRACTION_REDUCING_1_END - FRACTION_REDUCING_1_START);
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END)) {
				return enlarge(SIZE_FRACTION_25, SIZE_FRACTION_85, ddt, FRACTION_ENLARGING_2_END,
						FRACTION_ENLARGING_2_END - FRACTION_ENLARGING_2_START);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_2_START, IDLE_FRACTION_2_END)) {
				return SIZE_FRACTION_85;
			}
			return SIZE_FRACTION_0;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class YellowArc extends DrawableShape {
		// TIMING OF FRACTION SIZE
		private static final float IDLE_FRACTION_1_START = 0f;
		private static final float IDLE_FRACTION_1_END = 17 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_1_START = 17 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_1_END = 25 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_2_START = 25 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_2_END = 49 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_2_START = 49 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_2_END = 59 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_3_START = 59 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_3_END = 134 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_1_START = 134 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_1_END = 139 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_4_START = 139 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_4_END = 143 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_2_START = 143 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_2_END = 149 * Constants.FRAME_SPEED;

		// TIMING OF ARC ANGLE
		private static final float IDLE_ANGLE_1_START = 0f;
		private static final float IDLE_ANGLE_1_END = 26 * Constants.FRAME_SPEED;

		private static final float ANGLE_CHANGING_START = 26 * Constants.FRAME_SPEED;
		private static final float ANGLE_CHANGING_END = 59 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_85 = 0.85f;
		private static final float SIZE_FRACTION_60 = 0.6f;
		private static final float SIZE_FRACTION_0 = 0f;

		private static final float START_ANGLE_BEGIN = 90f;
		private static final float END_ANGLE_BEGIN = -110f;

		private static final float ZERO_ANGLE = 0f;
		private static final float MAX_ANGLE = 360f;

		private RectF rect;
		private float arcAngleBegin;
		private float arcAngle;

		public YellowArc(Paint paint) {
			super(new Paint(paint));
			getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
			rect = new RectF();
		}

		@Override
		protected float getSizeFraction() {
			return 1.0f;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			float coef = getWidthFraction(ddt);
			float leftRightMargin =  (getBounds().width() - getBounds().width() * coef) / 2f;
			float topBottomMargin = (getBounds().height() - getBounds().height() * coef) / 2f;
			float l = getBounds().left + leftRightMargin;
			float r = getBounds().right - leftRightMargin;
			float t = getBounds().top + topBottomMargin;
			float b = getBounds().bottom - topBottomMargin;
			rect.set(l, t, r, b);
			arcAngle = getArcAngle(ddt, MAX_ANGLE, ZERO_ANGLE);
			arcAngleBegin = getArcAngle(ddt, END_ANGLE_BEGIN, START_ANGLE_BEGIN);
		}

		private float getWidthFraction(float ddt) {
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_85;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END)) {
				return reduce(SIZE_FRACTION_85, SIZE_FRACTION_60, ddt, FRACTION_REDUCING_1_END,
						FRACTION_REDUCING_1_END - FRACTION_REDUCING_1_START);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_2_START, IDLE_FRACTION_2_END)) {
				return SIZE_FRACTION_60;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_2_START, FRACTION_REDUCING_2_END)) {
				return reduce(SIZE_FRACTION_60, SIZE_FRACTION_0, ddt, FRACTION_REDUCING_2_END,
						FRACTION_REDUCING_2_END - FRACTION_REDUCING_2_START);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_3_START, IDLE_FRACTION_3_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END)) {
				return enlarge(SIZE_FRACTION_0, SIZE_FRACTION_85, ddt, FRACTION_ENLARGING_1_END,
						FRACTION_ENLARGING_1_END - FRACTION_ENLARGING_1_START);
			}
			//
			if (DrawableUtils.between(ddt, IDLE_FRACTION_4_START, IDLE_FRACTION_4_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END)) {
				return enlarge(SIZE_FRACTION_0, SIZE_FRACTION_85, ddt, FRACTION_ENLARGING_2_END,
						FRACTION_ENLARGING_2_END - FRACTION_ENLARGING_1_START);
			}
			return SIZE_FRACTION_85;
		}

		private float getArcAngle(float ddt, float start, float end) {
			if (DrawableUtils.between(ddt, IDLE_ANGLE_1_START, IDLE_ANGLE_1_END)) {
				return start;
			}
			if (DrawableUtils.between(ddt, ANGLE_CHANGING_START, ANGLE_CHANGING_END)) {
				return reduce(start, end, ddt, ANGLE_CHANGING_END, ANGLE_CHANGING_END - ANGLE_CHANGING_START);
			}
			return start;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			canvas.drawArc(rect, arcAngleBegin, -arcAngle, false, getPaint());
		}
	}
}
