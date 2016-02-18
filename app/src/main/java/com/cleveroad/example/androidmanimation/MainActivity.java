package com.cleveroad.example.androidmanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cleveroad.androidmanimation.LoadingAnimation;

public class MainActivity extends AppCompatActivity {

	private LoadingAnimation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		animation = (LoadingAnimation) findViewById(R.id.animation);
		animation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (animation.getState() == LoadingAnimation.STATE_PAUSED || animation.getState() == LoadingAnimation.STATE_STOPPED) {
					animation.startAnimation();
				} else {
					animation.pauseAnimation();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		animation.startAnimation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		animation.pauseAnimation();
	}

	@Override
	protected void onDestroy() {
		animation.stopAnimation();
		super.onDestroy();
	}
}
