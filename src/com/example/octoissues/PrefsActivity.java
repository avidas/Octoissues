package com.example.octoissues;

import android.app.Activity;
import android.os.Bundle;

public class PrefsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prefs_activity);
		
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new PrefsFragment())
		.commit();
	}

}
