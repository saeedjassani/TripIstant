package com.technovanzahackathon.tripistant;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.technovanzahackathon.tripistant.Expense.ExpenseActivity;
import com.technovanzahackathon.tripistant.TripChecklists.ChecklistFragment;

/**
 * Created by Pooja S on 12/25/2016.
 */
public class MyNewIntentService extends IntentService {

	private static final int NOTIFICATION_ID = 5;

	public MyNewIntentService() {
		super("Notes");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		String title = intent.getStringExtra("rem_title");
		String content = intent.getStringExtra("rem_content");
		int noticode = intent.getExtras().getInt("code");

		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
				.setHintHideIcon(true)
				.setContentIcon(R.drawable.ic_menu_share);

		if (title.equals("Shopping")) {
			Intent sendIntent = new Intent();
			PendingIntent pIntent = PendingIntent.getActivity(MyNewIntentService.this, 0, sendIntent, 0);
			builder.addAction(0, "NO", pIntent);
		} else if (title.equals("Medication")) {
			builder.addAction(0, "NO", null);
		} else if (title.equals("Plan")) {
			Intent sendIntent = new Intent(getApplicationContext(), ChecklistFragment.class);
			PendingIntent pIntent = PendingIntent.getActivity(MyNewIntentService.this, 0, sendIntent, 0);
			builder.addAction(0, "CHECKLIST", pIntent);
		} else if (title.equals("Expense")) {
			Intent sendIntent = new Intent(getApplicationContext(), ExpenseActivity.class);
			PendingIntent pIntent = PendingIntent.getActivity(MyNewIntentService.this, 0, sendIntent, 0);
			builder.addAction(0, "Expense", pIntent);
		} else if (title.equals("Packing")) {
			builder.addAction(0, "NO", null);
		} else if (title.equals("Sleep")) {
			builder.addAction(0, "NO", null);
		} else if (title.equals("Photos")) {
			Intent cameraIntent = new Intent(Intent.ACTION_VIEW, null);
			PendingIntent pIntent = PendingIntent.getActivity(MyNewIntentService.this, 0, cameraIntent, 0);
			builder.addAction(0, "Expense", pIntent);
		} else if (title.equals("Cab")) {
			builder.addAction(0, "Cab", null);
		}

		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.ic_menu_share);
		builder.setSound(soundUri);
		builder.extend(wearableExtender);

		NotificationCompat.InboxStyle bigNote = new NotificationCompat.InboxStyle();
		bigNote.setBigContentTitle(title);
		String[] nc1 = content.split("\\r?\\n");
		for (String aNc1 : nc1) {
			bigNote.addLine(aNc1);
		}
		builder.setStyle(bigNote);

		Intent notifyIntentNoti = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, noticode, notifyIntentNoti, PendingIntent.FLAG_ONE_SHOT);
		builder.setContentIntent(pendingIntent);
		Notification notification = builder.build();
		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
		notificationManagerCompat.notify(noticode, notification);

	}
}
