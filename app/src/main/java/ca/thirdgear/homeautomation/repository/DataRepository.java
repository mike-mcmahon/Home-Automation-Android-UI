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
package ca.thirdgear.homeautomation.repository;

import android.util.Log;
import androidx.lifecycle.*;
import ca.thirdgear.homeautomation.HomeAutomationApp;
import ca.thirdgear.homeautomation.dao.UserCredentialsDAO;
import ca.thirdgear.homeautomation.entity.UserCredentials;
import ca.thirdgear.homeautomation.geofencing.GeofencingService;
import ca.thirdgear.homeautomation.http.HomeAutomationAPI;
import ca.thirdgear.homeautomation.http.NetworkServiceController;
import ca.thirdgear.homeautomation.model.NetworkApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javax.inject.Inject;
import javax.inject.Singleton;
import static java.lang.Thread.sleep;

/**
 * Repository class for handling network calls to REST API
 */
@Singleton
public class DataRepository
{
    //Network components
    @Inject
    NetworkServiceController networkServiceController;

    //DAO
    @Inject
    UserCredentialsDAO credentialsDAO;

    //Geofencing component
    @Inject
    GeofencingService geofencingService;

    private HomeAutomationAPI homeAutomationAPIService;
    private MutableLiveData<String> zoneOneState;
    private MutableLiveData<String> zoneTwoState;
    private MutableLiveData<String> garageDoorState;
    private MutableLiveData<String> adtSystemState;
    private MutableLiveData<String> panelTemperature;
    private String username;
    private String password;
    private static final String TAG = DataRepository.class.getSimpleName();

    @Inject
    public DataRepository()
    {
        //Inject Network Backend Components, Room DAO, & Geofencing Service
        HomeAutomationApp.getAppComponent().inject(this);

        //get user credentials immediately to allow network services to operate, then observe
        //for changes to user credentials.
        UserCredentials user = credentialsDAO.getUser();
        this.username = user.getUsername();
        this.password = user.getPassword();

        //create the network service
        createNetworkService();

        //create credentials observer, and observe changes in user credentials.
        //if there are changes to the credentials, create a new network service object.
        final Observer<UserCredentials> credentialsObserver = new Observer<UserCredentials>() {
            @Override
            public void onChanged(UserCredentials userCredentials)
            {
                //update string values for credentials
                username = userCredentials.getUsername();
                password = userCredentials.getPassword();

                //call createNetworkService() to create a new network service object
                createNetworkService();
            }
        };

        //register the observer
        credentialsDAO.getObservableUser().observeForever(credentialsObserver);

        zoneOneState = new MutableLiveData<>();
        zoneTwoState = new MutableLiveData<>();
        garageDoorState = new MutableLiveData<>();
        adtSystemState = new MutableLiveData<>();
        panelTemperature = new MutableLiveData<>();

        //Poll the server every 5 seconds on a separate thread for changes in the IO
        Thread thread = new Thread(new Runnable(){
            public void run()
            {
                // Moves the current Thread into the background
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                while(true)
                {
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getIoStatus();
                }
            }
        });
        thread.start();
    }

    /**
     * Get status of remote API I/O
     */
    public void getIoStatus()
    {
        Call<NetworkApiResponse> call = homeAutomationAPIService.getIoStatus();
        call.enqueue(new Callback<NetworkApiResponse>() {
            @Override
            public void onResponse(Call<NetworkApiResponse> call, Response<NetworkApiResponse> response)
            {
                if(response.isSuccessful())
                {
                    zoneOneState.setValue(response.body().getZoneOneState());
                    zoneTwoState.setValue(response.body().getZoneTwoState());
                    garageDoorState.setValue(response.body().getGarageDoorState());
                    adtSystemState.setValue(response.body().getSystemState());
                    panelTemperature.setValue(response.body().getPanelTemperature());
                }
                else
                {
                    String responseCode = String.valueOf(response.code());
                    Log.e(TAG, "getIoStatus() Network call returned with " + responseCode);
                }
            }

            @Override
            public void onFailure(Call<NetworkApiResponse> call, Throwable t)
            {
                Log.e(TAG, "Network call getIoStatus() failed on the client side.");
            }
        });
    }

