package price.controller;

import edu.fudan.common.util.Response;
import edu.fudan.common.util.logTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import price.entity.PriceConfig;
import price.service.PriceService;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/priceservice")
public class PriceController {

    @Autowired
    PriceService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceController.class);

    @GetMapping(path = "/prices/welcome")
    public String home() {
        return "Welcome to [ Price Service ] !";
    }

    @GetMapping(value = "/prices/{routeId}/{trainType}")
    public HttpEntity query(@PathVariable String routeId, @PathVariable String trainType,
                            @RequestHeader HttpHeaders headers) {
        //logTime.springEnrtyStart.add(System.nanoTime());
        PriceController.LOGGER.info("[findByRouteIdAndTrainType][Query price][RouteId: {}, TrainType: {}]",routeId,trainType);
        Response re = service.findByRouteIdAndTrainType(routeId, trainType, headers);
        //logTime.springEnrtyEnd.add(System.nanoTime());
        return ok(re);
    }

    @PostMapping(value = "/prices/byRouteIdsAndTrainTypes")
    public HttpEntity query(@RequestBody List<String> ridsAndTts,
                            @RequestHeader HttpHeaders headers) {
        //logTime.springEnrtyStart.add(System.nanoTime());
        PriceController.LOGGER.info("[findByRouteIdAndTrainType][Query price][routeId and Train Type: {}]", ridsAndTts);

        Response re = service.findByRouteIdsAndTrainTypes(ridsAndTts, headers);
        //logTime.springEnrtyEnd.add(System.nanoTime());
        return ok(re );
    }

    @GetMapping(value = "/prices")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        PriceController.LOGGER.info("[findAllPriceConfig][Query all prices]");
        return ok(service.findAllPriceConfig(headers));
    }

    @PostMapping(value = "/prices")
    public HttpEntity<?> create(@RequestBody PriceConfig info,
                                @RequestHeader HttpHeaders headers) {
        PriceController.LOGGER.info("[createNewPriceConfig][Create price][RouteId: {}, TrainType: {}]",info.getRouteId(),info.getTrainType());
        return new ResponseEntity<>(service.createNewPriceConfig(info, headers), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/prices/{pricesId}")
    public HttpEntity delete(@PathVariable String pricesId, @RequestHeader HttpHeaders headers) {
        PriceController.LOGGER.info("[deletePriceConfig][Delete price][PriceConfigId: {}]",pricesId);
        return ok(service.deletePriceConfig(pricesId, headers));
    }

    @PutMapping(value = "/prices")
    public HttpEntity update(@RequestBody PriceConfig info, @RequestHeader HttpHeaders headers) {
        PriceController.LOGGER.info("[updatePriceConfig][Update price][PriceConfigId: {}]",info.getId());
        return ok(service.updatePriceConfig(info, headers));
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
