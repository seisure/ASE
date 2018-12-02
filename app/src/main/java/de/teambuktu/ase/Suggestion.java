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

    private String preBuildTable() {
        int ruleCount = completenessSuggestions.get(0).row.size();
        StringBuilder sb = new StringBuilder();
        sb.append("<table><th></th>");

        for (int x = 0; x < ruleCount; x++) {
            sb.append(String.format("<th>R%d</th>", rules + (x + 1)));
        }

        for (int k = 0; k < conditions; k++) {
            sb.append(String.format("<tr><td>B%d: </td>", k));
            for (int i = 0; i < ruleCount; i++) {
                sb.append(String.format("<td>$%d%d</td>", k, i));
            }
            sb.append("</tr>");
        }

        sb.append("</table>");

        return sb.toString();
    }

    private String addTableData(String template) {

        int k = 0, i;
        String retVal = template;

        for (RuleRow missingRow: completenessSuggestions) {
            i = 0;
            for (Rule rule : missingRow.row) {
                String placeholder = String.format("$%d%d", k, i++);
                retVal = retVal.replace(placeholder, rule.getRuleConditionValue());
            }
            k++;
        }

        return retVal;
    }

    @Override
    public String toString() {
        String template = preBuildTable();

        return addTableData(template);
    }
}
