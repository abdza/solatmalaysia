package com.abdullahsolutions.solatmalaysia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ZonSolat extends Activity {

	private static final String TAG = "ZonSolat";

	private static final String[] negeri = {
            "Johor",
            "Kedah",
            "Kelantan",
			"Melaka",
            "Negeri Sembilan",
            "Pahang",
            "Perlis",
            "Pulau Pinang",
            "Perak",
			"Sabah",
            "Selangor Dan Wilayah Persekutuan",
            "Sarawak",
            "Terengganu",
			"Labuan"
    };

	private static final String[][] nama_kawasan = {
            { "Pulau Aur dan Pemanggil",
            "Kota Tinggi, Mersing, Johor Bahru",
            "Kluang dan Pontian",
            "Batu Pahat, Muar, Segamat, Gemas" },
            { "Kota Setar, Kubang Pasu, Pokok Sena",
            "Pendang, Kuala Muda, Yan",
            "Padang Terap, Sik",
            "Baling",
            "Kulim, Bandar Bahru",
            "Langkawi",
            "Gunung Jerai" },
            { "K.Bharu,Bachok,Pasir Puteh,Tumpat,Pasir Mas,Tnh. Merah,Machang,Kuala Krai,Mukim Chiku",
            "Jeli, Gua Musang (Mukim Galas, Bertam)" },
            { "Bandar Melaka, Alor Gajah, Jasin, Masjid Tanah, Merlimau, Nyalas" },
            { "Jempol, Tampin",
            "Port Dickson, Seremban, Kuala Pilah, Jelebu, Rembau" },
            { "Pulau Tioman",
            "Kuantan, Pekan, Rompin, Muadzam Shah",
            "Maran, Chenor, Temerloh, Bera, Jerantut",
            "Bentong, Raub, Kuala Lipis",
            "Genting Sempah, Janda Baik, Bukit Tinggi",
            "Bukit Fraser, Genting Higlands, Cameron Higlands" },
            { "Kangar, Padang Besar, Arau" },
            { "Seluruh Negeri Pulau Pinang" },
            { "Tapah,Slim River dan Tanjung Malim",
            "Ipoh, Batu Gajah, Kampar, Sg. Siput dan Kuala Kangsar",
            "Pengkalan Hulu, Grik dan Lenggong",
            "Temengor dan Belum",
            "Teluk Intan, Bagan Datoh, Kg.Gajah,Sri Iskandar, Beruas,Parit,Lumut,Setiawan dan Pulau Pangkor",
            "Selama, Taiping, Bagan Serai dan Parit Buntar",
            "Bukit Larut" },
            { "Zon 1 - Sandakan, Bdr. Bkt. Garam, Semawang, Temanggong, Tambisan",
            "Zon 2 - Pinangah, Terusan, Beluran, Kuamut, Telupit",
            "Zon 3 - Lahad Datu, Kunak, Silabukan, Tungku, Sahabat, Semporna",
            "Zon 4 - Tawau, Balong, Merotai, Kalabakan",
            "Zon 5 - Kudat, Kota Marudu, Pitas, Pulau Banggi",
            "Zon 6 - Gunung Kinabalu",
            "Zon 7 - Papar, Ranau, Kota Belud, Tuaran, Penampang, Kota Kinabalu",
            "Zon 8 - Pensiangan, Keningau, Tambunan, Nabawan",
            "Zon 9 - Sipitang, Membakut, Beaufort, Kuala Penyu, Weston, Tenom, Long Pa Sia" },
            { "Gombak,H.Selangor,Rawang, H.Langat,Sepang,Petaling,  S.Alam",
            "Sabak Bernam, Kuala Selangor,  Klang, Kuala Langat",
            "Kuala Lumpur",
            "Putrajaya" },
            { "Zon 1 - Limbang, Sundar, Terusan, Lawas",
            "Zon 2 - Niah, Belaga, Sibuti, Miri, Bekenu, Marudi",
            "Zon 3 - Song, Belingan, Sebauh, Bintulu, Tatau, Kapit",
            "Zon 4 - Igan, Kanowit, Sibu, Dalat, Oya",
            "Zon 5 - Belawai, Matu, Daro, Sarikei, Julau, Bitangor, Rajang",
            "Zon 6 - Kabong, Lingga, Sri Aman, Engkelili, Betong, Spaoh, Pusa, Saratok, Roban, Debak",
            "Zon 7 - Samarahan, Simunjan, Serian, Sebuyau, Meludam",
            "Zon 8 - Kuching, Bau, Lundu,Sematan",
            "Zon 9 - Zon Khas" },
            { "Kuala Terengganu, Marang",
            "Besut, Setiu",
            "Hulu Terengganu",
            "Kemaman Dungun" },
            { "Labuan" }
    };

	private static final String[][] kod_kawasan = {
            { "JHR01","JHR02","JHR03","JHR04" },
            { "KDH01","KDH02","KDH03","KDH04","KDH05","KDH06","KDH07" },
            { "KTN01","KTN03" },
            { "MLK01" },
            { "NGS01","NGS02" },
            { "PHG01","PHG02","PHG03","PHG04","PHG05","PHG06" },
            { "PLS01" },
            { "PNG01" },
            { "PRK01","PRK02","PRK03","PRK04","PRK05","PRK06","PRK07" },
            { "SBH01","SBH02","SBH03","SBH04","SBH05","SBH06","SBH07","SBH08","SBH09" },
            { "SGR01","SGR02","SGR03","SGR04" },
            { "SWK01","SWK02","SWK03","SWK04","SWK05","SWK06","SWK07","SWK08","SWK09" },
            { "TRG01","TRG02","TRG03","TRG04" },
            { "WLY02" }
    };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zon_solat);
		openNegeriDialog();
	}

	private void openNegeriDialog() {
		new AlertDialog.Builder(this).setTitle(R.string.select_negeri_default)
				.setItems(negeri, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						selectnegeri(i);
					}
				}).show();
	}

	private void selectnegeri(int i) {
		Log.d(TAG, "selected negeri " + i);
		getPreferences(MODE_PRIVATE).edit().putLong("negeri", i).commit();		
		openKawasanDialog();
	}

	private void openKawasanDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.select_kawasan_default)
				.setItems(
						nama_kawasan[(int) getPreferences(MODE_PRIVATE)
								.getLong("negeri", 0)],
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								selectkawasan(i);
							}
						}).show();
	}

	private void selectkawasan(int i) {
		Log.d(TAG, "selected kawasan " + i);
		getPreferences(MODE_PRIVATE).edit().putLong("kawasan", i).commit();
		Intent intent = new Intent(this, waktusolat.class);
		intent.putExtra("kod_kawasan", kod_kawasan[(int) getPreferences(MODE_PRIVATE).getLong("negeri", 0)][i]);
		intent.putExtra("kawasan", nama_kawasan[(int) getPreferences(MODE_PRIVATE).getLong("negeri", 0)][i]);
		intent.putExtra("negeri", negeri[(int) getPreferences(MODE_PRIVATE).getLong("negeri", 0)]);
		setResult(RESULT_OK,intent);
		finish();
	}
}
