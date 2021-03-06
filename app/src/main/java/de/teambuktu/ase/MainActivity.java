package de.teambuktu.ase;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static de.teambuktu.ase.FileHelper.sharedFolderPath;

public class MainActivity extends AppCompatActivity {
    private Menu menu;
    private Notification testCompleteNotification;
    ArrayList<Action> actionList = new ArrayList<>();
    ArrayList<Condition> conditionList = new ArrayList<>();
    private static final int REQUEST_EDIT_TABLE = 0;
    private static final int REQUEST_EXPORT_CSV = 1;
    private static final int REQUEST_IMPORT_CSV = 2;
    public static final int RESULT_IMPORT = 2;
    public static final int REQUEST_SHARE = 4;
    private static final int REQUEST_EXT_STORAGE_PERMISSION = 5;
    private StorageHelper storageHelper;

    private File tempSharedFile;

    private Toast consistencyToast = null;

    private void addRowToUi(final Action actionToAdd) {
        setTableVisible(R.id.tableHeader, true);

        TableLayout table;
        table = findViewById(R.id.tableAction);

        final TableRow row = new TableRow(this);
        final TextView columnId = new TextView(this);
        final EditText columnText = new EditText(this);

        table.addView(row);
        row.addView(columnId);
        row.addView(columnText);

        columnId.setText(getString(R.string.prefixActionRow) + actionList.indexOf(actionToAdd));
        columnId.setEms(2);
        columnId.setIncludeFontPadding(true);
        columnId.setPadding(10, 0, 0, 0);

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
                    new int[]{
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
        buttonDelete.setPadding(20, 15, 0, 15);
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

    private void addRowToUi(final Condition conditionToAdd) {
        setTableVisible(R.id.tableHeader, true);

        TableLayout table;
        table = findViewById(R.id.tableCondition);

        final TableRow row = new TableRow(this);
        final TextView columnId = new TextView(this);
        final EditText columnText = new EditText(this);

        table.addView(row);
        row.addView(columnId);
        row.addView(columnText);

        columnId.setText(getString(R.string.prefixConditionRow)
                + conditionList.indexOf(conditionToAdd));
        columnId.setEms(2);
        columnId.setIncludeFontPadding(true);
        columnId.setPadding(10, 0, 0, 0);

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
                storageHelper.update(actionList, conditionList);
            }
        });

