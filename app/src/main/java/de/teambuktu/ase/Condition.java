package de.teambuktu.ase;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

class Condition extends TableEntry {
    protected ArrayList<String> rules = new ArrayList<>();

    public Condition(int ruleCount) {
        for (int i = 0; i < ruleCount; i++) {
            rules.add("-");
        }
    }

    protected void setNumberOfRules(int count) {
        if (count > rules.size()) {
            for (int i = rules.size() - 1; i < count - 1; i++) {
                rules.add("-");
            }
        } else if (count < rules.size()) {
            for (int i = rules.size() - 1; i >= count; i--) {
                rules.remove(i);
            }
        }
    }

    public static Condition fromString(String serialized) {
        try {
            JSONObject json = new JSONObject(serialized);
            Condition condition = null;
            String title = null;
            int ID = -1;
            if (json.has("title")) title = json.getString("title");
            if (json.has("rules")) {
                JSONArray rulesJson = json.getJSONArray("rules");
                condition = new Condition(rulesJson.length());
                condition.rules = new ArrayList<>();
                for (int i = 0; i < rulesJson.length(); i++) {
                    condition.rules.add((String) rulesJson.get(i));
                }
                condition.setTitle(title);
            }
            return condition;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON() {
        try {
            JSONObject json = super.toJSON();
            json.put("rules", new JSONArray(this.rules));
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
