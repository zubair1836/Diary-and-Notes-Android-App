package com.diary.diaryandnotes;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.diaryandnotes.Database.SqliteHelper;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class Rv_Linkdetails_for_link_tabel_displayes extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int dis_date = 0;
    int Link = 1;
    int Ad = 2;

    Context c;

    Boolean Long_Press_Enable = false;

    ArrayList<String> Selected_Links = new ArrayList<>();

    ArrayList<String> arl_linkdetails;
    LinearLayout ll_f_visible;
    LinearLayout ll_longpres_visible;
    LinearLayout ll_search;
    ImageView iv_clearselection;
    ImageView iv_delete;
    ImageView iv_copycb;
    TextView tv_count;
    public static final String mypreference = "mypref";

    SqliteHelper helper;
    String Folder_name;
    boolean serach;
    String Searcg_String;
    ImageView iv_next;
    ImageView iv_previous;

    HashMap<String, String> link_time;

    ArrayList<View> itemview_search = new ArrayList<>();
    int Displayed_itemview = 0;
    boolean ad_is_visible;

    Rv_Linkdetails_for_link_tabel_displayes(ArrayList<String> arl_linkdetails, LinearLayout ll_f_visible, LinearLayout ll_longpres_visible, LinearLayout ll_search, ImageView iv_clearselection, ImageView iv_delete, ImageView iv_copycb, TextView tv_count, SqliteHelper helper, String Folder_name, boolean serach, String Search_String, ImageView iv_next, ImageView iv_previous, HashMap<String, String> link_time, boolean ad_is_visible) {
        this.arl_linkdetails = arl_linkdetails;
        this.ll_f_visible = ll_f_visible;
        this.ll_longpres_visible = ll_longpres_visible;
        this.iv_clearselection = iv_clearselection;
        this.iv_copycb = iv_copycb;
        this.iv_delete = iv_delete;
        this.tv_count = tv_count;
        this.helper = helper;
        this.Folder_name = Folder_name;
        this.ll_search = ll_search;
        this.serach = serach;
        this.Searcg_String = Search_String;
        this.iv_next = iv_next;
        this.iv_previous = iv_previous;
        this.link_time = link_time;
        this.ad_is_visible = ad_is_visible;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        c = parent.getContext();

        if (ad_is_visible) {
            if (viewType == dis_date) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_date, parent, false);
                return new Vholder_date(v);


            } else if (viewType == Link) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_preview_card_view, parent, false);
                return new Vholder(v);

            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.natic_ad_class, parent, false);
                return new Vholder_ad(v);
            }

        } else {
            if (viewType == dis_date) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_date, parent, false);
                return new Vholder_date(v);


            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_preview_card_view, parent, false);
                return new Vholder(v);

            }

        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (ad_is_visible) {
            if (holder.getItemViewType() == dis_date) {

                Vholder_date v_date = (Vholder_date) holder;
                v_date.setDate(position);


            } else if (holder.getItemViewType() == Link) {

                Vholder v_data = (Vholder) holder;
                v_data.setData(position);

            } else {
                Vholder_ad v_date = (Vholder_ad) holder;
                v_date.setDate(position);
            }

        } else {
            if (holder.getItemViewType() == dis_date) {

                Vholder_date v_date = (Vholder_date) holder;
                v_date.setDate(position);


            } else {

                Vholder v_data = (Vholder) holder;
                v_data.setData(position);

            }

        }


    }

    @Override
    public int getItemViewType(int position) {

        if (ad_is_visible) {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date javaDate = sdfrmt.parse(arl_linkdetails.get(position));
                Log.d("TAG", "getItemViewType: " + dis_date);
                return dis_date;
            }
            /* Date format is invalid */ catch (ParseException e) {

                if (position % 7 == 0) {
                    return Ad;

                } else {
//            System.out.println(strDate+" is Invalid Date format");
                    Log.d("TAG", "getItemViewType: " + Link);
                    return Link;
                }
            }

        } else {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date javaDate = sdfrmt.parse(arl_linkdetails.get(position));
                Log.d("TAG", "getItemViewType: " + dis_date);
                return dis_date;
            }
            /* Date format is invalid */ catch (ParseException e) {

//            System.out.println(strDate+" is Invalid Date format");
                Log.d("TAG", "getItemViewType: " + Link);
                return Link;
            }


        }

    }

    @Override
    public int getItemCount() {
        return arl_linkdetails.size();
    }

    class Vholder extends RecyclerView.ViewHolder {


        LinearLayout ll_preview;
        ImageView iv_img;
        TextView tv_sitetitle;
        TextView tv_sitedesc;

        TextView tv_linkpreview;

        ImageView iv_share,iv_delete_single;


        public Vholder(@NonNull View itemView) {
            super(itemView);

            ll_preview = itemView.findViewById(R.id.ll_preview);

//        int w = (int) (x.heightPixels / 2);
//        int h = matrix.heightPixels / 2;

//        ll_preview.setLayoutParams(new LinearLayout.LayoutParams(w, h));

            iv_img = itemView.findViewById(R.id.iv_img_url);
            tv_sitetitle = itemView.findViewById(R.id.tv_sitename);
            tv_sitedesc = itemView.findViewById(R.id.tv_site_desc);

            tv_linkpreview = itemView.findViewById(R.id.link_pre_text_id);

            iv_share = itemView.findViewById(R.id.iv_share);
            iv_delete_single = itemView.findViewById(R.id.iv_delete);


        }

        public void setData(final int position) {

//            tv_link.setText(arl_linkdetails.get(position));

            tv_linkpreview.setVisibility(View.GONE);
            Log.d("Link_details", "setData: "+arl_linkdetails.get(position));
            Preview_Init(arl_linkdetails.get(position), tv_sitetitle, tv_sitedesc, iv_img, tv_linkpreview,ll_preview);

            iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
//                    String shareMessage= "\nLet me recommend you this application\n\n";
//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, arl_linkdetails.get(position));
                    c.startActivity(Intent.createChooser(shareIntent, "choose one"));


                }
            });

            iv_delete_single.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    helper.DeleteLink_for_folder(Folder_name, arl_linkdetails.get(position));
                    arl_linkdetails.remove(arl_linkdetails.get(position));
                    notifyDataSetChanged();


                }
            });

