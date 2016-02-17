package com.cleveroad.androidmanimation;

/**
 * Created by Александр on 16.02.2016.
 */
public class DrawableUtils {

	public static float trapeze(float t, float a, float aT, float b, float bT, float c, float cT, float d, float dT) {
		if (t < aT) {
			return a;
		}
		if (t >= aT && t < bT) {
			return quad(normalize(t, aT, bT), a, (a + b) / 2, b);
		}
		if (t >= bT && t < cT) {
			return quad(normalize(t, bT, cT), b, (b + c) / 2, c);
		}
		if (t >= cT && t <= dT) {
			return quad(normalize(t, cT, dT), c, (c + d) / 2, d);
		}
		return d;
	}

	public static float triangle(float t, float a, float aT, float b, float bT, float c, float cT) {
		return trapeze(t, a, aT, b, bT, b, bT, c, cT);
	}

	public static float normalize(float val, float minVal, float maxVal) {
		return (val - minVal) / (maxVal - minVal);
	}

	/**
	 * Quadratic Bezier curve.
	 * @param t time
	 * @param p0 start point
	 * @param p1 control point
	 * @param p2 end point
	 * @return point on Bezier curve at some time <code>t</code>
	 */
	public static float quad(float t, float p0, float p1, float p2) {
		return (float) (p0 * Math.pow(1 - t, 2) + p1 * 2 * t * (1 - t) + p2 * t * t);
	}

	public static float rotateX(float pX, float pY, float cX, float cY, float angleInDegrees) {
		double angle = Math.toRadians(angleInDegrees);
		return (float) (Math.cos(angle) * (pX - cX) - Math.sin(angle) * (pY - cY) + cX);
	}

	public static float rotateY(float pX, float pY, float cX, float cY, float angleInDegrees) {
		double angle = Math.toRadians(angleInDegrees);
		return (float) (Math.sin(angle) * (pX - cX) + Math.cos(angle) * (pY - cY) + cY);
	}

	public static boolean between(float value, float start, float end) {
		if (start > end) {
			float tmp = start;
			start = end;
			end = tmp;
		}
		return value >= start && value <= end;
	}
}
