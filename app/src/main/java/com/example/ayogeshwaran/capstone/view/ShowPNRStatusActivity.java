package com.example.ayogeshwaran.capstone.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayogeshwaran.capstone.R;
import com.example.ayogeshwaran.capstone.adapter.PassengerListRecyclerViewAdapter;
import com.example.ayogeshwaran.capstone.model.PNRStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowPNRStatusActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String PNR_STATUS_OBJECT = "pnr_status_object";

    private PNRStatus pnrStatus;

    @BindView(R.id.passenger_list_rv)
    protected RecyclerView passengerListRecyclerView;

    @BindView(R.id.distance_between_value_tv)
    protected TextView distanceFromBoardingStationTv;

    @BindView(R.id.distance_container)
    protected LinearLayout distanceContainer;

    @BindView(R.id.loading_progress)
    protected ProgressBar progressBar;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION_CODE = 33;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private LocationCallback mLocationCallback;

    private double currentLatitude;

    private double currentLongitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pnr_status);
        ButterKnife.bind(this);
        if (getIntent().hasExtra(PNR_STATUS_OBJECT)) {
            pnrStatus = getIntent().getParcelableExtra(PNR_STATUS_OBJECT);
        }
        if (pnrStatus != null) {
            setValues(pnrStatus);
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.registerConnectionCallbacks(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        distanceContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            getPermission();
        }
    }

    private void setValues(PNRStatus pnrStatus) {
        passengerListRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        PassengerListRecyclerViewAdapter passengerListRecyclerViewAdapter =
                new PassengerListRecyclerViewAdapter(pnrStatus.getPassengers(), getBaseContext());
        passengerListRecyclerViewAdapter.updateList(pnrStatus.getPassengers());
        passengerListRecyclerView.setAdapter(passengerListRecyclerViewAdapter);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getPermission();
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {
            getCurrentLocation();
        }
    }



    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(ShowPNRStatusActivity.this,
                "onConnectionSuspended: " + String.valueOf(i),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(ShowPNRStatusActivity.this,
                "onConnectionFailed: " + connectionResult.toString(),
                Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();

                        distanceContainer.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        computeDistance();
                    } else {
                        distanceContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ShowPNRStatusActivity.this,
                                "getCurrentLocation - Location null", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void computeDistance() {
        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        Location boardingStationLocation = new Location("");
        boardingStationLocation.setLatitude(pnrStatus.getBoardingPoint().getLat());
        boardingStationLocation.setLongitude(pnrStatus.getBoardingPoint().getLng());

        float distanceInMeters = currentLocation.distanceTo(boardingStationLocation);
        float distanceInKms = distanceInMeters / 1000;
        distanceFromBoardingStationTv.setText(String.format(getApplicationContext().getResources().getString(
                R.string.distance_from_boarding_station_value), String.valueOf(distanceInKms)));
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION_CODE);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            distanceContainer.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            computeDistance();
        } else {
            distanceContainer.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ShowPNRStatusActivity.this,
                    "onLocationChanged - Location null", Toast.LENGTH_SHORT).show();
        }
    }
}
