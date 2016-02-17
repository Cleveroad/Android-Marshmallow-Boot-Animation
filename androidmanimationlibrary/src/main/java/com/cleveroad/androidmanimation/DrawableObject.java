package com.cleveroad.androidmanimation;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Created by Александр on 15.02.2016.
 */
public interface DrawableObject {

	void update(@NonNull RectF bounds, long dt);

	void draw(@NonNull Canvas canvas);
}
