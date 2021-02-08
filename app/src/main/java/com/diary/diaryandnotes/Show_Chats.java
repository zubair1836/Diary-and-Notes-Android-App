package com.diary.diaryandnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diary.diaryandnotes.Database.SqliteHelper;
import com.diary.diaryandnotes.In_App_Purchase.subscribe_activity;
import com.diary.diaryandnotes.Model.Link_Details;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Show_Chats extends AppCompatActivity {

    public static RecyclerView rv_links;
    public static LinearLayoutManager manager;

    String folder_name;

    ArrayList<Link_Details> arl_link = new ArrayList<>();

    SqliteHelper helper;

    EditText et_link, et_serachtext;

    ArrayList<String> arl_final_ds = new ArrayList<>();

    TextView tv_foldername;

    TextView tv_count;
    ImageView iv_copycb, iv_delete, iv_clear_selection, iv_cancelsearch;

    LinearLayout ll_f_visible, ll_longpress_visible, ll_serach_visible;

    ArrayList<String> Search_Data = new ArrayList<>();
    ArrayList<Integer> Search_Count = new ArrayList<>();

    String Search_String = "";

    ImageView iv_previous, iv_next;
    int Current_search_position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__chats);

        folder_name = getIntent().getStringExtra("folder");

        DBINITIALIZE();


