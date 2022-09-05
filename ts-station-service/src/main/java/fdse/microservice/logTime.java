package fdse.microservice;

import java.util.ArrayList;

public class logTime {
    public static ArrayList<Long> recvRequestTime = new ArrayList<Long>();
    public static ArrayList<Long> deserializationEndTime = new ArrayList<Long>();
    public static ArrayList<Long> logicStartTime = new ArrayList<Long>();
    public static ArrayList<Long> logicEndTime = new ArrayList<Long>();
    public static ArrayList<Long> serializationStartTime = new ArrayList<Long>();
    public static ArrayList<Long> sendResponseTime= new ArrayList<Long>();



    public static  void clear()
    {
        recvRequestTime.clear();
        logicStartTime.clear();
        logicEndTime.clear();
        sendResponseTime.clear();
    }

    public static void print()
    {
        for (long time :recvRequestTime)
        {
            System.out.println("recvRequestTime: "+time);
        }
        for (long time :logicStartTime)
        {
            System.out.println("logicStartTime: "+time);
        }
        for (long time :logicEndTime)
        {
            System.out.println("logicEndTime: "+time);
        }
        for (long time :sendResponseTime)
        {
            System.out.println("sendResponseTime: "+time);
        }
    }
}
