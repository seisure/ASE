package de.teambuktu.ase;

import java.util.ArrayList;

class Codegenerator {

    String generateCode(ArrayList<Condition> conditions, ArrayList<Action> actions) {
        StringBuilder codeString = new StringBuilder();
        int ruleCount = Utility.getRuleCount(conditions, actions);
        String title;

        for (int i = 0; i < ruleCount; i++) {

            codeString.append("if (");

            for (int j = 0; j < conditions.size(); j++) {
                Condition currentCondition = conditions.get(j);
                String conditionValue = currentCondition.rules.get(i).getRuleConditionValue();
                if (currentCondition.getTitle() == null) {
                    title = "B" + j;
                } else {
                    title = currentCondition.getTitle();
                }

                if (conditionValue.equals("J")) {
                    codeString.append(String.format("(%s)", title));
                    if (j < (conditions.size() - 1)) {
                        codeString.append(" && ");
                    }
                }
                if (conditionValue.equals("N")) {
                    codeString.append(String.format("!(%s)", title));
                    if (j < (conditions.size() - 1)) {
                        codeString.append(" && ");
                    }
                }
            }

            if (codeString.toString().endsWith("if (")) {
                codeString.append("true");
            } else if (codeString.toString().endsWith(" && ")) {
                codeString.replace(codeString.length() - 4, codeString.length(), "");
            }

            codeString.append(") { \n");

            for (int j = 0; j < actions.size(); j++) {
                Action currentAction = actions.get(j);
                boolean actionValue = currentAction.rules.get(i).getRuleActionValue();
                if (currentAction.getTitle() == null) {
                    title = "A" + j;
                } else {
                    title = currentAction.getTitle();
                }

                if (actionValue == true) {
                    codeString.append(String.format("%s();", title));
                    codeString.append("\n");
                }

            }

            codeString.append("} \n");
        }

        return codeString.toString();
    }

}