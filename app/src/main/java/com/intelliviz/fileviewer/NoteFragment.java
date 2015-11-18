package com.intelliviz.fileviewer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class NoteFragment extends Fragment {
    public static final int REQUEST_ITEM = 0;
    private static final String CURR_DIR_KEY = "curr_dir";
    private static final String DEFAULT_FILE_NAME = "Untitled.txt";
    private Button mSave;
    private EditText mText;
    private File mFile;
    private String mFileName;
    private boolean mNeedToSave = false;

    public static NoteFragment newInstance(File currDir) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURR_DIR_KEY, currDir);
        fragment.setArguments(args);
        return fragment;
    }

    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFile = (File)getArguments().getSerializable(CURR_DIR_KEY);
            if(mFile.isDirectory()) {
                mNeedToSave = true;
                mFileName = DEFAULT_FILE_NAME;
                mFile = new File(mFile, mFileName);
            } else {
                mFileName = mFile.getName();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        mText = (EditText) view.findViewById(R.id.edit_text);
        mSave = (Button)view.findViewById(R.id.saveButton);
        mSave.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mFile.getName().equals(DEFAULT_FILE_NAME)) {
                    SimpleTextDialog dialog = SimpleTextDialog.newInstance("Enter new file name", "", "File Name", false);
                    dialog.setTargetFragment(NoteFragment.this, REQUEST_ITEM);
                    dialog.show(getFragmentManager(), "Dial1");
                } else {
                    AndroidUtils.writeData(mFile, mText.getText().toString());
                }
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ITEM) {
            if (resultCode == Activity.RESULT_OK) {
                String fileName = data.getStringExtra(SimpleTextDialog.EXTRA_TEXT);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateUI() {
        String data = AndroidUtils.readData(mFile);
        mText.setText(data);
        getActivity().getActionBar().setSubtitle(mFileName);
    }

}
