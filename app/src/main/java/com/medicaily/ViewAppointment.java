package com.medicaily;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewAppointment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText editTextTitle;
    private EditText editTextDate;
    private EditText editTextTime;
    private EditText editTextLocation;
    private EditText editTextContactNumber;
    private EditText editTextReminder;
    private EditText editTextNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        Toolbar toolbar = findViewById(R.id.reminder_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String appointmentID = intent.getStringExtra(MainActivity.Appointment_ID);

        editTextTitle = findViewById(R.id.edit_text_appointment_title);
        editTextDate = findViewById(R.id.edit_text_appointment_date);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        editTextTime = findViewById(R.id.edit_text_appointment_time);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        editTextLocation = findViewById(R.id.edit_text_appointment_location);
        editTextContactNumber = findViewById(R.id.edit_text_appointment_contact_number);
        editTextReminder = findViewById(R.id.edit_text_appointment_reminder);
        editTextNote = findViewById(R.id.edit_text_appointment_note);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference appointmentRef = db.collection("appointment").document(appointmentID);
        appointmentRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String title = documentSnapshot.getString("title");
                            String date = documentSnapshot.getString("date");
                            String time = documentSnapshot.getString("time");
                            String location = documentSnapshot.getString("location");
                            String contactNumber = documentSnapshot.getString("contact_number");
                            String reminder = documentSnapshot.getString("reminder");
                            String note = documentSnapshot.getString("note");

                            editTextTitle.setText(title);
                            editTextDate.setText(date);
                            editTextTime.setText(time);
                            editTextLocation.setText(location);
                            editTextContactNumber.setText(contactNumber);
                            editTextReminder.setText(reminder);
                            editTextNote.setText(note);
                        } else {
                            Toast.makeText(ViewAppointment.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewAppointment.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
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
        editTextDate.setText(currentDateString);
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
                updateAppointment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateAppointment() {
        String title = editTextTitle.getText().toString();
        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String location = editTextLocation.getText().toString();
        String contactNumber = editTextContactNumber.getText().toString();
        String reminder = editTextReminder.getText().toString();
        String note = editTextNote.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert an appointment title", Toast.LENGTH_SHORT).show();
        } else if (date.isEmpty()) {
            Toast.makeText(this, "Please insert a date", Toast.LENGTH_SHORT).show();
        } else if (time.isEmpty()) {
            Toast.makeText(this, "Please insert a time", Toast.LENGTH_SHORT).show();
        } else if (location.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a location", Toast.LENGTH_SHORT).show();
        } else if (contactNumber.isEmpty()) {
            Toast.makeText(this, "Please insert a contact number", Toast.LENGTH_SHORT).show();
        } else if (reminder.isEmpty()) {
            Toast.makeText(this, "Please insert a reminder", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Intent intent = getIntent();
            final String appointmentID = intent.getStringExtra(MainActivity.Appointment_ID);
            DocumentReference appointmentRef = db.collection("appointment").document(appointmentID);
            appointmentRef.update("title", title);
            appointmentRef.update("date", date);
            appointmentRef.update("time", time);
            appointmentRef.update("location", location);
            appointmentRef.update("contact_number", contactNumber);
            appointmentRef.update("reminder", reminder);
            appointmentRef.update("note", note);
            Toast.makeText(this, "Appointment updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
