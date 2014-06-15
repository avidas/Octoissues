package com.example.octoissues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class RepoSelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.repo_select_view);
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
    	// TODO Auto-generated method stub
    	getMenuInflater().inflate(R.menu.issues_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    
    public void onClickSignIn(View view) {
    	Intent intent = new Intent(this, IssuesListActivity.class);
    	startActivity(intent);
    }
}
