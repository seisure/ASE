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

class Condition {
    protected String title;
    protected ArrayList<Rule> rules = new ArrayList<>();

    public Condition(int ruleCount) {
        for (int i = 0; i < ruleCount; i++) {
            rules.add(new Rule());
            rules.get(i).setRuleConditionValue("-");
        }
    }

    protected void setNumberOfRules(int count) {
        if (count > rules.size()) {
            for (int i = rules.size(); i <= count - 1; i++) {
                rules.add(new Rule());
                rules.get(i).setRuleConditionValue("-");
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
                JSONObject rulesJson = json.getJSONObject("rules");
                condition = new Condition(rulesJson.length());
                condition.rules = new ArrayList<>();
                for (int i = 0; i < rulesJson.length(); i++) {
                    condition.rules.add(new Rule());
                    Rule ruleImportDestination = condition.rules.get(i);
                    int currentRuleHash = (int) ((JSONObject)rulesJson.get(Integer.toString(i))).get("ruleHash");
                    String currentConditionValue = (String) ((JSONObject)rulesJson.get(Integer.toString(i))).get("ruleConditionValue");
                    ruleImportDestination.setRuleHash(currentRuleHash);
                    ruleImportDestination.setRuleConditionValue(currentConditionValue);
                }
                condition.title = title;
            }
            return condition;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSON() {
        try {
            JSONObject json = new JSONObject();
            json.put("hash", hashCode());
            json.put("title", this.title);
            JSONObject jsonObjectParent = new JSONObject();
            for (int i = 0; i < this.rules.size(); i++) {
                JSONObject jsonObjectChild = new JSONObject();
                jsonObjectChild.put("ruleHash", this.rules.get(i).getRuleHash());
                jsonObjectChild.put("ruleConditionValue", this.rules.get(i).getRuleConditionValue());
                jsonObjectParent.put(Integer.toString(i), jsonObjectChild);
            }
            json.put("rules", jsonObjectParent);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean store(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DE.TEAMBUKTU.ASE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> stored = sharedPreferences.getStringSet("conditions", null);
        String serialized = this.toJSON().toString();
        if (stored != null) {
            stored.add(serialized);
        } else {
            stored = new HashSet<>();
            stored.add(serialized);
        }
        editor.remove("conditions").commit();
        return editor.putStringSet("conditions", stored).commit();
    }
}
