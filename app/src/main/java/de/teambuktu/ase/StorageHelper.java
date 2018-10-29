package de.teambuktu.ase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

class StorageHelper {

    private final String SHAREDPREFERENCES = "DE.TEAMBUKTU.ASE";
    private final String ACTIONS = "actions";
    private final String CONDITIONS = "conditions";

    private SharedPreferences sharedPreferences;
    StorageHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(this.SHAREDPREFERENCES, MODE_PRIVATE);
    }

    void update(ArrayList<Action> actionList, ArrayList<Condition> conditionList) {
        sharedPreferences.edit().remove(ACTIONS).remove(CONDITIONS).apply();

        if (actionList != null) {
            Set<String> actionSet = new HashSet<>();
            for (Action action : actionList) {
                actionSet.add(action.toJSON().toString());
            }
            sharedPreferences.edit().putStringSet(ACTIONS, actionSet).apply();
        }
        if (conditionList != null) {
            Set<String> conditionSet = new HashSet<>();
            for (Condition condition : conditionList) {
                conditionSet.add(condition.toJSON().toString());
            }
            sharedPreferences.edit().putStringSet(CONDITIONS, conditionSet).apply();
        }
    }

    ArrayList<Action> loadActions() {
        Set<String> actionSet = sharedPreferences.getStringSet(ACTIONS, null);

        ArrayList<Action> actionList = new ArrayList<>();

        if (actionSet != null) {
            HashMap<Integer, Action> actionHashMap = new HashMap<>();
            Iterator<String> iterator = actionSet.iterator();
            while (iterator.hasNext()) {
                String actionAsString = iterator.next();
                Action action = Action.fromString(actionAsString);
                actionList.add(action);
                //actionHashMap.put(action.ID, action);
            }
//            Iterator<Action> actionIterator = actionHashMap.values().iterator();
//            while (actionIterator.hasNext()) {
//                Action currentAction = actionIterator.next();
//                actionList.add(currentAction);
//            }
        }
        return actionList;
    }
    ArrayList<Condition> loadConditions() {
        Set<String> conditionSet = sharedPreferences.getStringSet(CONDITIONS, null);
        ArrayList<Condition> conditionList = new ArrayList<>();

        if (conditionSet != null) {
            HashMap<Integer, Condition> conditionHashMap = new HashMap<>();
            Iterator<String> iterator = conditionSet.iterator();
            while (iterator.hasNext()) {
                String conditionAsString = iterator.next();
                Condition condition = Condition.fromString(conditionAsString);
                conditionList.add(condition);
                //conditionHashMap.put(condition.ID, condition);
            }

//            Iterator<Condition> conditionIterator = conditionHashMap.values().iterator();
//            while (conditionIterator.hasNext()) {
//                Condition currentCondition = conditionIterator.next();
//                conditionList.add(currentCondition);
//            }
        }
        return conditionList;
    }

    void exportToCSV(ArrayList<Condition> conditionList, ArrayList<Action> actionList, Context context) {
        String file = "ASE.csv";
        StringBuilder builder = new StringBuilder();

        for (Condition condition : conditionList) {
            builder.append('C').append(';');
            String title = condition.getTitle();
            builder.append(title == null ? "" : title).append(';');
            for (String rule :
                    condition.rules) {
                builder.append(rule).append(';');
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append('\n');
        }

        for (Action action : actionList) {
            builder.append('A').append(';');
            String title = action.getTitle();
            builder.append(title == null ? "" : title).append(';');
            for (Boolean rule :
                    action.rules) {
                builder.append(rule ? 1 : 0).append(';');
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append('\n');
        }

        writeToFile(file, builder.toString(), context);
    }

    String loadFromCSV(Context context) {
        String file = "ASE.csv";
        return readFromFile(file, context);
    }

    private void writeToFile(String file, String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));

            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String file, Context context) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(file);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append('\n');
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
