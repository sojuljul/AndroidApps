package com.example.episoderecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText titleEditText;
    EditText currentEpEditText;
    EditText totalEpEditText;
    Button addButton;

    DatabaseReference databaseShows;

    ListView listViewShows;
    List<Show> showList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseShows = FirebaseDatabase.getInstance().getReference("shows");

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        currentEpEditText = (EditText) findViewById(R.id.currentEpEditText);
        totalEpEditText = (EditText) findViewById(R.id.totalEpEditText);
        addButton = (Button) findViewById(R.id.addButton);

        listViewShows = (ListView) findViewById(R.id.listViewShows);

        showList = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShow();
            }
        });

        listViewShows.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Show show = showList.get(i);

                showUpdateDialog(show.getShowId(), show.getTitle(), show.getCurrentEp(), show.getTotalEp());

                return true;
            }
        });
    }

    private void showUpdateDialog(final String showId, final String showTitle, int currentEp, final int totalEp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        builder.setView(dialogView);

        final EditText currentEpEditText = (EditText) dialogView.findViewById(R.id.currentEpEditText);
        final Button updateBtn = (Button) dialogView.findViewById(R.id.updateBtn);
        final Button deleteBtn = (Button) dialogView.findViewById(R.id.deleteBtn);
        final Button plusBtn = (Button) dialogView.findViewById(R.id.plusBtn);
        final Button minusBtn = (Button) dialogView.findViewById(R.id.minusBtn);

        currentEpEditText.setText(String.valueOf(currentEp));
        currentEpEditText.setSelection(currentEpEditText.getText().length());

        builder.setTitle("Updating: " + showTitle);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentText = currentEpEditText.getText().toString();

                if (TextUtils.isEmpty(currentText)) {
                    currentEpEditText.setError("Current episode # required");
                    return;
                }

                int currentEp = Integer.parseInt(currentText);
                updateShow(showId, showTitle, currentEp, totalEp);

                alertDialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteShow(showId);
                alertDialog.dismiss();
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(currentEpEditText.getText().toString());
                count++;

                if (count > totalEp) {
                    count = totalEp;
                }

                currentEpEditText.setText(String.valueOf(count));
                currentEpEditText.setSelection(currentEpEditText.getText().length());
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(currentEpEditText.getText().toString());
                count--;

                if (count < 0) {
                    count = 0;
                }

                currentEpEditText.setText(String.valueOf(count));
                currentEpEditText.setSelection(currentEpEditText.getText().length());
            }
        });

    }

    private void deleteShow(String showId) {
        DatabaseReference drShow = FirebaseDatabase.getInstance().getReference("shows").child(showId);

        drShow.removeValue();

        Toast.makeText(this, "Show removed successfully", Toast.LENGTH_LONG).show();
    }

    private boolean updateShow(String id, String title, int currentEp, int totalEp) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shows").child(id);

        Show show = new Show(id, title, currentEp, totalEp);

        databaseReference.setValue(show);
        Toast.makeText(this, "Show updated successfully", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseShows.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showList.clear();

                for (DataSnapshot showSnapshot : dataSnapshot.getChildren()) {
                    Show show = showSnapshot.getValue(Show.class);
                    showList.add(show);
                }

                ShowList adapter = new ShowList(MainActivity.this, showList);
                listViewShows.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addShow() {
        String title = titleEditText.getText().toString().trim();
        String currentEpStr = currentEpEditText.getText().toString();
        String totalEpStr = totalEpEditText.getText().toString();

        if (!TextUtils.isEmpty((title)) && !TextUtils.isEmpty(currentEpStr) && !TextUtils.isEmpty(totalEpStr)) {

            int currentEp = Integer.parseInt(currentEpStr);
            int totalEp = Integer.parseInt(totalEpStr);

            if (currentEp > totalEp) {
                Toast.makeText(this, "Current episode count cannot be greater than total count", Toast.LENGTH_LONG).show();
                return;
            }

            String id = databaseShows.push().getKey();

            Show show = new Show(id, title, currentEp, totalEp);
            databaseShows.child(id).setValue(show);

            Toast.makeText(this, "Show added successfully", Toast.LENGTH_LONG).show();

            titleEditText.setText("");
            currentEpEditText.setText("");
            totalEpEditText.setText("");

        }
        else {
            Toast.makeText(this, "All fields required", Toast.LENGTH_LONG).show();
        }
    }
}
