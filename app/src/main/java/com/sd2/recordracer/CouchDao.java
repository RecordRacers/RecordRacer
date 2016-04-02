package com.sd2.recordracer;


import android.content.Context;
import android.util.Log;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;

import java.util.Iterator;
import java.util.Map;

public class CouchDao implements Dao {
    private Manager manager;
    private Database database;
    private User user;
    private boolean foundResult;
    final String DB_NAME = "record_racer_db";
    final String TAG = "Record Racer DAO";

    public CouchDao(Context context) {

        try {

            AndroidContext androidContext = new AndroidContext(context);
            manager = new Manager(androidContext, Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DB_NAME);
        } catch (Exception e) {
            Log.e(TAG, "Error creating database." + e.getMessage());
        }

    }

    public boolean createUser(String username, String encryptedPassword, String email, String sport, int height, int weight, boolean useCentimeters, boolean useKilograms)  {
        User user = new User(username, encryptedPassword, email);
        if (sport.compareTo("Running")==0) {
            user.setPreferredExercise(User.PreferredExercise.RUNNING);
        } else if (sport.compareTo("Biking")==0) {
            user.setPreferredExercise(User.PreferredExercise.BIKING);
        } else {
            user.setPreferredExercise(User.PreferredExercise.NONE);
        }
        user.setWeight(weight);
        user.setUseMetricSystem(useCentimeters);
        //TODO: below line causes nullpointer exception
        Document document = database.createDocument();
        Map<String, Object> map = user.objectToMap();
        try {
            document.putProperties(map);
        } catch (Exception e) {
            Log.e(TAG, "Error adding user to database. " + e.getMessage());
            return false;
        }
        return true;
    }

    public User getUserByUsername(String username) {
        foundResult = false;
        View usersView = database.getView("users");
        final String finalUsername = username;
        usersView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (finalUsername.equals(document.get("username"))){
                    user = User.mapToObject(document);
                    foundResult = true;
                }
            }
        }, "1");
        if (foundResult) {
            return user;
        } else {
            return null;
        }
    }

    public User getUserByEmail(String email) {
        /*
        View usersView = database.getView("users");
        final String finalEmail = email;
        usersView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (finalEmail.equals(document.get("email"))) {
                    user = User.mapToObject(document);
                    foundResult = true;
                }
            }
        }, "1");
        */
        try {
            Map<String, Object> currentUser;
            String usersEmail;
            Query query = database.createAllDocumentsQuery();
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {

                QueryRow row = it.next();
                //TODO: row.getDocumentProperties returns null - still need to debug this
                currentUser = row.getDocumentProperties();
                usersEmail = (String) currentUser.get("Email");
                if(usersEmail.compareTo(email)==0) {
                    return User.mapToObject(currentUser);
                }
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Error looking for user. " + e.getMessage());

        }
            return null;
    }
}

