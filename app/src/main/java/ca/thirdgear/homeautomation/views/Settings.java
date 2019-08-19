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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ca.thirdgear.homeautomation.R;
import ca.thirdgear.homeautomation.entity.UserCredentials;
import ca.thirdgear.homeautomation.model.settings.LoginFields;
import ca.thirdgear.homeautomation.viewmodel.SettingsViewModel;
import ca.thirdgear.homeautomation.databinding.SettingsFragmentBinding;

/**
 * Fragment displays interface for user settings (user name and password entry).
 * TBD - Enter geographic information for location services.
 */
public class Settings extends Fragment
{
    private SettingsViewModel settingsViewModel;

    public Settings()
    {
        //do nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        SettingsFragmentBinding fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false);

        View settingsView = fragmentBinding.getRoot();

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        if (savedInstanceState == null)
        {
            settingsViewModel.init();
        }

        fragmentBinding.setModel(settingsViewModel);

        setupButtonClick();

        //Unsubscribe old observers to prevent memory leaks
        settingsViewModel.getCredentials().removeObservers(this);

        //Register observers
        settingsViewModel.getCredentials().observe(this, new Observer<UserCredentials>() {
            @Override
            public void onChanged(UserCredentials userCredentials) {
                Toast.makeText(getView().getContext(), "User credentials set for user: " + userCredentials.getUsername(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return settingsView;
    }

    /**
     * Helper method to set up button on click listener.
     */
    private void setupButtonClick()
    {
        settingsViewModel.getLoginFields().observe(this, new Observer<LoginFields>()
        {
            @Override
            public void onChanged(LoginFields loginModel)
            {
                //When the user has entered their credentials, set the values in the DB and for use in
                //the NetworkServiceController.
                settingsViewModel.setCredentials(loginModel.getEmail(), loginModel.getPassword());
            }
        });
    }
}
