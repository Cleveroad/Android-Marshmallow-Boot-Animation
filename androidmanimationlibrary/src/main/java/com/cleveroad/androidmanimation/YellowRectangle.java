package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Yellow rounded rectangle class.
 */
final class YellowRectangle extends DrawableObjectImpl {


	private static final float SIZE_FRACTION = 0.6f;

	private static final float VISIBILITY_FRACTION_START = 16 * Constants.FRAME_SPEED;
	private static final float VISIBILITY_FRACTION_END = 137 * Constants.FRAME_SPEED;

	private static final float ENLARGE_FRACTION_START = 19 * Constants.FRAME_SPEED;
	private static final float ENLARGE_FRACTION_END = 36 * Constants.FRAME_SPEED;

	private static final float REDUCE_FRACTION_1_START = 36 * Constants.FRAME_SPEED;
	private static final float REDUCE_FRACTION_1_END = 56 * Constants.FRAME_SPEED;

	private static final float SWAP_FRACTION = 58 * Constants.FRAME_SPEED;

	private static final float REDUCE_FRACTION_2_START = 58 * Constants.FRAME_SPEED;
	private static final float REDUCE_FRACTION_2_END = 70 * Constants.FRAME_SPEED;

	private static final float REDUCE_FRACTION_3_START = 90 * Constants.FRAME_SPEED;
	private static final float REDUCE_FRACTION_3_END = 130 * Constants.FRAME_SPEED;

	private static final float REDUCE_FRACTION_4_START = 131 * Constants.FRAME_SPEED;
	private static final float REDUCE_FRACTION_4_END = 137 * Constants.FRAME_SPEED;

	private static final float MOVEMENT_FRACTION_START = 79 * Constants.FRAME_SPEED;
	private static final float MOVEMENT_FRACTION_END = 130 * Constants.FRAME_SPEED;

	private static final float START_ANGLE = (float) (Math.PI / 2);
	private static final float END_ANGLE = (float) (2.5 * Math.PI);

	private static final float SMALLEST_SIZE = 0.6f;
	private static final float SMALL_SIZE = 0.7f;

	private float cx1, cy1, cx2, cy2, cx3, cy3;
	private RectF rect;
	private boolean draw;
	private float radius;
	private boolean drawCircle;
	private float cx, cy;

	public YellowRectangle(Paint paint) {
		super(paint);
		this.rect = new RectF();
	}

	@Override
	protected float getSizeFraction() {
		return SIZE_FRACTION;
	}

	public void setFirstValues(float cx1, float cy1) {
		this.cx1 = cx1;
		this.cy1 = cy1;
	}

	public void setSecondValues(float cx2, float cy2) {
		this.cx2 = cx2;
		this.cy2 = cy2;
	}

	public void setThirdValues(float cx3, float cy3) {
		this.cx3 = cx3;
		this.cy3 = cy3;
	}

	public void updateRadius(float size) {
		this.radius = size * SIZE_FRACTION / 2f;
	}

	@Override
	protected void update(@NonNull RectF bounds, long dt, float ddt) {
		draw = DrawableUtils.between(ddt, VISIBILITY_FRACTION_START, VISIBILITY_FRACTION_END);
		if (!draw) {
			return;
		}
		drawCircle = ddt >= SWAP_FRACTION;
		if (DrawableUtils.between(ddt, VISIBILITY_FRACTION_START, ENLARGE_FRACTION_END)) {
			float l, r, t, b;
			l = cx1 - radius;
			t = cy1 - radius;
			r = cx1 + radius;
			b = cy1 + radius;
			if (ddt >= ENLARGE_FRACTION_START) {
				float time = DrawableUtils.normalize(ddt, ENLARGE_FRACTION_START, ENLARGE_FRACTION_END);
				r = DrawableUtils.enlarge(r, r + (cx2 - cx1), time);
			}
			rect.set(l, t, r, b);
		}
		if (DrawableUtils.between(ddt, REDUCE_FRACTION_1_START, REDUCE_FRACTION_1_END)) {
			float l, r, t, b;
			l = cx2 - radius;
			t = cy2 - radius;
			r = cx2 + radius;
			b = cy2 + radius;
			float time = DrawableUtils.normalize(ddt, REDUCE_FRACTION_1_START, REDUCE_FRACTION_1_END);
			l = DrawableUtils.enlarge(l - (cx2 - cx1), l, time);
			rect.set(l, t, r, b);
		}
		if (drawCircle) {
			cx = cx2;
			cy = cy2;
			rect.set(0, 0, radius * 2, radius * 2);
			if (DrawableUtils.between(ddt, REDUCE_FRACTION_2_START, REDUCE_FRACTION_2_END)) {
				float t = DrawableUtils.normalize(ddt, REDUCE_FRACTION_2_START, REDUCE_FRACTION_2_END);
				float size = DrawableUtils.reduce(radius * 2, radius * SMALL_SIZE, t);
				rect.set(0, 0, size, size);
			}
			if (DrawableUtils.between(ddt, REDUCE_FRACTION_2_END,REDUCE_FRACTION_3_START)) {
				rect.set(0, 0, radius * SMALL_SIZE, radius * SMALL_SIZE);
			}
			if (DrawableUtils.between(ddt, REDUCE_FRACTION_3_START, REDUCE_FRACTION_3_END)) {
				float t = DrawableUtils.normalize(ddt, REDUCE_FRACTION_3_START, REDUCE_FRACTION_3_END);
				float size = DrawableUtils.reduce(radius * SMALL_SIZE, radius * SMALLEST_SIZE, t);
				rect.set(0, 0, size, size);
			}
			if (DrawableUtils.between(ddt, REDUCE_FRACTION_3_END, REDUCE_FRACTION_4_START)) {
				rect.set(0, 0, radius * SMALLEST_SIZE, radius * SMALLEST_SIZE);
			}
			if (DrawableUtils.between(ddt, REDUCE_FRACTION_4_START, REDUCE_FRACTION_4_END)) {
				float t = DrawableUtils.normalize(ddt, REDUCE_FRACTION_4_START, REDUCE_FRACTION_4_END);
				float size = DrawableUtils.reduce(radius * SMALLEST_SIZE, 0, t);
				rect.set(0, 0, size, size);
			}
			if (DrawableUtils.between(ddt, MOVEMENT_FRACTION_START, MOVEMENT_FRACTION_END)) {
				float t = DrawableUtils.normalize(ddt, MOVEMENT_FRACTION_START, MOVEMENT_FRACTION_END);
				float height = getBounds().height() / 2f;
				float cos = (float) Math.sin(START_ANGLE + t * (END_ANGLE - START_ANGLE));
				cx = cx2 + t * (cx3 - cx2);
				cy = cy2 + height * (1 - Math.abs(cos));
			} else if (ddt >= MOVEMENT_FRACTION_END) {
				cx = cx3;
				cy = cy3;
			}
		}
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		if (draw) {
			if (drawCircle) {
				canvas.drawCircle(cx, cy, rect.width() / 2f, getPaint());
			} else {
				canvas.drawRoundRect(rect, radius, radius, getPaint());
			}
		}
	}
}
