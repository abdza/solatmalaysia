package com.abdullahsolutions.solatmalaysia;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import com.abdullahsolutions.solatmalaysia.R;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import android.text.format.DateFormat;
import android.util.Log;

public class waktusolat extends Activity implements OnClickListener {

	private static final String TAG = "waktusolat";

	private TextView imsak_time, subuh_time, syuruk_time, zohor_time,
			asar_time, maghrib_time, isya_time,kawasan,negeri,kemaskini;
	public static final String waktu_imsak = "waktu_imsak";
	public static final String waktu_subuh = "waktu_subuh";
	public static final String waktu_syuruk = "waktu_syuruk";
	public static final String waktu_zohor = "waktu_zohor";
	public static final String waktu_asar = "waktu_asar";
	public static final String waktu_maghrib = "waktu_maghrib";
	public static final String waktu_isya = "waktu_isya";
	public static final String waktu_kemaskini = "kemaskini";	
	
	static final int pick_kawasan_request = 0;

	private Handler guiThread;
	private ExecutorService waktuThread;
	private Runnable updatewaktu;
	private Future waktupending;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		View selectZoneButton = findViewById(R.id.select_zone);
		selectZoneButton.setOnClickListener(this);

		initThreading();
		findViews();
		guiThread.post(updatewaktu);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updatewaktuview();
	}

	private void findViews() {		
		
		imsak_time = (TextView) findViewById(R.id.imsak_time);
		subuh_time = (TextView) findViewById(R.id.subuh_time);
		syuruk_time = (TextView) findViewById(R.id.syuruk_time);
		zohor_time = (TextView) findViewById(R.id.zohor_time);
		asar_time = (TextView) findViewById(R.id.asar_time);
		maghrib_time = (TextView) findViewById(R.id.maghrib_time);
		isya_time = (TextView) findViewById(R.id.isya_time);
		negeri = (TextView) findViewById(R.id.negeri);
		kawasan = (TextView) findViewById(R.id.kawasan);
		kemaskini = (TextView) findViewById(R.id.kemaskini);
	}

	private void initThreading() {
		guiThread = new Handler();
		waktuThread = Executors.newSingleThreadExecutor();		
		updatewaktu = new Runnable() {
			public void run() {				
				String curdate = (String) android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date());				
				if(true || !curdate.equals(getPreferences(MODE_PRIVATE).getString("kemaskini", "Belum Pernah").substring(9))){
					if (waktupending != null)
						waktupending.cancel(true);
					try {
						Log.d(TAG, "Right here");
						waktusolatTask waktutask = new waktusolatTask(waktusolat.this);
						waktupending = waktuThread.submit(waktutask);
					} catch (RejectedExecutionException e) {
						Log.d(TAG, "Rejectedexception", e);
					}
				}
				else{
					updatewaktuview();
				}
			}
		};
	}

	public void savewaktu(String nama_waktu, String waktu) {
		getPreferences(MODE_PRIVATE).edit().putString(nama_waktu, waktu).commit();
	}
	
	public void savekemaskini() {
		String curdate = (String) android.text.format.DateFormat.format("hh:mm a dd/MM/yyyy", new java.util.Date());
		getPreferences(MODE_PRIVATE).edit().putString("kemaskini", curdate).commit();
	}

	public void updatewaktuview() {
		guiThread.post(new Runnable() {
			public void run() {
				negeri.setText(getPreferences(MODE_PRIVATE).getString("negeri", "Selangor Dan Wilayah Persekutuan"));
				kawasan.setText(getPreferences(MODE_PRIVATE).getString("kawasan", "Kuala Lumpur"));
				kemaskini.setText(getPreferences(MODE_PRIVATE).getString("kemaskini", "Belum Pernah"));
				SimpleDateFormat cdf = new SimpleDateFormat("HH:mm");
				try{
					String curtime = (String) android.text.format.DateFormat.format("kk:mm", new java.util.Date());
					Date curdate = cdf.parse(curtime);
					String strimsak = getPreferences(MODE_PRIVATE).getString(waktu_imsak, "--");
					String strsubuh = getPreferences(MODE_PRIVATE).getString(waktu_subuh, "--");
					String strsyuruk = getPreferences(MODE_PRIVATE).getString(waktu_syuruk, "--");
					String strzohor = getPreferences(MODE_PRIVATE).getString(waktu_zohor, "--");
					String strasar = getPreferences(MODE_PRIVATE).getString(waktu_asar, "--");
					String strmaghrib = getPreferences(MODE_PRIVATE).getString(waktu_maghrib, "--");
					String strisya = getPreferences(MODE_PRIVATE).getString(waktu_isya, "--");
					
					imsak_time.setText(strimsak);
					if(curdate.after(cdf.parse(strimsak)) && curdate.before(cdf.parse(strsubuh))){
						highlight(R.id.imsak_title,imsak_time,true);
					}
					else{
						highlight(R.id.imsak_title,imsak_time,false);
					}
					subuh_time.setText(strsubuh);
					if(curdate.after(cdf.parse(strsubuh)) && curdate.before(cdf.parse(strsyuruk))){
						highlight(R.id.subuh_title,subuh_time,true);
					}
					else{
						highlight(R.id.subuh_title,subuh_time,false);
					}
					syuruk_time.setText(strsyuruk);
					if(curdate.after(cdf.parse(strsyuruk)) && curdate.before(cdf.parse(strzohor))){
						highlight(R.id.syuruk_title,syuruk_time,true);
					}
					else{
						highlight(R.id.syuruk_title,syuruk_time,false);
					}
					zohor_time.setText(strzohor);
					if(curdate.after(cdf.parse(strzohor)) && curdate.before(cdf.parse(strasar))){
						highlight(R.id.zohor_title,zohor_time,true);
					}
					else{
						highlight(R.id.zohor_title,zohor_time,false);
					}
					asar_time.setText(strasar);
					if(curdate.after(cdf.parse(strasar)) && curdate.before(cdf.parse(strmaghrib))){
						highlight(R.id.asar_title,asar_time,true);
					}
					else{
						highlight(R.id.asar_title,asar_time,false);
					}
					maghrib_time.setText(strmaghrib);
					if(curdate.after(cdf.parse(strmaghrib)) && curdate.before(cdf.parse(strisya))){
						highlight(R.id.maghrib_title,maghrib_time,true);
					}
					else{
						highlight(R.id.maghrib_title,maghrib_time,false);
					}
					isya_time.setText(strisya);
					if(curdate.after(cdf.parse(strisya)) || curdate.before(cdf.parse(strimsak))){
						highlight(R.id.isya_title,isya_time,true);
					}
					else{
						highlight(R.id.isya_title,isya_time,false);
					}
				}
				catch(Exception e){
					Log.d(TAG, "Rejectedexception", e);
				}
				
				kemaskini.setText(getPreferences(MODE_PRIVATE).getString("kemaskini", "Belum Pernah"));
			}
		});
	}
	
	private void highlight(int titleint,TextView time,boolean hightlight){
		if(hightlight){
			TextView title = (TextView) findViewById(titleint);
			title.setBackgroundResource(R.color.hightlight);
			time.setBackgroundResource(R.color.hightlight);
		}
		else{
			TextView title = (TextView) findViewById(titleint);
			title.setBackgroundResource(R.color.green);
			time.setBackgroundResource(R.color.green);
		}
	}

	public String getkodkawasan() {
		return getPreferences(MODE_PRIVATE).getString("kod_kawasan", "sgr03");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_zone:
			Intent i = new Intent(this, ZonSolat.class);
			startActivityForResult(i,pick_kawasan_request);
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == pick_kawasan_request) {
            if (resultCode == RESULT_OK) {
            	if (data.hasExtra("kod_kawasan")) {
        			Bundle extras = data.getExtras();
        			String kod_kawasan = extras.getString("kod_kawasan");
        			getPreferences(MODE_PRIVATE).edit().putString("kod_kawasan", kod_kawasan).commit();
        			
        			String kawasan = extras.getString("kawasan");
        			getPreferences(MODE_PRIVATE).edit().putString("kawasan", kawasan).commit();
        			
        			String negeri = extras.getString("negeri");
        			getPreferences(MODE_PRIVATE).edit().putString("negeri", negeri).commit();
        			Log.d(TAG, "kod_kawasan:" + kod_kawasan);
        			
        			getPreferences(MODE_PRIVATE).edit().putBoolean("reset", true).commit();
        			updatewaktu.run();
        		}
            }
        }
    }
}