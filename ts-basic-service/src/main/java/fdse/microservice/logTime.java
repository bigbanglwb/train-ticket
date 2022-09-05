package fdse.microservice;

import java.util.ArrayList;

public class logTime {
    public static ArrayList<Long> serializationStartTime = new ArrayList<Long>();

    public static ArrayList<Long> sendRequestTime = new ArrayList<Long>();
    public static ArrayList<Long> recvResponseime = new ArrayList<Long>();
    public static ArrayList<Long> deserializationEndTime= new ArrayList<Long>();



    public static  void clear()
    {
        serializationStartTime.clear();
        sendRequestTime.clear();
        recvResponseime.clear();
        deserializationEndTime.clear();
    }

    public static void print()
    {
        for (long time :serializationStartTime)
        {
            System.out.println("serializationStartTime: "+time);
        }
        for (long time :sendRequestTime)
        {
            System.out.println("sendRequestTime: "+time);
        }
        for (long time :recvResponseime)
        {
            System.out.println("recvResponseime: "+time);
        }
        for (long time :deserializationEndTime)
        {
            System.out.println("deserializationEndTime: "+time);
        }
    }
}
