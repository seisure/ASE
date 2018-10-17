package de.teambuktu.ase;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int ruleCount = 3;
    ArrayList<Action> actionList = new ArrayList<>();
    ArrayList<Condition> conditionList = new ArrayList<>();

    private void addRow(Action actionToAdd) {
        TableLayout table;
        table = findViewById(R.id.tableAction);

        final TableRow row = new TableRow(this);
        TextView columnID = new TextView(this);
        EditText columnText = new EditText(this);

        table.addView(row);
        row.addView(columnID);
        row.addView(columnText);

        columnID.setText("A" + actionList.indexOf(actionToAdd));
        columnID.setEms(2);
        columnID.setIncludeFontPadding(true);
        columnID.setPadding(10, 0, 0, 0);

        columnText.setHint("Aktion");
        columnText.setEms(6);

        CheckBox actionRule;
        for (int i = 0; i < actionToAdd.rules.size(); i++) {
            actionRule = new CheckBox(this);
            actionRule.setChecked(actionToAdd.rules.get(i));
            actionRule.setEms(2);
            row.addView(actionRule);
        }

        final Context context = this.getApplicationContext();
        final ImageButton buttonDelete = new ImageButton(this);
        buttonDelete.setImageResource(R.drawable.close);
        buttonDelete.setImageAlpha(150);
        buttonDelete.setPadding(20,0,0,0);
        buttonDelete.setForegroundGravity(Gravity.CENTER);
        row.setVerticalGravity(Gravity.CENTER_VERTICAL);
        buttonDelete.setBackground(null);
        buttonDelete.setTag(actionToAdd);
        row.addView(buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                actionList.remove(button.getTag());
                StorageHelper storageHelper = new StorageHelper(context);
                storageHelper.update(actionList, conditionList);
                removeFromTableLayout(row, R.id.tableAction);
            }
        });
    }

    private void addRow(final Condition conditionToAdd) {
        TableLayout table;
        table = findViewById(R.id.tableCondition);

        final TableRow row = new TableRow(this);
        TextView columnID = new TextView(this);
        EditText columnText = new EditText(this);

        table.addView(row);
        row.addView(columnID);
        row.addView(columnText);

        columnID.setText("B" + conditionList.indexOf(conditionToAdd));
        columnID.setEms(2);
        columnID.setIncludeFontPadding(true);
        columnID.setPadding(10, 0, 0, 0);

        columnText.setHint("Bedingung");
        columnText.setEms(6);

        TextView conditionRule;
        for (int i = 0; i < conditionToAdd.rules.size(); i++) {
            conditionRule = new TextView(this);
            conditionRule.setText(conditionToAdd.rules.get(i));
            conditionRule.setEms(2);
            conditionRule.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            conditionRule.setClickable(true);
            conditionRule.setFocusable(true);
            row.addView(conditionRule);
        }

        final Context context = this.getApplicationContext();
        final ImageButton buttonDelete = new ImageButton(this);
        buttonDelete.setImageResource(R.drawable.close);
        buttonDelete.setImageAlpha(150);
        buttonDelete.setPadding(20,0,0,0);
        buttonDelete.setForegroundGravity(Gravity.CENTER);
        row.setVerticalGravity(Gravity.CENTER_VERTICAL);
        buttonDelete.setBackground(null);
        buttonDelete.setTag(conditionToAdd);
        row.addView(buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                conditionList.remove(button.getTag());
                StorageHelper storageHelper = new StorageHelper(context);
                storageHelper.update(actionList, conditionList);
                removeFromTableLayout(row, R.id.tableCondition);
            }
        });
    }

    private void removeFromTableLayout(View viewToRemove, int tableId) {
        TableLayout table = findViewById(tableId);
        table.removeView(viewToRemove);
    }

    private void clearTable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.clearTableAskFor);
        final Context context = this.getApplicationContext();

        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TableLayout conditionTable = findViewById(R.id.tableCondition);
                conditionTable.removeAllViews();
                TableLayout actionTable = findViewById(R.id.tableAction);
                actionTable.removeAllViews();
                conditionList.clear();
                actionList.clear();
                StorageHelper storageHelper = new StorageHelper(context);
                storageHelper.update(actionList, conditionList);
                Toast.makeText(MainActivity.this, R.string.clearTableSuccess, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StorageHelper storageHelper = new StorageHelper(this.getApplicationContext());
        ArrayList<Action> actions = storageHelper.loadActions();
        this.actionList = actions;
        for (Action action : actions) {
            addRow(action);
        }

        ArrayList<Condition> conditions = storageHelper.loadConditions();
        this.conditionList = conditions;
        for (Condition condition : conditions) {
            addRow(condition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        StorageHelper storageHelper = new StorageHelper(this.getApplicationContext());
        switch (item.getItemId()) {
            case R.id.buttonAddActionRow:
                Action action = new Action(ruleCount);
                actionList.add(action);
                storageHelper.update(actionList, conditionList);
                addRow(action);
                return true;
            case R.id.buttonAddConditionRow:
                Condition condition = new Condition(ruleCount);
                conditionList.add(condition);
                storageHelper.update(actionList, conditionList);
                addRow(condition);
                return true;
            case R.id.buttonRuleCount:
                setNumberOfRules();
                return true;
            case R.id.buttonClearTable:
                clearTable();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setNumberOfRules() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.ruleCount);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        final Context context = this.getApplicationContext();
        builder.setPositiveButton("Anpassen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                ruleCount = Integer.parseInt(text);

                TableLayout actionTable = findViewById(R.id.tableAction);
                actionTable.removeAllViews();
                for (Action action : actionList) {
                    action.setNumberOfRules(ruleCount);
                    addRow(action);
                }

                TableLayout conditionTable = findViewById(R.id.tableCondition);
                conditionTable.removeAllViews();
                for (Condition condition : conditionList) {
                    condition.setNumberOfRules(ruleCount);
                    addRow(condition);
                }

                StorageHelper storageHelper = new StorageHelper(context);
                storageHelper.update(actionList, conditionList);
                Toast.makeText(MainActivity.this, "Anzahl der Regeln auf " + text + " geändert", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
