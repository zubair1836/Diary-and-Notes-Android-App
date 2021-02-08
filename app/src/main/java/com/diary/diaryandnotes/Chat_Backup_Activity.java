package com.diary.diaryandnotes;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Chat_Backup_Activity extends AppCompatActivity {

    private TextView textView;
    String[] StringArray;
    private Pattern pattern;
    Account[] account;
    Context context;
    Button button;
    ArrayList<String> SampleArrayList = new ArrayList<String>();
    private String TAG = "AccountsActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_backup);

        context = Chat_Backup_Activity.this;

        button = (Button) findViewById(R.id.backup_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Crash time....", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Chat_Backup_Activity.this,CloudBackup.class);
                startActivity(intent);
//                Intent intent = new Intent(Chat_Backup_Activity.this, CloudBackup.class);
//                startActivity(intent);
            }
        });

        textView = (TextView) findViewById(R.id.chat_backup_text);
        pattern = Patterns.EMAIL_ADDRESS;
        getEmails();

    }
    private void getEmails() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;

        // Getting all registered Google Accounts;
//         Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");

        // Getting all registered Accounts;
        Account[] accounts = AccountManager.get(context).getAccounts();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String string = account.name;
                textView.setText(string);
//                SampleArrayList.add(account.name);
                Log.e(TAG, String.format("%s - %s", account.name, account.type));
                Log.e(TAG,"fg = "+string);
            }
        }
    }
}
