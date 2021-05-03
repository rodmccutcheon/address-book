package com.rodmccutcheon.addressbook.services;

import com.rodmccutcheon.addressbook.entities.AddressBook;
import com.rodmccutcheon.addressbook.entities.Contact;
import com.rodmccutcheon.addressbook.repositories.ContactRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private AddressBookService addressBookService;

    @InjectMocks
    private ContactService contactService;

    @Test
    @DisplayName("Should successfully add a new contact to address book")
    void testAddNewContactToAddressBook() {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        final var addressBook2 = new AddressBook(2L, "Bunk's address book");
        final var contact = new Contact("Bubbles");
        contact.setAddressBooks(new HashSet<>(Arrays.asList(addressBook, addressBook2)));
        given(contactRepository.save(contact)).willReturn(contact);

        contactService.addContactToAddressBook(addressBook.getId(), contact);

        verify(contactRepository, times(1)).save(contact);
        verify(addressBookService, times(1)).addContact(addressBook.getId(), contact);
    }

    @Test
    @DisplayName("Should successfully add an existing contact to a different address book")
    void testAddExistingContactToAddressBook() {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        final var addressBook2 = new AddressBook(2L, "Bunk's address book");
        final var contact = new Contact(1L, "Bubbles");
        contact.setAddressBooks(new HashSet<>(Arrays.asList(addressBook, addressBook2)));
        given(contactRepository.findById(contact.getId())).willReturn(Optional.of(contact));

        contactService.addContactToAddressBook(addressBook.getId(), contact);

        verify(contactRepository, times(0)).save(contact);
        verify(addressBookService, times(1)).addContact(addressBook.getId(), contact);
    }

    @Test
    @DisplayName("Should remove the contact from the address book but not delete it if it belongs to other address books")
    void testRemoveContactFromAddressBook() {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        final var addressBook2 = new AddressBook(2L, "Bunk's address book");
        final var contact = new Contact(1L, "Bubbles");
        contact.setAddressBooks(new HashSet<>(Arrays.asList(addressBook, addressBook2)));
        given(addressBookService.getAddressBook(addressBook.getId())).willReturn(addressBook);
        given(contactRepository.findById(contact.getId())).willReturn(Optional.of(contact));

        contactService.deleteContactFromAddressBook(addressBook.getId(), contact.getId());

        verify(addressBookService, times(1)).removeContact(addressBook, contact);
        verify(contactRepository, times(0)).delete(contact);
    }

    @Test
    @DisplayName("Should delete the contact if it doesn't belong to any other address books")
    void testDeleteContactFromAddressBookAndDeleteOrphanContact() {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        final var contact = new Contact(1L, "Bubbles");
        contact.setAddressBooks(new HashSet<>(Collections.singletonList(addressBook)));
        given(addressBookService.getAddressBook(addressBook.getId())).willReturn(addressBook);
        given(contactRepository.findById(contact.getId())).willReturn(Optional.of(contact));

        contactService.deleteContactFromAddressBook(addressBook.getId(), contact.getId());

        verify(addressBookService, times(1)).removeContact(addressBook, contact);
        verify(contactRepository, times(1)).delete(contact);
    }

    @Test
    @DisplayName("Should retrieve the set of unique contacts for the given address books")
    void testGetUniqueContacts() {
        contactService.getUniqueContacts();

        verify(contactRepository, times(1)).findDistinctByAddressBooksIn(anyIterable());
    }
}
