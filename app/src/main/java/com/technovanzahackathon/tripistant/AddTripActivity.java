package com.technovanzahackathon.tripistant;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.technovanzahackathon.tripistant.TripChecklists.ChecklistFragment;
import com.technovanzahackathon.tripistant.TripChecklists.EditTripChecklistsActivity;
import com.technovanzahackathon.tripistant.TripChecklists.TripChecklists;
import com.technovanzahackathon.tripistant.TripChecklists.Trips;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pooja S on 12/24/2016.
 */

public class AddTripActivity extends AppCompatActivity{

    EditText tripname, srce, deste;
    Button date, time, submit;
    TextView txtDate, txtTime;
    Toolbar toolbar;
    DatabaseHelper databaseHelper;
    Trips note;
    private static final String EXTRA_NOTE = "EXTRA_CHECK";
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtripactivity);

        tripname = (EditText) findViewById(R.id.tripname);
        srce = (EditText) findViewById(R.id.sourcename);
        deste = (EditText) findViewById(R.id.destinationname);

        date = (Button) findViewById(R.id.date_btn);
        time = (Button) findViewById(R.id.time_btn);
        submit = (Button) findViewById(R.id.submit);

        txtDate = (TextView) findViewById(R.id.textDate);
        txtTime = (TextView) findViewById(R.id.textTime);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                note.setTripdatetimestatus(1);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time.setText(hourOfDay + ":" + minute);
                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.add_trip_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        note = (Trips) getIntent().getSerializableExtra(EXTRA_NOTE);

        if (note != null) {
            tripname.setText(note.getTripname());
            srce.setText(note.getTripsrc());
            deste.setText(note.getTripdest());
            note.getTripdate();
            note.getTriptime();
        } else {
            note = new Trips();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNoteFormOk() && (txtDate.getText().toString().length() <= 0) && (txtTime.getText().toString().length() <= 0)) {
                    setNoteResult();
                    finish();
                }
                if(isNoteFormOk() && (txtDate.getText().toString().length() > 0) && (txtTime.getText().toString().length() > 0)){
                    validateReminderDateTime();
                }
            }
        });

    }

    public void validateReminderDateTime(){
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

        String[] dateArray = date.split("-");
        int day = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1])-1;
        int year = Integer.parseInt(dateArray[2]);

        String[] timeArray = time.split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);

        Calendar current = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minutes, 00);

        if(calendar.compareTo(current) <= 0){
            Toast.makeText(getApplicationContext(), "Invalid Date/Time", Toast.LENGTH_SHORT).show();
        } else {
            setNoteResult();
            finish();
        }

    }


    private boolean isNoteFormOk() {
        String title=tripname.getText().toString();
        return !(title==null || title.trim().length()==0);
    }


    private void setNoteResult() {
        note.setTripname(tripname.getText().toString());
        note.setTripsrc(srce.getText().toString());
        note.setTripdest(deste.getText().toString());
        note.setTripdate(String.valueOf(new Date()));
        if(txtDate.getText().toString().length() <= 0 && txtTime.getText().toString().length() <= 0){
            note.setTripdate("");
            note.setTriptime("");
            note.setTripdatetimestatus(0);
        }else{
            note.setTripdate(txtDate.getText().toString());
            note.setTriptime(txtTime.getText().toString());
            note.setTripdatetimestatus(1);
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intent);
        Toast.makeText(AddTripActivity.this, "TripChecklists Saved.", Toast.LENGTH_LONG).show();
    }

    private void onBack() {
            if (isNoteFormOk()) {
                if ((tripname.getText().toString().equals(note.getTripname())) && (srce.getText().toString().equals(note.getTripsrc())) & (deste.getText().toString().equalsIgnoreCase(note.getTripdest()))) {
                    setResult(RESULT_CANCELED, new Intent());
                    finish();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Delete")
                            .setMessage("Do You Want to Delete the TripChecklist?")
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setNoteResult();
                                    finish();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setResult(RESULT_CANCELED, new Intent());
                                    finish();
                                }
                            })
                            .show();
                }
            }else {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
    }

    private void addNote(Intent data) {
        TripChecklists note = (TripChecklists) data.getSerializableExtra(EXTRA_NOTE);
        long noteId = databaseHelper.createTrip(note);
        note.setId(noteId);
    }

    @Override
    public void onBackPressed() {
        onBack();
        Intent intentHome = new Intent(AddTripActivity.this, MainActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentHome.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intentHome);
    }

}
