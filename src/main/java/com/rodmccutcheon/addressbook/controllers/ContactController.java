package com.rodmccutcheon.addressbook.controllers;

import com.rodmccutcheon.addressbook.entities.Contact;
import com.rodmccutcheon.addressbook.services.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.rodmccutcheon.addressbook.controllers.AddressBookController.BASE_URL;

@RestController
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(BASE_URL + "/unique-contacts")
    public ResponseEntity<Set<Contact>> getUniqueContactsAcrossAllAddressBooks() {
        return ResponseEntity.ok(contactService.getUniqueContacts());
    }

    @PostMapping(BASE_URL + "/{addressBookId}/contact")
    public ResponseEntity<Contact> addContactToAddressBook(@PathVariable long addressBookId,
                                                           @RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.addContactToAddressBook(addressBookId, contact));
    }

    @DeleteMapping(BASE_URL + "/{addressBookId}/contact/{contactId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteContactFromAddressBook(@PathVariable long addressBookId, @PathVariable long contactId) {
        contactService.deleteContactFromAddressBook(addressBookId, contactId);
    }

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<String> handleException(Exception exception, HttpServletResponse resp) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
