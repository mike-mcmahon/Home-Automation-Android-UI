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
 * ViewModel for the Garage Fragment
 */
public class GarageViewModel extends ViewModel
{

    @Inject
    DataRepository dataRepository;

    private LiveData<String> garageDoorState;
    private LiveData<String> adtSystemState;

    public GarageViewModel()
    {
        HomeAutomationApp.getAppComponent().inject(this);
    }

    /**
     * Return garage door state to view.
     * @return LiveData<String>
     */
    public LiveData<String> getGarageDoorState()
    {
        if(garageDoorState == null)
        {
            garageDoorState = new MutableLiveData<>();
        }
        return garageDoorState;
    }

    /**
     * Return ADT System state to view.
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
     * Return geofencing permissive to view.
     * @return LiveData<Boolean>
     */
    public LiveData<Boolean> getGeofenceGaragePermissive()
    {
        return dataRepository.getGeofenceGaragePermissive();
    }

    /**
     * Used to initialize the value of garage door LiveData field
     * when called in Garage onViewCreated() & Button onClickListener
     */
    public void getIoData()
    {
        dataRepository.getIoStatus();
        this.updateStateData();
    }

    /**
     * Allows the view to open/close the garage door.
     */
    public void toggleGarageDoor()
    {
        dataRepository.toggleGarageDoor();
        this.updateStateData();
    }

    /**
     * Update ViewModel fields
     */
    private void updateStateData()
    {
        this.garageDoorState = dataRepository.getGarageDoorState();
        this.adtSystemState = dataRepository.getAdtSystemState();
    }
}
