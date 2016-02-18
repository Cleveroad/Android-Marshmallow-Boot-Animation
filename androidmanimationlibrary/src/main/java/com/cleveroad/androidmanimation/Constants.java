package com.cleveroad.androidmanimation;

/**
 * Created by Александр on 15.02.2016.
 */
public class Constants {

	private Constants() {
	}

	public static final float SPEED_COEFFICIENT = 1f;

	public static final long TOTAL_DURATION = 4530;

	public static final int TOTAL_FRAMES = 151;

	public static final int MS_PER_FRAME = 30;

	public static final float FRAME_SPEED = 1f * MS_PER_FRAME / TOTAL_DURATION;

	public static final float FIRST_FRAME_FRACTION = 0;

	public static final float LAST_FRAME_FRACTION = TOTAL_FRAMES * FRAME_SPEED;
}
