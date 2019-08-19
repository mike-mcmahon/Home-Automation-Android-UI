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
package ca.thirdgear.homeautomation.model.settings;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;
import ca.thirdgear.homeautomation.BR;
import ca.thirdgear.homeautomation.R;

/**
 * Class models UI form behavior.
 */
public class LoginForm extends BaseObservable
{
    private LoginFields fields = new LoginFields();
    private LoginErrorFields errors = new LoginErrorFields();
    private MutableLiveData<LoginFields> buttonClick = new MutableLiveData<>();

    @Bindable
    public boolean isValid()
    {
        boolean valid = isEmailValid(false);
        valid = isPasswordValid(false) && valid;
        notifyPropertyChanged(BR.emailError);
        notifyPropertyChanged(BR.passwordError);
        return valid;
    }

    public boolean isEmailValid(boolean setMessage)
    {
        // Minimum a@b.c
        String email = fields.getEmail();
        if (email != null && email.length() > 5)
        {
            int indexOfAt = email.indexOf("@");
            int indexOfDot = email.lastIndexOf(".");
            if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < email.length() - 1)
            {
                errors.setEmail(null);
                notifyPropertyChanged(BR.valid);
                return true;
            }
            else
                {
                    if (setMessage)
                    {
                        errors.setEmail(R.string.error_format_invalid);
                        notifyPropertyChanged(BR.valid);
                    }
                    return false;
                }
        }
        if (setMessage)
        {
            errors.setEmail(R.string.error_too_short);
            notifyPropertyChanged(BR.valid);
        }

        return false;
    }

    public boolean isPasswordValid(boolean setMessage)
    {
        String password = fields.getPassword();
        if (password != null && password.length() > 5)
        {
            errors.setPassword(null);
            notifyPropertyChanged(BR.valid);
            return true;
        } else
            {
                if (setMessage)
                {
                    errors.setPassword(R.string.error_too_short);
                    notifyPropertyChanged(BR.valid);
                }

                return false;
            }
    }

    public void onClick()
    {
        if (isValid())
        {
            buttonClick.setValue(fields);
        }
    }

    public MutableLiveData<LoginFields> getLoginFields()
    {
        return buttonClick;
    }

    public LoginFields getFields()
    {
        return fields;
    }

    @Bindable
    public Integer getEmailError()
    {
        return errors.getEmail();
    }

    @Bindable
    public Integer getPasswordError()
    {
        return errors.getPassword();
    }
}
