package com.eatx.wdj.geofencing;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.ui.main.AbsenceRequest;
import com.eatx.wdj.ui.main.CheckRequest;
import com.eatx.wdj.ui.main.delBoardRequest;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.eatx.wdj.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE;
    private GeofenceHelper geofenceHelper;
    private static MapsActivity ins;
    private final Handler handler = new Handler();
    private String GEOFENCE_ID = "GEOFENCE";
    private int RADIUS = 60;
    private int run;
    private TextView date , time , mMinute , fieldturn , username, date2 , username2 , time2;
    LinearLayout layoutbottomSheet;
    LinearLayout layoutbottomSheet2;
    private TimerTask updateTime;
    private String getCurrentTime , getCurrentMinute , getDate;
    private Button attendbutton ,absencebutton;
    long now = System.currentTimeMillis();
    Document doc ;
    private Context mContext;
    public static MapsActivity getInstance() {
        return ins;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ins = getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);

        LinearLayout layoutbottomSheet = findViewById(R.id.bottomSheet);
        LinearLayout layoutbottomSheet2 = findViewById(R.id.bottomSheet2);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutbottomSheet);
        layoutbottomSheet2.setVisibility(View.GONE);
        registerReceiver(broadcastReceiver,new IntentFilter("enter"));
        updateTime();
        username = findViewById(R.id.nameValue);
        username2 = findViewById(R.id.username2);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String name = intent.getStringExtra("NAME");
        int sid = intent.getIntExtra("SID",0);
        String classValue = intent.getStringExtra("CLASS");
        System.out.println(id + "id???");
        System.out.println(classValue + "id???");
        username.setText(name+"??? ???????????????!");
        username2.setText(name+"??? ??????????????? ????????? ????????? ???????????????");
        attendbutton = findViewById(R.id.attend);

        attendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                Toast.makeText(getApplicationContext(),"????????? ??????????????????",Toast.LENGTH_SHORT).show();
                                attendbutton.setEnabled(false);
                            } else {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"????????? ??????????????????",Snackbar.LENGTH_SHORT);
                                snackbar.setAnchorView(findViewById(R.id.bottomSheet2));
                                snackbar.show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // ????????? Volley??? ???????????? ?????????
                CheckRequest checkRequest = new CheckRequest(id,name,sid,classValue, Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID),getDate,getCurrentTime,run,responseListener);
                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                queue.add(checkRequest);
            }
        });

        absencebutton = findViewById(R.id.absencebutton);
        absencebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(MapsActivity.this);
                FrameLayout container = new FrameLayout(MapsActivity.this);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = MapsActivity.this.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = MapsActivity.this.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                et.setLayoutParams(params);
                container.addView(et);
                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(MapsActivity.this);
                alt_bld.setTitle("?????? ?????? ??????").setMessage("?????? ????????? ???????????????").setIcon(R.drawable.outline_developer_board_24).setCancelable(
                        true).setView(container).setPositiveButton("??????",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String value = et.getText().toString();
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success) {
                                                Toast.makeText(getApplicationContext(),"??????????????????????????? ",Toast.LENGTH_SHORT).show();
                                                absencebutton.setEnabled(false);
                                            } else {
                                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"?????? ????????????",Snackbar.LENGTH_SHORT);
                                                snackbar.setAnchorView(findViewById(R.id.bottomSheet2));
                                                snackbar.show();
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                // ????????? Volley??? ???????????? ?????????
                                AbsenceRequest absenceRequest = new AbsenceRequest(intent.getStringExtra("ID"),name,sid,classValue, Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID),getDate,getCurrentTime,value,responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                                queue.add(absenceRequest);
                              }

                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBottomSheet();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void updateBottomSheet() {
        MapsActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                LinearLayout layoutbottomSheet = findViewById(R.id.bottomSheet);
                LinearLayout layoutbottomSheet2 = findViewById(R.id.bottomSheet2);
                layoutbottomSheet.setVisibility(View.GONE);
                layoutbottomSheet2.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        init();

        enableUserLocation();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;

                }
                mMap.setMyLocationEnabled(true);
            } else {

            }

        }
    }

    private void addGeofence(LatLng latLng, float radius) {

        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofecingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }
    private void init() {

        LatLng yju = new LatLng(35.89689, 128.62090);
        LatLng myHome = new LatLng(35.89452748270415, 128.62565228358116);
        LatLng yju2 = new LatLng(35.8967187406931, 128.62031510715042);
        addMarker(yju,"??????????????? ??????","?????????????????????\n????????????");
        addMarker(myHome,"???","??????");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yju, 18));
        addCircle(yju,RADIUS);
        addCircle(myHome,RADIUS);
        addGeofence(yju,RADIUS);
        addGeofence(myHome,RADIUS);
        addGeofence(yju2,RADIUS);
    }

    private void addMarker(LatLng latLng ,String title, String snippet) {
        MarkerOptions yjuMarker = new MarkerOptions().position(latLng);
        yjuMarker.title(title);
        yjuMarker.snippet(snippet);
        mMap.addMarker(yjuMarker);
    }

    private void addCircle(LatLng latLng,  float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,255,0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(3);
        mMap.addCircle(circleOptions);
    }

    public void updateTime() {
        Log.d("UPDATE TIME","?????? ???????????????");
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy??? M??? d??? EE??????");
        getDate = simpleDate.format(mDate);
        date2 = findViewById(R.id.date2);
        date = findViewById(R.id.dateValue);
        date2.setText(getDate);
        date.setText(getDate);
        time = findViewById(R.id.timeValue);
        time2 = findViewById(R.id.time2);
        updateTime = new TimerTask() {
            @Override
            public void run() {
                Update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(updateTime, 0, 1000);
    }


    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                Date mTime = new Date(currentTime);
                SimpleDateFormat getTime = new SimpleDateFormat("aa h??? mm??? ss???");


                SimpleDateFormat getMinute = new SimpleDateFormat("hh:mm");
                getCurrentMinute = getMinute.format(mTime);
                getCurrentTime = getTime.format(mTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                int hour = calendar.get(Calendar.HOUR_OF_DAY) - 9;
                int minute = calendar.get(Calendar.MINUTE);
                fieldturn = findViewById(R.id.fieldturn);
                if(hour>0) {

                    run = hour * 12;
                    if (minute >= 5) {
                        run = run +((int) minute / 5);
                    }
                }  else if(hour == 0) {
                    run = 0;
                    if (minute >= 5) {
                        run = run +((int) minute / 5);
                    }
                }
//                System.out.println(run + "?????? ?????????");
//                System.out.println(hour+"???" + minute+"????????????");
                fieldturn.setText("?????? ?????? ??? " + run + "????????? ???????????? ?????????!");
                time.setText(getCurrentTime);
                time2.setText(getCurrentTime);
            }
        };
        handler.post(updater);
    }
}

