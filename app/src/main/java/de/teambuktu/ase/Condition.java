package de.teambuktu.ase;

import java.util.ArrayList;

class Condition {
    protected String ID;
    protected String title;
    protected ArrayList<String> rules = new ArrayList<>();

    public Condition (String id, int ruleCount) {
        ID = id;
        for (int i = 0; i <= ruleCount; i++) {
            rules.add("-");
        }
    }
}
