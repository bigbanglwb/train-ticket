package contacts.controller;

import contacts.entity.*;
import edu.fudan.common.util.Response;
import edu.fudan.common.util.logTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import contacts.service.ContactsService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("api/v1/contactservice")
public class ContactsController {


    @Autowired
    private ContactsService contactsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactsController.class);

    @GetMapping(path = "/contacts/welcome")
    public String home() {
        return "Welcome to [ Contacts Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/contacts")
    public HttpEntity getAllContacts(@RequestHeader HttpHeaders headers) {
        logTime.springEnrtyStart.add(System.nanoTime());
        ContactsController.LOGGER.info("[getAllContacts][Get All Contacts]");
        Response re = contactsService.getAllContacts(headers);
        logTime.springEnrtyEnd.add(System.nanoTime());
        return ok(re);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/contacts")
    public ResponseEntity<Response> createNewContacts(@RequestBody Contacts aci,
                                                      @RequestHeader HttpHeaders headers) {
        ContactsController.LOGGER.info("[createNewContacts][VerifyLogin Success]");
        return new ResponseEntity<>(contactsService.create(aci, headers), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/contacts/admin")
    public HttpEntity<?> createNewContactsAdmin(@RequestBody Contacts aci, @RequestHeader HttpHeaders headers) {
        aci.setId(UUID.randomUUID().toString());
        ContactsController.LOGGER.info("[createNewContactsAdmin][Create Contacts In Admin]");
        return new ResponseEntity<>(contactsService.createContacts(aci, headers), HttpStatus.CREATED);
    }


    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/contacts/{contactsId}")
    public HttpEntity deleteContacts(@PathVariable String contactsId, @RequestHeader HttpHeaders headers) {
        return ok(contactsService.delete(contactsId, headers));
    }


    @CrossOrigin(origins = "*")
    @PutMapping(path = "/contacts")
    public HttpEntity modifyContacts(@RequestBody Contacts info, @RequestHeader HttpHeaders headers) {
        ContactsController.LOGGER.info("[Contacts modifyContacts][Modify Contacts] ContactsId: {}", info.getId());
        return ok(contactsService.modify(info, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/contacts/account/{accountId}")
    public HttpEntity findContactsByAccountId(@PathVariable String accountId, @RequestHeader HttpHeaders headers) {
        ContactsController.LOGGER.info("[findContactsByAccountId][Find Contacts By Account Id][accountId: {}]", accountId);
        ContactsController.LOGGER.info("[ContactsService][VerifyLogin Success]");
        return ok(contactsService.findContactsByAccountId(accountId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/contacts/{id}")
    public HttpEntity getContactsByContactsId(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        ContactsController.LOGGER.info("[ContactsService][Contacts Id Print][id: {}]", id);
        ContactsController.LOGGER.info("[ContactsService][VerifyLogin Success]");
        return ok(contactsService.findContactsById(id, headers));
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
