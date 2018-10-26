package de.teambuktu.ase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Action extends TableEntry {
    protected ArrayList<Boolean> rules = new ArrayList<>();

    public Action(int ruleCount) {

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
            if (json.has("rules")) {
                JSONArray rulesJson = json.getJSONArray("rules");
                action = new Action(rulesJson.length());
                action.rules = new ArrayList<>();
                for (int i = 0; i < rulesJson.length(); i++) {
                    action.rules.add((Boolean) rulesJson.get(i));
                }
                action.setTitle(title);
            }
            return action;
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
