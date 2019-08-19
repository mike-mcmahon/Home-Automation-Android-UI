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
package ca.thirdgear.homeautomation.http;

import android.text.TextUtils;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Creates the network services required for this application.
 */
@Singleton
@SuppressWarnings("deprecation")
public class NetworkServiceController
{
    private static final String BASE_URL = "https://www.xxxxxxxxxx.xxx/xxxxxxxxxx/";


    @Inject
    public NetworkServiceController()
    {
        //do nothing
    }

    //create OkHttpClient
    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

    //create Retrofit
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    /**
     * Create and return http service.
     * @param serviceClass
     * @param username
     * @param password
     * @param <S>
     * @return Service
     */
    public static <S> S createService(Class<S> serviceClass, String username, String password)
    {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password))
        {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null);
    }

    /**
     * Create and return http service.
     * @param serviceClass
     * @param authToken
     * @param <S>
     * @return Service
     */
    private static <S> S createService(Class<S> serviceClass, final String authToken)
    {
        if (!TextUtils.isEmpty(authToken))
        {
            CredentialsInterceptor interceptor = new CredentialsInterceptor(authToken);

            if (!okHttpClient.interceptors().contains(interceptor))
            {
                okHttpClient.addInterceptor(interceptor);

                retrofitBuilder.client(okHttpClient.build());
                retrofit = retrofitBuilder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

}
