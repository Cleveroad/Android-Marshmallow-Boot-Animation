package com.cleveroad.example.androidmanimation;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.cleveroad.androidmanimation.LoadingAnimationView;

/**
 * Example of using animation as view in dialog.
 */
public class AnimationDialogFragment extends DialogFragment {

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new LoadingAnimationView
				.Builder(getContext())
				.setBackgroundColor(Color.WHITE)
				.build();
	}
}
