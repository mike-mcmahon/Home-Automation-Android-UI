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

import android.view.View;
import android.widget.EditText;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ca.thirdgear.homeautomation.HomeAutomationApp;
import ca.thirdgear.homeautomation.entity.UserCredentials;
import ca.thirdgear.homeautomation.model.settings.LoginFields;
import ca.thirdgear.homeautomation.model.settings.LoginForm;
import ca.thirdgear.homeautomation.repository.CredentialsRepository;
import javax.inject.Inject;

/**
 * ViewModel for the Settings Fragment
 */
public class SettingsViewModel extends ViewModel
{
    private LoginForm login;
    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPassword;

    @Inject
    CredentialsRepository credentialsRepository;

    public SettingsViewModel()
    {
        HomeAutomationApp.getAppComponent().inject(this);
        credentialsRepository.getCredentials();
    }

    @VisibleForTesting
    public void init()
    {
        login = new LoginForm();
        onFocusEmail =  new View.OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View view, boolean focused)
            {
                EditText et = (EditText) view;
                if (et.getText().length() > 0 && !focused)
                {
                    login.isEmailValid(true);
                }
            }
        };

        onFocusPassword = new View.OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View view, boolean focused)
            {
                EditText et = (EditText) view;
                if (et.getText().length() > 0 && !focused)
                {
                    login.isPasswordValid(true);
                }
            }
        };
    }

    public LoginForm getLogin()
    {
        return login;
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener()
    {
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getPasswordOnFocusChangeListener()
    {
        return onFocusPassword;
    }

    public void onButtonClick()
    {
        login.onClick();
    }

    public MutableLiveData<LoginFields> getLoginFields()
    {
        return login.getLoginFields();
    }

    public LoginForm getForm()
    {
        return login;
    }

    @BindingAdapter("error")
    public static void setError(EditText editText, Object strOrResId)
    {
        if (strOrResId instanceof Integer)
        {
            editText.setError(
                    editText.getContext().getString((Integer) strOrResId));
        }
            else
            {
                editText.setError((String) strOrResId);
            }
    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText editText, View.OnFocusChangeListener onFocusChangeListener)
    {
        if (editText.getOnFocusChangeListener() == null)
        {
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

    public void setCredentials(String userEmail, String password)
    {
        //Call CredentialsRepository method of same name passing the parameters.
        credentialsRepository.setCredentials(userEmail, password);
    }

    public LiveData<UserCredentials> getCredentials()
    {
        return credentialsRepository.getCredentials();
    }
}
