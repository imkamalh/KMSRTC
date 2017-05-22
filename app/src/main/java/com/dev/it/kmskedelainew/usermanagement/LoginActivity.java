package com.dev.it.kmskedelainew.usermanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.it.kmskedelainew.MainActivity;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.diskusi.util.Constants;
import com.dev.it.kmskedelainew.network.CustomRequest;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    EditText email, password;
    ProgressDialog pDialog;
    SessionManager sessionManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    Profile profile;
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Masuk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        sessionManager = new SessionManager(getApplicationContext());

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Button btnLogin = (Button) findViewById(R.id.buttonLogin);
        Button btnLoginFb = (Button) findViewById(R.id.buttonLoginFb);
        TextView txt = (TextView) findViewById(R.id.toregister);
        forgot = (TextView) findViewById(R.id.textForgot);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(in);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(in);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_email = email.getText().toString();
                String u_pass = password.getText().toString();

                if (u_email.equals("")){
                    Toast.makeText(getApplicationContext(), "Email Required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (u_pass.equals("")){
                    Toast.makeText(getApplicationContext(), "Password Required", Toast.LENGTH_LONG).show();
                    return;
                }


                login(u_email, u_pass);
            }
        });
        //login
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();

        btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile, email, user_birthday"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Toast.makeText(getApplicationContext(), "sukes", Toast.LENGTH_LONG).show();
                        profile = Profile.getCurrentProfile();
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        final JSONObject jsonObject = response.getJSONObject();
                                        //Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                                        try {
                                            String email = jsonObject.getString("email");
                                            String name =  jsonObject.getString("name");
                                            loginFb(email, email);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                        ProfileTracker profileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                Profile.setCurrentProfile(currentProfile);
                                this.stopTracking();
                            }
                        };
                        profileTracker.startTracking();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "canceled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    void login(final String email, final String password){
        if(!StateKMS.isNetworkConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }else {
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("please wait...");
            pDialog.show();
            pDialog.setCancelable(false);

            Map<String, String> map = new HashMap<String, String>();
            map.put("email", email);
            map.put("password", password);

            CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLEndpoints.LOGIN_USER, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        if (status.equals("error")){
                            pDialog.dismiss();
                            String message = response.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }else{
                            JSONObject jsonObject = response.getJSONObject("message");
                            sessionManager.createLoginSession(jsonObject.getString("nama_user"),email,jsonObject.getInt("id_user"),"nonfb");
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString(Constants.USER_NAME, jsonObject.getString("nama_user"));
                            edit.apply();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                    startActivity(intent);
                                }
                            }, 2000);
                        }
                    }catch (Exception e){}

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Sorry, Error Encountered", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(customRequest);
        }
    }

    void loginFb(final String email, final String password){
        if(!StateKMS.isNetworkConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();
        }else{
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("please wait...");
            pDialog.show();
            pDialog.setCancelable(false);

            Map<String, String> map = new HashMap<String, String>();
            map.put("email", email);
            map.put("password", password);

            CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLEndpoints.LOGIN_USER, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        if (status.equals("error")){
                            pDialog.dismiss();
                            String message = response.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            LoginManager.getInstance().logOut();
                        }else{
                            Log.d("response", response.toString());
                            JSONObject jsonObject = response.getJSONObject("message");
                            sessionManager.createLoginSession(jsonObject.getString("nama_user"),email,jsonObject.getInt("id_user"),"fb",
                                    jsonObject.getString("alamat"),jsonObject.getString("pekerjaan"));
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                    startActivity(intent);
                                }
                            }, 2000);
                        }
                    }catch (Exception e){}
                    //pDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Sorry, Error Encountered", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(customRequest);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getApplicationContext(), String.valueOf(resultCode), Toast.LENGTH_LONG).show();
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
