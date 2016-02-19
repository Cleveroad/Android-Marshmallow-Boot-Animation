package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Fourth layer of animation.
 */
class FourthLayer extends Layer {

	private final DrawableObject[] objects;

	public FourthLayer(Paint redPaint, Paint greenPaint, Paint bluePaint, Paint yellowPaint, Paint bgPaint) {
		objects = new DrawableObject[9];
		objects[0] = new BlueArc(bluePaint);
		objects[1] = new YellowArc(yellowPaint);
		objects[2] = new GreenCircle(greenPaint, bgPaint);
		objects[3] = new YellowCircle(yellowPaint); // first yellow circle
		objects[4] = new BlueCircle(bluePaint);
		objects[5] = new RedCircle(redPaint);
		objects[6] = new YellowCircle(yellowPaint, true); // second yellow circle
		objects[7] = new BlackCircle(bgPaint);
		objects[8] = new YellowBall(yellowPaint);
	}

	@Override
	public void update(@NonNull RectF bounds, long dt) {
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

	@Override
	public void reset() {
		for (DrawableObject object : objects) {
			if (object instanceof Resetable) {
				((Resetable) object).reset();
			}
		}
	}

	private static final class RedCircle extends DrawableObjectImpl {
		// TIMING
		private static final float IDLE_1_FRACTION_START = 0f;
		private static final float IDLE_1_FRACTION_END = 140 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_START = 140 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_END = 146 * Constants.FRAME_SPEED;

		private static final float IDLE_2_FRACTION_START = 146 * Constants.FRAME_SPEED;
		private static final float IDLE_2_FRACTION_END = 148 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_75 = 0.75f;
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
			width = getWidthFraction(ddt) * getBounds().width();
		}

		private float getWidthFraction(float ddt) {
			float time;
			if (DrawableUtils.between(ddt, IDLE_1_FRACTION_START, IDLE_1_FRACTION_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_START, FRACTION_ENLARGING_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_START, FRACTION_ENLARGING_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_75, time);
			}
			if (DrawableUtils.between(ddt, IDLE_2_FRACTION_START, IDLE_2_FRACTION_END)) {
				return SIZE_FRACTION_75;
			}
			return SIZE_FRACTION_75;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class BlueCircle extends DrawableObjectImpl {
		// TIMING
		private static final float IDLE_1_FRACTION_START = 0f;
		private static final float IDLE_1_FRACTION_END = 138 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_START = 138 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_END = 143 * Constants.FRAME_SPEED;

		private static final float IDLE_2_FRACTION_START = 143 * Constants.FRAME_SPEED;
		private static final float IDLE_2_FRACTION_END = 146 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_75 = 0.75f;
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
			float time;
			if (DrawableUtils.between(ddt, IDLE_1_FRACTION_START, IDLE_1_FRACTION_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_START, FRACTION_ENLARGING_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_START, FRACTION_ENLARGING_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_75, time);
			}
			if (DrawableUtils.between(ddt, IDLE_2_FRACTION_START, IDLE_2_FRACTION_END)) {
				return SIZE_FRACTION_75;
			}
			return SIZE_FRACTION_75;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class YellowCircle extends DrawableObjectImpl {
		// TIMING FOR FIRST YELLOW CIRCLE
		private static final float IDLE_FRACTION_1_START = 0f;
		private static final float IDLE_FRACTION_1_END = 133 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_1_START = 133 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_1_END = 139 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_2_START = 139 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_2_END = 143 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_2_START = 143 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_2_END = 149 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_75 = 0.75f;
		private static final float SIZE_FRACTION_0 = 0f;

		private float width;
		private boolean isSecondCircle;

		public YellowCircle(Paint paint) {
			super(paint);
		}

		public YellowCircle(Paint paint, boolean isSecondCircle) {
			super(paint);
			this.isSecondCircle = isSecondCircle;
		}

		@Override
		protected float getSizeFraction() {
			return 1f;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getBounds().width() * (isSecondCircle ? getWidthFractionSecondCircle(ddt) : getWidthFractionFirstCircle(ddt));
		}

		private float getWidthFractionFirstCircle(float ddt) {
			float time;
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_75, time);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_2_START, IDLE_FRACTION_2_END)) {
				return SIZE_FRACTION_75;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_75, time);
			}
			return SIZE_FRACTION_75;
		}

