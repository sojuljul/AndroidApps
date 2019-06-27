package com.example.salecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button calcBtn = (Button) findViewById(R.id.calcBtn);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNumEditText = (EditText) findViewById(R.id.origPriceEditText);
                EditText percentEditText = (EditText) findViewById(R.id.percentEditText);
                EditText taxEditText = (EditText) findViewById(R.id.taxEditText);
                TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

                if (firstNumEditText.getText().toString().equals("")) {
                    emptyDialog(view);
                    return;
                }

                float taxNum;

                if (taxEditText.getText().toString().equals("")) {
                    taxNum = 0;
                }
                else {
                    taxNum = Float.parseFloat(taxEditText.getText().toString());
                }

                int percent;
                if (percentEditText.getText().toString().equals("")) {
                    percent = 0;
                }
                else {
                    percent = Integer.parseInt(percentEditText.getText().toString());
                }

                float num = Float.parseFloat(firstNumEditText.getText().toString());

                if (percent > 100) {
                    openDialog(view);
                    return;
                }

                float sale = (num * percent) / 100;

                float total = num - sale;

                float taxVal = (total * taxNum) / 100;
                total += taxVal;

                DecimalFormat df = new DecimalFormat("0.00");
                resultTextView.setText(String.valueOf(df.format(total)));
            }
        });

        Button clearBtn = (Button) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNumEditText = (EditText) findViewById(R.id.origPriceEditText);
                EditText percentEditText = (EditText) findViewById(R.id.percentEditText);
                EditText taxEditText = (EditText) findViewById(R.id.taxEditText);
                TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

                firstNumEditText.setText("");
                percentEditText.setText("");
                taxEditText.setText("");
                resultTextView.setText("0.00");
            }
        });

        Button savingBtn = (Button) findViewById(R.id.savingBtn);
        savingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(startIntent);
            }
        });
    }

    public void emptyDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ERROR: Empty")
                .setMessage("The price field is required.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("ERROR: Out of Bounds")
                .setMessage("The sale percentage must be between 0 to 100.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
