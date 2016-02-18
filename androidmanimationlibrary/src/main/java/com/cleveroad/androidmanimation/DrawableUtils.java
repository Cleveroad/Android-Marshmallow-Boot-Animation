package com.cleveroad.androidmanimation;

/**
 * Helpful utils class.
 */
class DrawableUtils {

	private DrawableUtils() {}

	/**
	 * Trapeze function.
	 * @param t current value
	 * @param a value at <b>aT</b> point of time
	 * @param aT first point
	 * @param b value at <b>bT</b> point of time
	 * @param bT second point
	 * @param c value at <b>cT</b> point of time
	 * @param cT third point
	 * @param d value at <b>dT</b> point of time
	 * @param dT forth point
	 * @return calculated value
	 */
	public static float trapeze(float t, float a, float aT, float b, float bT, float c, float cT, float d, float dT) {
		if (t < aT) {
			return a;
		}
		if (t >= aT && t < bT) {
			float norm = normalize(t, aT, bT);
			return a + norm * (b - a);
		}
		if (t >= bT && t < cT) {
			float norm = normalize(t, bT, cT);
			return b + norm * (c - b);
		}
		if (t >= cT && t <= dT) {
			float norm = normalize(t, cT, dT);
			return c + norm * (d - c);
		}
		return d;
	}

	/**
	 * Normalize value between minimum and maximum.
	 * @param val value
	 * @param minVal minimum value
	 * @param maxVal maximum value
	 * @return normalized value in range <code>0..1</code>
	 * @throws IllegalArgumentException if value is out of range <code>[minVal, maxVal]</code>
	 */
	public static float normalize(float val, float minVal, float maxVal) {
		if (val < minVal || val > maxVal)
			throw new IllegalArgumentException("Value must be between min and max values. [val, min, max]: [" + val + "," + minVal + ", " + maxVal + "]");
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

	/**
	 * Rotate point P around center point C.
	 * @param pX x coordinate of point P
	 * @param pY y coordinate of point P
	 * @param cX x coordinate of point C
	 * @param cY y coordinate of point C
	 * @param angleInDegrees rotation angle in degrees
	 * @return new x coordinate
	 */
	public static float rotateX(float pX, float pY, float cX, float cY, float angleInDegrees) {
		double angle = Math.toRadians(angleInDegrees);
		return (float) (Math.cos(angle) * (pX - cX) - Math.sin(angle) * (pY - cY) + cX);
	}

	/**
	 * Rotate point P around center point C.
	 * @param pX x coordinate of point P
	 * @param pY y coordinate of point P
	 * @param cX x coordinate of point C
	 * @param cY y coordinate of point C
	 * @param angleInDegrees rotation angle in degrees
	 * @return new y coordinate
	 */
	public static float rotateY(float pX, float pY, float cX, float cY, float angleInDegrees) {
		double angle = Math.toRadians(angleInDegrees);
		return (float) (Math.sin(angle) * (pX - cX) + Math.cos(angle) * (pY - cY) + cY);
	}

	/**
	 * Checks if value belongs to range <code>[start, end]</code>
	 * @param value value
	 * @param start start of range
	 * @param end end of range
	 * @return true if value belogs to range, false otherwise
	 */
	public static boolean between(float value, float start, float end) {
		if (start > end) {
			float tmp = start;
			start = end;
			end = tmp;
		}
		return value >= start && value <= end;
	}

	/**
	 * Enlarge value from startValue to endValue
	 * @param startValue start size
	 * @param endValue end size
	 * @param time time of animation
	 * @return new size value
	 */
	public static float enlarge(float startValue, float endValue, float time) {
		if (startValue > endValue)
			throw new IllegalArgumentException("Start size can't be larger than end size.");
		return startValue + (endValue - startValue) * time;
	}

	/**
	 * Reduce value from startValue to endValue
	 * @param startValue start size
	 * @param endValue end size
	 * @param time time of animation
	 * @return new size value
	 */
	public static float reduce(float startValue, float endValue, float time) {
		if (startValue < endValue)
			throw new IllegalArgumentException("End size can't be larger than start size.");
		return endValue + (startValue - endValue) * (1 - time);
	}
}
