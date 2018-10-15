package de.teambuktu.ase;

import java.util.ArrayList;

class Action {
    protected String ID;
    protected String title;
    protected ArrayList<Boolean> rules = new ArrayList<>();

    public Action (String id, int ruleCount) {
        ID = id;
        for (int i = 0; i <= ruleCount; i++) {
            rules.add(false);
        }
    }
}
