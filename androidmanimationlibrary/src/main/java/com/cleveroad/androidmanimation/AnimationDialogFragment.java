package com.cleveroad.androidmanimation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dialog fragment with loading animation.
 */
public class AnimationDialogFragment extends DialogFragment {

	private static final String EXTRA_BUILDER = "EXTRA_BUILDER";
	private LoadingAnimationView animationView;

	public AnimationDialogFragment() {
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	@SuppressLint("ValidFragment")
	private AnimationDialogFragment(Builder builder) {
		this();
		Bundle args = new Bundle();
		args.putParcelable(EXTRA_BUILDER, builder);
		setArguments(args);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.view_loading_animation, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		animationView = (LoadingAnimationView) view.findViewById(R.id.animation);
		Bundle args = getArguments();
		if (args != null) {
			Builder builder = args.getParcelable(EXTRA_BUILDER);
			if (builder != null) {
				animationView.fromBuilder(builder);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		animationView.startAnimation();
	}

	@Override
	public void onPause() {
		animationView.pauseAnimation();
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		animationView.stopAnimation();
		super.onDestroyView();
	}

	/**
	 * Builder class for dialog with loading animation.
	 */
	public static final class Builder implements Parcelable {

		private Integer firstColor, secondColor, thirdColor, fourthColor;
		private Integer backgroundColor;
		private float speedCoefficient;


		/**
		 * Set first color. Default value: red.
		 * @param firstColor first color
		 */
		public Builder setFirstColor(@ColorInt int firstColor) {
			this.firstColor = firstColor;
			return this;
		}

		/**
		 * Set second color. Default value: green.
		 * @param secondColor second color
		 */
		public Builder setSecondColor(@ColorInt int secondColor) {
			this.secondColor = secondColor;
			return this;
		}

		/**
		 * Set third color. Default value: blue.
		 * @param thirdColor third color
		 */
		public Builder setThirdColor(@ColorInt int thirdColor) {
			this.thirdColor = thirdColor;
			return this;
		}

		/**
		 * Set fourth color. Default value: yellow.
		 * @param fourthColor fourth color
		 */
		public Builder setFourthColor(@ColorInt int fourthColor) {
			this.fourthColor = fourthColor;
			return this;
		}

		/**
		 * Set background color. Default value: black.
		 * @param backgroundColor background color
		 */
		public Builder setBackgroundColor(@ColorInt int backgroundColor) {
			this.backgroundColor = backgroundColor;
			return this;
		}

		/**
		 * Set speed coefficient. Default value: 1.
		 * @param speedCoefficient speed coefficient in range <code>[0..Integer.MAX_VALUE]</code>
		 */
		public Builder setSpeedCoefficient(float speedCoefficient) {
			this.speedCoefficient = speedCoefficient;
			return this;
		}

		int getFirstColor(@NonNull Context context) {
			if (firstColor == null)
				return ColorUtil.getColor(context, R.color.google_red);
			return firstColor;
		}

		int getSecondColor(@NonNull Context context) {
			if (secondColor == null)
				return ColorUtil.getColor(context, R.color.google_green);
			return secondColor;
		}

		int getThirdColor(@NonNull Context context) {
			if (thirdColor == null)
				return ColorUtil.getColor(context, R.color.google_blue);
			return thirdColor;
		}

		int getFourthColor(@NonNull Context context) {
			if (fourthColor == null)
				return ColorUtil.getColor(context, R.color.google_yellow);
			return fourthColor;
		}

		int getBackgroundColor() {
			if (backgroundColor == null)
				return Color.BLACK;
			return backgroundColor;
		}

		float getSpeedCoefficient() {
			return speedCoefficient;
		}


		/**
		 * Create new dialog fragment with loading animation.
		 * @return created dialog fragment.
		 */
		public AnimationDialogFragment build() {
			if (speedCoefficient < 0) {
				throw new IllegalArgumentException("Speed coefficient must be positive.");
			}
			if (speedCoefficient == 0) {
				speedCoefficient = Constants.DEFAULT_SPEED_COEFFICIENT;
			}
			return new AnimationDialogFragment(this);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeValue(this.firstColor);
			dest.writeValue(this.secondColor);
			dest.writeValue(this.thirdColor);
			dest.writeValue(this.fourthColor);
			dest.writeValue(this.backgroundColor);
			dest.writeFloat(this.speedCoefficient);
		}

		public Builder() {

		}

		protected Builder(Parcel in) {
			this.firstColor = (Integer) in.readValue(Integer.class.getClassLoader());
			this.secondColor = (Integer) in.readValue(Integer.class.getClassLoader());
			this.thirdColor = (Integer) in.readValue(Integer.class.getClassLoader());
			this.fourthColor = (Integer) in.readValue(Integer.class.getClassLoader());
			this.backgroundColor = (Integer) in.readValue(Integer.class.getClassLoader());
			this.speedCoefficient = in.readFloat();
		}

		public static final Creator<Builder> CREATOR = new Creator<Builder>() {
			public Builder createFromParcel(Parcel source) {
				return new Builder(source);
			}

			public Builder[] newArray(int size) {
				return new Builder[size];
			}
		};
	}
}