    /**
     * Toggle Zone 1
     */
    public void toggleZoneOne()
    {
        Call<NetworkApiResponse> call = homeAutomationAPIService.toggleZoneOne();
        call.enqueue(new Callback<NetworkApiResponse>() {
            @Override
            public void onResponse(Call<NetworkApiResponse> call, Response<NetworkApiResponse> response)
            {
                if(response.isSuccessful())
                {
                    zoneOneState.setValue(response.body().getZoneOneState());
                    zoneTwoState.setValue(response.body().getZoneTwoState());
                    garageDoorState.setValue(response.body().getGarageDoorState());
                    adtSystemState.setValue(response.body().getSystemState());
                    panelTemperature.setValue(response.body().getPanelTemperature());
                }
                else
                {
                    String responseCode = String.valueOf(response.code());
                    Log.e(TAG, "toggleZoneOne() Network call returned with " + responseCode);
                }
            }

            @Override
            public void onFailure(Call<NetworkApiResponse> call, Throwable t)
            {
                Log.e(TAG, "Network call toggleZoneOne() failed on the client side.");
            }
        });
    }

    /**
     * Toggle Zone 2
     */
    public void toggleZoneTwo()
    {
        Call<NetworkApiResponse> call = homeAutomationAPIService.toggleZoneTwo();
        call.enqueue(new Callback<NetworkApiResponse>() {
            @Override
            public void onResponse(Call<NetworkApiResponse> call, Response<NetworkApiResponse> response)
            {
                if(response.isSuccessful())
                {
                    zoneOneState.setValue(response.body().getZoneOneState());
                    zoneTwoState.setValue(response.body().getZoneTwoState());
                    garageDoorState.setValue(response.body().getGarageDoorState());
                    adtSystemState.setValue(response.body().getSystemState());
                    panelTemperature.setValue(response.body().getPanelTemperature());
                }
                else
                {
                    String responseCode = String.valueOf(response.code());
                    Log.e(TAG, "toggleZoneTwo() Network call returned with " + responseCode);
                }
            }

            @Override
            public void onFailure(Call<NetworkApiResponse> call, Throwable t)
            {
                Log.e(TAG, "Network call toggleZoneTwo() failed on the client side.");
            }
        });
    }

    /**
     * Toggle garage door
     */
    public void toggleGarageDoor()
    {
        Call<NetworkApiResponse> call = homeAutomationAPIService.toggleGarageDoor();
        call.enqueue(new Callback<NetworkApiResponse>() {
            @Override
            public void onResponse(Call<NetworkApiResponse> call, Response<NetworkApiResponse> response)
            {
                if(response.isSuccessful())
                {
                    zoneOneState.setValue(response.body().getZoneOneState());
                    zoneTwoState.setValue(response.body().getZoneTwoState());
                    garageDoorState.setValue(response.body().getGarageDoorState());
                    adtSystemState.setValue(response.body().getSystemState());
                    panelTemperature.setValue(response.body().getPanelTemperature());
                }
                else
                {
                    String responseCode = String.valueOf(response.code());
                    Log.e(TAG, "toggleGarageDoor() Network call returned with " + responseCode);
                }
            }

            @Override
            public void onFailure(Call<NetworkApiResponse> call, Throwable t)
            {
                Log.e(TAG, "Network call toggleGarageDoor() failed on the client side.");
            }
        });
    }

    /**
     * Create the network service when there are changes in the user credentials data.
     */
    private void createNetworkService()
    {
        homeAutomationAPIService = networkServiceController
                .createService(HomeAutomationAPI.class, username, password);
    }

    /**
     * Return zone one state to ViewModel
     * @return MutableLiveData<String>
     */
    public MutableLiveData<String> getZoneOneState()
    {
        return zoneOneState;
    }

    /**
     * Return zone two state to ViewModel
     * @return MutableLiveData<String>
     */
    public MutableLiveData<String> getZoneTwoState()
    {
        return zoneTwoState;
    }

    /**
     * Return garage door state to ViewModel
     * @return MutableLiveData<String>
     */
    public MutableLiveData<String> getGarageDoorState()
    {
        return garageDoorState;
    }

    /**
     * Return ADT System State to ViewModel
     * @return MutableLiveData<String>
     */
    public MutableLiveData<String> getAdtSystemState()
    {
        return adtSystemState;
    }

    /**
     * Return panel temperature to ViewModel
     * @return MutableLiveData<String>
     */
    public MutableLiveData<String> getPanelTemperature()
    {
        return panelTemperature;
    }

    /**
     * Get and return current geofence permissive and send to ViewModel
     * @return LiveData<Boolean>
     */
    public LiveData<Boolean> getGeofenceGaragePermissive()
    {
        return geofencingService.getGeofenceGaragePermissive();
    }

}