// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2014-06-17 19:43:21
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SRouteModel.java

package com.xinwei.lte.web.lte.model;


public class SRouteModel
{

    public SRouteModel()
    {
    }

    public String getSroute_net()
    {
        return sroute_net;
    }

    public String getSroute_index()
    {
        return sroute_index;
    }

    public void setSroute_index(String sroute_index)
    {
        this.sroute_index = sroute_index;
    }

    public void setSroute_net(String sroute_net)
    {
        this.sroute_net = sroute_net;
    }

    public String getSroute_netmask()
    {
        return sroute_netmask;
    }

    public void setSroute_netmask(String sroute_netmask)
    {
        this.sroute_netmask = sroute_netmask;
    }

    public String getSroute_gw()
    {
        return sroute_gw;
    }

    public void setSroute_gw(String sroute_gw)
    {
        this.sroute_gw = sroute_gw;
    }

    public String getSroute_metirc()
    {
        return sroute_metirc;
    }

    public void setSroute_metirc(String sroute_metirc)
    {
        this.sroute_metirc = sroute_metirc;
    }

    private String sroute_index;
    private String sroute_net;
    private String sroute_netmask;
    private String sroute_gw;
    private String sroute_metirc;
}