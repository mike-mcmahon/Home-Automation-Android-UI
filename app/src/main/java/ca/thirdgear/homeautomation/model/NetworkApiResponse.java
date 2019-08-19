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
package ca.thirdgear.homeautomation.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Model for the API response data.
 */
@Root(name = "onboardGpio", strict = false)
public class NetworkApiResponse
{
    @Element(name = "garageDoorFeedbackState")
    private String garageDoorState;

    @Element(name = "zoneOneState")
    private String zoneOneState;

    @Element(name = "zoneTwoState")
    private String zoneTwoState;

    @Element(name = "zoneThreeState")
    private String zoneThreeState;

    @Element(name = "systemArmedState")
    private String systemArmed;

    @Element(name = "panelTemperature")
    private String panelTemperature;

    @Element(name = "garageDoorRelayState")
    private String garageDoorRelayState;

    @Element(name = "zoneOneRelayState")
    private String zoneOneRelayState;

    @Element(name = "zoneTwoRelayState")
    private String zoneTwoRelayState;

    @Element(name = "zoneThreeRelayState")
    private String zoneThreeRelayState;

    public String getGarageDoorState()
    {
        return garageDoorState;
    }

    public void setGarageDoorState(String state)
    {
        this.garageDoorState = state;
    }

    public String getZoneOneState()
    {
        return zoneOneState;
    }

    public void setZoneOneState(String state)
    {
        this.zoneOneState = state;
    }

    public String getZoneTwoState()
    {
        return zoneTwoState;
    }

    public void setZoneTwoState(String state)
    {
        this.zoneTwoState = state;
    }

    public String getZoneThreeState()
    {
        return zoneThreeState;
    }

    public void setZoneThreeState(String state)
    {
        this.zoneThreeState = state;
    }

    public String getSystemState()
    {
        return systemArmed;
    }

    public void setSystemState(String armed)
    {
        this.systemArmed = armed;
    }

    public String getPanelTemperature()
    {
        return panelTemperature;
    }

    public void setPanelTemperature(String temp)
    {
        this.panelTemperature = temp;
    }

    public String getGarageDoorRelayState()
    {
        return garageDoorRelayState;
    }

    public void setGarageDoorRelayState(String state)
    {
        this.garageDoorRelayState = state;
    }

    public String getZoneOneRelayState()
    {
        return zoneOneRelayState;
    }

    public void setZoneOneRelayState(String state)
    {
        this.zoneOneRelayState = state;
    }

    public String getZoneTwoRelayState()
    {
        return zoneTwoRelayState;
    }

    public void setZoneTwoRelayState(String state)
    {
        this.zoneTwoRelayState = state;
    }

    public String getZoneThreeRelayState()
    {
        return zoneThreeRelayState;
    }

    public void setZoneThreeRelayState(String state)
    {
        this.zoneThreeRelayState = state;
    }
}

