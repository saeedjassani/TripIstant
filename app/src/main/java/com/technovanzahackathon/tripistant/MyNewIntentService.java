package com.technovanzahackathon.tripistant;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.AlarmClock;
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
				.setContentIcon(R.drawable.home);

		switch (title) {
			case "Shopping": {
                Intent buttonIntent = new Intent(getApplicationContext(), AutoDismissReceiver.class);
                buttonIntent.putExtra("notificationId",noticode);
                PendingIntent pIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, buttonIntent,0);

                Intent intentq = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.in"));
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0 , intentq, 0);
				builder.addAction(0, "NO", pIntent);
				builder.addAction(1, "YES", pIntent1);
				break;
			}
			case "Medication": {
                Intent buttonIntent = new Intent(getApplicationContext(), AutoDismissReceiver.class);
                buttonIntent.putExtra("notificationId", noticode);
                PendingIntent pIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, buttonIntent, 0);

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=pharmacies");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, mapIntent, 0);
                builder.addAction(0, "NO", pIntent);
                builder.addAction(1, "YES", pIntent1);
                break;
            }
			case "Plan": {
				Intent sendIntent = new Intent(getApplicationContext(), ChecklistFragment.class);
				PendingIntent pIntent = PendingIntent.getActivity(MyNewIntentService.this, 0, sendIntent, 0);
				builder.addAction(0, "CHECKLIST", pIntent);
                Intent buttonIntent = new Intent(getApplicationContext(), AutoDismissReceiver.class);
                buttonIntent.putExtra("notificationId", noticode);
                PendingIntent pIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, buttonIntent, 0);
                builder.addAction(1, "YES", pIntent1);

                break;
			}
			case "Expense": {
				Intent sendIntent = new Intent(getApplicationContext(), ExpenseActivity.class);
				PendingIntent pIntent = PendingIntent.getActivity(MyNewIntentService.this, 0, sendIntent, 0);
				builder.addAction(0, "Expense", pIntent);

                Intent buttonIntent = new Intent(getApplicationContext(), AutoDismissReceiver.class);
                buttonIntent.putExtra("notificationId", noticode);
                PendingIntent pIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, buttonIntent, 0);
                builder.addAction(1,"Dismiss", pIntent1);
				break;
			}
			case "Packing": {
                Intent buttonIntent = new Intent(getApplicationContext(), AutoDismissReceiver.class);
                buttonIntent.putExtra("notificationId", 0);
                PendingIntent pIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, buttonIntent, 0);

                Intent intentq = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.in"));
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentq, 0);
                builder.addAction(0, "NO", pIntent);
                builder.addAction(1, "YES", pIntent1);
                break;
            }
			case "Sleep": {
                Intent buttonIntent = new Intent(getApplicationContext(), AutoDismissReceiver.class);
                buttonIntent.putExtra("notificationId", noticode);
                PendingIntent pIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, buttonIntent, 0);

                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
                builder.addAction(0, "SET ALARM", pIntent);
                builder.addAction(1, "DISMISS", pIntent1);
                break;
            }
			case "Photos": {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                PendingIntent pIntent = PendingIntent.getActivity(MyNewIntentService.this, 0, cameraIntent, 0);
				builder.addAction(0, "Take Photos", pIntent);
				break;
			}
			case "Cab": {
                Intent intentapp = new Intent("com.ubercab.UBUberActivity");
                PendingIntent pIntent = PendingIntent.getActivity(MyNewIntentService.this, 0, intentapp, 0);
                Intent intentapp1 = new Intent("com.mobond.mindicator.SplashUI");
                PendingIntent pIntent1 = PendingIntent.getActivity(MyNewIntentService.this, 0, intentapp1, 0);
                builder.addAction(0, "Cab", pIntent);
                builder.addAction(1,"mindicator", pIntent1);
                break;
            }
		}

		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.home);
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
