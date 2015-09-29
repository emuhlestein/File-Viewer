package com.intelliviz.fileviewer;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FileViewFragment extends Fragment {
    public static final String EXTRA_FILE_TEXT = "com.intelliviz.fileviewer.file_text";
    private TextView mTextView;
    private String mText;

    public FileViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mText = (String)getArguments().getCharSequence(EXTRA_FILE_TEXT);
    }

    public static FileViewFragment newInstance(String text) {
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_FILE_TEXT, text);
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

        mTextView.setText(mText);

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


}
