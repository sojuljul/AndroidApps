package com.example.salecalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button calcSecondBtn = (Button) findViewById(R.id.calcSecondBtn);
        calcSecondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText origPriceEditText = (EditText) findViewById(R.id.origPriceEditText);
                EditText salePriceEditText = (EditText) findViewById(R.id.salePriceEditText);
                TextView savingsTextView = (TextView) findViewById(R.id.savingsTextView);

                float orig = Float.parseFloat(origPriceEditText.getText().toString());
                float sale = Float.parseFloat(salePriceEditText.getText().toString());

                if (sale > orig) {
                    invalidDialog(view);
                    return;
                }

                float total = orig - sale;
                float finalTotal = (total / orig) * 100;

                DecimalFormat df = new DecimalFormat("0.00");

                savingsTextView.setText(String.valueOf(df.format(finalTotal)));

            }
        });

        Button clearSecondBtn = (Button) findViewById(R.id.clearSecondBtn);
        clearSecondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText origPriceEditText = (EditText) findViewById(R.id.origPriceEditText);
                EditText salePriceEditText = (EditText) findViewById(R.id.salePriceEditText);
                TextView savingsTextView = (TextView) findViewById(R.id.savingsTextView);

                origPriceEditText.setText("");
                salePriceEditText.setText("");
                savingsTextView.setText("0");
            }
        });


        Button saleBtn = (Button) findViewById(R.id.saleBtn);
        saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(startIntent);
            }
        });
    }

    public void invalidDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ERROR: Invalid Price")
                .setMessage("Sale price must be lower than original price.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
