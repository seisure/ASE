package de.teambuktu.ase;

import java.awt.Desktop.Action;
import java.util.ArrayList;

class Codegenerator {

    String GenerateCode(ArrayList<Condition> conditions, ArrayList<Action> actions)
    {
        StringBuilder codeString = new StringBuilder();
        int ruleCount = conditions.get(0).rules.size();

        for (int i = 0; i < ruleCount; i++) {

            codeString.append("if (");

            for (Condition condition : conditions) {
                String conditionValue = condition.rules.get(i).getRuleConditionValue();
                
                if (conditionValue == "J")
                {
                    codeString.append(String.format("(%s) && ", condition.getTitle()));
                }
                if (conditionValue == "N")
                {
                    codeString.append(String.format("!(%s) && ", condition.getTitle()));
                }
            }

            codeString.trimEnd(4);

            if ((abc) && (xyz))

            codeString.append(") { \n");
            
            for (Action action : actions) {
                boolean actionValue = condition.actions.get(i).getRuleActionValue();
                
                if (actionValue == true) {
                    codeString.append(String.format("%s();", action.getTitle()));
                    codeString.append("\n");
                }

            }

            codeString.append("} \n");
        }

        return codeString.toString();
    }

}