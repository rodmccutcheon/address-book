package com.rodmccutcheon.addressbook.data;

import com.rodmccutcheon.addressbook.entities.AddressBook;
import com.rodmccutcheon.addressbook.entities.Contact;
import com.rodmccutcheon.addressbook.entities.PhoneNumberType;
import com.rodmccutcheon.addressbook.services.AddressBookService;
import com.rodmccutcheon.addressbook.services.ContactService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Component
@Profile("sample-data")
@Log4j2
public class SampleData {

    private final AddressBookService addressBookService;
    private final ContactService contactService;

    public SampleData(AddressBookService addressBookService, ContactService contactService) {
        this.addressBookService = addressBookService;
        this.contactService = contactService;
    }

    @PostConstruct
    @Transactional
    public void seedDatabase() {
        log.info("Seeding database with test data...");

        final var addressBook = addressBookService.createAddressBook(new AddressBook("McNulty's address book"));
        var contact = new Contact("Stringer Bell");
        contact.addPhoneNumber(PhoneNumberType.MOBILE, "+61406747789");
        contact.addPhoneNumber(PhoneNumberType.WORK, "+61398420944");
        var contact2 = new Contact("Bubbles");
        contact2.addPhoneNumber(PhoneNumberType.MOBILE, "+61402985777");
        contactService.addContactToAddressBook(addressBook.getId(), contact);
        contact2 = contactService.addContactToAddressBook(addressBook.getId(), contact2);

        final var addressBook2 = addressBookService.createAddressBook(new AddressBook("Bunk's address book"));
        var contact3 = new Contact("Omar");
        contact3.addPhoneNumber(PhoneNumberType.OTHER, "+61406747700");
        contactService.addContactToAddressBook(addressBook2.getId(), contact3);
        contactService.addContactToAddressBook(addressBook2.getId(), contact2);

        log.info("Seeding database complete.");
    }
}
