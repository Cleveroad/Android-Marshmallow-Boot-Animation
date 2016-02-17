package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Created by Александр on 16.02.2016.
 */
public class SecondLayer extends Layer {

	private final DrawableObject[] objects;

	public SecondLayer(Paint redPaint, Paint yellowPaint, Paint bgPaint) {
		objects = new DrawableObject[1];
		objects[0] = new RedRing(redPaint, yellowPaint, bgPaint);
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

	private static final class YellowCircleOuter extends DrawableShape {

		public YellowCircleOuter(Paint paint) {
			super(paint);
		}

		@Override
		protected float getSizeFraction() {
			return 0;
		}

		@Override
		protected float getSpacingFraction() {
			return 0;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {

		}

		@Override
		public void draw(@NonNull Canvas canvas) {

		}
	}

	private static final class RedRing extends DrawableShape {

		private static final float SIZE_FRACTION = 0.6f;
		private static final float SPACING_FRACTION = (1 - SIZE_FRACTION) / 2f;

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
		protected float getSpacingFraction() {
			return SPACING_FRACTION;
		}

		@Override
		protected void update(@NonNull RectF bounds, long dt, float ddt) {
			width = getBounds().width();
			{
				// black circle size
				if (ddt <= Constants.Circle2.IDLE_1_FRACTION) {
					blackWidth = getBounds().width() * BLACK_DEFAULT_WIDTH;
				} else if (ddt >= 1 - Constants.Circle2.SIZE_ANIMATION_FRACTION && ddt <= 1) {
					blackWidth = getBounds().width() * enlarge(BLACK_SMALL_WIDTH, BLACK_DEFAULT_WIDTH, ddt, 1, Constants.Circle2.SIZE_ANIMATION_FRACTION);
				} else {
					blackWidth = getBounds().width() * BLACK_SMALL_WIDTH;
				}
			}
			{
				// red ring size
				float reduceOffset = Constants.Circle1.SIZE_ANIMATION_FRACTION * 2 + Constants.Circle1.IDLE_1_FRACTION;
				float reduceAnimatedOffset = reduceOffset + Constants.Circle2.SIZE_ANIMATION_FRACTION;
				float lastAnimatedOffset = 1 - (Constants.Circle1.SIZE_ANIMATION_FRACTION - Constants.Circle2.SIZE_ANIMATION_FRACTION);
				float lastOffset = lastAnimatedOffset - Constants.Circle2.SIZE_ANIMATION_FRACTION;
				if (ddt >= reduceOffset && ddt <= reduceAnimatedOffset) {
					width = getBounds().width() * reduce(1, 0.5f, ddt, reduceAnimatedOffset, Constants.Circle2.SIZE_ANIMATION_FRACTION);
				} else if (ddt > reduceAnimatedOffset && ddt < lastOffset) {
					width = getBounds().width() * 0.5f;
				} else if (ddt >= lastOffset && ddt <= lastAnimatedOffset) {
					width = getBounds().width() * enlarge(0.5f, 1, ddt, lastAnimatedOffset, Constants.Circle2.SIZE_ANIMATION_FRACTION);
				} else {
					width = getBounds().width();
				}
			}
			{
				// yellow circle size
				float reduceOffset = Constants.Circle1.SIZE_ANIMATION_FRACTION * 2 + Constants.Circle1.IDLE_1_FRACTION;
				float reduceAnimatedOffset = reduceOffset + Constants.Circle2.SIZE_ANIMATION_FRACTION;
				float offset = Constants.Circle2.SIZE_ANIMATION_FRACTION;
				float animationOffset = offset + Constants.Circle2.SIZE_ANIMATION_FRACTION;
				if (ddt <= offset) {
					yellowWidth = getBounds().width() * YELLOW_DEFAULT_WIDTH;
				} else if (ddt >= offset && ddt <= animationOffset) {
					yellowWidth = getBounds().width() * enlarge(YELLOW_DEFAULT_WIDTH, YELLOW_LARGE_WIDTH, ddt, animationOffset, Constants.Circle2.SIZE_ANIMATION_FRACTION);
				} else if (ddt >= reduceOffset && ddt <= reduceAnimatedOffset) {
					yellowWidth = getBounds().width() * enlarge(0, YELLOW_DEFAULT_WIDTH, ddt, reduceAnimatedOffset, Constants.Circle2.SIZE_ANIMATION_FRACTION);
				}
			}
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
