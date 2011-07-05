package com.abdullahsolutions.solatmalaysia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;

public class waktusolatTask implements Runnable {

	private static final String TAG = "waktusolatTask";	
	private final waktusolat waktu;

	waktusolatTask(waktusolat waktu) {
		this.waktu = waktu;
	}

	public void run() {
		HttpURLConnection con = null;
		try {
			if (Thread.interrupted())
				throw new InterruptedException();
			
			URL url = new URL("http://www.e-solat.gov.my/solat.php?"
					+ "kod=" + waktu.getkodkawasan() + "&lang=Eng"
					+ "&url=http://blog.abdullahsolutions.com");
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(1000);
			con.setConnectTimeout(1000);
			con.setRequestMethod("GET");
			con.addRequestProperty("Referer",
					"http://blog.abdullahsolutions.com");
			con.setDoInput(true);
			con.connect();
			
			Log.d(TAG, "Trying to get latest data");
			
			if(Thread.interrupted())
				throw new InterruptedException();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(),"UTF-8"));
			
			String payload;
			String total="";
			int i=0;
			while((payload=reader.readLine())!=null){
				total+=payload;				
			}			
			
			String htmlTextStr = Html.fromHtml(total).toString();			
			Pattern waktupattern = Pattern.compile("(\\d+:\\d+)");			
			Matcher waktumatch = waktupattern.matcher(htmlTextStr);			
			int pos=0;
			while(waktumatch.find()){				
				switch(pos){
				case 0:					
					waktu.savewaktu(waktusolat.waktu_imsak,waktumatch.group());
					break;
				case 1:
					waktu.savewaktu(waktusolat.waktu_subuh,waktumatch.group());
					break;
				case 2:
					waktu.savewaktu(waktusolat.waktu_syuruk,waktumatch.group());
					break;
				case 3:
					waktu.savewaktu(waktusolat.waktu_zohor,waktumatch.group());
					break;
				case 4:
					waktu.savewaktu(waktusolat.waktu_asar,waktumatch.group());
					break;
				case 5:
					waktu.savewaktu(waktusolat.waktu_maghrib,waktumatch.group());
					break;
				case 6:
					waktu.savewaktu(waktusolat.waktu_isya,waktumatch.group());
					break;				
				}				
				pos++;				
			}			
			waktu.savekemaskini();
			waktu.updatewaktuview();

			reader.close();

		} catch (InterruptedException e) {
			Log.d(TAG, "Interrupted exception ", e);
		} catch (MalformedURLException e) {
			Log.d(TAG, "Malformed url exception ", e);
		} catch (IOException e) {
			Log.d(TAG, "IO exception ", e);
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}
}
