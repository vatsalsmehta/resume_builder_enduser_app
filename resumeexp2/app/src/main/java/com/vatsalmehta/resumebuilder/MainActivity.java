package com.vatsalmehta.resumebuilder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vatsalmehta.resumebuilder.datamodel.Resume;
import com.vatsalmehta.resumebuilder.fragments.EducationFragment;
import com.vatsalmehta.resumebuilder.fragments.EssentialsFragment;
import com.vatsalmehta.resumebuilder.fragments.ExperienceFragment;
import com.vatsalmehta.resumebuilder.fragments.PersonalInfoFragment;
import com.vatsalmehta.resumebuilder.fragments.PreviewFragment;
import com.vatsalmehta.resumebuilder.fragments.ProjectsFragment;
import com.vatsalmehta.resumebuilder.helper.ResumeFragment;

public class MainActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    private Resume resume;
    private String currentTitle;
    private String STATE_CURRENT_TITLE = "current title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLayout();

        Gson gson = new Gson();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = mPrefs.getString("SerializableObject", "");
        if (json.isEmpty())
            resume = Resume.createNewResume();
        else
            resume = gson.fromJson(json, Resume.class);

        if (savedInstanceState == null) {
            openFragment(PersonalInfoFragment.newInstance(resume));
            currentTitle = getString(R.string.navigation_personal_info);
        } else
            currentTitle = savedInstanceState.getString(STATE_CURRENT_TITLE);

        getSupportActionBar().setTitle(currentTitle);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(resume);
        prefsEditor.putString("SerializableObject", json);
        prefsEditor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_CURRENT_TITLE, currentTitle);
    }

    private void setupLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        drawerLayout.closeDrawers();
                        currentTitle = item.getTitle().toString();
                        getSupportActionBar().setTitle(currentTitle);
                        return handleMenuItem(item);
                    }
                });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.content_description_open_drawer,
                R.string.content_description_close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(getString(R.string.steps));
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(currentTitle);
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private boolean handleMenuItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_personal_info:
                openFragment(PersonalInfoFragment.newInstance(resume));
                break;
            case R.id.action_essentials:
                openFragment(EssentialsFragment.newInstance(resume));
                break;
            case R.id.action_projects:
                openFragment(ProjectsFragment.newInstance(resume));
                break;
            case R.id.action_education:
                openFragment(EducationFragment.newInstance(resume));
                break;
            case R.id.action_experience:
                openFragment(ExperienceFragment.newInstance(resume));
                break;
            case R.id.action_preview:
                openFragment(PreviewFragment.newInstance(resume));
                break;
            default:
                return false;
        }
        return true;
    }

    private void openFragment(ResumeFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }


    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imgView);

                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {

                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();

        }

    }



}
