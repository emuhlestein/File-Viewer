package com.intelliviz.fileviewer;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class FileViewFragment extends Fragment {
    public static final String EXTRA_FILE = "com.intelliviz.fileviewer.file";
    private TextView mTextView;
    private File mFile;

    public FileViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mFile = (File)getArguments().getSerializable(EXTRA_FILE);
    }

    public static FileViewFragment newInstance(File file) {
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_FILE, file);
        FileViewFragment fragment = new FileViewFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_view, container, false);

        mTextView = (TextView)view.findViewById(R.id.textView);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        updateUI();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean whiteBackground = prefs.getBoolean("white_background", false);

        if(whiteBackground) {
            mTextView.setBackgroundColor(Color.WHITE);
            mTextView.setTextColor(Color.BLACK);
        } else {
            mTextView.setBackgroundColor(Color.BLACK);
            mTextView.setTextColor(Color.WHITE);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.file_view_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_note:
                // need to launch new note taking fragment
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                NoteFragment fragment = NoteFragment.newInstance(mFile);
                ft.replace(R.id.fragmentHolder, fragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        String data = AndroidUtils.readData(mFile);
        mTextView.setText(data);
        getActivity().getActionBar().setSubtitle(mFile.getName() + " (Read Only)");
    }
}
