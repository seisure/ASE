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

    @Override
    public boolean equals(Object e) {
        if (this == e) {
            return true;
        }
        if (e == null) {
            return false;
        }
        if (e.getClass() != Rule.class) {
            return false;
        }
        Rule that = (Rule)e;
        if ((that.ruleConditionValue == null)
                && (this.ruleConditionValue == null)
                && (this.ruleActionValue == that.ruleActionValue)) {
            return true;
        } else if ((that.ruleActionValue == false)
                && (this.ruleActionValue == false)
                && this.ruleConditionValue.equals(that.ruleConditionValue)) {
            return true;
        } else if ((that.ruleActionValue == false)
                && (this.ruleActionValue == false)
                && (this.ruleConditionValue.equals("-") || (that.ruleConditionValue.equals("-")))) {
            return true;
        } else {
            return false;
        }
    }
}
