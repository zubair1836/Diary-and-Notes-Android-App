package com.diary.diaryandnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.diary.diaryandnotes.Database.SqliteHelper;
import com.diary.diaryandnotes.In_App_Purchase.subscribe_activity;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
//

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MainAdapter

            mainAdapter;

    SqliteHelper helper;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    String smallFont = "small";
    String mediumFont = "medium";
    String largeFont = "large";

    public static String DB_Name = "Diary_notes";

    Button bt_chats, bt_links;

    LinearLayout ll_newfolder;

    public static boolean chat = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        ll_newfolder = findViewById(R.id.ll_newfolder);

        Check_Permisiion();

//        Check_For_DB();

//        Check_For_DB();





        Search_Subscription();

        Show_Dialog();


        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(smallFont,false);
        editor.putBoolean(mediumFont,true);
        editor.putBoolean(largeFont,false);
        editor.commit();

        bt_chats = findViewById(R.id.bt_chats);
        bt_links = findViewById(R.id.bt_links);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bt_chats.setBackground(getResources().getDrawable(R.drawable.cv_background_button));

            bt_links.setBackground(getResources().getDrawable(R.drawable.circle_background));

            bt_chats.setTextColor(getResources().getColor(R.color.white));
            bt_links.setTextColor(getResources().getColor(R.color.main_button_un_click_color));
        }


        helper = new SqliteHelper(MainActivity.this, DB_Name, null, 1);
        helper.getWritableDatabase();


//        String path = new File(Environment.getExternalStorageDirectory(),"/Diary and diaryandnotes/"+DB_Name).toString();
//        Log.e("path","pa = "+path);
//        helper.openDataBase(path);


//
        if (chat) {
            Show_Chats_inrev();
        } else {
            Show_link_folder();

        }


        //        Show_preview("https://stackoverflow.com");

//        Read_DB();

    }

    private void Chek_for_previous_data() {


        String Pref = "DB_Pref";
        String showed = "showed";
        String TAG = "SEARCH_DB";
        SharedPreferences preferances = getSharedPreferences(Pref,MODE_PRIVATE);
        if(preferances.contains(showed)){
            if(preferances.getBoolean(showed,true)){
                Log.d(TAG, "Chek_for_previous_data: contained tru in if ");


            }else{
                Log.d(TAG, "Chek_for_previous_data: contained tru in else");
                Check_For_DB();
            }

        }else{
            Log.d(TAG, "Chek_for_previous_data: contained else ");
            Check_For_DB();
            SharedPreferences.Editor editor = preferances.edit();
            editor.putBoolean(showed,true);
            editor.commit();
        }

    }

    private void Check_Permisiion() {

        String[] perm = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Permissions.check(MainActivity.this, perm, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.e("aaa","Permission is granted1");
////                Chek_for_previous_data();
//                //            return true;
//            } else {
//
//                Log.e("aaa","Permission is revoked1");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
//                //            return false;
//            }
//        }



    }
    private void Check_For_DB() {

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"DN");

        if(f.exists()){
            File f2 = new File(f.getAbsolutePath(),DB_Name);

            if(f2.exists()){
                Load_db();
                Log.e("aaa","db_checked");
//                recreate();

                Show_Chats_inrev();
            }else{
                newDb();
                Log.e("aaa","db_not_checked");
//                recreate();

            }

        }else{

        }

    }

    private void newDb() {

        helper = new SqliteHelper(MainActivity.this, DB_Name, null, 1);
        helper.getWritableDatabase();



    }

    private void Load_db() {

        Log.d("ABC_MNO_PQR", "Load_db: ");

        File backupDB = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/DN/" + DB_Name);

        InputStream mInput = null;
        try {
            mInput = new FileInputStream(backupDB);
            File f = MainActivity.this.getDatabasePath(DB_Name);
            OutputStream mOutput = new FileOutputStream(f.getAbsolutePath());
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
            Log.d("TIS", "Load_db: ");


//            Show_Chats_inrev();

//            Show_Recycler();


//            recreate();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("TAG", "Read_DB: "+e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "Read_DB: "+e);
        }

    }

    private void Show_DIalog_for_exit() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)

                .setIcon(android.R.drawable.ic_dialog_alert)

                .setTitle("Are you sure to Exit")

                .setMessage("Exiting will call finish() method")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        finish();
                    }
                })
                .show();
    }


    public void Show_Recycler(){

        helper = new SqliteHelper(MainActivity.this, DB_Name, null, 1);
        helper.getWritableDatabase();

        ArrayList<String> arl_folder_name = new ArrayList<>();
        arl_folder_name = helper.getFolderList();
        ArrayList<String> arl_lastmsg = new ArrayList<>();
        for (String f_name : arl_folder_name) {

            arl_lastmsg.add(helper.GetLastMessage(f_name));

        }

        if(arl_folder_name.isEmpty()){
            ll_newfolder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        }else{
            ll_newfolder.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            Log.d("abc_mno_pqr", "Show_Chats_inrev: ");

            recyclerView.setAdapter(new MainAdapter(arl_folder_name, arl_lastmsg));
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        }

        ll_newfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show_Create_Folder_Dialog();
            }
        });


    }


