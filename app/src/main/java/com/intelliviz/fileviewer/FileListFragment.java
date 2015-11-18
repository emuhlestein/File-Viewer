package com.intelliviz.fileviewer;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    private File mRoot;
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

        // causes onCreateOptionMenu to get called
        setHasOptionsMenu(true);

        mDir = (File)getArguments().getSerializable(EXTRA_CURR_DIR);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dir_list_fragment_menu, menu);
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

        mRoot = Environment.getExternalStorageDirectory();

        Log.d(LOG_TAG, mDir.toString());
        mFiles = mDir.list();

        mFileList = (ListView)view.findViewById(R.id.fileList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mFiles);

        mFileList.setAdapter(arrayAdapter);

        mFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //QueryPreferences.setCurrentDirectory(getActivity(), mFiles[position]);
                File file = new File(mDir, mFiles[position]);
                Log.d(LOG_TAG, file.toString());
                //mCallback.onFileSelected(file);
                setCurrentFile(file);
            }
        });


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_note:
                // need to launch new note taking fragment
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                NoteFragment fragment = NoteFragment.newInstance(mDir);
                ft.replace(R.id.fragmentHolder, fragment);
                ft.commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void setCurrentFile(File file) {
        Fragment fragment;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(file.isDirectory()) {
            if(!isRoot(file)) {
                getActivity().getActionBar().setSubtitle(file.getName());
            }
            fragment = FileListFragment.newInstance(file);
            ft.replace(R.id.fragmentHolder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            if(file.getName().contains(".txt")) {
                String data = AndroidUtils.readData(file);
                fragment = FileViewFragment.newInstance(file);
                ft.replace(R.id.fragmentHolder, fragment);
                ft.addToBackStack(null);
                ft.commit();
            } else if(file.getName().contains(".pdf")) {
                Intent intent;

                Uri uri = Uri.fromFile(file);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");

                startActivity(intent);
                return;
            } else {
                Toast toast = Toast.makeText(getActivity(), "File cannot be read: " + file.getName(), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }
    }

    private boolean isRoot(File dir) {
        if(dir.toString().equals(mRoot.toString())) {
            return true;
        } else {
            return false;
        }
    }

}
