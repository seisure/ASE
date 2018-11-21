package de.teambuktu.ase;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utility {

    static boolean isListComplete(List<Condition> conditionList) {
        int condCounter = conditionList.size();


        List<RuleRow> referenceRows = initializeList(condCounter);
        List<RuleRow> testRows = createList(conditionList);
        for (RuleRow row: referenceRows) {
            if (!testRows.contains(row)) {
                return false;
            }
        }

        return true;
    }

    /*
        The first parameter has to be the conditions, the second has to be the actions.
     */
    static List<Pair<Integer,Integer>> testForConsistency(List<? extends TableEntry> conditionList,
                                                          List<? extends TableEntry> actionList) {

        List<RuleRow> conditionRows = createList(conditionList);
        List<RuleRow> actionRows = createList(actionList);

        List<Pair<Integer,Integer>> badRows = new ArrayList<>();

        for (int i = 0; i < conditionRows.size(); i++) {
            for (int k = i + 1; k < conditionRows.size(); k++) {
                if (conditionRows.get(i).equals(conditionRows.get(k))) {
                    if (!(actionRows.get(i).equals(actionRows.get(k)))) {
                        badRows.add(new Pair<>(i,k));
                    }
                }
            }
        }
        return badRows;
    }

    private static List<RuleRow> createList(List<? extends TableEntry> originList) {
        List<RuleRow> returnList = new ArrayList<>();
        for (int i = 0; i < originList.get(0).rules.size(); i++) {
            List<Rule> row = new ArrayList<>();
            for (TableEntry cond: originList) {
                row.add(cond.rules.get(i));
            }
            returnList.add(new RuleRow(row));
        }
        return returnList;
    }

    private static List<RuleRow> initializeList(int conditions) {
        List<RuleRow> referenzeRows  = new ArrayList<>();

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