//    private void Show_preview(String url) {
//
//        RichLinkView rvp = findViewById(R.id.rvp);
//
//        rvp.setLink(url, new ViewListener() {
//
//            @Override
//            public void onSuccess(boolean status) {
//                Log.d("TAG", "onSuccess: "+status);
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.d("TAG", "onError: "+e);
//
//            }
//        });
//    }

    public void Show_Chats_inrev() {

        chat = true;

        ArrayList<String> arl_folder_name = new ArrayList<>();
        arl_folder_name = helper.getFolderList();
        ArrayList<String> arl_lastmsg = new ArrayList<>();
        for (String f_name : arl_folder_name) {

            arl_lastmsg.add(helper.GetLastMessage(f_name));

        }

        if(arl_folder_name.isEmpty()){
            ll_newfolder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        }else{
            ll_newfolder.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            Log.d("abc_mno_pqr", "Show_Chats_inrev: ");

            recyclerView.setAdapter(new MainAdapter(arl_folder_name, arl_lastmsg));
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        }

        ll_newfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show_Create_Folder_Dialog();
            }
        });

    }

    public void Show_Chats(View view) {
        chat = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bt_chats.setBackground(getResources().getDrawable(R.drawable.cv_background_button));

            bt_links.setBackground(getResources().getDrawable(R.drawable.circle_background));

            bt_chats.setTextColor(getResources().getColor(R.color.white));
            bt_links.setTextColor(getResources().getColor(R.color.main_button_un_click_color));
        }
        Show_Chats_inrev();
        Show_add();

    }
    public void Show_add(){


//        final com.facebook.ads.InterstitialAd  interstitial;
//        final Ad[] adfacebook = new Ad[1];
//
//
////        AdSettings.setTestAdType(AdSettings.TestAdType.CAROUSEL_IMG_SQUARE_LINK);
////        AdSettings.getTestAdType();
//        AdSettings.setDebugBuild(true);
//        interstitial = new com.facebook.ads.InterstitialAd(MainActivity.this, getResources().getString(R.string.facebook_interstial));
//        interstitial.setAdListener(new AbstractAdListener() {
//            public void onAdLoaded(Ad ad) {
//                adfacebook[0] = ad;
//            }
//        });
//        interstitial.loadAd();
//        interstitial.setAdListener(new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//                // Interstitial ad displayed callback
//                Log.e("LogHere", "Interstitial ad displayed.");
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//                // Interstitial dismissed callback
//                Log.e("LogHere", "Interstitial ad dismissed.");
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Ad error callback
//                Log.e("LogHere", "Interstitial ad failed to load: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Interstitial ad is loaded and ready to be displayed
//                Log.d("LogHere", "Interstitial ad is loaded and ready to be displayed!");
//                // Show the ad
//                //  interstitial.loadAd();
//                interstitial.show();
//                adfacebook[0] = ad;
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Ad clicked callback
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Ad impression logged callback
//            }
//        });


//        final InterstitialAd interstitialAd = new InterstitialAd(MainActivity.this);
//        interstitialAd.setAdUnitId("204471580891771_204473564224906");
//
//        Log.d("LogHere","shwoing ads " + this.getResources().getString(R.string.ad_unit_id));
//
//
////        interstitialAd.setAdUnitId("ca-app-pub-1402293978206326/5766724311");
//        interstitialAd.loadAd(new AdRequest.Builder().build());
//
//        interstitialAd.setAdListener(new AdListener(){
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                Log.d("LogHere"," ads failed "+i);
//            }
//
//            @Override
//            public void onAdLoaded() {
//                interstitialAd.show();
//            }
//        });
    }

    public void Show_Links(View view) {
        chat = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bt_chats.setBackground(getResources().getDrawable(R.drawable.circle_background));

            bt_links.setBackground(getResources().getDrawable(R.drawable.cv_background_button));

            bt_chats.setTextColor(getResources().getColor(R.color.main_button_un_click_color));
            bt_links.setTextColor(getResources().getColor(R.color.white));
        }

        Show_link_folder();
        Show_add();

    }
    private void Show_link_folder() {
        chat = false;

        ArrayList<String> arl_folder_name = new ArrayList<>();
        arl_folder_name = helper.getFolderList_for_link_from_linktable();
        Log.d("TAG", "Show_link:folder list: "+arl_folder_name);
        ArrayList<String> arl_lastmsg = new ArrayList<>();
        for (String f_name : arl_folder_name) {

            arl_lastmsg.add(helper.GetLastMessage_for_link(f_name));

        }

        if (arl_folder_name.isEmpty()) {
            ll_newfolder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        } else {
            ll_newfolder.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);


            Rv_Link_Adapter adp = new Rv_Link_Adapter(arl_folder_name, arl_lastmsg);
            adp.notifyDataSetChanged();
            recyclerView.setAdapter(adp);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


//            MainAdapter adp = new MainAdapter(arl_folder_name,arl_lastmsg);
//
//            recyclerView.setAdapter(adp);
//            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        }

        ll_newfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show_Create_Folder_Dialog();
            }
        });

    }

    public void Show_PopUp(View view) {

        ImageView b = findViewById(R.id.more);

        PopupMenu popup = new PopupMenu(MainActivity.this, b);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.main_popupmenu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_createfolder) {

                    Show_Create_Folder_Dialog();

                }
                else if (item.getItemId() == R.id.menu_setting) {
                    Intent i=new Intent(MainActivity.this,Setting_Activity.class);
                    startActivity(i);
                }
                return true;
            }
        });

        popup.show();


    }

    private void Show_Create_Folder_Dialog() {
        final Dialog d = new Dialog(MainActivity.this);
        d.setContentView(R.layout.rename_folder_dialogue_box);
        d.setCanceledOnTouchOutside(false);

        final EditText et_folder_name = d.findViewById(R.id.et_foldername);

        Button create = d.findViewById(R.id.bt_createfolder);
        create.setText("Create");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat) {
                    if (et_folder_name.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Please Eneter Foldername", Toast.LENGTH_SHORT).show();
                    } else {

                        int retval = helper.Add_new_Folder(et_folder_name.getText().toString());
                        if (retval == 1) {
                            d.dismiss();

                            Show_Chats_inrev();
                        }
                    }

                } else {

                    if (et_folder_name.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Please Eneter Foldername", Toast.LENGTH_SHORT).show();
                    } else {

                        int retval = helper.Add_new_Folder_for_link(et_folder_name.getText().toString());
                        if (retval == 1) {
                            d.dismiss();

                            Show_link_folder();
                        }
                    }
                }
            }
        });


        d.show();

    }

    public void Show_Search(View view) {

        if(chat){
            Intent i = new Intent(MainActivity.this, Search_Activity.class);
            i.putExtra("from",0);
            startActivity(i);

        }else{
            Intent i = new Intent(MainActivity.this, Search_Activity.class);
            i.putExtra("from",1);
            startActivity(i);

        }

    }

    private void Search_Subscription() {

        Intent i = new Intent(MainActivity.this, subscribe_activity.class);
        i.putExtra("msg",0);
        startActivity(i);
    }


    public void Show_Dialog(){

        String pref = "mypref";
        String field = "F_DIAlOG";

        SharedPreferences preferences = getSharedPreferences(pref,MODE_PRIVATE);

        if(preferences.contains(field)){

            Log.d("TAG", "Show_Dialog:not Display_ dialog ");

        }else{
//            Log.d("TAG", "Show_Dialog:Display_dialog ");
//            Toast.makeText(this, "DIalod is displayed", Toast.LENGTH_SHORT).show();

            final Dialog d = new Dialog(MainActivity.this);
            d.setContentView(R.layout.aim_dialogue);
            d.setCanceledOnTouchOutside(false);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            ImageView iv_close = d.findViewById(R.id.close_aim);
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
//                    finish();
                    Chek_for_previous_data();
                }
            });

            Button bt_install = d.findViewById(R.id.install_button);
            bt_install.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=in.all.india.shaadi")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=in.all.india.shaadi")));
                    }

                }
            });
            d.show();

//            Check_For_DB();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(field,true);
            editor.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (chat) {
            Show_Chats_inrev();
        } else {
            Show_link_folder();

        }




    }
}
