package com.xingress.ximage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private ToggleButton toggleButton;
    private String xstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences sharedPreferences = getSharedPreferences("ximage", 1);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean ximage = sharedPreferences.getBoolean("ximage", false);
        toggleButton = (ToggleButton) findViewById(R.id.ximage);
        toggleButton.setChecked(ximage);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    xstatus = getString(R.string.close);
                } else {
                    xstatus = getString(R.string.open);
                }
                Snackbar.make(buttonView, getString(R.string.message) + xstatus, Snackbar.LENGTH_LONG)
                        .show();
                editor.putBoolean("ximage", isChecked);
                editor.commit();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
}
