package fdse.microservice;

import java.util.ArrayList;

public class logTime {
    public static ArrayList<Long> RPCStartTime = new ArrayList<Long>();
    public static ArrayList<Long> serializationStartTime = new ArrayList<Long>();
    public static ArrayList<Long> sendRequestTime = new ArrayList<Long>();
    public static ArrayList<Long> recvResponseime = new ArrayList<Long>();
    public static ArrayList<Long> deserializationEndTime= new ArrayList<Long>();
    public static ArrayList<Long> RPCEndTime= new ArrayList<Long>();


    public static  void clear()
    {
        RPCStartTime.clear();
        serializationStartTime.clear();
        sendRequestTime.clear();
        recvResponseime.clear();
        deserializationEndTime.clear();
        RPCEndTime.clear();
    }

    public static void print()
    {
        System.out.println("************************");
        for (long time :RPCStartTime)
        {
            System.out.println("RPCStartTime="+time);
        }
        for (long time :serializationStartTime)
        {
            System.out.println("client_serializationStartTime="+time);
        }
        for (long time :sendRequestTime)
        {
            System.out.println("sendRequestTime="+time);
        }
        for (long time :recvResponseime)
        {
            System.out.println("recvResponseime="+time);
        }
        for (long time :deserializationEndTime)
        {
            System.out.println("client_deserializationEndTime="+time);
        }
        for (long time :RPCEndTime)
        {
            System.out.println("RPCEndTime="+time);
        }
    }
}
