package de.teambuktu.ase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int actionCount = 0;
    int conditionCount = 0;
    int ruleCount = 3;

    private void addRow(boolean action) {
        TableLayout table;
        if (action) {
            table = findViewById(R.id.tableAction);
        }
        else {
            table = findViewById(R.id.tableCondition);
        }

        TableRow row = new TableRow(this);
        TextView column1 = new TextView(this);
        EditText column2 = new EditText(this);

        if (action) {
            row.setTag("A" + actionCount++);
            column1.setText("A" + actionCount);
        }
        else {
            row.setTag("C" + conditionCount++);
            column1.setText("B" + conditionCount);
        }
        column1.setEms(2);

        column1.setIncludeFontPadding(true);
        column1.setPadding(10,0,0,0);
        row.addView(column1);

        column2.setHint(action ? "Aktion" : "Bedingung");
        column2.setEms(6);
        row.addView(column2);

        if (action) {
            CheckBox actionRule;
            for (int i = 1; i <= ruleCount; i++) {
                actionRule = new CheckBox(this);
                actionRule.setTag("A" + (conditionCount - 1) + "-R" + i);
                actionRule.setEms(2);
                row.addView(actionRule);
            }
        }
        else {
            TextView conditionRule;
            for (int i = 1; i <= ruleCount; i++) {
                conditionRule = new TextView(this);
                conditionRule.setTag("C" + (conditionCount - 1) + "-R" + i);
                conditionRule.setText("-");
                conditionRule.setEms(2);
                conditionRule.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                conditionRule.setClickable(true);
                conditionRule.setFocusable(true);
                row.addView(conditionRule);
            }
        }

        table.addView(row);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buttonAddActionRow:
                addRow(true);
                return true;
            case R.id.buttonAddConditionRow:
                addRow(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
