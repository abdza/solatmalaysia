package com.abdullahsolutions.solatmalaysia;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class Azan extends BroadcastReceiver {
	
	private static final String TAG = "waktusolat";

	//private NotificationManager mNM;
	private SharedPreferences settings;
	public static final String PREFS_NAME = "MyPrefsFile";
	private SolatDB solatdb;

	private int NOTIFICATION = R.string.solatnotification;

	static void setalarm(Context context, String nextwaktu, String waktu) {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, Azan.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		String[] nwaktu = nextwaktu.split(":");
		Log.d(TAG,"AM_PM PM " + time.get(Calendar.AM_PM) + " " + time.get(Calendar.PM));
		if(waktu=="isya" && time.get(Calendar.AM_PM)==1){
			time.add(Calendar.DATE, 1);
		}
		time.set(Calendar.HOUR_OF_DAY, Integer.valueOf(nwaktu[0]));
		time.set(Calendar.MINUTE, Integer.valueOf(nwaktu[1]));		
		alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
				pendingIntent);
		Log.d(TAG,"Azan set by Azan at " + time.toString());
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		String curtime = (String) android.text.format.DateFormat.format(
				"kk:mm", new java.util.Date());
		SimpleDateFormat cdf = new SimpleDateFormat("HH:mm");
		String waktu = "";
		String nextwaktu = "";
		try {
			Date curdate = cdf.parse(curtime);
			String curday = (String) android.text.format.DateFormat.format(
					"dd-MM-yyyy", new java.util.Date());
			solatdb = new SolatDB(context);
			SQLiteDatabase db = solatdb.getReadableDatabase();
			Cursor cursorwaktu = db
					.rawQuery(
							"select imsak,subuh,syuruk,zohor,asar,maghrib,isya,tarikh,kawasan from waktusolat where tarikh='"
									+ curday
									+ "' and kawasan='"
									+ settings
											.getString("kod_kawasan", "sgr03")
									+ "'", null);
			if (cursorwaktu.getCount() > 0) {
				cursorwaktu.moveToFirst();
				String strimsak = cursorwaktu.getString(0);
				String strsubuh = cursorwaktu.getString(1);
				String strsyuruk = cursorwaktu.getString(2);
				String strzohor = cursorwaktu.getString(3);
				String strasar = cursorwaktu.getString(4);
				String strmaghrib = cursorwaktu.getString(5);
				String strisya = cursorwaktu.getString(6);
				if (curdate.equals(cdf.parse(strimsak))
						|| (curdate.after(cdf.parse(strimsak)) && curdate
								.before(cdf.parse(strsubuh)))) {
					waktu = "imsak";
					nextwaktu = strsubuh;
				} else if (curdate.equals(cdf.parse(strsubuh))
						|| (curdate.after(cdf.parse(strsubuh)) && curdate
								.before(cdf.parse(strsyuruk)))) {
					waktu = "subuh";
					nextwaktu = strsyuruk;
				} else if (curdate.equals(cdf.parse(strsyuruk))
						|| (curdate.after(cdf.parse(strsyuruk)) && curdate
								.before(cdf.parse(strzohor)))) {
					waktu = "syuruk";
					nextwaktu = strzohor;
				} else if (curdate.equals(cdf.parse(strzohor))
						|| (curdate.after(cdf.parse(strzohor)) && curdate
								.before(cdf.parse(strasar)))) {
					waktu = "zohor";
					nextwaktu = strasar;
				} else if (curdate.equals(cdf.parse(strasar))
						|| (curdate.after(cdf.parse(strasar)) && curdate
								.before(cdf.parse(strmaghrib)))) {
					waktu = "asar";
					nextwaktu = strmaghrib;
				} else if (curdate.equals(cdf.parse(strmaghrib))
						|| (curdate.after(cdf.parse(strmaghrib)) && curdate
								.before(cdf.parse(strisya)))) {
					waktu = "maghrib";
					nextwaktu = strisya;
				} else if (curdate.equals(cdf.parse(strisya))
						|| curdate.after(cdf.parse(strisya))) {
					waktu = "isya";
					nextwaktu = strimsak;
				}
			}
		} catch (Exception e) {

		} finally {
			solatdb.close();
		}
		
		ComponentName me = new ComponentName(context,
				SolatWidget.class);
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);

		mgr.updateAppWidget(me,
				SolatWidget.updateview(context));
				
		setalarm(context, nextwaktu, waktu);
		NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence text = "Telah masuk waktu " + waktu;
		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.icon, text,
				System.currentTimeMillis());
		if (Prefs.getAzan(context, waktu)) {
			notification.sound = Uri
					.parse("android.resource://com.abdullahsolutions.solatmalaysia/"
							+ R.raw.azan);
		}
		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, waktusolat.class), 0);
		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(context, "Haiya Ala Solah", text,
				contentIntent);
		// Send the notification.
		mNM.notify(NOTIFICATION, notification);
	}

}
