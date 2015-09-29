package com.intelliviz.fileviewer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends BasicFragmentActivity implements FileListFragment.Callback {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ListView mFileList;
    private String[] mFiles;
    private File mDir;
    private File mRoot;

    @Override
    protected Fragment createFragment() {
        mDir = Environment.getExternalStorageDirectory();
        mRoot = mDir;
        Fragment fragment = FileListFragment.newInstance(mDir);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Fragment fragment;
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = SettingsFragment.newInstance();
            ft.replace(R.id.fragmentHolder, fragment);
            ft.addToBackStack(null);
            ft.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFileSelected(File file) {
        Fragment fragment;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(file.isDirectory()) {
            if(!isRoot(file)) {
                getActionBar().setSubtitle(file.getName());
            }
            fragment = FileListFragment.newInstance(file);
        } else {
            if(file.getName().contains(".txt")) {
                String data = AndroidUtils.readData(file);
                fragment = FileViewFragment.newInstance(data);
            } else if(file.getName().contains(".pdf")) {
                Intent intent;

                Uri uri = Uri.fromFile(file);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");

                startActivity(intent);
                return;
            } else {
                Toast toast = Toast.makeText(this, "File cannot be read: " + file.getName(), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }

        ft.replace(R.id.fragmentHolder, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private boolean isRoot(File dir) {
        if(dir.toString().equals(mRoot.toString())) {
            return true;
        } else {
            return false;
        }
    }
}
