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
package ca.thirdgear.homeautomation.views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ca.thirdgear.homeautomation.R;
import ca.thirdgear.homeautomation.viewmodel.GarageViewModel;
import static java.lang.Thread.sleep;

/**
 * Fragment displays the button and contains the controls for the garage door.
 */
public class Garage extends Fragment
{
    private GarageViewModel garageModel;
    private Button garageControlButton;
    private Boolean permissive;
    private Boolean geofenceGaragePermissive;

    public Garage() {
        //do nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.garage_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize geofencing permissive to known state.
        //Permissive is true when within 1km of house.  Permissive is
        //false when outside a 1km radius of the house.
        geofenceGaragePermissive = false;

        //network and local related tasks
        permissive = false;

        garageModel = ViewModelProviders.of(this).get(GarageViewModel.class);

        //observe changes in garage door status
        final Observer<String> garageDoorModelObserver = new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                if(s.equals("CLOSED"))
                {
                    garageControlButton.setText("Garage Door CLOSED");
                    garageControlButton.setTextColor(Color.WHITE);
                    garageControlButton.setBackgroundColor(Color.rgb(63, 81, 181));
                }
                if(s.equals("OPEN"))
                {
                    garageControlButton.setText("Garage Door OPEN");
                    garageControlButton.setTextColor(Color.BLACK);
                    garageControlButton.setBackgroundColor(Color.GREEN);
                }
            }
        };

        //observe changes in ADT System state
        final Observer<String> adtSystemObserver = new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                if(s.equals("DISARMED"))
                {
                    permissive = true;
                }
                if(s.equals("ARMED"))
                {
                    permissive = false;
                }
            }
        };

        //observe changes in geofence permissive
        final Observer<Boolean> geofenceObserver = new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean b)
            {
                //User is within boundary of geofence
                if(b == true)
                {
                    geofenceGaragePermissive = b;
                    Toast.makeText(getContext(), "Entered/Within Geofence", Toast.LENGTH_SHORT).show();
                }
                //User is outside boundary of geofence
                if(b == false)
                {
                    geofenceGaragePermissive = b;
                    Toast.makeText(getContext(), "Exited/Outside Geofence", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //initialize IO status/data
        garageModel.getIoData();

        //Unsubscribe old observers to prevent memory leaks
        garageModel.getGarageDoorState().removeObservers(this);
        garageModel.getAdtSystemState().removeObservers(this);
        garageModel.getGeofenceGaragePermissive().removeObservers(this);

        //Register observers
        garageModel.getGarageDoorState().observe(this, garageDoorModelObserver);
        garageModel.getAdtSystemState().observe(this, adtSystemObserver);
        garageModel.getGeofenceGaragePermissive().observe(this, geofenceObserver);

        garageControlButton = getView().findViewById(R.id.garage_control);

        //open or close the garage door, and
        //get IO status update.
        garageControlButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(permissive && geofenceGaragePermissive)
                {
                    garageModel.toggleGarageDoor();
                    Thread thread = new Thread(new Runnable(){
                        public void run()
                        {
                            // Moves the current Thread into the background
                            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                            try {
                                sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            garageModel.getIoData();
                        }
                    });
                    thread.start();
                }

                if(!permissive || !geofenceGaragePermissive)
                {
                    //if either the alarm is set or user is out of range.
                    if(!permissive && !geofenceGaragePermissive)
                    {
                        Toast.makeText(v.getContext(),
                                " System is ARMED and you are out of range.  Can't open Garage Door.",Toast.LENGTH_SHORT).show();
                    }

                    //if the system is armed but user is within geofence.
                    if(!permissive && geofenceGaragePermissive)
                    {
                        Toast.makeText(v.getContext(),
                                " System is ARMED.  Can't open Garage Door.",Toast.LENGTH_SHORT).show();
                    }

                    //if the user is outside the geofence but the system is not armed.
                    if(!geofenceGaragePermissive && permissive)
                    {
                        Toast.makeText(v.getContext(),
                                " You are out of range.  Can't open Garage Door.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
