package my.abdza.solatmalaysia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ZonSolat extends Activity implements OnClickListener {

	private static final String TAG = "ZonSolat";

	private static final String[] negeri = { "Johor", "Kedah", "Kelantan",
			"Melaka", "Negeri Sembilan", "Pahang", "Perak", "Pahang", "Perlis",
			"Pulau Pinang", "Sabah", "Sarawak",
			"Selangor Dan Wilayah Persekutuan", "Terengganu",
			"Wilayah Persekutuan" };

	private static final String[][] nama_kawasan = {
			{ "Kota Tinggi, Mersing, Johor Bahru",
					"Batu Pahat, Muar, Segamat, Gemas", "Kluang dan Pontian",
					"Pulau Aur dan Pemanggil" },
			{ "Puncak Gunung Jerai", "Kota Setar, Kubang Pasu, Pokok Sena",
					"Langkawi", "Pendang, Kuala Muda, Yan",
					"Padang Terap, Sik, Baling", "Kulim, Bandar Baharu" },
			{
					"Jeli, Gua Musang (Mukim Galas, Bertam)",
					"K.Bharu,Bachok,Pasir Puteh,Tumpat,Pasir Mas,Tnh. Merah,Machang,Kuala Krai,Mukim Chiku" },
			{ "Bandar Melaka, Alor Gajah, Jasin, Masjid Tanah, Merlimau, Nyalas" },
			{ "Port Dickson, Seremban, Kuala Pilah, Jelebu, Rembau",
					"Jempol, Tampin" },
			{ "Genting Sempah, Janda Baik, Bukit Tinggi",
					"Bentong, Raub, Kuala Lipis",
					"Maran, Chenor, Temerloh, Bera, Jerantut",
					"Bukit Fraser, Genting Higlands, Cameron Higlands",
					"Kuantan, Pekan, Rompin, Muadzam Shah", "Pulau Tioman" },
			{
					"Bukit Larut",
					"Ipoh, Batu Gajah, Kampar, Sg. Siput dan Kuala Kangsar",
					"Tapah,Slim River dan Tanjung Malim",
					"Pengkalan Hulu, Grik dan Lenggong",
					"Temengor dan Belum",
					"Teluk Intan, Bagan Datoh, Kg.Gajah,Sri Iskandar, Beruas,Parit,Lumut,Setiawan dan Pulau Pangkor",
					"Selama, Taiping, Bagan Serai dan Parit Buntar" },
			{ "Kangar, Padang Besar, Arau" },
			{ "Seluruh Negeri Pulau Pinang" },
			{
					"Zon 9 - Sipitang, Membakut, Beaufort, Kuala Penyu, Weston, Tenom, Long Pa Sia",
					"Zon 8 - Pensiangan, Keningau, Tambunan, Nabawan",
					"Zon 7 - Papar, Ranau, Kota Belud, Tuaran, Penampang, Kota Kinabalu",
					"Zon 6 - Gunung Kinabalu",
					"Zon 5 - Kudat, Kota Marudu, Pitas, Pulau Banggi",
					"Zon 3 - Lahad Datu, Kunak, Silabukan, Tungku, Sahabat, Semporna",
					"Zon 2 - Pinangah, Terusan, Beluran, Kuamut, Telupit",
					"Zon 1 - Sandakan, Bdr. Bkt. Garam, Semawang, Temanggong, Tambisan",
					"Zon 4 - Tawau, Balong, Merotai, Kalabakan" },
			{
					"Zon 1 - Limbang, Sundar, Terusan, Lawas",
					"Zon 8 - Kuching, Bau, Lundu,Sematan",
					"Zon 7 - Samarahan, Simunjan, Serian, Sebuyau, Meludam",
					"Zon 6 - Kabong, Lingga, Sri Aman, Engkelili, Betong, Spaoh, Pusa, Saratok, Roban, Debak",
					"Zon 5 - Belawai, Matu, Daro, Sarikei, Julau, Bitangor, Rajang",
					"Zon 4 - Igan, Kanowit, Sibu, Dalat, Oya",
					"Zon 3 - Song, Belingan, Sebauh, Bintulu, Tatau, Kapit",
					"Zon 2 - Niah, Belaga, Sibuti, Miri, Bekenu, Marudi" },
			{ "Gombak,H.Selangor,Rawang,H.Langat,Sepang,Petaling,S.Alam",
					"Sabak Bernam, Kuala Selangor, Klang, Kuala Langat",
					"Kuala Lumpur", "Putrajaya" },
			{ "Kuala Terengganu, Marang", "Kemaman Dungun", "Hulu Terengganu",
					"Besut, Setiu" }, { "Labuan" } };

	private static final String[][] kod_kawasan = {
			{ "jhr02", "jhr04", "jhr03", "jhr01" },
			{ "kdh06", "kdh01", "kdh05", "kdh02", "kdh03", "kdh04" },
			{ "ktn03", "ktn01" },
			{ "mlk01" },
			{ "ngs02", "ngs01" },
			{ "phg05", "phg04", "phg03", "phg06", "phg02", "phg01" },
			{ "prk07", "prk02", "prk01", "prk03", "prk04", "prk05", "prk06" },
			{ "pls01" },
			{ "png01" },
			{ "sbh09", "sbh08", "sbh07", "sbh06", "sbh05", "sbh03", "sbh02",
					"sbh01", "sbh04" },
			{ "swk01", "swk08", "swk07", "swk06", "swk05", "swk04", "swk03",
					"swk02" }, { "sgr01", "sgr02", "sgr03", "sgr04" },
			{ "trg01", "trg04", "trg03", "trg02" }, { "wly02" } };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zon_solat);

		View selectNegeriButton = findViewById(R.id.select_negeri);
		selectNegeriButton.setOnClickListener(this);

		View selectKawasanButton = findViewById(R.id.select_kawasan);
		selectKawasanButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_negeri:
			openNegeriDialog();
			break;
		case R.id.select_kawasan:
			openKawasanDialog();
			break;
		}
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
		final Button button = (Button) findViewById(R.id.select_negeri);
		button.setText(negeri[i]);
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
		startActivity(intent);
	}

}
