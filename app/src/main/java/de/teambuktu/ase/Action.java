package de.teambuktu.ase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Action extends TableEntry {
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
            if (json.has("title")) title = json.getString("title");
            if (json.has("rules")) {
                JSONObject rulesJson = json.getJSONObject("rules");
                action = new Action(rulesJson.length());
                action.rules = new ArrayList<>();
                for (int i = 0; i < rulesJson.length(); i++) {
                    action.rules.add(new Rule());
                    Rule ruleImportDestination = action.rules.get(i);
                    Boolean currentActionValue = (Boolean) ((JSONObject)rulesJson.get(Integer.toString(i))).get("ruleActionValue");
                    ruleImportDestination.setRuleActionValue(currentActionValue);
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
            JSONObject jsonObjectParent = new JSONObject();
            for (int i = 0; i < this.rules.size(); i++) {
                JSONObject jsonObjectChild = new JSONObject();
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
