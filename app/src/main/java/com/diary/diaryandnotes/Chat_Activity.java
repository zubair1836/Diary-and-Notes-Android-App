package com.diary.diaryandnotes;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Chat_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private RadioButton small,medium,large;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    private LinearLayout chat_backup_linearlayout;
    private LinearLayout font_size_linear_layout;
    String smallFont = "small";
    String mediumFont = "medium";
    String largeFont = "large";
    String filename = "/data/" + "com.diary.diaryandnotes" + "/databases/" + MainActivity.DB_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        font_size_linear_layout = (LinearLayout) findViewById(R.id.font_size_linear_layout);
        chat_backup_linearlayout = (LinearLayout) findViewById(R.id.chat_backup_linear_layout);
        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        font_size_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogue();
            }
        });

        chat_backup_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] perm = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

                Permissions.check(Chat_Activity.this, perm, null, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {

                        SvaeDB();
                        Toast.makeText(Chat_Activity.this, "File saved in folder DN", Toast.LENGTH_SHORT).show();

                    }
                });
//                writeData(getStorageDir(filename));


//                Intent intent = new Intent(Chat_Activity.this,Chat_Backup_Activity.class);
//                startActivity(intent);
            }
        });

    }
    //---------------------------create folder---------------------

    private void SvaeDB() {

        String Directory_name = "DN";


//        File  f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),Directory_name);
//
//        if(!f.exists()){
//            Log.d("FOlder_creation", "SvaeDB: "+f);
//            f.mkdir();
//        }
//
//        File STORED_FILE = new File(f.getAbsolutePath(),MainActivity.DB_Name);
//
//
//        File sd = Environment.getExternalStorageDirectory();
//        File data = Environment.getDataDirectory();
//        FileChannel source=null;
//        FileChannel destination=null;
//        String currentDBPath = Chat_Activity.this.getDatabasePath(MainActivity.DB_Name).toString();
//        String backupDBPath = STORED_FILE.getAbsolutePath();
//        File currentDB = new File(data, currentDBPath);
//        Log.d("ABC_mno", "SvaeDB: "+currentDB);
//        File backupDB = new File(sd, backupDBPath);
//        try {
//            source = new FileInputStream(currentDB).getChannel();
//            destination = new FileOutputStream(backupDB).getChannel();
//            destination.transferFrom(source, 0, source.size());
//            source.close();
//            destination.close();
//            Toast.makeText(this, "Your Database is Exported !!", Toast.LENGTH_LONG).show();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//        }

        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/"+ "com.diary.diaryandnotes" +"/databases/"+MainActivity.DB_Name ;
        String backupDBPath = MainActivity.DB_Name;

        File f = new File(sd, Directory_name);

        if (!f.exists()) {
            f.mkdir();
        }


        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(f, backupDBPath);
        Log.d("TAG", "SvaeDB: " + backupDB);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Your Database is Exported !!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



//    public void writeData(String filePath) {
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
////            fileOutputStream.write(data.getText().toString().getBytes());
//            fileOutputStream.close();
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //checks if external storage is available for read and write
//    public boolean isExternalStorageAvailable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            return true;
//        }
//        return false;
//    }
//
//    //checks if external storage is available for read
//    public boolean isExternalStorageReadable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//            return true;
//        }
//        return false;
//    }
//
//    public String getStorageDir(String fileName) {
//        //create folder
//        File file = new File(Environment.getExternalStorageDirectory(), "DN");
//        if (!file.mkdirs()) {
//            file.mkdirs();
//        }
//        String filePath = file.getAbsolutePath() + File.separator + fileName;
//        return filePath;
//    }


    public void showdialogue(){
        final Dialog d = new Dialog(Chat_Activity.this);
        d.setContentView(R.layout.font_size_dialogue);
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(false);

        radioGroup = (RadioGroup) d.findViewById(R.id.radio_button_font_size_group);
        small = (RadioButton) d.findViewById(R.id.small_font_radio_button);
        medium = (RadioButton) d.findViewById(R.id.medium_font_radio_button);
        large = (RadioButton) d.findViewById(R.id.large_font_radio_button);

        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(smallFont,true)){
            small.setChecked(true);
        }else {
            small.setChecked(false);
        }
        if (sharedPreferences.getBoolean(mediumFont,true)){
            medium.setChecked(true);
        }else {
            medium.setChecked(false);
        }
        if (sharedPreferences.getBoolean(largeFont,true)){
            large.setChecked(true);
        }else {
            large.setChecked(false);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (small.isChecked())
                {
                    sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(smallFont,true);
                    editor.putBoolean(mediumFont,false);
                    editor.putBoolean(largeFont,false);
                    editor.commit();
                    d.dismiss();
                }
                else if (medium.isChecked())
                {
                    sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(smallFont,false);
                    editor.putBoolean(mediumFont,true);
                    editor.putBoolean(largeFont,false);
                    editor.commit();
                    d.dismiss();
                }
                else
                {
                    sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(smallFont,false);
                    editor.putBoolean(mediumFont,false);
                    editor.putBoolean(largeFont,true);
                    editor.commit();
                    d.dismiss();
                }
            }
        });
        d.show();
    }
}
