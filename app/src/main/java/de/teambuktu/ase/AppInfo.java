package de.teambuktu.ase;

import android.content.Context;
import android.content.DialogInterface;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.howToTitle);
        builder.setMessage(R.string.howToText);
        final Context context = this.getApplicationContext();

        builder.setPositiveButton(R.string.howToAccept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StorageHelper storageHelper = new StorageHelper(context);
                storageHelper.setInitialStartupFlag(false);
            }
        });

        builder.show();
    }
}
