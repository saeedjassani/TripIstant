package com.technovanzahackathon.tripistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pooja S on 12/24/2016.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{

    View mainView;
    Context context;

    Button btnyes1, btnyes2, btnyes3, btnyes4, btnno1, btnno2, btnno3, btndismiss1, btndismiss2, btndismiss3, btndismiss4, btncheck, btnexpense, btnalarm, btnphoto, btnminidi, btncab;
    String date, time;
    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_layout, container, false);
        context = mainView.getContext();

        date = getArguments().getString("someInt");
        time = getArguments().getString("someInt1");

        btnyes1 = (Button) mainView.findViewById(R.id.buttonyes1);
        btnyes2 = (Button) mainView.findViewById(R.id.buttonyes2);
        btnyes3 = (Button) mainView.findViewById(R.id.buttonyes3);
        btnyes4 = (Button) mainView.findViewById(R.id.buttonyes4);
        btnno1 = (Button) mainView.findViewById(R.id.buttonno1);
        btnno2 = (Button) mainView.findViewById(R.id.buttonno2);
        btnno3 = (Button) mainView.findViewById(R.id.buttonno3);
        btndismiss1 = (Button) mainView.findViewById(R.id.buttondismiss);
        btndismiss2 = (Button) mainView.findViewById(R.id.buttondismiss2);
        btndismiss3 = (Button) mainView.findViewById(R.id.buttondismiss3);
        btndismiss4 = (Button) mainView.findViewById(R.id.buttondismiss4);
        btncheck = (Button) mainView.findViewById(R.id.buttoncheck);
        btnexpense = (Button) mainView.findViewById(R.id.buttonexpense);
        btnalarm = (Button) mainView.findViewById(R.id.buttonalarm);
        btnphoto = (Button) mainView.findViewById(R.id.buttonphoto);
        btnminidi = (Button) mainView.findViewById(R.id.buttonmindicator);
        btncab = (Button) mainView.findViewById(R.id.buttoncab);


        String[] dateArray = date.split("-");
        int day = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1])-1;
        int year = Integer.parseInt(dateArray[2]);

        String[] timeArray = time.split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);

        Calendar current = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 12, 25, 06, 13, 00);
        if(current.compareTo(calendar) == 0){
        }
        calendar.set(year, month, day-3, 12, 03, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Shopping", "Have You Done your Shopping for the Trip?");
        }
        calendar.set(year, month, day-3, 15, 37, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Medication", "Have You Taken Necessary Medications for the Trip?");
        }
        calendar.set(year, month, day-2, 15, 25, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Plan", "Have You Planned About What To Do on the Trip?");
        }
        calendar.set(year, month, day-2, 19, 40, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Expense", "Plan the Expense for the Trip?");
        }
        calendar.set(year, month, day-1, 21, 40, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Packing", "Have You Packed the Bags for the Trip?");
        }
        calendar.set(year, month, day-1, 23, 00, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Sleep", "Have a Deep Sleep for a Fresh Morning.");
        }
        calendar.set(year, month, day, hour-3, 03, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Photos", "Take Photos of Documents and Luggage");
        }
        calendar.set(year, month, day, hour-2, 10, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Tickets", "Do Not Forget to get your Journe Tickets. Again, Check Your Luggage and Documents.");
        }
        calendar.set(year, month, day, hour-1, 10, 00);
        if(current.compareTo(calendar) == 0){
            setAlarm(calendar, "Cab", "Book a Cab or Check Timings on M-Indicator");
        }

        return mainView;
    }

    private void setAlarm(Calendar calendar, String title, String content) {
        Date date = calendar.getTime();
        Toast.makeText(getActivity(), "Reminder Set for"+ DATETIME_FORMAT.format(date), Toast.LENGTH_SHORT).show();
        int RQS = (int) System.currentTimeMillis();
        Intent intent = new Intent(getActivity(), MyReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("code", RQS);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), RQS, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onClick(View view) {

    }
}