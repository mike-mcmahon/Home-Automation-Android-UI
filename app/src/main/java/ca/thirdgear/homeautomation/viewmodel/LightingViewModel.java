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
 * ViewModel for the Lighting Fragment
 */
public class LightingViewModel extends ViewModel
{

    @Inject
    DataRepository dataRepository;

    private LiveData<String> zoneOneState;
    private LiveData<String> zoneTwoState;

    public LightingViewModel()
    {
        //dataRepository = DataRepository.getInstance();
        HomeAutomationApp.getAppComponent().inject(this);
    }

    /**
     * Return zone 1 state
     * @return LiveData<String>
     */
    public LiveData<String> getZoneOneState()
    {
        if(zoneOneState == null)
        {
            zoneOneState = new MutableLiveData<>();
        }
        return zoneOneState;
    }

    /**
     * Return zone 2 state
     * @return LiveData<String>
     */
    public LiveData<String> getZoneTwoState()
    {
        if(zoneTwoState == null)
        {
            zoneTwoState = new MutableLiveData<>();
        }
        return zoneTwoState;
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
     * Allows view to toggle state of zone 1.
     */
    public void toggleZoneOne()
    {
        dataRepository.toggleZoneOne();
        this.updateStateData();
    }

    /**
     * Allows view to toggle state of zone 2.
     */
    public void toggleZoneTwo()
    {
        dataRepository.toggleZoneTwo();
        this.updateStateData();
    }

    /**
     * Update ViewModel fields
     */
    private void updateStateData()
    {
        this.zoneOneState = dataRepository.getZoneOneState();
        this.zoneTwoState = dataRepository.getZoneTwoState();
    }
}
