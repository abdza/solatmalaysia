package my.abdza.solatmalaysia;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

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
				if(!curdate.equals(getPreferences(MODE_PRIVATE).getString("kemaskini", "Belum Pernah").substring(6))){
					if (waktupending != null)
						waktupending.cancel(true);
					try {
						waktusolatTask waktutask = new waktusolatTask(waktusolat.this);
						waktupending = waktuThread.submit(waktutask);
					} catch (RejectedExecutionException e) {
						Log.d(TAG, "Rejectedexception", e);
					}
				}
			}
		};
	}

	public void savewaktu(String nama_waktu, String waktu) {
		getPreferences(MODE_PRIVATE).edit().putString(nama_waktu, waktu).commit();
	}
	
	public void savekemaskini() {
		String curdate = (String) android.text.format.DateFormat.format("hh:mm dd/MM/yyyy", new java.util.Date());
		getPreferences(MODE_PRIVATE).edit().putString("kemaskini", curdate).commit();
	}

	public void updatewaktu() {
		guiThread.post(new Runnable() {
			public void run() {
				imsak_time.setText(getPreferences(MODE_PRIVATE).getString(waktu_imsak, "--"));
				subuh_time.setText(getPreferences(MODE_PRIVATE).getString(waktu_subuh, "--"));
				syuruk_time.setText(getPreferences(MODE_PRIVATE).getString(waktu_syuruk, "--"));
				zohor_time.setText(getPreferences(MODE_PRIVATE).getString(waktu_zohor, "--"));
				asar_time.setText(getPreferences(MODE_PRIVATE).getString(waktu_asar, "--"));
				maghrib_time.setText(getPreferences(MODE_PRIVATE).getString(waktu_maghrib, "--"));
				isya_time.setText(getPreferences(MODE_PRIVATE).getString(waktu_isya, "--"));
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