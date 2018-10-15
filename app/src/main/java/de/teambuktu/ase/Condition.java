package de.teambuktu.ase;

import java.util.ArrayList;

class Condition {
    protected String ID;
    protected String title;
    protected ArrayList<String> rules = new ArrayList<>();

    public Condition (String id, int ruleCount) {
        ID = id;
        for (int i = 0; i < ruleCount; i++) {
            rules.add("-");
        }
    }

    protected void setNumberOfRules(int count) {
        if (count > rules.size()) {
            for (int i = rules.size() - 1; i < count - 1; i++) {
                rules.add("-");
            }
        }
        else if (count < rules.size()) {
            for (int i = rules.size() - 1; i >= count; i--) {
                rules.remove(i);
            }
        }
    }
}
