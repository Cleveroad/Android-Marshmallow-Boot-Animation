package com.cleveroad.example.androidmanimation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cleveroad.androidmanimation.AnimationDialogFragment;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_activity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, AnimationActivity.class));
			}
		});
		findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AnimationDialogFragment fragment = new AnimationDialogFragment.Builder()
						.setBackgroundColor(Color.WHITE)
						.setFirstColor(getResources().getColor(R.color.google_red))
						.setSecondColor(getResources().getColor(R.color.google_green))
						.setThirdColor(getResources().getColor(R.color.google_blue))
						.setFourthColor(getResources().getColor(R.color.google_yellow))
						.setSpeedCoefficient(1.0f)
						.build();
				fragment.show(getSupportFragmentManager(), "Animation");
			}
		});
	}
}
