package de.teambuktu.ase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Action {
    protected String title;
    protected ArrayList<Rule> rules = new ArrayList<>();

    public Action(int ruleCount) {
        for (int i = 0; i < ruleCount; i++) {
            rules.add(new Rule());
            rules.get(i).setRuleActionValue(false);
        }
    }

    protected void setNumberOfRules(int count) {
        if (count > rules.size()) {
            for (int i = rules.size(); i <= count - 1; i++) {
                rules.add(new Rule());
                rules.get(i).setRuleActionValue(false);
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
                JSONObject rulesJson = json.getJSONObject("rules");
                action = new Action(rulesJson.length());
                action.rules = new ArrayList<>();
                for (int i = 0; i < rulesJson.length(); i++) {
                    action.rules.add(new Rule());
                    Rule ruleImportDestination = action.rules.get(i);
                    int currentRuleHash = (int) ((JSONObject)rulesJson.get(Integer.toString(i))).get("ruleHash");
                    Boolean currentActionValue = (Boolean) ((JSONObject)rulesJson.get(Integer.toString(i))).get("ruleActionValue");
                    ruleImportDestination.setRuleHash(currentRuleHash);
                    ruleImportDestination.setRuleActionValue(currentActionValue);
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
            json.put("hash", hashCode());
            json.put("title", this.title);
            JSONObject jsonObjectParent = new JSONObject();
            for (int i = 0; i < this.rules.size(); i++) {
                JSONObject jsonObjectChild = new JSONObject();
                jsonObjectChild.put("ruleHash", this.rules.get(i).getRuleHash());
                jsonObjectChild.put("ruleActionValue", this.rules.get(i).getRuleActionValue());
                jsonObjectParent.put(Integer.toString(i), jsonObjectChild);
            }
            json.put("rules", jsonObjectParent);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
