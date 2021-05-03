package com.rodmccutcheon.addressbook.services;

import com.rodmccutcheon.addressbook.entities.AddressBook;
import com.rodmccutcheon.addressbook.entities.Contact;
import com.rodmccutcheon.addressbook.repositories.AddressBookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressBookServiceTest {

    @Mock
    private AddressBookRepository addressBookRepository;

    @InjectMocks
    private AddressBookService addressBookService;

    @Test
    @DisplayName("Should successfully create a new address book")
    void testCreateAddressBook() {
        final var addressBook = new AddressBook("McNulty's address book");

        addressBookService.createAddressBook(addressBook);

        verify(addressBookRepository, times(1)).save(addressBook);
    }

    @Test
    @DisplayName("Should successfully return the specified address book")
    void testGetAddressBook() {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        given(addressBookRepository.findById(addressBook.getId())).willReturn(Optional.of(addressBook));

        assertEquals(addressBook, addressBookService.getAddressBook(addressBook.getId()));
    }

    @Test
    @DisplayName("Should successfully return all address books")
    void testGetAllAddressBooks() {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        final var addressBook2 = new AddressBook(2L, "Bunk's address book");
        given(addressBookRepository.findAll()).willReturn(List.of(addressBook, addressBook2));

        assertEquals(List.of(addressBook, addressBook2), addressBookService.getAllAddressBooks());
    }

    @Test
    @DisplayName("Should successfully update the specified address book")
    void testUpdateAddressBook() {
        final var addressBookName = "McNulty's address book";
        final var addressBook = spy(new AddressBook(1L, addressBookName));
        given(addressBookRepository.findById(addressBook.getId())).willReturn(Optional.of(addressBook));

        addressBookService.updateAddressBook(addressBook.getId(), addressBook);

        verify(addressBook).setName(addressBookName);
        verify(addressBookRepository, times(1)).save(addressBook);
    }

    @Test
    @DisplayName("Should successfully delete an address book")
    void testDeleteAddressBook() {
        final var addressBook = new AddressBook(1L, "McNulty's address book");

        addressBookService.deleteAddressBook(addressBook.getId());

        verify(addressBookRepository, times(1)).deleteById(addressBook.getId());
    }

    @Test
    @DisplayName("Should successfully add a contact to the specified address book")
    void testAddContact() {
        final var addressBook = spy(new AddressBook(1L, "McNulty's address book"));
        final var contact = new Contact(1L, "Bubbles");
        given(addressBookRepository.findById(addressBook.getId())).willReturn(Optional.of(addressBook));

        addressBookService.addContact(addressBook.getId(), contact);

        verify(addressBook).addContact(contact);
        verify(addressBookRepository, times(1)).save(addressBook);
    }

    @Test
    @DisplayName("Should successfully remove a contact from the specified address book")
    void testRemoveContact() {
        final var addressBook = spy(new AddressBook(1L, "McNulty's address book"));
        final var contact = new Contact(1L, "Bubbles");
        addressBook.addContact(contact);

        addressBookService.removeContact(addressBook, contact);

        verify(addressBook).removeContact(contact);
        verify(addressBookRepository, times(1)).save(addressBook);
    }

}
