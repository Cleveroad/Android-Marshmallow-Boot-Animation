package com.cleveroad.androidmanimation;

import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Implementation of {@link DrawableObject}.
 */
abstract class DrawableObjectImpl implements DrawableObject, Resetable {

	private final Paint paint;
	private final RectF bounds;

	public DrawableObjectImpl(Paint paint) {
		this.paint = paint;
		this.bounds = new RectF();
	}

	@Override
	public final void update(@NonNull RectF bounds, float dt) {
		float spacing = (1 - getSizeFraction()) / 2f;
		float l = bounds.left + spacing * bounds.width();
		float t = bounds.top + spacing * bounds.height();
		float r = l + getSizeFraction() * bounds.width();
		float b = t + getSizeFraction() * bounds.height();
		getBounds().set(l, t, r, b);
		updateImpl(bounds, dt);
	}

	protected abstract float getSizeFraction();

	protected abstract void updateImpl(@NonNull RectF bounds, float ddt);

	public Paint getPaint() {
		return paint;
	}

	public RectF getBounds() {
		return bounds;
	}

	@Override
	public void reset() {

	}
}
