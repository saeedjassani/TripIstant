package com.technovanzahackathon.tripistant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

public class PhotosActivity extends AppCompatActivity implements View.OnClickListener {

	private static final int TAKE_PHOTO_CODE = 100;
	private static final String CAPTURE_TITLE = "HI";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);

		FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fab);
		button.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	private Uri getImageUri() {
		// Store image in dcim
		File file = new File(Environment.getExternalStorageDirectory() + "/TripIstant", CAPTURE_TITLE);
		return Uri.fromFile(file);
	}
}
