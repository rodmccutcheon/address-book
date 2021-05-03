package com.rodmccutcheon.addressbook.controllers;

import com.rodmccutcheon.addressbook.entities.AddressBook;
import com.rodmccutcheon.addressbook.services.AddressBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.util.NoSuchElementException;

import static com.rodmccutcheon.addressbook.controllers.AddressBookController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class AddressBookController {

    public static final String BASE_URL = "/api/v1/address-book";

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @PostMapping
    public ResponseEntity<AddressBook> createAddressBook(@RequestBody AddressBook addressBook) {
        return ResponseEntity.ok(addressBookService.createAddressBook(addressBook));
    }

    @GetMapping
    public ResponseEntity<Iterable<AddressBook>> getAllAddressBooks() {
        return ResponseEntity.ok(addressBookService.getAllAddressBooks());
    }

    @GetMapping("/{addressBookId}")
    public ResponseEntity<AddressBook> getAddressBook(@PathVariable long addressBookId) {
        return ResponseEntity.ok(addressBookService.getAddressBook(addressBookId));
    }

    @PutMapping("/{addressBookId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AddressBook> updateAddressBook(@PathVariable long addressBookId,
                                                         @RequestBody AddressBook changedAddressBook) {
        return ResponseEntity.ok(addressBookService.updateAddressBook(addressBookId, changedAddressBook));
    }

    @DeleteMapping("/{addressBookId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAddressBook(@PathVariable long addressBookId) {
        addressBookService.deleteAddressBook(addressBookId);
    }

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<String> handleException(Exception exception, HttpServletResponse resp) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

}
