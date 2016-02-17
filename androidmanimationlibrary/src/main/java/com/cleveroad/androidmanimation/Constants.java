package com.cleveroad.androidmanimation;

/**
 * Created by Александр on 15.02.2016.
 */
public class Constants {

	private Constants() {
	}

	public static final int SPEED_COEFFICIENT = 1;

	public static final long TOTAL_DURATION = 4530 * SPEED_COEFFICIENT;

	public static class Circle1 {

		public static final long SIZE_ANIMATION_DURATION = 300 * SPEED_COEFFICIENT;
		public static final long IDLE_1_DURATION = 1500 * SPEED_COEFFICIENT;
		public static final long IDLE_2_DURATION = 1800 * SPEED_COEFFICIENT;

		public static final float SIZE_ANIMATION_FRACTION = 1f * SIZE_ANIMATION_DURATION / Constants.TOTAL_DURATION;
		public static final float IDLE_1_FRACTION = 1f * IDLE_1_DURATION / Constants.TOTAL_DURATION;
		public static final float IDLE_2_FRACTION = 1f * IDLE_2_DURATION / Constants.TOTAL_DURATION;

		private Circle1() {
		}
	}

	public static class Circle2 {

		public static final long SIZE_ANIMATION_DURATION = Circle1.SIZE_ANIMATION_DURATION / 4 * SPEED_COEFFICIENT;
		public static final long IDLE_1_DURATION = 450 * SPEED_COEFFICIENT;

		public static final float SIZE_ANIMATION_FRACTION = 1f * SIZE_ANIMATION_DURATION / Constants.TOTAL_DURATION;
		public static final float IDLE_1_FRACTION = 1f * IDLE_1_DURATION / Constants.TOTAL_DURATION;

		private Circle2() {
		}
	}

}
