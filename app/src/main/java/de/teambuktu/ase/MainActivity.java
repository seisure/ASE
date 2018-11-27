package de.teambuktu.ase;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Action> actionList = new ArrayList<>();
    ArrayList<Condition> conditionList = new ArrayList<>();
    private static final int REQUEST_EDIT_TABLE = 0;
    private static final int REQUEST_EXPORT_CSV = 1;
    private static final int REQUEST_IMPORT_CSV = 2;
    public static final int RESULT_IMPORT = 2;

    private void addRowToUI(final Action actionToAdd) {
        setTableVisible(R.id.tableHeader, true);
        
        TableLayout table;
        table = findViewById(R.id.tableAction);

        final TableRow row = new TableRow(this);
        final TextView columnID = new TextView(this);
        final EditText columnText = new EditText(this);

        table.addView(row);
        row.addView(columnID);
        row.addView(columnText);

        columnID.setText(getString(R.string.prefixActionRow) + actionList.indexOf(actionToAdd));
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
                if (count != 0 && s.charAt(start) == ';') {
                    String replace = s.toString().replace(";", "");
                    columnText.setText(replace);
                    columnText.setError(getString(R.string.noSemicolons));
                }
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
            actionRule.setChecked(actionToAdd.rules.get(i).getRuleActionValue());
            actionRule.setEms(getEmsForRuleCol(i));
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_enabled}
                    },
                    new int[] {
                            getColor(R.color.colorPrimaryDark)
                    }
            );

            actionRule.setButtonTintList(colorStateList);

            actionRule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fnOnClickActionRule(v, actionToAdd, ruleIndex);
                    List<Pair<Integer, Integer>> badRows = Utility
                            .testForConsistency(conditionList, actionList);
                    showBadRows(badRows);
                }
            });
            row.addView(actionRule);
        }

        final Context context = this.getApplicationContext();
        final ImageButton buttonDelete = new ImageButton(this);
        buttonDelete.setImageResource(R.drawable.close);
        buttonDelete.setImageAlpha(150);
        buttonDelete.setPadding(20,15,0,15);
        buttonDelete.setForegroundGravity(Gravity.CENTER);
        buttonDelete.setBackground(null);
        buttonDelete.setTag(actionToAdd);
        row.setVerticalGravity(Gravity.CENTER_VERTICAL);
        row.addView(buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOnClickButtonDeleteRow(actionList, v, context, row);
            }
        });
    }

    private void addRowToUI(final Condition conditionToAdd) {
        setTableVisible(R.id.tableHeader, true);
        
        TableLayout table;
        table = findViewById(R.id.tableCondition);

        final TableRow row = new TableRow(this);
        final TextView columnID = new TextView(this);
        final EditText columnText = new EditText(this);

        table.addView(row);
        row.addView(columnID);
        row.addView(columnText);

        columnID.setText(getString(R.string.prefixConditionRow)
                + conditionList.indexOf(conditionToAdd));
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
                if (count != 0 && s.charAt(start) == ';') {
                    String replace = s.toString().replace(";", "");
                    columnText.setText(replace);
                    columnText.setError(getString(R.string.noSemicolons));
                }
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
            conditionRule.setText(conditionToAdd.rules.get(i).getRuleConditionValue());
            conditionRule.setEms(getEmsForRuleCol(i));
            conditionRule.setPadding(5,15,5,15);
            conditionRule.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            conditionRule.setTextColor(getColor(R.color.colorPrimaryDark));
            conditionRule.setClickable(true);
            conditionRule.setFocusable(true);
            conditionRule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fnOnClickConditionRule(v, conditionToAdd, ruleIndex);
                    boolean testComplete = Utility.isListComplete(conditionList);
                    if (!testComplete) {
                        Toast.makeText(MainActivity.this, R.string.warningComplete,
                                Toast.LENGTH_SHORT).show();
                    }
                    List<Pair<Integer, Integer>> badRows = Utility
                            .testForConsistency(conditionList, actionList);
                    showBadRows(badRows);
                }
            });
            row.addView(conditionRule);
        }

        final Context context = this.getApplicationContext();
        final ImageButton buttonDelete = new ImageButton(this);
        buttonDelete.setImageResource(R.drawable.close);
        buttonDelete.setImageAlpha(150);
        buttonDelete.setPadding(20,15,0,15);
        buttonDelete.setForegroundGravity(Gravity.CENTER);
        buttonDelete.setBackground(null);
        buttonDelete.setTag(conditionToAdd);
        row.setVerticalGravity(Gravity.CENTER_VERTICAL);
        row.addView(buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOnClickButtonDeleteRow(conditionList, v, context, row);
            }
        });
    }

    private void addRuleColHeader(int iRuleCount) {
        TextView columnText = new TextView(this);
        TableLayout table = findViewById(R.id.tableHeader);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        columnText.setEms(getEmsForRuleCol(iRuleCount));
        columnText.setText(getString(R.string.prefixRuleCol) + iRuleCount);
        final Context context = this.getApplicationContext();
        final ImageButton buttonDelete = new ImageButton(this);
        buttonDelete.setImageResource(R.drawable.close);
        buttonDelete.setImageAlpha(150);
        buttonDelete.setPadding(-35,0,0,20); //TODO check for center horizontal
        buttonDelete.setForegroundGravity(Gravity.CENTER);
        TableRow row = (TableRow) table.getChildAt(0);
        row.setVerticalGravity(Gravity.BOTTOM);
        buttonDelete.setBackground(null);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOnClickButtonDeleteCol(v, context);
            }
        });
        linearLayout.addView(buttonDelete);
        linearLayout.addView(columnText);
        row.addView(linearLayout);
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

    private void clearHeaderTable() {
        setTableVisible(R.id.tableHeader, false);
        TableLayout table = findViewById(R.id.tableHeader);
        TableRow row = (TableRow) table.getChildAt(0);
        row.removeViews(2, row.getChildCount() - 2);
    }

    private void showBadRows(List<Pair<Integer, Integer>> badRows) {
        TableLayout conditionTable = findViewById(R.id.tableCondition);
        TableLayout actionTable = findViewById(R.id.tableAction);
        for (int i = 0; i < conditionTable.getChildCount(); i++) {
            TableRow row = (TableRow) conditionTable.getChildAt(i);
            for (int j = 2; j < row.getChildCount() - 1; j++) {
                row.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
            }
            if (!badRows.isEmpty()) {
                Toast.makeText(this, R.string.warningConsitency, Toast.LENGTH_SHORT).show();
            }
            for (int j = 0; j < badRows.size(); j++) {
                row.getChildAt(badRows.get(j).first + 2).setBackgroundColor(getResources().getColor(R.color.colorRedMel));
                row.getChildAt(badRows.get(j).second + 2).setBackgroundColor(getResources().getColor(R.color.colorRedMel));
            }
        }
        for (int i = 0; i < actionTable.getChildCount(); i++) {
            TableRow row = (TableRow) actionTable.getChildAt(i);
            for (int j = 2; j < row.getChildCount() - 1; j++) {
                row.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
            }
            for (int j = 0; j < badRows.size(); j++) {
                row.getChildAt(badRows.get(j).first + 2).setBackgroundColor(getResources().getColor(R.color.colorRedMel));
                row.getChildAt(badRows.get(j).second + 2).setBackgroundColor(getResources().getColor(R.color.colorRedMel));
            }
        }
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
        showBadRows(Utility.testForConsistency(conditionList, actionList));

        createHeaderColsRules();

        if (this.actionList.size() == 0 && this.conditionList.size() == 0) {
            Intent initialIntent = new Intent(this, InitialActivity.class);

            initialIntent.putExtra("rules", Utility.getRuleCount(conditionList, actionList));
            startActivityForResult(initialIntent, REQUEST_EDIT_TABLE);
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
                Action action = new Action(Utility.getRuleCount(conditionList, actionList));
                actionList.add(action);
                storageHelper.update(actionList, conditionList);
                addRowToUI(action);
                showBadRows(Utility.testForConsistency(conditionList, actionList));
                return true;
            case R.id.buttonAddConditionRow:
                Condition condition = new Condition(Utility.getRuleCount(conditionList, actionList));
                conditionList.add(condition);
                storageHelper.update(actionList, conditionList);
                addRowToUI(condition);
                showBadRows(Utility.testForConsistency(conditionList, actionList));
                return true;
            case R.id.buttonAddRuleColumn:
                if (!conditionList.isEmpty() || !actionList.isEmpty()) {
                    int ruleCount = Utility.getRuleCount(conditionList, actionList);
                    setNumberOfRules(ruleCount + 1);
                    addRuleColHeader(ruleCount);
                }
                storageHelper.update(actionList, conditionList);
                showBadRows(Utility.testForConsistency(conditionList, actionList));
                return true;
            case R.id.buttonCreateInitialTable:
                Intent initialIntent = new Intent(this, InitialActivity.class);
                initialIntent.putExtra("conditions", conditionList.size());
                initialIntent.putExtra("actions", actionList.size());
                initialIntent.putExtra("rules", Utility.getRuleCount(conditionList, actionList));
                startActivityForResult(initialIntent, REQUEST_EDIT_TABLE);
                return true;
            case R.id.buttonClearTable:
                clearTableDialog();
                return true;
            case R.id.buttonExport:
                showExportDialog();
                return true;
            case R.id.buttonImportCSV:
                showImportDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showExportDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.export);
        dialogBuilder.setMessage(R.string.exportAskFor);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_export, null);
        final EditText etFileName = dialogView.findViewById(R.id.editTextFileName);
        Button btnDoExport = dialogView.findViewById(R.id.buttonDoExport);
        Button cancel = dialogView.findViewById(R.id.buttonCancelExport);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupExportFormat);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbExportJava:
                        etFileName.setText(getString(R.string.app_name) + getString(R.string.extJava));
                        break;
                    case R.id.rbExportCS:
                        etFileName.setText(getString(R.string.app_name) + getString(R.string.extCs));
                        break;
                    case R.id.rbExportCSV:
                        etFileName.setText(getString(R.string.app_name) + getString(R.string.extCsv));
                        break;
                }
            }
        });

        btnDoExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = etFileName.getText().toString();
                Codegenerator codegenerator;
                String code;

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }

                File fileToShare;

                switch(radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rbExportJava:
                        codegenerator = new Codegenerator();
                        code = codegenerator.generateCode(conditionList, actionList);
                        fileToShare = FileHelper.buildFile(code, filename);
                        showShareActivity(fileToShare);
                        dialog.cancel();
                        break;
                    case R.id.rbExportCS:
                        codegenerator = new Codegenerator();
                        code = codegenerator.generateCode(conditionList, actionList);
                        fileToShare = FileHelper.buildFile(code, filename);
                        showShareActivity(fileToShare);
                        dialog.cancel();
                        break;
                    case R.id.rbExportCSV:
                        fileToShare = FileHelper.exportToCSV(conditionList, actionList, filename);
                        showShareActivity(fileToShare);
                        dialog.cancel();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, R.string.warningNoFormat, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void showShareActivity(File fileToShare) {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/csv");
            Uri fileUri = FileProvider.getUriForFile(this, "com.myfileprovider", fileToShare);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            this.startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
        }
    }

    private void showImportDialog() {
        if (conditionList.isEmpty() && actionList.isEmpty()) {
            showOpenFileActivity();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.importCsv);
            builder.setMessage(R.string.importCsvAskFor);

            builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showOpenFileActivity();
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
    }

    private void showOpenFileActivity() {
        int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        Intent openFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        openFileIntent.setType("text/*");
        openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(openFileIntent, REQUEST_IMPORT_CSV);
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

                    clearHeaderTable();
                    createHeaderColsRules();

                    updateStorage(getApplicationContext());
                } else if (resultCode == RESULT_IMPORT) {
                    showImportDialog();
                }
                break;
            case REQUEST_IMPORT_CSV:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri;
                    if (data != null) {
                        Context context = getApplicationContext();
                        uri = data.getData();

                        InputStream stream;
                        ArrayList<TableEntry> entries = new ArrayList<>();
                        try {
                            stream = context.getContentResolver().openInputStream(uri);
                            entries = FileHelper.loadFromCSV(stream);
                            if (entries.isEmpty()) {
                                Toast.makeText(context, R.string.invalidCSV, Toast.LENGTH_LONG).show();
                                break;
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        conditionList = new ArrayList<>();
                        actionList = new ArrayList<>();
                        clearUITable();

                        for (TableEntry entry : entries) {
                            if (entry.getClass() == Condition.class) {
                                conditionList.add((Condition) entry);
                                addRowToUI((Condition) entry);
                            } else if (entry.getClass() == Action.class) {
                                actionList.add((Action) entry);
                                addRowToUI((Action) entry);
                            }
                        }

                        clearHeaderTable();
                        createHeaderColsRules();

                        updateStorage(getApplicationContext());
                        Toast.makeText(context, R.string.validCSV, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fnOnClickActionRule (View v, Action actionToAdd, int ruleIndex) {
        final int index = actionList.indexOf(actionToAdd);
        actionList.get(index).rules.get(ruleIndex).setRuleActionValue(((CheckBox) v).isChecked());
        updateStorage(getApplicationContext());
    }
    
    private void fnOnClickConditionRule (View v, Condition conditionToAdd, int ruleIndex) {
        String currentText = (String) ((TextView) v).getText();
        int conditionIndex = conditionList.indexOf(conditionToAdd);
        switch (currentText) {
            case "-":
                ((TextView) v).setText("J");
                conditionList.get(conditionIndex).rules.get(ruleIndex).setRuleConditionValue("J");
                break;
            case "J":
                ((TextView) v).setText("N");
                conditionList.get(conditionIndex).rules.get(ruleIndex).setRuleConditionValue("N");
                break;
            case "N":
                ((TextView) v).setText("-");
                conditionList.get(conditionIndex).rules.get(ruleIndex).setRuleConditionValue("-");
                break;
        }
        updateStorage(getApplicationContext());
    }

    private void fnOnClickButtonDeleteRow (Object list, View v, Context context, TableRow row) {
        ImageButton button = (ImageButton) v;
        boolean bIsActionListEmpty = true, bIsConditionListEmpty = true;
        if (((ArrayList<?>)list).get(0) instanceof Action) {
            actionList.remove(button.getTag());
            removeFromTableLayout(row, R.id.tableAction);
            bIsActionListEmpty = actionList.size() == 0;
            bIsConditionListEmpty = conditionList.size() == 0;
            updateUIRowNumbers(R.id.tableAction, getString(R.string.prefixActionRow));
        } else if (((ArrayList<?>)list).get(0) instanceof Condition) {
            conditionList.remove(button.getTag());
            removeFromTableLayout(row, R.id.tableCondition);
            bIsConditionListEmpty = conditionList.size() == 0;
            bIsActionListEmpty = actionList.size() == 0;
            updateUIRowNumbers(R.id.tableCondition, getString(R.string.prefixConditionRow));
        }
        boolean bIsTableEmpty = bIsActionListEmpty && bIsConditionListEmpty;
        if (bIsTableEmpty) {
            setTableVisible(R.id.tableHeader, false);
            clearHeaderTable();
        }
        showBadRows(Utility.testForConsistency(conditionList, actionList));
        updateStorage(context);
    }

    private void fnOnClickButtonDeleteCol(View v, Context context) {
        ImageButton button = (ImageButton) v;
        int ruleToDelete = getRuleIndexInRow(button);
        int iRowCount;

        for (Action action:actionList) {
            action.rules.remove(ruleToDelete);
        }
        for (Condition condition:conditionList) {
            condition.rules.remove(ruleToDelete);
        }

        TableLayout headerTable = findViewById(R.id.tableHeader);
        TableRow row = (TableRow)headerTable.getChildAt(0);
        View col = row.getChildAt(ruleToDelete + 2);
        row.removeView(col);
        updateUIColNumbers();

        TableLayout conditionsTable = findViewById(R.id.tableCondition);
        iRowCount = conditionsTable.getChildCount();
        for (int i = 0; i < iRowCount; i++) {
            row = (TableRow)conditionsTable.getChildAt(i);
            col = row.getChildAt(ruleToDelete + 2);
            row.removeView(col);
        }
        TableLayout actionsTable = findViewById(R.id.tableAction);
        iRowCount = actionsTable.getChildCount();
        for (int i = 0; i < iRowCount; i++) {
            row = (TableRow)actionsTable.getChildAt(i);
            col = row.getChildAt(ruleToDelete + 2);
            row.removeView(col);
        }
        showBadRows(Utility.testForConsistency(conditionList, actionList));
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
        clearHeaderTable();
        conditionList.clear();
        actionList.clear();
        updateStorage(context);
        Toast.makeText(MainActivity.this, R.string.clearTableSuccess, Toast.LENGTH_SHORT).show();
    }

    private void updateStorage (Context context) {
        StorageHelper storageHelper = new StorageHelper(context);
        storageHelper.update(actionList, conditionList);
    }

    private void updateUIRowNumbers (int tableId, String type) {
        TableLayout table = findViewById(tableId);
        int rowCount = table.getChildCount();
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = (TableRow) table.getChildAt(i);
            TextView col = (TextView) tableRow.getChildAt(0);
            col.setText(type + i);
        }
    }

    private void updateUIColNumbers () {
        TableLayout table = findViewById(R.id.tableHeader);
        TableRow row = (TableRow) table.getChildAt(0);
        int ruleCount = row.getChildCount() - 2;
        for (int i = 0; i < ruleCount; i++) {
            LinearLayout linearLayout = (LinearLayout) row.getChildAt(i + 2);
            TextView textView = (TextView) linearLayout.getChildAt(1);
            textView.setText(getString(R.string.prefixRuleCol) + i);
        }
    }

    private void createHeaderColsRules () {
        boolean bIsActionListFilled = this.actionList.size() != 0;
        boolean bIsConditionListFilled = this.conditionList.size() != 0;
        if (bIsConditionListFilled) {
            setTableVisible(R.id.tableHeader, true);
            for(int i = 0; i < Utility.getRuleCount(conditionList, actionList); i++) {
                addRuleColHeader(i);
            }
        } else if (bIsActionListFilled) {
            setTableVisible(R.id.tableHeader, true);
            for(int i = 0; i < Utility.getRuleCount(conditionList, actionList); i++) {
                addRuleColHeader(i);
            }
        }
    }

    private int getRuleIndexInRow(ImageButton button) {
        return ((TableRow)(button.getParent().getParent())).indexOfChild((View)button.getParent()) - 2;
    }

    private int getEmsForRuleCol (int i) {
        int numberLength = getNumberLength(i);
        int emsToReturn = 2;
        if (numberLength != 1) {
            emsToReturn = numberLength;
        }
        return emsToReturn;
    }

    private int getNumberLength (int i) {
        return Integer.toString(i).length();
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

    private void setTableVisible (int iTableId, boolean bVisible) {
        TableLayout table = findViewById(iTableId);
        if (bVisible) {
            table.setVisibility(View.VISIBLE);
        } else if (!bVisible) {
            table.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        //File toDelete = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ASE.csv");
        //if (toDelete.exists())
        //    toDelete.delete();
        super.onDestroy();
    }
}
