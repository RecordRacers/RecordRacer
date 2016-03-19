package com.sd2.recordracer;


import android.util.Log;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.util.Map;

public class CouchDao implements Dao {
    private Manager manager;
    private Database database;
    final String DB_NAME = "RecordRacerDB";
    final String TAG = "Record Racer DAO";

    public CouchDao(AndroidContext context) {

        try {
            manager = new Manager(context, Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DB_NAME);
        } catch (Exception e) {
            Log.e(TAG, "Error creating database." + e.getMessage());
        }

    }

    public boolean createUser(String username, String encryptedPassword, String email) {
        User user = new User(username, encryptedPassword, email);
        Document document = database.createDocument();
        Map<String, Object> map = user.objectToMap();
        try {
            document.putProperties(map);
        } catch(Exception e) {
            Log.e(TAG, "Error adding user to database. " + e.getMessage());
            return false;
        }
        return true;
    }
}
