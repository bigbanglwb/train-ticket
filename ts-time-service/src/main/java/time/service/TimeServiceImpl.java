package time.service;

import edu.fudan.common.util.Response;
import edu.fudan.common.util.logTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



import java.util.*;
import java.util.concurrent.*;

/**
 * @author fdse
 */
@Service
public class TimeServiceImpl implements TimeService {



    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeServiceImpl.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(20, new CustomizableThreadFactory("HttpClientThreadPool-"));

    private String getServiceUrl(String serviceName) {
        return "http://" + serviceName;
    }


    public Response queryLoggingTime(HttpHeaders headers) {
        List<String> serviceList = Arrays.asList(
                "basic",
                "station",
                "train",
                "route",
                "price",
                "seat",
                "order",
                "config",
                "stationfood",
                "food",
                "consign",
                "user",
                "preserve",
                "trainfood",
                "security",
                "orderOther",
                "contacts",
                "assurance",
                "travel"
        );
        Map<String,Object> map = new HashMap<>();
        for(String serviceName :serviceList)
        {
            String serviceUrl;
            String parameter;
            if (serviceName =="stationfood")
            {
                serviceUrl= getServiceUrl("ts-station-food-service");
                parameter = "/api/v1/"+serviceName+"service"+"/loggingTime";
            }
            else if(serviceName =="trainfood")
            {
                serviceUrl= getServiceUrl("ts-train-food-service");
                parameter = "/api/v1/"+serviceName+"service"+"/loggingTime";
            }
            else if(serviceName =="orderOther")
            {
                serviceUrl= getServiceUrl("ts-order-other-service");
                parameter = "/api/v1/"+serviceName+"Service"+"/loggingTime";
            }
            else if(serviceName =="contacts")
            {
                serviceUrl= getServiceUrl("ts-contacts-service");
                parameter = "/api/v1/contactservice/loggingTime";
            }
            else {
                serviceUrl = getServiceUrl("ts-"+serviceName+"-service");
                parameter = "/api/v1/"+serviceName+"service"+"/loggingTime";
            }

            HttpEntity requestEntity = new HttpEntity(null);
            ResponseEntity<Response> re = restTemplate.exchange(
                    serviceUrl + parameter,
                    HttpMethod.GET,
                    requestEntity,
                    Response.class
            );
            map.put(serviceName,re.getBody().getData());
        }
        map.put("travel",logTime.getSpringTime());
        return new Response<>(1,"loggingTime",map );
    }

    public Response clearTime(HttpHeaders headers) {
        List<String> serviceList = Arrays.asList(
                "basic",
                "station",
                "train",
                "route",
                "price",
                "seat",
                "order",
                "config",
                "stationfood",
                "food",
                "consign",
                "user",
                "preserve",
                "trainfood",
                "security",
                "orderOther",
                "contacts",
                "assurance",
                "travel"
        );

        for(String serviceName :serviceList)
        {
            String serviceUrl;
            String parameter;
            if (serviceName =="stationfood")
            {
                serviceUrl= getServiceUrl("ts-station-food-service");
                parameter = "/api/v1/"+serviceName+"service"+"/clearTime";
            }
            else if(serviceName =="trainfood")
            {
                serviceUrl= getServiceUrl("ts-train-food-service");
                parameter = "/api/v1/"+serviceName+"service"+"/clearTime";
            }
            else if(serviceName =="orderOther")
            {
                serviceUrl= getServiceUrl("ts-order-other-service");
                parameter = "/api/v1/"+serviceName+"Service"+"/clearTime";
            }
            else if(serviceName =="contacts")
            {
                serviceUrl= getServiceUrl("ts-contacts-service");
                parameter = "/api/v1/contactservice/clearTime";
            }
            else {
                serviceUrl = getServiceUrl("ts-"+serviceName+"-service");
                parameter = "/api/v1/"+serviceName+"service"+"/clearTime";
            }
            HttpEntity requestEntity = new HttpEntity(null);
            ResponseEntity<Response> re = restTemplate.exchange(
                    serviceUrl + parameter,
                    HttpMethod.GET,
                    requestEntity,
                    Response.class
            );

        }

        logTime.clear();
        return new Response<>(1,"clearTime",null );
    }

    
}
