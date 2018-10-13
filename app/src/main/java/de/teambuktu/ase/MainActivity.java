package de.teambuktu.ase;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int rowCount = 0;

    private void addRow() {
        TableLayout table = findViewById(R.id.table);
        TableRow row = new TableRow(this);
        row.setId(rowCount++);
        row.setBottom(2);
        TextView col1 = new TextView(this);
        col1.setText("A" + rowCount);
        col1.setIncludeFontPadding(true);
        col1.setPaddingRelative(50,50,0,50);
        row.addView(col1);
        EditText col2 = new EditText(this);
        col2.setHint("Aktion");
        row.addView(col2);
        table.addView(row);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAddRow = findViewById(R.id.buttonAddRow);
        buttonAddRow.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addRow();
            }
        });
    }
}
