package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Александр on 16.02.2016.
 */
public class SecondLayer extends Layer {

	private static final long SIZE_ANIMATION_DURATION = 200 * Constants.SPEED_COEFFICIENT;
	private static final long IDLE_1_DURATION = 2100 * Constants.SPEED_COEFFICIENT;
	private static final long LONG_ANIMATION_DURATION = 600 * Constants.SPEED_COEFFICIENT;
	private static final long ROTATION_DELAY_DURATION = 100 * Constants.SPEED_COEFFICIENT;

	private static final float SIZE_ANIMATION_FRACTION = 1f * SIZE_ANIMATION_DURATION / Constants.TOTAL_DURATION;
	private static final float IDLE_1_FRACTION = 1f * IDLE_1_DURATION / Constants.TOTAL_DURATION;
	private static final float LONG_ANIMATION_FRACTION = 1f * LONG_ANIMATION_DURATION / Constants.TOTAL_DURATION;
	private static final float ROTATION_DELAY_FRACTION = 1f * ROTATION_DELAY_DURATION / Constants.TOTAL_DURATION;

	private final DrawableObject[] objects;

	public SecondLayer(Paint redPaint, Paint yellowPaint, Paint bgPaint) {
		objects = new DrawableObject[3];
		objects[0] = new YellowCircleInner(yellowPaint);
		objects[1] = new RedSunRays(redPaint);
		objects[2] = new RedRing(redPaint, yellowPaint, bgPaint);
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

	private static final class RedSunRays extends DrawableShape {

		private static final float ROTATION_FRACTION_START = 54 * Constants.FRAME_SPEED;
		private static final float ROTATION_FRACTION_END = 88 * Constants.FRAME_SPEED;

		private static final float ENLARGE_1_FRACTION_START = 48 * Constants.FRAME_SPEED;
		private static final float ENLARGE_1_FRACTION_END = 57 * Constants.FRAME_SPEED;

		private static final float REDUCE_1_FRACTION_START = 58 * Constants.FRAME_SPEED;
		private static final float REDUCE_1_FRACTION_END = 64 * Constants.FRAME_SPEED;

		private static final float CIRCLES_MOVEMENT_FRACTION_START = 63 * Constants.FRAME_SPEED;
		private static final float CIRCLES_MOVEMENT_FRACTION_END = 68 * Constants.FRAME_SPEED;

		private static final float CIRCLES_VISIBILITY_FRACTION_START = 60 * Constants.FRAME_SPEED;
		private static final float CIRCLES_VISIBILITY_FRACTION_END = 62 * Constants.FRAME_SPEED;

		private static final float RAYS_VISIBILITY_1_FRACTION_START = 65 * Constants.FRAME_SPEED;
		private static final float RAYS_VISIBILITY_1_FRACTION_END = 68 * Constants.FRAME_SPEED;

		private static final float ENLARGE_2_FRACTION_START = 114 * Constants.FRAME_SPEED;
		private static final float ENLARGE_2_FRACTION_END = 126 * Constants.FRAME_SPEED;

		private static final float REDUCE_2_FRACTION_START = 127 * Constants.FRAME_SPEED;
		private static final float REDUCE_2_FRACTION_END = 137 * Constants.FRAME_SPEED;

		private static final float RAYS_VISIBILITY_2_FRACTION_START = 134 * Constants.FRAME_SPEED;
		private static final float RAYS_VISIBILITY_2_FRACTION_END = 136 * Constants.FRAME_SPEED;

		private static final float SWAP_FRACTION = 90 * Constants.FRAME_SPEED;

		private static final float SIZE_FRACTION = 0.8f;

		private static final int RAYS_COUNT = 8;
		private static final float END_ANGLE = -90;

		private final Paint dotsPaint;
		private final RectF rect;
		private final Interpolator interpolator;
		private float angle;
		private boolean draw;
		private float dotSize;
		private float dotCx, dotCy;

		public RedSunRays(Paint paint) {
			super(new Paint(paint));
			this.dotsPaint = new Paint(paint);
			this.rect = new RectF();
			this.interpolator = new AccelerateDecelerateInterpolator();
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			draw = true;
			dotSize = 0;
			if (ddt < ENLARGE_1_FRACTION_START || ddt > REDUCE_2_FRACTION_END) {
				draw = false;
				return;
			}

			if (DrawableUtils.between(ddt, ROTATION_FRACTION_START, ROTATION_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, ROTATION_FRACTION_START, ROTATION_FRACTION_END);
				angle = t * END_ANGLE;
			}

			float width = getBounds().width() * 0.07f;
			float height = getBounds().width() * 0.24f;
			float halfSize = width / 2f;
			float cx = getBounds().centerX();

			if (DrawableUtils.between(ddt, ENLARGE_1_FRACTION_START, ENLARGE_1_FRACTION_END)) {
				float l, t, r, b;
				l = cx - halfSize;
				r = cx + halfSize;
				b = getBounds().top + height;
				t = b - enlarge(width, height, ddt, ENLARGE_1_FRACTION_END, ENLARGE_1_FRACTION_END - ENLARGE_1_FRACTION_START);
				rect.set(l, t, r, b);
			}
			if (DrawableUtils.between(ddt, REDUCE_1_FRACTION_START, REDUCE_1_FRACTION_END)) {
				float l, t, r, b;
				l = cx - halfSize;
				r = cx + halfSize;
				t = getBounds().top;
				b = t + reduce(height, width, ddt, REDUCE_1_FRACTION_END, REDUCE_1_FRACTION_END - REDUCE_1_FRACTION_START);
				rect.set(l, t, r, b);
			}

			if (DrawableUtils.between(ddt, ENLARGE_2_FRACTION_START, ENLARGE_2_FRACTION_END)) {
				float l, t, r, b;
				l = cx - halfSize;
				r = cx + halfSize;
				t = getBounds().top;
				b = t + enlarge(width, height, ddt, ENLARGE_2_FRACTION_END, ENLARGE_2_FRACTION_END - ENLARGE_2_FRACTION_START);
				rect.set(l, t, r, b);
			}
			if (DrawableUtils.between(ddt, REDUCE_2_FRACTION_START, REDUCE_2_FRACTION_END)) {
				float l, t, r, b;
				l = cx - halfSize;
				r = cx + halfSize;
				b = getBounds().top + height;
				t = b - reduce(height, width, ddt, REDUCE_2_FRACTION_END, REDUCE_2_FRACTION_END - REDUCE_2_FRACTION_START);
				rect.set(l, t, r, b);
			}

			float startPoint = getBounds().top + height - halfSize;
			if (DrawableUtils.between(ddt, CIRCLES_VISIBILITY_FRACTION_START, SWAP_FRACTION)) {
				dotCx = cx;
				if (dotCy == 0)
					dotCy = startPoint;
				dotSize = width;
			} else {
				dotSize = 0;
			}

			if (DrawableUtils.between(ddt, CIRCLES_MOVEMENT_FRACTION_START, CIRCLES_VISIBILITY_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, CIRCLES_MOVEMENT_FRACTION_START, CIRCLES_VISIBILITY_FRACTION_END);
				dotsPaint.setAlpha((int) (t * 255));
			}
			if (DrawableUtils.between(ddt, CIRCLES_MOVEMENT_FRACTION_START, CIRCLES_MOVEMENT_FRACTION_END)) {
				dotCy = reduce(startPoint, getBounds().top + halfSize, ddt, CIRCLES_MOVEMENT_FRACTION_END, CIRCLES_MOVEMENT_FRACTION_END - CIRCLES_MOVEMENT_FRACTION_START);
				dotsPaint.setAlpha(255);
			}


			if (DrawableUtils.between(ddt, RAYS_VISIBILITY_1_FRACTION_START, RAYS_VISIBILITY_1_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, RAYS_VISIBILITY_1_FRACTION_START, RAYS_VISIBILITY_1_FRACTION_END);
				getPaint().setAlpha((int) ((1 - t) * 255));
			}

			if (DrawableUtils.between(ddt, RAYS_VISIBILITY_2_FRACTION_START, RAYS_VISIBILITY_2_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, RAYS_VISIBILITY_2_FRACTION_START, RAYS_VISIBILITY_2_FRACTION_END);
				getPaint().setAlpha((int) ((1 - t) * 255));
			}

			if (ddt >= SWAP_FRACTION) {
				getPaint().setAlpha(255);
				dotsPaint.setAlpha(0);
			}
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (!draw)
				return;
			float halfSize = rect.width() / 2f;
			float cx = getBounds().centerX();
			float cy = getBounds().centerY();
			canvas.save();
			canvas.rotate(angle, cx, cy);
			for (int i=0; i<RAYS_COUNT; i++) {
				canvas.rotate(i * 360f / RAYS_COUNT, cx, cy);
				canvas.drawRoundRect(rect, halfSize, halfSize, getPaint());
				if (dotSize > 0) {
					canvas.drawCircle(dotCx, dotCy, dotSize / 2f, dotsPaint);
				}
			}
			canvas.restore();
		}
	}

	private static final class YellowCircleInner extends DrawableShape {

		private static final float ENLARGE_FRACTION_START = 65 * Constants.FRAME_SPEED;
		private static final float ENLARGE_FRACTION_END = 72 * Constants.FRAME_SPEED;

		private static final float MORE_ENLARGE_FRACTION_START = 97 * Constants.FRAME_SPEED;
		private static final float MORE_ENLARGE_FRACTION_END = 110 * Constants.FRAME_SPEED;

		private static final float MORE_REDUCE_FRACTION_START = 110 * Constants.FRAME_SPEED;
		private static final float MORE_REDUCE_FRACTION_END = 121 * Constants.FRAME_SPEED;


		private static final float SIZE_FRACTION = 0.6f;

		private float size;

		public YellowCircleInner(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			size = getSizeFraction(ddt) * getBounds().width();
		}

		private float getSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, ENLARGE_FRACTION_START, ENLARGE_FRACTION_END)) {
				return enlarge(0.5f, 0.65f, ddt, ENLARGE_FRACTION_END, ENLARGE_FRACTION_END - ENLARGE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, ENLARGE_FRACTION_END, MORE_ENLARGE_FRACTION_START)) {
				return 0.65f;
			}
			if (DrawableUtils.between(ddt, MORE_ENLARGE_FRACTION_START, MORE_ENLARGE_FRACTION_END)) {
				return enlarge(0.65f, 0.8f, ddt, MORE_ENLARGE_FRACTION_END, MORE_ENLARGE_FRACTION_END - MORE_ENLARGE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, MORE_ENLARGE_FRACTION_END, MORE_REDUCE_FRACTION_START)) {
				return 0.8f;
			}
			if (DrawableUtils.between(ddt, MORE_REDUCE_FRACTION_START, MORE_REDUCE_FRACTION_END)) {
				return reduce(0.8f, 0.5f, ddt, MORE_REDUCE_FRACTION_END, MORE_REDUCE_FRACTION_END - MORE_REDUCE_FRACTION_START);
			}
			return 0;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			if (size > 0) {
				float cx = getBounds().centerX();
				float cy = getBounds().centerY();
				canvas.drawCircle(cx, cy, size / 2f, getPaint());
			}
		}
	}

	private static final class RedRing extends DrawableShape {

		private static final float FRACTION_START = Constants.FIRST_FRAME_FRACTION;

		private static final float YELLOW_ENLARGE_FRACTION_START = 7 * Constants.FRAME_SPEED;
		private static final float YELLOW_ENLARGE_FRACTION_END = 19 * Constants.FRAME_SPEED;
		private static final float YELLOW_MEDIUM_ENLARGE_FRACTION_START = 52 * Constants.FRAME_SPEED;
		private static final float YELLOW_MEDIUM_ENLARGE_FRACTION_END = 62 * Constants.FRAME_SPEED;
		private static final float YELLOW_MEDIUM_REDUCE_FRACTION_START = 131 * Constants.FRAME_SPEED;
		private static final float YELLOW_MEDIUM_REDUCE_FRACTION_END = 139 * Constants.FRAME_SPEED;
		private static final float YELLOW_RESTORE_FRACTION_START = 140 * Constants.FRAME_SPEED;
		private static final float YELLOW_RESTORE_FRACTION_END = Constants.LAST_FRAME_FRACTION;

		private static final float BLACK_REDUCED_FRACTION = 12 * Constants.FRAME_SPEED;
		private static final float BLACK_RESTORE_FRACTION_START = 139 * Constants.FRAME_SPEED;
		private static final float BLACK_RESTORE_FRACTION_END = Constants.LAST_FRAME_FRACTION;

		private static final float RED_REDUCE_FRACTION_START = 50 * Constants.FRAME_SPEED;
		private static final float RED_REDUCE_FRACTION_END = 60 * Constants.FRAME_SPEED;
		private static final float RED_ENLARGE_FRACTION_START = 125 * Constants.FRAME_SPEED;
		private static final float RED_ENLARGE_FRACTION_END = 139 * Constants.FRAME_SPEED;


		private static final float SIZE_FRACTION = 0.6f;

		private static final float YELLOW_DEFAULT_SIZE = 0.25f;
		private static final float YELLOW_LARGE_SIZE = 1.1f;

		private static final float BLACK_DEFAULT_SIZE = 0.5f;
		private static final float BLACK_SMALL_SIZE = 0.1f;

		private static final float RED_DEFAULT_SIZE = 1f;
		private static final float RED_SMALL_SIZE = 0.5f;

		private static final float ZERO_SIZE = 0f;

		private final Paint bgPaint;
		private final Paint yellowPaint;
		private float blackWidth;
		private float width;
		private float yellowWidth;

		public RedRing(Paint paint, Paint yellowPaint, Paint bgPaint) {
			super(paint);
			this.bgPaint = bgPaint;
			this.yellowPaint = yellowPaint;
		}

		@Override
		protected float getSizeFraction() {
			return SIZE_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = computeRedSizeFraction(ddt) * getBounds().width();
			blackWidth = computeBlackSizeFraction(ddt) * getBounds().width();
			yellowWidth = computeYellowSizeFraction(ddt) * getBounds().width();
		}

		private float computeRedSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, FRACTION_START, RED_REDUCE_FRACTION_START)) {
				return RED_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, RED_REDUCE_FRACTION_START, RED_REDUCE_FRACTION_END)) {
				return reduce(RED_DEFAULT_SIZE, RED_SMALL_SIZE, ddt, RED_REDUCE_FRACTION_END, RED_REDUCE_FRACTION_END - RED_REDUCE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, RED_REDUCE_FRACTION_END, RED_ENLARGE_FRACTION_START)) {
				return RED_SMALL_SIZE;
			}
			if (DrawableUtils.between(ddt, RED_ENLARGE_FRACTION_START, RED_ENLARGE_FRACTION_END)) {
				return enlarge(RED_SMALL_SIZE, RED_DEFAULT_SIZE, ddt, RED_ENLARGE_FRACTION_END, RED_ENLARGE_FRACTION_END - RED_ENLARGE_FRACTION_START);
			}
			return RED_DEFAULT_SIZE;
		}

		private float computeBlackSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, FRACTION_START, BLACK_REDUCED_FRACTION)) {
				return BLACK_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, BLACK_REDUCED_FRACTION, BLACK_RESTORE_FRACTION_START)) {
				return BLACK_SMALL_SIZE;
			}
			if (DrawableUtils.between(ddt, BLACK_RESTORE_FRACTION_START, BLACK_RESTORE_FRACTION_END)) {
				return enlarge(BLACK_SMALL_SIZE, BLACK_DEFAULT_SIZE, ddt, BLACK_RESTORE_FRACTION_END, BLACK_RESTORE_FRACTION_END - BLACK_RESTORE_FRACTION_START);
			}
			return ZERO_SIZE;
		}

		private float computeYellowSizeFraction(float ddt) {
			if (DrawableUtils.between(ddt, FRACTION_START, YELLOW_ENLARGE_FRACTION_START)) {
				return YELLOW_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, YELLOW_ENLARGE_FRACTION_START, YELLOW_ENLARGE_FRACTION_END)) {
				return enlarge(YELLOW_DEFAULT_SIZE, YELLOW_LARGE_SIZE, ddt, YELLOW_ENLARGE_FRACTION_END, YELLOW_ENLARGE_FRACTION_END - YELLOW_ENLARGE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, YELLOW_ENLARGE_FRACTION_END, YELLOW_MEDIUM_ENLARGE_FRACTION_START)) {
				return ZERO_SIZE;
			}
			if (DrawableUtils.between(ddt, YELLOW_MEDIUM_ENLARGE_FRACTION_START, YELLOW_MEDIUM_ENLARGE_FRACTION_END)) {
				return enlarge(ZERO_SIZE, YELLOW_DEFAULT_SIZE, ddt, YELLOW_MEDIUM_ENLARGE_FRACTION_END, YELLOW_MEDIUM_ENLARGE_FRACTION_END - YELLOW_MEDIUM_ENLARGE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, YELLOW_MEDIUM_ENLARGE_FRACTION_END, YELLOW_MEDIUM_REDUCE_FRACTION_START)) {
				return YELLOW_DEFAULT_SIZE;
			}
			if (DrawableUtils.between(ddt, YELLOW_MEDIUM_REDUCE_FRACTION_START, YELLOW_MEDIUM_REDUCE_FRACTION_END)) {
				return reduce(YELLOW_DEFAULT_SIZE, ZERO_SIZE, ddt, YELLOW_MEDIUM_REDUCE_FRACTION_END, YELLOW_MEDIUM_REDUCE_FRACTION_END - YELLOW_MEDIUM_REDUCE_FRACTION_START);
			}
			if (DrawableUtils.between(ddt, YELLOW_MEDIUM_REDUCE_FRACTION_END, YELLOW_RESTORE_FRACTION_START)) {
				return ZERO_SIZE;
			}
			if (DrawableUtils.between(ddt, YELLOW_RESTORE_FRACTION_START, YELLOW_RESTORE_FRACTION_END)) {
				return enlarge(ZERO_SIZE, YELLOW_DEFAULT_SIZE, ddt, YELLOW_RESTORE_FRACTION_END, YELLOW_RESTORE_FRACTION_END - YELLOW_RESTORE_FRACTION_START);
			}
			return ZERO_SIZE;
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			float cx = getBounds().centerX();
			float cy = getBounds().centerY();
			canvas.drawCircle(cx, cy, width / 2f, getPaint());
			if (blackWidth > 0) {
				canvas.drawCircle(cx, cy, blackWidth / 2f, bgPaint);
			}
			if (yellowWidth > 0) {
				canvas.drawCircle(cx, cy, yellowWidth / 2f, yellowPaint);
			}
		}
	}
}
