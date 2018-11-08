package de.teambuktu.ase;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

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
        ArrayList<Action> actionList = new ArrayList<>();

        try {
            String actionsString = sharedPreferences.getString(ACTIONS, "");
            JSONArray actionsJsonArray = new JSONArray(actionsString);
            for (int i = 0; i < actionsJsonArray.length(); i++) {
                Action action = Action.fromString(actionsJsonArray.get(i).toString());
                actionList.add(action);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassCastException ec) {
            sharedPreferences.edit().clear().apply();
        }

        return actionList;
    }

    ArrayList<Condition> loadConditions() {
        ArrayList<Condition> conditionList = new ArrayList<>();

        try {
            String conditionsString = sharedPreferences.getString(CONDITIONS, "");
            JSONArray conditionsJsonArray = new JSONArray(conditionsString);
            for (int i = 0; i < conditionsJsonArray.length(); i++) {
                Condition condition = Condition.fromString(conditionsJsonArray.get(i).toString());
                conditionList.add(condition);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassCastException ec) {
            sharedPreferences.edit().clear().apply();
        }

        return conditionList;
    }
}
