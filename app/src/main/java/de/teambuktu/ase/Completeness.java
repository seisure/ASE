package de.teambuktu.ase;

import java.util.ArrayList;
import java.util.Arrays;

public class Completeness {

    static boolean isListComplete(ArrayList<Condition> conditionList) {
        int condCounter = conditionList.size();


        ArrayList<RuleRow> referenceRows = initializeList(condCounter);
        ArrayList<RuleRow> testRows = createList(conditionList);
        for (RuleRow row: referenceRows) {
            if (!testRows.contains(row)) {
                return false;
            }
        }

        return true;
    }

    private static ArrayList<RuleRow> createList(ArrayList<Condition> conditionList) {
        ArrayList<RuleRow> returnList = new ArrayList<>();
        for (int i = 0; i < conditionList.get(0).rules.size(); i++) {
            ArrayList<Rule> row = new ArrayList<>();
            for (Condition cond: conditionList) {
                row.add(cond.rules.get(i));
            }
            returnList.add(new RuleRow(row));
        }
        return returnList;
    }

    private static ArrayList<RuleRow> initializeList(int conditions) {
        ArrayList<RuleRow> referenzeRows  = new ArrayList<>();

        int powerOfCondition = (int)Math.pow(2,conditions);

        Rule trueRule = new Rule();
        trueRule.setRuleConditionValue("J");

        Rule falseRule = new Rule();
        falseRule.setRuleConditionValue("N");

        Rule[][] ruleMatrix = new Rule[powerOfCondition][conditions];

        int columnhalf = powerOfCondition;

        for (int k = 0; k < conditions; k++) {
            for (int i = 0; i < powerOfCondition; i++) {
                if (i % columnhalf < columnhalf / 2) {
                    ruleMatrix[i][k] = trueRule;
                } else if (i % columnhalf >= columnhalf / 2) {
                    ruleMatrix[i][k] = falseRule;
                }
            }
            columnhalf = columnhalf / 2;
        }

        for (int i = 0; i < powerOfCondition; i++) {
            referenzeRows.add(new RuleRow(new ArrayList<>(Arrays.asList(ruleMatrix[i]))));
        }

        return referenzeRows;
    }
}