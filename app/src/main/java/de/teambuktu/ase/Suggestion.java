package de.teambuktu.ase;

import java.util.List;

public class Suggestion {

    private final List<RuleRow> completenessSuggestions;
    private final int conditions;
    private final int rules;

    Suggestion(List<RuleRow> condSuggestions, int conditions, int rules) {
        this.completenessSuggestions = condSuggestions;
        this.conditions = conditions;
        this.rules = rules;
    }

    int getSuggestionSize() {
        return this.completenessSuggestions.size();
    }

    private String generateTemplate() {
        StringBuilder sb = new StringBuilder();

        sb.append("   \t");

        for (int i = 0; i < completenessSuggestions.size(); i++) {
            sb.append(String.format("R%d\t", this.rules + (i + 1)));
        }

        sb.append("\n");

        for (int k = 0; k < this.conditions; k++) {
            sb.append(String.format("B%d\t", k));
            for (int i = 0; i < this.completenessSuggestions.size(); i++) {
                sb.append(String.format("$%d%d\t", k, i));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private String fillTemplate(String template) {
        int k = 0;
        int i = 0;
        String retVal = template;

        for (RuleRow missingRow : completenessSuggestions) {
            for (Rule rule : missingRow.row) {
                String placeholder = String.format("$%d%d", k++, i);
                retVal = retVal.replace(placeholder, String.format("%s\t",
                        rule.getRuleConditionValue()));
            }
            k = 0;
            i++;
        }

        return retVal;
    }

    @Override
    public String toString() {
        String suggestions = "";

        if (getSuggestionSize() < 9) {
            String template = generateTemplate();
            suggestions = fillTemplate(template);
        }

        return suggestions;
    }
}
