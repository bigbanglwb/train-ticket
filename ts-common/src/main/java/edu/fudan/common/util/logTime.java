package edu.fudan.common.util;

import java.util.ArrayList;
import java.util.HashMap;

public class logTime {
    public static ArrayList<Long> springEnrtyStart = new ArrayList<Long>();
    public static ArrayList<Long> springEnrtyEnd = new ArrayList<Long>();
    public static ArrayList<Long> springExitStart = new ArrayList<Long>();
    public static ArrayList<Long> springExitEnd = new ArrayList<Long>();


    public static  void clear()
    {
        springEnrtyStart.clear();
        springExitStart.clear();
        springExitEnd.clear();
        springEnrtyEnd.clear();
    }

    public static HashMap<String,ArrayList<Long>>  getSpringTime()
    {

        HashMap<String,ArrayList<Long>> list = new HashMap<> ();
        list.put("springEntryStart",springEnrtyStart);
        list.put("springEntryEnd",springEnrtyEnd);
        list.put("springExitStart",springExitStart);
        list.put("springExitEnd",springExitEnd);
        return  list;
    }


    public static void printSpring()
    {
        System.out.println("************************");
        for (long time :springEnrtyStart)
        {
            System.out.println("springEnrtyStart="+time);
        }
        for (long time :springEnrtyEnd)
        {
            System.out.println("springEnrtyEnd="+time);
        }
        for (long time :springExitStart)
        {
            System.out.println("springExitStart="+time);
        }
        for (long time :springExitEnd)
        {
            System.out.println("springExitEnd="+time);
        }

    }
}
