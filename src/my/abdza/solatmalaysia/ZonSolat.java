package my.abdza.solatmalaysia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ZonSolat extends Activity implements OnClickListener {
	
	private static final String TAG="ZonSolat";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zon_solat);		
		
		View selectNegeriButton = findViewById(R.id.select_negeri);
		selectNegeriButton.setOnClickListener(this);		
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.select_negeri:			
			openNegeriDialog();
			break;
		}		
	}
	
	private void openNegeriDialog() {
		new AlertDialog.Builder(this)
			.setTitle(R.string.select_negeri_default)
			.setItems(R.array.negeri,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialoginterface,
						int i) {
					selectnegeri(i);
				}
			})
			.show();
	}
	
	private void selectnegeri(int i) {
		Log.d(TAG,"selected negeri " + i);
	}
	
	private void openKawasanDialog() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.select_kawasan_default)
		.setItems(R.array.negeri,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface,
					int i) {
				selectkawasan(i);
			}
		})
		.show();
	}
	
	private void selectkawasan(int i) {
		Log.d(TAG,"selected kawasan " + i);
	}

}
