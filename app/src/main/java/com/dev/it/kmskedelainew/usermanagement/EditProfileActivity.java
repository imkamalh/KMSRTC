package com.dev.it.kmskedelainew.usermanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.dev.it.kmskedelainew.network.CustomRequest;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import org.json.JSONObject;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    EditText username, alamat, pekerjaan;
    Button buttonEdit;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());

        username = (EditText) findViewById(R.id.username);
        alamat = (EditText) findViewById(R.id.alamat);
        pekerjaan = (EditText) findViewById(R.id.pekerjaan);
        buttonEdit = (Button) findViewById(R.id.buttonEditProfile);
        try{
            username.setText(sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
        }catch (Exception e){}
        try{
            alamat.setText(sessionManager.getUserDetails().get(SessionManager.KEY_ALAMAT));
        }catch (Exception e){}
        try{
            pekerjaan.setText(sessionManager.getUserDetails().get(SessionManager.KEY_PEKERJAAN));
        }catch (Exception e){}

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Username Required", Toast.LENGTH_LONG).show();
                    return;
                }

                editProfile();
            }
        });

    }

    public void editProfile(){
        if(!StateKMS.isNetworkConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }else{
            final ProgressDialog loading = ProgressDialog.show(EditProfileActivity.this,"Edit Profile","Please wait...",false,false);
            loading.show();
            final String u_name = username.getText().toString();
            final String u_pekerjaan = pekerjaan.getText().toString();
            final String u_alamat = alamat.getText().toString();
            HashMap<String, String> params = new HashMap<>();
            params.put("nama_user", u_name);
            params.put("pekerjaan", u_pekerjaan);
            params.put("alamat", u_alamat);
            params.put("id_user", sessionManager.getUserDetails().get(SessionManager.KEY_ID));
            CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLEndpoints.EDIT_PROFILE , params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        if (status.equals("success")){
                            HashMap<String, String> user = sessionManager.getUserDetails();
                            sessionManager.createLoginSession(u_name,user.get(SessionManager.KEY_EMAIL),
                                    Integer.valueOf(user.get(SessionManager.KEY_ID)),user.get(SessionManager.KEY_LOGIN),
                                    u_alamat,u_pekerjaan);
                            Toast.makeText(getApplicationContext(), "Successfully edit your profile", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Error Encountered", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){}
                    loading.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error Encountered", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(customRequest);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
