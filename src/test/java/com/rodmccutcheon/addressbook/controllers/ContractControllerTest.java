package com.rodmccutcheon.addressbook.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodmccutcheon.addressbook.entities.AddressBook;
import com.rodmccutcheon.addressbook.entities.Contact;
import com.rodmccutcheon.addressbook.services.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.Set;

import static com.rodmccutcheon.addressbook.controllers.AddressBookController.BASE_URL;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ContactController.class)
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactService contactService;

    @Test
    @DisplayName("Should successfully add a contact to an address book that exists")
    void testAddContactToAddressBookThatExists() throws Exception {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        final var contact = new Contact("Bubbles");

        mockMvc.perform(post(AddressBookController.BASE_URL + "/" + addressBook.getId() + "/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk());

        verify(contactService, times(1)).addContactToAddressBook(addressBook.getId(), contact);
    }

    @Test
    @DisplayName("Should return a 400 bad request status code and an error message when the address book does not exist")
    void testAttemptToAddContactToAddressBookThatDoesntExist() throws Exception {
        final var addressBookId = 7L;
        final var contact = new Contact("Bubbles");
        given(contactService.addContactToAddressBook(addressBookId, contact)).willThrow(new NoSuchElementException("No value present"));

        mockMvc.perform(post(AddressBookController.BASE_URL + "/" + addressBookId + "/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("No value present", result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Should successfully delete a contact from an address book that exists")
    void testDeleteContactFromAddressBookThatExists() throws Exception {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        final var contact = new Contact(1L, "Bubbles");

        mockMvc.perform(delete(AddressBookController.BASE_URL + "/" + addressBook.getId() + "/contact/" + contact.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk());

        verify(contactService, times(1)).deleteContactFromAddressBook(addressBook.getId(), contact.getId());
    }

    @Test
    @DisplayName("Should return a 400 bad request status code and an error message when the address book does not exist")
    void testAttemptToDeleteContactFromAddressBookThatDoesntExist() throws Exception {
        final var addressBookId = 7L;
        final var contactId = 3L;
        doThrow(new NoSuchElementException("No value present")).when(contactService).deleteContactFromAddressBook(addressBookId, contactId);

        mockMvc.perform(delete(AddressBookController.BASE_URL + "/" + addressBookId + "/contact/" + contactId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("No value present", result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Should return a set of unique contacts across all address books")
    void testGetUniqueContactsAcrossAllAddressBooks() throws Exception {
        final var contact = new Contact(1L, "Bubbles");
        given(contactService.getUniqueContacts()).willReturn(Set.of(contact));

        mockMvc.perform(get(BASE_URL + "/unique-contacts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(contact.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(contact.getName())));
    }
}
