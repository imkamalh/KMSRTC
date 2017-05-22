package com.dev.it.kmskedelainew.bukutamu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.SplashActivity;
import com.dev.it.kmskedelainew.extras.DownloadActivity;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.network.CustomRequest;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.usermanagement.LoginActivity;
import com.dev.it.kmskedelainew.usermanagement.ProfileUserActivity;
import com.dev.it.kmskedelainew.usermanagement.RegisterActivity;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.login.LoginManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BukutamuActivity extends AppCompatActivity {
    String[] SPINNERLIST = {"Peneliti (Pakar, Penyelia, Pembudidaya)",
            "Pengajar (Guru, Dosen, Penyuluh)", "Pelajar (Siswa, Mahasiswa)", "Lainnya"};
    Button btnSubmitTamu;
    CoordinatorLayout coordinatorLayout;
    SessionManager sessionManager;

    EditText nama, email, hp, alamat;
    MaterialBetterSpinner pekerjaan;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukutamu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());

        nama = (EditText) findViewById(R.id.nama_tamu);
        email = (EditText) findViewById(R.id.email_tamu);
        hp = (EditText) findViewById(R.id.hp_tamu);
        alamat = (EditText) findViewById(R.id.alamat_tamu);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        pekerjaan = (MaterialBetterSpinner)
                findViewById(R.id.android_material_design_spinner);
        pekerjaan.setAdapter(arrayAdapter);
        pekerjaan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = pekerjaan.getAdapter().getItem(position).toString();
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coor_bukutamu);
        btnSubmitTamu = (Button) findViewById(R.id.buttonSubmitTamu);
        btnSubmitTamu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(BukutamuActivity.this)
                        .setTitle("Isi Buku Tamu")
                        .setMessage("Anda yakin mengirim data?")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String url = URLEndpoints.SEND_BUKU_TAMU;

                                if(!check(nama.getText().toString(), "Nama Lengkap")){
                                    nama.requestFocus();
                                    return;
                                }
                                if(!check(email.getText().toString(), "Email")){
                                    email.requestFocus();
                                    return;
                                }
                                if(!check(hp.getText().toString(), "Nomor HP")){
                                    hp.requestFocus();
                                    return;
                                }
                                if(!check(alamat.getText().toString(), "Alamat")){
                                    alamat.requestFocus();
                                    return;
                                }


                                Map<String, String> map = new HashMap<String, String>();
                                map.put("bt_nama_lengkap", nama.getText().toString());
                                map.put("bt_email", email.getText().toString());
                                map.put("bt_pekerjaan", item);
                                map.put("bt_alamat", alamat.getText().toString());
                                map.put("bt_hp", hp.getText().toString());
                                CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try{
                                            String hasil = response.getString("hasil");
                                            if (hasil.equals("true")){
                                                nama.setText("");email.setText("");hp.setText("");alamat.setText("");
                                                Snackbar s = Snackbar.make(coordinatorLayout, "Berhasil dikirim", Snackbar.LENGTH_LONG);
                                                s.setAction("OK", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = getIntent();
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                });
                                                s.show();
                                            }else{
                                                nama.setText("");email.setText("");hp.setText("");alamat.setText("");
                                                Snackbar s = Snackbar.make(coordinatorLayout, "Terdapat Kesalahan", Snackbar.LENGTH_LONG);
                                                s.setAction("OK", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = getIntent();
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                });
                                                s.show();
                                            }

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        nama.setText("");email.setText("");hp.setText("");alamat.setText("");
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Snackbar s = Snackbar.make(coordinatorLayout, "Sever Error, Sorry", Snackbar.LENGTH_LONG);
                                        s.setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = getIntent();
                                                finish();
                                                startActivity(intent);
                                            }
                                        });
                                        s.show();
                                        Log.d("error_volley", error.toString());
                                    }
                                });
                                VolleySingleton.getInstance().addToRequestQueue(customRequest);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private boolean check(String cek, String param){
        if (cek.equals("")){
            Snackbar s = Snackbar.make(coordinatorLayout, "Masukkan "+ param, Snackbar.LENGTH_LONG);
            s.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            s.show();
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(sessionManager.isLoggedIn())
            getMenuInflater().inflate(R.menu.menu_after_login, menu);
        else
            getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent in = new Intent(getApplicationContext(), SettingsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            return true;
        } else if (id == R.id.action_profile) {
            Intent in = new Intent(getApplicationContext(), ProfileUserActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            return true;
        } else if (id == R.id.action_user) {
            Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            return true;
        } else if (id == R.id.action_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(BukutamuActivity.this)
                    .setTitle("Keluar")
                    .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pDialog = new ProgressDialog(BukutamuActivity.this);
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
        } else if (id == R.id.action_login){
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        } else if (id == R.id.action_download){
            Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        } else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
