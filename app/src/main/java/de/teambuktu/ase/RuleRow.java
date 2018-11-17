package de.teambuktu.ase;

import java.util.ArrayList;

public class RuleRow {
    ArrayList<Rule> row;

    public RuleRow(ArrayList<Rule> row) {
        this.row = row;
    }

    public int size() {
        return row.size();
    }

    @Override
    public boolean equals(Object e) {
        if (this == e) {
            return true;
        }
        if (e == null) {
            return false;
        }
        if (e.getClass() != RuleRow.class) {
            return false;
        }
        RuleRow that = (RuleRow)e;

        if (that.size() != this.row.size()) {
            return false;
        }
        for (int index = 0; index < this.row.size(); index++) {
            if (!(this.row.get(index).equals(that.row.get(index)))) {
                return false;
            }
        }
        return true;
    }
}
