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

		private static final float SIZE_FRACTION = 0.8f;

		private static final int RAYS_COUNT = 8;

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
			float curStep = IDLE_1_FRACTION - 100f * Constants.SPEED_COEFFICIENT / Constants.TOTAL_DURATION;
			if (ddt <= curStep) {
				draw = false;
				return;
			}
			draw = true;
			float width = getBounds().width() * 0.07f;
			float height = getBounds().width() * 0.24f;
			float halfSize = width / 2f;
			float cx = getBounds().centerX();
			float rotationDelayOffset = curStep + ROTATION_DELAY_FRACTION;
			float halfAnimationFraction = LONG_ANIMATION_FRACTION / 2f;
			float quadAnimationFraction = LONG_ANIMATION_FRACTION / 4f;
			float halfDurationOffset = curStep + halfAnimationFraction;
			float reverseOffset = curStep + quadAnimationFraction;
			float movementOffset = halfDurationOffset + quadAnimationFraction;
			curStep += LONG_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				if (ddt >= rotationDelayOffset) {
					float t = DrawableUtils.normalize(ddt, rotationDelayOffset, curStep);
					t = interpolator.getInterpolation(t);
					angle = -1 * t * 90;
				}
				if (ddt <= halfDurationOffset) {
					getPaint().setAlpha(255);
					dotsPaint.setAlpha(0);
					float l, t, r, b;
					l = cx - halfSize;
					r = cx + halfSize;
					if (ddt <= reverseOffset) {
						b = getBounds().top + height;
						t = b - enlarge(width, height, ddt, reverseOffset, quadAnimationFraction);
					} else {
						t = getBounds().top;
						b = t + reduce(height, width, ddt, halfDurationOffset, quadAnimationFraction);
					}
					rect.set(l, t, r, b);
					dotSize = 0;
				} else {
					float startPoint = getBounds().top + height - halfSize;
					dotSize = width;
					dotCx = cx;
					float t = DrawableUtils.normalize(ddt, halfDurationOffset, curStep);
					float dotsK = DrawableUtils.triangle(t, 0, 0, 1, 0.33f, 1, 1);
					float lineK = DrawableUtils.triangle(t, 1, 0, 1, 0.66f, 0, 1);
					dotsPaint.setAlpha((int) (dotsK * 255));
					getPaint().setAlpha((int) (lineK * 255));
					if (ddt <= movementOffset) {
						dotCy = startPoint;
					} else {
						dotCy = reduce(startPoint, getBounds().top + halfSize, ddt, curStep, quadAnimationFraction);
					}
				}
			} else {
				curStep = 1 - LONG_ANIMATION_FRACTION - SIZE_ANIMATION_FRACTION * 2;
				if (ddt > curStep) {
					dotSize = 0;
					getPaint().setAlpha(255);
					dotsPaint.setAlpha(0);
					halfDurationOffset = curStep + SIZE_ANIMATION_FRACTION;
					curStep += SIZE_ANIMATION_FRACTION * 4;
					if (ddt <= curStep) {
						float l, t, r, b;
						l = cx - halfSize;
						r = cx + halfSize;
						if (ddt <= halfDurationOffset) {
							t = getBounds().top;
							b = t + enlarge(width, height, ddt, halfDurationOffset, SIZE_ANIMATION_FRACTION*2);
						} else {
							b = getBounds().top + height;
							t = b - reduce(height, width, ddt, curStep, SIZE_ANIMATION_FRACTION*2);
						}
						rect.set(l, t, r, b);
					} else {
						draw = false;
					}
				}
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

		private static final float SIZE_FRACTION = 0.4f;

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
			float curStep = IDLE_1_FRACTION + SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return  0;
			}
			curStep += 2 * SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return enlarge(0, 1, ddt, curStep, SIZE_ANIMATION_FRACTION * 2);
			}
			curStep = 1 - LONG_ANIMATION_FRACTION - SIZE_ANIMATION_FRACTION * 3;
			if (ddt <= curStep) {
				return  1;
			}
			curStep += SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return enlarge(1, 1.3f, ddt, curStep, SIZE_ANIMATION_FRACTION);
			}
			curStep += SIZE_ANIMATION_FRACTION * 2;
			if (ddt <= curStep) {
				return reduce(1.3f, 0, ddt, curStep, SIZE_ANIMATION_FRACTION * 2);
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

		private static final float SIZE_FRACTION = 0.6f;

		private static final float YELLOW_DEFAULT_WIDTH = 0.25f;
		private static final float YELLOW_LARGE_WIDTH = 1.1f;

		private static final float BLACK_DEFAULT_WIDTH = 0.5f;
		private static final float BLACK_SMALL_WIDTH = 0.1f;

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
			float curStep = IDLE_1_FRACTION;
			if (ddt <= curStep) {
				return 1;
			}

			curStep += SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return reduce(1, 0.5f, ddt, curStep, SIZE_ANIMATION_FRACTION);
			}

			curStep = 1 - LONG_ANIMATION_FRACTION - SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return 0.5f;
			}

			curStep += LONG_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return enlarge(0.5f, 1, ddt, curStep, LONG_ANIMATION_FRACTION);
			}
			return 1;
		}

		private float computeBlackSizeFraction(float ddt) {
			float curStep = SIZE_ANIMATION_FRACTION * 2;
			if (ddt <= curStep) {
				return BLACK_DEFAULT_WIDTH;
			}
			curStep = 1 - LONG_ANIMATION_FRACTION - SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return BLACK_SMALL_WIDTH;

			}
			curStep = 1;
			if (ddt <= curStep) {
				return enlarge(BLACK_SMALL_WIDTH, BLACK_DEFAULT_WIDTH, ddt, curStep, SIZE_ANIMATION_FRACTION);
			}
			return 0;
		}

		private float computeYellowSizeFraction(float ddt) {
			float curStep = SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return YELLOW_DEFAULT_WIDTH;
			}

			curStep += SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return enlarge(YELLOW_DEFAULT_WIDTH, YELLOW_LARGE_WIDTH, ddt, curStep, SIZE_ANIMATION_FRACTION);
			}

			curStep = IDLE_1_FRACTION;
			if (ddt <= curStep) {
				return 0;
			}

			curStep += SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return enlarge(0, YELLOW_DEFAULT_WIDTH, ddt, curStep, SIZE_ANIMATION_FRACTION);
			}

			curStep = 1 - SIZE_ANIMATION_FRACTION * 2;
			if (ddt <= curStep) {
				return YELLOW_DEFAULT_WIDTH;
			}

			curStep += SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return reduce(YELLOW_DEFAULT_WIDTH, 0, ddt, curStep, SIZE_ANIMATION_FRACTION);
			}

			curStep += SIZE_ANIMATION_FRACTION;
			if (ddt <= curStep) {
				return enlarge(0, YELLOW_DEFAULT_WIDTH, ddt, curStep, SIZE_ANIMATION_FRACTION);
			}

			return 0;
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
