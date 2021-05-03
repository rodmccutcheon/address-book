package com.rodmccutcheon.addressbook.repositories;

import com.rodmccutcheon.addressbook.entities.AddressBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressBookRepository extends CrudRepository<AddressBook, Long> {
}
