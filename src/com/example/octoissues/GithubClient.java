package com.example.octoissues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/*
 * Client for interacting with the Github API
 */

public class GithubClient {
	
	private static final String GITHUB_BASE_URL = "https://api.github.com/";

	/*
	 * Makes Github API request and returns String response
	 */
	private String getGithubResponse(String endpoint) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(GITHUB_BASE_URL + endpoint);
		String responseBody = null;
		try {
			HttpResponse response = httpClient.execute(request);
			responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseBody;
	}
	
	/*
	 * Get issues for given Github repo, owner and sort type (created/updated). 
	 * Caching client side could be done
	 */
	protected ArrayList<Map<String, String>> getIssues(String owner, String repo, String sortBy) throws JSONException {
		
		String issuesEndpoint = "repos/" + owner + "/" + repo + "/issues?sort?=" + sortBy;
		String responseBody = getGithubResponse(issuesEndpoint);
		
		ArrayList<Map<String, String>> issues = new ArrayList<Map<String, String>>();	
		
		Object responseJSON = new JSONTokener(responseBody).nextValue();
		
		//Check JSON type, if it is a JSONArray it is a valid response, if it is a 
		//JSON Object it is an error payload https://developer.github.com/v3/
		if (responseJSON instanceof JSONArray) {
			JSONArray issuesArray = (JSONArray) responseJSON;
	        for (int i=0; i < issuesArray.length(); i++){
	        	JSONObject issue = issuesArray.getJSONObject(i);
	        	
	        	//Get title and body
	        	String title = issue.getString("title");
	        	String detail = issue.getString("body");
	        	
	        	//Store github issue number for later getting the comments for this particular issue
	        	issues.add(getMapItem(title, detail, Integer.toString(issue.getInt("number"))));
	        }
		    return issues;
		} else if (responseJSON instanceof JSONObject) {
			return getErrorResponse(responseJSON);
		}
		return null;
	}

	/*
	 * Get comments for given Github repo, owner and sort type (created/updated)
	 */
	protected ArrayList<Map<String, String>> getComments(String organization, String repo, String issue_number) throws JSONException {
		
		String commentsEndpoint = "repos/" + organization + "/" + repo + "/issues/" + issue_number + "/comments";
		String responseBody = getGithubResponse(commentsEndpoint);
		
		ArrayList<Map<String, String>> comments = new ArrayList<Map<String, String>>();
		
		Object responseJSON = new JSONTokener(responseBody).nextValue();
		
		if (responseJSON instanceof JSONArray) {
			JSONArray commentsArray = (JSONArray) responseJSON;
	        for (int i=0; i < commentsArray.length(); i++){
	        	JSONObject issue = commentsArray.getJSONObject(i);
	        	
	        	//Only interested in body and the username to be displayed in list dialog
	        	String detail = issue.getString("body");
	        	JSONObject user = issue.getJSONObject("user");
	        	String username = user.getString("login");
	        	comments.add(getMapItem(username, detail, null));
	        }
	        return comments;
		} else if (responseJSON instanceof JSONObject) {
			return getErrorResponse(responseJSON);
		}
	    return null;
	}
	
	/*
	 * Returns HashMap with fields understood by Listview field mappings, could 
	 * be controlled via configuration settings
	 */
	private HashMap<String, String> getMapItem(String header, String body, String issue_number) {
		
    	//Display 140 char snippet for list view
    	String snippet = (body != null && body.length() >= 140) ? body.substring(0, 140) : body;
    	
	    HashMap<String, String> issue = new HashMap<String, String>();
	    issue.put("header", header);
	    issue.put("body", body);
	    issue.put("snippet", snippet);
	    issue.put("issue_number", issue_number);
	    return issue;
	}
	
	/*
	 * Parse error message received from Github
	 */
	private ArrayList<Map<String, String>> getErrorResponse(Object errorMessage) throws JSONException{
		ArrayList<Map<String, String>> error = new ArrayList<Map<String, String>>();
		JSONObject errorResponse = (JSONObject) errorMessage;
		error.add(getMapItem(errorResponse.getString("message"), errorResponse.getString("documentation_url"), null));
		return error;
	}
}
