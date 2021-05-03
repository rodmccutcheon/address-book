package com.rodmccutcheon.addressbook.services;

import com.rodmccutcheon.addressbook.entities.AddressBook;
import com.rodmccutcheon.addressbook.entities.Contact;
import com.rodmccutcheon.addressbook.repositories.AddressBookRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;

    public AddressBookService(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    public AddressBook createAddressBook(AddressBook addressBook) {
        return addressBookRepository.save(addressBook);
    }

    public Iterable<AddressBook> getAllAddressBooks() {
        return addressBookRepository.findAll();
    }

    public AddressBook getAddressBook(long addressBookId) {
        return addressBookRepository.findById(addressBookId).orElseThrow();
    }

    public AddressBook updateAddressBook(long addressBookId, AddressBook changedAddressBook) {
        final var addressBook = addressBookRepository.findById(addressBookId).orElseThrow();
        addressBook.setName(changedAddressBook.getName());
        return addressBookRepository.save(addressBook);
    }

    public void deleteAddressBook(long addressBookId) {
        addressBookRepository.deleteById(addressBookId);
    }

    public void addContact(long addressBookId, Contact contact) {
        final var addressBook = getAddressBook(addressBookId);
        addressBook.addContact(contact);
        addressBookRepository.save(addressBook);
    }

    public void removeContact(AddressBook addressBook, Contact contact) {
        addressBook.removeContact(contact);
        addressBookRepository.save(addressBook);
    }
}
