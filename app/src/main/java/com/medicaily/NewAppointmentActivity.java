package com.medicaily;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class NewAppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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
        setContentView(R.layout.activity_new_appointment);

        Toolbar toolbar = findViewById(R.id.reminder_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add an appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextTitle = findViewById(R.id.edit_text_appointment_title);
        editTextDate = findViewById(R.id.edit_text_appointment_date);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker" );
            }
        });
        editTextTime = findViewById(R.id.edit_text_appointment_time);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker" );
            }
        });
        editTextLocation = findViewById(R.id.edit_text_appointment_location);
        editTextContactNumber = findViewById(R.id.edit_text_appointment_contact_number);
        editTextReminder = findViewById(R.id.edit_text_appointment_reminder);
        editTextNote = findViewById(R.id.edit_text_appointment_note);
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
                saveAppointment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveAppointment() {
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
            CollectionReference appointmentRef = FirebaseFirestore.getInstance()
                    .collection("appointment");
            appointmentRef.add(new Appointment(title, date, time, location, contactNumber, reminder, note, FirebaseAuth.getInstance().getCurrentUser().getUid()));
            Toast.makeText(this, "Appointment added", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
