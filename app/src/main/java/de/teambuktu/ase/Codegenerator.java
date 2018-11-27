package de.teambuktu.ase;

import java.util.ArrayList;

class Codegenerator {

    String generateCode(ArrayList<Condition> conditions, ArrayList<Action> actions) {
        StringBuilder codeString = new StringBuilder();
        int ruleCount = Utility.getRuleCount(conditions, actions);

        for (int i = 0; i < ruleCount; i++) {

            codeString.append("if (");

            for (int j = 0; j < conditions.size(); j++) {
                Condition currentCondition = conditions.get(j);
                String conditionValue = currentCondition.rules.get(i).getRuleConditionValue();

                if (conditionValue.equals("J"))
                {
                    codeString.append(String.format("(%s)", currentCondition.getTitle()));
                    if (j < (conditions.size() - 1)) {
                        codeString.append(String.format(" && ", currentCondition.getTitle()));
                    }
                }
                if (conditionValue.equals("N"))
                {
                    codeString.append(String.format("!(%s)", currentCondition.getTitle()));
                    if (j < (conditions.size() - 1)) {
                        codeString.append(String.format(" && ", currentCondition.getTitle()));
                    }
                }
                if (conditionValue.equals("-"))
                {
                    codeString.append(String.format("true", currentCondition.getTitle()));
                }
            }

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