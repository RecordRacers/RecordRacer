package com.sd2.recordracer;


import android.content.Context;
import android.util.Log;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import java.util.Iterator;
import java.util.Map;

//import org.apache.shiro.*;

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

        if (getUserByEmail(email)!=null) {
            Log.d(TAG, "User with that email is already in the database.");
            return false;
        }

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
        Document document = database.createDocument();
        Map<String, Object> map = user.objectToMap();
        String docId = document.getId();
        try {
            document.putProperties(map);
        } catch (Exception e) {
            Log.e(TAG, "Error adding user to database. " + e.getMessage());
            return false;
        }
        /*
        Uncomment when debugging. See "info" String for what was just stored in the database
        Document retrievedDocument = database.getDocument(docId);
        String info = String.valueOf(retrievedDocument.getProperties());
        */
        return true;
    }

    public User getUserByUsername(String username) {
        try {
            Map<String, Object> currentUser;
            String usersUsername;
            Query query = database.createAllDocumentsQuery();
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {

                QueryRow row = it.next();
                String docId = row.getSourceDocumentId();
                Document retrievedDocument = database.getDocument(docId);
                Map<String, Object> userMap = retrievedDocument.getProperties();
                String info = String.valueOf(userMap);
                Log.d(TAG,"Checking user: "+info);
                usersUsername = (String) userMap.get("Username");
                if(usersUsername.compareTo(username)==0) {
                    return User.mapToObject(userMap);
                }
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Error looking for user. " + e.getMessage());
        }
        return null;
    }
    public User getUserByEmail(String email) {
        try {
            Map<String, Object> currentUser;
            String usersEmail;
            Query query = database.createAllDocumentsQuery();
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {

                QueryRow row = it.next();
                String docId = row.getSourceDocumentId();
                Document retrievedDocument = database.getDocument(docId);
                Map<String, Object> userMap = retrievedDocument.getProperties();
                String info = String.valueOf(userMap);
                Log.d(TAG,"Checking user: "+info);
                usersEmail = (String) userMap.get("Email");
                if(usersEmail.compareTo(email)==0) {
                    return User.mapToObject(userMap);
                }
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Error looking for user. " + e.getMessage());
        }
            return null;


    }

    public void updateUser(User user) throws IllegalArgumentException {
        Document document = getUserDocument(user);
        if (document==null) {
            throw new IllegalArgumentException("The user is not in the database");
        }
        try {
            document.putProperties(user.objectToMap());
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
            throw new IllegalArgumentException("User could not be updated");
        }
    }

    /**
     * @param user whose docId is wanted
     * @return docId of the user, a null string if none found
     */
    private Document getUserDocument(User user) {

        try {
            Map<String, Object> currentUser;
            String usersUsername;
            Query query = database.createAllDocumentsQuery();
            QueryEnumerator result = query.run();
            String documentId;
            String username = user.getUsername();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {

                QueryRow row = it.next();
                String docId = row.getSourceDocumentId();
                Document retrievedDocument = database.getDocument(docId);
                Map<String, Object> userMap = retrievedDocument.getProperties();
                String info = String.valueOf(userMap);
                Log.d(TAG,"Checking user: "+info);
                usersUsername = (String) userMap.get("Username");
                if(usersUsername.compareTo(username)==0) {
                    return retrievedDocument;
                }
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Error looking for user. " + e.getMessage());
        }
        return null;

    }
}

