package com.dev.it.kmskedelainew;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.it.kmskedelainew.artikel.ArtikelActivity;
import com.dev.it.kmskedelainew.bukutamu.BukutamuActivity;
import com.dev.it.kmskedelainew.diskusi.MainActivityRTC;
import com.dev.it.kmskedelainew.extras.DownloadActivity;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.kedelai.KedelaiActivity;
import com.dev.it.kmskedelainew.konsultasi.KonsultasiActivity;
import com.dev.it.kmskedelainew.pengolahan.PengolahanActivity;
import com.dev.it.kmskedelainew.penyuluh.PenyuluhActivity;
import com.dev.it.kmskedelainew.teknologi.TeknologiActivity;
import com.dev.it.kmskedelainew.tentang.TentangActivity;
import com.dev.it.kmskedelainew.usermanagement.LoginActivity;
import com.dev.it.kmskedelainew.usermanagement.ProfileUserActivity;
import com.dev.it.kmskedelainew.usermanagement.RegisterActivity;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.facebook.login.LoginManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //linear layout home
    LinearLayout kedelai, pengolahan, penyuluh, tentang, teknologi, konsultasi, artikel, bukutamu, diskusi;
    SessionManager sessionManager;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(getApplicationContext());

        kedelai = (LinearLayout) findViewById(R.id.kedelai);
        pengolahan = (LinearLayout) findViewById(R.id.pengolahan);
        penyuluh = (LinearLayout) findViewById(R.id.penyuluh);
        tentang = (LinearLayout) findViewById(R.id.tentang);
        teknologi = (LinearLayout) findViewById(R.id.teknologi);
        konsultasi = (LinearLayout) findViewById(R.id.konsultasi);
        artikel = (LinearLayout) findViewById(R.id.artikel);
        bukutamu = (LinearLayout) findViewById(R.id.bukutamu);
        diskusi = (LinearLayout) findViewById(R.id.diskusi);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coor_main);

        //jika user sudah login
        if(sessionManager.isLoggedIn()){
            //ambil informasi user yang sedang login
            HashMap<String, String> user = sessionManager.getUserDetails();
            Toast.makeText(getApplicationContext(),"Welcome, " + user.get(SessionManager.KEY_NAME), Toast.LENGTH_LONG).show();
        }

        setEvents();
    }

    public void  setEvents(){
        kedelai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), KedelaiActivity.class);
                startActivity(i);
            }
        });

        teknologi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TeknologiActivity.class);
                startActivity(i);
            }
        });
        pengolahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PengolahanActivity.class);
                startActivity(i);
            }
        });

        konsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), KonsultasiActivity.class);
                startActivity(i);
            }
        });

        penyuluh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PenyuluhActivity.class);
                startActivity(i);
            }
        });

        artikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ArtikelActivity.class);
                startActivity(i);
            }
        });
        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TentangActivity.class);
                startActivity(i);
            }
        });

        bukutamu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BukutamuActivity.class);
                startActivity(i);
            }
        });

        diskusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("sd", String.valueOf(sessionManager.isLoggedIn()));
                if(sessionManager.isLoggedIn()){
                    Intent i = new Intent(getApplicationContext(), MainActivityRTC.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(sessionManager.isLoggedIn())
            getMenuInflater().inflate(R.menu.menu_after_login, menu);
        else
            getMenuInflater().inflate(R.menu.menu_konsultasi, menu);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Keluar")
                    .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
