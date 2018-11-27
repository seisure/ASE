package de.teambuktu.ase;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

class Condition extends TableEntry {

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
            if (json.has("title")) {
                title = json.getString("title");
            }
            if (json.has("rules")) {
                JSONObject rulesJson = json.getJSONObject("rules");
                condition = new Condition(rulesJson.length());
                condition.rules = new ArrayList<>();
                for (int i = 0; i < rulesJson.length(); i++) {
                    condition.rules.add(new Rule());
                    Rule ruleImportDestination = condition.rules.get(i);
                    String currentConditionValue = (String) ((JSONObject)rulesJson
                            .get(Integer.toString(i))).get("ruleConditionValue");
                    ruleImportDestination.setRuleConditionValue(currentConditionValue);
                }
                condition.setTitle(title);
            }
            return condition;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJson() {
        try {
            JSONObject json = super.toJson();
            JSONObject jsonObjectParent = new JSONObject();
            for (int i = 0; i < this.rules.size(); i++) {
                JSONObject jsonObjectChild = new JSONObject();
                jsonObjectChild.put("ruleConditionValue", this.rules.get(i)
                        .getRuleConditionValue());
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
