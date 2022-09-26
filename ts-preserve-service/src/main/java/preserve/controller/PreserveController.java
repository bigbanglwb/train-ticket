package preserve.controller;

import edu.fudan.common.util.Response;
import edu.fudan.common.util.logTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.*;
import preserve.service.PreserveService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/preserveservice")
public class PreserveController {

    @Autowired
    private PreserveService preserveService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PreserveController.class);

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ Preserve Service ] !";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/preserve")
    public HttpEntity preserve(@RequestBody OrderTicketsInfo oti,
                               @RequestHeader HttpHeaders headers) {
        logTime.springEnrtyStart.add(System.nanoTime());
        PreserveController.LOGGER.info("[preserve][Preserve Account order][from {} to {} at {}]", oti.getFrom(), oti.getTo(), oti.getDate());
        Response re= preserveService.preserve(oti, headers);
        logTime.springEnrtyEnd.add(System.nanoTime());
        return ok(re);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/loggingTime")
    public HttpEntity queryLoggingTime(@RequestHeader HttpHeaders headers) {
        return ok(new Response(1,"success", logTime.getSpringTime()));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/clearTime")
    public HttpEntity clearTime(@RequestHeader HttpHeaders headers) {
        logTime.clear();
        return ok(new Response(1,"success",null));
    }

}
