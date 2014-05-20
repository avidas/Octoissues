package com.example.octoissues;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditOwnerRepoDialog extends DialogFragment {
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


}
