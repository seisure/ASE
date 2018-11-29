package de.teambuktu.ase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

public class Notification {

    private Suggestion suggestion;

    public Notification() {

    }

    public void addSuggestions(List<RuleRow> suggestions) {
        if (!suggestions.isEmpty()) {
            suggestion = new Suggestion(suggestions);
        }
    }

    public boolean isEmpty() {
        return suggestion == null;
    }

    public Dialog createWarningDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bedingungen unvollständig");
        builder.setMessage(suggestion.toString());
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