//
//        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
//                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//                    @Override
//                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                        // Show the ad.
//                    }
//                })
//                .withAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(int errorCode) {
//                        Log.d("LogHere","failed "+errorCode);
//                        // Handle the failure by logging, altering the UI, and so on.
//                    }
//                })
//                .withNativeAdOptions(new NativeAdOptions.Builder()
//                        // Methods in the NativeAdOptions.Builder class can be
//                        // used here to specify individual options settings.
//                        .build())
//                .build();
//
//
//        adLoader.loadAd(new AdRequest.Builder().build());

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Log.d("LogHere","failed "+errorCode);
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });


        init();

        AssortFullData(false, Search_String);


    }

    private void AssortFullData(boolean serach, String Search_String) {

        arl_final_ds.clear();

        arl_link = helper.getLinks_for_Folder_name(folder_name);

        ArrayList<String> arl_date = new ArrayList<>();

        HashMap<String,String> link_time = new HashMap<>();


        for (int i = 0; i < arl_link.size(); i++) {
            if (!arl_date.contains(arl_link.get(i).getLink_Date())) {
                arl_date.add(arl_link.get(i).getLink_Date());
            }
        }

        for (String single_date : arl_date) {

            arl_final_ds.add(single_date);

            for (int i = 0; i < arl_link.size(); i++) {

                if (arl_link.get(i).getLink_Date().equals(single_date)) {
                    arl_final_ds.add(arl_link.get(i).getLink_data());
                }
            }
        }

        link_time.clear();

        for(Link_Details det : arl_link){
            Log.d("TAG", "AssortFullData: "+det.getLink_time());
            link_time.put(det.getLink_data(),det.getLink_time());
        }

        Log.d("TAG", "SetRecyclerview: " + arl_final_ds);

        SetRecyclerview(serach, Search_String,link_time);


    }


    public void DBINITIALIZE() {
        helper = new SqliteHelper(Show_Chats.this, MainActivity.DB_Name, null, 1);
        helper.getWritableDatabase();

    }

    private void init() {
        rv_links = findViewById(R.id.rv_links);

        tv_foldername = findViewById(R.id.tv_folder_name);
        tv_foldername.setText(folder_name);

        et_link = findViewById(R.id.et_link);

        tv_count = findViewById(R.id.tv_count);

        iv_copycb = findViewById(R.id.iv_copy_cb);
        iv_delete = findViewById(R.id.iv_delete);
        iv_clear_selection = findViewById(R.id.iv_clear_selection);
        iv_cancelsearch = findViewById(R.id.iv_cancel_search);

        ll_f_visible = findViewById(R.id.ll_f_visible);
        ll_longpress_visible = findViewById(R.id.ll_long_pressvisible);
        ll_serach_visible = findViewById(R.id.ll_serach_visible);

        et_serachtext = findViewById(R.id.et_serachtext);

        iv_previous = findViewById(R.id.iv_previous);
        iv_next = findViewById(R.id.iv_next);


    }

    private void SetRecyclerview(boolean search, String Search_String,HashMap<String,String> link_time) {

        SharedPreferences preferences = Show_Chats.this.getSharedPreferences(subscribe_activity.mypref, Context.MODE_PRIVATE);
        boolean ad_is_visible = true;
        if(preferences.contains(subscribe_activity.subscribed)){
            if(preferences.getBoolean(subscribe_activity.subscribed,false)){
                ad_is_visible = false;
            }else{
                ad_is_visible = true;
                SetAds();

            }

        }else{
            ad_is_visible = true;
            SetAds();

        }

        Rv_Linkdetails rv_linkdetails = new Rv_Linkdetails(arl_final_ds, ll_f_visible, ll_longpress_visible, ll_serach_visible, iv_clear_selection, iv_delete, iv_copycb, tv_count, helper, folder_name, search, Search_String, iv_next, iv_previous,link_time,ad_is_visible);
        rv_links.setAdapter(rv_linkdetails);

        manager = new LinearLayoutManager(Show_Chats.this);
        manager.setStackFromEnd(true);
        rv_links.setLayoutManager(manager);
//        rv_links.smoothScrollToPosition(arl_final_ds.size() - 1);


    }

    private void SetAds() {
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");


        for (int i = 0; i < arl_final_ds.size(); i++) {

            try {
                Date javaDate = sdfrmt.parse(arl_final_ds.get(i));
//                Log.d("TAG", "getItemViewType: " +);

            } catch (ParseException e) {

                if (i % 10 == 0) {
                    arl_final_ds.add(i, "adview");
                } else {

                }

            }


        }


    }

    public void show_more(View view) {

        ImageView b = findViewById(R.id.iv_more);

        PopupMenu popup = new PopupMenu(Show_Chats.this, b);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.more_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_clearchat) {

                    ClearChat();

                } else if (item.getItemId() == R.id.menu_deletefolder) {
                    DeleteFolder();

                } else if (item.getItemId() == R.id.menu_rename) {
                    RenameFolder();
                } else if (item.getItemId() == R.id.menu_search) {
                    SearchData();
                }
                return true;
            }
        });

        popup.show();

    }

    private void SearchData() {

        ll_serach_visible.setVisibility(View.VISIBLE);
        ll_f_visible.setVisibility(View.INVISIBLE);
        ll_longpress_visible.setVisibility(View.INVISIBLE);

        iv_cancelsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_serach_visible.setVisibility(View.INVISIBLE);
                ll_f_visible.setVisibility(View.VISIBLE);
                AssortFullData(false, Search_String);
            }
        });


        et_serachtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                Search_Data.clear();
                Search_Count.clear();

                for (String check : arl_final_ds) {
                    if (check.contains(s)) {
                        Search_Data.add(check);
                        Search_Count.add(arl_final_ds.indexOf(check));
                        Log.d("TAG", "onTextChanged:searchde " + Search_Count);
                    }
                }

                Log.d("TAG", "onTextChanged: " + Search_Count.size());
                Log.d("TAG", "onTextChanged: " + Search_Count.isEmpty());
                if (!Search_Count.isEmpty()) {
                    Current_search_position = 0;
                    Search_String = Search_Data.get(Current_search_position);
                    rv_links.smoothScrollToPosition(Search_Count.get(0));
                }

                AssortFullData(true, Search_String);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TAG = "STACK";
                if(!et_serachtext.getText().toString().equals(null)) {
                    if (!Search_Count.isEmpty()) {
                        if (Current_search_position < Search_Count.size() - 1) {
                            Log.d(TAG, "onClick:cur: " + Current_search_position);
                            Log.d(TAG, "onClick:size: " + Search_Count.size());
                            Current_search_position++;
                            AssortFullData(true, Search_Data.get(Current_search_position));
                            rv_links.smoothScrollToPosition(Search_Count.get(Current_search_position));
                        } else {
                            Log.d(TAG, "onClick:cur: " + Current_search_position);
                            Log.d(TAG, "onClick:size: " + Search_Count.size());
                            Toast.makeText(Show_Chats.this, "Last Element", Toast.LENGTH_SHORT).show();
                            Current_search_position = 0;
                            AssortFullData(true, Search_Data.get(Current_search_position));
                            rv_links.smoothScrollToPosition(Search_Count.get(Current_search_position));
                        }
                    }
                }

            }
        });

        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TAG = "MInus_STACK";

                if(!et_serachtext.getText().toString().equals(null)) {
                    if(!Search_Count.isEmpty()) {
                        if (Current_search_position > 0) {
                            Log.d(TAG, "onClick:cur: " + Current_search_position);
                            Log.d(TAG, "onClick:size: " + Search_Count.size());
                            Current_search_position--;

                            AssortFullData(true, Search_Data.get(Current_search_position));
                            rv_links.smoothScrollToPosition(Search_Count.get(Current_search_position));
                        } else {
                            Toast.makeText(Show_Chats.this, "First Element", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClick:cur: " + Current_search_position);
                            Log.d(TAG, "onClick:size: " + Search_Count.size());
                            Current_search_position = Search_Count.size() - 1;
                            AssortFullData(true, Search_Data.get(Current_search_position));
                            rv_links.smoothScrollToPosition(Search_Count.get(Current_search_position));
                        }
                    }
                }


            }
        });


    }

    private void RenameFolder() {

        final Dialog d = new Dialog(Show_Chats.this);
        d.setContentView(R.layout.rename_folder_dialogue_box);
        d.setCanceledOnTouchOutside(false);

        final EditText et_folder_name = d.findViewById(R.id.et_foldername);

        Button create = d.findViewById(R.id.bt_createfolder);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_folder_name.getText().toString().equals("")) {
                    Toast.makeText(Show_Chats.this, "Enter new Name", Toast.LENGTH_SHORT).show();

                } else {
                    String f_name = et_folder_name.getText().toString();

                    helper.Rename_Folder(folder_name, f_name);

                    tv_foldername.setText(f_name);
                    d.dismiss();
                }

            }
        });


        d.show();
    }

    private void DeleteFolder() {

        final Dialog d = new Dialog(Show_Chats.this);
        d.setContentView(R.layout.delete_folder_dialogue_box);

        Button bt_yes = d.findViewById(R.id.delete_dia_yes_button);
        Button bt_no = d.findViewById(R.id.delete_dia_no_button);

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.DeleteFolder(folder_name);

//                Intent i = new Intent(Show_Chats.this, MainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                finish();
                onBackPressed();
                d.dismiss();

            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d.dismiss();

            }
        });

        d.show();




    }

    private void ClearChat() {

        final Dialog d = new Dialog(Show_Chats.this);
        d.setContentView(R.layout.delete_folder_dialogue_box);

        Button bt_yes = d.findViewById(R.id.delete_dia_yes_button);
        Button bt_no = d.findViewById(R.id.delete_dia_no_button);

        TextView tv_delete = d.findViewById(R.id.tv_deletetext);

        tv_delete.setText("Are you sure you want to clear all diaryandnotes in this folder?");

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int abc = helper.ClearChat(folder_name);
                Log.d("TAG", "ClearChat: " + abc);
                AssortFullData(false, Search_String);
                Toast.makeText(Show_Chats.this, "Chat cleared", Toast.LENGTH_SHORT).show();

                d.dismiss();

            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d.dismiss();

            }
        });

        d.show();






    }

    public void Save_Link(View view) {

        if (et_link.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter Link", Toast.LENGTH_SHORT).show();

        } else {

            String linkdetails = et_link.getText().toString();

            Calendar c = Calendar.getInstance();

            int month = c.get(Calendar.MONTH) + 1;

            String cur_time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

            String Final_time = SetTextIntoTextView(cur_time);
            Log.d("TAG", "Save_Link: " + Final_time);

            String cur_date = +c.get(Calendar.DATE) + "/" + month + "/" + c.get(Calendar.YEAR);

            Log.d("abc", "Save_Link: " + cur_date);
            Log.d("TAG", "Save_Link: " + linkdetails);

            helper.Add_Link(folder_name, cur_date, linkdetails, Final_time);

            et_link.setText("");

            AssortFullData(false, Search_String);

        }


    }

    public void back(View view) {

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        Intent i = new Intent(Show_Chats.this, MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
    }


    private String SetTextIntoTextView(String CUST_TIME) {

        String[] ary_time = CUST_TIME.split(":");
        String SLOT = "AM";

        int h = Integer.parseInt(ary_time[0]);
//        int m = Integer.parseInt(ary_time[1]);

        if (h > 12) {
            h = h - 12;
            SLOT = "PM";
        }

        String Set_in_text = h + ":" + ary_time[1] + " " + SLOT;

        return Set_in_text;

    }
}
