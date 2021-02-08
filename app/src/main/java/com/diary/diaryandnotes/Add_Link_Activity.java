package com.diary.diaryandnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Add_Link_Activity extends AppCompatActivity {

    String Folder_name;

    TextView tv_foldername;
//    TextView tv_linkpreview;

    TextView tv_preview;

//    RichLinkView richLinkView;


    DisplayMetrics matrix = new DisplayMetrics();

    SqliteHelper helper;


    public static RecyclerView rv_links;
    public static LinearLayoutManager manager;


    ArrayList<Link_Details> arl_link = new ArrayList<>();


    EditText et_link, et_serachtext;

    ArrayList<String> arl_final_ds = new ArrayList<>();


    TextView tv_count;
    ImageView iv_copycb, iv_delete, iv_clear_selection, iv_cancelsearch;

    LinearLayout ll_f_visible, ll_longpress_visible, ll_serach_visible;

    ArrayList<String> Search_Data = new ArrayList<>();
    ArrayList<Integer> Search_Count = new ArrayList<>();

    String Search_String = "";

    ImageView iv_previous, iv_next;
    int Current_search_position = 0;


//    TextCrawler textCrawler;

    LinearLayout ll_preview;
    ImageView iv_img;
    TextView tv_sitetitle, tv_sitedesc;



    String share_link;

    TextView textView;

    LinearLayout linearLayout;


//    RichLinkView lvp;







    String Gated_url ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__link_);

        getWindowManager().getDefaultDisplay().getMetrics(matrix);

        Folder_name = getIntent().getStringExtra("foldername");

        INITIALIZEDB();

        init();

        AssortFullData(false, Search_String);


//        tv_linkpreview = findViewById(R.id.tv_linkpreview);
        et_link = findViewById(R.id.et_link);
        tv_foldername = findViewById(R.id.tv_folder_name);

        tv_foldername.setText(Folder_name);

        int w = (int) (matrix.heightPixels / 2);
        int h = matrix.heightPixels / 3;

//        tv_linkpreview.setLayoutParams(new LinearLayout.LayoutParams(w, h));

        et_link.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

               // tv_linkpreview.setText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String url = "https://stackoverflow.com";




    }

    private void init() {
        rv_links = findViewById(R.id.rv_links);

        tv_foldername = findViewById(R.id.tv_folder_name);
        tv_foldername.setText(Folder_name);

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


//        TextCrawler crawler = new TextCrawler();

//        richLinkView.setLink("https://stackoverflow.com/", new ViewListener() {
//            @Override
//            public void onSuccess(boolean status) {
//                Log.d("TAG", "onSuccess: ");
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });

