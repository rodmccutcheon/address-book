package com.rodmccutcheon.addressbook.services;

import com.rodmccutcheon.addressbook.entities.Contact;
import com.rodmccutcheon.addressbook.repositories.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final AddressBookService addressBookService;

    public ContactService(ContactRepository contactRepository, AddressBookService addressBookService) {
        this.contactRepository = contactRepository;
        this.addressBookService = addressBookService;
    }

    public Contact addContactToAddressBook(long addressBookId, Contact contact) {
        Contact savedContact;
        if (contact.getId() != null) {
            savedContact = contactRepository.findById(contact.getId()).orElseThrow();
        } else {
            savedContact = contactRepository.save(contact);
        }
        addressBookService.addContact(addressBookId, savedContact);
        return savedContact;
    }

    public void deleteContactFromAddressBook(long addressBookId, long contactId) {
        final var addressBook = addressBookService.getAddressBook(addressBookId);
        final var contact = contactRepository.findById(contactId).orElseThrow();
        contact.getAddressBooks().remove(addressBook);
        addressBookService.removeContact(addressBook, contact);
        if (contact.getAddressBooks().isEmpty()) {
            contactRepository.delete(contact);
        }
    }

    public Set<Contact> getUniqueContacts() {
        return contactRepository.findDistinctByAddressBooksIn(addressBookService.getAllAddressBooks());
    }
}
