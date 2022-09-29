package edu.fudan.common.util;

import java.util.ArrayList;
import java.util.HashMap;

public class logTime {
    public static ArrayList<Long> springEnrtyStart = new ArrayList<Long>();
    public static ArrayList<Long> springEnrtyEnd = new ArrayList<Long>();
    public static ArrayList<Long> springExitStart = new ArrayList<Long>();
    public static ArrayList<Long> springExitEnd = new ArrayList<Long>();
    public static ArrayList<Long> clientSerializationStartTime = new ArrayList<Long>();
    public static ArrayList<Long> sendRequestTime = new ArrayList<Long>();
    public static ArrayList<Long> recvResponseime = new ArrayList<Long>();
    public static ArrayList<Long> clientDeserializationEndTime= new ArrayList<Long>();



    public static ArrayList<Long> recvRequestTime = new ArrayList<Long>();
    public static ArrayList<Long> serverDeserializationEndTime = new ArrayList<Long>();

    public static ArrayList<Long> serverSerializationStartTime = new ArrayList<Long>();
    public static ArrayList<Long> sendResponseTime= new ArrayList<Long>();

    public static ArrayList<Long> selectEventTime= new ArrayList<Long>();

    public static ArrayList<Long> serverSocketProcessTime= new ArrayList<Long>();
    public static ArrayList<Long> serverHTTP11ProcessTime= new ArrayList<Long>();
    public static ArrayList<Long> serverBodyStartTime= new ArrayList<Long>();
    public static ArrayList<Long> serverBodyEndTime= new ArrayList<Long>();

    public static  void clear()
    {
        springEnrtyStart.clear();
        springExitStart.clear();
        springExitEnd.clear();
        springEnrtyEnd.clear();

        clientSerializationStartTime.clear();
        sendRequestTime.clear();
        recvResponseime.clear();
        clientDeserializationEndTime.clear();

        serverDeserializationEndTime.clear();
        recvRequestTime.clear();
        sendResponseTime.clear();
        serverSerializationStartTime.clear();
        selectEventTime.clear();
        serverSocketProcessTime.clear();
        serverHTTP11ProcessTime.clear();
        serverBodyEndTime.clear();
        serverBodyStartTime.clear();
    }

    public static HashMap<String,ArrayList<Long>>  getSpringTime()
    {

        HashMap<String,ArrayList<Long>> list = new HashMap<> ();
        list.put("springEntryStart",springEnrtyStart);
        list.put("springEntryEnd",springEnrtyEnd);
        list.put("springExitStart",springExitStart);
        list.put("springExitEnd",springExitEnd);

        list.put("clientSerializationStartTime",clientSerializationStartTime);
        list.put("sendRequestTime",sendRequestTime);
        list.put("recvResponseime",recvResponseime);
        list.put("clientDeserializationEndTime",clientDeserializationEndTime);


        list.put("serverDeserializationEndTime",serverDeserializationEndTime);
        list.put("recvRequestTime",recvRequestTime);
        list.put("sendResponseTime ",sendResponseTime);
        list.put("serverSerializationStartTime",serverSerializationStartTime);
        list.put("selectEventTime",selectEventTime);
        list.put("serverSocketProcessTime",serverSocketProcessTime);
        list.put("serverHTTP11ProcessTime",serverHTTP11ProcessTime);
        list.put("serverBodyEndTime",serverBodyEndTime);
        list.put("serverBodyStartTime",serverBodyStartTime);

        return  list;
    }


}
