package com.technovanzahackathon.tripistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pooja S on 12/24/2016.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

	View mainView;
	Context context;

	Button btnyes1, btnyes2, btnyes3, btnyes4, btnno1, btnno2, btnno3, btndismiss1, btndismiss2, btndismiss3, btndismiss4, btncheck, btnexpense, btnalarm, btnphoto, btnminidi, btncab;
	RelativeLayout rl1, rl2, rl3, rl4, rl5, rl6, rl7, rl8, rl9, rl10;
	String date, time;
	private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_layout, container, false);
		context = mainView.getContext();

		date = getArguments().getString("someInt");
		time = getArguments().getString("someInt1");

		rl1 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout1);
		rl2 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout2);
		rl3 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout3);
		rl4 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout4);
		rl5 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout5);
		rl6 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout6);
		rl7 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout7);
		rl8 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout8);
		rl9 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout9);
		rl10 = (RelativeLayout) mainView.findViewById(R.id.relativeLayout10);


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
		int month = Integer.parseInt(dateArray[1]) - 1;
		int year = Integer.parseInt(dateArray[2]);

		Log.d("Dev110", "onCreateView: " + day);

		String[] timeArray = time.split(":");
		int hour = Integer.parseInt(timeArray[0]);
		int minutes = Integer.parseInt(timeArray[1]);

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day - 3, hour + 1, minutes + 5, 0);
		setAlarm(calendar, "Shopping", "Have You Done your Shopping for the Trip?", 8);
		calendar.set(year, month, day - 3, hour + 3, minutes, 0);
		setAlarm(calendar, "Medication", "Have You Taken Necessary Medications for the Trip?", 7);
		calendar.set(year, month, day - 2, hour + 1, minutes, 0);
		setAlarm(calendar, "Plan", "Have You Planned About What To Do on the Trip?", 6);
		calendar.set(year, month, day - 2, hour + 4, minutes, 0);
		setAlarm(calendar, "Expense", "Plan the Expense for the Trip?", 4);
		calendar.set(year, month, day - 1, hour + 6, minutes, 0);
		setAlarm(calendar, "Packing", "Have You Packed the Bags for the Trip?", 3);
		calendar.set(year, month, day - 1, hour + 10, minutes, 0);
		setAlarm(calendar, "Sleep", "Have a Deep Sleep for a Fresh Morning.", 2);
		calendar.set(year, month, day, hour - 3, minutes, 0);
		setAlarm(calendar, "Photos", "Take Photos of Documents and Luggage", 1);
		calendar.set(year, month, day, hour - 2, minutes, 0);
		setAlarm(calendar, "Tickets", "Do not forget to get your Journey tickets. Again, check your luggage and documents.", 5);
		calendar.set(year, month, day, hour - 1, minutes, 0);
		setAlarm(calendar, "Cab", "Book a Cab or Check Timings on M-Indicator", 0);
		calendar.set(year, month, day, hour + 1, minutes, 0);
		setAlarm(calendar, "Happy Journey", "Happy Journey.Enjoy the Trip", 9);

		CardView sightsCard = (CardView) mainView.findViewById(R.id.cardviewn);
		sightsCard.setOnClickListener(this);

		CardView photosCard = (CardView) mainView.findViewById(R.id.cardview_photos);
		photosCard.setOnClickListener(this);

		return mainView;
	}

	private void setAlarm(Calendar calendar, String title, String content, int notification_id) {
		Date date = calendar.getTime();
//		Toast.makeText(getActivity(), "Reminder Set for" + DATETIME_FORMAT.format(date), Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getActivity(), MyReceiver.class);
		intent.putExtra("title", title);
		intent.putExtra("content", content);
		intent.putExtra("code", notification_id);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), notification_id, intent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.cardviewn:
				startActivity(new Intent(getActivity(), SightsActivity.class));
				break;
			case R.id.cardview_photos:
				startActivity(new Intent(getActivity(), PhotosActivity.class));
				break;
		}

	}
}