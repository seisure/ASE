package de.teambuktu.ase;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InitialActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = this.getIntent();
        int conditions = intent.getIntExtra("conditions", 0);
        int actions = intent.getIntExtra("actions", 0);
        int rules = intent.getIntExtra("rules", 0);
        EditText conditionsText = findViewById(R.id.editTextConditionsToAdd);
        EditText actionsText = findViewById(R.id.editTextActionsToAdd);
        EditText rulesText = findViewById(R.id.editTextRulesToAdd);
        conditionsText.setText(String.valueOf(conditions));
        actionsText.setText(String.valueOf(actions));
        rulesText.setText(String.valueOf(rules));

        Button clickButton1 = findViewById(R.id.buttonCplus);
        clickButton1.setOnClickListener(this);
        Button clickButton2 = findViewById(R.id.buttonCminus);
        clickButton2.setOnClickListener(this);
        Button clickButton3 = findViewById(R.id.buttonAplus);
        clickButton3.setOnClickListener(this);
        Button clickButton4 = findViewById(R.id.buttonAminus);
        clickButton4.setOnClickListener(this);
        Button clickButton5 = findViewById(R.id.buttonRplus);
        clickButton5.setOnClickListener(this);
        Button clickButton6 = findViewById(R.id.buttonRminus);
        clickButton6.setOnClickListener(this);
        Button clickButton7 = findViewById(R.id.buttonCreate);
        clickButton7.setOnClickListener(this);
    }

    private void changeNum(int view, Boolean plus) {
        EditText editText = findViewById(view);
        int num = Integer.parseInt(editText.getText().toString());
        if (plus) {
            editText.setText(String.valueOf(num + 1));
        } else if (!plus && num > 0) {
            editText.setText(String.valueOf(num - 1));
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCplus: {
                changeNum(R.id.editTextConditionsToAdd, true);
                break;
            }
            case R.id.buttonCminus: {
                changeNum(R.id.editTextConditionsToAdd, false);
                break;
            }
            case R.id.buttonAplus: {
                changeNum(R.id.editTextActionsToAdd, true);
                break;
            }
            case R.id.buttonAminus: {
                changeNum(R.id.editTextActionsToAdd, false);
                break;
            }
            case R.id.buttonRplus: {
                changeNum(R.id.editTextRulesToAdd, true);
                break;
            }
            case R.id.buttonRminus: {
                changeNum(R.id.editTextRulesToAdd, false);
                break;
            }
            case R.id.buttonCreate: {
                Intent intent = new Intent(this, InitialActivity.class);
                EditText editText = findViewById(R.id.editTextConditionsToAdd);
                int cons = Integer.parseInt(editText.getText().toString());
                editText = findViewById(R.id.editTextActionsToAdd);
                int acts = Integer.parseInt(editText.getText().toString());
                editText = findViewById(R.id.editTextRulesToAdd);
                int rules = Integer.parseInt(editText.getText().toString());
                intent.putExtra("conditions", cons);
                intent.putExtra("actions", acts);
                intent.putExtra("rules", rules);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case android.R.id.home : {
                setResult(RESULT_CANCELED);
                finish();
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
