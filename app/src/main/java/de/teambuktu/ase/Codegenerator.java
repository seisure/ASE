package de.teambuktu.ase;

import java.util.ArrayList;

class Codegenerator {

    String generateCode(ArrayList<Condition> conditions, ArrayList<Action> actions)
    {
        StringBuilder codeString = new StringBuilder();
        int ruleCount = conditions.get(0).rules.size();

        for (int i = 0; i < ruleCount; i++) {

            codeString.append("if (");

            for (Condition condition : conditions) {
                String conditionValue = condition.rules.get(i).getRuleConditionValue();
                
                if (conditionValue.equals("J"))
                {
                    codeString.append(String.format("(%s) && ", condition.getTitle()));
                }
                if (conditionValue.equals("N"))
                {
                    codeString.append(String.format("!(%s) && ", condition.getTitle()));
                }
            }

            codeString.replace(codeString.length() - 4, codeString.length(), "");

            codeString.append(") { \n");
            
            for (Action action : actions) {
                boolean actionValue = action.rules.get(i).getRuleActionValue();
                
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