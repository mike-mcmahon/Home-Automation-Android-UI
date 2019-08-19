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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ca.thirdgear.homeautomation.R;
import ca.thirdgear.homeautomation.viewmodel.LightingViewModel;
import static java.lang.Thread.sleep;

/**
 * Fragment displays the buttons and contains the controls for zoned lighting.
 */
public class Lighting extends Fragment
{
    private LightingViewModel lightingModel;
    private Button foyerLightsControlButton;
    private Button frontDoorLightsControlButton;

    public Lighting()
    {
        //do nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.lighting_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        lightingModel = ViewModelProviders.of(this).get(LightingViewModel.class);

        //observe changes in lighting
        final Observer<String> foyerLightsModelObserver = new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                if(s.equals("OFF"))
                {
                    foyerLightsControlButton.setText("Foyer Lights OFF");
                    foyerLightsControlButton.setTextColor(Color.WHITE);
                    foyerLightsControlButton.setBackgroundColor(Color.rgb(63, 81, 181));
                }
                if(s.equals("ON"))
                {
                    foyerLightsControlButton.setText("Foyer Lights ON");
                    foyerLightsControlButton.setTextColor(Color.BLACK);
                    foyerLightsControlButton.setBackgroundColor(Color.GREEN);
                }
            }
        };

        //observe changes in lighting
        final Observer<String> frontDoorLightModelObserver = new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                if(s.equals("OFF"))
                {
                    frontDoorLightsControlButton.setText("Front Door Light OFF");
                    frontDoorLightsControlButton.setTextColor(Color.WHITE);
                    frontDoorLightsControlButton.setBackgroundColor(Color.rgb(63, 81, 181));
                }
                if(s.equals("ON"))
                {
                    frontDoorLightsControlButton.setText("Front Door Light ON");
                    frontDoorLightsControlButton.setTextColor(Color.BLACK);
                    frontDoorLightsControlButton.setBackgroundColor(Color.GREEN);
                }
            }
        };

        //initialize IO status/data
        lightingModel.getIoData();

        //Unsubscribe old observers to prevent memory leaks
        lightingModel.getZoneOneState().removeObservers(this);
        lightingModel.getZoneTwoState().removeObservers(this);

        //Register observers
        lightingModel.getZoneOneState().observe(this, foyerLightsModelObserver);
        lightingModel.getZoneTwoState().observe(this, frontDoorLightModelObserver);

        foyerLightsControlButton = getView().findViewById(R.id.zone1);
        frontDoorLightsControlButton = getView().findViewById(R.id.zone2);

        //turn on/off foyer lights, and
        //get IO status update.
        foyerLightsControlButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                lightingModel.toggleZoneOne();
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
                        lightingModel.getIoData();
                    }
                });
                thread.start();
            }
        });

        //turn on/off front door light, and
        //get IO status update.
        frontDoorLightsControlButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                lightingModel.toggleZoneTwo();
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
                        lightingModel.getIoData();
                    }
                });
                thread.start();
            }
        });
    }
}
