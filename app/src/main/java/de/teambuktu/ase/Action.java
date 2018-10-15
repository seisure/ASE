package de.teambuktu.ase;

import java.util.ArrayList;

class Action {
    protected String ID;
    protected String title;
    protected ArrayList<Boolean> rules = new ArrayList<>();

    public Action (String id, int ruleCount) {
        ID = id;
        for (int i = 0; i < ruleCount; i++) {
            rules.add(false);
        }
    }

    protected void setNumberOfRules(int count) {
        if (count > rules.size()) {
            for (int i = rules.size() - 1; i < count - 1; i++) {
                rules.add(false);
            }
        }
        else if (count < rules.size()) {
            for (int i = rules.size() - 1; i >= count; i--) {
                rules.remove(i);
            }
        }
    }
}
