package com.example.octoissues;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditOwnerRepoDialog extends DialogFragment implements OnEditorActionListener{
	
	public interface EditRepoDialogListener {
		void onFinishEditDialog(String inputText);
	}
	
	private EditText editOwner;
	private EditText editRepo;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_repo, container);
        editOwner = (EditText) view.findViewById(R.id.owner_name);
        editRepo = (EditText) view.findViewById(R.id.repo_name);
        getDialog().setTitle("Enter Owner and Repo");

        return view;
    }

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


}
