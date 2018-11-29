package de.teambuktu.ase;

import java.util.List;

public class Suggestion {

    List<RuleRow> completenessSuggestions;

    public Suggestion(List<RuleRow> condSuggestions) {
        this.completenessSuggestions = condSuggestions;
    }

    @Override
    public String toString() {

        String retVal = "";
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Fehlende Bedingungen: \n\n");

        for (RuleRow missingRow : completenessSuggestions) {
            stringBuilder.append("B:  ");
            for (Rule rule : missingRow.row){
                stringBuilder.append(String.format("%-3s",rule.getRuleConditionValue()));
            }
            stringBuilder.append("\n");
            retVal += stringBuilder.toString();
            stringBuilder = new StringBuilder();
        }

        return retVal;
    }
}
