package edu.fudan.common.util;

import java.util.ArrayList;

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

    public static ArrayList<ArrayList<Long>>  getSpringTime()
    {

        ArrayList<ArrayList<Long>> list = new ArrayList<ArrayList<Long>> ();
        list.add(springEnrtyStart);
        list.add(springEnrtyEnd);
        list.add(springExitStart);
        list.add(springExitEnd);
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
