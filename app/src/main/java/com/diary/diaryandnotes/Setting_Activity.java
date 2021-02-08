package com.diary.diaryandnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.diary.diaryandnotes.In_App_Purchase.subscribe_activity;

public class Setting_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout chat_linearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chat_linearlayout = (LinearLayout) findViewById(R.id.chat_linear_layout);
        chat_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_Activity.this,Chat_Activity.class);
                startActivity(intent);
            }
        });

    }

    public void More_Apps(View view) {


        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://dev?id=8414621939972068743")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=8414621939972068743")));
        }
    }

    public void Show_policy(View view) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gulbai.wordpress.com/privacy-policy-diary-notes/"));
        startActivity(browserIntent);

    }

    public void Invite(View view) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.diary.diaryandnotes");
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }

    public void open_instagram(View view) {

        Uri uri = Uri.parse("https://www.instagram.com/dithin.denny/");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/dithin.denny/")));
        }




    }

    public void remove_add(View view) {

        Intent i = new Intent(Setting_Activity.this,subscribe_activity.class);
        i.putExtra("msg",1);
        startActivity(i);

    }
}
