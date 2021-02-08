package com.diary.diaryandnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.diary.diaryandnotes.Database.SqliteHelper;

import java.util.ArrayList;

public class Search_Activity extends AppCompatActivity {

    EditText et_Search;

    String TAG = "SEARCHING";
    ImageView imageView;

    SqliteHelper helper;
    ArrayList<String>  Arl_searcheed = new ArrayList<>();

    RecyclerView rv_searched_folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);

        final int activity = getIntent().getIntExtra("from",0);

        imageView = findViewById(R.id.back_arrow);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_Search = findViewById(R.id.et_search);

        rv_searched_folder = findViewById(R.id.rv_search_details);

        INITIALIZEDB();

        et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Arl_searcheed.clear();
                String f_name = String.valueOf(s);

                if(activity == 0) {

                    ArrayList<String> arl_foldername = helper.getFolderList();

                    for (String n : arl_foldername) {
                        if (n.contains(f_name)) {
                            Arl_searcheed.add(n);

                        }
                    }

                    ArrayList<String> arl_lastmsg = new ArrayList<>();
                    for (String fold_name : Arl_searcheed) {
                        arl_lastmsg.add(helper.GetLastMessage(fold_name));

                    }
                    SetRecyclerView(arl_lastmsg);
                }else{
                    ArrayList<String> arl_foldername = helper.getFolderList_for_link_from_linktable();

                    for (String n : arl_foldername) {
                        if (n.contains(f_name)) {
                            Arl_searcheed.add(n);

                        }
                    }

                    ArrayList<String> arl_lastmsg = new ArrayList<>();
                    for (String fold_name : Arl_searcheed) {
                        arl_lastmsg.add(helper.GetLastMessage(fold_name));

                    }
                    SetRecyclerView(arl_lastmsg);


                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void SetRecyclerView(ArrayList<String> arl_lastmsg) {
        rv_searched_folder.setAdapter(new Rv_Searched_Adapter(Arl_searcheed,arl_lastmsg));
        rv_searched_folder.setLayoutManager(new LinearLayoutManager(Search_Activity.this));

    }

    private void INITIALIZEDB() {
        helper = new SqliteHelper(Search_Activity.this,MainActivity.DB_Name,null,1);
        helper.getWritableDatabase();
    }
}
