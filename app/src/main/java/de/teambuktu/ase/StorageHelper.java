package de.teambuktu.ase;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static java.io.File.createTempFile;

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
            JSONArray actionArray = new JSONArray();
            for (Action action : actionList) {
                actionArray.put(action.toJSON());
            }
            sharedPreferences.edit().putString(ACTIONS, actionArray.toString()).apply();
        }
        if (conditionList != null) {
            JSONArray conditionArray = new JSONArray();
            for (Condition condition : conditionList) {
                conditionArray.put(condition.toJSON());
            }
            sharedPreferences.edit().putString(CONDITIONS, conditionArray.toString()).apply();
        }
    }

    ArrayList<Action> loadActions() {
        String actionsString = sharedPreferences.getString(ACTIONS, null);
        ArrayList<Action> actionList = new ArrayList<>();

        try {
            JSONArray actionsJsonArray = new JSONArray(actionsString);
            for (int i = 0; i < actionsJsonArray.length(); i++) {
                Action action = Action.fromString(actionsJsonArray.get(i).toString());
                actionList.add(action);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return actionList;
    }

    ArrayList<Condition> loadConditions() {
        String conditionsString = sharedPreferences.getString(CONDITIONS, null);
        ArrayList<Condition> conditionList = new ArrayList<>();

        try {
            JSONArray conditionsJsonArray = new JSONArray(conditionsString);
            for (int i = 0; i < conditionsJsonArray.length(); i++) {
                Condition condition = Condition.fromString(conditionsJsonArray.get(i).toString());
                conditionList.add(condition);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conditionList;
    }
}
