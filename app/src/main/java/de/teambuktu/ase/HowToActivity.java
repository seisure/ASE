package de.teambuktu.ase;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HowToActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        boolean isInitial = getIntent().getExtras().getBoolean("isInitial");
        if (isInitial) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(R.string.howToTitle);
            }
            Button button = findViewById(R.id.buttonFinishAndStart);
            TextView textView = findViewById(R.id.textViewTableInfo);
            button.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
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

    public void finishHowTo(View v) {
        StorageHelper storageHelper = new StorageHelper(getApplicationContext());
        storageHelper.setInitialStartupFlag(false);
        finish();
    }
}
