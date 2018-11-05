package de.teambuktu.ase;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InitialActivity extends AppCompatActivity implements View.OnClickListener {
    int conditionsIn;
    int actionsIn;
    int rulesIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = this.getIntent();
        conditionsIn = intent.getIntExtra("conditions", 0);
        actionsIn = intent.getIntExtra("actions", 0);
        rulesIn = intent.getIntExtra("rules", 0);
        EditText conditionsText = findViewById(R.id.editTextConditionsToAdd);
        EditText actionsText = findViewById(R.id.editTextActionsToAdd);
        EditText rulesText = findViewById(R.id.editTextRulesToAdd);
        conditionsText.setText(String.valueOf(conditionsIn));
        actionsText.setText(String.valueOf(actionsIn));
        rulesText.setText(String.valueOf(rulesIn));

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
                int conditionsOut = Integer.parseInt(editText.getText().toString());
                editText = findViewById(R.id.editTextActionsToAdd);
                int actionsOut = Integer.parseInt(editText.getText().toString());
                editText = findViewById(R.id.editTextRulesToAdd);
                int rulesOut = Integer.parseInt(editText.getText().toString());

                if (conditionsOut < conditionsIn || actionsOut < actionsIn || rulesOut < rulesIn) {

                    String ausgabe = getString(R.string.warningForDelete);

                    if (conditionsOut < conditionsIn) {
                        ausgabe = ausgabe.concat(getString(R.string.conditions) + "\n");
                    }
                    if (actionsOut < actionsIn) {
                        ausgabe = ausgabe.concat(getString(R.string.actions) + "\n");
                    }
                    if (rulesOut < rulesIn) {
                        ausgabe = ausgabe.concat(getString(R.string.rules) + "\n");
                    }
                    ausgabe = ausgabe.concat(getString(R.string.continueWarning));

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.warning));
                    builder.setMessage(ausgabe);

                    final Intent tempIntent = intent;
                    final int tempConditionsOut = conditionsOut;
                    final int tempActionsOut = actionsOut;
                    final int tempRulesOut = rulesOut;
                    builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tempIntent.putExtra("conditions", tempConditionsOut);
                            tempIntent.putExtra("actions", tempActionsOut);
                            tempIntent.putExtra("rules", tempRulesOut);
                            setResult(RESULT_OK, tempIntent);
                            finish();
                        }
                    });

                    builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else {
                    intent.putExtra("conditions", conditionsOut);
                    intent.putExtra("actions", actionsOut);
                    intent.putExtra("rules", rulesOut);
                    setResult(RESULT_OK, intent);
                    finish();
                }
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
