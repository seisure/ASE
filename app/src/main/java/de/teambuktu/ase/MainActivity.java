package de.teambuktu.ase;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Action> actionList = new ArrayList<>();
    ArrayList<Condition> conditionList = new ArrayList<>();
    private static final int REQUEST_EDIT_TABLE = 0;
    private static final int REQUEST_EXPORT_CSV = 1;
    private static final int REQUEST_IMPORT_CSV = 2;

    private void addRowToUI(final Action actionToAdd) {
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

        columnText.setText(actionToAdd.getTitle());
        columnText.setHint(R.string.action);
        columnText.setEms(6);
        columnText.setSingleLine();
        columnText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int index = actionList.indexOf(actionToAdd);
                actionList.get(index).setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                StorageHelper storageHelper = new StorageHelper(getApplicationContext());
                storageHelper.update(actionList, conditionList);
            }
        });

        CheckBox actionRule;
        for (int i = 0; i < actionToAdd.rules.size(); i++) {
            final int ruleIndex = i;
            actionRule = new CheckBox(this);
            row.addView(actionRule);
            actionRule.setChecked(actionToAdd.rules.get(i));
            actionRule.setEms(2);

            actionRule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fnOnClickActionRule(v, actionToAdd, ruleIndex);
                }
            });
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
                fnOnClickButtonDeleteRow(actionList, v, context, row);
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

        columnText.setText(conditionToAdd.getTitle());
        columnText.setHint(R.string.condition);
        columnText.setEms(6);
        columnText.setSingleLine();
        columnText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int conditionIndex = conditionList.indexOf(conditionToAdd);
                conditionList.get(conditionIndex).setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                StorageHelper storageHelper = new StorageHelper(getApplicationContext());
                storageHelper.update(actionList, conditionList);
            }
        });

        TextView conditionRule;
        for (int i = 0; i < conditionToAdd.rules.size(); i++) {
            final int ruleIndex = i;
            conditionRule = new TextView(this);
            conditionRule.setText(conditionToAdd.rules.get(i));
            conditionRule.setEms(2);
            conditionRule.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            conditionRule.setClickable(true);
            conditionRule.setFocusable(true);
            conditionRule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fnOnClickConditionRule(v, conditionToAdd, ruleIndex);
                }
            });
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
                fnOnClickButtonDeleteRow(conditionList, v, context, row);
            }
        });
    }

    private void removeFromTableLayout(View viewToRemove, int tableId) {
        TableLayout table = findViewById(tableId);
        table.removeView(viewToRemove);
    }

    private void clearTableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.clearTableAskFor);
        final Context context = this.getApplicationContext();

        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fnOnClickPositiveButtonClearTable(context);
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
            startActivityForResult(initialIntent, REQUEST_EDIT_TABLE);
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
            case R.id.buttonCreateInitialTable:
                Intent initialIntent = new Intent(this, InitialActivity.class);
                initialIntent.putExtra("conditions", conditionList.size());
                initialIntent.putExtra("actions", actionList.size());
                initialIntent.putExtra("rules", getRuleCount());
                startActivityForResult(initialIntent, REQUEST_EDIT_TABLE);
                return true;
            case R.id.buttonClearTable:
                clearTableDialog();
                return true;
            case R.id.buttonExportCSV:
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File csvContent = new StorageHelper(getApplicationContext()).exportToCSV(conditionList, actionList);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/csv");
                    Uri fileUri = FileProvider.getUriForFile(this, "com.myfileprovider", csvContent);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    this.startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
                }

                return true;
            case R.id.buttonImportCSV:
                if (conditionList.isEmpty() && actionList.isEmpty()) {
                    importTableEntriesFromCSV();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.importCsv);
                    builder.setMessage(R.string.importCsvAskFor);

                    builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            importTableEntriesFromCSV();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_TABLE:
                if (resultCode == RESULT_OK) {
                    int conditions = data.getIntExtra("conditions", 0);
                    int actions = data.getIntExtra("actions", 0);
                    int rules = data.getIntExtra("rules", 0);

                    setNumberOfRules(rules);

                    int conditionListCount = conditionList.size();
                    int actionListCount = actionList.size();

                    if (conditions > conditionListCount) {
                        for (int i = 0; i < conditions - conditionListCount; i++) {
                            Condition condition = new Condition(rules);
                            conditionList.add(condition);
                            addRowToUI(condition);
                        }
                    } else if (conditions < conditionListCount) {
                        for (int i = conditionListCount - 1; i > conditions - 1; i--) {
                            conditionList.remove(i);
                            TableLayout tableLayout = findViewById(R.id.tableCondition);
                            tableLayout.removeViewAt(i);
                        }
                    }

                    if (actions > actionListCount) {
                        for (int i = 0; i < actions - actionListCount; i++) {
                            Action action = new Action(rules);
                            actionList.add(action);
                            addRowToUI(action);
                        }
                    } else if (actions < actionListCount) {
                        for (int i = actionListCount - 1; i > actions - 1; i--) {
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

    private void importTableEntriesFromCSV() {
        Context context = getApplicationContext();
        StorageHelper storageHelper = new StorageHelper(context);
        String csvString = storageHelper.loadFromCSV(context);
        String[] csv = csvString.split("\n");
        int rules = csv[0].split(";").length - 2;

        conditionList = new ArrayList<Condition>();
        actionList = new ArrayList<Action>();
        clearUITable();

        for (String line : csv) {
            String[] lineParts = line.split(";");
            if (lineParts[0].equals("C")) {
                Condition newCondition = new Condition(rules);
                newCondition.setTitle(lineParts[1]);
                int j = 0;
                for (int i = 2; i < lineParts.length; i++) {
                    newCondition.rules.set(j++, lineParts[i]);
                }
                conditionList.add(newCondition);
                addRowToUI(newCondition);
            }
            else if (lineParts[0].equals("A")) {
                Action newAction = new Action(rules);
                newAction.setTitle(lineParts[1]);
                int j = 0;
                for (int i = 2; i < lineParts.length; i++) {
                    newAction.rules.set(j++, lineParts[i].equals("1"));
                }
                actionList.add(newAction);
                addRowToUI(newAction);
            }
        }
    }

    private void fnOnClickActionRule (View v, Action actionToAdd, int ruleIndex) {
        final int index = actionList.indexOf(actionToAdd);
        if (((CheckBox) v).isChecked()) {
            actionList.get(index).rules.set(ruleIndex, true);
        }
        else {
            actionList.get(index).rules.set(ruleIndex, false);
        }
        StorageHelper storageHelper = new StorageHelper(getApplicationContext());
        storageHelper.update(actionList, conditionList);
    }
    
    private void fnOnClickConditionRule (View v, Condition conditionToAdd, int ruleIndex) {
        String currentText = (String) ((TextView) v).getText();
        int conditionIndex = conditionList.indexOf(conditionToAdd);
        if (currentText.equals("-")) {
            ((TextView) v).setText("J");
            conditionList.get(conditionIndex).rules.set(ruleIndex, "J");
        } else if (currentText.equals("J")) {
            ((TextView) v).setText("N");
            conditionList.get(conditionIndex).rules.set(ruleIndex, "N");
        } else if (currentText.equals("N")) {
            ((TextView) v).setText("-");
            conditionList.get(conditionIndex).rules.set(ruleIndex, "-");
        }
        StorageHelper storageHelper = new StorageHelper(getApplicationContext());
        storageHelper.update(actionList, conditionList);
    }

    private void fnOnClickButtonDeleteRow (Object list, View v, Context context, TableRow row) {
        ImageButton button = (ImageButton) v;
        if (((ArrayList<?>)list).get(0) instanceof Action) {
            actionList.remove(button.getTag());
            removeFromTableLayout(row, R.id.tableAction);
        } else if (((ArrayList<?>)list).get(0) instanceof Condition) {
            conditionList.remove(button.getTag());
            removeFromTableLayout(row, R.id.tableCondition);
        }
        updateStorage(context);
    }

    private void clearUITable() {
        TableLayout conditionTable = findViewById(R.id.tableCondition);
        conditionTable.removeAllViews();
        TableLayout actionTable = findViewById(R.id.tableAction);
        actionTable.removeAllViews();
    }

    private void fnOnClickPositiveButtonClearTable (Context context) {
        clearUITable();
        conditionList.clear();
        actionList.clear();
        updateStorage(context);
        Toast.makeText(MainActivity.this, R.string.clearTableSuccess, Toast.LENGTH_SHORT).show();
    }

    private void updateStorage (Context context) {
        StorageHelper storageHelper = new StorageHelper(context);
        storageHelper.update(actionList, conditionList);
    }

    private void setNumberOfRules(int count) {
        clearUITable();

        for (Action action : actionList) {
            action.setNumberOfRules(count);
            addRowToUI(action);
        }

        for (Condition condition : conditionList) {
            condition.setNumberOfRules(count);
            addRowToUI(condition);
        }
    }

    @Override
    protected void onDestroy() {
        File toDelete = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ASE.csv");
        if (toDelete.exists())
            toDelete.delete();
        super.onDestroy();
    }
}
