package com.diary.diaryandnotes;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class Rv_Linkdetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int dis_date = 0;
    int Link = 1;
    int Ad = 2;

    Context c;

    Boolean Long_Press_Enable = false;

    ArrayList<String> Selected_Links = new ArrayList<>();

    ArrayList<String> arl_linkdetails;
    LinearLayout linearLayout;
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

    ArrayList<Integer> arl_selected = new ArrayList<>();

    boolean ad_is_visible;

    Rv_Linkdetails(ArrayList<String> arl_linkdetails, LinearLayout ll_f_visible, LinearLayout ll_longpres_visible, LinearLayout ll_search, ImageView iv_clearselection, ImageView iv_delete, ImageView iv_copycb, TextView tv_count, SqliteHelper helper, String Folder_name, boolean serach, String Search_String, ImageView iv_next, ImageView iv_previous, HashMap<String, String> link_time, boolean ad_is_visible) {
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
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_detials, parent, false);
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
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_detials, parent, false);
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

                if (position % 10 == 0) {
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

        TextView tv_link, tv_time;

        public Vholder(@NonNull View itemView) {
            super(itemView);

            tv_link = itemView.findViewById(R.id.tv_link);
            tv_time = itemView.findViewById(R.id.tv_time);
//            linearLayout = (LinearLayout) itemView.findViewById(R.id.chat_detail_card_view);
        }

        public void setData(final int position) {

            tv_link.setText(arl_linkdetails.get(position));
//            tv_time.setText("2:5");


//            String Preef_name = null;

            SharedPreferences sharedPreferences = c.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

//            SharedPreferences preferences = c.getSharedPreferences(mypreference,Context.MODE_PRIVATE);
//            String perf_det = preferences.getString("small_name",5);

            if (sharedPreferences.getBoolean("small", true)) {
                tv_link.setTextSize(16);

            } else if (sharedPreferences.getBoolean("medium", true)) {

                tv_link.setTextSize(18);

            } else if (sharedPreferences.getBoolean("large", true)) {
                tv_link.setTextSize(20);
            }


            if (link_time.containsKey(tv_link.getText().toString())) {
                tv_time.setText(link_time.get(tv_link.getText().toString()));
            }

            itemView.setBackgroundColor(c.getResources().getColor(R.color.white));

            if (serach) {
//                for(String s : Search_count){
//                    if(arl_linkdetails.get(position).contains(s)){
//                        itemView.setBackgroundColor(c.getResources().getColor(R.color.text_light_color));
//                    }
//                }

                if (tv_link.getText().toString().equals(Searcg_String)) {
                    itemView.setBackgroundColor(c.getResources().getColor(R.color.text_light_color));
                }


            }

            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    ll_f_visible.setVisibility(View.INVISIBLE);
                    ll_search.setVisibility(View.INVISIBLE);
                    ll_longpres_visible.setVisibility(View.VISIBLE);

                    Long_Press_Enable = true;
                    itemView.setBackgroundColor(c.getResources().getColor(R.color.select_caht_color));
                    Selected_Links.add(arl_linkdetails.get(position));

                    arl_selected.add(position);

                    Log.d("TAG  ", "onLongClick: " + arl_selected);

                    tv_count.setText(String.valueOf(Selected_Links.size()));
                    Log.d("TAG", "onLongClick: " + Selected_Links);
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "onClick: " + Long_Press_Enable);
                    if (Long_Press_Enable) {
                        if (arl_selected.contains(position)) {
                            Selected_Links.remove(arl_linkdetails.get(position));
                            arl_selected.remove(arl_selected.indexOf(position));
                            tv_count.setText(String.valueOf(Selected_Links.size()));
                            itemView.setBackgroundColor(c.getResources().getColor(R.color.white));
                            Log.d("TAG", "onClick: " + arl_selected);
                            if (arl_selected.isEmpty()) {
                                Long_Press_Enable = false;
                                Selected_Links.clear();
                                ll_f_visible.setVisibility(View.VISIBLE);
                                ll_longpres_visible.setVisibility(View.INVISIBLE);
                            }
                        } else {

                            arl_selected.add(position);
                            Selected_Links.add(arl_linkdetails.get(position));
                            tv_count.setText(String.valueOf(Selected_Links.size()));
                            itemView.setBackgroundColor(c.getResources().getColor(R.color.select_caht_color));

                            Log.d("TAG", "onClick: " + arl_selected);
                        }

//                        if (Selected_Links.contains(arl_linkdetails.get(position))) {
//                            Selected_Links.remove(arl_linkdetails.get(position));
//                            tv_count.setText(String.valueOf(Selected_Links.size()));
//                            itemView.setBackgroundColor(c.getResources().getColor(R.color.white));
//                            Log.d("TAG", "onClick: " + Selected_Links);
//                            if (Selected_Links.isEmpty()) {
//                                Long_Press_Enable = false;
//                                ll_f_visible.setVisibility(View.VISIBLE);
//                                ll_longpres_visible.setVisibility(View.INVISIBLE);
//                            }
//
//                        } else {
//                            Selected_Links.add(arl_linkdetails.get(position));
//                            tv_count.setText(String.valueOf(Selected_Links.size()));
//                            itemView.setBackgroundColor(c.getResources().getColor(R.color.purple));
//
//                            Log.d("TAG", "onClick: " + Selected_Links);
//                        }

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

                            for (String single_link : Selected_Links) {
                                helper.DeleteLink(Folder_name, single_link);
                                arl_linkdetails.remove(single_link);

                                ll_f_visible.setVisibility(View.VISIBLE);
                                ll_longpres_visible.setVisibility(View.INVISIBLE);
                                ll_search.setVisibility(View.INVISIBLE);

                            }

                            notifyDataSetChanged();
                            Selected_Links.clear();

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


//                    for (String single_link : Selected_Links) {
//                        helper.DeleteLink(Folder_name, single_link);
//                        arl_linkdetails.remove(single_link);
//                        notifyDataSetChanged();
//                        Selected_Links.clear();
//                        ll_f_visible.setVisibility(View.VISIBLE);
//                        ll_longpres_visible.setVisibility(View.INVISIBLE);
//                        ll_search.setVisibility(View.INVISIBLE);
//
//                    }

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

            String Preef_name = null;

//            SharedPreferences preferences = c.getSharedPreferences(Preef_name,Context.MODE_PRIVATE);
//            String perf_det = preferences.getString("small_name",5);
            SharedPreferences sharedPreferences = c.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

            if (sharedPreferences.getBoolean("small", true)) {
                tv_date.setTextSize(16);

            } else if (sharedPreferences.getBoolean("medium", true)) {

                tv_date.setTextSize(18);

            } else if (sharedPreferences.getBoolean("large", true)) {
                tv_date.setTextSize(20);
            }


//            tv_date.setText(arl_linkdetails.get(position));


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
}
