package fdse.microservice;

import java.util.ArrayList;
import edu.fudan.common.util.logTime;
public class stationLogTime extends logTime {

    public static ArrayList<Long> recvRequestTime = new ArrayList<Long>();
    public static ArrayList<Long> deserializationEndTime = new ArrayList<Long>();

    public static ArrayList<Long> serializationStartTime = new ArrayList<Long>();
    public static ArrayList<Long> sendResponseTime= new ArrayList<Long>();

    public static ArrayList<Long> selectEventTime= new ArrayList<Long>();

    public static ArrayList<Long> socketProcessTime= new ArrayList<Long>();
    public static ArrayList<Long> HTTP11ProcessTime= new ArrayList<Long>();
    public static ArrayList<Long> bodyStartTime= new ArrayList<Long>();
    public static ArrayList<Long> bodyEndTime= new ArrayList<Long>();


    public static  void clear()
    {
        deserializationEndTime.clear();
        recvRequestTime.clear();
        sendResponseTime.clear();
        serializationStartTime.clear();
        selectEventTime.clear();
        socketProcessTime.clear();
        HTTP11ProcessTime.clear();
        bodyEndTime.clear();
        bodyStartTime.clear();

        springEnrtyStart.clear();
        springExitStart.clear();
        springExitEnd.clear();
        springEnrtyEnd.clear();
    }


}
