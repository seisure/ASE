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

class Action {
    protected int ID;
    protected String title;
    protected ArrayList<Boolean> rules = new ArrayList<>();

    public Action(int id, int ruleCount) {
        ID = id;
        for (int i = 0; i < ruleCount; i++) {
            rules.add(false);
        }
    }

    protected void setNumberOfRules(int count) {
        if (count > rules.size()) {
            for (int i = rules.size() - 1; i < count - 1; i++) {
                rules.add(false);
            }
        } else if (count < rules.size()) {
            for (int i = rules.size() - 1; i >= count; i--) {
                rules.remove(i);
            }
        }
    }

    public static Action fromString(String serialized) {
        try {
            JSONObject json = new JSONObject(serialized);
            Action action = null;
            String title = null;
            int ID = -1;
            if (json.has("title")) title = json.getString("title");
            if (json.has("ID")) ID = json.getInt("ID");
            if (json.has("rules")) {
                JSONArray rulesJson = json.getJSONArray("rules");
                action = new Action(ID, rulesJson.length());
                action.rules = new ArrayList<>();
                for (int i = 0; i < rulesJson.length(); i++) {
                    action.rules.add((Boolean) rulesJson.get(i));
                }
                action.title = title;
            }
            return action;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON() {
        try {
            JSONObject json = new JSONObject();
            json.put("ID", this.ID);
            json.put("title", this.title);
            JSONArray jsonArray = new JSONArray(this.rules);
            json.put("rules", jsonArray);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean store(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DE.TEAMBUKTU.ASE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> stored = sharedPreferences.getStringSet("actions", null);
        String serialized = this.toJSON().toString();
        if (stored != null) {
            stored.add(serialized);
        } else {
            stored = new HashSet<>();
            stored.add(serialized);
        }
        editor.remove("actions").commit();
        return editor.putStringSet("actions", stored).commit();
    }
}
