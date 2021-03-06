package de.teambuktu.ase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AppInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.appInfoHeading);
        }

        setTextViewLinkClickable(R.id.appInfoProjectLocation);
        setCurrentVersionInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setTextViewLinkClickable(int textviewId) {
        TextView textView = findViewById(textviewId);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setCurrentVersionInfo() {
        String versionCode = BuildConfig.VERSION_NAME;
        TextView textView = findViewById(R.id.appInfoVersionNumber);
        textView.setText("Version " + versionCode);
    }

    public void onClickAppInfoHowTo(View v) {
        Intent howToIntent = new Intent(this, HowToActivity.class);
        howToIntent.putExtra("isInitial", true);
        startActivity(howToIntent);
    }
}
