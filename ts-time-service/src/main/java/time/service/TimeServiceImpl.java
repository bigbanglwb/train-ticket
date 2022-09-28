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

    static  HashMap<String,String> serviceUrlName = new HashMap<String,String>(){{
        put("ts-admin-basic-info-service","adminbasicservice");
        put("ts-admin-order-service","adminorderservice");
        put("ts-admin-route-service","adminrouteservice");
        put("ts-admin-travel-service","admintravelservice");
        put("ts-admin-user-service","adminuserservice/users");
        put("ts-assurance-service","assuranceservice");
        put("ts-auth-service","auth");
        put("ts-basic-service","basicservice");
        put("ts-cancel-service","cancelservice");
        put("ts-config-service","configservice");
        put("ts-consign-price-service","consignpriceservice");
        put("ts-consign-service","consignservice");
        put("ts-contacts-service","contactservice");
        put("ts-execute-service","excuteservice");
        put("ts-food-delivery-service","fooddeliveryservice");
        put("ts-food-service","foodservice");
        put("ts-inside-payment-service","inside_pay_service");
        put("ts-notification-service","notifyservice");
        put("ts-order-other-service","orderOtherService");
        put("ts-order-service","orderservice");
        put("ts-payment-service","paymentservice");
        put("ts-preserve-other-service","preserveotherservice");
        put("ts-preserve-service","preserveservice");
        put("ts-price-service","priceservice");
        put("ts-rebook-service","rebookservice");
        put("ts-route-plan-service","routeplanservice");
        put("ts-route-service","routeservice");
        put("ts-seat-service","seatservice");
        put("ts-security-service","securityservice");
        put("ts-station-food-service","stationfoodservice");
        put("ts-station-service","stationservice");
        put("ts-train-food-service","trainfoodservice");
        put("ts-train-service","trainservice");
        put("ts-travel-plan-service","travelplanservice");
        put("ts-travel-service","travelservice");
        put("ts-travel2-service","travel2service");
        put("ts-user-service","userservice");
        put("ts-verification-code-service","verifycode");
    }};
    public Response queryLoggingTime(HttpHeaders headers) {
        Map<String,Object> map = new HashMap<>();
        for(Map.Entry<String,String> entry : serviceUrlName.entrySet())
        {

            String serviceUrl = getServiceUrl(entry.getKey());
            HttpEntity requestEntity = new HttpEntity(null);
            ResponseEntity<Response> re = restTemplate.exchange(
                    serviceUrl +"/api/v1/" +entry.getValue()+"/loggingTime",
                    HttpMethod.GET,
                    requestEntity,
                    Response.class
            );
            map.put(entry.getKey(),re.getBody().getData());
        }

        return new Response<>(1,"loggingTime",map );
    }

    public Response clearTime(HttpHeaders headers) {
        for(Map.Entry<String,String> entry : serviceUrlName.entrySet())
        {

            String serviceUrl = getServiceUrl(entry.getKey());
            HttpEntity requestEntity = new HttpEntity(null);
            ResponseEntity<Response> re = restTemplate.exchange(
                    serviceUrl +"/api/v1/" +entry.getValue()+"/clearTime",
                    HttpMethod.GET,
                    requestEntity,
                    Response.class
            );
        }

        logTime.clear();
        return new Response<>(1,"clearTime",null );
    }

    
}
