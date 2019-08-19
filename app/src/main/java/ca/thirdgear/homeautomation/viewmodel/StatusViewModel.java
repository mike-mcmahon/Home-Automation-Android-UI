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
package ca.thirdgear.homeautomation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ca.thirdgear.homeautomation.HomeAutomationApp;
import ca.thirdgear.homeautomation.repository.DataRepository;
import javax.inject.Inject;

/**
 * ViewModel for control panel and ADT system status information.
 */
public class StatusViewModel extends ViewModel
{

    @Inject
    DataRepository dataRepository;

    private LiveData<String> adtSystemState;
    private LiveData<String> panelTemperature;

    public StatusViewModel()
    {
        //dataRepository = DataRepository.getInstance();
        HomeAutomationApp.getAppComponent().inject(this);
    }

    /**
     * Return ADT System state to view
     * @return LiveData<String>
     */
    public LiveData<String> getAdtSystemState()
    {
        if(adtSystemState == null)
        {
            adtSystemState = new MutableLiveData<>();
        }
        return adtSystemState;
    }

    /**
     * Return panel temperature to view
     * @return LiveData<String>
     */
    public LiveData<String> getPanelTemperature()
    {
        if(panelTemperature == null)
        {
            panelTemperature = new MutableLiveData<>();
        }
        return panelTemperature;
    }

    /**
     * Used to initialize the value of zone one and zone two LiveData fields
     * when called in Lighting onViewCreated() & Button onClickListener
     */
    public void getIoData()
    {
        dataRepository.getIoStatus();
        this.updateStateData();
    }

    /**
     * Update ViewModel fields
     */
    private void updateStateData()
    {
        this.adtSystemState = dataRepository.getAdtSystemState();
        this.panelTemperature = dataRepository.getPanelTemperature();
    }
}
