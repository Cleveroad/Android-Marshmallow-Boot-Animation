package com.cleveroad.androidmanimation;

import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Created by Александр on 15.02.2016.
 */
public abstract class DrawableShape implements DrawableObject {

	private final Paint paint;
	private final RectF bounds;
	private long duration;

	public DrawableShape(Paint paint) {
		this.paint = paint;
		this.bounds = new RectF();
	}

	@Override
	public final void update(@NonNull RectF bounds, long dt) {
		float l = bounds.left + getSpacingFraction() * bounds.width();
		float t = bounds.top + getSpacingFraction() * bounds.height();
		float r = l + getSizeFraction() * bounds.width();
		float b = t + getSizeFraction() * bounds.height();
		getBounds().set(l, t, r, b);
		this.duration += dt;
		if (this.duration > Constants.TOTAL_DURATION) {
			this.duration %= Constants.TOTAL_DURATION;
		}
		update(bounds, dt, 1f * duration / Constants.TOTAL_DURATION);
	}

	protected abstract float getSizeFraction();

	protected abstract float getSpacingFraction();

	protected abstract void update(@NonNull RectF bounds, long dt, float ddt);

	float enlarge(float startSize, float endSize, float dt, float maxDt, float length) {
		float t = (1 - (maxDt - dt) / length);
		return startSize + (endSize - startSize) * t;
	}

	float reduce(float startSize, float endSize, float dt, float maxDt, float length) {
		float t = (maxDt - dt) / length;
		return endSize + (startSize - endSize) * t;
	}

	public Paint getPaint() {
		return paint;
	}

	public RectF getBounds() {
		return bounds;
	}
}
