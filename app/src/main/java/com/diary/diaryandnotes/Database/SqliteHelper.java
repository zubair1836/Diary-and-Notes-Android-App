package com.diary.diaryandnotes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.diary.diaryandnotes.Model.Link_Details;

import java.util.ArrayList;

public class SqliteHelper extends SQLiteOpenHelper {


    String Folder_Table = "Diary_Folder";
    String Link_Table = "Diary_Link";

    String Folder_table_for_link = "Link_folder_list";
    String Link_Table_for_link = "Link_folder_link_list";

    String cm_id = "ID";
    String cm_date = "Date";
    String cm_folder = "Folder";
    String cm_link = "Link";
    String cm_time = "Time";

    SQLiteDatabase db;

    String TAG  = "DATABASE";

    Context context;


    public SqliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String Create_Folder_Table = "CREATE TABLE " + Folder_Table + "\n" +
                "(" + cm_id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "" + cm_folder + " varchar(255));";


        String Create_Link_Table = "CREATE TABLE " + Link_Table + "\n" +
                "(" + cm_id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "" + cm_date + " varchar(255) ,\n" +
                "" + cm_time + " varchar(255) ,\n" +
                "" + cm_folder + " varchar(255) ,\n" +
                "" + cm_link + " varchar(255));";


        String Create_Folder_Table_for_link = "CREATE TABLE " + Folder_table_for_link + "\n" +
                "(" + cm_id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "" + cm_folder + " varchar(255));";


        String Create_Link_Table_for_link = "CREATE TABLE " + Link_Table_for_link + "\n" +
                "(" + cm_id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "" + cm_date + " varchar(255) ,\n" +
                "" + cm_time + " varchar(255) ,\n" +
                "" + cm_folder + " varchar(255) ,\n" +
                "" + cm_link + " varchar(255));";



        db.execSQL(Create_Folder_Table);
        db.execSQL(Create_Link_Table);
        db.execSQL(Create_Folder_Table_for_link);
        db.execSQL(Create_Link_Table_for_link);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public int Add_new_Folder(String Folder_name){

        db = this.getWritableDatabase();

        ArrayList<String> arl_f_name = new ArrayList<>();

        String query = "SELECT * FROM "+Folder_Table+";";

        Cursor c = db.rawQuery(query,null);


        while (c.moveToNext()){
            arl_f_name.add(c.getString(1));
        }

        if(!arl_f_name.contains(Folder_name)){
            ContentValues values = new ContentValues();
            values.put(cm_folder, Folder_name);
            db.insert(Folder_Table,null,values);
            Toast.makeText(context, "Folder is created..", Toast.LENGTH_SHORT).show();
            return 1;
        }else{
            Toast.makeText(context, "Folder Already Exists", Toast.LENGTH_SHORT).show();
            return 0;
        }



    }

    public ArrayList<String> getFolderList() {
        db = this.getWritableDatabase();

        ArrayList<String> arl_f_name = new ArrayList<>();

        String query = "SELECT * FROM "+Folder_Table+";";

        Cursor c = db.rawQuery(query,null);


        while (c.moveToNext()){
            arl_f_name.add(c.getString(1));
        }

        Log.d(TAG, "getFolderList: "+arl_f_name);

        return arl_f_name;
    }

    public String GetLastMessage(String Folder_name){

        db = this.getWritableDatabase();

        String Message = "Empty MessageBox";

        String Query = "SELECT * FROM "+Link_Table+" WHERE "+cm_folder+" = '"+Folder_name+"'";

        Cursor c = db.rawQuery(Query,null);

        while (c.moveToNext()){
            Message = c.getString(4);
            Log.d(TAG, "GetLastMessage: "+Message);
        }

        return Message;





    }

    public String GetLastMessage_for_link(String Folder_name){

        db = this.getWritableDatabase();

        String Message = "Empty MessageBox";

        String Query = "SELECT * FROM "+Link_Table_for_link+" WHERE "+cm_folder+" = '"+Folder_name+"'";

        Cursor c = db.rawQuery(Query,null);

        while (c.moveToNext()){
            Message = c.getString(4);
            Log.d(TAG, "GetLastMessage: "+Message);
        }

        return Message;

    }

    public void Add_Link(String Folder_name,String date,String link,String cur_time){
        db = this.getWritableDatabase();

        ArrayList<String> arl_f_name = new ArrayList<>();

        String query = "SELECT * FROM "+Folder_Table+";";

        Cursor c = db.rawQuery(query,null);


        while (c.moveToNext()){
            arl_f_name.add(c.getString(1));
        }

        if(arl_f_name.contains(Folder_name)){

            ContentValues values = new ContentValues();
            values.put(cm_folder,Folder_name);
            values.put(cm_date,date);
            values.put(cm_link,link);
            values.put(cm_time,cur_time);
            db.insert(Link_Table,null,values);
        }else{
            Toast.makeText(context, "Folder Is not Exists", Toast.LENGTH_SHORT).show();
        }

    }

