package time.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.web.bind.annotation.*;
import edu.fudan.common.util.logTime;

import time.service.TimeService;



import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/timeservice")

public class TimeController {

    @Autowired
    private TimeService timeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeController.class);

    @GetMapping(path = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        return "Welcome to [ Time Service ] !";
    }

   

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/loggingTime")
    public HttpEntity queryLoggingTime(@RequestHeader HttpHeaders headers) {
        return ok(timeService.queryLoggingTime(headers));
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/clearTime")
    public HttpEntity clearTime(@RequestHeader HttpHeaders headers) {
        return ok(timeService.clearTime(headers));
    }

}
