package com.intelliviz.fileviewer;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class FileListFragment extends Fragment {
    public static final String LOG_TAG = FileListFragment.class.getSimpleName();
    public static final String EXTRA_CURR_DIR= "com.intelliviz.fileviewer.current_dir";
    private ListView mFileList;
    private String[] mFiles;
    private File mDir;
    private Callback mCallback;

    public interface Callback {
        void onFileSelected(File file);
    }

    public FileListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDir = (File)getArguments().getSerializable(EXTRA_CURR_DIR);
    }

    @Override
    public void onResume() {
        super.onResume();
        String name = mDir.getName();

        if(name.equals("0")) {
            getActivity().getActionBar().setSubtitle("");
        } else {
            getActivity().getActionBar().setSubtitle(name);
        }
    }

    public static FileListFragment newInstance(File dir) {
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_CURR_DIR, dir);
        FileListFragment fragment = new FileListFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);

        //mDir = Environment.getExternalStorageDirectory();
        //mDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        String path = mDir.getAbsolutePath();

        Log.d(LOG_TAG, mDir.toString());
        File[] documents = mDir.listFiles();
        mFiles = mDir.list();


        mFileList = (ListView)view.findViewById(R.id.fileList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mFiles);

        mFileList.setAdapter(arrayAdapter);

        mFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(mDir, mFiles[position]);
                Log.d(LOG_TAG, file.toString());
                //String data = AndroidUtils.readData(file);
                //Log.d(LOG_TAG, data);
                mCallback.onFileSelected(file);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (Callback)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
