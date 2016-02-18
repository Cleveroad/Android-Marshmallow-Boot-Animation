package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Interface of drawable shape
 */
interface DrawableObject {

	void update(@NonNull RectF bounds, long dt);

	void draw(@NonNull Canvas canvas);
}
