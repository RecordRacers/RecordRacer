package com.sd2.recordracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by woodr_000 on 3/29/2016.
 */
public class CreateAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private Spinner heightUnits;
    private Spinner weightUnits;
    private EditText height;
    private EditText weight;
    private Spinner sport;
    private TextView failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        sport = (Spinner) findViewById(R.id.create_sport);
        username = (EditText) findViewById(R.id.create_username);
        email = (EditText) findViewById(R.id.create_email);
        password = (EditText) findViewById(R.id.create_password);
        heightUnits = (Spinner) findViewById(R.id.create_heightUnits);
        weightUnits = (Spinner) findViewById(R.id.create_weightUnits);
        height = (EditText) findViewById(R.id.create_height);
        weight = (EditText) findViewById(R.id.create_weight);
        failure = (TextView) findViewById(R.id.fail);
        failure.setVisibility(View.GONE);

        Button createAccountButton = (Button) findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAccount() {
        Dao couchDao = new CouchDao(this);
        String usernameString = username.getText().toString();
        Editable editablePassword = password.getText();
        String passwordString = editablePassword.toString();
        //TODO: encrypt the password
        String emailString = email.getText().toString();
        String sportString = sport.getSelectedItem().toString();
        int heightInt = Integer.valueOf(height.getText().toString());
        int weightInt = Integer.valueOf(weight.getText().toString());
        boolean useCentimeters = true;
        if(heightUnits.getSelectedItem().toString().compareTo("inches")==0) {
            useCentimeters=false;
        }
        boolean useKilograms = true;
        if(weightUnits.getSelectedItem().toString().compareTo("pounds")==0) {
            useKilograms=false;
        }

        if(couchDao.createUser(usernameString,passwordString, emailString, sportString, heightInt, weightInt, useCentimeters, useKilograms)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("Account_Created", true);
            intent.putExtra("ID", "Create_Account");
            startActivity(intent);
        }
        else {
            failure.setVisibility(View.VISIBLE);
        }
    }
}
