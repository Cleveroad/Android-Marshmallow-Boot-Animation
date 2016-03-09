package com.cleveroad.example.androidmanimation;

import android.content.Intent;
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
						.build();
				fragment.show(getSupportFragmentManager(), "Animation");
			}
		});
	}
}
