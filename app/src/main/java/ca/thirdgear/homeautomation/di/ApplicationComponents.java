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
package ca.thirdgear.homeautomation.di;

import ca.thirdgear.homeautomation.repository.CredentialsRepository;
import ca.thirdgear.homeautomation.repository.DataRepository;
import ca.thirdgear.homeautomation.viewmodel.GarageViewModel;
import ca.thirdgear.homeautomation.viewmodel.LightingViewModel;
import ca.thirdgear.homeautomation.viewmodel.SettingsViewModel;
import ca.thirdgear.homeautomation.viewmodel.StatusViewModel;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Injector class/Component which assigns references to services and view models defined
 * in module classes.
 */
@Singleton
@Component(modules={NetworkModule.class, DataRepositoryModule.class, DatabaseModule.class, CredentialsRepoModule.class, GeofencingServiceModule.class})
public interface ApplicationComponents
{
    void inject(DataRepository repository);
    void inject(LightingViewModel lightingViewModel);
    void inject(GarageViewModel garageViewModel);
    void inject(StatusViewModel statusViewModel);
    void inject(CredentialsRepository credentialsRepository);
    void inject(SettingsViewModel settingsViewModel);
}
