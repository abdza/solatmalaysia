package com.abdullahsolutions.solatmalaysia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;

public class waktusolat extends Activity implements OnClickListener {

	private static final String TAG = "waktusolat";
	public static final String PREFS_NAME = "MyPrefsFile";

	private TextView imsak_time, subuh_time, syuruk_time, zohor_time,
			asar_time, maghrib_time, isya_time, kawasan, negeri, kemaskini;
	public static final String waktu_imsak = "waktu_imsak";
	public static final String waktu_subuh = "waktu_subuh";
	public static final String waktu_syuruk = "waktu_syuruk";
	public static final String waktu_zohor = "waktu_zohor";
	public static final String waktu_asar = "waktu_asar";
	public static final String waktu_maghrib = "waktu_maghrib";
	public static final String waktu_isya = "waktu_isya";
	public static final String waktu_kemaskini = "kemaskini";

	static final int pick_kawasan_request = 0;

	private SolatDB solatdb;

	private SharedPreferences settings;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences(PREFS_NAME, 0);
		setContentView(R.layout.main);
		View selectZoneButton = findViewById(R.id.select_zone);
		selectZoneButton.setOnClickListener(this);
		findViews();
		updateview();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateview();
	}
	
	private void setalarm(Context context, String nextwaktu,String waktu) {
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
		Log.d(TAG,"Azan set by waktusolat at " + time.toString());
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

	public void updateview() {
		negeri.setText(settings.getString("negeri",
				"Selangor Dan Wilayah Persekutuan"));
		kawasan.setText(settings.getString("kawasan", "Kuala Lumpur"));
		kemaskini.setText(settings.getString("kemaskini", "Belum Pernah"));
		SimpleDateFormat cdf = new SimpleDateFormat("HH:mm");
		try {
			String curtime = (String) android.text.format.DateFormat.format(
					"kk:mm", new java.util.Date());
			String curday = (String) android.text.format.DateFormat.format(
					"dd-MM-yyyy", new java.util.Date());			
			solatdb = new SolatDB(this.getApplicationContext());
			SQLiteDatabase db = solatdb.getReadableDatabase();
			Cursor cursorwaktu = db
					.rawQuery(
							"select imsak,subuh,syuruk,zohor,asar,maghrib,isya,tarikh,kawasan from waktusolat where tarikh='"
									+ curday
									+ "' and kawasan='"
									+ getkodkawasan() + "'", null);
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
				kemaskini.setText(cursorwaktu.getString(7));
				Log.d(TAG, cursorwaktu.getString(8) + "::::" + getkodkawasan());
				solatdb.close();
				imsak_time.setText(strimsak);
				String nextwaktu="";
				String waktu="";
				if (curdate.equals(cdf.parseObject(strimsak))
						|| (curdate.after(cdf.parse(strimsak)) && curdate
								.before(cdf.parse(strsubuh)))) {
					highlight(R.id.imsak_title, imsak_time, true);
					//setalarm(this.getApplicationContext(),strsubuh,"imsak");
					nextwaktu=strsubuh;
					waktu="imsak";
				} else {
					highlight(R.id.imsak_title, imsak_time, false);
				}
				subuh_time.setText(strsubuh);
				if (curdate.equals(cdf.parseObject(strsubuh))
						|| (curdate.after(cdf.parse(strsubuh)) && curdate
								.before(cdf.parse(strsyuruk)))) {
					highlight(R.id.subuh_title, subuh_time, true);
					//setalarm(this.getApplicationContext(),strsyuruk,"subuh");
					nextwaktu=strsyuruk;
					waktu="subuh";
				} else {
					highlight(R.id.subuh_title, subuh_time, false);
				}
				syuruk_time.setText(strsyuruk);
				if (curdate.equals(cdf.parseObject(strsyuruk))
						|| (curdate.after(cdf.parse(strsyuruk)) && curdate
								.before(cdf.parse(strzohor)))) {
					highlight(R.id.syuruk_title, syuruk_time, true);
					//setalarm(this.getApplicationContext(),strzohor,"syuruk");
					nextwaktu=strzohor;
					waktu="syuruk";
				} else {
					highlight(R.id.syuruk_title, syuruk_time, false);
				}
				zohor_time.setText(strzohor);
				if (curdate.equals(cdf.parseObject(strzohor))
						|| (curdate.after(cdf.parse(strzohor)) && curdate
								.before(cdf.parse(strasar)))) {
					highlight(R.id.zohor_title, zohor_time, true);
					//setalarm(this.getApplicationContext(),strasar,"zohor");
					nextwaktu=strasar;
					waktu="zohor";
				} else {
					highlight(R.id.zohor_title, zohor_time, false);
				}
				asar_time.setText(strasar);
				if (curdate.equals(cdf.parseObject(strasar))
						|| (curdate.after(cdf.parse(strasar)) && curdate
								.before(cdf.parse(strmaghrib)))) {
					highlight(R.id.asar_title, asar_time, true);
					//setalarm(this.getApplicationContext(),strmaghrib,"asar");
					nextwaktu=strmaghrib;
					waktu="asar";
				} else {
					highlight(R.id.asar_title, asar_time, false);
				}
				maghrib_time.setText(strmaghrib);
				if (curdate.equals(cdf.parseObject(strmaghrib))
						|| (curdate.after(cdf.parse(strmaghrib)) && curdate
								.before(cdf.parse(strisya)))) {
					highlight(R.id.maghrib_title, maghrib_time, true);
					//setalarm(this.getApplicationContext(),strisya,"maghrib");
					nextwaktu=strisya;
					waktu="maghrib";
				} else {
					highlight(R.id.maghrib_title, maghrib_time, false);
				}
				isya_time.setText(strisya);
				if (curdate.equals(cdf.parseObject(strisya))
						|| curdate.after(cdf.parse(strisya))
						|| curdate.before(cdf.parse(strimsak))) {
					highlight(R.id.isya_title, isya_time, true);	
					//setalarm(this.getApplicationContext(),strimsak,"isya");
					nextwaktu=strimsak;
					waktu="isya";
				} else {
					highlight(R.id.isya_title, isya_time, false);
				}
				if(settings.getString("curwaktu","tiada")!=waktu){
					settings.edit().putString("curwaktu",waktu).commit();
					setalarm(this.getApplicationContext(), nextwaktu, waktu);
				}
				/* Calendar c = Calendar.getInstance();
				int month = c.get(Calendar.MONTH) + 1;
				int year = c.get(Calendar.YEAR);
				solatdb = new SolatDB(this.getApplicationContext());
				db = solatdb.getReadableDatabase();
				month += 1;
				if (month > 12) {
					month = 1;
					year += 1;
				}
				curday = "01-" + month + "-" + year;
				cursorwaktu = db.rawQuery(
						"select _id from waktusolat where tarikh='" + curday
								+ "' and kawasan='" + getkodkawasan() + "'",
						null);
				if (cursorwaktu.getCount() == 0) {
					Intent i = new Intent(this, ZonSolat.class);
					Bundle bundle = i.getExtras();
					solatdb.close();
					new JadualSolatDepan(this).execute(bundle);
				}
				else{
					solatdb.close();
				} */
				
			} else {
				solatdb.close();
				imsak_time.setText("Updating...");
				subuh_time.setText("Updating...");
				syuruk_time.setText("Updating...");
				zohor_time.setText("Updating...");
				asar_time.setText("Updating...");
				maghrib_time.setText("Updating...");
				isya_time.setText("Updating...");
				Intent i = new Intent(this, ZonSolat.class);
				Bundle bundle = i.getExtras();
				new JadualSolat(this).execute(bundle);
			}
		} catch (Exception e) {
			Log.d(TAG, "Rejectedexception", e);
		}
	}

	private void highlight(int titleint, TextView time, boolean hightlight) {
		if (hightlight) {
			TextView title = (TextView) findViewById(titleint);
			title.setBackgroundResource(R.color.hightlight);
			time.setBackgroundResource(R.color.hightlight);
		} else {
			TextView title = (TextView) findViewById(titleint);
			title.setBackgroundResource(R.color.green);
			time.setBackgroundResource(R.color.green);
		}
	}

	public String getkodkawasan() {
		return settings.getString("kod_kawasan", "sgr03");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_zone:
			Intent i = new Intent(this, ZonSolat.class);
			startActivityForResult(i, pick_kawasan_request);
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == pick_kawasan_request) {
			if (resultCode == RESULT_OK) {
				if (data.hasExtra("kod_kawasan")) {
					Bundle extras = data.getExtras();
					String kod_kawasan = extras.getString("kod_kawasan");
					settings.edit().putString("kod_kawasan", kod_kawasan)
							.commit();

					String kawasan = extras.getString("kawasan");
					settings.edit().putString("kawasan", kawasan).commit();

					String negeri = extras.getString("negeri");
					settings.edit().putString("negeri", negeri).commit();
					Log.d(TAG, "kod_kawasan:" + kod_kawasan);

					settings.edit().putBoolean("reset", true).commit();
					ComponentName me = new ComponentName(this,
							SolatWidget.class);
					AppWidgetManager mgr = AppWidgetManager.getInstance(this);

					mgr.updateAppWidget(me,
							SolatWidget.updateview(getApplicationContext()));
					settings.edit().putBoolean("updatetime", true).commit();
					/* updatewaktu.run(); */
					String curday = (String) android.text.format.DateFormat
							.format("dd-MM-yyyy", new java.util.Date());
					solatdb = new SolatDB(this.getApplicationContext());
					SQLiteDatabase db = solatdb.getReadableDatabase();
					Cursor cursorwaktu = db
							.rawQuery(
									"select imsak,subuh,syuruk,zohor,asar,maghrib,isya,tarikh,kawasan from waktusolat where tarikh='"
											+ curday
											+ "' and kawasan='"
											+ getkodkawasan() + "'", null);
					if (cursorwaktu.getCount() == 0) {
						Intent i = new Intent(this, ZonSolat.class);
						Bundle bundle = i.getExtras();
						new JadualSolat(this).execute(bundle);
					}
					solatdb.close();
				}
			}
		}
	}

	public void updatejadualsolat(View view) {
		Toast toast;
		toast = Toast.makeText(getApplicationContext(),
				"Jadual solat sedang dikemaskini", Toast.LENGTH_SHORT);
		toast.show();
		Intent i = new Intent(this, ZonSolat.class);
		Bundle bundle = i.getExtras();
		new JadualSolat(this).execute(bundle);
	}

	private class JadualSolat extends AsyncTask<Bundle, Void, Boolean> {

		private SolatDB solatdb;
		private String[][] waktuwaktu;
		waktusolat context;

		public JadualSolat(final waktusolat context) {
			this.context = context;
		}

		protected Boolean doInBackground(Bundle... bundles) {
			waktuwaktu = new String[32][];
			Log.d(TAG, "Updating waktu solat");
			HttpURLConnection con = null;
			try {
				Calendar c = Calendar.getInstance();
				int month = c.get(Calendar.MONTH) + 1;
				int year = c.get(Calendar.YEAR);

				URL url = new URL("http://abdullahsolutions.com/esolat/esolat.php?"
						+ "zon=" + getkodkawasan() + "&tahun=" + year + "&bulan=" + month);
				con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(1000);
				con.setConnectTimeout(1000);
				con.setRequestMethod("GET");
				con.addRequestProperty("Referer",
						"http://blog.abdullahsolutions.com");
				con.setDoInput(true);
				con.connect();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "UTF-8"));

				String payload;
				String total = "";				
				while ((payload = reader.readLine()) != null) {
					total += payload;
				}
				String htmlTextStr = Html.fromHtml(total).toString();
				Log.d(TAG,"Found: " + htmlTextStr);
				Pattern waktupattern = Pattern
						.compile("(\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)");
				Matcher waktumatch = waktupattern.matcher(htmlTextStr);
				int curpos = 0;
				while (waktumatch.find()) {					
					waktuwaktu[curpos] = new String[8];
					waktuwaktu[curpos][0] = waktumatch.group(1) + "-" + String.format("%02d",month) + "-" + year;					
					waktuwaktu[curpos][1] = waktumatch.group(2);
					waktuwaktu[curpos][2] = waktumatch.group(3);
					waktuwaktu[curpos][3] = waktumatch.group(4);
					waktuwaktu[curpos][4] = waktumatch.group(5);
					waktuwaktu[curpos][5] = waktumatch.group(6);
					waktuwaktu[curpos][6] = waktumatch.group(7);
					waktuwaktu[curpos][7] = waktumatch.group(8);
					curpos++;
				}

			} catch (MalformedURLException e) {
				Log.d(TAG, "Malformed url exception ", e);
			} catch (IOException e) {
				Log.d(TAG, "IO exception ", e);
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			Toast toast;
			if (context != null) {
				solatdb = new SolatDB(this.context);
				SQLiteDatabase db = solatdb.getWritableDatabase();
				for (int i = 0; i < 31; i++) {
					if (waktuwaktu[i] != null) {
						ContentValues values = new ContentValues();
						db.execSQL("delete from waktusolat where kawasan='"
								+ getkodkawasan() + "' and tarikh='"
								+ waktuwaktu[i][0] + "'");
						values.put("kawasan", getkodkawasan());
						values.put("tarikh", waktuwaktu[i][0]);
						values.put("imsak", waktuwaktu[i][1]);
						values.put("subuh", waktuwaktu[i][2]);
						values.put("syuruk", waktuwaktu[i][3]);
						values.put("zohor", waktuwaktu[i][4]);
						values.put("asar", waktuwaktu[i][5]);
						values.put("maghrib", waktuwaktu[i][6]);
						values.put("isya", waktuwaktu[i][7]);
						db.insertOrThrow("waktusolat", null, values);
						Log.d(TAG, values.toString());
					}
				}
				solatdb.close();
				toast = Toast.makeText(getApplicationContext(),
						"Jadual solat telah dikemaskini", Toast.LENGTH_SHORT);
				toast.show();
				ComponentName me = new ComponentName(this.context, SolatWidget.class);
				AppWidgetManager mgr = AppWidgetManager.getInstance(this.context);

				mgr.updateAppWidget(me, SolatWidget.updateview(getApplicationContext()));
				updateview();
			}
		}
	}

	private class JadualSolatDepan extends AsyncTask<Bundle, Void, Boolean> {

		private SolatDB solatdb;
		private String[][] waktuwaktu;
		waktusolat context;

		public JadualSolatDepan(final waktusolat context) {
			this.context = context;
		}

		protected Boolean doInBackground(Bundle... bundles) {
			waktuwaktu = new String[32][];
			Log.d(TAG, "Updating waktu solat");
			HttpURLConnection con = null;
			try {
				Calendar c = Calendar.getInstance();
				int month = c.get(Calendar.MONTH) + 1;
				int year = c.get(Calendar.YEAR);
				month += 1;

				if (month > 12) {
					month = 1;
					year += 1;
				}

				URL url = new URL("http://abdullahsolutions.com/esolat/esolat.php?"
						+ "zon=" + getkodkawasan() + "&tahun=" + year + "&bulan=" + month);
				con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(1000);
				con.setConnectTimeout(1000);
				con.setRequestMethod("GET");
				con.addRequestProperty("Referer",
						"http://blog.abdullahsolutions.com");
				con.setDoInput(true);
				con.connect();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "UTF-8"));

				String payload;
				String total = "";				
				while ((payload = reader.readLine()) != null) {
					total += payload;
				}
				String htmlTextStr = Html.fromHtml(total).toString();
				Pattern waktupattern = Pattern
						.compile("(\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)\\s+(\\d+:\\d+)");
				Matcher waktumatch = waktupattern.matcher(htmlTextStr);
				int curpos = 0;
				while (waktumatch.find()) {
					waktuwaktu[curpos] = new String[8];
					waktuwaktu[curpos][0] = waktumatch.group(1) + "-" + String.format("%02d",month) + "-" + year;					
					waktuwaktu[curpos][1] = waktumatch.group(2);
					waktuwaktu[curpos][2] = waktumatch.group(3);
					waktuwaktu[curpos][3] = waktumatch.group(4);
					waktuwaktu[curpos][4] = waktumatch.group(5);
					waktuwaktu[curpos][5] = waktumatch.group(6);
					waktuwaktu[curpos][6] = waktumatch.group(7);
					waktuwaktu[curpos][7] = waktumatch.group(8);
					curpos++;
				}
			} catch (MalformedURLException e) {
				Log.d(TAG, "Malformed url exception ", e);
			} catch (IOException e) {
				Log.d(TAG, "IO exception ", e);
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {			
			if (context != null) {
				solatdb = new SolatDB(this.context);
				SQLiteDatabase db = solatdb.getWritableDatabase();
				for (int i = 0; i < 31; i++) {
					if (waktuwaktu[i] != null) {
						ContentValues values = new ContentValues();
						db.execSQL("delete from waktusolat where kawasan='"
								+ getkodkawasan() + "' and tarikh='"
								+ waktuwaktu[i][0] + "'");
						values.put("kawasan", getkodkawasan());
						values.put("tarikh", waktuwaktu[i][0]);
						values.put("imsak", waktuwaktu[i][1]);
						values.put("subuh", waktuwaktu[i][2]);
						values.put("syuruk", waktuwaktu[i][3]);
						values.put("zohor", waktuwaktu[i][4]);
						values.put("asar", waktuwaktu[i][5]);
						values.put("maghrib", waktuwaktu[i][6]);
						values.put("isya", waktuwaktu[i][7]);
						db.insertOrThrow("waktusolat", null, values);
						Log.d(TAG, "Next month: " + values.toString());
					}
				}
				solatdb.close();
			}
		}
	}
}
