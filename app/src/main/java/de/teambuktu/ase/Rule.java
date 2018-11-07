package de.teambuktu.ase;

public class Rule {
    private String ruleConditionValue;
    private boolean ruleActionValue;

    public String getRuleConditionValue() {
        return ruleConditionValue;
    }

    public boolean getRuleActionValue() {
        return ruleActionValue;
    }

    public void setRuleConditionValue(String ruleConditionValue) {
        this.ruleConditionValue = ruleConditionValue;
    }

    public void setRuleActionValue(boolean ruleActionValue) {
        this.ruleActionValue = ruleActionValue;
    }
}