    public void GetAllLink(){
        db = this.getWritableDatabase();

        String query = "SELECT * FROM "+Link_Table+"";

        Cursor c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            c = db.rawQuery(query,null,null);
        }

        while (c.moveToNext()){
            Log.d(TAG, "GetAllLink:123: "+c.getString(1));
            Log.d(TAG, "GetAllLink:123: "+c.getString(2));
            Log.d(TAG, "getLinks_for_Folder_name:123: "+c.getString(3));
        }

    }

    public ArrayList<Link_Details> getLinks_for_Folder_name(String Folder_name){

        db = this.getWritableDatabase();

        ArrayList<Link_Details> arl_link_details = new ArrayList<>();

        String query = "SELECT * FROM "+Link_Table+" WHERE "+cm_folder+" = '"+Folder_name+"';";

        Cursor c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            c = db.rawQuery(query,null,null);
        }

        while (c.moveToNext()){

            Link_Details details = new Link_Details();

            details.setLink_Date(c.getString(1));
            details.setLink_time(c.getString(2));
            details.setLink_data(c.getString(4));

            arl_link_details.add(details);


        }

        return arl_link_details;

    }

    public int DeleteLink(String Folder_name,String Link){
        db  = this.getWritableDatabase();

        String Query = "DELETE FROM "+Link_Table+" WHERE "+cm_folder+" = '"+Folder_name+"' AND "+cm_link+" = '"+Link+"';";

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.rawQuery(Query,null,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return db.delete(Link_Table,cm_folder+"=? AND "+cm_link+"=?",new String[]{Folder_name,Link});

//        GetAllLink();

    }

    public void DeleteFolder(String Folder_Name){
        db  = this.getWritableDatabase();

        String dele_Link_Query = "DELETE FROM "+Link_Table+" WHERE "+cm_folder+" = '"+Folder_Name+"';";
        String delete_fold_query = "DELETE FROm "+Folder_Table+" WHERE "+cm_folder+" = '"+Folder_Name+"';";

        db.delete(Link_Table, cm_folder+"=?",new String[]{Folder_Name});
        db.delete(Folder_Table,cm_folder+"=?",new String[]{Folder_Name});

//        db.rawQuery(dele_Link_Query,null);
//        db.rawQuery(delete_fold_query,null);
    }

    public void Rename_Folder(String Older_name, String New_Name){

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(cm_folder,New_Name);
        db.update(Folder_Table,values,cm_folder +"=?",new String[]{Older_name});
        db.update(Link_Table,values,cm_folder +"=?",new String[]{Older_name});

        Toast.makeText(context, "Folder name chnged succesfully", Toast.LENGTH_SHORT).show();

    }

    public int ClearChat(String Folder_name){

        db = this.getWritableDatabase();

        String Query = "DELETE FROM "+Link_Table+" WHERE "+cm_folder+" = '"+Folder_name+"'";

        return db.delete(Link_Table,cm_folder + "=?",new String[]{Folder_name});


    }





    public int Add_new_Folder_for_link(String Folder_name) {

        db = this.getWritableDatabase();

        ArrayList<String> arl_f_name = new ArrayList<>();

        String query = "SELECT * FROM "+Folder_table_for_link+";";

        Cursor c = db.rawQuery(query,null);


        while (c.moveToNext()){
            arl_f_name.add(c.getString(1));
        }

        if(!arl_f_name.contains(Folder_name)){
            ContentValues values = new ContentValues();
            values.put(cm_folder, Folder_name);
            db.insert(Folder_table_for_link,null,values);
            Toast.makeText(context, "Folder Created", Toast.LENGTH_SHORT).show();
            return 1;
        }else{
            Toast.makeText(context, "Folder Already Exists", Toast.LENGTH_SHORT).show();
            return 0;
        }



    }


    public ArrayList<String> getFolderList_for_link_from_linktable() {

        db = this.getWritableDatabase();

        ArrayList<String> arl_f_name = new ArrayList<>();

        String query = "SELECT * FROM "+Folder_table_for_link+";";

        Cursor c = db.rawQuery(query,null);


        while (c.moveToNext()){
            arl_f_name.add(c.getString(1));
        }

        Log.d(TAG, "getFolderList: "+arl_f_name);

        return arl_f_name;





    }

    public ArrayList<Link_Details> getLinks_for_Folder_name_from_linktable(String Folder_name){

        db = this.getWritableDatabase();

        ArrayList<Link_Details> arl_link_details = new ArrayList<>();

        String query = "SELECT * FROM "+Link_Table_for_link+" WHERE "+cm_folder+" = '"+Folder_name+"';";

        Cursor c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            c = db.rawQuery(query,null,null);
        }

        while (c.moveToNext()){

            Link_Details details = new Link_Details();

            details.setLink_Date(c.getString(1));
            details.setLink_time(c.getString(2));
            details.setLink_data(c.getString(4));

            arl_link_details.add(details);


        }

        return arl_link_details;

    }

    public void Add_Link_for_link_table(String Folder_name,String date,String link,String cur_time){
        db = this.getWritableDatabase();

        ArrayList<String> arl_f_name = new ArrayList<>();

        String query = "SELECT * FROM "+Folder_table_for_link+";";

        Cursor c = db.rawQuery(query,null);


        while (c.moveToNext()){
            arl_f_name.add(c.getString(1));
            Log.d(TAG, "Add_Link_for_link_table: "+c.getString(1));
        }

        Log.d(TAG, "Add_Link_for_link_table:foldername: "+Folder_name);

        if(arl_f_name.contains(Folder_name)){

            ContentValues values = new ContentValues();
            values.put(cm_folder,Folder_name);
            values.put(cm_date,date);
            values.put(cm_link,link);
            values.put(cm_time,cur_time);
            db.insert(Link_Table_for_link,null,values);

            Toast.makeText(context, "Link Inserted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Folder Is not Exists", Toast.LENGTH_SHORT).show();
        }

    }

    public int Rename_Folder_for_link(String Older_name, String New_Name){

        db = this.getWritableDatabase();

        ArrayList<String> arl_f_name = new ArrayList<>();

        String query = "SELECT * FROM "+Folder_table_for_link+";";

        Cursor c = db.rawQuery(query,null);


        while (c.moveToNext()){
            arl_f_name.add(c.getString(1));
        }

        if(arl_f_name.contains(New_Name)){
            Toast.makeText(context, "Folder is exists please choose anothername", Toast.LENGTH_SHORT).show();

            return 0;

        }else {

            ContentValues values = new ContentValues();
            values.put(cm_folder, New_Name);
            db.update(Folder_table_for_link, values, cm_folder + "=?", new String[]{Older_name});
            db.update(Link_Table_for_link, values, cm_folder + "=?", new String[]{Older_name});

            Toast.makeText(context, "Folder name chnged succesfully", Toast.LENGTH_SHORT).show();
            return 1;
        }

    }

    public void DeleteFolder_for_link(String Folder_Name){
        db  = this.getWritableDatabase();

        String dele_Link_Query = "DELETE FROM "+Link_Table_for_link+" WHERE "+cm_folder+" = '"+Folder_Name+"';";
        String delete_fold_query = "DELETE FROm "+Folder_table_for_link+" WHERE "+cm_folder+" = '"+Folder_Name+"';";

        int a = db.delete(Link_Table_for_link, cm_folder+"=?",new String[]{Folder_Name});
        int b = db.delete(Folder_table_for_link,cm_folder+"=?",new String[]{Folder_Name});

        Log.d(TAG, "DeleteFolder_for_link: "+a);

//        db.rawQuery(dele_Link_Query,null);
//        db.rawQuery(delete_fold_query,null);
    }

    public int ClearChat_for_link(String Folder_name){

        db = this.getWritableDatabase();

        String Query = "DELETE FROM "+Link_Table_for_link+" WHERE "+cm_folder+" = '"+Folder_name+"'";

        return db.delete(Link_Table_for_link,cm_folder + "=?",new String[]{Folder_name});


    }


    public boolean openDataBase(String DB_FILE) throws SQLException {

//        db = this.getWritableDatabase();
        // Log.v("DB_PATH", DB_FILE.getAbsolutePath());
        db = SQLiteDatabase.openDatabase(DB_FILE, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        // mDataBase = SQLiteDatabase.openDatabase(DB_FILE, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return db != null;
    }

    public int DeleteLink_for_folder(String Folder_name,String Link){
        db  = this.getWritableDatabase();

        String Query = "DELETE FROM "+Link_Table_for_link+" WHERE "+cm_folder+" = '"+Folder_name+"' AND "+cm_link+" = '"+Link+"';";

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.rawQuery(Query,null,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return db.delete(Link_Table_for_link,cm_folder+"=? AND "+cm_link+"=?",new String[]{Folder_name,Link});

//        GetAllLink();

    }



}