//            tv_time.setText("2:5");


//            String Preef_name = null;
//
//            SharedPreferences preferences = c.getSharedPreferences(Preef_name,Context.MODE_PRIVATE);
//            String perf_det = preferences.getString("small_name",5);
//
//            if(perf_det.equals("small")){
//                tv_link.setTextSize();
//
//            }else if(perf_det.equals("medium")){
//
//                tv_link.setTextSize();
//
//            }else if(perf_det.equals("large")){
//                tv_link.setTextSize();
//            }


//            if (link_time.containsKey(tv_link.getText().toString())) {
//                tv_time.setText(link_time.get(tv_link.getText().toString()));
//            }

            itemView.setBackgroundColor(c.getResources().getColor(R.color.white));

            if (serach) {
//                for(String s : Search_count){
//                    if(arl_linkdetails.get(position).contains(s)){
//                        itemView.setBackgroundColor(c.getResources().getColor(R.color.text_light_color));
//                    }
//                }

//                if (tv_link.getText().toString().equals(Searcg_String)) {
//                    itemView.setBackgroundColor(c.getResources().getColor(R.color.text_light_color));
//                }


            }

            ll_preview.setLongClickable(true);
            ll_preview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    ll_f_visible.setVisibility(View.INVISIBLE);
                    ll_search.setVisibility(View.INVISIBLE);
                    ll_longpres_visible.setVisibility(View.VISIBLE);

                    Long_Press_Enable = true;
                    itemView.setBackgroundColor(c.getResources().getColor(R.color.select_caht_color));
                    Selected_Links.add(arl_linkdetails.get(position));
                    tv_count.setText(String.valueOf(Selected_Links.size()));
                    Log.d("TAG", "onLongClick: " + Selected_Links);
                    return true;
                }
            });

            ll_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "onClick: " + Long_Press_Enable);
                    if (Long_Press_Enable) {
                        if (Selected_Links.contains(arl_linkdetails.get(position))) {
                            Selected_Links.remove(arl_linkdetails.get(position));
                            tv_count.setText(String.valueOf(Selected_Links.size()));
                            itemView.setBackgroundColor(c.getResources().getColor(R.color.white));
                            Log.d("TAG", "onClick: " + Selected_Links);
                            if (Selected_Links.isEmpty()) {
                                Long_Press_Enable = false;
                                ll_f_visible.setVisibility(View.VISIBLE);
                                ll_longpres_visible.setVisibility(View.INVISIBLE);
                            }

                        } else {
                            Selected_Links.add(arl_linkdetails.get(position));
                            tv_count.setText(String.valueOf(Selected_Links.size()));
                            itemView.setBackgroundColor(c.getResources().getColor(R.color.select_caht_color));

                            Log.d("TAG", "onClick: " + Selected_Links);
                        }

                        if (Selected_Links.size() > 1) {
                            iv_copycb.setVisibility(View.INVISIBLE);
                        } else {
                            iv_copycb.setVisibility(View.VISIBLE);
                        }

                    }
                }
            });

            iv_clearselection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Selected_Links.clear();
                    tv_count.setText(String.valueOf(Selected_Links.size()));
                    ll_longpres_visible.setVisibility(View.INVISIBLE);
                    ll_f_visible.setVisibility(View.VISIBLE);

                    Long_Press_Enable = false;

                    notifyDataSetChanged();

                }
            });

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog d = new Dialog(c);
                    d.setContentView(R.layout.delete_folder_dialogue_box);
                    d.setCanceledOnTouchOutside(false);

                    TextView tv_delete = d.findViewById(R.id.tv_deletetext);

                    tv_delete.setText("Delete message?");

                    Button bt_yes = d.findViewById(R.id.delete_dia_yes_button);
                    Button bt_no = d.findViewById(R.id.delete_dia_no_button);

                    bt_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.d("list_of_selected", "onClick: "+Selected_Links);

                            for (String single_link : Selected_Links) {
                                helper.DeleteLink_for_folder(Folder_name, single_link);
                                arl_linkdetails.remove(single_link);
                                notifyDataSetChanged();
                                Selected_Links.clear();
                                ll_f_visible.setVisibility(View.VISIBLE);
                                ll_longpres_visible.setVisibility(View.INVISIBLE);
                                ll_search.setVisibility(View.INVISIBLE);
                                d.dismiss();

                            }


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
            });

            iv_copycb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ll_search.setVisibility(View.INVISIBLE);
                    ll_f_visible.setVisibility(View.VISIBLE);
                    ll_longpres_visible.setVisibility(View.INVISIBLE);
                    ClipboardManager manager = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Source Text", Selected_Links.get(0));
                    manager.setPrimaryClip(clipData);
                    Selected_Links.clear();
                    notifyDataSetChanged();
                    Toast.makeText(c, "Text Copied To clipboard", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    class Vholder_date extends RecyclerView.ViewHolder {

        TextView tv_date;

        public Vholder_date(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
        }

        public void setDate(int position) {

            Calendar calendar = Calendar.getInstance();

            int month = calendar.get(Calendar.MONTH) + 1;
            String cur_date = +calendar.get(Calendar.DATE) + "/" + month + "/" + calendar.get(Calendar.YEAR);

            if (arl_linkdetails.get(position).equals(cur_date)) {
                tv_date.setText("Today");
            } else {
                tv_date.setText(arl_linkdetails.get(position));

            }


//            String Preef_name = null;
//
//            SharedPreferences preferences = c.getSharedPreferences(Preef_name,Context.MODE_PRIVATE);
//            String perf_det = preferences.getString("small_name",5);
//
//            if(perf_det.equals("small")){
//                tv_date.setTextSize();
//
//            }else if(perf_det.equals("medium")){
//
//                tv_date.setTextSize();
//
//            }else if(perf_det.equals("large")){
//                tv_date.setTextSize();
//            }
            SharedPreferences sharedPreferences = c.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

            if (sharedPreferences.getBoolean("small", true)) {
                tv_date.setTextSize(16);

            } else if (sharedPreferences.getBoolean("medium", true)) {

                tv_date.setTextSize(18);

            } else if (sharedPreferences.getBoolean("large", true)) {
                tv_date.setTextSize(20);
            }


        }
    }

    class Vholder_ad extends RecyclerView.ViewHolder {

        NativeAd nativeAd;

        public Vholder_ad(@NonNull final View itemView) {
            super(itemView);

            nativeAd = new NativeAd(c, "2691052121125437_2691055111125138");

            // Initiate a request to load an ad.
            nativeAd.loadAd();

            nativeAd.setAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                }

                @Override
                public void onError(com.facebook.ads.Ad ad, AdError adError) {

                    Log.d("adds","asss"+adError.getErrorCode());
                    Log.d("adds","asssss"+adError.getErrorMessage());

                }

                @Override
                public void onAdLoaded(com.facebook.ads.Ad ad) {

                    View adView = NativeAdView.render(c, nativeAd);
                    LinearLayout nativeAdContainer = (LinearLayout) itemView.findViewById(R.id.natic_add);
                    // Add the Native Ad View to your ad container.
                    // The recommended dimensions for the ad container are:
                    // Width: 280dp - 500dp
                    // Height: 250dp - 500dp
                    // The template, however, will adapt to the supplied dimensions.
                    nativeAdContainer.addView(adView, new LinearLayout.LayoutParams(MATCH_PARENT, 800));



                }

                @Override
                public void onAdClicked(com.facebook.ads.Ad ad) {

                }

                @Override
                public void onLoggingImpression(com.facebook.ads.Ad ad) {

                }
            });

        }

        public void setDate(int position) {
        }
    }

    public void notifydataset() {
        notifyDataSetChanged();

    }

    private void Preview_Init(final String p_url, final TextView tv_sitetitle, final TextView tv_sitedesc, final ImageView iv_img, final TextView tv_link_previewtext, final LinearLayout ll_preview) {


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


                    Async async = new Async(img_for_set, iv_img);
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
                    tv_link_previewtext.setVisibility(View.INVISIBLE);
                    ll_preview.setVisibility(View.VISIBLE);

                    ll_preview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!Long_Press_Enable) {


                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(p_url));
                                c.startActivity(i);
                            }
                        }
                    });

//                    Log.d("TAG", "onPos: " + sourceContent.getImages().get(0));
//                    Log.d("TAG", "onPos: " + sourceContent.getTitle());
//                    Log.d("TAG", "onPos: " + sourceContent.getDescription());
                } else {
                    tv_link_previewtext.setVisibility(View.VISIBLE);
                    Toast.makeText(c, "Please Check your link", Toast.LENGTH_SHORT).show();
                }

            }
        };

        textCrawler.makePreview(linkPreviewCallback, p_url);


    }

    class Async extends AsyncTask {
        String url_1;
        ImageView iv_img;

        Async(String url_1, ImageView iv_img) {
            this.url_1 = url_1;
            this.iv_img = iv_img;
            Log.d("TAG", "Async: " + url_1);

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (!(url_1 == (null))) {

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
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        iv_img.setImageDrawable(c.getResources().getDrawable(R.drawable.ic_launcher_background));

                    }
                });
            }

            return null;


        }
    }

}
