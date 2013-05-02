package com.abdullahsolutions.solatmalaysia;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;

public class SolatWidget extends AppWidgetProvider {
	private static final String TAG = "waktusolat";
	public static final String PREFS_NAME = "MyPrefsFile";
	static private SharedPreferences settings;
	public static final String waktu_imsak = "waktu_imsak";
	public static final String waktu_subuh = "waktu_subuh";
	public static final String waktu_syuruk = "waktu_syuruk";
	public static final String waktu_zohor = "waktu_zohor";
	public static final String waktu_asar = "waktu_asar";
	public static final String waktu_maghrib = "waktu_maghrib";
	public static final String waktu_isya = "waktu_isya";
	public static final String waktu_kemaskini = "kemaskini";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		RemoteViews updateViews = updateview(context);
		Intent intent = new Intent(context, waktusolat.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		updateViews.setOnClickPendingIntent(R.id.solatwidget, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, updateViews);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	static void setalarm(Context context, String nextwaktu, String waktu) {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, Azan.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		String[] nwaktu = nextwaktu.split(":");
		Log.d(TAG,"AM_PM PM " + time.get(Calendar.PM) + " " + time.get(Calendar.PM));
		if(waktu=="isya" && time.get(Calendar.AM_PM)==1){
			time.add(Calendar.DATE, 1);
		}
		time.set(Calendar.HOUR_OF_DAY, Integer.valueOf(nwaktu[0]));
		time.set(Calendar.MINUTE, Integer.valueOf(nwaktu[1]));		
		alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
				pendingIntent);
		Log.d(TAG,"Azan set by Widget at " + time.toString());
	}

