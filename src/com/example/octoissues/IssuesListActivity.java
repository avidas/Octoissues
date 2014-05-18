package com.example.octoissues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class IssuesListActivity extends Activity {
	
	ListView issuesView, commentsView;
	Dialog commentsDialog;
    private static final String OWNER = "joyent";
    private static final String REPO = "node";
	
	GithubClient client = new GithubClient();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.issues_view);
		
		//Set repository name in view
		TextView repoNameView = (TextView) findViewById(R.id.repoName);
		repoNameView.setText("Repo Issues");
		
		//Associate the listview with id issues_list in the context of this Activity
		issuesView = (ListView) findViewById(R.id.issues_list);
		
		if (isNetworkAvailable()) {		
		    
			//Get issues for repo named rails, owner named rails
			GithubIssuesTask githubIssuesTask = new GithubIssuesTask();
		    githubIssuesTask.execute(OWNER, REPO);
		    
		    issuesView.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View viewCLicked, int position,
						long id) {
					
					if (isNetworkAvailable()) {
						
					    commentsDialog = new Dialog(IssuesListActivity.this);
					    commentsDialog.setCancelable(true);
					    
					    commentsDialog.setContentView(R.layout.comments_view);
					    commentsDialog.setTitle("Comments");
					    
					    //Associate the listview with id comments in the context of commentsDialog
					    commentsView = (ListView) commentsDialog.findViewById(R.id.comments_list);
					    
					    //Suppressing type cast warning here since it is known that type will be HashMap<String, String>
				    	@SuppressWarnings("unchecked")
						Map<String, String>  itemMap = (HashMap<String, String>) issuesView.getItemAtPosition(position);
				        
				    	//Get Github issue number, pass to client to get comments for that issue
				    	int issueNumber = Integer.parseInt(itemMap.get("issue_number"));
				    	GithubCommentsTask githubCommentsTask = new GithubCommentsTask();
				    	githubCommentsTask.execute(OWNER, REPO, Integer.toString(issueNumber));
					    
					}
				}
		    });
		}
	}
	
	/*
	 * Task that make Github API request for issues in the background and post result to 
	 * UI thread when available. 
	 * -- share code/be expose via interfaces with GithubCommentsTask
	 */
	private class GithubIssuesTask extends AsyncTask<String, String, List<Map<String, String>>> {
    	@Override
    	protected void onPreExecute() {
    		//Display feedback to user while background process in running
    		IssuesListActivity.this.setProgressBarIndeterminateVisibility(true);
    	}
    	
		@Override
		protected List<Map<String, String>> doInBackground(String... params) {	
			List<Map<String, String>> issues = null;
			try {
				String owner = params[0];
			    String repo = params[1];
			    //Get issues sorted by last updated
				issues = client.getIssues(owner, repo, "updated");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return issues;
		}		
		
		@Override
		protected void onPostExecute(List<Map<String, String>> issues) {
			if (issues != null) {	
				//Map fieldnames to listview item elements
			    String[] from = { "header", "body" };
			    int[] to = { android.R.id.text1, android.R.id.text2 };
	
			    // Initialize adapter with result as the dataset, using
			    // android.R.layout.simple_list_item_2 for simplicity but custom
			    // layout could be used for better flexibility
			    SimpleAdapter adapter = new SimpleAdapter(IssuesListActivity.this, issues,
			        android.R.layout.simple_list_item_2, from, to);
			    
			    // Associate the adapter with the view, could update dataset later 
			    // by invoking notifyDataSetChanged
			    issuesView.setAdapter(adapter);
				IssuesListActivity.this.setProgressBarIndeterminateVisibility(false);
			} else {
				displayErrorDialog("Error getting Github Issues");
			}
		}
	}
	
	/*
	 * Task that make Github API request for comments in the background and post result to 
	 * UI thread when available. 
	 * -- catching responses, requesting comments for a few issues above and below the current
	 * item would improve response time
	 */
	private class GithubCommentsTask extends AsyncTask<String, String, List<Map<String, String>>> {
		
    	@Override
    	protected void onPreExecute() {
    		IssuesListActivity.this.setProgressBarIndeterminateVisibility(true);
    	}
    	
		@Override
		protected List<Map<String, String>> doInBackground(String... params) {	
			List<Map<String, String>> comments = null;
			try {
				String owner = params[0];
			    String repo = params[1];
			    String issueNumber = params[2];
				comments = client.getComments(owner, repo, issueNumber);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return comments;
		}		
		
		@Override
		protected void onPostExecute(List<Map<String, String>> comments) {	
			if (comments != null) {
			    String[] from = { "header", "body" };
			    int[] to = { android.R.id.text1, android.R.id.text2 };
	            
			    //Use simple list item for comments, issues and errors for now
			    SimpleAdapter adapter = new SimpleAdapter(IssuesListActivity.this, comments,
			        android.R.layout.simple_list_item_2, from, to);
			        
			    IssuesListActivity.this.setProgressBarIndeterminateVisibility(false);
			    commentsView.setAdapter(adapter);
			    commentsDialog.show();
			} else {
				displayErrorDialog("Error getting Github Comments");
			}			    
		}
	}
	
	/*
	 * Display error message in a Dialog
	 */
	private void displayErrorDialog(String message){
		Dialog dialog = new Dialog(IssuesListActivity.this);
		dialog.setTitle(message);
		dialog.show();		
	}
	
	/*
	 * Return true if network is available, otherwise display 
	 * Dialog with Error message and return false
	 */
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    if (!(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting())) {
			Dialog dialog = new Dialog(IssuesListActivity.this);
			dialog.setTitle("No Internet Connection");
			dialog.setCancelable(true);
			dialog.show();
			return false;
	    }
	    return true;
	}
}