		private float getWidthFractionSecondCircle(float ddt) {
			float time;
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_2_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_75, time);
			}
			return SIZE_FRACTION_75;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class YellowBall extends DrawableObjectImpl {
		// TIMING
		private static final float IDLE_FRACTION_1_START = 0f;
		private static final float IDLE_FRACTION_1_END = 131 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_1_START = 131 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_1_END = 137 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_20 = 0.2f;
		private static final float SIZE_FRACTION_0 = 0f;

		private float width;

		public YellowBall(Paint paint) {
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
			float time;
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END);
				return DrawableUtils.reduce(SIZE_FRACTION_20, SIZE_FRACTION_0, time);
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

	private static final class BlackCircle extends DrawableObjectImpl {
		// TIMING
		private static final float IDLE_FRACTION_1_START = 0f;
		private static final float IDLE_FRACTION_1_END = 37 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_1_START = 37 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_1_END = 51 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_2_START = 51 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_2_END = 79 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_1_START = 79 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_1_END = 86 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_3_START = 86 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_3_END = 116 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_2_START = 116 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_2_END = 130 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_25 = 0.25f;
		private static final float SIZE_FRACTION_20 = 0.2f;
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
			float time;
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_20, time);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_2_START, IDLE_FRACTION_2_END)) {
				return SIZE_FRACTION_20;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END);
				return DrawableUtils.reduce(SIZE_FRACTION_20, SIZE_FRACTION_0, time);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_3_START, IDLE_FRACTION_3_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_25, time);
			}
			return SIZE_FRACTION_25;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class GreenCircle extends DrawableObjectImpl {
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

		private static final float IDLE_BG_1_START = 0f;
		private static final float IDLE_BG_1_END = 12 * Constants.FRAME_SPEED;

		// DIMENSIONS FACTORS
		private static final float SIZE_FRACTION_75 = 0.75f;
		private static final float SIZE_FRACTION_45 = 0.45f;
		private static final float SIZE_FRACTION_25 = 0.25f;
		private static final float SIZE_FRACTION_0 = 0f;

		private float width;
		private float bgWidth;
		private Paint bgPaint;

		public GreenCircle(Paint paint, Paint bgPaint) {
			super(paint);
			this.bgPaint = bgPaint;
		}

		@Override
		protected float getSizeFraction() {
			return 1.0f;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getWidthFraction(ddt) * getBounds().width();
			bgWidth = getBgWidthFraction(ddt) * getBounds().width();
		}

		private float getWidthFraction(float ddt) {
			float time;
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_45, time);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_45;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END);
				return DrawableUtils.reduce(SIZE_FRACTION_45, SIZE_FRACTION_25, time);
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_25, SIZE_FRACTION_75, time);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_2_START, IDLE_FRACTION_2_END)) {
				return SIZE_FRACTION_75;
			}
			return SIZE_FRACTION_0;
		}

		private float getBgWidthFraction(float ddt) {
			if (DrawableUtils.between(ddt, IDLE_BG_1_START, IDLE_BG_1_END)) {
				return SIZE_FRACTION_25;
			}
			return SIZE_FRACTION_0;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (bgWidth > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), bgWidth / 2f, bgPaint);
			}
			if (width > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), width / 2f, getPaint());
			}
		}
	}

	private static final class BlueArc extends DrawableObjectImpl {
		// TIMING
		private static final float IDLE_1_ANGLE_START = 0f;
		private static final float IDLE_1_ANGLE_END = 29 * Constants.FRAME_SPEED;

		private static final float ANGLE_CHANGING_START = 29 * Constants.FRAME_SPEED;
		private static final float ANGLE_CHANGING_END = 59 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_60 = 0.6f;
		private static final float SIZE_FRACTION_0 = 0f;

		private static final float START_ANGLE_BEGIN = 90f;
		private static final float END_ANGLE_BEGIN = -110f;

		private static final float ZERO_ANGLE = 0f;
		private static final float MAX_ANGLE = 360f;

		private Interpolator interpolator = new AccelerateDecelerateInterpolator();

		private float arcAngle;
		private float arcAngleBegin;
		private RectF rect;

		public BlueArc(Paint paint) {
			super(paint);
			rect = new RectF();
		}

		@Override
		protected float getSizeFraction() {
			return 1f;
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
			if (DrawableUtils.between(ddt, IDLE_1_ANGLE_START, IDLE_1_ANGLE_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, ANGLE_CHANGING_START, ANGLE_CHANGING_END)) {
				return SIZE_FRACTION_60;
			}
			return SIZE_FRACTION_0;
		}

		private float getArcAngle(float ddt, float start, float end) {
			float time;
			if (DrawableUtils.between(ddt, IDLE_1_ANGLE_START, IDLE_1_ANGLE_END)) {
				return start;
			}
			if (DrawableUtils.between(ddt, ANGLE_CHANGING_START, ANGLE_CHANGING_END)) {
				time = interpolator.getInterpolation(DrawableUtils.normalize(ddt, ANGLE_CHANGING_START, ANGLE_CHANGING_END));
				return end + (start - end) * (1 - time);
			}
			return start;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (Math.abs(arcAngle) > 0) {
				canvas.drawArc(rect, arcAngleBegin, -arcAngle, true, getPaint());
			}
		}
	}

	private static final class YellowArc extends DrawableObjectImpl {
		// TIMING OF FRACTION SIZE
		private static final float IDLE_FRACTION_1_START = 0f;
		private static final float IDLE_FRACTION_1_END = 17 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_1_START = 17 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_1_END = 25 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_2_START = 25 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_2_END = 53 * Constants.FRAME_SPEED;

		private static final float FRACTION_REDUCING_2_START = 53 * Constants.FRAME_SPEED;
		private static final float FRACTION_REDUCING_2_END = 59 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_3_START = 59 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_3_END = 133 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_1_START = 133 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_1_END = 139 * Constants.FRAME_SPEED;

		private static final float IDLE_FRACTION_4_START = 139 * Constants.FRAME_SPEED;
		private static final float IDLE_FRACTION_4_END = 143 * Constants.FRAME_SPEED;

		private static final float FRACTION_ENLARGING_2_START = 143 * Constants.FRAME_SPEED;
		private static final float FRACTION_ENLARGING_2_END = 149 * Constants.FRAME_SPEED;

		// TIMING OF ARC ANGLE
		private static final float IDLE_ANGLE_1_START = 0f;
		private static final float IDLE_ANGLE_1_END = 27 * Constants.FRAME_SPEED;

		private static final float ANGLE_CHANGING_START = 27 * Constants.FRAME_SPEED;
		private static final float ANGLE_CHANGING_END = 59 * Constants.FRAME_SPEED;

		// DIMENSIONS
		private static final float SIZE_FRACTION_75 = 0.75f;
		private static final float SIZE_FRACTION_60 = 0.6f;
		private static final float SIZE_FRACTION_0 = 0f;

		private static final float START_ANGLE_BEGIN = 90f;
		private static final float END_ANGLE_BEGIN = -110f;

		private static final float ZERO_ANGLE = 0f;
		private static final float MAX_ANGLE = 360f;

		private Interpolator accelerate = new AccelerateDecelerateInterpolator();

		private RectF rect;
		private float arcAngleBegin;
		private float arcAngle;

		public YellowArc(Paint paint) {
			super(paint);
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
			float time;
			if (DrawableUtils.between(ddt, IDLE_FRACTION_1_START, IDLE_FRACTION_1_END)) {
				return SIZE_FRACTION_75;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_REDUCING_1_START, FRACTION_REDUCING_1_END);
				return DrawableUtils.reduce(SIZE_FRACTION_75, SIZE_FRACTION_60, time);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_2_START, IDLE_FRACTION_2_END)) {
				return SIZE_FRACTION_60;
			}
			if (DrawableUtils.between(ddt, FRACTION_REDUCING_2_START, FRACTION_REDUCING_2_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_REDUCING_2_START, FRACTION_REDUCING_2_END);
				return DrawableUtils.reduce(SIZE_FRACTION_60, SIZE_FRACTION_0, time);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_3_START, IDLE_FRACTION_3_END)) {
				return SIZE_FRACTION_0;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_1_START, FRACTION_ENLARGING_1_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_75, time);
			}
			if (DrawableUtils.between(ddt, IDLE_FRACTION_4_START, IDLE_FRACTION_4_END)) {
				return SIZE_FRACTION_75;
			}
			if (DrawableUtils.between(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END)) {
				time = DrawableUtils.normalize(ddt, FRACTION_ENLARGING_2_START, FRACTION_ENLARGING_2_END);
				return DrawableUtils.enlarge(SIZE_FRACTION_0, SIZE_FRACTION_75, time);
			}
			return SIZE_FRACTION_75;
		}

		private float getArcAngle(float ddt, float start, float end) {
			float time;
			if (DrawableUtils.between(ddt, IDLE_ANGLE_1_START, IDLE_ANGLE_1_END)) {
				return start;
			}
			if (DrawableUtils.between(ddt, ANGLE_CHANGING_START, ANGLE_CHANGING_END)) {
				time = accelerate.getInterpolation(DrawableUtils.normalize(ddt, ANGLE_CHANGING_START, ANGLE_CHANGING_END));
				return end + (start - end) * (1 - time);
			}
			return start;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (Math.abs(arcAngle) > 0) {
				canvas.drawArc(rect, arcAngleBegin, -arcAngle, true, getPaint());
			}
		}
	}
}
