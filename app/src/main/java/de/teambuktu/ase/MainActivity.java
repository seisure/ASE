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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int actionCount = 0;
    int conditionCount = 0;
    int ruleCount = 3;
    ArrayList<Action> actionList = new ArrayList<>();
    ArrayList<Condition> conditionList = new ArrayList<>();

    private void addRow(Action actionToAdd) {
        TableLayout table;
        table = findViewById(R.id.tableAction);

        TableRow row = new TableRow(this);
        row.setTag(actionToAdd.ID);
        TextView columnID = new TextView(this);
        EditText columnText = new EditText(this);

        table.addView(row);
        row.addView(columnID);
        row.addView(columnText);

        columnID.setText(actionToAdd.ID);
        columnID.setEms(2);
        columnID.setIncludeFontPadding(true);
        columnID.setPadding(10,0,0,0);

        columnText.setHint("Aktion");
        columnText.setEms(6);

        CheckBox actionRule;
        for (int i = 1; i < actionToAdd.rules.size(); i++) {
            actionRule = new CheckBox(this);
            actionRule.setTag(actionToAdd.ID + "-R" + i);
            actionRule.setChecked(actionToAdd.rules.get(i));
            actionRule.setEms(2);
            row.addView(actionRule);
        }
    }

    private void addRow(Condition conditionToAdd) {
        TableLayout table;
        table = findViewById(R.id.tableCondition);

        TableRow row = new TableRow(this);
        row.setTag(conditionToAdd.ID);
        TextView columnID = new TextView(this);
        EditText columnText = new EditText(this);

        table.addView(row);
        row.addView(columnID);
        row.addView(columnText);

        columnID.setText(conditionToAdd.ID);
        columnID.setEms(2);
        columnID.setIncludeFontPadding(true);
        columnID.setPadding(10,0,0,0);

        columnText.setHint("Bedingung");
        columnText.setEms(6);

        TextView conditionRule;
        for (int i = 1; i < conditionToAdd.rules.size(); i++) {
            conditionRule = new TextView(this);
            conditionRule.setTag(conditionToAdd.ID + "-R" + i);
            conditionRule.setText(conditionToAdd.rules.get(i));
            conditionRule.setEms(2);
            conditionRule.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            conditionRule.setClickable(true);
            conditionRule.setFocusable(true);
            row.addView(conditionRule);
        }
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
                Action action = new Action("A" + actionCount++, ruleCount);
                actionList.add(action);
                addRow(action);
                return true;
            case R.id.buttonAddConditionRow:
                Condition condition = new Condition("B" + conditionCount++, ruleCount);
                conditionList.add(condition);
                addRow(condition);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
