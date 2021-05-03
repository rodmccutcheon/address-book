package com.rodmccutcheon.addressbook.repositories;

import com.rodmccutcheon.addressbook.entities.AddressBook;
import com.rodmccutcheon.addressbook.entities.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
    Set<Contact> findDistinctByAddressBooksIn(Iterable<AddressBook> addressBooks);
}
