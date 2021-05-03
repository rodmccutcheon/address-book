package com.rodmccutcheon.addressbook.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "contacts")
public class AddressBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "addressBooks")
    @JsonIgnoreProperties("addressBooks")
    private Set<Contact> contacts = new HashSet<>();

    public AddressBook(String name) {
        this.name = name;
    }

    public AddressBook(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        contact.getAddressBooks().add(this);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }
}
