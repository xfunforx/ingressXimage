package com.xingress.ximage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
	public static final String TAG = "IngressXimage";

	public static final String INGRESSPACKAGENAME = "com.nianticproject.ingress";
	public static final String SCANNERREDACTEDPACKAGENAME = "com.nianticlabs.ingress.prime.qa";
	public static final String XIMAGE = "ximage";

	private String xstatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final SharedPreferences sharedPreferences = getSharedPreferences(XIMAGE, Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = sharedPreferences.edit();
		final Resources res = getResources();

		final boolean ximage = sharedPreferences.getBoolean(XIMAGE, false);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String buildDate = dateFormat.format(new java.util.Date(BuildConfig.TIMESTAMP));

		String text = res.getString(R.string.app_name) + " " + String.format(res.getString(R.string.credits_version), BuildConfig.VERSION_NAME, buildDate);

		TextView tv_credits = findViewById(R.id.tv_credits);
		tv_credits.setText(text);

		ToggleButton toggleButton = findViewById(R.id.ximage);
		toggleButton.setChecked(ximage);
		toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					xstatus = getString(R.string.close);
				} else {
					xstatus = getString(R.string.open);
				}

				Snackbar.make(buttonView, getString(R.string.message) + xstatus, Snackbar.LENGTH_LONG).show();

				editor.putBoolean(XIMAGE, isChecked).commit();
			}
		});

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				String[] recipients = new String[]{"xfunforx@gmail.com"};
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "hi, xfunforx this is about ximage");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "i need for some help.");
				emailIntent.setType("text/plain");
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();

		fixPermissions();
	}

	private void fixPermissions() {
		boolean dataFolderExists = false;
		boolean prefFolderExists = false;
		boolean prefFileExists = false;

		boolean dataFolderReadable = false;
		boolean dataFolderExecutable = false;
		boolean prefFolderReadable = false;
		boolean prefFolderExecutable = false;
		boolean prefFileReadable = false;

		File dataFolder = new File(this.getApplicationInfo().dataDir);

		if (dataFolder.exists()) { // this should be true, as soon as the app has been started
			dataFolderExists = true;
			dataFolderReadable = dataFolder.setReadable(true, false);
			dataFolderExecutable = dataFolder.setExecutable(true, false);

			File sharedPrefsFolder = new File(this.getApplicationInfo().dataDir + "/shared_prefs");
			//Log.d(TAG, "sharedPrefsFolder: " + sharedPrefsFolder.toString());

			if (sharedPrefsFolder.exists()) {
				prefFolderExists = true;
				prefFolderReadable = sharedPrefsFolder.setReadable(true, false);
				prefFolderExecutable = sharedPrefsFolder.setExecutable(true, false);

				File prefFile = new File(sharedPrefsFolder.getAbsolutePath() + "/" + XIMAGE + ".xml");

				if (prefFile.exists()) {
					prefFileExists = true;
					prefFileReadable = prefFile.setReadable(true, false);
				} else {
					Log.e(TAG, "preferences file doesn't exist");
				}
			} else {
				Log.e(TAG, "preferences folder doesn't exist");
			}
		} else {
			Log.e(TAG, "data folder doesn't exist");
		}

		if (dataFolderExists && !dataFolderReadable) {
			Log.e(TAG, "data folder set readable FAILED");
		}

		if (dataFolderExists && !dataFolderExecutable) {
			Log.e(TAG, "data folder set executable FAILED");
		}

		if (prefFolderExists && !prefFolderReadable) {
			Log.e(TAG, "preferences folder set readable FAILED");
		}

		if (prefFolderExists && !prefFolderExecutable) {
			Log.e(TAG, "preferences folder set executable FAILED");
		}

		if (prefFileExists && !prefFileReadable) {
			Log.e(TAG, "preferences file set readable FAILED");
		}
	}
}
