package com.abdullahsolutions.solatmalaysia;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore.Audio;
import android.util.Log;

public class SolatService extends Service {
    private NotificationManager mNM;
    public static final String PREFS_NAME = "MyPrefsFile";	
    static private SharedPreferences settings;
    private String curwaktu;
    
    private Timer timer;
    
    public static final String waktu_imsak = "waktu_imsak";
	public static final String waktu_subuh = "waktu_subuh";
	public static final String waktu_syuruk = "waktu_syuruk";
	public static final String waktu_zohor = "waktu_zohor";
	public static final String waktu_asar = "waktu_asar";
	public static final String waktu_maghrib = "waktu_maghrib";
	public static final String waktu_isya = "waktu_isya";
	
	private static final long alert_interval = 6000;
	
	private TimerTask waitSolat = new TimerTask() {
		public void run() {
			settings = getSharedPreferences(PREFS_NAME, 0);
    	  	String curtime = (String) android.text.format.DateFormat.format("kk:mm", new java.util.Date());
			SimpleDateFormat cdf = new SimpleDateFormat("HH:mm");
			try{
				Date curdate = cdf.parse(curtime);
				Log.d("SolatNotification", "In timer");
		    	String strimsak = settings.getString(waktu_imsak, "--");		    	
				String strsubuh = settings.getString(waktu_subuh, "--");
				String strsyuruk = settings.getString(waktu_syuruk, "--");
				String strzohor = settings.getString(waktu_zohor, "--");
				String strasar = settings.getString(waktu_asar, "--");
				String strmaghrib = settings.getString(waktu_maghrib, "--");
				String strisya = settings.getString(waktu_isya, "--");
				Log.d("SolatNotification","Curwaktu:" + curwaktu);
				if(curdate.equals(cdf.parseObject(strimsak)) && !curwaktu.equals("imsak")){
					notifySolat("imsak");
					updatewidget();
					curwaktu="imsak";					
					Log.d("SolatNotification", "waktu imsak");
				}
				else if(curdate.equals(cdf.parseObject(strsubuh)) && !curwaktu.equals("subuh")){
					notifySolat("solat subuh");
					updatewidget();
					curwaktu="subuh";
					Log.d("SolatNotification", "waktu subuh");
				}
				else if(curdate.equals(cdf.parseObject(strsyuruk)) && !curwaktu.equals("syuruk")){
					notifySolat("syuruk");
					updatewidget();
					curwaktu="syuruk";
					Log.d("SolatNotification", "waktu syuruk");
				}
				else if(curdate.equals(cdf.parseObject(strzohor)) && !curwaktu.equals("zohor")){
					notifySolat("solat zohor");
					updatewidget();
					curwaktu="zohor";
					Log.d("SolatNotification", "waktu zohor");
				}
				else if(curdate.equals(cdf.parseObject(strasar)) && !curwaktu.equals("asar")){
					notifySolat("solat asar");
					updatewidget();
					curwaktu="asar";
					Log.d("SolatNotification", "waktu asar");
				}
				else if(curdate.equals(cdf.parseObject(strmaghrib)) && !curwaktu.equals("maghrib")){
					notifySolat("solat maghrib");
					updatewidget();
					curwaktu="maghrib";
					Log.d("SolatNotification", "waktu maghrib");
				}
				else if(curdate.equals(cdf.parseObject(strisya)) && !curwaktu.equals("isya")){
					notifySolat("solat isya");
					updatewidget();
					curwaktu = "isya";
					Log.d("SolatNotification", "waktu isya");
				}
			}
			catch(Exception e){
				
			}   
		}
    };

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.solatnotification;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	SolatService getService() {
            return SolatService.this;
        }
    }

    @Override
    public void onCreate() {
    	Log.d("SolatNotification", "Here now");
    	curwaktu = "Tiada";
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        timer = new Timer();
        timer.schedule(waitSolat,1000,SolatService.alert_interval);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SolatNotification", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
    }

    @Override
    public IBinder onBind(Intent intent) {  
    	Log.d("SolatNotification","run again");
    	waitSolat.run();
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
    
    
    public void notifySolat(String waktu){
    	// In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "Telah masuk waktu " + waktu;

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.icon, text, System.currentTimeMillis());
        
        notification.sound = Uri.parse("android.resource://com.abdullahsolutions.solatmalaysia/" + R.raw.azan);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, waktusolat.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, "Haiya Ala Solah",text, contentIntent);

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
    
    public void updatewidget(){
    	ComponentName me=new ComponentName(this, SolatWidget.class);        			
		AppWidgetManager mgr=AppWidgetManager.getInstance(this);
		
		mgr.updateAppWidget(me,SolatWidget.updateview(getApplicationContext()));
    	Log.d("SolatNotification","In update");    	
    }   
    
}
