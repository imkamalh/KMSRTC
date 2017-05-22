package com.dev.it.kmskedelainew.usermanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.network.CustomRequest;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import org.json.JSONObject;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText oldpassword, newpassword, retypenewpasswd;
    Button buttonSubmit;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());

        oldpassword = (EditText) findViewById(R.id.oldpassword);
        newpassword = (EditText) findViewById(R.id.newpassword);
        retypenewpasswd = (EditText) findViewById(R.id.retypepassword);
        buttonSubmit = (Button) findViewById(R.id.buttonNewPassword);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldpassword.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(), "Old password required",
                            Toast.LENGTH_SHORT).show();
                    oldpassword.requestFocus();
                    return;
                }
                checkOldPassword();
            }
        });
    }

    public void checkOldPassword(){
        if(!StateKMS.isNetworkConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String> params = new HashMap<>();
            params.put("oldpassword", oldpassword.getText().toString());
            params.put("id_user", sessionManager.getUserDetails().get(SessionManager.KEY_ID));
            CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLEndpoints.CHECK_PASSWORD, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        if(status.equals("exist")){
                            if(newpassword.getText().toString().equals("") ){
                                Toast.makeText(getApplicationContext(), "New password required",
                                        Toast.LENGTH_SHORT).show();
                                newpassword.requestFocus();
                                return;
                            }else if(newpassword.getText().toString().length() < 6){
                                Toast.makeText(getApplicationContext(), "Password length must be more than 6 characters",
                                        Toast.LENGTH_SHORT).show();
                                newpassword.requestFocus();
                                return;
                            }
                            if(retypenewpasswd.getText().toString().equals("") || !retypenewpasswd.getText().toString().equals(newpassword.getText().toString())){
                                Toast.makeText(getApplicationContext(), "New password doesn't match",
                                        Toast.LENGTH_SHORT).show();
                                retypenewpasswd.requestFocus();
                                return;
                            }

                            submitEditpass();
                        }else{
                            Toast.makeText(getApplicationContext(), "Your old password is incorrect, try 'forgot password instead if your forgot your password'",
                                    Toast.LENGTH_SHORT).show();
                            newpassword.requestFocus();
                        }
                    }catch (Exception e){}
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.d("errorpass", error.toString());
                    Toast.makeText(getApplicationContext(), "Error Encountered", Toast.LENGTH_SHORT).show();
                }
            });

            VolleySingleton.getInstance().addToRequestQueue(customRequest);
        }
    }

    private void submitEditpass(){
        HashMap<String, String> params = new HashMap<>();
        params.put("newpassword", newpassword.getText().toString());
        params.put("id_user", sessionManager.getUserDetails().get(SessionManager.KEY_ID));
        final ProgressDialog loading = ProgressDialog.show(ResetPasswordActivity.this,"Reset Password","Please wait...",false,false);
        loading.show();
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLEndpoints.NEW_PASSWORD, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String status = response.getString("status");
                    if(status.equals("success")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Successfully reset your password",
                                Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(getApplicationContext(), SettingsActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(in);
                    }else{
                        Toast.makeText(getApplicationContext(), "Error Encountered", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){}
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("errorpass", error.toString());
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Error Encountered", Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance().addToRequestQueue(customRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
