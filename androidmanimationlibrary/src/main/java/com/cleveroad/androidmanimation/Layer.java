package com.cleveroad.androidmanimation;

import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Created by Александр on 15.02.2016.
 */
public abstract class Layer implements DrawableObject {

	private final RectF bounds;
	private long duration;

	public Layer() {
		this.bounds = new RectF();
	}

	public final void update(@NonNull RectF bounds, long dt) {
		this.bounds.set(bounds);
		this.duration += dt;
		if (this.duration > Constants.TOTAL_DURATION * Constants.SPEED_COEFFICIENT) {
			this.duration %= Constants.TOTAL_DURATION * Constants.SPEED_COEFFICIENT;
		}
		update(bounds, dt, 1f * duration / (Constants.TOTAL_DURATION * Constants.SPEED_COEFFICIENT));
	}

	protected abstract void update(@NonNull RectF bounds, long dt, float ddt);

	void reset() {
		this.duration = 0;
	}
}
