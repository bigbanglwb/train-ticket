package seat.controller;

import edu.fudan.common.util.Response;
import edu.fudan.common.util.logTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.Seat;
import seat.service.SeatService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/seatservice")
public class SeatController {

    @Autowired
    private SeatService seatService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SeatController.class);

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ Seat Service ] !";
    }

    /**
     * Assign seats by seat request
     *
     * @param seatRequest seat request
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/seats")
    public HttpEntity create(@RequestBody Seat seatRequest, @RequestHeader HttpHeaders headers) {
        //logTime.springEnrtyStart.add(System.nanoTime());
        SeatController.LOGGER.info("[distributeSeat][Create seat][TravelDate: {},TrainNumber: {},SeatType: {}]",seatRequest.getTravelDate(),seatRequest.getTrainNumber(),seatRequest.getSeatType());
        Response re= seatService.distributeSeat(seatRequest, headers);
        //logTime.springEnrtyEnd.add(System.nanoTime());

        return ok(re);
    }

    /**
     * get left ticket of interval
     * query specific interval residual
     *
     * @param seatRequest seat request
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/seats/left_tickets")
    public HttpEntity getLeftTicketOfInterval(@RequestBody Seat seatRequest, @RequestHeader HttpHeaders headers) {
        // int
        //logTime.springEnrtyStart.add(System.nanoTime());
        SeatController.LOGGER.info("[getLeftTicketOfInterval][Get left ticket of interval][TravelDate: {},TrainNumber: {},SeatType: {}]",seatRequest.getTravelDate(),seatRequest.getTrainNumber(),seatRequest.getSeatType());
        Response re = seatService.getLeftTicketOfInterval(seatRequest, headers);
        //logTime.springEnrtyEnd.add(System.nanoTime());
        return ok(re);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/loggingTime")
    public HttpEntity queryLoggingTime(@RequestHeader HttpHeaders headers) {
        return ok(new Response(1,"success",logTime.getSpringTime()));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/clearTime")
    public HttpEntity clearTime(@RequestHeader HttpHeaders headers) {
        logTime.clear();
        return ok(new Response(1,"success",null));
    }
}
