package edu.fudan.common.util;

import java.util.ArrayList;
import java.util.HashMap;

public class logTime {
    public static ArrayList<Long> springEnrtyStart = new ArrayList<Long>();
    public static ArrayList<Long> springEnrtyEnd = new ArrayList<Long>();
    public static ArrayList<Long> springExitStart = new ArrayList<Long>();
    public static ArrayList<Long> springExitEnd = new ArrayList<Long>();
    public static ArrayList<Long> serializationStartTime = new ArrayList<Long>();
    public static ArrayList<Long> sendRequestTime = new ArrayList<Long>();
    public static ArrayList<Long> recvResponseime = new ArrayList<Long>();
    public static ArrayList<Long> deserializationEndTime= new ArrayList<Long>();

    public static  void clear()
    {
        springEnrtyStart.clear();
        springExitStart.clear();
        springExitEnd.clear();
        springEnrtyEnd.clear();
        serializationStartTime.clear();
        sendRequestTime.clear();
        recvResponseime.clear();
        deserializationEndTime.clear();
    }

    public static HashMap<String,ArrayList<Long>>  getSpringTime()
    {

        HashMap<String,ArrayList<Long>> list = new HashMap<> ();
        list.put("springEntryStart",springEnrtyStart);
        list.put("springEntryEnd",springEnrtyEnd);
        list.put("springExitStart",springExitStart);
        list.put("springExitEnd",springExitEnd);

        list.put("serializationStartTime",serializationStartTime);
        list.put("sendRequestTime",sendRequestTime);
        list.put("recvResponseime ",recvResponseime);
        list.put("deserializationEndTime",deserializationEndTime);

        return  list;
    }


}
