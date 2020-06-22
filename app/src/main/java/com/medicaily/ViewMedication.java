package com.medicaily;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewMedication extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText editTextName;
    private EditText editTextType;
    private EditText editTextDosage;
    private EditText editTextInterval;
    private EditText editTextTime;
    private EditText editTextInstruction;
    private EditText editTextStart;
    private EditText editTextEnd;
    private EditText editTextRemain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medication);

        Toolbar toolbar = findViewById(R.id.reminder_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medication");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String medicationID = intent.getStringExtra(MainActivity.Medication_ID);

        editTextName = findViewById(R.id.edit_text_medication_name);
        editTextType = findViewById(R.id.edit_text_medication_type);
        editTextDosage = findViewById(R.id.edit_text_medication_dosage);
        editTextInterval = findViewById(R.id.edit_text_medication_interval);
        editTextInstruction = findViewById(R.id.edit_text_medication_instruction);
        editTextStart = findViewById(R.id.edit_text_medication_start_from);
        editTextEnd = findViewById(R.id.edit_text_medication_end);
        editTextEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        editTextTime = findViewById(R.id.edit_text_medication_time);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        editTextRemain = findViewById(R.id.edit_text_medication_remaining);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference medicationRef = db.collection("medication").document(medicationID);
        medicationRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String type = documentSnapshot.getString("type");
                            String dosage = documentSnapshot.getString("dosage");
                            String interval = documentSnapshot.getString("interval");
                            String instruction = documentSnapshot.getString("instruction");
                            String start = documentSnapshot.getString("start");
                            String end = documentSnapshot.getString("end");
                            String time = documentSnapshot.getString("time");
                            String remain = documentSnapshot.getString("remain");

                            editTextName.setText(name);
                            editTextType.setText(type);
                            editTextDosage.setText(dosage);
                            editTextInterval.setText(interval);
                            editTextInstruction.setText(instruction);
                            editTextStart.setText(start);
                            editTextEnd.setText(end);
                            editTextTime.setText(time);
                            editTextRemain.setText(remain);
                        } else {
                            Toast.makeText(ViewMedication.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewMedication.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                updateMedication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        String currentTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        editTextTime.setText(currentTimeString);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        editTextEnd.setText(currentDateString);
    }

    private void updateMedication() {
        String name = editTextName.getText().toString();
        String type = editTextType.getText().toString();
        String dosage = editTextDosage.getText().toString();
        String time = editTextTime.getText().toString();
        String interval = editTextInterval.getText().toString();
        String instruction = editTextInstruction.getText().toString();
        String start = editTextStart.getText().toString();
        String end = editTextEnd.getText().toString();
        String remain = editTextRemain.getText().toString();

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert an medicine name", Toast.LENGTH_SHORT).show();
        } else if (dosage.isEmpty()) {
            Toast.makeText(this, "Please insert a dosage", Toast.LENGTH_SHORT).show();
        } else if (time.isEmpty()) {
            Toast.makeText(this, "Please insert a time", Toast.LENGTH_SHORT).show();
        } else if (interval.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a interval such as weekly or daily", Toast.LENGTH_SHORT).show();
        } else if (instruction.isEmpty()) {
            Toast.makeText(this, "Please insert an instruction on how to take the medicine", Toast.LENGTH_SHORT).show();
        } else if (start.isEmpty()) {
            Toast.makeText(this, "Please insert a start date", Toast.LENGTH_SHORT).show();
        } else if (end.isEmpty()) {
            Toast.makeText(this, "Please insert a end date", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Intent intent = getIntent();
            final String medicationID = intent.getStringExtra(MainActivity.Medication_ID);
            DocumentReference medicationRef = db.collection("medication").document(medicationID);
            medicationRef.update("name", name);
            medicationRef.update("type", type);
            medicationRef.update("dosage", dosage);
            medicationRef.update("time", time);
            medicationRef.update("interval", interval);
            medicationRef.update("instruction", instruction);
            medicationRef.update("start", start);
            medicationRef.update("end", end);
            medicationRef.update("remain", remain);
            Toast.makeText(this, "Medication updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
