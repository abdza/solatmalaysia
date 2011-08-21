package com.abdullahsolutions.solatmalaysia;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;


public class SolatWidget extends AppWidgetProvider {
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
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){		
		RemoteViews updateViews = updateview(context);
		Intent intent = new Intent(context, waktusolat.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		updateViews.setOnClickPendingIntent(R.id.solatwidget, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds,updateViews);		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}	
	
	static public RemoteViews updateview(Context context){
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		String curtime = (String) android.text.format.DateFormat.format("kk:mm", new java.util.Date());
		SimpleDateFormat cdf = new SimpleDateFormat("HH:mm");
		try{
		Date curdate = cdf.parse(curtime);
		String strimsak = settings.getString(waktu_imsak, "--");
		String strsubuh = settings.getString(waktu_subuh, "--");
		String strsyuruk = settings.getString(waktu_syuruk, "--");
		String strzohor = settings.getString(waktu_zohor, "--");
		String strasar = settings.getString(waktu_asar, "--");
		String strmaghrib = settings.getString(waktu_maghrib, "--");
		String strisya = settings.getString(waktu_isya, "--");
		updateViews.setTextViewText(R.id.imsak, settings.getString(waktu_imsak, "--"));
		if(curdate.equals(cdf.parse(strimsak)) || (curdate.after(cdf.parse(strimsak)) && curdate.before(cdf.parse(strsubuh)))){
			updateViews.setInt(R.id.imsakline, "setBackgroundResource", R.color.hightlight);
		}
		else{
			updateViews.setInt(R.id.imsakline, "setBackgroundResource", 0);
		}
				
		updateViews.setTextViewText(R.id.subuh, settings.getString(waktu_subuh, "--"));
		if(curdate.equals(cdf.parse(strsubuh)) || (curdate.after(cdf.parse(strsubuh)) && curdate.before(cdf.parse(strsyuruk)))){
			updateViews.setInt(R.id.subuhline, "setBackgroundResource", R.color.hightlight);
		}
		else{
			updateViews.setInt(R.id.subuhline, "setBackgroundResource", 0);
		}
		
		updateViews.setTextViewText(R.id.syuruk, settings.getString(waktu_syuruk, "--"));
		if(curdate.equals(cdf.parse(strsyuruk)) || (curdate.after(cdf.parse(strsyuruk)) && curdate.before(cdf.parse(strzohor)))){
			updateViews.setInt(R.id.syurukline, "setBackgroundResource", R.color.hightlight);
		}
		else{
			updateViews.setInt(R.id.syurukline, "setBackgroundResource", 0);
		}
		
		updateViews.setTextViewText(R.id.zohor, settings.getString(waktu_zohor, "--"));
		if(curdate.equals(cdf.parse(strzohor)) || (curdate.after(cdf.parse(strzohor)) && curdate.before(cdf.parse(strasar)))){
			updateViews.setInt(R.id.zohorline, "setBackgroundResource", R.color.hightlight);
		}
		else{
			updateViews.setInt(R.id.zohorline, "setBackgroundResource", 0);
		}
		
		updateViews.setTextViewText(R.id.asar, settings.getString(waktu_asar, "--"));
		if(curdate.equals(cdf.parse(strasar)) || (curdate.after(cdf.parse(strasar)) && curdate.before(cdf.parse(strmaghrib)))){
			updateViews.setInt(R.id.asarline, "setBackgroundResource", R.color.hightlight);
		}
		else{
			updateViews.setInt(R.id.asarline, "setBackgroundResource", 0);
		}
		
		updateViews.setTextViewText(R.id.maghrib, settings.getString(waktu_maghrib, "--"));
		if(curdate.equals(cdf.parse(strmaghrib)) || (curdate.after(cdf.parse(strmaghrib)) && curdate.before(cdf.parse(strisya)))){
			updateViews.setInt(R.id.maghribline, "setBackgroundResource", R.color.hightlight);
		}
		else{
			updateViews.setInt(R.id.maghribline, "setBackgroundResource", 0);
		}
		
		updateViews.setTextViewText(R.id.isya, strisya);
		if(curdate.equals(cdf.parse(strimsak)) || curdate.after(cdf.parse(strisya)) || curdate.before(cdf.parse(strimsak))){
			updateViews.setInt(R.id.isyaline, "setBackgroundResource", R.color.hightlight);
		}
		else{
			updateViews.setInt(R.id.isyaline, "setBackgroundResource", 0);
		}
		

		}
		catch(Exception e){
			
		}
		updateViews.setTextViewText(R.id.tarikh, settings.getString(waktu_kemaskini, "--"));
		String kawasan = settings.getString("kawasan", "--");
		if(kawasan.length()>18){
			kawasan = kawasan.substring(0,18);
		}
		updateViews.setTextViewText(R.id.tempat, kawasan);
		return updateViews;
	}
}
