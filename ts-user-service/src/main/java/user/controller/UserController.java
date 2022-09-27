package user.controller;

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
import user.dto.UserDto;
import user.service.UserService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/userservice")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users/hello")
    public String testHello() {
        return "Hello";
    }

    @GetMapping("/users")
    public ResponseEntity<Response> getAllUser(@RequestHeader HttpHeaders headers) {
        UserController.LOGGER.info("[getAllUser][Get all user]");
        return ok(userService.getAllUsers(headers));
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity<Response> getUserByUserName(@PathVariable String userName, @RequestHeader HttpHeaders headers) {
        UserController.LOGGER.info("[getUserByUserName][Get user by user name][UserName: {}]",userName);
        return ok(userService.findByUserName(userName, headers));
    }
    @GetMapping("/users/id/{userId}")
    public ResponseEntity<Response> getUserByUserId(@PathVariable String userId, @RequestHeader HttpHeaders headers) {
        //logTime.springEnrtyStart.add(System.nanoTime());
        UserController.LOGGER.info("[getUserByUserId][Get user by user id][UserId: {}]",userId);
        Response re = userService.findByUserId(userId, headers);
        //logTime.springEnrtyEnd.add(System.nanoTime());
        return ok(re);
    }

    @PostMapping("/users/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserDto userDto, @RequestHeader HttpHeaders headers) {
        UserController.LOGGER.info("[registerUser][Register user][UserName: {}]",userDto.getUserName());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userDto, headers));
    }


    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Response> deleteUserById(@PathVariable String userId,
                                                   @RequestHeader HttpHeaders headers) {
        // only admin token can delete
        UserController.LOGGER.info("[deleteUserById][Delete user][UserId: {}]",userId);
        return ok(userService.deleteUser(userId, headers));
    }

    @PutMapping("/users")
    public ResponseEntity<Response> updateUser(@RequestBody UserDto user,
                                               @RequestHeader HttpHeaders headers) {
        UserController.LOGGER.info("[updateUser][Update user][UserId: {}]",user.getUserId());
        return ok(userService.updateUser(user, headers));
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
