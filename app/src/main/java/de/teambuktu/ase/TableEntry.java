package de.teambuktu.ase;

import org.json.JSONException;
import org.json.JSONObject;

class TableEntry {
    private String title;

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONObject toJSON()  {
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
