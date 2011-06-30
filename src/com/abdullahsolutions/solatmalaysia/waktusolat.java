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
	private static final String WAKTU_PREF = "savedwaktu";

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

	private Handler guiThread;
	private ExecutorService waktuThread;
	private Runnable updatewaktu;
	private Future waktupending;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		
		
		if (getIntent().hasExtra("kod_kawasan")) {
			Bundle extras = getIntent().getExtras();
			String kod_kawasan = extras.getString("kod_kawasan");
			getPreferences(MODE_PRIVATE).edit().putString("kod_kawasan", kod_kawasan).commit();
			
			String kawasan = extras.getString("kawasan");
			getPreferences(MODE_PRIVATE).edit().putString("kawasan", kawasan).commit();
			
			String negeri = extras.getString("negeri");
			getPreferences(MODE_PRIVATE).edit().putString("negeri", negeri).commit();
			Log.d(TAG, "kod_kawasan:" + kod_kawasan);
		}
		View selectZoneButton = findViewById(R.id.select_zone);
		selectZoneButton.setOnClickListener(this);

		initThreading();
		findViews();
		guiThread.post(updatewaktu);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updatewaktu();
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
		
		negeri.setText(getPreferences(MODE_PRIVATE).getString("negeri", "Selangor Dan Wilayah Persekutuan"));
		kawasan.setText(getPreferences(MODE_PRIVATE).getString("kawasan", "Kuala Lumpur"));
		kemaskini.setText(getPreferences(MODE_PRIVATE).getString("kemaskini", "Belum Pernah"));
	}

	private void initThreading() {
		guiThread = new Handler();
		waktuThread = Executors.newSingleThreadExecutor();
		updatewaktu = new Runnable() {
			public void run() {
				String curdate = (String) android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date());				
				if(getIntent().hasExtra("kod_kawasan") || !curdate.equals(getPreferences(MODE_PRIVATE).getString("kemaskini", "Belum Pernah").substring(9))){
					if (waktupending != null)
						waktupending.cancel(true);
					try {
						waktusolatTask waktutask = new waktusolatTask(waktusolat.this);
						waktupending = waktuThread.submit(waktutask);
					} catch (RejectedExecutionException e) {
						Log.d(TAG, "Rejectedexception", e);
					}
				}
				else{
					updatewaktu();
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

	public void updatewaktu() {
		guiThread.post(new Runnable() {
			public void run() {
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
						TextView title = (TextView) findViewById(R.id.imsak_title);
						title.setBackgroundResource(R.color.hightlight);
						imsak_time.setBackgroundResource(R.color.hightlight);
					}
					subuh_time.setText(strsubuh);
					if(curdate.after(cdf.parse(strsubuh)) && curdate.before(cdf.parse(strsyuruk))){
						TextView title = (TextView) findViewById(R.id.subuh_title);
						title.setBackgroundResource(R.color.hightlight);
						subuh_time.setBackgroundResource(R.color.hightlight);
					}
					syuruk_time.setText(strsyuruk);
					if(curdate.after(cdf.parse(strsyuruk)) && curdate.before(cdf.parse(strzohor))){
						TextView title = (TextView) findViewById(R.id.syuruk_title);
						title.setBackgroundResource(R.color.hightlight);
						syuruk_time.setBackgroundResource(R.color.hightlight);
					}
					zohor_time.setText(strzohor);
					if(curdate.after(cdf.parse(strzohor)) && curdate.before(cdf.parse(strasar))){
						TextView title = (TextView) findViewById(R.id.zohor_title);
						title.setBackgroundResource(R.color.hightlight);
						zohor_time.setBackgroundResource(R.color.hightlight);
					}
					asar_time.setText(strasar);
					if(curdate.after(cdf.parse(strasar)) && curdate.before(cdf.parse(strmaghrib))){
						TextView title = (TextView) findViewById(R.id.asar_title);
						title.setBackgroundResource(R.color.hightlight);
						asar_time.setBackgroundResource(R.color.hightlight);
					}
					maghrib_time.setText(strmaghrib);
					if(curdate.after(cdf.parse(strmaghrib)) && curdate.before(cdf.parse(strisya))){
						TextView title = (TextView) findViewById(R.id.maghrib_title);
						title.setBackgroundResource(R.color.hightlight);
						maghrib_time.setBackgroundResource(R.color.hightlight);
					}
					isya_time.setText(strisya);
					if(curdate.after(cdf.parse(strisya)) || curdate.before(cdf.parse(strimsak))){
						TextView title = (TextView) findViewById(R.id.isya_title);
						title.setBackgroundResource(R.color.hightlight);
						isya_time.setBackgroundResource(R.color.hightlight);
					}
				}
				catch(Exception e){
					Log.d(TAG, "Rejectedexception", e);
				}
				
				kemaskini.setText(getPreferences(MODE_PRIVATE).getString("kemaskini", "Belum Pernah"));
			}
		});
	}

	public String getkodkawasan() {
		return getPreferences(MODE_PRIVATE).getString("kod_kawasan", "sgr03");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_zone:
			Intent i = new Intent(this, ZonSolat.class);
			startActivity(i);
			break;
		}
	}
}