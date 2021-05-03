package com.rodmccutcheon.addressbook.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodmccutcheon.addressbook.entities.AddressBook;
import com.rodmccutcheon.addressbook.services.AddressBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AddressBookController.class)
class AddressBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddressBookService addressBookService;

    @Test
    @DisplayName("Should successfully create an address book")
    void testCreateAddressBook() throws Exception {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        given(addressBookService.createAddressBook(addressBook)).willReturn(addressBook);

        mockMvc.perform(post(AddressBookController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(addressBook.getId().intValue())))
                .andExpect(jsonPath("$.name", is(addressBook.getName())));
    }

    @Test
    @DisplayName("Should successfully return an address book that exists")
    void testGetAddressBookThatExists() throws Exception {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        given(addressBookService.getAddressBook(addressBook.getId())).willReturn(addressBook);

        mockMvc.perform(get(AddressBookController.BASE_URL + "/" + addressBook.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(addressBook.getId().intValue())))
                .andExpect(jsonPath("$.name", is(addressBook.getName())));
    }

    @Test
    @DisplayName("Should return a 400 bad request status code and an error message when an address book does not exist")
    void testAttemptToGetAddressBookThatDoesNotExist() throws Exception {
        final long addressBookId = 7L;
        given(addressBookService.getAddressBook(addressBookId)).willThrow(new NoSuchElementException("No value present"));

        mockMvc.perform(get(AddressBookController.BASE_URL + "/" + addressBookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("No value present", result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Should successfully return all address books")
    void testGetAllAddressBooks() throws Exception {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        final var addressBook2 = new AddressBook(2L, "Bunk's address book");
        given(addressBookService.getAllAddressBooks()).willReturn(List.of(addressBook, addressBook2));

        mockMvc.perform(get(AddressBookController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(addressBook.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(addressBook.getName())))
                .andExpect(jsonPath("$[1].id", is(addressBook2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(addressBook2.getName())));
    }

    @Test
    @DisplayName("Should successfully update the given address book")
    void testUpdateAddressBookThatExists() throws Exception {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        given(addressBookService.updateAddressBook(addressBook.getId(), addressBook)).willReturn(addressBook);

        mockMvc.perform(put(AddressBookController.BASE_URL + "/" + addressBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(addressBook.getId().intValue())))
                .andExpect(jsonPath("$.name", is(addressBook.getName())));
    }

    @Test
    @DisplayName("Should return a 400 bad request status code and an error message when an address book does not exist")
    void testAttemptToUpdateAddressBookThatDoesNotExist() throws Exception {
        final var addressBook = new AddressBook(1L, "McNulty's address book");
        given(addressBookService.updateAddressBook(addressBook.getId(), addressBook)).willThrow(new NoSuchElementException("No value present"));

        mockMvc.perform(put(AddressBookController.BASE_URL + "/" + addressBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressBook)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("No value present", result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Should successfully delete the given address book")
    void testDeleteAddressBookThatExists() throws Exception {
        final var addressBook = new AddressBook(1L, "McNulty's address book");

        mockMvc.perform(delete(AddressBookController.BASE_URL + "/" + addressBook.getId()))
                .andExpect(status().isOk());

        verify(addressBookService, times(1)).deleteAddressBook(addressBook.getId());
    }

    @Test
    @DisplayName("Should return a 400 bad request status code and an error message when attempting to delete an address book that does not exist")
    void testAttemptToDeleteAddressBookThatDoesNotExist() throws Exception {
        final long addressBookId = 7L;
        doThrow(new NoSuchElementException("No value present")).when(addressBookService).deleteAddressBook(addressBookId);

        mockMvc.perform(delete(AddressBookController.BASE_URL + "/" + addressBookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("No value present", result.getResponse().getContentAsString()));
    }

}
