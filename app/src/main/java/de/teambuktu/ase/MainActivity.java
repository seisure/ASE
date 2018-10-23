package de.teambuktu.ase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    ArrayList<Action> actionList = new ArrayList<>();
    ArrayList<Condition> conditionList = new ArrayList<>();

    private void addRowToUI(Action actionToAdd) {
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

    private void addRowToUI(final Condition conditionToAdd) {
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
            addRowToUI(action);
        }

        ArrayList<Condition> conditions = storageHelper.loadConditions();
        this.conditionList = conditions;
        for (Condition condition : conditions) {
            addRowToUI(condition);
        }

        if (this.actionList.size() == 0 && this.conditionList.size() == 0) {
            Intent initialIntent = new Intent(this, InitialActivity.class);

            initialIntent.putExtra("rules", getRuleCount());
            startActivityForResult(initialIntent, 1);
        }
    }

    private int getRuleCount() {
        int rules = 0;
        if (!conditionList.isEmpty()) {
            rules = conditionList.get(0).rules.size();
        } else if (!actionList.isEmpty()) {
            rules = actionList.get(0).rules.size();
        }
        return rules;
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
                Action action = new Action(getRuleCount());
                actionList.add(action);
                storageHelper.update(actionList, conditionList);
                addRowToUI(action);
                return true;
            case R.id.buttonAddConditionRow:
                Condition condition = new Condition(getRuleCount());
                conditionList.add(condition);
                storageHelper.update(actionList, conditionList);
                addRowToUI(condition);
                return true;
            case R.id.buttonAddRuleColumn:
                if (!conditionList.isEmpty()) {
                    setNumberOfRules(conditionList.get(0).rules.size() + 1);
                } else if (!actionList.isEmpty()) {
                    setNumberOfRules(actionList.get(0).rules.size() + 1);
                }
                storageHelper.update(actionList, conditionList);
                return true;
            case R.id.buttonRuleCount:
                setNumberOfRulesDialog();
                return true;
            case R.id.buttonCreatenitialTable:
                Intent initialIntent = new Intent(this, InitialActivity.class);
                initialIntent.putExtra("conditions", conditionList.size());
                initialIntent.putExtra("actions", actionList.size());
                initialIntent.putExtra("rules", getRuleCount());
                startActivityForResult(initialIntent, 1);
                return true;
            case R.id.buttonClearTable:
                clearTable();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            int conditions = data.getIntExtra("conditions", 0);
            int actions = data.getIntExtra("actions", 0);
            int rules = data.getIntExtra("rules", 0);

            setNumberOfRules(rules);

            if (conditions > conditionList.size()) {
                for (int i = 0; i <= conditions - conditionList.size(); i++) {
                    Condition condition = new Condition(rules);
                    conditionList.add(condition);
                    addRowToUI(condition);
                }
            } else if (conditions < conditionList.size()) {
                for (int i = conditionList.size() - 1; i > conditions - 1; i--) {
                    conditionList.remove(i);
                    TableLayout tableLayout = findViewById(R.id.tableCondition);
                    tableLayout.removeViewAt(i);
                }
            }

            if (actions > actionList.size()) {
                for (int i = 0; i <= actions - actionList.size(); i++) {
                    Action action = new Action(rules);
                    actionList.add(action);
                    addRowToUI(action);
                }
            } else if (actions < actionList.size()) {
                for (int i = actionList.size() - 1; i > actions - 1; i--) {
                    actionList.remove(i);
                    TableLayout tableLayout = findViewById(R.id.tableAction);
                    tableLayout.removeViewAt(i);
                }
            }

            StorageHelper storageHelper = new StorageHelper(this.getApplicationContext());
            storageHelper.update(actionList, conditionList);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setNumberOfRules(int count) {
        TableLayout actionTable = findViewById(R.id.tableAction);
        actionTable.removeAllViews();
        for (Action action : actionList) {
            action.setNumberOfRules(count);
            addRowToUI(action);
        }

        TableLayout conditionTable = findViewById(R.id.tableCondition);
        conditionTable.removeAllViews();
        for (Condition condition : conditionList) {
            condition.setNumberOfRules(count);
            addRowToUI(condition);
        }
    }

    private void setNumberOfRulesDialog() {
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
                int ruleCount = Integer.parseInt(text);
                setNumberOfRules(ruleCount);

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
