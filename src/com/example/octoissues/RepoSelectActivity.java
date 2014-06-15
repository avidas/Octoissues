package com.example.octoissues;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

public class RepoSelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.repo_select_view);
		ActionBar actionBar = getActionBar();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	        actionBar.setHomeButtonEnabled(true);
		}
        
        
        NumberPicker picker = (NumberPicker) findViewById(R.id.repoPicker);
        String[] values = new String[]{"first", "second"};
        picker.setMinValue(1);
        picker.setMaxValue(2);
        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(values);
        picker.setValue(1);
        Button signin = (Button) findViewById(R.id.view_issues);
        signin.setTextSize(30);    
    }
    
     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.issues_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
     
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_settings:
    		startActivity(new Intent(this, PrefsActivity.class));
    		return true;
    	case R.id.saved_issues:
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    public void onClickSignIn(View view) {
    	Intent intent = new Intent(this, IssuesListActivity.class);
    	startActivity(intent);
    }
}
