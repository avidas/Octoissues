package com.example.octoissues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        picker.setDisplayedValues(values);
        Button signin = (Button) findViewById(R.id.view_issues);
        signin.setTextSize(30);    
    }
    
    public void onClickSignIn(View view) {
    	Intent intent = new Intent(this, IssuesListActivity.class);
    	startActivity(intent);
    }
}
