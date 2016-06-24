package com.example.ahmedayman.instabug;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 21-Jun-16.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reposManager";
    private static final String TABLE_REPOS = "repos";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FORK = "fork_status";
    private static final String KEY_URL = "url";
    private static final String KEY_OWNERURL = "owner_url";
// REPOS TABLE
//    ID   NAME   USERNAME DESCRIPTION    FORKSTATUS      URL     OWNER URL

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_REPOS_TABLE = "CREATE TABLE " + TABLE_REPOS + "(" + KEY_ID + "INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," + KEY_USERNAME + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_FORK + " TEXT," +
                KEY_URL + " TEXT," + KEY_OWNERURL + " TEXT" + ")";
        db.execSQL(CREATE_REPOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTs" + TABLE_REPOS);
        onCreate(db);
    }

    /**
     * Reading and adding methods
     */

    public void addRepo(RepoModel repo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, repo.getRepoName());
        values.put(KEY_USERNAME, repo.getUsername());
        values.put(KEY_DESCRIPTION, repo.getDescription());
        values.put(KEY_FORK, repo.getFork());
        values.put(KEY_URL, repo.getUrl());
        values.put(KEY_OWNERURL, repo.getOwnerUrl());
        // Inserting Row
        db.insert(TABLE_REPOS, null, values);
        db.close();
    }

    public List<RepoModel> getAllRepos() {
        List<RepoModel> contactList = new ArrayList<RepoModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REPOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RepoModel repo = new RepoModel();
               // repo.setId(Integer.parseInt(cursor.getString(0)));
                repo.setRepoName(cursor.getString(1));
                repo.setUsername(cursor.getString(2));
                repo.setDescription(cursor.getString(3));
                repo.setFork(cursor.getString(4));
                repo.setUrl(cursor.getString(5));
                repo.setOwnerUrl(cursor.getString(6));
                contactList.add(repo);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }
}
