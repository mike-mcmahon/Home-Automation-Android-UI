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
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ca.thirdgear.homeautomation.R;
import ca.thirdgear.homeautomation.viewmodel.StatusViewModel;

/**
 * Fragment displays interface for System Status data.
 */
public class SystemStatus extends Fragment
{
    private StatusViewModel statusViewModel;
    private TextView adtTextView;
    private TextView temperatureTextView;

    public SystemStatus()
    {
        //do nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.system_status_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        statusViewModel = ViewModelProviders.of(this).get(StatusViewModel.class);
        adtTextView = getView().findViewById(R.id.status);
        temperatureTextView = getView().findViewById(R.id.text_temperature);

        //observe changes in ADT System
        final Observer<String> adtSystemModelObserver = new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                if(s.equals("DISARMED"))
                {
                    adtTextView.setText("ADT System is DISARMED");
                    adtTextView.setTextColor(Color.BLACK);
                }
                if(s.equals("ARMED"))
                {
                    adtTextView.setText("ADT System is ARMED");
                    adtTextView.setTextColor(Color.BLACK);
                }
            }
        };

        //observe changes in panel temperature
        final Observer<String> panelTempModelObserver = new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                temperatureTextView.setText(s + " degrees C");
                temperatureTextView.setTextColor(Color.BLACK);

            }
        };

        //initialize IO status/data
        statusViewModel.getIoData();

        //Unsubscribe old observers to prevent memory leaks
        statusViewModel.getAdtSystemState().removeObservers(this);
        statusViewModel.getPanelTemperature().removeObservers(this);

        //Register observers
        statusViewModel.getAdtSystemState().observe(this, adtSystemModelObserver);
        statusViewModel.getPanelTemperature().observe(this, panelTempModelObserver);
    }
}
