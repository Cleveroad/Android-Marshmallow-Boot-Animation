package com.cleveroad.androidmanimation;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Color utils class.
 */
class ColorUtil {

	private ColorUtil() {

	}

	@SuppressWarnings("deprecation")
	public static int getColor(@NonNull Context context, int colorId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return context.getColor(colorId);
		}
		return context.getResources().getColor(colorId);
	}
}
