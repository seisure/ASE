package de.teambuktu.ase;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

abstract class TableEntry {
    private String title;

    protected ArrayList<Rule> rules = new ArrayList<>();

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONObject toJson()  {
        try {
            JSONObject json = new JSONObject();
            json.put("hash", hashCode());
            json.put("title", this.title);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
