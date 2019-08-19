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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Receiver for geofence transition changes.
 *
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition.
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver
{
    private static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();
    private Context context;
    private boolean geofencePermissive = false;

    /**
     * Receives the intent from Android OS Location Services when a
     * geofence transition occurs.
     * @param context
     * @param intent
     */
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError())
        {
            String errorMessage = GeofenceErrorMessages.getErrorString(context, geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
        {
            // Get the transition detail as a String.
            String geofenceTransitionString = getTransitionString(geofenceTransition);

            //update geofence boolean permissive
            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            {
                geofencePermissive = true;
            }
            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            {
                geofencePermissive = false;
            }

            //communicate value of permissive with UI.
            ObservablePermissive.getInstance().updateValue(geofencePermissive);

            //LEAVE THIS FOR NOW
            //displays message on UI indicating entry/exit transitions
            sendNotification(geofenceTransitionString);

            // Log the transition details.
            Log.i(TAG, geofenceTransitionString);
        }
        else
        {
            // Log the error.
            Log.e(TAG, "Geofence transition error: invalid transition type " + geofenceTransition);
        }

    }

    /**
     * Returns the geofence transition type as a simple string.
     * @param transitionType
     * @return transition type as string.
     */
    private String getTransitionString(int transitionType)
    {
        switch (transitionType)
        {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Entered/Within Geofence";

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exited/Outside Geofence";

            default:
                return "Unknown Transition";
        }
    }

    /**
     * Display toast message when a geofence transition occurs.
     * @param notificationDetails
     */
    private void sendNotification(String notificationDetails)
    {
        Toast.makeText(context, String.format(notificationDetails), Toast.LENGTH_SHORT).show();
    }
}