/**
 *	███╗   ███╗██╗██╗  ██╗███████╗    ███╗   ███╗ ██████╗███╗   ███╗ █████╗ ██╗  ██╗ ██████╗ ███╗   ██╗
 *	████╗ ████║██║██║ ██╔╝██╔════╝    ████╗ ████║██╔════╝████╗ ████║██╔══██╗██║  ██║██╔═══██╗████╗  ██║
 *	██╔████╔██║██║█████╔╝ █████╗      ██╔████╔██║██║     ██╔████╔██║███████║███████║██║   ██║██╔██╗ ██║
 *	██║╚██╔╝██║██║██╔═██╗ ██╔══╝      ██║╚██╔╝██║██║     ██║╚██╔╝██║██╔══██║██╔══██║██║   ██║██║╚██╗██║
 *	██║ ╚═╝ ██║██║██║  ██╗███████╗    ██║ ╚═╝ ██║╚██████╗██║ ╚═╝ ██║██║  ██║██║  ██║╚██████╔╝██║ ╚████║
 *	╚═╝     ╚═╝╚═╝╚═╝  ╚═╝╚══════╝    ╚═╝     ╚═╝ ╚═════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
 *
 *	██████╗ ██████╗ ██████╗  ██████╗ ███████╗ █████╗ ██████╗     ██████╗ █████╗
 *	╚════██╗██╔══██╗██╔══██╗██╔════╝ ██╔════╝██╔══██╗██╔══██╗   ██╔════╝██╔══██╗
 *	 █████╔╝██████╔╝██║  ██║██║  ███╗█████╗  ███████║██████╔╝   ██║     ███████║
 *	 ╚═══██╗██╔══██╗██║  ██║██║   ██║██╔══╝  ██╔══██║██╔══██╗   ██║     ██╔══██║
 *	██████╔╝██║  ██║██████╔╝╚██████╔╝███████╗██║  ██║██║  ██║██╗╚██████╗██║  ██║
 *	╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝ ╚═════╝╚═╝  ╚═╝
 *
 *	Copyright (C) 2019 Mike McMahon, A.Sc.T.
 *
 *	**********************************************************************
 *	*****    Android Client for Home Automation RESTful web service  *****
 *  **********************************************************************
 *
 *   Author: Mike McMahon, A.Sc.T
 *   Company: 3rdGear SAI
 *   Version: 1.0
 *   Date: May 2019
 *
 */
package ca.thirdgear.homeautomation.geofencing;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import ca.thirdgear.homeautomation.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;

/**
 * Class uses Google Geofencing API to determine whether user is within range of home.
 */
@Singleton
public class GeofencingService implements LocationListener, OnCompleteListener<Void>, java.util.Observer
{
    private Context context;
    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofence;
    private PendingIntent geofencePendingIntent;
    private static final String TAG = GeofencingService.class.getSimpleName();
    private MutableLiveData<Boolean> geofenceGaragePermissive;
    private ObservablePermissive observablePermissive;
    private LocationManager locationManager;

    @Inject
    public GeofencingService(Context context)
    {
        this.context = context;
        geofence = new ArrayList<Geofence>();
        geofencingClient = LocationServices.getGeofencingClient(context);
        geofencePendingIntent = null;
        populateGeofence();
        ObservablePermissive.getInstance().addObserver(this);

        geofenceGaragePermissive = new MutableLiveData<>();
        geofenceGaragePermissive.setValue(false);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //poll GPS hardware every 5 seconds
        if(ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }

        if(checkPermissions())
        {
            performPendingGeofenceTask();
        }
    }

    /**
     * Hard code the geofence data from OneCypressStreet.
     */
    private void populateGeofence()
    {
        for (Map.Entry<String, LatLng> entry : OneCypressStreet.ONE_CYPRESS_STREET.entrySet())
        {

            geofence.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this geofence.
                    .setRequestId(entry.getKey())
                    //Don't allow the geofence to expire
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            OneCypressStreet.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    // Create the geofence.
                    .build());
        }
    }

    /**
     * Used to communicate with GeofenceBroadcastReceiver and fetches permissive value via
     * an observable object - ObservablePermissive
     * @param observable
     * @param arg
     */
    @Override
    public void update(Observable observable, Object arg)
    {
        observablePermissive = (ObservablePermissive) observable;

        //update value of local permissive
        geofenceGaragePermissive.setValue(observablePermissive.getPermissive());
        //Toast.makeText(getContext(), String.valueOf("" + geofenceGaragePermissive), Toast.LENGTH_SHORT).show();
    }

    /**
     * Return geofencing permissive for use by garage.
     * @return Boolean permissive.
     */
    public LiveData<Boolean> getGeofenceGaragePermissive()
    {
        return geofenceGaragePermissive;
    }

    /**
     * Check that the application has permission to access location services.
     * @return boolean checkPermissions
     */
    private boolean checkPermissions()
    {
        int permissionState = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Performs the geofencing task that was pending until location permission was granted.
     */
    private void performPendingGeofenceTask()
    {
        addGeofences();
    }

    /**
     * Add geofences
     */
    @SuppressWarnings("MissingPermission")
    private void addGeofences()
    {
        if (!checkPermissions())
        {
            Resources resources = context.getResources();
            //let user know that permissions are missing
            Toast.makeText(context, resources.getString(R.string.insufficient_permissions), Toast.LENGTH_SHORT).show();
            return;
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }

    /**
     * Build the geofencing request
     * @return GeofencingRequest
     */
    private GeofencingRequest getGeofencingRequest()
    {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        //set the initial triggers to be either entry OR exit
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(geofence);
        return builder.build();
    }

    /**
     * Create the pending intent
     * @return PendingIntent
     */
    private PendingIntent getGeofencePendingIntent()
    {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null)
        {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        //use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addGeofences()
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    /**
     * Runs when the result of calling {@link #addGeofences()}
     * is available.
     * @param task the resulting Task, containing either a result or error.
     */
    @Override
    public void onComplete(@NonNull Task<Void> task)
    {

        if (task.isSuccessful())
        {
            //if the geofence was not added, set it so it is.
            if(!getGeofencesAdded())
            {
                updateGeofencesAdded(true);
                Log.i(TAG, "Geofence was successfully added.");
            }
        }
        else
        {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(context, task.getException());
            Log.w(TAG, errorMessage);
        }
    }

    /**
     * Returns true if geofences were added, otherwise false.
     * @return boolean
     */
    private boolean getGeofencesAdded()
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                OneCypressStreet.GEOFENCES_ADDED_KEY, false);
    }

    /**
     * Stores whether geofences were added or removed in {@link SharedPreferences};
     *
     * @param added Whether geofences were added or removed.
     */
    private void updateGeofencesAdded(boolean added)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(OneCypressStreet.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

    /**
     * DO NOTHING - Do nothing with returned location data.  LocationManager is
     * being used to poll GPS Hardware for updates only to increase accuracy and
     * responsiveness of Geofencing activities related to this application.
     * @param location
     */
    @Override
    public void onLocationChanged(Location location)
    {
        // Do nothing with the location.  Only want to poll GPS hardware to initial geofence transitions
    }

    /**
     * DO NOTHING
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        //do nothing
    }

    /**
     * DO NOTHING
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider)
    {
        //do nothing
    }

    /**
     * DO NOTHING
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider)
    {
        //do nothing
    }
}
