package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Third layer of animation.
 */
class ThirdLayer extends Layer {

	private final DrawableObject[] objects;

	public ThirdLayer(Paint redPaint, Paint greenPaint, Paint bgPaint) {
		objects = new DrawableObject[3];
		objects[0] = new RedRing(redPaint, bgPaint);
		objects[1] = new GreenRing(greenPaint, bgPaint);
		objects[2] = new RedCircle(redPaint, bgPaint);
	}

	@Override
	public void update(@NonNull RectF bounds, float dt) {
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

	private static final class GreenRing extends DrawableObjectImpl {

		private static final float SIZE_FRACTION = 0.75f;
		private static final float GREEN_DEFAULT_SIZE = 0.6f;
		private static final float GREEN_LARGE_SIZE = 0.8f;
		private static final float GREEN_SMALL_SIZE = 0.4f;
		private static final float BLACK_DEFAULT_SIZE = 0.3f;
		private static final float BLACK_LARGE_SIZE = 0.4f;
		private static final float BLACK_ARC_DEFAULT_SIZE = 0.5f;
		private static final float BLACK_ARC_SMALL_SIZE = 0f;
		private static final float START_ANGLE = -90;
		private static final float END_ANGLE = -480;
		private static final float START_SWEEP_ANGLE = 260;
		private static final float END_SWEEP_ANGLE = 360;

		private static final float BLACK_ENLARGE_FRACTION_START = 3 * Constants.FRAME_SPEED;
		private static final float BLACK_ENLARGE_FRACTION_END = 7 * Constants.FRAME_SPEED;

		private static final float GREEN_ENLARGE_FRACTION_START = 7 * Constants.FRAME_SPEED;
		private static final float GREEN_ENLARGE_FRACTION_END = 21 * Constants.FRAME_SPEED;

		private static final float SWITCH_TO_ARC_FRACTION = 58 * Constants.FRAME_SPEED;

		private static final float ARC_ROTATION_FRACTION_START = 64 * Constants.FRAME_SPEED;
		private static final float ARC_ROTATION_FRACTION_END = 106 * Constants.FRAME_SPEED;

		private static final float ARC_SIZE_CHANGE_FRACTION_START = 87 * Constants.FRAME_SPEED;
		private static final float ARC_SIZE_CHANGE_FRACTION_END = 106 * Constants.FRAME_SPEED;

		private static final float ARC_MERGING_FRACTION_START = 93 * Constants.FRAME_SPEED;
		private static final float ARC_MERGING_FRACTION_END = 109 * Constants.FRAME_SPEED;

		private static final float RESTORE_FRACTION_START = 136 * Constants.FRAME_SPEED;
		private static final float RESTORE_FRACTION_END = 150 * Constants.FRAME_SPEED;

		private Paint bgPaint;
		private float greenSize;
		private float blackSize;
		boolean drawArc;
		private final RectF rect;
		private float angle;
		private float sweepAngle;

		public GreenRing(Paint paint, Paint bgPaint) {
			super(paint);
			this.bgPaint = bgPaint;
			this.rect = new RectF();
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void updateImpl(@NonNull RectF bounds, float ddt) {
			greenSize = computeGreenSizeFraction(ddt) * getBounds().width();
			blackSize = computeBlackSizeFraction(ddt) * getBounds().width();
			if (drawArc) {
				float cx = getBounds().centerX();
				float cy = getBounds().centerY();
				float halfSize = greenSize / 2f;
				rect.set(cx - halfSize, cy - halfSize, cx + halfSize, cy + halfSize);
				if (ddt <= ARC_ROTATION_FRACTION_START)
					angle = START_ANGLE;
				else if (ddt >= ARC_ROTATION_FRACTION_END)
					angle = END_ANGLE;
				if (ddt <= ARC_MERGING_FRACTION_START)
					sweepAngle = START_SWEEP_ANGLE;
				else if (ddt >= ARC_MERGING_FRACTION_END)
					sweepAngle = END_SWEEP_ANGLE;
				if (DrawableUtils.between(ddt, ARC_ROTATION_FRACTION_START, ARC_ROTATION_FRACTION_END)) {
					float t = DrawableUtils.normalize(ddt, ARC_ROTATION_FRACTION_START, ARC_ROTATION_FRACTION_END);
					angle = START_ANGLE + t * (END_ANGLE - START_ANGLE);
				}
				if (DrawableUtils.between(ddt, ARC_MERGING_FRACTION_START, ARC_MERGING_FRACTION_END)) {
					float t = DrawableUtils.normalize(ddt, ARC_MERGING_FRACTION_START, ARC_MERGING_FRACTION_END);
					sweepAngle = START_SWEEP_ANGLE + t * (END_SWEEP_ANGLE - START_SWEEP_ANGLE);
				}
			}
		}

		private float computeBlackSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, Constants.FIRST_FRAME_FRACTION, BLACK_ENLARGE_FRACTION_START)) {
				return 0;
			}
			if (DrawableUtils.between(ddt, BLACK_ENLARGE_FRACTION_START, BLACK_ENLARGE_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, BLACK_ENLARGE_FRACTION_START, BLACK_ENLARGE_FRACTION_END);
				return DrawableUtils.enlarge(BLACK_DEFAULT_SIZE, BLACK_LARGE_SIZE, t);
			}
			if (DrawableUtils.between(ddt, BLACK_ENLARGE_FRACTION_END, GREEN_ENLARGE_FRACTION_END)) {
				return BLACK_LARGE_SIZE;
			}
			if (DrawableUtils.between(ddt, SWITCH_TO_ARC_FRACTION, ARC_SIZE_CHANGE_FRACTION_START)) {
				return BLACK_ARC_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, ARC_SIZE_CHANGE_FRACTION_START, ARC_SIZE_CHANGE_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, ARC_SIZE_CHANGE_FRACTION_START, ARC_SIZE_CHANGE_FRACTION_END);
				return DrawableUtils.reduce(BLACK_ARC_DEFAULT_SIZE, BLACK_ARC_SMALL_SIZE, t);
			}
			return 0;
		}

		private float computeGreenSizeFraction(float ddt) {
			drawArc = ddt >= SWITCH_TO_ARC_FRACTION;
			if (DrawableUtils.between(ddt, Constants.FIRST_FRAME_FRACTION, GREEN_ENLARGE_FRACTION_START)) {
				return GREEN_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, GREEN_ENLARGE_FRACTION_START, GREEN_ENLARGE_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, GREEN_ENLARGE_FRACTION_START, GREEN_ENLARGE_FRACTION_END);
				return DrawableUtils.enlarge(GREEN_DEFAULT_SIZE, GREEN_LARGE_SIZE, t);
			}
			if (DrawableUtils.between(ddt, GREEN_ENLARGE_FRACTION_END, ARC_SIZE_CHANGE_FRACTION_START)) {
				return GREEN_LARGE_SIZE;
			}
			if (DrawableUtils.between(ddt, ARC_SIZE_CHANGE_FRACTION_START, ARC_SIZE_CHANGE_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, ARC_SIZE_CHANGE_FRACTION_START, ARC_SIZE_CHANGE_FRACTION_END);
				return DrawableUtils.reduce(GREEN_LARGE_SIZE, GREEN_SMALL_SIZE, t);
			}
			if (DrawableUtils.between(ddt, ARC_SIZE_CHANGE_FRACTION_END, RESTORE_FRACTION_START)) {
				return GREEN_SMALL_SIZE;
			}
			if (DrawableUtils.between(ddt, RESTORE_FRACTION_START, RESTORE_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, RESTORE_FRACTION_START, RESTORE_FRACTION_END);
				return DrawableUtils.enlarge(GREEN_SMALL_SIZE, GREEN_DEFAULT_SIZE, t);
			}
			return GREEN_DEFAULT_SIZE;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (drawArc) {
				canvas.drawArc(rect, angle, sweepAngle, true, getPaint());
			} else {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), greenSize / 2f, getPaint());
			}
			if (blackSize > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), blackSize / 2f, bgPaint);
			}
		}
	}

	private static final class RedRing extends DrawableObjectImpl {

		private static final float SIZE_FRACTION = 0.75f;
		private static final float START_ANGLE = 0;
		private static final float END_ANGLE = 580;
		private static final float SWEEP_START_ANGLE = 0;
		private static final float SWEEP_END_ANGLE = 410;
		private static final float RED_DEFAULT_SIZE = 0.7f;
		private static final float RED_SMALL_SIZE = 0.4f;
		private static final float BLACK_DEFAULT_SIZE = RED_DEFAULT_SIZE - 0.1f;
		private static final float BLACK_SMALL_SIZE = RED_SMALL_SIZE - 0.1f;

		private static final float ROTATION_FRACTION_START = 90 * Constants.FRAME_SPEED;
		private static final float ROTATION_FRACTION_END = 134 * Constants.FRAME_SPEED;

		private static final float SIZE_CHANGE_FRACTION_START = 134 * Constants.FRAME_SPEED;
		private static final float SIZE_CHANGE_FRACTION_END = 145 * Constants.FRAME_SPEED;

		private Paint bgPaint;
		private float redSize;
		private float blackSize;
		private float angle;
		private float sweepAngle;
		private RectF rect;

		public RedRing(Paint paint, Paint bgPaint) {
			super(paint);
			this.bgPaint = bgPaint;
			this.rect = new RectF();
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void updateImpl(@NonNull RectF bounds, float ddt) {
			redSize = computeRedSizeFraction(ddt) * getBounds().width();
			blackSize = computeBlackSizeFraction(ddt) * getBounds().width();
			if (ddt <= ROTATION_FRACTION_START) {
				angle = START_ANGLE;
				sweepAngle = SWEEP_START_ANGLE;
			} else if (ddt >= ROTATION_FRACTION_END) {
				angle = END_ANGLE;
				sweepAngle = SWEEP_END_ANGLE;
			} else {
				float t = DrawableUtils.normalize(ddt, ROTATION_FRACTION_START, ROTATION_FRACTION_END);
				angle = START_ANGLE + t * (END_ANGLE - START_ANGLE);
				sweepAngle = SWEEP_START_ANGLE + t * (SWEEP_END_ANGLE - SWEEP_START_ANGLE);
			}
			if (redSize > 0) {
				float cx = getBounds().centerX();
				float cy = getBounds().centerY();
				float halfSize = redSize / 2f;
				rect.set(cx - halfSize, cy - halfSize, cx + halfSize, cy + halfSize);
			}
		}

		private float computeBlackSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, Constants.FIRST_FRAME_FRACTION, ROTATION_FRACTION_START)) {
				return 0;
			}
			if (DrawableUtils.between(ddt, ROTATION_FRACTION_START, ROTATION_FRACTION_END)) {
				return BLACK_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, SIZE_CHANGE_FRACTION_START, SIZE_CHANGE_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, SIZE_CHANGE_FRACTION_START, SIZE_CHANGE_FRACTION_END);
				return DrawableUtils.reduce(BLACK_DEFAULT_SIZE, BLACK_SMALL_SIZE, t);
			}
			return 0;
		}

		private float computeRedSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, Constants.FIRST_FRAME_FRACTION, ROTATION_FRACTION_START)) {
				return 0;
			}
			if (DrawableUtils.between(ddt, ROTATION_FRACTION_START, ROTATION_FRACTION_END)) {
				return RED_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, SIZE_CHANGE_FRACTION_START, SIZE_CHANGE_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, SIZE_CHANGE_FRACTION_START, SIZE_CHANGE_FRACTION_END);
				return DrawableUtils.reduce(RED_DEFAULT_SIZE, RED_SMALL_SIZE, t);
			}
			return 0;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (redSize > 0) {
				canvas.drawArc(rect, angle, sweepAngle, true, getPaint());
			}
			if (blackSize > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), blackSize / 2f, bgPaint);
			}
		}
	}

	private static final class RedCircle extends DrawableObjectImpl {

		private static final float SIZE_FRACTION = 0.75f;
		private static final float RED_DEFAULT_SIZE = 0.3f;
		private static final float RED_LARGER_SIZE = 0.4f;
		private static final float RED_LARGEST_SIZE = 0.8f;
		private static final float BLACK_DEFAULT_SIZE = 0;
		private static final float BLACK_LARGE_SIZE = 0.4f;


		private static final float ENLARGE_1_FRACTION_START = 4 * Constants.FRAME_SPEED;
		private static final float ENLARGE_1_FRACTION_END = 15 * Constants.FRAME_SPEED;

		private static final float ENLARGE_2_FRACTION_START = 17 * Constants.FRAME_SPEED;
		private static final float ENLARGE_2_FRACTION_END = 30 * Constants.FRAME_SPEED;

		private static final float BLACK_ENLARGE_FRACTION_START = 19 * Constants.FRAME_SPEED;
		private static final float BLACK_ENLARGE_FRACTION_END = 30 * Constants.FRAME_SPEED;

		private static final float VISIBILITY_FRACTION = 39 * Constants.FRAME_SPEED;

		private static final float ENLARGE_3_FRACTION_START = 144 * Constants.FRAME_SPEED;
		private static final float ENLARGE_3_FRACTION_END = Constants.LAST_FRAME_FRACTION;

		private Paint bgPaint;
		private float redSize;
		private float blackSize;

		public RedCircle(Paint paint, Paint bgPaint) {
			super(paint);
			this.bgPaint = bgPaint;
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void updateImpl(@NonNull RectF bounds, float ddt) {
			redSize = computeRedSizeFraction(ddt) * getBounds().width();
			blackSize = computeBlackSizeFraction(ddt) * getBounds().width();
		}

		private float computeBlackSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, BLACK_ENLARGE_FRACTION_START, BLACK_ENLARGE_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, BLACK_ENLARGE_FRACTION_START, BLACK_ENLARGE_FRACTION_END);
				return DrawableUtils.enlarge(BLACK_DEFAULT_SIZE, BLACK_LARGE_SIZE, t);
			}
			if (DrawableUtils.between(ddt, BLACK_ENLARGE_FRACTION_END, VISIBILITY_FRACTION)) {
				return BLACK_LARGE_SIZE;
			}
			return BLACK_DEFAULT_SIZE;
		}

		private float computeRedSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, Constants.FIRST_FRAME_FRACTION, ENLARGE_1_FRACTION_START)) {
				return RED_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, ENLARGE_1_FRACTION_START, ENLARGE_1_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, ENLARGE_1_FRACTION_START, ENLARGE_1_FRACTION_END);
				return DrawableUtils.enlarge(RED_DEFAULT_SIZE, RED_LARGER_SIZE, t);
			}
			if (DrawableUtils.between(ddt, ENLARGE_1_FRACTION_END, ENLARGE_2_FRACTION_START)) {
				return RED_LARGER_SIZE;
			}
			if (DrawableUtils.between(ddt, ENLARGE_2_FRACTION_START, ENLARGE_2_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, ENLARGE_2_FRACTION_START, ENLARGE_2_FRACTION_END);
				return DrawableUtils.enlarge(RED_LARGER_SIZE, RED_LARGEST_SIZE, t);
			}
			if (DrawableUtils.between(ddt, ENLARGE_2_FRACTION_END, VISIBILITY_FRACTION)) {
				return RED_LARGEST_SIZE;
			}
			if (DrawableUtils.between(ddt, VISIBILITY_FRACTION, ENLARGE_3_FRACTION_START)) {
				return 0;
			}
			if (DrawableUtils.between(ddt, ENLARGE_3_FRACTION_START, ENLARGE_3_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, ENLARGE_3_FRACTION_START, ENLARGE_3_FRACTION_END);
				return DrawableUtils.enlarge(0, RED_DEFAULT_SIZE, t);
			}
			return 0;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (redSize > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), redSize / 2f, getPaint());
			}
			if (blackSize > 0) {
				canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), blackSize / 2f, bgPaint);
			}
		}
	}
}
