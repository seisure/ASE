package de.teambuktu.ase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

import java.util.List;

public class Notification {

    private Suggestion suggestion;

    public Notification() {

    }

    public void addSuggestions(List<RuleRow> suggestions, int conditionSize, int rules) {
        if (!suggestions.isEmpty()) {
            suggestion = new Suggestion(suggestions, conditionSize, rules);
        }
    }

    public boolean isEmpty() {
        return suggestion == null;
    }

    public Dialog createWarningDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.warningDialogTitle);
        builder.setMessage(R.string.warningDialogMessage);
        TextView textView = new TextView(context);
        textView.setText(suggestion.toString());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        builder.setView(textView);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