        TextView conditionRule;
        for (int i = 0; i < conditionToAdd.rules.size(); i++) {
            final int ruleIndex = i;
            conditionRule = new TextView(this);
            conditionRule.setText(conditionToAdd.rules.get(i).getRuleConditionValue());
            conditionRule.setEms(getEmsForRuleCol(i));
            conditionRule.setPadding(5, 15, 5, 15);
            conditionRule.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            conditionRule.setTextColor(getColor(R.color.colorPrimaryDark));
            conditionRule.setClickable(true);
            conditionRule.setFocusable(true);
            conditionRule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fnOnClickConditionRule(v, conditionToAdd, ruleIndex);
                    checkCompleteness();
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
        buttonDelete.setPadding(20, 15, 0, 15);
        buttonDelete.setForegroundGravity(Gravity.CENTER);
        buttonDelete.setBackground(null);
        buttonDelete.setTag(conditionToAdd);
        row.setVerticalGravity(Gravity.CENTER_VERTICAL);
        row.addView(buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOnClickButtonDeleteRow(conditionList, v, context, row);
                checkCompleteness();
            }
        });
    }

    private void addRuleColHeader(int ruleCount) {
        TextView columnText = new TextView(this);
        final TableLayout table = findViewById(R.id.tableHeader);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        columnText.setEms(getEmsForRuleCol(ruleCount));
        columnText.setText(getString(R.string.prefixRuleCol) + ruleCount);
        final Context context = this.getApplicationContext();
        final ImageButton buttonDelete = new ImageButton(this);
        buttonDelete.setImageResource(R.drawable.close);
        buttonDelete.setImageAlpha(150);
        buttonDelete.setPadding(-35, 0, 0, 20); //TODO check for center horizontal
        buttonDelete.setForegroundGravity(Gravity.CENTER);
        TableRow row = (TableRow) table.getChildAt(0);
        row.setVerticalGravity(Gravity.BOTTOM);
        buttonDelete.setBackground(null);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOnClickButtonDeleteCol(v, context);
                checkCompleteness();
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
                showWarningSymbol(false);
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

    private void checkCompleteness() {
        testCompleteNotification = Utility.isListComplete(this.conditionList);
        showWarningSymbol(!testCompleteNotification.isEmpty());
    }

    private void showBadRows(List<Pair<Integer, Integer>> badRows) {
        if (!badRows.isEmpty()) {
            storageHelper.setConsistencyFlag(false);
            consistencyToast.setText(R.string.warningConsitency);
            consistencyToast.show();
        } else if (!storageHelper.getConsistencyFlag()) {
            storageHelper.setConsistencyFlag(true);
            consistencyToast.setText(R.string.validConsistency);
            consistencyToast.show();
        }
        TableLayout conditionTable = findViewById(R.id.tableCondition);
        TableLayout actionTable = findViewById(R.id.tableAction);
        for (int i = 0; i < conditionTable.getChildCount(); i++) {
            TableRow row = (TableRow) conditionTable.getChildAt(i);
            for (int j = 2; j < row.getChildCount() - 1; j++) {
                row.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
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

        storageHelper = new StorageHelper(this.getApplicationContext());

        consistencyToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        ArrayList<Action> actions = storageHelper.loadActions();
        this.actionList = actions;
        for (Action action : actions) {
            addRowToUi(action);
        }

        ArrayList<Condition> conditions = storageHelper.loadConditions();
        this.conditionList = conditions;
        for (Condition condition : conditions) {
            addRowToUi(condition);
        }
        showBadRows(Utility.testForConsistency(conditionList, actionList));

        if (this.actionList.isEmpty() && this.conditionList.isEmpty()) {
            Condition initialCondition = new Condition(1);
            Action initialAction = new Action(1);
            conditionList.add(initialCondition);
            actionList.add(initialAction);
            addRowToUi(initialCondition);
            addRowToUi(initialAction);
            updateStorage(getApplicationContext());
        }

        createHeaderColsRules();
        handleDeleteButtonColor();
        handleShowHowTo();
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
        this.menu = menu;
        checkCompleteness();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.symbolWarning:
                Dialog dialog = testCompleteNotification.createWarningDialog(this);
                dialog.show();
                return true;
            case R.id.buttonAddActionRow:
                Action action = new Action(Utility.getRuleCount(conditionList, actionList));
                actionList.add(action);
                storageHelper.update(actionList, conditionList);
                addRowToUi(action);
                handleDeleteButtonColor();
                showBadRows(Utility.testForConsistency(conditionList, actionList));
                return true;
            case R.id.buttonAddConditionRow:
                Condition condition = new Condition(Utility.getRuleCount(conditionList, actionList));
                conditionList.add(condition);
                storageHelper.update(actionList, conditionList);
                addRowToUi(condition);
                handleDeleteButtonColor();

                checkCompleteness();
                showBadRows(Utility.testForConsistency(conditionList, actionList));
                return true;
            case R.id.buttonAddRuleColumn:
                if (!conditionList.isEmpty() || !actionList.isEmpty()) {
                    int ruleCount = Utility.getRuleCount(conditionList, actionList);
                    setNumberOfRules(ruleCount + 1);
                    addRuleColHeader(ruleCount);
                }
                storageHelper.update(actionList, conditionList);
                handleDeleteButtonColor();
                checkCompleteness();
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
                handleDeleteButtonColor();
                return true;
            case R.id.buttonExport:
                handleExportPermission();
                return true;
            case R.id.buttonImportCSV:
                showImportDialog();
                return true;
            case R.id.buttonShowAppInfo:
                Intent appInfoIntent = new Intent(this, AppInfo.class);
                startActivity(appInfoIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleExportPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXT_STORAGE_PERMISSION);
        } else {
            showExportDialog();
        }
    }

    private void showExportDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.export);
        dialogBuilder.setMessage(R.string.exportAskFor);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_export, null);
        final Spinner spinner = dialogView.findViewById(R.id.spinnerExportFormat);
        final EditText etFileName = dialogView.findViewById(R.id.editTextFileName);
        final Button cancel = dialogView.findViewById(R.id.buttonCancelExport);
        final Button btnDoExport = dialogView.findViewById(R.id.buttonDoExport);

        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();

        btnDoExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = etFileName.getText().toString();
                if (filename.isEmpty()) {
                    filename = getString(R.string.app_name);
                }

                Codegenerator codegenerator;
                String code;

                File fileToShare;

                switch ((int)spinner.getSelectedItemId()) {
                    case 0:
                        codegenerator = new Codegenerator();
                        code = codegenerator.generateCode(conditionList, actionList);
                        fileToShare = FileHelper.buildFile(code, filename + getString(R.string.extJava));
                        showShareActivity(fileToShare);
                        dialog.cancel();
                        break;
                    case 1:
                        codegenerator = new Codegenerator();
                        code = codegenerator.generateCode(conditionList, actionList);
                        fileToShare = FileHelper.buildFile(code, filename + getString(R.string.extCs));
                        showShareActivity(fileToShare);
                        dialog.cancel();
                        break;
                    case 2:
                        fileToShare = FileHelper.exportToCsv(conditionList, actionList, filename + getString(R.string.extCsv));
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
            tempSharedFile = fileToShare;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/*");
            Uri fileUri = FileProvider.getUriForFile(this, "com.myfileprovider", fileToShare);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            this.startActivityForResult(Intent.createChooser(shareIntent, getString(R.string.share)), REQUEST_SHARE);
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
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        Intent openFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        openFileIntent.setType("text/*");
        openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(openFileIntent, REQUEST_IMPORT_CSV);
    }

    private void handleDeleteButtonColor() {
        setDeleteButtonColor(actionList.size(), R.id.tableAction);
        setDeleteButtonColor(conditionList.size(), R.id.tableCondition);
        setDeleteButtonColor(getRuleCount(), R.id.tableHeader);
    }

    private void setDeleteButtonColor(int listSize, int tableId) {
        TableLayout table = findViewById(tableId);
        TableRow row = (TableRow) table.getChildAt(0);
        ImageButton deleteButton;
        if (tableId == R.id.tableHeader) {
            LinearLayout linearLayout = (LinearLayout) row.getChildAt(2);
            deleteButton = (ImageButton) linearLayout.getChildAt(0);
        } else {
            deleteButton = (ImageButton) row.getChildAt(row.getChildCount() - 1);
        }
        if (listSize <= 1) {
            deleteButton.setColorFilter(Color.GRAY);
        } else {
            deleteButton.clearColorFilter();
        }
    }

    private void showWarningSymbol(boolean show) {
        MenuItem warningItem = menu.findItem(R.id.symbolWarning);
        warningItem.setVisible(show);
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
                            addRowToUi(condition);
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
                            addRowToUi(action);
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
                    handleDeleteButtonColor();

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
                            entries = FileHelper.loadFromCsv(stream);
                            if (entries.isEmpty()) {
                                Toast.makeText(context, R.string.invalidCSV, Toast.LENGTH_LONG).show();
                                break;
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        conditionList = new ArrayList<>();
                        actionList = new ArrayList<>();
                        clearUiTable();

                        for (TableEntry entry : entries) {
                            if (entry.getClass() == Condition.class) {
                                conditionList.add((Condition) entry);
                                addRowToUi((Condition) entry);
                            } else if (entry.getClass() == Action.class) {
                                actionList.add((Action) entry);
                                addRowToUi((Action) entry);
                            }
                        }

                        clearHeaderTable();
                        createHeaderColsRules();
                        handleDeleteButtonColor();

                        updateStorage(getApplicationContext());
                        Toast.makeText(context, R.string.validCSV, Toast.LENGTH_LONG).show();
                    }
                    checkCompleteness();
                }
                break;
            case REQUEST_SHARE:
                tempSharedFile.delete();
                (new File(sharedFolderPath)).delete();
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXT_STORAGE_PERMISSION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showExportDialog();
                }
                break;
            default:
                break;
        }
    }

    private void fnOnClickActionRule(View v, Action actionToAdd, int ruleIndex) {
        final int index = actionList.indexOf(actionToAdd);
        actionList.get(index).rules.get(ruleIndex).setRuleActionValue(((CheckBox) v).isChecked());
        updateStorage(getApplicationContext());
    }

    private void fnOnClickConditionRule(View v, Condition conditionToAdd, int ruleIndex) {
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
            default:
                break;
        }
        updateStorage(getApplicationContext());
    }

    private void fnOnClickButtonDeleteRow(List list, View buttonDelete, Context context, TableRow rowUi) {
        if (list.size() > 1) {
            ImageButton button = (ImageButton) buttonDelete;
            boolean isActionListEmpty = actionList.isEmpty();
            boolean isConditionListEmpty = conditionList.isEmpty();
            if (((ArrayList<?>) list).get(0) instanceof Action) {
                actionList.remove(button.getTag());
                removeFromTableLayout(rowUi, R.id.tableAction);
                updateUiRowNumbers(R.id.tableAction, getString(R.string.prefixActionRow));
            } else if (((ArrayList<?>) list).get(0) instanceof Condition) {
                conditionList.remove(button.getTag());
                removeFromTableLayout(rowUi, R.id.tableCondition);
                updateUiRowNumbers(R.id.tableCondition, getString(R.string.prefixConditionRow));
            }
            boolean isTableEmpty = isActionListEmpty && isConditionListEmpty;
            if (isTableEmpty) {
                setTableVisible(R.id.tableHeader, false);
                clearHeaderTable();
            }
            showBadRows(Utility.testForConsistency(conditionList, actionList));
            updateStorage(context);
            handleDeleteButtonColor();
        }
    }

    private void fnOnClickButtonDeleteCol(View v, Context context) {
        if (getRuleCount() > 1) {
            ImageButton button = (ImageButton) v;
            int ruleToDelete = getRuleIndexInRow(button);

            for (Action action : actionList) {
                action.rules.remove(ruleToDelete);
            }
            for (Condition condition : conditionList) {
                condition.rules.remove(ruleToDelete);
            }

            TableLayout headerTable = findViewById(R.id.tableHeader);
            TableRow row = (TableRow) headerTable.getChildAt(0);
            View col = row.getChildAt(ruleToDelete + 2);
            row.removeView(col);
            updateUiColNumbers();

            TableLayout conditionsTable = findViewById(R.id.tableCondition);
            int rowCount = conditionsTable.getChildCount();
            for (int i = 0; i < rowCount; i++) {
                row = (TableRow) conditionsTable.getChildAt(i);
                col = row.getChildAt(ruleToDelete + 2);
                row.removeView(col);
            }
            TableLayout actionsTable = findViewById(R.id.tableAction);
            rowCount = actionsTable.getChildCount();
            for (int i = 0; i < rowCount; i++) {
                row = (TableRow) actionsTable.getChildAt(i);
                col = row.getChildAt(ruleToDelete + 2);
                row.removeView(col);
            }
            showBadRows(Utility.testForConsistency(conditionList, actionList));
            updateStorage(context);
            handleDeleteButtonColor();
        }
    }

    private void clearUiTable() {
        TableLayout conditionTable = findViewById(R.id.tableCondition);
        conditionTable.removeAllViews();
        TableLayout actionTable = findViewById(R.id.tableAction);
        actionTable.removeAllViews();
    }

    private void fnOnClickPositiveButtonClearTable(Context context) {
        clearUiTable();
        clearHeaderTable();
        conditionList.clear();
        actionList.clear();

        Condition initialCondition = new Condition(1);
        Action initialAction = new Action(1);
        conditionList.add(initialCondition);
        actionList.add(initialAction);
        addRowToUi(initialCondition);
        addRowToUi(initialAction);
        addRuleColHeader(0);
        handleDeleteButtonColor();

        updateStorage(context);
        storageHelper.setConsistencyFlag(true);
        Toast.makeText(MainActivity.this, R.string.clearTableSuccess, Toast.LENGTH_SHORT).show();
    }

    private void updateStorage(Context context) {
        storageHelper.update(actionList, conditionList);
    }

    private void updateUiRowNumbers(int tableId, String type) {
        TableLayout table = findViewById(tableId);
        int rowCount = table.getChildCount();
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = (TableRow) table.getChildAt(i);
            TextView col = (TextView) tableRow.getChildAt(0);
            col.setText(type + i);
        }
    }

    private void updateUiColNumbers() {
        TableLayout table = findViewById(R.id.tableHeader);
        TableRow row = (TableRow) table.getChildAt(0);
        int ruleCount = row.getChildCount() - 2;
        for (int i = 0; i < ruleCount; i++) {
            LinearLayout linearLayout = (LinearLayout) row.getChildAt(i + 2);
            TextView textView = (TextView) linearLayout.getChildAt(1);
            textView.setText(getString(R.string.prefixRuleCol) + i);
        }
    }

    private void createHeaderColsRules() {
        boolean isActionListFilled = this.actionList.size() != 0;
        boolean isConditionListFilled = this.conditionList.size() != 0;
        if (isConditionListFilled) {
            setTableVisible(R.id.tableHeader, true);
            for (int i = 0; i < Utility.getRuleCount(conditionList, actionList); i++) {
                addRuleColHeader(i);
            }
        } else if (isActionListFilled) {
            setTableVisible(R.id.tableHeader, true);
            for (int i = 0; i < Utility.getRuleCount(conditionList, actionList); i++) {
                addRuleColHeader(i);
            }
        }
    }

    private int getRuleIndexInRow(ImageButton button) {
        return ((TableRow) (button.getParent().getParent())).indexOfChild((View) button.getParent()) - 2;
    }

    private int getEmsForRuleCol(int i) {
        int numberLength = getNumberLength(i);
        int emsToReturn = 2;
        if (numberLength != 1) {
            emsToReturn = numberLength;
        }
        return emsToReturn;
    }

    private int getNumberLength(int i) {
        return Integer.toString(i).length();
    }

    private void setNumberOfRules(int count) {
        clearUiTable();

        for (Action action : actionList) {
            action.setNumberOfRules(count);
            addRowToUi(action);
        }

        for (Condition condition : conditionList) {
            condition.setNumberOfRules(count);
            addRowToUi(condition);
        }
    }

    private void setTableVisible(int tableId, boolean visible) {
        TableLayout table = findViewById(tableId);
        if (visible) {
            table.setVisibility(View.VISIBLE);
        } else {
            table.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void handleShowHowTo() {
        boolean isInitialStartup = storageHelper.getInitialStartup();
        if (isInitialStartup) {
            Intent howToIntent = new Intent(this, HowToActivity.class);
            howToIntent.putExtra("isInitial", false);
            startActivity(howToIntent);
            storageHelper.setInitialStartupFlag(false);
        } else {
            handleMoveToInitialActivity(this.actionList, this.conditionList);
        }
    }

    private void handleMoveToInitialActivity(List<Action> actionList, List<Condition> conditionList) {
        if (actionList.size() == 0 && conditionList.size() == 0) {
            Intent initialIntent = new Intent(this, InitialActivity.class);

            initialIntent.putExtra("rules", Utility.getRuleCount(conditionList, actionList));
            startActivityForResult(initialIntent, REQUEST_EDIT_TABLE);
        }
    }
}