	static public RemoteViews updateview(Context context) {
		SolatDB solatdb;
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		String curtime = (String) android.text.format.DateFormat.format(
				"kk:mm", new java.util.Date());
		String curday = (String) android.text.format.DateFormat.format(
				"dd-MM-yyyy", new java.util.Date());
		SimpleDateFormat cdf = new SimpleDateFormat("HH:mm");
		solatdb = new SolatDB(context);
		try {			
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
				Date curdate = cdf.parse(curtime);
				String strimsak = cursorwaktu.getString(0);
				String strsubuh = cursorwaktu.getString(1);
				String strsyuruk = cursorwaktu.getString(2);
				String strzohor = cursorwaktu.getString(3);
				String strasar = cursorwaktu.getString(4);
				String strmaghrib = cursorwaktu.getString(5);
				String strisya = cursorwaktu.getString(6);
				updateViews.setTextViewText(R.id.imsak, strimsak);
				String waktu = "";
				String nextwaktu = "";
				if (curdate.equals(cdf.parse(strimsak))
						|| (curdate.after(cdf.parse(strimsak)) && curdate
								.before(cdf.parse(strsubuh)))) {
					updateViews.setInt(R.id.imsakline, "setBackgroundResource",
							R.color.hightlight);
					//setalarm(context, strsubuh,"imsak");
					waktu="imsak";
					nextwaktu=strsubuh;
				} else {
					updateViews.setInt(R.id.imsakline, "setBackgroundResource",
							0);
				}

				updateViews.setTextViewText(R.id.subuh, strsubuh);
				if (curdate.equals(cdf.parse(strsubuh))
						|| (curdate.after(cdf.parse(strsubuh)) && curdate
								.before(cdf.parse(strsyuruk)))) {
					updateViews.setInt(R.id.subuhline, "setBackgroundResource",
							R.color.hightlight);
					//setalarm(context, strsyuruk,"subuh");
					waktu="subuh";
					nextwaktu=strsyuruk;
				} else {
					updateViews.setInt(R.id.subuhline, "setBackgroundResource",
							0);
				}

				updateViews.setTextViewText(R.id.syuruk, strsyuruk);
				if (curdate.equals(cdf.parse(strsyuruk))
						|| (curdate.after(cdf.parse(strsyuruk)) && curdate
								.before(cdf.parse(strzohor)))) {
					updateViews.setInt(R.id.syurukline,
							"setBackgroundResource", R.color.hightlight);
					//setalarm(context,strzohor,"syuruk");
					waktu="syuruk";
					nextwaktu=strzohor;
				} else {
					updateViews.setInt(R.id.syurukline,
							"setBackgroundResource", 0);
				}

				updateViews.setTextViewText(R.id.zohor, strzohor);
				if (curdate.equals(cdf.parse(strzohor))
						|| (curdate.after(cdf.parse(strzohor)) && curdate
								.before(cdf.parse(strasar)))) {
					updateViews.setInt(R.id.zohorline, "setBackgroundResource",
							R.color.hightlight);
					//setalarm(context,strasar,"zohor");
					waktu="zohor";
					nextwaktu=strasar;
				} else {
					updateViews.setInt(R.id.zohorline, "setBackgroundResource",
							0);
				}

				updateViews.setTextViewText(R.id.asar, strasar);
				if (curdate.equals(cdf.parse(strasar))
						|| (curdate.after(cdf.parse(strasar)) && curdate
								.before(cdf.parse(strmaghrib)))) {
					updateViews.setInt(R.id.asarline, "setBackgroundResource",
							R.color.hightlight);
					//setalarm(context,strmaghrib,"asar");
					waktu="asar";
					nextwaktu=strmaghrib;
				} else {
					updateViews.setInt(R.id.asarline, "setBackgroundResource",
							0);
				}

				updateViews.setTextViewText(R.id.maghrib, strmaghrib);
				if (curdate.equals(cdf.parse(strmaghrib))
						|| (curdate.after(cdf.parse(strmaghrib)) && curdate
								.before(cdf.parse(strisya)))) {
					updateViews.setInt(R.id.maghribline,
							"setBackgroundResource", R.color.hightlight);
					//setalarm(context,strisya,"maghrib");
					waktu="maghrib";
					nextwaktu=strisya;
				} else {
					updateViews.setInt(R.id.maghribline,
							"setBackgroundResource", 0);
				}

				updateViews.setTextViewText(R.id.isya, strisya);

				updateViews.setTextViewText(R.id.tarikh, curday);

				if (curdate.equals(cdf.parse(strimsak))
						|| curdate.after(cdf.parse(strisya))
						|| curdate.before(cdf.parse(strimsak))) {
					updateViews.setInt(R.id.isyaline, "setBackgroundResource",
							R.color.hightlight);
					//setalarm(context,strimsak,"isya");
					waktu="isya";
					nextwaktu=strimsak;
				} else {
					updateViews.setInt(R.id.isyaline, "setBackgroundResource",
							0);
				}
						
				if(settings.getString("curwaktu","tiada")!=waktu){
					settings.edit().putString("curwaktu",waktu).commit();
					setalarm(context, nextwaktu, waktu);
				}
			} else {
				updateViews.setTextViewText(R.id.tarikh, "Updating..");
				updateViews.setTextViewText(R.id.imsak, "Updating..");
				updateViews.setTextViewText(R.id.subuh, "Updating..");
				updateViews.setTextViewText(R.id.syuruk, "Updating..");
				updateViews.setTextViewText(R.id.zohor, "Updating..");
				updateViews.setTextViewText(R.id.asar, "Updating..");
				updateViews.setTextViewText(R.id.maghrib, "Updating..");
				updateViews.setTextViewText(R.id.isya, "Updating..");
			}

		} catch (Exception e) {

		} finally {
			solatdb.close();
		}
		

		String kawasan = settings.getString("kawasan", "Kuala Lumpur");
		if (kawasan.length() > 18) {
			kawasan = kawasan.substring(0, 18);
		}
		updateViews.setTextViewText(R.id.tempat, kawasan);
		return updateViews;
	}
}
