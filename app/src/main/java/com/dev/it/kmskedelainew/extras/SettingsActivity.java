package com.dev.it.kmskedelainew.extras;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.SplashActivity;
import com.dev.it.kmskedelainew.network.CustomRequest;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.usermanagement.EditProfileActivity;
import com.dev.it.kmskedelainew.usermanagement.ResetPasswordActivity;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout account;
    SessionManager sessionManager;
    TextView logout;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        sessionManager = new SessionManager(getApplicationContext());
        account = (LinearLayout) findViewById(R.id.account);

        if(!sessionManager.isLoggedIn()){
            account.setVisibility(View.GONE);
        }

        logout = (TextView)findViewById(R.id.textLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Keluar")
                        .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                        .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog pDialog = new ProgressDialog(SettingsActivity.this);
                                pDialog.setMessage("mohon tunggu ...");
                                pDialog.show();
                                pDialog.setCancelable(false);

                                sessionManager.logoutUser();
                                LoginManager.getInstance().logOut();
                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        pDialog.dismiss();
                                        //LoginManager.getInstance().logOut();
                                        Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        finish();
                                        startActivity(intent);
                                    }
                                }, 2000);
                            }
                        })
                        .setNegativeButton("Batal", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        TextView help = (TextView) findViewById(R.id.textHelp);
        TextView privacy = (TextView) findViewById(R.id.textPrivacy);
        TextView tos = (TextView) findViewById(R.id.textTos);
        TextView report = (TextView) findViewById(R.id.textReport);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ExtrasActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("id", "1");
                in.putExtra("nama", "Petunjuk Penggunaan");
                startActivity(in);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ExtrasActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("id", "2");
                in.putExtra("nama", "Kebijakan");
                startActivity(in);
            }
        });
        tos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ExtrasActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("id", "3");
                in.putExtra("nama", "Syarat dan Ketentuan");
                startActivity(in);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.layout_report, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Laporkan Masalah")
                        .setView(layout)
                        .setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText nama = (EditText) layout.findViewById(R.id.editNama);
                                EditText report = (EditText) layout.findViewById(R.id.editReport);

                                Map<String, String> map = new HashMap<String, String>();
                                map.put("nama_pelapor", nama.getText().toString());
                                map.put("isi_laporan", report.getText().toString());

                                CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLEndpoints.REPORT, map, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getApplicationContext(), "Terima Kasih", Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), "Terima Kasih", Toast.LENGTH_LONG).show();
                                    }
                                });
                                VolleySingleton.getInstance().addToRequestQueue(customRequest);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        TextView editProfile = (TextView) findViewById(R.id.textEditProfile);
        TextView ubahPassword = (TextView) findViewById(R.id.textUbahPassword);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(in);
            }
        });

        ubahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(in);
            }
        });
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
