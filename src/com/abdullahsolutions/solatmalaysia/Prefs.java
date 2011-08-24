package com.abdullahsolutions.solatmalaysia;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
	public static boolean getAzan(Context context,String waktu){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(waktu,true);
	}
}
