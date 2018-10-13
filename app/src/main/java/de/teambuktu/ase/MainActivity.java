package de.teambuktu.ase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int actionCount = 0;
    int conditionCount = 0;

    private void addRow(boolean action) {
        TableLayout table;

        if (action) {
            table = findViewById(R.id.tableAction);
        }
        else {
            table = findViewById(R.id.tableCondition);
        }
        TableRow row = new TableRow(this);
        TextView column1 = new TextView(this);
        EditText column2 = new EditText(this);

        if (action) {
            row.setId(actionCount++);
            column1.setText("A" + actionCount);
        }
        else {
            row.setId(conditionCount++);
            column1.setText("B" + conditionCount);
        }

        column1.setIncludeFontPadding(true);
        column1.setPadding(10,0,0,0);
        row.addView(column1);

        column2.setHint(action ? "Aktion" : "Bedingung");
        column2.setEms(8);
        row.addView(column2);

        table.addView(row);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAddActionRow = findViewById(R.id.buttonAddActionRow);
        buttonAddActionRow.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addRow(true);
            }
        });

        Button buttonAddConditionRow = findViewById(R.id.buttonAddConditionRow);
        buttonAddConditionRow.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addRow(false);
            }
        });
    }
}