//        LinkPreviewCallback linkPreviewCallback = new LinkPreviewCallback() {
//            @Override
//            public void onPre() {
//                // Any work that needs to be done before generating the preview. Usually inflate
//                // your custom preview layout here.
//
//            }
//
//            @Override
//            public void onPos(SourceContent sourceContent, boolean b) {
//                // Populate your preview layout with the results of sourceContent.
//
//                Log.d("TAG", "onPos: "+sourceContent.isSuccess());
//                Log.d("TAG", "onPos: "+sourceContent.getImages().get(0));
//
//            }
//        };
//
//        crawler.makePreview( linkPreviewCallback, "https://stackoverflow.com/");


    }

    private void AssortFullData(boolean serach, String Search_String) {

        arl_final_ds.clear();

        arl_link = helper.getLinks_for_Folder_name_from_linktable(Folder_name);

        ArrayList<String> arl_date = new ArrayList<>();

        HashMap<String, String> link_time = new HashMap<>();


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

        for (Link_Details det : arl_link) {
            Log.d("TAG", "AssortFullData: " + det.getLink_time());
            link_time.put(det.getLink_data(), det.getLink_time());
        }

        Log.d("TAG", "SetRecyclerview: " + arl_final_ds);

        SetRecyclerview(serach, Search_String, link_time);


    }

    private void SetRecyclerview(boolean search, String Search_String, HashMap<String, String> link_time) {

        SharedPreferences preferences = Add_Link_Activity.this.getSharedPreferences(subscribe_activity.mypref, Context.MODE_PRIVATE);
        boolean ad_is_visible = true;
        if (preferences.contains(subscribe_activity.subscribed)) {
            if (preferences.getBoolean(subscribe_activity.subscribed, false)) {
                ad_is_visible = false;
            } else {
                ad_is_visible = true;
                SetAds();

            }

        } else {
            ad_is_visible = true;
            SetAds();

        }


        Log.d("TAG", "SetRecyclerview: " + arl_final_ds);

        Rv_Linkdetails_for_link_tabel_displayes rv_linkdetails = new Rv_Linkdetails_for_link_tabel_displayes(arl_final_ds, ll_f_visible, ll_longpress_visible, ll_serach_visible, iv_clear_selection, iv_delete, iv_copycb, tv_count, helper, Folder_name, search, Search_String, iv_next, iv_previous, link_time, ad_is_visible);

        rv_links.setAdapter(rv_linkdetails);

        manager = new LinearLayoutManager(Add_Link_Activity.this);
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

                if (i % 7 == 0) {
                    arl_final_ds.add(i, "adview");
                } else {

                }

            }


        }


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
                        Toast.makeText(Add_Link_Activity.this, "Last Element", Toast.LENGTH_SHORT).show();
                        Current_search_position = 0;
                        AssortFullData(true, Search_Data.get(Current_search_position));
                        rv_links.smoothScrollToPosition(Search_Count.get(Current_search_position));
                    }
                }

            }
        });

        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TAG = "MInus_STACK";

                if (Current_search_position > 0) {
                    Log.d(TAG, "onClick:cur: " + Current_search_position);
                    Log.d(TAG, "onClick:size: " + Search_Count.size());
                    Current_search_position--;

                    AssortFullData(true, Search_Data.get(Current_search_position));
                    rv_links.smoothScrollToPosition(Search_Count.get(Current_search_position));
                } else {
                    Toast.makeText(Add_Link_Activity.this, "First Element", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick:cur: " + Current_search_position);
                    Log.d(TAG, "onClick:size: " + Search_Count.size());
                    Current_search_position = Search_Count.size() - 1;
                    AssortFullData(true, Search_Data.get(Current_search_position));
                    rv_links.smoothScrollToPosition(Search_Count.get(Current_search_position));
                }


            }
        });


    }

    private void INITIALIZEDB() {

        helper = new SqliteHelper(Add_Link_Activity.this, MainActivity.DB_Name, null, 1);
        helper.getWritableDatabase();
    }

    private void Preview_Init(String p_url) {

        ll_preview = findViewById(R.id.ll_preview);
//        int w = (int) (matrix.heightPixels / 2);
//        int h = matrix.heightPixels / 2;

//        ll_preview.setLayoutParams(new LinearLayout.LayoutParams(w, h));

        iv_img = findViewById(R.id.iv_img_url);
        tv_sitetitle = findViewById(R.id.tv_sitename);
        tv_sitedesc = findViewById(R.id.tv_site_desc);

        TextCrawler textCrawler = new TextCrawler();

// ..


        LinkPreviewCallback linkPreviewCallback = new LinkPreviewCallback() {
            @Override
            public void onPre() {
                // Any work that needs to be done before generating the preview. Usually inflate
                // your custom preview layout here.
            }

            @Override
            public void onPos(SourceContent sourceContent, boolean b) {
                // Populate your preview layout with the results of sourceContent.


                if (sourceContent.isSuccess()) {

                    Log.d("TAG", "onPos: " + sourceContent.isSuccess());
                    Log.d("TAG", "onPos:onpos " + b);

                    List<String> arl_img_url = sourceContent.getImages();

                    String img_for_set = null;

                    if (!arl_img_url.isEmpty()) {
                        img_for_set = arl_img_url.get(0);
                    }


                    Async async = new Async(img_for_set);
                    async.execute();


//                    Picasso.get().load(img_for_set).into(iv_img, new Callback() {
//                        @Override
//                        public void onSuccess() {
//
//
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            iv_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
//
//                        }
//                    });

                    tv_sitetitle.setText(sourceContent.getTitle());
                    tv_sitedesc.setText(sourceContent.getDescription());


//                    Log.d("TAG", "onPos: " + sourceContent.getImages().get(0));
//                    Log.d("TAG", "onPos: " + sourceContent.getTitle());
//                    Log.d("TAG", "onPos: " + sourceContent.getDescription());
                } else {
                    Toast.makeText(Add_Link_Activity.this, "Please Check your link", Toast.LENGTH_SHORT).show();
                }

            }
        };

        textCrawler.makePreview(linkPreviewCallback, p_url);


    }

    public void back(View view) {
        onBackPressed();
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

    public void share(View view) {
        if(!share_link.equals("")) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share_link);
            startActivity(Intent.createChooser(sharingIntent, "Choose Option"));
        }else{
            Toast.makeText(this, "Please add Link", Toast.LENGTH_SHORT).show();
        }


    }

    public void delete(View view) {

        et_link.setText("");
        linearLayout.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);


        //tv_linkpreview.setText("Link Preview");

    }

    public void Show_more(View view) {


        ImageView b = findViewById(R.id.iv_more);

        PopupMenu popup = new PopupMenu(Add_Link_Activity.this, b);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.more_menu_add_link, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_clearchat_1) {

                    ClearChat();

                } else if (item.getItemId() == R.id.menu_deletefolder_1) {
                    DeleteFolder();

                } else if (item.getItemId() == R.id.menu_rename_1) {
                    RenameFolder();
                }
                return true;
            }
        });

        popup.show();

    }


    public void Save_Link(View view) {

        if (et_link.getText().toString().equals("")) {

            Toast.makeText(this, "Please Enter Link details", Toast.LENGTH_SHORT).show();

        } else {

//            Preview_Init(et_link.getText().toString());

            String linkdetails = et_link.getText().toString();

            Calendar c = Calendar.getInstance();

            int month = c.get(Calendar.MONTH) + 1;

            String cur_time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

            String Final_time = SetTextIntoTextView(cur_time);
            Log.d("TAG", "Save_Link: " + Final_time);

            String cur_date = +c.get(Calendar.DATE) + "/" + month + "/" + c.get(Calendar.YEAR);

            Log.d("abc", "Save_Link: " + cur_date);
            Log.d("TAG", "Save_Link: " + linkdetails);
            Log.d("TAG", "Save_Link: " + Folder_name);

            helper.Add_Link_for_link_table(Folder_name, cur_date, linkdetails, Final_time);

            et_link.setText("");


            AssortFullData(false, Search_String);








//            textView = findViewById(R.id.link_pre_text_id);
//            textView.setVisibility(View.GONE);
//
//            linearLayout = findViewById(R.id.ll_preview);
//            linearLayout.setVisibility(View.VISIBLE);
//
//            Preview_Init(et_link.getText().toString());
//
//            share_link = et_link.getText().toString();
//            String link = et_link.getText().toString();
//
//            Calendar c = Calendar.getInstance();
//
//            int month = c.get(Calendar.MONTH) + 1;
//
//            String cur_time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
//
//            String Final_time = SetTextIntoTextView(cur_time);
//            Log.d("TAG", "Save_Link: " + Final_time);
//
//            String cur_date = +c.get(Calendar.DATE) + "/" + month + "/" + c.get(Calendar.YEAR);
//
////            helper.Add_Link(Folder_name, cur_date, link, Final_time);
//            et_link.setText("");
//            //tv_linkpreview.setText("Link Preview");
//
//            Gated_url = et_link.getText().toString();


        }


    }

    private void RenameFolder() {

        final Dialog d = new Dialog(Add_Link_Activity.this);
        d.setContentView(R.layout.rename_folder_dialogue_box);
        d.setCanceledOnTouchOutside(false);

        final EditText et_folder_name = d.findViewById(R.id.et_foldername);

        Button create = d.findViewById(R.id.bt_createfolder);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_folder_name.getText().toString().equals("")) {
                    Toast.makeText(Add_Link_Activity.this, "Enter new Name of folder", Toast.LENGTH_SHORT).show();

                } else {
                    String f_name = et_folder_name.getText().toString();

                    helper.Rename_Folder_for_link(Folder_name, f_name);

                    tv_foldername.setText(f_name);
                    d.dismiss();
                }

            }
        });


        d.show();
    }

    private void DeleteFolder() {

        final Dialog d = new Dialog(Add_Link_Activity.this);
        d.setContentView(R.layout.delete_folder_dialogue_box);

        Button bt_yes = d.findViewById(R.id.delete_dia_yes_button);
        Button bt_no = d.findViewById(R.id.delete_dia_no_button);

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                helper.DeleteFolder(Folder_name);

                helper.DeleteFolder_for_link(Folder_name);

//                Intent i = new Intent(Add_Link_Activity.this, MainActivity.class);
////                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(i);
                finish();

//                helper.DeleteFolder_for_link(Folder_name);
//                finish();
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

        final Dialog d = new Dialog(Add_Link_Activity.this);
        d.setContentView(R.layout.delete_folder_dialogue_box);

        Button bt_yes = d.findViewById(R.id.delete_dia_yes_button);
        Button bt_no = d.findViewById(R.id.delete_dia_no_button);

        TextView tv_delete = d.findViewById(R.id.tv_deletetext);

        tv_delete.setText("Are you sure you want to clear all diaryandnotes in this folder?");

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.ClearChat_for_link(Folder_name);


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

    class Async extends AsyncTask {
        String url_1;

        Async(String url_1) {
            this.url_1 = url_1;
            Log.d("TAG", "Async: "+url_1);

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if(!(url_1 == (null))) {

//                URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
//                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                imageView.setImageBitmap(bmp);


                try {
                    URL myfileurl = new URL(url_1);
                    HttpURLConnection conn = (HttpURLConnection) myfileurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    int length = conn.getContentLength();
                    if (length > 0) {
                        int[] bitmapData = new int[length];
                        byte[] bitmapData2 = new byte[length];
                        InputStream is = conn.getInputStream();
                        Bitmap bmImg = BitmapFactory.decodeStream(is);

                        final Bitmap finalBmImg = bmImg;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                iv_img.setImageBitmap(finalBmImg);

                            }
                        });

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        iv_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));

                    }
                });
            }

            return null;


        }
    }

}
