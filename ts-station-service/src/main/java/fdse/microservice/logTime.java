package fdse.microservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class logTime {

    public static ArrayList<Long> recvRequestTime = new ArrayList<Long>();
    public static ArrayList<Long> deserializationEndTime = new ArrayList<Long>();
    public static ArrayList<Long> logicStartTime = new ArrayList<Long>();
    public static ArrayList<Long> logicEndTime = new ArrayList<Long>();
    public static ArrayList<Long> serializationStartTime = new ArrayList<Long>();
    public static ArrayList<Long> sendResponseTime= new ArrayList<Long>();

    public static ArrayList<Long> selectEventTime= new ArrayList<Long>();



    public static  void clear()
    {
        deserializationEndTime.clear();
        recvRequestTime.clear();
        logicStartTime.clear();
        logicEndTime.clear();
        sendResponseTime.clear();
        serializationStartTime.clear();
        selectEventTime.clear();
    }

    public static void print()
    {
        for (long time :selectEventTime)
        {
            System.out.println("selectEventTime="+time);
        }
        for (long time :recvRequestTime)
        {
            System.out.println("recvRequestTime="+time);
        }
        for (long time :deserializationEndTime)
        {
            System.out.println("server_deserializationEndTime="+time);
        }
        for (long time :logicStartTime)
        {
            System.out.println("logicStartTime="+time);
        }
        for (long time :logicEndTime)
        {
            System.out.println("logicEndTime="+time);
        }
        for (long time :serializationStartTime)
        {
            System.out.println("server_serializationStartTime="+time);
        }
        for (long time :sendResponseTime)
        {
            System.out.println("sendResponseTime="+time);
        }
    }
    public static void print1()
    {
        System.out.println("************************");
        System.out.println("selectEventTime="+selectEventTime.get(0));
        System.out.println("recvRequestTime="+recvRequestTime.get(0));
        System.out.println("server_deserializationEndTime="+deserializationEndTime.get(0));
        System.out.println("logicStartTime="+logicStartTime.get(0));
        System.out.println("logicEndTime="+logicEndTime.get(0));
        System.out.println("server_serializationStartTime="+serializationStartTime.get(0));
        System.out.println("sendResponseTime="+sendResponseTime.get(0));

    }
}
